package top.cookizi.bot.manage.mirai;

import feign.Headers;
import feign.Param;
import feign.QueryMap;
import feign.RequestLine;
import top.cookizi.bot.modle.msg.Msg;
import top.cookizi.bot.modle.resp.*;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface MiraiApiClient {

    @RequestLine("POST verify")
    AuthResp verify(@Param("verifyKey") String authKey);

    @RequestLine("POST bind")
    VerifyResp bind(@Param("sessionKey") String sessionKey, @Param("qq") Long qq);

    @RequestLine("POST release")
    Map<String, String> release(@Param("sessionKey") String sessionKey, @Param("qq") Long qq);

    @RequestLine("POST sendFriendMessage")
    Map<String, String> sendFriendMessage(@Param("sessionKey") String sessionKey,
                                          @Param("target") Long targetQQ,
                                          @Param("quote") Long quoteId,
                                          @Param("messageChain") List<Msg> messageChain);

    default Map<String, String> sendFriendMessage(String sessionKey,
                                                  Long targetQQ,
                                                  List<Msg> messageChain) {
        return sendFriendMessage(sessionKey, targetQQ, null, messageChain)
                ;
    }

    @RequestLine("POST uploadImage")
    @Headers("Content-Type: multipart/form-data")
    ImgUploadResp uploadImage(@Param("sessionKey") String sessionKey,
                              @Param("type") String type,
                              @Param("img") File img);

    @RequestLine("POST sendGroupMessage")
    Map<String, String> sendGroupMessage(@Param("sessionKey") String sessionKey,
                                         @Param("target") Long targetGroup,
                                         @Param("quote") Long quoteId,
                                         @Param("messageChain") List<Msg> messageChain);

    default Map<String, String> sendGroupMessage(String sessionKey,
                                                 Long targetGroup,
                                                 List<Msg> messageChain) {
        return sendGroupMessage(sessionKey, targetGroup, null, messageChain);
    }

    @RequestLine("POST config")
    void updateSessionConfig(@Param("sessionKey") String session,
                             @Param("cacheSize") int cacheSize,
                             @Param("enableWebsocket") boolean enableWebsocket);

    @RequestLine("GET groupList")
    BaseResponse<List<GroupInfo>> getGroupList(@QueryMap Map<String, Object> map);
}
