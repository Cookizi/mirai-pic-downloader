package top.cookizi.bot.common.utils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 一个时间窗口限流器
 */
public class Limiter {

    //时间窗口长度，秒
    private long windowLength;
    //刷新频率，暂定秒
    private int refreshFrequency;

    //最大允许数量
    private int maxLimit;
    //每个key允许的数量
    private int perLimit;

    //限流器，保存数据的地方
    private Map<String, List<Date>> limiter = new ConcurrentHashMap<>();

    //定期清理超时数据
    ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    public Limiter(long windowLength, int refreshFrequency, int maxLimit, int perLimit) {
        this.windowLength = windowLength;
        this.refreshFrequency = refreshFrequency;
        this.maxLimit = maxLimit;
        this.perLimit = perLimit;
        executor.schedule(this::clear, refreshFrequency, TimeUnit.SECONDS);
    }

    public boolean isLimited(String key) {
        List<Date> list = limiter.get(key);
        if (list.isEmpty()) limiter.remove(key);

    }

    private void clear() {
        if (limiter.isEmpty()) return;
        Date now = new Date();
        limiter.keySet().forEach(k -> {
            List<Date> list = limiter.get(k);
            list.removeIf(x -> x.before(now));
            if (list.isEmpty())
                limiter.remove(k);
        });
    }
}
