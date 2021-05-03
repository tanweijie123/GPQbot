package cmd.mod;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import config.Settings;
import logic.GuildMethod;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class NewRegistration extends Command {

    public NewRegistration() {
        super.name = "new";
        super.help = "Create a new GPQ registration (only mods)";
        super.requiredRole = Settings.BOT_MOD_NAME;
        super.arguments = "[message]";
    }

    @Override
    protected void execute(CommandEvent event) {
        String anyExistingReg = GuildMethod.getCurrentGPQLink(event.getGuild().getId());

        if (anyExistingReg != null) {
            String reply = String.format("You have an existing registration open: %s", anyExistingReg);
            event.reply(reply);
            return;
        }

        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Register for GPQ!");
        eb.setThumbnail(event.getGuild().getIconUrl());
        eb.setDescription(event.getArgs());
        eb.setColor(java.awt.Color.GREEN);
        eb.setFooter("This request is made by " + event.getMember().getEffectiveName() + " on " + ZonedDateTime.now(ZoneId.of("GMT+8")).format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
        Message msg = event.getChannel().sendMessage(eb.build()).complete();
        msg.addReaction("U+2705").queue();
        msg.pin().queue();

        event.getChannel().sendMessage("@everyone: " + event.getMember().getEffectiveName() + " has created a new registration!").queue();
        GuildMethod.setCurrentGPQLink(event.getGuild().getId(), msg.getJumpUrl());
    }
}
