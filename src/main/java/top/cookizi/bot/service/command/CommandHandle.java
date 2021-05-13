package top.cookizi.bot.service.command;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.cookizi.bot.common.utils.StringMathUtils;
import top.cookizi.bot.common.utils.StringUtils;
import top.cookizi.bot.modle.resp.MsgResp;
import top.cookizi.bot.service.MiraiApiService;
import top.cookizi.bot.service.MsgSendService;

import java.util.List;

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
        switch (commands[0]) {
            case "random":
                msgSendService.sendTextToGroup(msgResp.getSender().getGroup().getId(), "随机数：" + String.valueOf((int) (Math.random() * 100)));
                break;
            case "计算":
                String res = "结果：";
                try {
                    List<String> postFix = StringMathUtils.infixToPostfix(commands[1]);
                    res = res + StringMathUtils.calculate(postFix);
                } catch (RuntimeException e) {
                    res = e.getMessage();
                }
                msgSendService.sendTextToGroup(msgResp.getSender().getGroup().getId(), res);
                break;
            case "session":
                apiService.resetSession();
                break;
            default:
                log.warn("command not found");
        }

    }

}
