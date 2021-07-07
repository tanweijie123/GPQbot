package cmd.general;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import config.Settings;
import data.JobList;
import logic.UsersMethod;
import model.UserAccount;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

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

        int floor = 0;
        try {
            floor = Integer.parseInt(event.getArgs().split(" ")[0]);
            if (floor < 0) {
                event.reply("Oof.. You can't even beat a snail?");
                return;
            } else if (floor > 70) {
                event.reply("Woahh.. You are strong. Too strong in fact.");
                return;
            }
        } catch (NumberFormatException ev) {
            event.reply("Huhh? Your floor is not a number? Idk how to use it nyaa (Eg. \"" + Settings.botCommand.getPrefix() + "floor 50\")");
            return;
        }

        //TODO; maybe change logic flow
        UsersMethod.getOrCreateUser(event.getGuild().getId(), event.getAuthor().getId());
        UsersMethod.updateFloor(event.getGuild().getId(), event.getAuthor().getId(), floor);
        UserAccount ua = UsersMethod.getOrCreateUser(event.getGuild().getId(), event.getAuthor().getId());

        event.reply("Hellonyaa~ This is what I have of you:\n" + ua.replyString(event.getMember().getEffectiveName()));

    }


    public void execute(SlashCommandEvent event) {
        long floor = event.getOption("floor_number").getAsLong();
        if (floor < 0) {
            event.reply("Oof.. You can't even beat a snail?").queue();
            return;
        } else if (floor > 70) {
            event.reply("Woahh.. You are strong. Too strong in fact.").queue();
            return;
        }

        UsersMethod.getOrCreateUser(event.getGuild().getId(), event.getUser().getId());
        UsersMethod.updateFloor(event.getGuild().getId(), event.getUser().getId(), Math.toIntExact(floor));
        UserAccount ua = UsersMethod.getOrCreateUser(event.getGuild().getId(), event.getUser().getId());

        event.reply("Hellonyaa~ This is what I have of you:\n" + ua.replyString(event.getMember().getEffectiveName())).setEphemeral(true).queue();

    }
}
