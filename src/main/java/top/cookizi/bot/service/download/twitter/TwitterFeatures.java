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
    boolean responsive_web_twitter_blue_verified_badge_is_enabled=true;
    boolean responsive_web_graphql_exclude_directive_enabled=true;
    boolean verified_phone_label_enabled=false;
    boolean responsive_web_graphql_timeline_navigation_enabled=true;
    boolean responsive_web_graphql_skip_user_profile_image_extensions_enabled=false;
    boolean tweetypie_unmention_optimization_enabled=true;
    boolean vibe_api_enabled=true;
    boolean responsive_web_edit_tweet_api_enabled=true;
    boolean graphql_is_translatable_rweb_tweet_is_translatable_enabled=true;
    boolean view_counts_everywhere_api_enabled=true;
    boolean longform_notetweets_consumption_enabled=true;
    boolean tweet_awards_web_tipping_enabled=false;
    boolean freedom_of_speech_not_reach_fetch_enabled=false;
    boolean standardized_nudges_misinfo=true;
    boolean tweet_with_visibility_results_prefer_gql_limited_actions_policy_enabled=false;
    boolean interactive_text_enabled=true;
    boolean responsive_web_text_conversations_enabled=false;
    boolean longform_notetweets_richtext_consumption_enabled=false;
    boolean responsive_web_enhance_cards_enabled=false;



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
