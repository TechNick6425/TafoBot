package me.technick6425.tafobot.command;

import me.technick6425.tafobot.TafoBot;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Message;

public class CommandStream extends Command {
	private TafoBot tafoBot;

	public CommandStream(TafoBot tafoBot) {
		this.tafoBot = tafoBot;
	}

	@Override
	public void execute(Message message, String... args) {
		assertOwner(message);

		if(args.length == 0) {
			message.getTextChannel().sendMessage("Didn't include a URL.").queue();
		}

		if(args[0].equals("off")) {
			tafoBot.getJda().getPresence().setGame(Game.of(tafoBot.getJda().getPresence().getGame().getName()));
			return;
		}

		String url = args[0];
		Game target;
		Game previous = tafoBot.getJda().getPresence().getGame();

		if(!Game.isValidStreamingUrl(url)) {
			message.getTextChannel().sendMessage("Not a valid streaming URL.").queue();
			return;
		}

		target = Game.of(previous.getName(), url);

		tafoBot.getJda().getPresence().setGame(target);
	}
}
