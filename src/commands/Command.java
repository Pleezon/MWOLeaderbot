package commands;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;

import java.util.Arrays;

public abstract class Command {
    static final String prefix = "!";
    public abstract void exec(GuildMessageReceivedEvent event);


    public static Command[] commands = new Command[]{
            new Help()
    };

    public static void handle(GuildMessageReceivedEvent e){
        Arrays.stream(commands).filter((c)->e.getMessage().getContentRaw().startsWith(c.getClass().getSimpleName().toLowerCase())).forEach((c)->{c.exec(e);});
    }
}
