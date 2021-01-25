package commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.time.Instant;

public class Help extends Command {
    @Override
    public void exec(MessageReceivedEvent event) {
        final TextChannel channel = (TextChannel) event.getChannel();

        EmbedBuilder b = new EmbedBuilder();
        b.setColor(new Color(0x5d0505))
                .setAuthor("MWO Leaderboard Bot", null, null)
                .setTitle("Help")
                .addField("jarls [playername]", "shows Jarl's List of the specified player (your discord nickname if none given)", false)
                .addField("leaderboard [page]", "shows the given leaderboard page (page 1 by default)", false)
                .addField("player [playername]", "shows leaderboard profile of the specified player (your discord nickname if none given)", false)
                .addField("addmatch [match ids]", "adds matches to the database (Admin only)", false)

                .setThumbnail("https://cdn.discordapp.com/app-icons/751793516396544040/f2ac95ab98cf319dbc24ef7c6a1c23ce.png")
                .setFooter("MWO Leaderboards Bot", null)
                .setTimestamp(Instant.now());
        channel.sendMessage(b.build()).queue();
    }
}
