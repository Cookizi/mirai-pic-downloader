package top.cookizi.bot.service.command;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.cookizi.bot.common.constant.MemoryConst;
import top.cookizi.bot.common.enums.MsgType;
import top.cookizi.bot.common.utils.StringUtils;
import top.cookizi.bot.modle.domain.CmdRes;
import top.cookizi.bot.modle.domain.SendMsg;
import top.cookizi.bot.modle.resp.MsgResp;
import top.cookizi.bot.service.MiraiApiService;
import top.cookizi.bot.service.MsgSendService;

import java.util.function.Function;

/**
 * @author heq
 * @date 2021/5/13 7:17 下午
 * @description
 */
@Service
@Slf4j
public class CommandHandle {

    @Autowired
    private MsgSendService msgSendService;
    @Autowired
    private MiraiApiService apiService;

    public static boolean isCommand(String text) {
        if (StringUtils.isBlank(text)) {
            return false;
        }
        return text.charAt(0) == '/';
    }

    public void runCommand(String text, MsgResp msgResp, String sessionKey) {
        text = text.substring(1);
        String[] commands = text.split(" ");
        if (commands.length == 0) {
            return;
        }

        CmdRes cmdRes;
        try {
            Function<String[], ? extends CmdRes> run = MemoryConst.getCommand(commands[0]);
            if (run == null) {
                return;
            }
            cmdRes = run.apply(commands);
        } catch (Exception e) {
            cmdRes = CmdRes.builder().sendMsg(true)
                    .msgBody(SendMsg.TextMsg("指令异常" + e.getMessage())).build();
            e.printStackTrace();
        }

        if (cmdRes.isBeanCmd()) {
            switch (commands[0]) {
                case "session":
                    apiService.resetSession();
                    break;
                default:
            }
        }

        if (cmdRes.isSendMsg()) {
            SendMsg sendMsg = cmdRes.getMsgBody();
            switch (MsgType.parse(msgResp.getType())) {
                case GROUP_MESSAGE:
                    sendMsg.setTarget(msgResp.getSender().getGroup().getId());
                    msgSendService.sendToGroup(sendMsg);
                    break;
                case FRIEND_MESSAGE:
                    sendMsg.setTarget(msgResp.getSender().getId());
                    msgSendService.sendToFriend(sendMsg);
                    break;
                default:
                    log.warn("non msg type");
            }
        }
    }

}
