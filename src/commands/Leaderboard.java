package commands;

import leaderboard.Player;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.Color;
import java.time.Instant;
import java.util.stream.Collectors;

/**
 * @author Pleezon & B4CKF1SH
 */
public class Leaderboard extends Command {
    @Override
    public void exec(MessageReceivedEvent event) {
        final TextChannel channel = (TextChannel) event.getChannel();

        String[] contents = event.getMessage().getContentRaw().split(" ");

        int page = 1;
        if (contents.length > 1) {
            page = Integer.parseInt(contents[1]);
        }

        int minRank = 10 * (page - 1);
        int maxRank = 10 * (page);

        Player[] ranks = leaderboard.Leaderboard.getRanks().skip(minRank).limit(maxRank - minRank).collect(Collectors.toUnmodifiableList()).toArray(new Player[0]);

        StringBuilder f1 = new StringBuilder();
        StringBuilder f2 = new StringBuilder();
        StringBuilder f3 = new StringBuilder();

        for (int i = 0; i < ranks.length; i++) {
            f1.append(i + minRank + 1).append("\n");
            f2.append(ranks[i].getName()).append("\n");
            f3.append(ranks[i].getElo()).append("\n");
        }

        EmbedBuilder b = new EmbedBuilder();
        b.setColor(new Color(0xffaa1e))
                .setAuthor("MWO Comp Leaderboard", null, null)
                .setTitle(String.format("Page %d (Rank %d to %d)", page, minRank+1, maxRank))
                .addField("Rank", f1.toString(), true)
                .addField("Name", f2.toString(), true)
                .addField("Elo", f3.toString(), true)
                .setThumbnail("https://1.bp.blogspot.com/-KluZqBTRsDk/XFrdJXz_gAI/AAAAAAAAAAQ/VInbxjjdSrookRkrtP1uK7_hRB1uuO4XwCK4BGAYYCw/s1600/mwocomplogosmall.png")
                .setFooter("MWO Leaderboards Bot", null)
                .setTimestamp(Instant.now());


        channel.sendMessage(b.build()).queue();
    }
}
