package model.alloc;

import model.UserAccountExport;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Team {
    private List<UserAccountExport> teamList;

    public Team() {
        teamList = new ArrayList<>();
    }

    public UserAccountExport get(int index) {
        return teamList.get(index);
    }

    public List<UserAccountExport> getTeamList() {
        return teamList;
    }

    public boolean addMember(UserAccountExport c) {
        if (c == null) return false;

        if (teamList.size() < 6) {
            teamList.add(c);
            return true;
        } else
            return false;
    }

    public boolean isFull() {
        return teamList.size() == 6;
    }

    public int size() {
        return teamList.size();
    }

    /**
     * Sort from highest to lowest floor for this team, if same floor level, sort by IGN.
     */
    public void sortByFloor() {
        teamList.sort( (UserAccountExport x, UserAccountExport y) -> {
            int compare = Integer.compare(y.getFloor(),x.getFloor());
            if (compare != 0) return compare;
            return x.getIgn().compareTo(y.getIgn());
        });
    }

    @Override
    public String toString() {
        String ret = "[LEADER] ";

        for (int i = 0; i < teamList.size(); i++) {
            ret += teamList.get(i).getIgn() + "[" + teamList.get(i).getFloor() + "]\n";
        }
        return ret;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Team)) return false;

        Team oth = (Team) o;
        return (oth.teamList.equals(this.teamList));
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.teamList);
    }
}
