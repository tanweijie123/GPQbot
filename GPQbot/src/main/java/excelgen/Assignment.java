package excelgen;

import model.UserAccountExport;
import model.alloc.Team;
import model.alloc.Tunnel;
import model.rule.Rule;
import model.rule.RuleList;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Assignment {
    private static List<UserAccountExport> participantList = new ArrayList<>();
    private static List<Tunnel> tunnelList = new ArrayList<>();

    public static void loadParticipants(List<UserAccountExport> uaeList) {
        participantList = uaeList;
    }

    public static void loadTunnel() {
        int numParty = (int) Math.ceil(participantList.size() / 6.0);
        int numTunnel = (int) Math.ceil(numParty / 3.0);

        for (int i = 1; i <= numTunnel; i++) {
            Tunnel t = new Tunnel(i);
            tunnelList.add(t);
        }
    }

    public static void compactEveryTunnel() {
        tunnelList.stream().forEach(Tunnel::compact);
    }

    public static void exportToExcel(String exportPath) {
        tunnelList.sort(Comparator.comparingInt(x -> x.getId()));
        List<String> export = new ArrayList<>();

        export.add(",,Generated at: " + DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss").format(LocalDateTime.now()));

        for (int i = 0; i < tunnelList.size(); i++) {
            Tunnel t = tunnelList.get(i);
            t.sortByFloor();

            //Print header
            export.add(",,,Tunnel " + t.getId() + ",,,");
            export.add(",Team1,Floor,Team2,Floor,Team3,Floor");

            for (int j = 0; j < 6; j++) {
                String join = ",";

                if (t.getTeam1().size() > j) {
                    join += t.getTeam1().get(j).getIgn() + "," + t.getTeam1().get(j).getFloor() + ",";
                } else {
                    join += ",,";
                }

                if (t.getTeam2().size() > j) {
                    join += t.getTeam2().get(j).getIgn() + "," + t.getTeam2().get(j).getFloor() + ",";
                } else {
                    join += ",,";
                }

                if (t.getTeam3().size() > j) {
                    join += t.getTeam3().get(j).getIgn() + "," + t.getTeam3().get(j).getFloor() + ",";
                } else {
                    join += ",,";
                }

                export.add(join);
            }
            export.add("");
            export.add("");
        }


        //---------------------------------------------------------------------------------------------

        ExportExcel excelExport = new ExportExcel(exportPath, true);
        String[][] arr = export.stream().map(x -> x.split(",")).toArray(String[][]::new);
        excelExport.export(arr, "");
    }


    /** ASSIGNMENT METHODS */


    /**
     * Default assignment is specified as:
     *    - UserAccountExports are added into tunnel from the highest floor to lowest floor
     */
    public static void startDefaultAssignment() {
        int tunnelID = 1;
        while (participantList.size() > 0) {
            assignTunnel(tunnelList, participantList, tunnelID);
            tunnelID++;
        }
    }

    public static void runRule(Rule rule) {
        RuleList.getFunction(rule).accept(participantList, tunnelList);
    }

    /** HELPER METHODS */

    private static void assignTunnel(List<Tunnel> tunnelList, List<UserAccountExport> charList, int tunnelID) {
        Tunnel t = tunnelList.get(tunnelID - 1);

        while (!t.isFull() && charList.size() > 0) {
            Team team = t.getLowestTeam();
            team.addMember(charList.remove(0));
        }
    }
}
