package com.lyle.product.generator.pool;

import com.lyle.product.generator.exception.TimeConvertException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class JdbcTemplatePool {
    private Map<Long, JdbcTemplate> jdbcTemplateMap = new ConcurrentHashMap<>();
    private Map<Long, Timestamp> expireMap = new ConcurrentHashMap<>();

    public JdbcTemplate getJdbcTemplateByConnectionId(Long connectionId) {
        JdbcTemplate jdbcTemplate = jdbcTemplateMap.get(connectionId);
        return jdbcTemplate;
    }

    public Long removeJdbcTemplateByConnectionId(Long connectionId) {
        jdbcTemplateMap.remove(connectionId);
        return connectionId;
    }

    public JdbcTemplate addJdbcTemplate(Long connectionId, JdbcTemplate jdbcTemplate, Long surviveTime, TimeUnit timeUnit) {
        jdbcTemplateMap.put(connectionId, jdbcTemplate);

        expireMap.put(connectionId, new Timestamp(getExpireTime(surviveTime, timeUnit)));

        return jdbcTemplateMap.get(connectionId);
    }


    public JdbcTemplate addJdbcTemplate(Long connectionId, JdbcTemplate jdbcTemplate, Long surviveTime) {
        return addJdbcTemplate(connectionId, jdbcTemplate, surviveTime, TimeUnit.SECONDS);
    }

    public void resetSurviveTime(Long connectionId, Long surviveTime, TimeUnit timeUnit) {
        expireMap.put(connectionId, new Timestamp(getExpireTime(surviveTime, timeUnit)));
    }

    /**
     * 连接过期维护定时任务 每 2分钟 查询一次
     */
    @Scheduled(cron = "0 0/2 * * * ?")
    private void jdbcTemplateExpireTask() {
        Set<Long> keys = expireMap.keySet();
        for (Long key : keys) {

            Timestamp timestamp = expireMap.get(key);
            if (null == timestamp) {
                jdbcTemplateMap.remove(key);
                expireMap.remove(key);
            } else if (System.currentTimeMillis() - expireMap.get(key).getTime() > 0) {
                log.info("时间到期，移除" + key + "\t" + System.currentTimeMillis() + "\t" + expireMap.get(key));
                jdbcTemplateMap.remove(key);
                expireMap.remove(key);
            }
        }

        log.info("连接池定时过期信息" + expireMap.toString());
    }


    private long convertTimeToMillSeconds(long time, TimeUnit timeUnit) {
        long timeMillSeconds = 0;
        switch (timeUnit) {
            case DAYS:
                timeMillSeconds = TimeUnit.DAYS.toMillis(time);
                break;
            case HOURS:
                timeMillSeconds = TimeUnit.HOURS.toMillis(time);
                break;
            case MINUTES:
                timeMillSeconds = TimeUnit.MINUTES.toMillis(time);
                break;
            case SECONDS:
                timeMillSeconds = TimeUnit.SECONDS.toMillis(time);
                break;
            case NANOSECONDS:
                timeMillSeconds = TimeUnit.NANOSECONDS.toMillis(time);
                break;
            case MICROSECONDS:
                timeMillSeconds = TimeUnit.MICROSECONDS.toMillis(time);
                break;
            case MILLISECONDS:
                timeMillSeconds = TimeUnit.MILLISECONDS.toMillis(time);
                break;
            default:
                throw new TimeConvertException("类型转换错误");
        }
        return timeMillSeconds;
    }

    private long getExpireTime(long surviveTime, TimeUnit timeUnit) {
        return System.currentTimeMillis() + convertTimeToMillSeconds(surviveTime, timeUnit);
    }


}
