package top.cookizi.bot.modle.domain;

import lombok.Builder;
import lombok.Data;

/**
 * @author heq
 * @date 2021/5/14 3:28 下午
 * @description
 */
@Data
@Builder
public class CmdRes {

    /**
     * 是否是bean类型指令
     */
    private boolean beanCmd;

    private boolean sendMsg;

    private SendMsg msgBody;

    public static CmdRes BeanCmd() {
        return CmdRes.builder().beanCmd(true).build();
    }

}
