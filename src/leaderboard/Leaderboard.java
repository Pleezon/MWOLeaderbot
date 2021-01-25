package leaderboard;

import java.io.*;

public class Leaderboard implements Serializable {


    public void writeToFile(String path) {
        try {
            FileOutputStream fileOut = new FileOutputStream(path + "/leaderboard.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this);
            out.close();
            fileOut.close();
            System.out.println("Serialized data is saved in " + path + "/leaderboard.ser");
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    public static Leaderboard loadFromFile(String path) {
        Leaderboard l = null;

        try {
            FileInputStream fileIn = new FileInputStream(path + "/leaderboard.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            l = (Leaderboard) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException i) {
            i.printStackTrace();
        } catch (ClassNotFoundException c) {
            System.out.println("Leaderboard class not found");
            c.printStackTrace();
        }

        return l;
    }
}
