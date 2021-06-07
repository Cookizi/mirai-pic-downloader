package top.cookizi.bot.dispatcher.data;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ExecuteResultEnum {
    OK("执行成功，将会返回结果"),
    IGNORE("忽略执行结果，不返回"),
    ERROR("发生异常"),;

    public final String desc;
}
