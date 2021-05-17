package top.cookizi.bot.service.download;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.cookizi.bot.common.constant.MemoryConst;
import top.cookizi.bot.common.utils.StringUtils;
import top.cookizi.bot.config.AppConfig;
import top.cookizi.bot.manage.mirai.MiraiApiClient;
import top.cookizi.bot.modle.msg.ImgMsg;
import top.cookizi.bot.modle.msg.Msg;
import top.cookizi.bot.modle.msg.PlainTextMsg;
import top.cookizi.bot.modle.resp.ImgUploadResp;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ImgForwardService {
    @Autowired
    private MiraiApiClient miraiApiClient;
    @Autowired
    private AppConfig appConfig;
    @Autowired
    private ThreadPoolExecutor threadPoolExecutor;

    /**
     * 转发下载的图片到指定的群里面
     * <p>
     * 1）读取下载的问题
     * 2）发送到QQ服务器，返回图片地址
     * 3）通过图片地址来发送消息到群里
     * 4）清理临时文件
     *
     * @param imgMsgList  图片来源
     * @param imgNameList 本地图片路径
     */
    public void forward(List<Msg> imgMsgList, List<String> imgNameList) {
        log.info("开始转发{}张图片到群", imgMsgList.size());

        List<File> fileList = imgNameList.stream().map(x -> new File(appConfig.getSavePath() + x)).collect(Collectors.toList());
        List<Msg> msgList = new ArrayList<>();
        log.info("开始上传图片");
        for (File file : fileList) {
            ImgUploadResp uploadResp = miraiApiClient.uploadImage(MemoryConst.getSession(), "group", file);
            msgList.add(new ImgMsg(uploadResp.getUrl(), uploadResp.getImageId(), uploadResp.getPath()));
        }

        if (msgList.isEmpty()) {
            log.debug("没有上传成功的图");
            return;
        }

        List<String> source = imgMsgList.stream()
                .filter(x -> x instanceof PlainTextMsg)
                .map(x -> (PlainTextMsg) x)
                .map(PlainTextMsg::getText)
                .collect(Collectors.toList());
        if (!source.isEmpty()) {
            msgList.add(new PlainTextMsg("来源：\n" + String.join(",", source)));
        }

        log.info("图片上传成，开始转发");
        appConfig.getForwardGroups()
                .forEach(group -> threadPoolExecutor.submit(
                        () -> miraiApiClient.sendGroupMessage(MemoryConst.getSession(), group, msgList)
                        )
                );
        threadPoolExecutor.execute(() -> {
            log.info("开始异步清理缓存");
            String miraiHttpApiPath = appConfig.getMiraiHttpApiPath();

            if (StringUtils.isBlank(miraiHttpApiPath)) {
                log.info("未配置mcl路径，将不清理缓存");
                return;
            }
            File tempImgDir = new File(miraiHttpApiPath + "/data/net.mamoe.mirai-api-http/images");
            if (!tempImgDir.exists()) {
                return;
            }
            clearDir(tempImgDir);
            log.info("缓存清理完成");
        });
    }

    private void clearDir(File file) {
        File[] files = file.listFiles();
        if (files == null) return;
        for (File f : files) {
            if (f.isDirectory()) clearDir(f);
            f.delete();
        }
    }
}
