package excelgen;

import model.UserAccountExport;
import model.rule.Rule;

import java.io.File;
import java.util.List;

public class Generator {
    private List<UserAccountExport> uaeList;
    private List<Rule> ruleList;
    private String guildId;
    private File filepath;

    public Generator(List<UserAccountExport> uaeList, List<Rule> ruleList, String guildId, File filepath) {
        this.uaeList = uaeList;
        this.ruleList = ruleList;
        this.guildId = guildId;
        this.filepath = filepath;
    }

    public boolean generate() {
        Assignment.loadParticipants(uaeList);
        Assignment.loadTunnel();

        for (Rule rule : ruleList) {
            Assignment.runRule(rule);
        }

        Assignment.startDefaultAssignment();
        Assignment.compactEveryTunnel();
        Assignment.exportToExcel(filepath.getPath());
        return false;
    }
}
