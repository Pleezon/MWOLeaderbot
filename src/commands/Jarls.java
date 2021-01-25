package commands;



import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;
import utils.JsonReader;

import java.awt.*;
import java.io.IOException;
import java.nio.channels.Channel;
import java.time.Instant;
import java.util.Objects;

public class Jarls extends Command{
    public void exec(GuildMessageReceivedEvent message) {
        final TextChannel channel = message.getChannel();

        String[] contents = message.getMessage().getContentRaw().split(" ");
        if (contents.length == 1) {
            String name = Objects.requireNonNull(message.getMember()).getNickname();
            assert name != null;
            sendOverall(channel, name.replace(" ", "+"));
        }
        else {
            String name = contents[1];
            if (contents.length > 2) {
                for (int i = 2; i < contents.length; i++) {
                    name += " " + contents[i];
                }
            }

            sendOverall(channel, name.replace(" ", "+"));
        }
    }

    private static void sendNotFound(TextChannel channel, String playerName) {
        EmbedBuilder b = new EmbedBuilder();
        b.setAuthor("Jarl's List", "https://leaderboard.isengrim.org/", null)
                .setTitle("Not Found!")
                .setDescription("The Player with Username " + playerName + " was not found.")
                .setThumbnail("https://leaderboard.isengrim.org/Isenlogosmall.png")
                .setFooter("MWO Leaderboards Bot", null)
                .setTimestamp(Instant.now());
        channel.sendMessage(b.build()).queue();
    }

    private static void sendOverall(TextChannel channel, String playerName) {

        try {
            JSONObject data = JsonReader.readJsonFromUrl("https://leaderboard.isengrim.org/api/usernames/" + playerName);
            try {

                String name = data.getString("PilotName");

                int rank = data.getInt("Rank");

                double percentile = 0;
                try {
                    percentile = data.getDouble("Percentile");
                } catch (JSONException lol) {}

                String tag = "";
                try {
                    String unit = data.getString("UnitTag");
                    if (unit != null) tag = "[" + unit + "] ";
                } catch (JSONException lol) {}
                String tagRef = tag;

                double wlRatio = data.getDouble("WLRatio");
                double kdRatio = data.getDouble("KDRatio");

                int survivalRate = data.getInt("SurvivalRate");
                double killsPerMatch = data.getDouble("KillsPerMatch");

                int gamesPlayed = data.getInt("GamesPlayed");
                int adjustedScore = data.getInt("AdjustedScore");

                int firstSeason = data.getInt("FirstSeason");
                int lastSeason = data.getInt("LastSeason");

                int lightPercent = data.getInt("LightPercent");
                int mediumPercent = data.getInt("MediumPercent");
                int heavyPercent = data.getInt("HeavyPercent");
                int assaultPercent = data.getInt("AssaultPercent");

                int serverSeason = data.getInt("ServerSeason");


                boolean retired = rank == 0;

                final String percentileRef = percentile + "%";

                EmbedBuilder b = new EmbedBuilder();
                        b.setColor(new Color(0xf74a19))
                                .setAuthor("Jarl's List (Season " + serverSeason + ")", "https://leaderboard.isengrim.org/", null)
                                .setTitle(tagRef + name)
                                .addField("Rank", retired ? "Retired" : String.valueOf(rank), true)
                                .addField("Percentile", percentileRef, true)
                                .addField("Games Played", String.valueOf(gamesPlayed), true)

                                .addField("W/L Ratio", String.valueOf(wlRatio), true)
                                .addField("K/D Ratio", String.valueOf(kdRatio), true)
                                .addField("Adjusted Score", String.valueOf(adjustedScore), true)

                                .addField("Survival Rate", survivalRate + "%", true)
                                .addField("Kills Per Match", String.valueOf(killsPerMatch), true)

                                .addField("L/M/H/A", lightPercent + "%/" + mediumPercent + "%/" + heavyPercent + "%/" + assaultPercent + "%", false)

                                .addField("First Season", String.valueOf(firstSeason), true)
                                .addField("Last Season", String.valueOf(lastSeason), true)

                                .setThumbnail("https://leaderboard.isengrim.org/Isenlogosmall.png")
                                .setFooter("MWO Leaderboards Bot", null)
                                .setTimestamp(Instant.now());
                        channel.sendMessage(b.build()).queue();
            } catch (JSONException exception) {
                exception.printStackTrace();
                sendNotFound(channel, playerName);
            }
        } catch (IOException e) {
            sendNotFound(channel, playerName);
        }
    }
}
