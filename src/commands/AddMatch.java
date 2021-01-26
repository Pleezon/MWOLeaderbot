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

    //how much the players middle out after a tie
    private static final float TIE_TO_AVG_PERCENT = 0.1f;
    //straight up change for a win/loss with equal elo
    private static final int WIN_CHANGE = 10;
    //biggest elo gain/loss allowed (reached at an elo difference of MAX_UPSET)
    private static final int MAX_CHANGE = 100;
    //the biggest elo difference accounted for
    private static final int MAX_UPSET = 1000;

    private static final double SCALE_VALUE = Math.pow(MAX_UPSET, 2) / (MAX_CHANGE - WIN_CHANGE);

    @Override
    public void exec(MessageReceivedEvent event) {

        final TextChannel channel = (TextChannel) event.getChannel();

        if (!event.isFromGuild() || !event.getGuild().getId().equals("782714600399175712")
                || event.getMember() == null || !event.getMember().hasPermission(Permission.MANAGE_PERMISSIONS)) {
            event.getMessage().addReaction("❌").queue();
            return;
        }

        String[] contents = event.getMessage().getContentRaw().split(" ");
        if (contents.length == 1) {
            Command.commands[0].exec(event);
        } else {
           String[] ids = Arrays.stream(contents).skip(1).collect(Collectors.toUnmodifiableList()).toArray(new String[0]);

           int failed = 0;
           List<String> failedIds = new ArrayList<>();
           int successful = 0;
           int skipped = 0;

           int attemptCap = 0;

            channel.sendMessage(
                    String.format("%d matches, estimated time: %d minutes", ids.length, ids.length/59)
            ).queue();

           for (String id : ids) {
               attemptCap++;
               if (attemptCap > 60) {
                   try {
                       Thread.sleep(61000);
                       attemptCap = 0;
                   } catch (InterruptedException e) {
                   }
               }
               System.out.printf("INFO: Adding match ID %s...%n", id);

               Match match = Match.loadFromUrl(String.format("https://mwomercs.com/api/v1/matches/%s?api_token=%s", id, Token.getApi()));

               if (Leaderboard.matchExists(id)) {
                   System.out.printf("WARN: Match ID %s already registered, skipping.%n", id);
                   skipped++;
                   continue;
               }

               if (match != null) {
                   MatchPlayer[] teamA = match.getTeamA();
                   MatchPlayer[] teamB = match.getTeamB();

                   if (teamA.length == teamB.length) {

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
                       int aElo = 0;
                       int bElo = 0;

                       for (Player p : teamAL) {
                           avgElo += p.getElo();
                           aElo += p.getElo();
                       }
                       for (Player p : teamBL) {
                           avgElo += p.getElo();
                           bElo += p.getElo();
                       }

                       avgElo /= (teamA.length + teamB.length);
                       aElo /= teamA.length;
                       bElo /= teamB.length;

                       int matchVal = (int) Math.round(Math.pow(bElo - aElo, 2) / SCALE_VALUE);

                       int aWinVal = Math.max(Math.min(matchVal + WIN_CHANGE, MAX_CHANGE), 0);
                       int aLossVal = Math.max(Math.min(matchVal - WIN_CHANGE, 0), -MAX_CHANGE);

                       int bWinVal = Math.max(Math.min(-matchVal + WIN_CHANGE, MAX_CHANGE), 0);
                       int bLossVal = Math.max(Math.min(-matchVal - WIN_CHANGE, 0), -MAX_CHANGE);

                       int aChange = match.getWinner() == 1 ? aWinVal : aLossVal;
                       int bChange = match.getWinner() == 2 ? bWinVal : bLossVal;

                       //Tie
                       if (match.getWinner() == 0) {
                           for (Player p : teamAL) {
                               int elo = p.getElo();

                               int newElo = (int) (elo - (TIE_TO_AVG_PERCENT * (elo - avgElo)));

                               p.setElo(newElo);
                               Leaderboard.save(p);
                           }
                           for (Player p : teamBL) {
                               int elo = p.getElo();

                               int newElo = (int) (elo - (TIE_TO_AVG_PERCENT * (elo - avgElo)));

                               p.setElo(newElo);
                               Leaderboard.save(p);
                           }

                       }

                       //Other Outcome
                       else {
                           for (Player p : teamAL) {
                               int elo = p.getElo();
                               int newElo = elo + aChange;
                               p.setElo(newElo);
                               Leaderboard.save(p);
                           }
                           for (Player p : teamBL) {
                               int elo = p.getElo();
                               int newElo = elo + bChange;
                               p.setElo(newElo);
                               Leaderboard.save(p);
                           }
                       }
                   }
                   successful++;
                   Leaderboard.saveMatch(id);
               }
               else {
                   System.out.printf("ERROR: Match ID %s failed to load!%n", id);
                   failed++;
                   failedIds.add(id);
               }

           }

           channel.sendMessage(
                   String.format("%d matches entered, %d successful, %d failed, %d skipped.", successful + failed + skipped, successful, failed, skipped)
           ).queue();

           String message = "Failed IDs: \n";

           for (String id : failedIds) {
               message += id + " ";
           }

           if (failedIds.size() > 0) channel.sendMessage(message).queue();
        }
    }
}
