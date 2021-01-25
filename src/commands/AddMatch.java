package commands;

import leaderboard.Leaderboard;
import leaderboard.Player;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import token.Token;
import utils.Match;
import utils.MatchPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Pleezon & B4CKF1SH
 */
public class AddMatch extends Command {

    @Override
    public void exec(MessageReceivedEvent event) {

        final TextChannel channel = (TextChannel) event.getChannel();

        if (!event.isFromGuild() || !event.getGuild().getId().equals("782714600399175712")
                || event.getMember() == null || !event.getMember().hasPermission(Permission.MANAGE_PERMISSIONS)) {
            System.out.println("No Perms");
            return;
        }

        String[] contents = event.getMessage().getContentRaw().split(" ");
        if (contents.length == 1) {
            Command.commands[0].exec(event);
        } else {
           String[] ids = Arrays.stream(contents).skip(1).collect(Collectors.toUnmodifiableList()).toArray(new String[0]);
           boolean failed = false;
           for (String id : ids) {
               System.out.printf("INFO: adding match id %s%n", id);

               Match match = Match.loadFromUrl(String.format("https://mwomercs.com/api/v1/matches/%s?api_token=%s", id, Token.getApi()));

               if (match != null) {
                   MatchPlayer[] teamA = match.getTeamA();
                   MatchPlayer[] teamB = match.getTeamB();

                   List<Player> teamAL = new ArrayList<>();
                   List<Player> teamBL = new ArrayList<>();

                   for (MatchPlayer p : teamA) {
                       Player player = Leaderboard.getPlayer(p.getUsername());
                       player.setW(player.getWins() + (match.getWinner() == 1 ? 1 : 0));
                       player.setL(player.getLosses() + (match.getWinner() == 2 ? 1 : 0));
                       player.setGamesPlayed(player.getGamesPlayed() + 1);
                       player.setK(player.getKills() + p.getKills());
                       player.setD(player.getDeaths() + (p.getHealthPercentage() == 0 ? 1 : 0));
                       teamAL.add(player);
                   }
                   for (MatchPlayer p : teamB) {
                       Player player = Leaderboard.getPlayer(p.getUsername());
                       player.setW(player.getWins() + (match.getWinner() == 2 ? 1 : 0));
                       player.setL(player.getLosses() + (match.getWinner() == 1 ? 1 : 0));
                       player.setGamesPlayed(player.getGamesPlayed() + 1);
                       player.setK(player.getKills() + p.getKills());
                       player.setD(player.getDeaths() + (p.getHealthPercentage() == 0 ? 1 : 0));
                       teamBL.add(player);
                   }

                   int avgElo = 0;
                   for (Player p : teamAL) {
                       avgElo += p.getElo();
                   }
                   for (Player p : teamBL) {
                       avgElo += p.getElo();
                   }

                   avgElo /= (teamA.length + teamB.length);

                   //Tie
                   if (match.getWinner() == 0) {
                       for (Player p : teamAL) {
                           int elo = p.getElo();
                           int newElo = (int) (elo - (0.2 * (elo - avgElo)));
                           p.setElo(newElo);
                           Leaderboard.save(p);
                       }
                       for (Player p : teamBL) {
                           int elo = p.getElo();
                           int newElo = (int) (elo - (0.2 * (elo - avgElo)));
                           p.setElo(newElo);
                           Leaderboard.save(p);
                       }

                   }
                   //Team A Win
                   else if (match.getWinner() == 1) {
                       for (Player p : teamAL) {
                           int elo = p.getElo();
                           int newElo = (int) (elo - (0.2 * (elo - (avgElo + 100))));
                           p.setElo(newElo);
                           Leaderboard.save(p);
                       }
                       for (Player p : teamBL) {
                           int elo = p.getElo();
                           int newElo = (int) (elo - (0.2 * (elo - (avgElo - 100))));
                           p.setElo(newElo);
                           Leaderboard.save(p);
                       }
                   }
                   //Team B Win
                   else if (match.getWinner() == 2) {
                       for (Player p : teamAL) {
                           int elo = p.getElo();
                           int newElo = (int) (elo - (0.2 * (elo - (avgElo - 100))));
                           p.setElo(newElo);
                           Leaderboard.save(p);
                       }
                       for (Player p : teamBL) {
                           int elo = p.getElo();
                           int newElo = (int) (elo - (0.2 * (elo - (avgElo + 100))));
                           p.setElo(newElo);
                           Leaderboard.save(p);
                       }
                   }

               }
               else {
                   failed = true;
               }
               if (failed) event.getMessage().addReaction("❌").queue();
               else event.getMessage().addReaction("✔").queue();
           }

        }
    }
}
