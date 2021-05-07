package logic;

import db.SQLFunctions;
import model.rule.Rule;
import model.rule.RuleList;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RulesMethod {

    /**
     * Retrieve the set of rules for assigning members in excel.
     * @param guildId The rules of the guild to look up from
     * @return Returns a list of rules ordered by the priority stored in db.
     */
    @NotNull
    public static List<Rule> getRules(String guildId) {
        try {
            PreparedStatement stmt = SQLFunctions.getRules();
            stmt.setString(1, guildId);
            ResultSet resultSet = stmt.executeQuery();

            List<Rule> ruleList = new ArrayList<>();
            while (resultSet.next()) {
                ruleList.add(RuleList.getRule(resultSet.getInt("idx"), resultSet.getString("content")));
            }

            stmt.close();
            return ruleList;
        } catch (SQLException ex) {
            System.err.println("Error in performing function getRules()");
            System.err.println("SQLException: " + ex.getMessage());
            return new ArrayList<>();
        }
    }
}
