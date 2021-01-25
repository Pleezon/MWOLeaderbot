package commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Arrays;

public abstract class Command extends ListenerAdapter {
    static final String prefix = "!";
    public abstract void exec(MessageReceivedEvent event);

    public static Command[] commands = new Command[]{
            new Help(), new Jarls()
    };



    public static void handle(MessageReceivedEvent e){
        Arrays.stream(commands).filter((c)->e.getMessage().getContentRaw().startsWith(c.getClass().getSimpleName().toLowerCase())).forEach((c)->{c.exec(e);});
    }
}
