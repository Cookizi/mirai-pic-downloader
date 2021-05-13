package top.cookizi.bot.modle.msg;

import lombok.Data;
import lombok.EqualsAndHashCode;
import top.cookizi.bot.common.enums.MsgChainType;

@Data
@EqualsAndHashCode(callSuper = true)
public class SourceMsg extends Msg {
    long id;
    long time;

    @Override
    public String getType() {
        return MsgChainType.SOURCE.type;
    }
}
