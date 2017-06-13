package me.technick6425.tafobot.command;

import me.technick6425.tafobot.TafoBot;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.List;

public class CommandLog extends Command {
	private TafoBot tafoBot;

	public CommandLog(TafoBot tafoBot) {
		this.tafoBot = tafoBot;
	}

	@Override
	public void execute(Message message, String... args) {
		assertOwner(message, true);
		if(args.length != 2) {
			message.getTextChannel().sendMessage("`" + tafoBot.config.commandPrefix + "log <guild id> <channel name>`").queue();
			return;
		}

		Guild g = tafoBot.getJda().getGuildById(args[0]);

		if(g == null) {
			message.getTextChannel().sendMessage("Guild not found").queue();
			return;
		}

		List<TextChannel> channels = g.getTextChannelsByName(args[1], true);

		if(channels.size() == 0) {
			message.getTextChannel().sendMessage("Channel not found").queue();
			return;
		}

		if(channels.size() > 1) {
			String s = "Multiple channels found.\n\n";

			for(TextChannel c : channels) {
				s = s + c.getName() + "\n";
			}

			message.getTextChannel().sendMessage(s).queue();
			return;
		}

		tafoBot.logService.logChannel(channels.get(0), message.getTextChannel());

		message.getTextChannel().sendMessage("Started logging!").queue();
	}
}
