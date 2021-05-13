package top.cookizi.bot.common.enums.command;

import lombok.AllArgsConstructor;
import top.cookizi.bot.common.constant.MemoryConst;
import top.cookizi.bot.modle.domain.CmdRes;
import top.cookizi.bot.modle.domain.SendMsg;

import java.util.Calendar;
import java.util.function.Function;

/**
 * @author HeQ
 * @version 1.0
 * @date 2021/5/15 23:32
 */
@AllArgsConstructor
public enum OnmyojiCmd implements ICommand {

    MONDAY("歌姬", Command.ONMYOJI_CMD, (commands) -> {
        return CmdRes.builder().sendMsg(true)
                .msgBody(SendMsg.builder()
                        .image("https://ok.166.net/reunionpub/ds/kol/20210428/173052-veys63um2j.jpeg")
                        .image("https://ok.166.net/reunionpub/ds/kol/20210428/173402-oc4p7vwyt5.jpeg")
                        .image("https://ok.166.net/reunionpub/ds/kol/20210428/173420-j5y81cdur6.jpeg")
                        .image("https://ok.166.net/reunionpub/ds/kol/20210428/173434-oljhv7zsui.jpeg")
                        .image("https://ok.166.net/reunionpub/ds/kol/20210428/173513-68etmiqcpu.jpeg")
                        .image("https://ok.166.net/reunionpub/ds/kol/20210428/173546-ijs6bmgc70.jpeg")
                        .build()).build();
    }),
    TUESDAY("蜃气楼", Command.ONMYOJI_CMD, (commands) -> {
        return CmdRes.builder().sendMsg(true)
                .msgBody(SendMsg.builder()
                        .image("https://ok.166.net/reunionpub/ds/kol/20210428/173141-aky1p3wzel.jpeg")
                        .image("https://ok.166.net/reunionpub/ds/kol/20210428/174157-htjdb329sy.jpeg")
                        .image("https://ok.166.net/reunionpub/ds/kol/20210428/174218-z6t2n4jcvu.jpeg")
                        .image("https://ok.166.net/reunionpub/ds/kol/20210428/174234-8qhunirewm.jpeg")
                        .image("https://ok.166.net/reunionpub/ds/kol/20210428/174249-bey2ssful7.jpeg")
                        .image("https://ok.166.net/reunionpub/ds/kol/20210428/174303-trs7uvfjml.jpeg")
                        .build()).build();
    }),
    WEDNESDAY("土蜘蛛", Command.ONMYOJI_CMD, (commands) -> {
        return CmdRes.builder().sendMsg(true)
                .msgBody(SendMsg.builder()
                        .image("https://ok.166.net/reunionpub/ds/kol/20210428/173158-ahbspvy7ge.jpeg")
                        .image("https://ok.166.net/reunionpub/ds/kol/20210428/174850-ht02s5vwy9.jpeg")
                        .image("https://ok.166.net/reunionpub/ds/kol/20210428/174906-s0ezkq63mo.jpeg")
                        .image("https://ok.166.net/reunionpub/ds/kol/20210428/174925-o50mshgib2.jpeg")
                        .image("https://ok.166.net/reunionpub/ds/kol/20210428/174936-09kdi87mpo.jpeg")
                        .build()).build();
    }),
    THURSDAY("荒骷髅", Command.ONMYOJI_CMD, (commands) -> {
        return CmdRes.builder().sendMsg(true)
                .msgBody(SendMsg.builder()
                        .image("https://ok.166.net/reunionpub/ds/kol/20210428/173218-a659u4cr7s.jpeg")
                        .image("https://ok.166.net/reunionpub/ds/kol/20210428/175459-n1sptw2hoi.jpeg")
                        .image("https://ok.166.net/reunionpub/ds/kol/20210428/175609-vlna1ycfi5.jpeg")
                        .image("https://ok.166.net/reunionpub/ds/kol/20210428/175622-a9souwzv7k.jpeg")
                        .image("https://ok.166.net/reunionpub/ds/kol/20210428/175636-0kba7d4gcr.jpeg")
                        .image("https://ok.166.net/reunionpub/ds/kol/20210428/175652-dnw4fv8r6s.jpeg")
                        .build()).build();
    }),
    FRIDAY("地震鲶", Command.ONMYOJI_CMD, (commands) -> {
        return CmdRes.builder().sendMsg(true)
                .msgBody(SendMsg.builder()
                        .image("https://ok.166.net/reunionpub/ds/kol/20210428/173230-9f8clqioh2.jpeg")
                        .image("https://ok.166.net/reunionpub/ds/kol/20210428/180244-key4ordji9.jpeg")
                        .image("https://ok.166.net/reunionpub/ds/kol/20210428/180304-i2hcrw1sg9.jpeg")
                        .image("https://ok.166.net/reunionpub/ds/kol/20210428/180319-c30ru789i6.jpeg")
                        .image("https://ok.166.net/reunionpub/ds/kol/20210428/180332-w40nbtea6d.jpeg")
                        .image("https://ok.166.net/reunionpub/ds/kol/20210428/180346-mwuhdvo7zn.jpeg")
                        .image("https://ok.166.net/reunionpub/ds/kol/20210428/180401-r365n8y0kl.jpeg")
                        .build()).build();
    }),
    WEEKEND("胧车", Command.ONMYOJI_CMD, (commands) -> {
        return CmdRes.builder().sendMsg(true)
                .msgBody(SendMsg.builder()
                        .image("https://ok.166.net/reunionpub/ds/kol/20210428/173239-c5d3ry6egq.jpeg")
                        .image("https://ok.166.net/reunionpub/ds/kol/20210428/180932-a9vihl8fcm.jpeg")
                        .image("https://ok.166.net/reunionpub/ds/kol/20210428/180950-3htnja6iws.jpeg")
                        .image("https://ok.166.net/reunionpub/ds/kol/20210428/181005-sl5g248y7f.jpeg")
                        .image("https://ok.166.net/reunionpub/ds/kol/20210428/181027-4th1nsm96r.jpeg")
                        .image("https://ok.166.net/reunionpub/ds/kol/20210428/181045-snky8smoli.jpeg")
                        .image("https://ok.166.net/reunionpub/ds/kol/20210428/181105-wpm7ugyasz.jpeg")
                        .build()).build();
    }),

    ;

    public String command;

    public Command commandType;

    public Function<String[], ? extends CmdRes> run;

    @Override
    public void init() {
        MemoryConst.addCommand(command, this);
    }

    @Override
    public Function<String[], ? extends CmdRes> getRun() {
        return run;
    }
}
