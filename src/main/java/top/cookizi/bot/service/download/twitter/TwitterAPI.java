package top.cookizi.bot.service.download.twitter;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import top.cookizi.bot.config.AppConfig;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class TwitterAPI {
    String AUTH = "Bearer AAAAAAAAAAAAAAAAAAAAANRILgAAAAAAnNwIzUejRCOuH5E6I8xnZz4puTs%3D1Zv7ttfk8LF81IUq16cHjhLTvJu4FA33AGWWjCpTnA";
    String UA = "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko";
    Map<String, String> params = new HashMap<String, String>() {{
        put("include_profile_interstitial_type", "1");
        put("include_blocking", "1");
        put("include_blocked_by", "1");
        put("include_followed_by", "1");
        put("include_want_retweets", "1");
        put("include_mute_edge", "1");
        put("include_can_dm", "1");
        put("include_can_media_tag", "1");
        put("skip_status", "1");
        put("cards_platform", "Web-12");
        put("include_cards", "1");
        put("include_composer_source", "true");
        put("include_ext_alt_text", "true");
        put("include_reply_count", "1");
        put("tweet_mode", "extended");
        put("include_entities", "true");
        put("include_user_entities", "true");
        put("include_ext_media_color", "true");
        put("include_ext_media_availability", "true");
        put("send_error_codes", "true");
        put("simple_quoted_tweet", "true");
//        put(#  "count", "20");
//        put(#"cursor", None);
        put("count", "100");
        put("ext", "mediaStats%2ChighlightedLabel%2CcameraMoment");
        put("include_quote_count", "true");

    }};
    Map<String, String> cookies;
    Map<String, String> heads = new HashMap<String, String>() {{
        put("authorization", AUTH);
        put("x-twitter-client-language", "en");
        put("x-twitter-active-user", "yes");
        put("Origin", "https,//twitter.com");
    }};
    @Autowired
    private AppConfig appConfig;


    private String guest_token() throws IOException {

        Connection.Response response = Jsoup.connect("https://api.twitter.com/1.1/guest/activate.json")
                .proxy(appConfig.getProxyHost(), appConfig.getProxyPort()).headers(heads)
                .ignoreContentType(true).method(Connection.Method.POST)

                .timeout(Integer.MAX_VALUE)
                .execute();
        JsonObject jsonObject = new Gson().fromJson(response.body(), JsonObject.class);
        return jsonObject.get("guest_token").getAsString();
    }

    private String call(String urlApi) throws IOException {

        String csrf = DigestUtils.md5DigestAsHex(String.valueOf(System.currentTimeMillis()).getBytes(StandardCharsets.UTF_8));
        heads.put("x-csrf-token", csrf);
        cookies = new HashMap<>();
        cookies.put("ct0", csrf);
        String guest_token = guest_token();
        heads.put("x-guest-token", guest_token);
        cookies.put("gt", guest_token);


        urlApi = "https://api.twitter.com" + urlApi;
        Connection.Response response = Jsoup.connect(urlApi).ignoreContentType(true).timeout(Integer.MAX_VALUE)
                .headers(heads).cookies(cookies).proxy(appConfig.getProxyHost(), appConfig.getProxyPort())
                .execute();
        return response.body();

    }

    public List<String> tweet(String url) throws IOException {
        Matcher matcher = Pattern.compile(".*/status/([0-9]+).*").matcher(url);
        List<String> result = new ArrayList<>();
        if (!matcher.find()) {
            return result;
        }
        String id = matcher.group(1);
        String urlApi = String.format("/2/timeline/conversation/%s.json?tweet_mode=extended", id);
        String resp = this.call(urlApi);
        JsonObject jsonObject = new Gson().fromJson(resp, JsonObject.class);
        //obj.globalObjects.tweets.${id}.entities.media[].media_url
        jsonObject.get("globalObjects").getAsJsonObject()
                .get("tweets").getAsJsonObject()
                .get(id).getAsJsonObject()
                .get("entities").getAsJsonObject()
                .get("media").getAsJsonArray()
                .forEach(x -> result.add(x.getAsJsonObject().get("media_url").getAsString()));
        return result;
    }
}
