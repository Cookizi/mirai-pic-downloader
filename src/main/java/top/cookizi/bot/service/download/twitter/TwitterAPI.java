package top.cookizi.bot.service.download.twitter;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.cookizi.bot.common.utils.StringUtils;
import top.cookizi.bot.config.AppConfig;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TwitterAPI {
    String UA = "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko";

    @Autowired
    private AppConfig appConfig;

    String TOKEN_REGEX = "([a-zA-Z0-9%]{104})";
    String QUERY_ID_REGEX = "queryId:\"([a-zA-Z0-9\\-_]+)\",operationName:\"TweetDetail\"";
    String TWITTER_ID_REGEX = ".*/status/([0-9]+).*";

    public List<String> tweet(String url) {

        String id;
        try {
            String js = getJsContent(url);
            String bearerToken = extractContent(js, TOKEN_REGEX);
            log.debug("bearerToken={}", bearerToken);
            String queryId = extractContent(js, QUERY_ID_REGEX);
            log.debug("queryId={}", queryId);
            id = extractContent(url, TWITTER_ID_REGEX);
            log.debug("twitter id={}", id);

            return getMediaUrls(bearerToken, queryId, id);
        } catch (Exception e) {
            log.warn("下载Twitter图失败，地址={}", url, e);
        }
        return Lists.newArrayList();
    }

    private List<String> getMediaUrls(String bearerToken, String queryId, String id) throws IOException {
        String var = TwitterVariables.toJson(id);
        String dataUrl = "https://twitter.com/i/api/graphql/" + queryId + "/TweetDetail?variables=" + var;
        String data = getConnection().url(dataUrl)
                .header("Authorization", "Bearer " + bearerToken)
                .headers(appConfig.getHeaders())
                .cookies(appConfig.getCookies())
                .execute().body();

        String jsonPath = "$.data.threaded_conversation_with_injections.instructions[0].entries[0].content.itemContent.tweet_results.result.legacy.extended_entities.media[*]";
        JSONArray array = null;
        try {
            array = JsonPath.read(data, jsonPath);
        } catch (Exception e) {
            log.warn("解析json失败，data={}", data);
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
        String body = getConnection().url(url).execute().body();
        Pattern compile = Pattern.compile(".*(https://abs.twimg.com/responsive-web/client-web-legacy/main.[a-f0-9]+.js).*");
        Matcher matcher = compile.matcher(body);
        matcher.find();
        String jsUrl = matcher.group(1);
        log.debug("开始获取js内容，url={}", jsUrl);

        return getConnection().url(jsUrl).execute().body();
    }

    private Map<String, String> getCookies() {
        HashMap<String, String> c = Maps.newHashMap();
        c.put("guest_id_marketing", "v1%3A164473636445035023");
        c.put("guest_id_ads", "v1%3A164473636445035023");
        c.put("personalization_id", "\"v1_017dgBNUhXNnO5CrgUEdzA==\"");
        c.put("guest_id", "v1%3A164473636445035023");
        c.put("ct0", "2e4b9d5bd184862aef0ec0a34996ae56a8fd0ec26128e53b3e286ee812b416e19c2f7c7d8b360fc8d527955de152909267468aa640fa2f0a3885c60dc0efaded43e927ab9f2b18b55e42c5708fbab39a");
        c.put("_ga", "GA1.2.444115999.1644736368");
        c.put("kdt", "2WwSX4T3SyYPGEAMMrcQYpkTPwYH8QGepP49m1ep");
        c.put("twid", "u%3D2832765391");
        c.put("auth_token", "c44eb90fc140db817bf33793889019b02bd660e0");
        c.put("external_referer", "8e8t2xd8A2w%3D|0|4abf247TNXg4Rylyqt4v49u2LWyy1%2FaFyfd602NkflM%3D");
        c.put("_gid", "GA1.2.1164167540.1647353893");
        c.put("lang", "zh-cn");
        return c;
    }

    private Map<String, String> getHeaders() {
        Map<String, String> h = Maps.newHashMap();
        h.put("x-twitter-auth-type", "OAuth2Session");
        h.put("x-twitter-client-language", "zh-cn");
        h.put("x-twitter-active-user", "yes");
        h.put("x-csrf-token", "2e4b9d5bd184862aef0ec0a34996ae56a8fd0ec26128e53b3e286ee812b416e19c2f7c7d8b360fc8d527955de152909267468aa640fa2f0a3885c60dc0efaded43e927ab9f2b18b55e42c5708fbab39a");
        h.put("Sec-Fetch-Dest", "empty");
        h.put("Sec-Fetch-Mode", "cors");
        h.put("Sec-Fetch-Site", "same-origin");
        return h;
    }


    public Connection getConnection() {
        return Jsoup.newSession().
                ignoreHttpErrors(true)
                .ignoreContentType(true)
                .proxy(appConfig.getProxyHost(), appConfig.getProxyPort())
                .timeout(60 * 1000);
    }
}
