package top.cookizi.bot.service.download.twitter;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.AccessLevel;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TwitterFeatures {
    boolean unified_cards_follow_card_query_enabled = false;
    boolean unified_cards_ad_metadata_container_dynamic_card_content_query_enabled = false;
    boolean dont_mention_me_view_api_enabled = true;
    boolean responsive_web_uc_gql_enabled = true;
    boolean vibe_api_enabled = true;
    boolean responsive_web_edit_tweet_api_enabled = true;
    boolean standardized_nudges_misinfo = true;
    boolean tweet_with_visibility_results_prefer_gql_limited_actions_policy_enabled = false;
    boolean interactive_text_enabled = true;
    boolean responsive_web_text_conversations_enabled = false;
    boolean responsive_web_enhance_cards_enabled = true;
    boolean graphql_is_translatable_rweb_tweet_is_translatable_enabled = false;
    boolean responsive_web_graphql_timeline_navigation_enabled = false;
    boolean tweetypie_unmention_optimization_enabled = false;
    boolean verified_phone_label_enabled = false;
    boolean responsive_web_twitter_blue_verified_badge_is_enabled = false;


    @SneakyThrows
    public static String toJson() {

        var filePath = featurePath();
        var gson = new Gson();
        JsonObject json;
        try (FileReader fileReader = new FileReader(filePath)) {
            json = gson.fromJson(fileReader, JsonObject.class);
            fileReader.close();
            return json.toString();
        } catch (IOException e) {

            var object = creatDefaultFeatures();
            return object.toString();
        }
    }


    @SneakyThrows
    public static JsonObject creatDefaultFeatures() {
        var filePath = featurePath();
        var gson = new Gson();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            var tree = gson.toJsonTree(new TwitterFeatures());

            writer.write(tree.toString());
            writer.flush();
            writer.close();
            return tree.getAsJsonObject();
        }
    }

    public static String featurePath() {
        var dirPath = System.getProperty("user.dir");
        return dirPath + "\\twitterFeatures.json";
    }
}
