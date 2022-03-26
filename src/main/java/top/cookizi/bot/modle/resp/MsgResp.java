package top.cookizi.bot.modle.resp;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import top.cookizi.bot.common.enums.MsgChainType;
import top.cookizi.bot.modle.domain.Sender;
import top.cookizi.bot.modle.msg.Msg;
import top.cookizi.bot.modle.msg.SourceMsg;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MsgResp {
    protected String type;
    protected List<Msg> messageChain;
    protected Sender sender;

    @SuppressWarnings("unchecked")
    public <T> List<T> getSpecificMsg(Class<T> clazz) {
        return messageChain.stream().filter(x -> x.getClass().equals(clazz))
                .map(x -> (T) x)
                .collect(Collectors.toList());
    }

    public Long getMsgId() {
        return this.messageChain.stream().filter(x -> MsgChainType.SOURCE.type.equals(x.getType()))
                .map(x -> ((SourceMsg) x).getId())
                .findFirst().orElse(null);
    }
}
