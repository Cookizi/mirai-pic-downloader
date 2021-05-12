package top.cookizi.saver.data.msg;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public abstract class Msg {
    protected String type;

    public static class MsgBuilder {
        private final List<Msg> msgList = new ArrayList<>();

        public static MsgBuilder newBuilder() {
            return new MsgBuilder();
        }

        public MsgBuilder append(Msg msg) {
            msgList.add(msg);
            return this;
        }

        public List<Msg> build() {
            return msgList;
        }
    }

    public Msg() {
        this.type = getType();
    }

    public abstract String getType();
}
