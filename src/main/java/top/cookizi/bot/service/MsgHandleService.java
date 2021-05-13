package top.cookizi.bot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.cookizi.bot.common.enums.MsgType;
import top.cookizi.bot.modle.resp.MsgResp;

/**
 * @author heq
 * @date 2021/5/13 3:37 下午
 * @description
 */
@Service
@Slf4j
public class MsgHandleService {

    /**
     * 接受消息统一入口
     *
     * @param msgResp
     * @param sessionKey
     */
    public void messageHandle(MsgResp msgResp, String sessionKey) {
        MsgType msgType = MsgType.parse(msgResp.getType());

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

    private void groupMessageHandle(MsgResp msgResp, String sessionKey) {

    }

    private void friendMessageHandle(MsgResp msgResp, String sessionKey) {

    }


}
