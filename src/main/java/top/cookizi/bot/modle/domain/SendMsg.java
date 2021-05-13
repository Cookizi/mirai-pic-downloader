package top.cookizi.bot.modle.domain;

import lombok.Data;
import top.cookizi.bot.common.enums.MsgType;
import top.cookizi.bot.modle.msg.ImgMsg;
import top.cookizi.bot.modle.msg.Msg;
import top.cookizi.bot.modle.msg.PlainTextMsg;

import java.util.ArrayList;
import java.util.List;

/**
 * @author heq
 * @date 2021/5/14 10:46 上午
 * @description
 */
@Data
public class SendMsg {

    private Long target;

    private List<Msg> msgList;

    private MsgType msgType;

    public static SendMsgBuilder builder() {
        return new SendMsgBuilder();
    }

    public static class SendMsgBuilder {

        private final SendMsg sendMsg;

        public SendMsgBuilder() {
            this.sendMsg = new SendMsg();
            sendMsg.msgList = new ArrayList<>();
        }

        public SendMsgBuilder text(String text) {
            sendMsg.msgList.add(new PlainTextMsg(text));
            return this;
        }

        public SendMsgBuilder image(String url) {
            sendMsg.msgList.add(new ImgMsg(url));
            return this;
        }


        public SendMsgBuilder image(String url, String imageId, String path) {
            sendMsg.msgList.add(new ImgMsg(url, imageId, path));
            return this;
        }

        public SendMsgBuilder target(Long target) {
            sendMsg.target = target;
            return this;
        }

        public SendMsgBuilder msgType(MsgType msgType) {
            sendMsg.msgType = msgType;
            return this;
        }

        public SendMsg build() {
            return sendMsg;
        }

    }
}
