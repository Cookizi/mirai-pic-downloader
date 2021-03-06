package top.cookizi.saver.service.afterDownload;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.cookizi.saver.config.AppConfig;
import top.cookizi.saver.data.msg.ImgMsg;
import top.cookizi.saver.data.msg.Msg;
import top.cookizi.saver.service.remote.MiraiApiClient;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ImgForwardService extends AbstractAfterDownloadService {
    @Autowired
    private MiraiApiClient miraiApiClient;
    @Autowired
    private AppConfig appConfig;

    @Override
    public void process(String session, List<String> urlList) {
        log.info("开始转发【{}】张图片到配置的群内。", urlList.size());
        List<Msg> msgList = urlList.stream().map(ImgMsg::new).collect(Collectors.toList());
        appConfig.getForwardGroups().parallelStream()
                .forEach(x -> miraiApiClient.sendGroupMessage(session, x, msgList));
    }
}
