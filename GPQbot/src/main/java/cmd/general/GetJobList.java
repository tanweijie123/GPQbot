package cmd.general;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import data.JobList;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import java.util.concurrent.TimeUnit;

public class GetJobList extends Command {
    public GetJobList() {
        super.name = "joblist";
        super.help = "Gets the list of job";
    }

    @Override
    protected void execute(CommandEvent event) {
        event.getMessage().delete().queue();
        event.getChannel()
                .sendMessage(JobList.getJobListWithNumber_OneBased() + "\nThis message will be deleted after 60 seconds to prevent clogging.")
                .queue(x -> x.delete().queueAfter(60, TimeUnit.SECONDS));
    }

    public void execute(SlashCommandEvent event) {
        event.reply(JobList.getJobListWithNumber_OneBased()).setEphemeral(true).queue(); //slash-command cannot be deleted from bot.
    }
}
