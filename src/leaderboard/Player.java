package leaderboard;

/**
 * @author Pleezon & B4CKF1SH
 */
public class Player {
    private String name;
    private int k;
    private int d;
    private int w;
    private int l;
    private int gamesPlayed;
    private int elo;
    public static final String separator = " : ";

    public Player(String name, int k, int d, int w, int l, int gamesPlayed, int elo) {
        this.name = name;
        this.k = k;
        this.d = d;
        this.w = w;
        this.l = l;
        this.gamesPlayed = gamesPlayed;
        this.elo = elo;
    }

    public String getName() {
        return name;
    }

    public int getKills() {
        return k;
    }

    public void setK(int k) {
        this.k = k;
    }

    public void setD(int d) {
        this.d = d;
    }

    public void setW(int w) {
        this.w = w;
    }

    public void setL(int l) {
        this.l = l;
    }

    public void setGamesPlayed(int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    public void setElo(int elo) {
        this.elo = elo;
    }

    public int getDeaths() {
        return d;
    }

    public int getWins() {
        return w;
    }

    public int getLosses() {
        return l;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public int getElo() {
        return elo;
    }

    public double getKDR(){
        return d==0? k :(double)k/(double) d;
    }
    public double getWLR(){
        return l==0? w :(double)w/(double) l;
    }

    @Override
    public String toString() {
        return this.name + separator + this.k + separator + this.d + separator + this.w + separator + this.l + separator + this.gamesPlayed + separator + this.elo;
    }

    public static Player fromString(String s) {
        String[] a = s.split(separator);
        try {

            String name = a[0];
            int k = Integer.parseInt(a[1]);
            int d = Integer.parseInt(a[2]);
            int w = Integer.parseInt(a[3]);
            int l = Integer.parseInt(a[4]);
            int gamesPlayed = Integer.parseInt(a[5]);
            int elo = Integer.parseInt(a[6]);

            return new Player(name, k, d, w, l, gamesPlayed, elo);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Player && this.name .equals(((Player) other).getName());
    }

}
