package top.cookizi.bot.modle.resp;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import top.cookizi.bot.modle.msg.Msg;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class MsgResp {
    protected String type;
    protected List<Msg> messageChain;
    protected Sender sender;

    public MsgResp() {
        this.type = getType();
    }

    public abstract String getType();
}
