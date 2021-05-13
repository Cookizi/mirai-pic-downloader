package top.cookizi.bot.common.utils;

import lombok.Builder;
import lombok.Data;

import java.util.Calendar;

/**
 * @author heq
 * @date 2021/4/26 3:05 下午
 * @description
 */
public class TimeUtils {

    public static final long MILLIS_PER_SECOND = 1000L;
    public static final long MILLIS_PER_MINUTE = 60 * MILLIS_PER_SECOND;
    public static final long MILLIS_PER_HOUR = 60 * MILLIS_PER_MINUTE;
    public static final long MILLIS_PER_DAY = 24 * MILLIS_PER_HOUR;
    public static final long DAY_PRE_WEEK = 7L;

    /**
     * 中国周，周一是开始，和Calendar的西方周不一样
     */
    public final static int MONDAY = 1;
    public final static int TUESDAY = 2;
    public final static int WEDNESDAY = 3;
    public final static int THURSDAY = 4;
    public final static int FRIDAY = 5;
    public final static int SATURDAY = 6;
    public final static int SUNDAY = 7;

    /**
     * 获取相对当前周的 第n周 时间（第n周:周一0点 至 第n+1周:周一0点）
     * <p>
     * 以 周几几点 为更新周期，如果当前周为第一周
     * 周一0点至 周几几点 返回 第n-1周 的时间范围，
     * 周几几点 至 周日24点 返回 第n周 的时间范围
     *
     * @param dateTimestamp  要处理的时间
     * @param dayOfWeek      更新周期所在的天
     * @param clockTimestamp 更新周期当天的时间点（以距离0点为准的时间戳）
     * @param weekOrder      第几周 1 是第一周（本周）
     */
    public static Week getOrderWeek(long dateTimestamp, int dayOfWeek, long clockTimestamp, int weekOrder) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dateTimestamp
                + MILLIS_PER_DAY * ((weekOrder - 1) * DAY_PRE_WEEK - dayOfWeek) - clockTimestamp);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        long startTime = calendar.getTimeInMillis();
        long endTime = startTime + MILLIS_PER_DAY * 7;

        return Week.builder()
                .year(calendar.get(Calendar.YEAR))
                .startOfWeek(startTime)
                .endOfWeek(endTime)
                .build();
    }

    @Data
    @Builder
    public static class Week {

        private int year;

        private long startOfWeek;

        private long endOfWeek;
    }
}
