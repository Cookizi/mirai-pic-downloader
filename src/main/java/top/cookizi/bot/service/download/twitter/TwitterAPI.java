package top.cookizi.bot.service.download.twitter;

import com.google.common.collect.Lists;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.jayway.jsonpath.JsonPath;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okio.ByteString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import top.cookizi.bot.common.utils.StringUtils;
import top.cookizi.bot.config.AppConfig;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
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


    String TOKEN_REGEX = "[\"'](AAA[a-zA-Z0-9%-]+%[a-zA-Z0-9%-]+)[\"']";
    String QUERY_ID_REGEX = "queryId:\"([a-zA-Z0-9\\-_]+)\",operationName:\"TweetDetail\"";
    String TWITTER_ID_REGEX = ".*/status/([0-9]+).*";

    @SneakyThrows
    public List<String> tweet(String url) {

        String id;

        String js = getJsContent(url);
        String bearerToken = extractContent(js, TOKEN_REGEX);
        log.debug("bearerToken={}", bearerToken);
        String queryId = extractContent(js, QUERY_ID_REGEX);
        log.debug("queryId={}", queryId);
        id = extractContent(url, TWITTER_ID_REGEX);
        log.debug("twitter id={}", id);

        return getMediaUrls(bearerToken, queryId, id);

    }

    private List<String> getMediaUrls(String bearerToken, String queryId, String id) throws IOException {
        String dataUrl = "https://twitter.com/i/api/graphql/" + queryId + "/TweetDetail";
        HttpUrl httpUrl = HttpUrl.get(dataUrl).newBuilder().addQueryParameter("variables", TwitterVariables.toJson(id))
                .addQueryParameter("features", TwitterFeatures.toJson()).build();
        Request.Builder builder = new Request.Builder().url(httpUrl);
        builder.addHeader("Authorization", "Bearer " + bearerToken);
        appConfig.getHeaders().forEach(builder::addHeader);
        appConfig.getCookies().forEach((k, v) -> builder.addHeader("cookie", String.format("%s=%s", k, v)));

        ByteString byteString = client.newCall(builder.build()).execute().body().byteString();
//        byte[] bytes = client.newCall(builder.build()).execute().body().bytes();

        String jsonPath = "$.data.threaded_conversation_with_injections_v2.instructions[0].entries[0].content.itemContent.tweet_results.result.legacy.extended_entities.media[*]";
        JSONArray array = null;
        try {
            array = JsonPath.read(new ByteArrayInputStream(byteString.toByteArray()), jsonPath);
        } catch (Exception e) {
            log.warn("解析json失败，data={}", new String(byteString.toByteArray()));
            return Lists.newArrayList();
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


    private String extractContent(String content, String regex) {
        Pattern compile = Pattern.compile(regex);
        Matcher matcher = compile.matcher(content);
        matcher.find();
        return matcher.group(1);
    }

    private String getJsContent(String url) throws IOException {
        String body = client.newCall(new Request.Builder().url(url).build())
                .execute().body()
                .string();

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
