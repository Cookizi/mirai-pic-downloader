/*
package top.cookizi.bot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.cookizi.bot.common.enums.MsgChainType;
import top.cookizi.bot.common.enums.MsgType;
import top.cookizi.bot.modle.msg.Msg;
import top.cookizi.bot.modle.msg.PlainTextMsg;
import top.cookizi.bot.modle.resp.MsgResp;
import top.cookizi.bot.service.command.CommandHandle;

import java.util.List;

*/
/**
 * @author heq
 * @date 2021/5/13 3:37 下午
 * @description
 *//*

@Service
@Slf4j
public class MsgHandleService {

    @Autowired
    private CommandHandle commandHandle;

    */
/**
     * 接受消息统一入口
     *
     * @param msgResp
     * @param sessionKey
     *//*

    public void messageHandle(MsgResp msgResp, String sessionKey) {
        MsgType msgType = MsgType.parse(msgResp.getType());

        if (before(msgResp, sessionKey)) {
            return;
        }

        switch (msgType) {
            case GROUP_MESSAGE:
                groupMessageHandle(msgResp, sessionKey);
                break;
            case FRIEND_MESSAGE:
                friendMessageHandle(msgResp, sessionKey);
                break;
            default:
                log.error("不存在消息类型:{},sessionKey:{}", msgResp.getType(), sessionKey);
        }
    }

    private boolean before(MsgResp msgResp, String sessionKey) {
        List<Msg> messageChain = msgResp.getMessageChain();
        if (messageChain == null || messageChain.size() == 0) {
            return true;
        }

        Msg secondMsg = messageChain.get(1);
        if (!MsgChainType.PLAIN.type.equals(secondMsg.getType())) {
            return false;
        }
        PlainTextMsg textMsg = (PlainTextMsg) secondMsg;
        if (!CommandHandle.isCommand(textMsg.getText())) {
            return false;
        }

        commandHandle.runCommand(textMsg.getText(), msgResp, sessionKey);
        return true;
    }

    private void groupMessageHandle(MsgResp msgResp, String sessionKey) {

    }

    private void friendMessageHandle(MsgResp msgResp, String sessionKey) {

    }


}
*/
