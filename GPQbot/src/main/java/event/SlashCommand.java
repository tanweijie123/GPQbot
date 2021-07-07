package event;

import cmd.Help;
import cmd.general.GetJobList;
import cmd.general.SetFloor;
import cmd.general.SetJob;
import cmd.general.WhoAmI;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class SlashCommand extends ListenerAdapter {
    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        if (event.getGuild() == null) return;

        switch (event.getName()) {
            case "help":
                new Help().execute(event);
                break;
            case "whoami":
                new WhoAmI().execute(event);
                break;
            case "joblist":
                new GetJobList().execute(event);
                break;
            case "floor":
                new SetFloor().execute(event);
                break;
            case "job":
                new SetJob().execute(event);
                break;
            default: event.reply("I can't handle that command right now :(").setEphemeral(true).queue();
        }

    }
}
