package me.technick6425.tafobot.command;

import me.technick6425.tafobot.TafoBot;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;

import java.awt.*;

public class CommandRemoveServer extends Command {
	private TafoBot tafoBot;

	public CommandRemoveServer(TafoBot tafoBot) {
		this.tafoBot = tafoBot;
	}

	@Override
	public void execute(Message message, String... args) {
		assertOwner(message);

		if(!tafoBot.mongoDBManager.IsGuildAllowed(message.getGuild())) {
			message.getTextChannel().sendMessage(new EmbedBuilder()
					.setTitle("Error", null)
					.setColor(Color.RED)
					.setDescription("This server isn't approved.")
					.build()).queue();
			return;
		}

		if(tafoBot.mongoDBManager.RemoveGuild(message.getGuild())) {
			message.getTextChannel().sendMessage(new EmbedBuilder()
					.setTitle("Removed!", null)
					.setColor(Color.GREEN)
					.setDescription("This server has been removed from the list of approved servers.")
					.build()).queue();
		} else {
			message.getTextChannel().sendMessage(new EmbedBuilder()
					.setTitle("Error", null)
					.setColor(Color.GREEN)
					.setDescription("There was an error removing the server.")
					.build()).queue();
		}
	}
}
