package cmd.general;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import config.Settings;
import data.Data;
import model.UserAccount;
import net.dv8tion.jda.internal.utils.tuple.MutablePair;

public class SetJob extends Command {

    public SetJob() {
        super.name = "job";
        super.help = "Update your job";
        super.arguments = "[job_number]";
    }

    @Override
    protected void execute(CommandEvent event) {
        if (event.getArgs().isEmpty()) {
            event.reply("Huhh? Where is your job? (Eg. \"" + Settings.botCommand.getPrefix() +"job 1\" for Ho Young)");
            return;
        }

        long userID = event.getAuthor().getIdLong();
        UserAccount ua = Data.currentUserList.getByUserKey(userID, event.getMember().getEffectiveName());

        if (ua == null) {
            ua = new UserAccount(userID);
            Data.currentUserList.add(new MutablePair<>(userID, ua));
            ua.setIgn(event.getMember().getEffectiveName());
        }

        try {
            int job = Integer.parseInt(event.getArgs().split(" ")[0]);
            if (job < 1 || job > 46) {
                event.reply("Oof.. Invalid number..");
            } else {
                ua.setJob(job);
                event.reply("Hellonyaa~ This is what I have of you:\n" + ua.toString());
            }
        } catch (NumberFormatException ev) {
            event.reply("Huhh? I only accept numbers. (Eg. \"" + Settings.botCommand.getPrefix() + "job 1\" for Ho Young)");
        }
    }
}
