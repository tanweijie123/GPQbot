package cmd.general;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import config.Settings;
import data.JobList;
import logic.UsersMethod;
import model.UserAccount;

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

        int job = 0;
        try {
            job = Integer.parseInt(event.getArgs().split(" ")[0]);
            if (job < 1 || job > JobList.FULL_JOB_LIST.length) {
                event.reply("Oof.. Invalid number..");
                return;
            }
        } catch (NumberFormatException ev) {
            event.reply("Huhh? I only accept numbers. (Eg. \"" + Settings.botCommand.getPrefix() + "job 1\" for Ho Young)");
            return;
        }

        //TODO; maybe change logic flow
        UsersMethod.getOrCreateUser(event.getGuild().getId(), event.getAuthor().getId());
        UsersMethod.updateJob(event.getGuild().getId(), event.getAuthor().getId(), job);
        UserAccount ua = UsersMethod.getOrCreateUser(event.getGuild().getId(), event.getAuthor().getId());

        event.reply("Hellonyaa~ This is what I have of you:\n" + ua.replyString(event.getMember().getEffectiveName()));
    }
}
