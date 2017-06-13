package me.technick6425.tafobot.command;

import me.technick6425.tafobot.TafoBot;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.List;

public class CommandChannels extends Command {
	private TafoBot tafoBot;

	public CommandChannels(TafoBot tafoBot) {
		this.tafoBot = tafoBot;
	}
 	@Override
	public void execute(Message message, String... args) {
		assertOwner(message, true);
		if(args.length < 1) {
			message.getTextChannel().sendMessage("Please include a server ID").queue();
			return;
		}

		Guild g = tafoBot.getJda().getGuildById(args[0]);
		if(g == null) {
			message.getTextChannel().sendMessage("Server not found.").queue();
			return;
		}

		List<TextChannel> channels = g.getTextChannels();
		String s = "Channels: \n\n";
		for(TextChannel t : channels) {
			s = s + t.getName() + "\n";
		}

		message.getTextChannel().sendMessage(s).queue();
	}
}
