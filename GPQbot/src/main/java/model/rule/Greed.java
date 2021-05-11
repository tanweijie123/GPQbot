package model.rule;

import model.alloc.Team;

import java.util.Arrays;

public class Greed extends Rule {

    /** content style: 1,1 */
    private String content; //content specifies which tunnel and which team to allocate all. If team is not found, ignore;

    public Greed(String content) {
        this.content = content;

        super.func = (participantList, tunnelList) -> {
            try {
                String[] tunnelTeam = content.trim().split(",");
                int tunnelNo = Integer.parseInt(tunnelTeam[0]) - 1;
                int teamNo = Integer.parseInt(tunnelTeam[1]);

                Team team = null;
                switch (teamNo) {
                    case 1:
                        team = tunnelList.get(tunnelNo).getTeam1();
                        break;
                    case 2:
                        team = tunnelList.get(tunnelNo).getTeam2();
                        break;
                    case 3:
                        team = tunnelList.get(tunnelNo).getTeam3();
                        break;
                }

                while (!team.isFull() && participantList.size() > 0) {
                    team.addMember(participantList.remove(0));
                }
            } catch (NumberFormatException nfe) {
                System.err.println("Error in parsing tunnelNo or teamNo");
                System.err.println(Arrays.toString(nfe.getStackTrace()));
            } catch (NullPointerException e) {
                System.err.println("NullPointerExeception");
                System.err.println(Arrays.toString(e.getStackTrace()));
            }
        };
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "Greed --> " + content;
    }
}
