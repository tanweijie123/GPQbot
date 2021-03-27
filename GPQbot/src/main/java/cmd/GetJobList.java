package cmd;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import data.JobList;

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
                .complete()
                .delete().queueAfter(60, TimeUnit.SECONDS);
    }
}
