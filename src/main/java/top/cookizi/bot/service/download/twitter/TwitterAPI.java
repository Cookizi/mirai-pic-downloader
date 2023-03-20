package top.cookizi.bot.service.download.twitter;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.jayway.jsonpath.JsonPath;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import okhttp3.*;
import okio.ByteString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import top.cookizi.bot.common.utils.StringUtils;
import top.cookizi.bot.config.AppConfig;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TwitterAPI {
    String UA = "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko";

    @Autowired
    private AppConfig appConfig;
    @Autowired
    @Qualifier("proxyClient")
    private OkHttpClient client;
    @Autowired
    @Qualifier("miraiThreadPool")
    private ThreadPoolExecutor executor;


    String TOKEN_REGEX = "[\"'](AAA[a-zA-Z0-9%-]+%[a-zA-Z0-9%-]+)[\"']";
    String QUERY_ID_REGEX = "queryId:\"([a-zA-Z0-9\\-_]+)\",operationName:\"TweetDetail\"";
    String TWITTER_ID_REGEX = ".*/status/([0-9]+).*";

    @SneakyThrows
    public List<String> tweet(String url) {

        String id;


        String body = client.newCall(new Request.Builder().url(url).build())
                .execute().body()
                .string();

        String js = getJsContent(body);
        String bearerToken = extractContent(js, TOKEN_REGEX);
        log.debug("bearerToken={}", bearerToken);
        String queryId = fetchQueryId(body);
        log.debug("queryId={}", queryId);
        id = extractContent(url, TWITTER_ID_REGEX);
        log.debug("twitter id={}", id);

        return getMediaUrls(bearerToken, queryId, id);

    }

    private String fetchQueryId(String body) throws IOException {
        var reg = "\"endpoints\\.Conversation\":\"(.*?)\"";
        Matcher matcher = Pattern.compile(reg).matcher(body);
        matcher.find();
        String ConversationJsHash = matcher.group(1);
        String url = String.format("https://abs.twimg.com/responsive-web/client-web-legacy/endpoints.Conversation.%sa.js", ConversationJsHash);
        String js = client.newCall(new Request.Builder().url(url).build())
                .execute().body()
                .string();
        return extractContent(js, QUERY_ID_REGEX);
    }

    private List<String> getMediaUrls(String bearerToken, String queryId, String id) throws IOException {
        String dataUrl = "https://twitter.com/i/api/graphql/" + queryId + "/TweetDetail";
        HttpUrl httpUrl = HttpUrl.get(dataUrl).newBuilder().addQueryParameter("variables", TwitterVariables.toJson(id))
                .addQueryParameter("features", TwitterFeatures.toJson()).build();
        Request.Builder builder = new Request.Builder().url(httpUrl);
        builder.addHeader("Authorization", "Bearer " + bearerToken);
        appConfig.getHeaders().forEach(builder::addHeader);
        appConfig.getCookies().forEach((k, v) -> builder.addHeader("cookie", String.format("%s=%s", k, v)));

        Request request = builder.build();
        ByteString byteString = client.newCall(request).execute().body().byteString();
        String data = new String(byteString.toByteArray());
        JSONArray array;
        try {
            String pathPrefix = "$.data.threaded_conversation_with_injections_v2.instructions[0].entries[0].content.itemContent.tweet_results.result.";

            String typename = JsonPath.read(data, pathPrefix + "__typename");
            //2022/11/29 推特改了响应体结构
            if ("TweetWithVisibilityResults".equals(typename)) {
                pathPrefix = pathPrefix + "tweet.";
            }

            String jsonPath = pathPrefix + "legacy.extended_entities.media[*]";

            array = JsonPath.read(data, jsonPath);

            follow(request, pathPrefix, data);
        } catch (Exception e) {
            log.warn("解析json失败，data={}", new String(byteString.toByteArray()), e);
            throw new RuntimeException(String.format("解析json失败，data=%s", new String(byteString.toByteArray())), e);

        }
        List<TwitterMedia> twitterMediaList = new Gson().fromJson(array.toJSONString(), new TypeToken<List<TwitterMedia>>() {
        }.getType());

        List<String> urlList =
                Optional.ofNullable(twitterMediaList).stream()
                        .flatMap(Collection::stream)
                        .map(TwitterMedia::toDownloadUrl)
                        .filter(StringUtils::isNotBlank)
                        .collect(Collectors.toList());
        log.info("获取Twitter图片结束，获得{}张图片/视频", urlList.size());

        return urlList;

    }

    /**
     * 异步加关注，失败也无所谓
     */
    private void follow(Request request, String pathPrefix, String data) {
        executor.execute(() -> {
            String userIdPath = pathPrefix + "core.user_results.result.rest_id";
            String isFollowingPath = pathPrefix + "core.user_results.result.legacy.following";
            String userId = null;
            try {
                Boolean isFollowing = JsonPath.read(data, isFollowingPath);
                if (isFollowing) {
                    return;
                }
                userId = JsonPath.read(data, userIdPath);

                Headers headers = request.headers();
                RequestBody b = getFollowRequest(userId);

                String followUrl = "https://twitter.com/i/api/1.1/friendships/create.json";

                Request followRequest = request.newBuilder().url(followUrl).headers(headers).post(b).build();
                client.newCall(followRequest).execute();
            } catch (IOException e) {
                log.warn("failed to follow user id={}", userId);
            }
        });
    }

    private RequestBody getFollowRequest(String userId) {
        return new FormBody.Builder()
                .add("include_profile_interstitial_type", "1")
                .add("include_blocking", "1")
                .add("include_blocked_by", "1")
                .add("include_followed_by", "1")
                .add("include_want_retweets", "1")
                .add("include_mute_edge", "1")
                .add("include_can_dm", "1")
                .add("include_can_media_tag", "1")
                .add("include_ext_has_nft_avatar", "1")
                .add("include_ext_is_blue_verified", "1")
                .add("skip_status", "1")
                .add("user_id", userId)
                .build();
    }


    private String extractContent(String content, String regex) {
        Pattern compile = Pattern.compile(regex);
        Matcher matcher = compile.matcher(content);
        matcher.find();
        return matcher.group(1);
    }

    private String getJsContent(String body) throws IOException {
        Pattern compile = Pattern.compile(".*(https://abs.twimg.com/responsive-web/client-web-legacy/main.[a-f0-9]+.js).*");
        Matcher matcher = compile.matcher(body);
        matcher.find();
        String jsUrl = matcher.group(1);
        log.debug("开始获取js内容，url={}", jsUrl);

        return client.newCall(new Request.Builder().url(jsUrl).build())
                .execute().body()
                .string();
    }
}
