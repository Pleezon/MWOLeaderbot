package leaderboard;

public class Player {
    private double kdr;
    private double wlr;
    private int gamesPlayed;
    private int elo;

    private Player(double kdr, double wlr, int gamesPlayed, int elo) {
        this.kdr = kdr;
        this.wlr = wlr;
        this.gamesPlayed = gamesPlayed;
        this.elo = elo;
    }
}
