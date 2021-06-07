package top.cookizi.bot.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import top.cookizi.bot.service.MiraiApiService;

/**
 * @author heq
 * @date 2021/6/7 5:57 下午
 * @description
 */
@Component
@Slf4j
public class CommonSchedule {

    @Autowired
    private MiraiApiService apiService;

    /**
     * 每小时刷新一次session
     */
    @Scheduled(cron = "0 1 * * * ? ")
    public void resetSession() {
        apiService.resetSession();
    }
}
