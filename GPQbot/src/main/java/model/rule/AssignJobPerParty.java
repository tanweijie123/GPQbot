package model.rule;

import data.JobList;
import model.UserAccountExport;
import model.alloc.Team;

import java.util.List;
import java.util.stream.Collectors;

public class AssignJobPerParty extends Rule {
    private String job;

    public AssignJobPerParty(String job) {
        this.job = job;
        final int JOB_NUMBER = JobList.getIndexByJob_OneBased(job);
        super.func = (participantList, tunnelList) -> {
            List<UserAccountExport> concernedList = participantList
                    .stream()
                    .filter(x -> x.getJob() == JOB_NUMBER)
                    .collect(Collectors.toList());

            for (int i = 0; i < tunnelList.size(); i++) {
                Team[] team = tunnelList.get(i).getAllTeam();

                for (int j = 0; j < 3 ; j++) {
                    if (concernedList.size() > 0 && !team[j].isFull()) {
                        UserAccountExport c = concernedList.remove(0);
                        team[j].addMember(c);
                        participantList.remove(c);
                    }
                }
            }
        };
    }

    @Override
    public String getContent() {
        return job;
    }

    @Override
    public String toString() {
        return "Assign Job Per Party --> " + job;
    }

}
