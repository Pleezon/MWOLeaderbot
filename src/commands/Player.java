package commands;

import leaderboard.Leaderboard;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.json.JSONException;
import org.json.JSONObject;
import utils.JsonReader;

import java.awt.*;
import java.io.IOException;
import java.time.Instant;
import java.util.Objects;
import java.util.OptionalInt;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Player extends Command {
    public void exec(MessageReceivedEvent message) {

        final TextChannel channel = (TextChannel) message.getChannel();

        String[] contents = message.getMessage().getContentRaw().split(" ");
        if (contents.length == 1) {
            String name = Objects.requireNonNull(message.getMember()).getEffectiveName();
            sendOverall(channel, name);
        } else {
            String name = contents[1];
            if (contents.length > 2) {
                for (int i = 2; i < contents.length; i++) {
                    name += " " + contents[i];
                }
            }

            sendOverall(channel, name);
        }
    }

    private static void sendNotFound(TextChannel channel, String playerName) {
        EmbedBuilder b = new EmbedBuilder();
        b.setColor(new Color(0xffaa1e))
                .setAuthor("MWO Comp Leaderboard", null, null)
                .setTitle("Not Found!")
                .setDescription("The Player with Username " + playerName + " was not found.")
                .setThumbnail("https://1.bp.blogspot.com/-KluZqBTRsDk/XFrdJXz_gAI/AAAAAAAAAAQ/VInbxjjdSrookRkrtP1uK7_hRB1uuO4XwCK4BGAYYCw/s1600/mwocomplogosmall.png")
                .setFooter("MWO Leaderboards Bot", null)
                .setTimestamp(Instant.now());
        channel.sendMessage(b.build()).queue();
    }

    private static void sendOverall(TextChannel channel, String playerName) {

        leaderboard.Player player = Leaderboard.getPlayerSafe(playerName);
        if (player == null) {
            sendNotFound(channel, playerName);
            return;
        }

        Stream<leaderboard.Player> ranks = Leaderboard.getRanks();


        String rank = "None";

        int rankInt = Leaderboard.getRank(ranks, player);

        if (rankInt >= 0) {
            rank = String.valueOf(rankInt + 1);
        }

        EmbedBuilder b = new EmbedBuilder();
        b.setColor(new Color(0xffaa1e))
                .setAuthor("MWO Comp Leaderboard", null, null)
                .setTitle(playerName)
                .addField("Rank", rank, true)
                .addField("Elo", String.valueOf(player.getElo()), true)
                .addField("Games Played", String.valueOf(player.getGamesPlayed()), true)

                .addField("Wins", String.valueOf(player.getWins()), true)
                .addField("Losses", String.valueOf(player.getLosses()), true)
                .addField("W/L Ratio", String.format("%.3g", player.getWLR()), true)

                .addField("Kills", String.valueOf(player.getKills()), true)
                .addField("Deaths", String.valueOf(player.getDeaths()), true)
                .addField("K/D Ratio", String.format("%.3g", player.getKDR()), true)


                .setThumbnail("https://1.bp.blogspot.com/-KluZqBTRsDk/XFrdJXz_gAI/AAAAAAAAAAQ/VInbxjjdSrookRkrtP1uK7_hRB1uuO4XwCK4BGAYYCw/s1600/mwocomplogosmall.png")
                .setFooter("MWO Leaderboards Bot", null)
                .setTimestamp(Instant.now());
        channel.sendMessage(b.build()).queue();
    }
}

