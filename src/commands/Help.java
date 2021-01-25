package commands;

import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Help extends Command {
    @Override
    public void exec(MessageReceivedEvent event) {
        final TextChannel channel = (TextChannel) event.getChannel();

        channel.sendMessage("HELP!").queue();
    }
}
