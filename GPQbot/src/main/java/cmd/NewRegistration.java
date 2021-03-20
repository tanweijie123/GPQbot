package cmd;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import config.Settings;
import data.Data;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.internal.utils.tuple.MutablePair;

import java.net.MalformedURLException;
import java.net.URL;
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
        String anyExistingReg = Data.currentGPQList.getByGuildKey(event.getGuild().getIdLong());

        if (anyExistingReg != null) {
            String reply = String.format("You have an existing registration open: %s", anyExistingReg); //TODO: provide a way for them to reset registration
            event.reply(reply);
            return;
        }

        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Register for GPQ Day!");
        eb.setDescription(event.getArgs());
        eb.setColor(java.awt.Color.GREEN);
        eb.setFooter("This request is made by " + event.getMember().getEffectiveName() + " on " + ZonedDateTime.now(ZoneId.of("GMT+8")).format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
        Message msg = event.getChannel().sendMessage(eb.build()).complete();
        msg.addReaction("\uD83C\uDDF2").queue();
        msg.addReaction("\uD83C\uDDF9").queue();
        msg.addReaction("\uD83C\uDDFC").queue();
        msg.addReaction("\uD83C\uDDED").queue();
        msg.addReaction("\uD83C\uDDEB").queue();
        msg.addReaction("\uD83C\uDDF8").queue();
        msg.addReaction("\uD83D\uDE34").queue();
        msg.pin().queue();

        event.getChannel().sendMessage("@everyone: " + event.getMember().getEffectiveName() + " has created a new registration!").queue();

        try {
            MutablePair<Long, URL> p = new MutablePair<>(msg.getGuild().getIdLong(), new URL(msg.getJumpUrl()));
            Data.currentGPQList.add(p);
        } catch (MalformedURLException malformedURLException) {
            System.err.println("Unable to convert " + msg.getJumpUrl() + " into URL and store into GPQ List");
        }
    }
}
