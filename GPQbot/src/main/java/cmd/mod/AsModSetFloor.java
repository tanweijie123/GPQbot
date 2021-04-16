package cmd.mod;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import config.Settings;
import data.Data;
import model.UserAccount;
import net.dv8tion.jda.api.entities.Member;

import java.util.List;

public class AsModSetFloor extends Command {

    public AsModSetFloor() {
        super.name = "floor";
        super.help = "Shows everyone that is registered with NyanBot in this guild.";
        super.arguments = "<taggedMember> <floor> ...";
        super.requiredRole = Settings.BOT_MOD_NAME;
        super.hidden = true;
    }

    @Override
    protected void execute(CommandEvent event) {
        List<Member> taggedMemberList = event.getMessage().getMentionedMembers();

        if (taggedMemberList.size() == 0) {
            event.reply("No user mentioned found.");
            return;
        }

        String[] split = event.getArgs().split(" ");

        if (split.length / 2 != taggedMemberList.size() || !split[0].contains(taggedMemberList.get(0).getId())) {
            event.reply("Pair not found or invalid. (Eg. \"@A 12 @b 20\") ");
            return;
        }
        String reply = "These member's floor had been updated:\n";

        for (int i = 0; i < taggedMemberList.size(); i++) {
            try {
                UserAccount ua = Data.currentUserList.getByUserKey(taggedMemberList.get(i).getIdLong(), taggedMemberList.get(i).getEffectiveName());

                if (ua == null) {
                    reply += taggedMemberList.get(i).getAsMention() + " is not registered.\n";
                } else {
                    int oldFlr = ua.getFloor();
                    int newFlr = Integer.parseInt(split[i*2+1]);
                    if (newFlr < 0 || newFlr > 70)
                        throw new NumberFormatException();
                    ua.setFloor(newFlr);
                    reply += taggedMemberList.get(i).getAsMention() + " floor changed from " + oldFlr + "F to " + newFlr + "F \n";
                }
            } catch (NumberFormatException nfe) {
                reply += taggedMemberList.get(i).getAsMention() + " has been given an invalid floor.\n";
            }
        }

        event.reply(reply);

    }

}
