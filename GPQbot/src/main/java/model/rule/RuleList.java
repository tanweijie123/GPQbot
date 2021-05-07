package model.rule;

public class RuleList {
    public static final String[] FULL_RULE_LIST = new String[] {
            "Assign Job Per Party" //[0]
    };

    public static Rule getRule(int idx, String content) {
        switch (idx) {
            case 0: return new AssignJobPerParty(content);
            default: throw new UnsupportedOperationException("Not available option in Rule List");
        }
    }
}
