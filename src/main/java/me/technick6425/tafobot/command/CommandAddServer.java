package me.technick6425.tafobot.command;

import me.technick6425.tafobot.TafoBot;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;

import java.awt.*;

public class CommandAddServer extends Command {
	private TafoBot tafoBot;

	public CommandAddServer(TafoBot tafoBot) {
		this.tafoBot = tafoBot;
	}

	@Override
	public void execute(Message message, String... args) {
		assertOwner(message);

		if(tafoBot.mongoDBManager.IsGuildAllowed(message.getGuild())) {
			message.getTextChannel().sendMessage(new EmbedBuilder()
					.setTitle("Error", null)
					.setColor(Color.RED)
					.setDescription("This server is already approved.")
					.build()).queue();
			return;
		}

		tafoBot.mongoDBManager.RegisterGuild(message.getGuild());

		message.getTextChannel().sendMessage(new EmbedBuilder()
				.setTitle("Registered!", null)
				.setColor(Color.GREEN)
				.setDescription("This server has been added to the list of approved servers.")
				.build()).queue();
	}
}
