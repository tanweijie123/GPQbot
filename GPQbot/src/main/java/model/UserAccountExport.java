package model;

public class UserAccountExport extends UserAccount {
    private String ign;

    public UserAccountExport(String guildId, String userId, int job, int floor, boolean registered, String ign) {
        super(guildId, userId, job, floor, registered);
        this.ign = ign;
    }

    public String getIgn() {
        return ign;
    }
}
