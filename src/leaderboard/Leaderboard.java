package leaderboard;


import java.io.*;
import java.util.Collection;
import java.util.Comparator;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Pleezon & B4CKF1SH
 */
public class Leaderboard {

    private static final int STARTING_ELO = 1500;

    private static String filename = "./leaderboard.mwo";
    private static String matchfile = "./matches.mwo";

    public static void init() {
        try {
            new File(filename).createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            new File(filename + ".old").createNewFile();

            Properties p = new Properties();
            p.load(new BufferedInputStream(new FileInputStream(filename)));

            p.store(new BufferedOutputStream(new FileOutputStream(filename + ".old")), "");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            new File(matchfile).createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static boolean matchExists(String id) {
        try {
            Properties p = new Properties();
            p.load(new BufferedInputStream(new FileInputStream(matchfile)));
            return p.containsKey(id);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void saveMatch(String id) {
        try {
            Properties p = new Properties();
            p.load(new BufferedInputStream(new FileInputStream(matchfile)));
            p.put(id, ".");
            p.store(new BufferedOutputStream(new FileOutputStream(matchfile)), "");
        } catch (Exception e) {
            System.out.printf("Failed whilst adding match id %s to used list.%n", id);
            e.printStackTrace();
        }
    }

    public static Player getPlayer(String name) {
        try {
            Properties p = new Properties();
            p.load(new BufferedInputStream(new FileInputStream(filename)));
            if (!p.containsKey(name)) {
                Player player = new Player(name, 0, 0, 0, 0, 0, STARTING_ELO);
                save(player);
                return player;
            } else {
                return Player.fromString(p.getProperty(name));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }

    public static void save(Player player) {
        try {
            Properties p = new Properties();
            p.load(new BufferedInputStream(new FileInputStream(filename)));
            p.put(player.getName(), player.toString());
            p.store(new BufferedOutputStream(new FileOutputStream(filename)), "");
        } catch (Exception e) {
            System.out.println("Failed whilst saving player: " + player);
            e.printStackTrace();
        }
    }

    public static int getRank(Stream<Player> ranks, Player p) {
        return ranks.collect(Collectors.toUnmodifiableList()).indexOf(p);
    }

    /**
     * VERY inefficient method of getting all ranks
     * use as few times as possible.
     *
     * @return Stream of all Players sorted by ranks
     */
    public static Stream<Player> getRanks() {
        try {
            Properties p = new Properties();
            p.load(new BufferedInputStream(new FileInputStream(filename)));
            Collection<Object> objects = p.values();
            return objects.stream().filter(o -> o instanceof String).map((o) -> (String) o).map(Player::fromString).sorted(Comparator.comparingDouble(Player::getKDR).reversed()).sorted(Comparator.comparingDouble(Player::getWLR).reversed()).sorted(Comparator.comparingInt(Player::getElo).reversed());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Player getPlayerSafe(String name) {
        try {
            Properties p = new Properties();
            p.load(new BufferedInputStream(new FileInputStream(filename)));
            if (!p.containsKey(name)) {
                return null;
            } else {
                return Player.fromString(p.getProperty(name));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
