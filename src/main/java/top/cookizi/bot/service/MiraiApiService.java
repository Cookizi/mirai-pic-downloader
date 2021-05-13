package top.cookizi.bot.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.cookizi.bot.config.AppConfig;
import top.cookizi.bot.modle.resp.AuthResp;
import top.cookizi.bot.manage.mirai.MiraiApiClient;

@Service
public class MiraiApiService {

    @Autowired
    private MiraiApiClient miraiApiClient;

    @Autowired
    private AppConfig appConfig;

    public String enableWebsocket() {
        AuthResp auth = miraiApiClient.auth(appConfig.getAuthKey());
        miraiApiClient.verify(auth.getSession(), appConfig.getQq());
        miraiApiClient.updateSessionConfig(auth.getSession(),4096,true);

        return auth.getSession();
    }

    public void releaseSession(String session) {
        miraiApiClient.release(session, appConfig.getQq());
    }
}
