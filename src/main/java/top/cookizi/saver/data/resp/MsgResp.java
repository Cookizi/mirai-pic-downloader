package top.cookizi.saver.data.resp;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import top.cookizi.saver.data.Sender;
import top.cookizi.saver.data.msg.Msg;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public  class MsgResp {
    protected String type;
    protected List<Msg> messageChain;
    protected Sender sender;

//    public MsgResp() {
//        this.type = getType();
//    }
//
//    public abstract String getType();
}
