package top.cookizi.bot.service;

import top.cookizi.bot.modle.domain.SendMsg;

/**
 * @author heq
 * @date 2021/5/13 4:36 下午
 * @description
 */
public interface MsgSendService {

    boolean sendTextToGroup(long id, String text);

    boolean sendTextToFriend(long id, String text);

    boolean sendToGroup(SendMsg sendMsg);

    boolean sendToFriend(SendMsg sendMsg);
}
