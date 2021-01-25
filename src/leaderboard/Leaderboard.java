package leaderboard;

import java.io.*;
import java.nio.Buffer;
import java.util.Properties;

public class Leaderboard{
    private static String filename = "./leaderboard.mwo";
    public static void init(){
        try {
            new File(filename).createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static Player getPlayer(String name){
        try{
            Properties p = new Properties();
            p.load(new BufferedInputStream(new FileInputStream(filename)));
            if(!p.containsKey(name)){
                Player player = new Player(name,0,0,0,0,0,5000);
                save(player);
                return player;
            }else{
                return Player.fromString(p.getProperty(name));
            }
        }catch (Exception e){e.printStackTrace();return null;}


    }

    public static void save(Player player){
        try{
            Properties p = new Properties();
            p.load(new BufferedInputStream(new FileInputStream(filename)));
            p.put(player.getName(),player.toString());
            p.store(new BufferedOutputStream(new FileOutputStream(filename)),"");
        }catch (Exception e){
            System.out.println("Failed whilst saving player: " + player);
            e.printStackTrace();
        }

    }


}
