package top.cookizi.bot.modle.msg;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import top.cookizi.bot.common.enums.MsgChainType;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PlainTextMsg extends Msg {
    String text;

    public PlainTextMsg(String text) {
        this.text = text;
    }

    @Override
    public String getType() {
        return MsgChainType.PLAIN.getType();
    }
}
