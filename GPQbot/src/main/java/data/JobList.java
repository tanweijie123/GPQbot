package data;

import java.util.Arrays;

public class JobList {
    public static final String[] FULL_JOB_LIST = new String[] {
            "ANIMA: Ho Young",
            "CHILD OF GOD: Zero",
            "CYGNUS: Flame Wizard",
            "CYGNUS: Soul Master",
            "CYGNUS: Mihile",
            "CYGNUS: Night Walker",
            "CYGNUS: Striker",
            "CYGNUS: Wind Breaker",
            "EXPLORER: Bow Master",
            "EXPLORER: Marksman",
            "EXPLORER: Pathfinder",
            "EXPLORER: Bishop",
            "EXPLORER: Fire Poison",
            "EXPLORER: Ice Lightning",
            "EXPLORER: Viper",
            "EXPLORER: Captain",
            "EXPLORER: Cannon Master",
            "EXPLORER: Dual Blade",
            "EXPLORER: Night Lord",
            "EXPLORER: Shadower",
            "EXPLORER: Dark Knight",
            "EXPLORER: Hero",
            "EXPLORER: Paladin",
            "FLORA: Adele",
            "FLORA: Ark",
            "FLORA: Illium",
            "HERO: Aran",
            "HERO: Evan",
            "HERO: Luminous",
            "HERO: Mercedes",
            "HERO: Phantom",
            "HERO: Eunwol",
            "NOVA : Cadena",
            "NOVA: Angelic Buster",
            "NOVA: Kaiser",
            "RESISTANCE: Battle Mage",
            "RESISTANCE: Blaster",
            "RESISTANCE: Citizen",
            "RESISTANCE: Demon Avenger",
            "RESISTANCE: Demon Slayer",
            "RESISTANCE: Mechanic",
            "RESISTANCE: Wild Hunter",
            "RESISTANCE: Xenon",
            "SENGOKU: Hayato",
            "SENGOKU: Kanna",
            "SPECIAL: Kinesis"
    };

    public static String getJobListWithNumber_OneBased() {
        String output = "";

        for (int i = 1; i <= FULL_JOB_LIST.length; i++) {
            output += "[" + i + "] " + FULL_JOB_LIST[i-1] + "\n";
        }
        return output;
    }

    public static int getIndexByJob_OneBased(String jobToSearch) {
        for (int i = 0; i < FULL_JOB_LIST.length; i++) {
            if (FULL_JOB_LIST[i].equalsIgnoreCase(jobToSearch))
                return i + 1;
        }
        return -1;
    }
}
