package model.alloc;

import model.UserAccountExport;

import java.util.List;
import java.util.Objects;

/**
 * Creates a immutable Tunnel
 */
public class Tunnel {
    private final int id;
    private final Team team1;
    private final Team team2;
    private final Team team3;

    public Tunnel(int id) {
        this.id = id;
        this.team1 = new Team();
        this.team2 = new Team();
        this.team3 = new Team();
    }

    public Tunnel(int id, Team t1, Team t2, Team t3) {
        this.id = id;
        this.team1 = t1;
        this.team2 = t2;
        this.team3 = t3;
    }

    public int getId() {
        return this.id;
    }

    public Team getTeam1() {
        return team1;
    }

    public Team getTeam2() {
        return team2;
    }

    public Team getTeam3() {
        return team3;
    }

    public Team[] getAllTeam() {
        return new Team[] { team1, team2, team3 };
    }

    /**
     * Gets the team with the lowest number of participants. If there is a tie, the first team found will be returned.
     */
    public Team getLowestTeam() {
        int min = Math.min(Math.min(team1.size(), team2.size()), team3.size());
        if (min == team1.size()) {
            return team1;
        } else if (min == team2.size()) {
            return team2;
        } else {
            return team3;
        }
    }

    /**
     * Sort from highest to lowest floor for every team
     */
    public void sortByFloor() {
        team1.sortByFloor();
        team2.sortByFloor();
        team3.sortByFloor();
    }

    public boolean isFull() {
        return team1.isFull() && team2.isFull() && team3.isFull();
    }

    /**
     * Reorganise the tunnel (compact) if every team is not full.
     * Tries to shrink as many half parties as possible.
     */
    public void compact() {
        if (!isFull()) {
            int capacity = team1.size() + team2.size() + team3.size();
            int numTeams = (int) Math.ceil(capacity / 6.0);

            assert(numTeams >= 0 && numTeams <= 3);

            if (numTeams == 2) { //can be shrink into 1 or 2 parties
                List<UserAccountExport> list = team3.getTeamList();
                while (list.size() > 0) {
                    if (team1.size () > team2.size())
                        team2.addMember(list.remove(0));
                    else
                        team1.addMember(list.remove(0));
                }
            } else if (numTeams == 1) {
                List<UserAccountExport> list = team3.getTeamList();
                while (list.size() > 0) {
                    team1.addMember(list.remove(0));
                }
                list = team2.getTeamList();
                while (list.size() > 0) {
                    team1.addMember(list.remove(0));
                }
            }
        }
    }

    @Override
    public String toString() {
       return String.format("Team 1:\n%s\nTeam2:\n%s\nTeam3:\n%s", team1.toString(), team2.toString(), team3.toString());
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Tunnel)) return false;

        Tunnel oth = (Tunnel) o;
        return (oth.id == this.id && oth.team1.equals(this.team1) && oth.team2.equals(this.team2)
                && oth.team3.equals(this.team3));
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.team1, this.team2, this.team3);
    }
}
