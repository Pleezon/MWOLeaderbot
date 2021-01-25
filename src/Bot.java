import commands.Command;
import commands.MessageHandler;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;

public class Bot {
    public static JDA jda;

    public static void main(String[] args) throws LoginException, InterruptedException {
        jda = JDABuilder.createDefault(Token.getToken()).addEventListeners(
            new MessageHandler()
        ).setActivity(Activity.watching("sample text")).build();
        jda.awaitReady();
    }
}
