package commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageHandler extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event){
        if(event.isFromGuild()){
            Command.handle(event);
        }
    }
}
