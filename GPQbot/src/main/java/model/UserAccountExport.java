package model;

import data.JobList;

public class UserAccountExport extends UserAccount {
    private String ign;

    public UserAccountExport(String guildId, String userId, int job, int floor, boolean registered, String ign) {
        super(guildId, userId, job, floor, registered);
        this.ign = ign;
    }

    public String getIgn() {
        return ign;
    }

    @Override
    public String toString() {
        return String.format("[%s (%s)] -> %dF", ign, (job == 0) ? "<no job added>" : JobList.FULL_JOB_LIST[job-1], floor);
    }
}
