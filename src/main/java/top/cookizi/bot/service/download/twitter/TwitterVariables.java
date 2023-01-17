package top.cookizi.bot.service.download.twitter;

import com.google.gson.Gson;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
class TwitterVariables {
    String focalTweetId;//="1540628605612466179";
    boolean with_rux_injections = false;
    boolean includePromotedContent = true;
    boolean withCommunity = true;
    boolean withQuickPromoteEligibilityTweetFields = true;
    boolean withBirdwatchNotes = true;
    boolean withSuperFollowsUserFields = true;
    boolean withDownvotePerspective = false;
    boolean withReactionsMetadata = false;
    boolean withReactionsPerspective = false;
    boolean withSuperFollowsTweetFields = true;
    boolean withVoice = true;
    boolean withV2Timeline = true;

    private TwitterVariables(String focalTweetId) {
        this.focalTweetId = focalTweetId;
    }

    public static String toJson(String id) {
        return new Gson().toJson(new TwitterVariables(id));
    }
}