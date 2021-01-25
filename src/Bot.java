import commands.Command;
import commands.MessageHandler;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;

public class Bot {
    public static JDA jda;

    public static void main(String[] args) throws LoginException, InterruptedException {
        jda = JDABuilder.createDefault("").addEventListeners(
            new MessageHandler()
        ).setActivity(Activity.watching("CommunityJam")).build();
        jda.awaitReady();
    }
}
