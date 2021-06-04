package event;

import config.Settings;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.AttachmentOption;

import java.io.File;
import java.util.List;

public class NotesEvent extends ListenerAdapter {

    /*
        Notes commands starts with "//":
          //gpqnote

     */

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot() || !event.getMessage().getContentRaw().startsWith("//")) {
            return;
        }
        event.getMessage().delete().queue();
        String command = event.getMessage().getContentRaw().substring(2);

        if (command.equalsIgnoreCase("gpqnote")) {
            runGpqNote(event);
            return;
        }
    }

    private void runGpqNote(GuildMessageReceivedEvent event) {
        File file = new File("NotesEvent/gpqnote.png");
        event.getChannel().sendFile(file, "gpqnotes.png").queue();
    }

}
