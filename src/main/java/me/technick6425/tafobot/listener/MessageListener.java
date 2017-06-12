package me.technick6425.tafobot.listener;

import me.technick6425.tafobot.TafoBot;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class MessageListener extends ListenerAdapter {
	private TafoBot tafoBot;

	public MessageListener(TafoBot tafoBot) {
		this.tafoBot = tafoBot;
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if(event.getChannel().getType() != ChannelType.TEXT) return;

		tafoBot.logService.onMessage(event.getMessage());

		if(event.getAuthor().isBot()) return;

		if(event.getMessage().getContent().startsWith(tafoBot.config.commandPrefix)) {
			tafoBot.commandManager.executeCommand(event.getMessage());
			return;
		}
	}
}
