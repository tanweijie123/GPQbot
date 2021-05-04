package model;

import data.JobList;

public class UserAccount {

    private final String guildId;
    private final String userId;
    private final int job;
    private final int floor;
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

    public String replyString(String ign) {
        return String.format("[%s (%s)] -> %dF", ign, (job == 0) ? "<no job added>" : JobList.FULL_JOB_LIST[job-1], floor);
    }

    public String gpqString(String ign) {
        return String.format("%s/%s/%d", ign, (job == 0) ? "" : JobList.FULL_JOB_LIST[job-1], floor);
    }
}
