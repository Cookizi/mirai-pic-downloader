package top.cookizi.bot.service.download;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.commons.codec.digest.Md5Crypt;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.cookizi.bot.config.AppConfig;
import top.cookizi.bot.modle.msg.Msg;
import top.cookizi.bot.modle.resp.MsgResp;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TwitterDownloadService extends AbstractDownloadService {
    @Autowired
    private AppConfig appConfig;

    @Override
    public List<Msg> handleMsg(MsgResp msg) {
        return null;
    }

    @Override
    public String handleFileName(Msg msg) {
        return null;
    }

    @Override
    public String handleImageUrl(Msg msg) {
        return null;
    }

    @Override
    public boolean isTypeMatch(MsgResp msg) {
        return false;
    }

    class TwitterAPI {
        String AUTH = "Bearer AAAAAAAAAAAAAAAAAAAAANRILgAAAAAAnNwIzUejRCOuH5E6I8xnZz4puTs%3D1Zv7ttfk8LF81IUq16cHjhLTvJu4FA33AGWWjCpTnA";
        String UA = "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko";
        Map<String, String> params = new HashMap<>();

        public TwitterAPI() throws IOException {
            String csrf = Md5Crypt.md5Crypt(String.valueOf(System.currentTimeMillis()).getBytes(StandardCharsets.UTF_8));
            Map<String, String> heads = new HashMap<>();
            heads.put("authorization", AUTH);
            heads.put("x-twitter-client-language", "en");
            heads.put("x-twitter-active-user", "yes");
            heads.put("x-csrf-token", csrf);
            heads.put("Origin", "https,//twitter.com");

            Map<String, String> cookies = new HashMap<>();
            cookies.put("ct0", csrf);
            String guest_token = guest_token(heads);
            heads.put("x-guest-token", guest_token);
            cookies.put("gt", guest_token);

            params.put("include_profile_interstitial_type", "1");
            params.put("include_blocking", "1");
            params.put("include_blocked_by", "1");
            params.put("include_followed_by", "1");
            params.put("include_want_retweets", "1");
            params.put("include_mute_edge", "1");
            params.put("include_can_dm", "1");
            params.put("include_can_media_tag", "1");
            params.put("skip_status", "1");
            params.put("cards_platform", "Web-12");
            params.put("include_cards", "1");
            params.put("include_composer_source", "true");
            params.put("include_ext_alt_text", "true");
            params.put("include_reply_count", "1");
            params.put("tweet_mode", "extended");
            params.put("include_entities", "true");
            params.put("include_user_entities", "true");
            params.put("include_ext_media_color", "true");
            params.put("include_ext_media_availability", "true");
            params.put("send_error_codes", "true");
            params.put("simple_quoted_tweet", "true");
//        params.put(#  "count", "20");
            params.put("count", "100");
//        params.put(#"cursor", None);
            params.put("ext", "mediaStats%2ChighlightedLabel%2CcameraMoment");
            params.put("include_quote_count", "true");

        }

        private String guest_token(Map<String, String> heads) throws IOException {

            Connection.Response response = Jsoup.connect("https://api.twitter.com/1.1/guest/activate.json")
                    .proxy(appConfig.getProxyHost(), appConfig.getProxyPort()).headers(heads)
                    .ignoreContentType(true).method(Connection.Method.POST)
                    .timeout(Integer.MAX_VALUE)
                    .execute();
            JsonObject jsonObject = new Gson().fromJson(response.body(), JsonObject.class);
            return jsonObject.get("guest_token").getAsString();
        }

    }
}

