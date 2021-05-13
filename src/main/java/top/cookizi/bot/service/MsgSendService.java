package top.cookizi.bot.service;

/**
 * @author heq
 * @date 2021/5/13 4:36 下午
 * @description
 */
public interface MsgSendService {

    void sendToGroup(long id, String text);
}
