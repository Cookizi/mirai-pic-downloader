package top.cookizi.bot.service.impl;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.cookizi.bot.common.constant.CommonConst;
import top.cookizi.bot.common.enums.MsgType;
import top.cookizi.bot.manage.mirai.MiraiApiClient;
import top.cookizi.bot.modle.msg.Msg;
import top.cookizi.bot.modle.msg.PlainTextMsg;
import top.cookizi.bot.service.MsgSendService;

import java.util.ArrayList;
import java.util.List;
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
    public void sendToGroup(long id, String text) {
        List<Msg> msgList = new ArrayList<>();
        PlainTextMsg textMsg = new PlainTextMsg();
        textMsg.setText(text);
        msgList.add(textMsg);
        Map<String, String> res = sendMsg(id, msgList, MsgType.GROUP_MESSAGE);
        System.out.println(JSON.toJSONString(res));
    }

    private Map<String, String> sendMsg(Long target, List<Msg> msgList, MsgType msgType) {
        Map<String, String> res = null;
        switch (msgType) {
            case FRIEND_MESSAGE:
                res = apiClient.sendFriendMessage(CommonConst.getSession(), target, msgList);
                break;
            case GROUP_MESSAGE:
                res = apiClient.sendGroupMessage(CommonConst.getSession(), target, msgList);
                break;
            default:
                log.warn("不存在消息类型,target:{},msgList:{}", target, JSON.toJSON(msgList));
        }
        return res;
    }
}
