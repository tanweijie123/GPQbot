package model.rule;

public class AssignJobPerParty extends Rule {
    private String job;

    public AssignJobPerParty(String job) {
        this.job = job;
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
