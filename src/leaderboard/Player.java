package leaderboard;

import java.util.UUID;

public class Player {
    private UUID uniqueId;
    private double kdr;
    private double wlr;
    private int gamesPlayed;
    private int elo;

    private Player(UUID uniqueId, double kdr, double wlr, int gamesPlayed, int elo) {
        this.uniqueId = uniqueId;
        this.kdr = kdr;
        this.wlr = wlr;
        this.gamesPlayed = gamesPlayed;
        this.elo = elo;
    }

    public static Player loadFromDB(UUID uuid) {
        return null;
    }
}
