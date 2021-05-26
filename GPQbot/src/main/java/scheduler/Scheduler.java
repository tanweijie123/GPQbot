package scheduler;

import net.dv8tion.jda.api.JDA;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Scheduler {
    public static void init(JDA jda) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(5);
        scheduleFlagAlert(scheduler, jda);
        scheduleClearMessage(scheduler, jda);
    }

    private static void scheduleFlagAlert(ScheduledExecutorService scheduler, JDA jda) {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("GMT+8"));
        ZonedDateTime alarm0 = ZonedDateTime.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth(), 11, 55, 0, 0, ZoneId.of("GMT+8"));
        ZonedDateTime alarm1 = ZonedDateTime.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth(), 18, 55, 0, 0, ZoneId.of("GMT+8"));
        ZonedDateTime alarm2 = ZonedDateTime.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth(), 20, 55, 0, 0, ZoneId.of("GMT+8"));

        if (alarm0.isBefore(now)) {
            alarm0 = alarm0.plusDays(1);
        }
        if (alarm1.isBefore(now)) {
            alarm1 = alarm1.plusDays(1);
        }
        if (alarm2.isBefore(now)) {
            alarm2 = alarm2.plusDays(1);
        }

        scheduler.scheduleAtFixedRate(FlagAlert.alarmForFlag(jda, 0), Duration.between(now, alarm0).toSeconds(), 86400, TimeUnit.SECONDS);
        scheduler.scheduleAtFixedRate(FlagAlert.alarmForFlag(jda, 1), Duration.between(now, alarm1).toSeconds(), 86400, TimeUnit.SECONDS);
        scheduler.scheduleAtFixedRate(FlagAlert.alarmForFlag(jda, 2), Duration.between(now, alarm2).toSeconds(), 86400, TimeUnit.SECONDS);
    }

    private static void scheduleClearMessage(ScheduledExecutorService scheduler, JDA jda) {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("GMT+8"));
        ZonedDateTime alarm0 = ZonedDateTime.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth(), 0, 0, 0, 0, ZoneId.of("GMT+8"));
        if (alarm0.isBefore(now)) {
            alarm0 = alarm0.plusDays(1);
        }
        scheduler.scheduleAtFixedRate(ClearMessage.clearAfterMessage(jda), Duration.between(now, alarm0).toSeconds(), 86400, TimeUnit.SECONDS);
    }
}
