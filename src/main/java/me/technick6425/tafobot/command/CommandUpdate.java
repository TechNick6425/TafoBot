package me.technick6425.tafobot.command;

import me.technick6425.tafobot.TafoBot;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;

import java.awt.*;
import java.io.IOException;

public class CommandUpdate extends Command {
	private TafoBot tafoBot;

	public CommandUpdate(TafoBot tafoBot) {
		this.tafoBot = tafoBot;
	}

	@Override
	public void execute(Message message, String... args) {
		assertOwner(message);
		try {
			Runtime.getRuntime().exec("git pull");

			message.getTextChannel().sendMessage(new EmbedBuilder()
					.setColor(Color.RED)
					.setTitle("Success", null)
					.setDescription("Bot updated to latest git version. Restart with `.restart`")
					.build()).queue();
		} catch (IOException e) {
			e.printStackTrace();
			message.getTextChannel().sendMessage(new EmbedBuilder()
					.setColor(Color.RED)
					.setTitle("Error", "https://stackoverflow.com/")
					.setDescription("There was an error updating.")
					.build()).queue();
		}
	}
}
