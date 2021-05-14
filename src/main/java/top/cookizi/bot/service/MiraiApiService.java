package top.cookizi.bot.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.cookizi.bot.common.constant.MemoryConst;
import top.cookizi.bot.config.AppConfig;
import top.cookizi.bot.modle.resp.AuthResp;
import top.cookizi.bot.manage.mirai.MiraiApiClient;

@Service
@Slf4j
public class MiraiApiService {

    @Autowired
    private MiraiApiClient miraiApiClient;

    @Autowired
    private AppConfig appConfig;

    public String enableWebsocket() {
        AuthResp auth = miraiApiClient.auth(appConfig.getAuthKey());
        miraiApiClient.verify(auth.getSession(), appConfig.getQq());
        miraiApiClient.updateSessionConfig(auth.getSession(), 4096, true);

        return auth.getSession();
    }

    public void releaseSession(String session) {
        miraiApiClient.release(session, appConfig.getQq());
    }

    public void resetSession() {
        log.info("update session old session:{}", MemoryConst.getSession());
        miraiApiClient.release(MemoryConst.getSession(), appConfig.getQq());
        AuthResp auth = miraiApiClient.auth(appConfig.getAuthKey());
        miraiApiClient.verify(auth.getSession(), appConfig.getQq());
        MemoryConst.setSession(auth.getSession());
        log.info("update session new session:{}", MemoryConst.getSession());
    }
}
