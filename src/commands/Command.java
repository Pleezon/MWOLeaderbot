package commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Arrays;

/**
 * @author Pleezon & B4CKF1SH
 */
public abstract class Command extends ListenerAdapter {
    static final String jarlsLogo = "https://leaderboard.isengrim.org/Isenlogosmall.png";
    static final String mwoCompLogo = "https://1.bp.blogspot.com/-KluZqBTRsDk/XFrdJXz_gAI/AAAAAAAAAAQ/VInbxjjdSrookRkrtP1uK7_hRB1uuO4XwCK4BGAYYCw/s1600/mwocomplogosmall.png";
    static final String botLogo = "https://cdn.discordapp.com/app-icons/751793516396544040/f2ac95ab98cf319dbc24ef7c6a1c23ce.png";

    static final String prefix = "!";
    public abstract void exec(MessageReceivedEvent event);

    public static Command[] commands = new Command[]{
            new Help(), new Jarls(), new Leaderboard(), new Player(), new AddMatch()
    };



    public static void handle(MessageReceivedEvent e){
        Arrays.stream(commands).filter((c)->e.getMessage().getContentRaw().startsWith(prefix + c.getClass().getSimpleName().toLowerCase())).forEach((c)-> c.exec(e));
    }
}
