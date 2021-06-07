package top.cookizi.bot.cmd;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import top.cookizi.bot.dispatcher.annotation.MiraiCmd;
import top.cookizi.bot.dispatcher.annotation.MiraiCmdDefine;
import top.cookizi.bot.dispatcher.config.CmdType;
import top.cookizi.bot.dispatcher.config.CommandScope;
import top.cookizi.bot.dispatcher.data.CmdExecuteResult;
import top.cookizi.bot.modle.msg.Msg;
import top.cookizi.bot.modle.msg.PlainTextMsg;
import top.cookizi.bot.modle.resp.MsgResp;
import top.cookizi.bot.service.download.AbstractDownloadService;
import top.cookizi.bot.service.download.ImgForwardService;

import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@MiraiCmd
public class PicDownload {
    @Autowired
    private List<AbstractDownloadService> downloadServiceList;
    @Autowired
    private ImgForwardService imgForwardService;
    @Autowired
    private ThreadPoolExecutor threadPool;


    @MiraiCmdDefine(name = "下图", cmdType = CmdType.LISTENER, desc = "下载站点的图", scope = CommandScope.FRIEND)
    public CmdExecuteResult<List<Msg>> downloadImg(MsgResp msgResp) {

        Msg.MsgBuilder builder = Msg.MsgBuilder.newBuilder();
        for (AbstractDownloadService downloadService : downloadServiceList) {
            List<Msg> imgMsgList = downloadService.handleMsg(msgResp);
            if (imgMsgList.isEmpty()) {
                continue;
            }
            try {
                List<String> imgNameList = downloadService.download(imgMsgList);
                if (imgMsgList.isEmpty()) {
                    builder.append(new PlainTextMsg("未发现图片"));
                    break;
                }
                threadPool.execute(() -> imgForwardService.forward(imgMsgList, imgNameList));
                builder.append(new PlainTextMsg("下载成功，下载的文件：" + String.join("，", imgNameList)));
                break;
            } catch (Exception e) {
                builder.append(new PlainTextMsg("下载图片发生异常，异常原因：" + e.getMessage()));
                break;
            }
        }
        List<Msg> msgList = builder.build();
        if (msgList.isEmpty()) {
            return CmdExecuteResult.ignore();
        }
        return CmdExecuteResult.ok(msgList);
    }

}
