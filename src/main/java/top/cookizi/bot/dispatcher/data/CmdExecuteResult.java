package top.cookizi.bot.dispatcher.data;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CmdExecuteResult<T> {

    ExecuteResultEnum resultEnum;
    String msg;
    T data;

    private CmdExecuteResult() {
    }

    public boolean isResp() {
        return resultEnum != ExecuteResultEnum.IGNORE;
    }

    public static <T> CmdExecuteResult<T> ok(T data) {
        CmdExecuteResult<T> result = new CmdExecuteResult<>();
        result.data = data;
        result.resultEnum = ExecuteResultEnum.OK;
        return result;
    }

    public static <T> CmdExecuteResult<T> ignore() {
        CmdExecuteResult<T> result = new CmdExecuteResult<>();
        result.resultEnum = ExecuteResultEnum.IGNORE;
        return result;
    }

    public static CmdExecuteResult<Exception> error(Exception e) {
        CmdExecuteResult<Exception> result = new CmdExecuteResult<>();
        result.resultEnum = ExecuteResultEnum.ERROR;
        result.data = e;
        return result;
    }
}
