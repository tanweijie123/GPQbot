package cmd.mod;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import config.Settings;

public class AsMod extends Command {

    public AsMod() {
        super.name = "mod";
        super.help = "execute commands appended as mod";
        super.requiredRole = Settings.BOT_MOD_NAME;
        super.hidden = true;
        super.children = new Command[] {new AsModShowMember(), new AsModSetFloor() };
    }

    @Override
    protected void execute(CommandEvent event) {
    }
}
