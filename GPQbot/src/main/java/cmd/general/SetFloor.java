package cmd.general;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import config.Settings;
import data.Data;
import model.UserAccount;

public class SetFloor extends Command {

    public SetFloor() {
        super.name = "floor";
        super.help = "Update your dojo floor";
        super.arguments = "[floor_number]";
    }

    @Override
    protected void execute(CommandEvent event) {
        if (event.getArgs().isEmpty()) {
            event.reply("Huhh? Where is your floor? (Eg. \"" + Settings.botCommand.getPrefix() +"floor 50\")");
            return;
        }

        long userID = event.getAuthor().getIdLong();
        UserAccount ua = Data.currentUserList.getByUserKey(userID, event.getMember().getEffectiveName());

        if (ua == null) {
            event.reply("Have I seen you before? " +
                    "Try \"" + Settings.botCommand.getPrefix() + "whoami\" if this is the first time you're talking to me nyaa~\n");
        } else {
            try {
                int floor = Integer.parseInt(event.getArgs().split(" ")[0]);
                if (floor < 0) {
                    event.reply("Oof.. You can't even beat a snail?");
                } else if (floor > 70) {
                    event.reply("Woahh.. You are strong. Too strong in fact.");
                } else {
                    ua.setFloor(floor);
                    event.reply("Hellonyaa~ This is what I have of you:\n" + ua.toString());
                }
            } catch (NumberFormatException ev) {
                event.reply("Huhh? Your floor is not a number? Idk how to use it nyaa (Eg. \"" + Settings.botCommand.getPrefix() + "floor 50\")");
            }
        }
    }
}