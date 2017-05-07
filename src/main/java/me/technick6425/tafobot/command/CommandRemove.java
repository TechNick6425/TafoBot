package me.technick6425.tafobot.command;

import me.technick6425.tafobot.TafoBot;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;

import java.awt.*;

public class CommandRemove extends Command {
	private TafoBot tafoBot;

	public CommandRemove(TafoBot tafoBot) {
		this.tafoBot = tafoBot;
	}

	@Override
	public void execute(Message message, String... args) {
		if(args.length != 1) {
			message.getTextChannel().sendMessage(new EmbedBuilder()
					.setTitle("Error!", null)
					.setDescription("You didn't include a match ID!")
					.setColor(Color.RED)
					.build()).queue();
			return;
		}

		if(tafoBot.mongoDBManager.RemoveMatch(args[0])) {
			message.getTextChannel().sendMessage(new EmbedBuilder()
					.setTitle("Success", null)
					.setDescription("Match removed!")
					.setColor(Color.GREEN)
					.build()).queue();
			return;
		}

		message.getTextChannel().sendMessage(new EmbedBuilder()
				.setTitle("Error!", null)
				.setDescription("That is not a valid match ID!")
				.build()).queue();
		return;
	}
}
