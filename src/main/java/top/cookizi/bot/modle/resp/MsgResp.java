package top.cookizi.bot.modle.resp;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import top.cookizi.bot.modle.domain.Sender;
import top.cookizi.bot.modle.msg.Msg;

import java.util.List;
import java.util.stream.Collectors;

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
}
