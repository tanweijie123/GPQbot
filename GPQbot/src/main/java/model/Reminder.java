package model;

import util.Utility;

import java.time.ZonedDateTime;

public class Reminder {
    public final int id;
    public final String gid;
    public final String uid;
    public final ZonedDateTime createdDate;
    public final ZonedDateTime expectedDate;
    public final String reminderLink;

    /*
        id = 0 if it is a new reminder
     */
    public Reminder(int id, String gid, String uid, ZonedDateTime createdDate, ZonedDateTime expectedDate,
                    String reminderLink) {
        this.id = id;
        this.gid = gid;
        this.uid = uid;
        this.createdDate = createdDate;
        this.expectedDate = expectedDate;
        this.reminderLink = reminderLink;
    }

    @Override
    public String toString() {
        return String.format("(%d, %s, %s, %s, %s, %s)", this.id, this.gid, this.uid,
                Utility.toDateTimeString(createdDate), Utility.toDateTimeString(expectedDate), this.reminderLink);
    }

}
