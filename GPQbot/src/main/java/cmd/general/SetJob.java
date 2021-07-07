package cmd.general;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import config.Settings;
import data.JobList;
import logic.UsersMethod;
import model.UserAccount;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

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

    public void execute(SlashCommandEvent event) {
        long job = event.getOption("job_number").getAsLong();
        if (job < 1 || job > JobList.FULL_JOB_LIST.length) {
            event.reply("Oof.. Invalid number..").queue();
            return;
        }

        UsersMethod.getOrCreateUser(event.getGuild().getId(), event.getUser().getId());
        UsersMethod.updateJob(event.getGuild().getId(), event.getUser().getId(), Math.toIntExact(job));
        UserAccount ua = UsersMethod.getOrCreateUser(event.getGuild().getId(), event.getUser().getId());

        event.reply("Hellonyaa~ This is what I have of you:\n" + ua.replyString(event.getMember().getEffectiveName())).setEphemeral(true).queue();
    }
}
