package model;

import data.JobList;

import java.util.Objects;

/**
 * UserAccount is an immutable class which carry a set of information pertaining to 1 user.
 */
public class UserAccount {

    private final String guildId;
    private final String userId;
    protected final int job; //1-based. 0 refers to no job assigned
    protected final int floor;
    private final boolean registered;

    public UserAccount(String guildId, String userId, int job, int floor, boolean registered) {
        this.guildId = guildId;
        this.userId = userId;
        this.job = job;
        this.floor = floor;
        this.registered = registered;
    }

    public String getGuildId() {
        return guildId;
    }

    public String getUserId() {
        return this.userId;
    }

    public int getJob() {
        return this.job;
    }

    public int getFloor() {
        return this.floor;
    }

    public boolean isRegistered() {
        return registered;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof UserAccount)) return false;

        UserAccount oth = (UserAccount) o;
        return (oth.guildId.equals(this.guildId) && oth.userId.equals(this.userId)
                && oth.job == this.job && oth.floor == this.floor && oth.registered == this.registered);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.guildId, this.userId, this.job, this.floor, this.registered);
    }

    public String replyString(String ign) {
        return String.format("[%s (%s)] -> %dF", ign, (job == 0) ? "<no job added>" : JobList.FULL_JOB_LIST[job-1], floor);
    }

    public String gpqString(String ign) {
        return String.format("%s/%s/%d", ign, (job == 0) ? "" : JobList.FULL_JOB_LIST[job-1], floor);
    }
}
