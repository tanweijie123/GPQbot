package model;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

public class GPQParticipation {
    private long guildId;
    private String dateTime;
    private int score;
    private List<UserAccount> participantList;

    public GPQParticipation(long guildId, ZonedDateTime dateTime, List<UserAccount> participantList) {
        this.guildId = guildId;
        this.dateTime = dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        this.score = 0;
        this.participantList = participantList;
    }

    public long getGuildId() {
        return guildId;
    }

    public ZonedDateTime getDateTime() {
        return ZonedDateTime.parse(dateTime, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss").withZone(ZoneId.of("GMT+8")));
    }

    public List<UserAccount> getParticipantList() {
        return new ArrayList<>(participantList);
    }

    public int getScore() {
        return score;
    }
}
