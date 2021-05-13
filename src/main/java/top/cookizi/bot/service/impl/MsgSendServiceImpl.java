package top.cookizi.bot.service.impl;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import top.cookizi.bot.cache.CommonCache;
import top.cookizi.bot.common.enums.MsgType;
import top.cookizi.bot.manage.mirai.MiraiApiClient;
import top.cookizi.bot.modle.domain.SendMsg;
import top.cookizi.bot.service.MsgSendService;

import java.util.Map;

/**
 * @author heq
 * @date 2021/5/13 5:02 下午
 * @description
 */
@Service
@Slf4j
public class MsgSendServiceImpl implements MsgSendService {

    @Autowired
    private MiraiApiClient apiClient;

    @Override
    public boolean sendTextToGroup(long id, String text) {
        return sendToGroup(SendMsg.builder()
                .target(id)
                .text(text)
                .build());
    }

    @Override
    public boolean sendTextToFriend(long id, String text) {
        return sendToFriend(SendMsg.builder()
                .target(id)
                .text(text)
                .build());
    }

    @Override
    public boolean sendToGroup(SendMsg sendMsg) {
        Map<String, String> res = sendMsg(sendMsg, MsgType.GROUP_MESSAGE);
        log.info("send msg to group res :{}", JSON.toJSONString(res));
        return checkSendRes(res);
    }

    @Override
    public boolean sendToFriend(SendMsg sendMsg) {
        Map<String, String> res = sendMsg(sendMsg, MsgType.FRIEND_MESSAGE);
        log.info("send msg to friend res :{}", JSON.toJSONString(res));
        return checkSendRes(res);
    }

    /**
     * 消息唯一出口方便监控
     *
     * @param sendMsg
     * @param msgType
     * @return
     */
    private Map<String, String> sendMsg(SendMsg sendMsg, MsgType msgType) {
        Map<String, String> res = null;
        switch (msgType) {
            case FRIEND_MESSAGE:
                res = apiClient.sendFriendMessage(CommonCache.getSession(), sendMsg.getTarget(), sendMsg.getMsgList());
                break;
            case GROUP_MESSAGE:
                res = apiClient.sendGroupMessage(CommonCache.getSession(), sendMsg.getTarget(), sendMsg.getMsgList());
                break;
            default:
                log.warn("不存在消息类型,msgType:{},sendMsg:{}", msgType, JSON.toJSON(sendMsg));
        }
        return res;
    }

    private boolean checkSendRes(Map<String, String> res) {
        if (CollectionUtils.isEmpty(res)) {
            return false;
        }
        return "0".equals(res.get("code"));
    }
}
