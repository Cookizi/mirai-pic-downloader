package top.cookizi.bot.modle.resp;

import lombok.Data;

@Data
public class BaseResponse<T> {

    int code;

    String msg;

    T data;
}
