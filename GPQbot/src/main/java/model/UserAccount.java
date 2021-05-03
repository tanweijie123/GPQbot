package model;

import data.JobList;

public class UserAccount {

    private String guildId;
    private String userId;
    private int job;
    private int floor;

    public UserAccount(String guildId, String userId, int job, int floor) {
        this.guildId = guildId;
        this.userId = userId;
        this.job = job;
        this.floor = floor;
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

    public String replyString(String ign) {
        return String.format("[%s (%s)] -> %dF", ign, (job == 0) ? "<no job added>" : JobList.FULL_JOB_LIST[job-1], floor);
    }

    public String gpqString(String ign) {
        return String.format("%s/%s/%d", ign, (job == 0) ? "" : JobList.FULL_JOB_LIST[job-1], floor);
    }
}
