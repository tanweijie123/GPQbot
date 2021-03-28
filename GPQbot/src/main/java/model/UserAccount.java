package model;

import data.Data;
import data.JobList;
import net.dv8tion.jda.internal.utils.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserAccount {

    private Long userId;
    private String ign;
    private int job;
    private int floor;

    public UserAccount(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return this.userId;
    }

    public String getIgn() {
        return this.ign;
    }

    public int getJob() {
        return this.job;
    }

    public int getFloor() {
        return this.floor;
    }

    public void setIgn(String ign) {
        if (ign == null || ign.contains(" "))
            return;
        this.ign = ign;
        Data.currentUserList.save();
    }

    public void setJob(int job) {
        this.job = job;
        Data.currentUserList.save();
    }

    public void setFloor(int floor) {
        this.floor = floor;
        Data.currentUserList.save();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof UserAccount)) return false;

        UserAccount oth = (UserAccount) o;
        return (oth.ign.equals(this.ign) && oth.job == this.job && oth.floor == this.floor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.ign, this.job, this.floor);
    }

    @Override
    public String toString() {
        return String.format("[%s (%s)] -> %dF", ign, (job == 0) ? "<no job added>" : JobList.FULL_JOB_LIST[job-1], floor);
    }
}
