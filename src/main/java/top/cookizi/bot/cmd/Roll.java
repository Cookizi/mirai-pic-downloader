package top.cookizi.bot.cmd;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import top.cookizi.bot.dispatcher.annotation.MiraiCmd;
import top.cookizi.bot.dispatcher.annotation.MiraiCmdArg;
import top.cookizi.bot.dispatcher.annotation.MiraiCmdDefine;
import top.cookizi.bot.dispatcher.config.CommandScope;
import top.cookizi.bot.service.group.RollService;

@Slf4j
@MiraiCmd
public class Roll {

    @Autowired
    private RollService rollService;


    @MiraiCmdDefine(name = "r", scope = CommandScope.GROUP, desc = "随机roll点")
    public String roll(@MiraiCmdArg String rollArgs) {
        String result = rollService.calc(rollArgs);
        return result;
    }
    @MiraiCmdDefine(name = "rd", scope = CommandScope.GROUP, desc = "随机roll点")
    public String rollDefault() {
        String result = rollService.calc("d");
        return result;
    }


}
