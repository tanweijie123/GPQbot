package event;

import config.Settings;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.List;

public class TextEvent extends ListenerAdapter {

    /*
        Nyaa commands:

     */

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if (event.getMessage().getContentRaw().toLowerCase().startsWith("good bot") && !event.getAuthor().isBot()) {
            runGoodBotEvent(event);
            return;
        }

        if (event.getAuthor().isBot() || !event.getMessage().getContentRaw().startsWith(Settings.botCommand.getPrefix())) {
            return;
        }

        if (event.getMessage().getContentRaw().startsWith(Settings.botCommand.getPrefix() + "as nyaa")) {
            runCreatorEvent(event);
        }
    }

    private void runCreatorEvent(GuildMessageReceivedEvent event) {
        throw new UnsupportedOperationException("This function is not supported");
    }



    /* HELPER METHODS */

    /**
     * Replies "arigatou" if it was a direct reply to this bot, or the latest bot message is from this bot.
     * Otherwise, ask if i am a good bot.
     * @param event
     */
    private void runGoodBotEvent(GuildMessageReceivedEvent event) {

        //if it is a direct reply
        Message replyMsg = event.getMessage().getReferencedMessage();
        if (replyMsg != null) {
            if (replyMsg.getAuthor().getIdLong() == event.getJDA().getSelfUser().getIdLong()) {
                event.getMessage().reply("arigatou nyaaa~ :smiling_face_with_3_hearts:").queue();
            } else {
                event.getMessage().reply("am i a good bot? :pleading_face:").queue();
            }
            return;
        }

        List<Message> msgHistory = event.getChannel().getHistoryBefore(event.getMessageIdLong(), 3).complete().getRetrievedHistory();

        int idx = 0;
        for (; idx < msgHistory.size(); idx++) {
            if (msgHistory.get(idx).getAuthor().isBot()) {
                if (msgHistory.get(idx).getAuthor().getIdLong() == event.getJDA().getSelfUser().getIdLong()) {
                    event.getMessage().reply("arigatou nyaaa~ :smiling_face_with_3_hearts:").queue();
                    return;
                }
                break;
            }
        }

        event.getMessage().reply("am i a good bot? :pleading_face:").queue();
        return;
    }
}
