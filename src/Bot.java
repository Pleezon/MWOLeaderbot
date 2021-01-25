import commands.MessageHandler;
import leaderboard.Leaderboard;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import token.Token;

import javax.security.auth.login.LoginException;

/**
 * @author Pleezon & B4CKF1SH
 */
public class Bot {
    public static JDA jda;

    public static void main(String[] args) throws LoginException, InterruptedException {
        jda = JDABuilder.createDefault(Token.getToken()).addEventListeners(
            new MessageHandler()
        ).setActivity(Activity.watching("Comp Leaderboard / Jarl's List")).build();
        jda.awaitReady();

        Leaderboard.init();
    }
}
