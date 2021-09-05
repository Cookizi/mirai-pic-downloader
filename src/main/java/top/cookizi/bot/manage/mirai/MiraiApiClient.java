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

    @RequestLine("POST auth")
    AuthResp auth(@Param("authKey") String authKey);

    @RequestLine("POST verify")
    VerifyResp verify(@Param("sessionKey") String sessionKey, @Param("qq") Long qq);

    @RequestLine("POST release")
    Map<String, String> release(@Param("sessionKey") String sessionKey, @Param("qq") Long qq);

    @RequestLine("POST sendFriendMessage")
    Map<String, String> sendFriendMessage(@Param("sessionKey") String sessionKey,
                                          @Param("target") Long targetQQ,
                                          @Param("messageChain") List<Msg> messageChain);

    @RequestLine("POST uploadImage")
    @Headers("Content-Type: multipart/form-data")
    ImgUploadResp uploadImage(@Param("sessionKey") String sessionKey,
                              @Param("type") String type,
                              @Param("img") File img);

    @RequestLine("POST sendGroupMessage")
    Map<String, String> sendGroupMessage(@Param("sessionKey") String sessionKey,
                                         @Param("target") Long targetGroup,
                                         @Param("messageChain") List<Msg> messageChain);

    @RequestLine("POST config")
    void updateSessionConfig(@Param("sessionKey") String session,
                             @Param("cacheSize") int cacheSize,
                             @Param("enableWebsocket") boolean enableWebsocket);

    @RequestLine("GET groupList")
    List<GroupInfo> getGroupList(@QueryMap Map<String,Object>  map);
}
