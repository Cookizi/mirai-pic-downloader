package top.cookizi.bot.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import top.cookizi.bot.common.enums.command.ICommand;
import top.cookizi.bot.common.enums.command.OnmyojiCmd;
import top.cookizi.bot.modle.domain.CmdRes;
import top.cookizi.bot.modle.domain.SendMsg;
import top.cookizi.bot.service.MiraiApiService;
import top.cookizi.bot.service.MsgSendService;

import java.util.Calendar;

import static java.util.Calendar.DAY_OF_WEEK;

/**
 * @author heq
 * @date 2021/5/13 4:55 下午
 * @description
 */
@Component
@Slf4j
public class OnmyojiSchedule {

    @Autowired
    private MsgSendService sendService;
    @Autowired
    private MiraiApiService apiService;

    public static final Long[] GROUP_NUM = new Long[]{618701394L};

    @Scheduled(cron = "0 0 17 * * ? ")
    public void fMTime() {
        ICommand command;
        switch (getDayOfWeek()) {
            case Calendar.MONDAY:
                command = OnmyojiCmd.MONDAY;
                break;
            case Calendar.TUESDAY:
                command = OnmyojiCmd.TUESDAY;
                break;
            case Calendar.WEDNESDAY:
                command = OnmyojiCmd.WEDNESDAY;
                break;
            case Calendar.THURSDAY:
                command = OnmyojiCmd.THURSDAY;
                break;
            case Calendar.FRIDAY:
                command = OnmyojiCmd.FRIDAY;
                break;
            case Calendar.SATURDAY:
            case Calendar.SUNDAY:
                command = OnmyojiCmd.WEEKEND;
                break;
            default:
                return;
        }
        CmdRes cmdRes = command.getRun().apply(new String[]{});
        SendMsg sendMsg = cmdRes.getMsgBody();
        sendMsgToGroupList(sendMsg);

    }

    @Scheduled(cron = "0 0 12,20 * * ? ")
    public void battle(){
        sendMsgToGroupList(SendMsg.TextMsg("斗技开始"));
    }

    @Scheduled(cron = "0 0 19 * * ? ")
    public void yJTime(){
        String msg = "";
        switch (getDayOfWeek()) {
            case Calendar.MONDAY:
                msg="狩猎战开始，今日：火麒麟";
                break;
            case Calendar.TUESDAY:
                msg="狩猎战开始，今日：风麒麟";
                break;
            case Calendar.WEDNESDAY:
                msg="狩猎战开始，今日：水麒麟";
                break;
            case Calendar.THURSDAY:
                msg="狩猎战开始，今日：雷麒麟";
                break;
            case Calendar.FRIDAY:
            case Calendar.SATURDAY:
            case Calendar.SUNDAY:
                msg="阴界之门开始";
                break;
            default:
                return;
        }
        sendMsgToGroupList(SendMsg.TextMsg(msg));
    }

    @Scheduled(cron = "0 45,55 20 * * ? ")
    public void yJEnd(){
        String msg = "";
        switch (getDayOfWeek()) {
            case Calendar.FRIDAY:
            case Calendar.SATURDAY:
            case Calendar.SUNDAY:
                msg="阴界之门即将关闭，没刷的赶紧刷";
                break;
            default:
                return;
        }
        sendMsgToGroupList(SendMsg.builder().text(msg).atAll().build());
    }

    @Scheduled(cron = "0 45 21 * * ? ")
    public void fMEnd(){
        String msg = "";
        switch (getDayOfWeek()) {
            case Calendar.FRIDAY:
            case Calendar.SATURDAY:
            case Calendar.SUNDAY:
                msg="逢魔之时即将关闭，没刷的赶紧刷";
                break;
            default:
                return;
        }
        sendMsgToGroupList(SendMsg.TextMsg(msg));
    }

    private int getDayOfWeek(){
        Calendar calendar = Calendar.getInstance();
        return calendar.get(DAY_OF_WEEK);
    }

    private void sendMsgToGroupList(SendMsg sendMsg){
        apiService.resetSession();
        for(Long num:GROUP_NUM){
            sendMsg.setTarget(num);
            sendService.sendToGroup(sendMsg);
        }
    }


}
