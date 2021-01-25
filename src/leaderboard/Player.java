package leaderboard;

import java.io.Serializable;
import java.util.UUID;

public class Player implements Serializable {
    private UUID uniqueId;
    private String name;
    private double kdr;
    private double wlr;
    private int gamesPlayed;
    private int elo;

    public Player(UUID uniqueId, String name, double kdr, double wlr, int gamesPlayed, int elo) {
        this.uniqueId = uniqueId;
        this.name = name;
        this.kdr = kdr;
        this.wlr = wlr;
        this.gamesPlayed = gamesPlayed;
        this.elo = elo;
    }
}
