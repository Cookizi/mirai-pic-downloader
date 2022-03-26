package top.cookizi.bot.dispatcher.config;

/**
 * 消息处理完之后返回信息的类型
 */
public enum CmdRespType {
    NORMAL, //默认情况，直接返回文本信息
    AT, //@发送命令的人
    QUOTE //回复发送信息的人（可能不太方便做）
}
