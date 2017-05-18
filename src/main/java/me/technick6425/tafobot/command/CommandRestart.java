package me.technick6425.tafobot.command;

import me.technick6425.tafobot.TafoBot;
import net.dv8tion.jda.core.entities.Message;

import java.io.IOException;

public class CommandRestart extends Command {
	private TafoBot tafoBot;

	public CommandRestart(TafoBot tafoBot) {
		this.tafoBot = tafoBot;
	}

	@Override
	public void execute(Message message, String... args) {
		assertOwner(message);
		try {
			Runtime.getRuntime().exec("./gradlew run");
			message.getTextChannel().sendMessage("Restarting...").queue((x) -> {
				tafoBot.getJda().shutdown();
				System.exit(0);
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
