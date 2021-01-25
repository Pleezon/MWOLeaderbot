package utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Match {

    private final MatchPlayer[] specs;
    private final MatchPlayer[] teamA;
    private final MatchPlayer[] teamB;
    private final int winner;
    private final Instant time;

    private Match(MatchPlayer[] specs, MatchPlayer[] teamA, MatchPlayer[] teamB, int winner, Instant time) {

        this.specs = specs;
        this.teamA = teamA;
        this.teamB = teamB;
        this.winner = winner;
        this.time = time;
    }

    public static Match loadFromUrl(String url) {
        try {
            JSONObject data = JsonReader.readJsonFromUrl(url);

            try {
                //catch invalid Match ID
                String msg = data.getString("message");
                return null;
            } catch(JSONException e) {}

            try {
                //catch invalid API key
                String error = data.getString("error");
                return null;
            } catch(JSONException e) {}

            List<MatchPlayer> playerList = new ArrayList<>();

            List<MatchPlayer> specList = new ArrayList<>();
            List<MatchPlayer> teamAList = new ArrayList<>();
            List<MatchPlayer> teamBList = new ArrayList<>();


            JSONArray players = data.getJSONArray("UserDetails");
            for (int i = 0; i < players.length(); i++) {
                JSONObject playerJson = players.getJSONObject(i);
                playerList.add(new MatchPlayer(playerJson));
            }

            for (MatchPlayer p : playerList) {
                if (p.isSpectator()) {
                    specList.add(p);
                }
                else if ("A".equals(p.getTeam())) {
                    teamAList.add(p);
                }
                else if ("B".equals(p.getTeam())) {
                    teamBList.add(p);
                }
            }

            JSONObject matchDetails = data.getJSONObject("MatchDetails");
            String winningTeam = null;
            try {
                winningTeam = matchDetails.getString("WinningTeam");
            } catch(JSONException e) {}

            int winner = 0;
            if (winningTeam != null) winner = Integer.parseInt(winningTeam);

            Instant date = Instant.parse(matchDetails.getString("CompleteTime").substring(0, 19) + ".00Z");


            return new Match(specList.toArray(new MatchPlayer[0]), teamAList.toArray(new MatchPlayer[0]), teamBList.toArray(new MatchPlayer[0]), winner, date);


        } catch (IOException e) {
            return null;
        }
    }

    public MatchPlayer[] getTeamA() {
        return teamA;
    }

    public MatchPlayer[] getTeamB() {
        return teamB;
    }

    public MatchPlayer[] getSpecs() {
        return specs;
    }

    public int getWinner() {
        return winner;
    }

    public Instant getTime() {
        return time;
    }
}
