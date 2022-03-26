package top.cookizi.bot.service.download.twitter;

import com.google.gson.Gson;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
class TwitterVariables {
    String focalTweetId;
    boolean with_rux_injections = true;
    boolean includePromotedContent = true;
    boolean withCommunity = true;
    boolean withQuickPromoteEligibilityTweetFields = true;
    boolean withBirdwatchNotes = false;
    boolean withSuperFollowsUserFields = true;
    boolean withDownvotePerspective = true;
    boolean withReactionsMetadata = false;
    boolean withReactionsPerspective = false;
    boolean withSuperFollowsTweetFields = true;
    boolean withVoice = true;
    boolean withV2Timeline = false;
    boolean __fs_dont_mention_me_view_api_enabled = false;
    boolean __fs_interactive_text_enabled = true;
    boolean __fs_responsive_web_uc_gql_enabled = false;

    private TwitterVariables(String focalTweetId) {
        this.focalTweetId = focalTweetId;
    }

    public static String toJson(String id) {
        return URLEncoder.encode(new Gson().toJson(new TwitterVariables(id)), StandardCharsets.UTF_8);
    }
}