package model.rule;

import model.UserAccountExport;
import model.alloc.Tunnel;

import java.util.List;
import java.util.function.BiConsumer;

public abstract class Rule {
    BiConsumer<List<UserAccountExport>, List<Tunnel>> func;

    public abstract String getContent();
    public BiConsumer<List<UserAccountExport>, List<Tunnel>> getFunc() {
        return func;
    }
}
