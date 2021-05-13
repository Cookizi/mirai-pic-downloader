package top.cookizi.bot.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
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
public class CommonSchedule {

    @Autowired
    private MsgSendService sendService;

    @Scheduled(cron = "0 35 17 * * ? ")
    public void first() {
        log.info("start");
        Calendar calendar = Calendar.getInstance();
        String msg = null;
        switch (calendar.get(DAY_OF_WEEK)) {
            case Calendar.SUNDAY:
                ;
            case Calendar.MONDAY:
                ;
            case Calendar.TUESDAY:
                ;
            case Calendar.WEDNESDAY:
                ;
            case Calendar.THURSDAY:
                msg = "自动消息测试:今日逢魔：荒骷髅"
                ;
            case Calendar.FRIDAY:
                ;
            case Calendar.SATURDAY:
                ;
            default:
        }
        sendService.sendTextToGroup(618701394, msg);

    }
}
