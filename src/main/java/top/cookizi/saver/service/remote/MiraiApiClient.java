package top.cookizi.saver.service.remote;

import feign.Param;
import feign.RequestLine;
import top.cookizi.saver.data.msg.Msg;
import top.cookizi.saver.data.resp.AuthResp;
import top.cookizi.saver.data.resp.VerifyResp;

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

    @RequestLine("POST sendGroupMessage")
    Map<String, String> sendGroupMessage(@Param("sessionKey") String sessionKey,
                                          @Param("target") Long targetGroup,
                                          @Param("messageChain") List<Msg> messageChain);

    @RequestLine("POST config")
    void updateSessionConfig(@Param("sessionKey") String session,
                             @Param("cacheSize") int cacheSize,
                             @Param("enableWebsocket") boolean enableWebsocket);
}
