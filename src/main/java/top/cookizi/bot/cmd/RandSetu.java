package top.cookizi.bot.cmd;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import top.cookizi.bot.common.constant.MemoryConst;
import top.cookizi.bot.common.utils.ImageUtil;
import top.cookizi.bot.config.AppConfig;
import top.cookizi.bot.dispatcher.annotation.MiraiCmd;
import top.cookizi.bot.dispatcher.annotation.MiraiCmdDefine;
import top.cookizi.bot.dispatcher.data.CmdExecuteResult;
import top.cookizi.bot.manage.mirai.MiraiApiClient;
import top.cookizi.bot.modle.msg.ImgMsg;
import top.cookizi.bot.modle.msg.Msg;
import top.cookizi.bot.modle.msg.PlainTextMsg;
import top.cookizi.bot.modle.resp.ImgUploadResp;

import java.io.File;
import java.util.List;
import java.util.Random;

@Slf4j
@MiraiCmd
public class RandSetu {
    @Autowired
    private AppConfig appConfig;
    @Autowired
    private MiraiApiClient miraiApiClient;

    @MiraiCmdDefine(name = "色图谢谢")
    public CmdExecuteResult<List<Msg>> setu() {
        Msg.MsgBuilder builder = Msg.MsgBuilder.newBuilder();

        int v = (int) (Math.random() * 100);
        log.debug("随机色图={}，概率={}", v, appConfig.getSetuRate());
        if (v > appConfig.getSetuRate()) {
            return CmdExecuteResult.ok(builder.append(new PlainTextMsg("色不出来,色气值=" + (100 - v))).build());
        }

        String path = appConfig.getSavePath();
        File file = new File(path);
        File[] list = file.listFiles();
        if (list == null) {
            builder.append(new PlainTextMsg("没有色图"));
            return CmdExecuteResult.ok(builder.build());
        }
        Random random = new Random(System.currentTimeMillis());

        file = list[random.nextInt(list.length)];
        ImgUploadResp group = miraiApiClient.uploadImage(MemoryConst.getSession(), "group", file);

        builder.append(new ImgMsg(group.getUrl(), group.getImageId(), group.getPath()));
        try {
            var comment = ImageUtil.readComment(file);
            if (comment != null) {
                builder.append(new PlainTextMsg("来源：" + comment));
            }
        } catch (Exception ignored) {

        }
        return CmdExecuteResult.ok(builder.build());

    }
}
