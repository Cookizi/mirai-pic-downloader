package top.cookizi.bot.modle.msg;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import top.cookizi.bot.common.enums.MsgChainType;

/**
 * @author heq
 * @date 2021/5/19 5:56 下午
 * @description
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AtAllMsg extends Msg{

    private long target;

    @Override
    public String getType() {
        return MsgChainType.AT_ALL.type;
    }
}
