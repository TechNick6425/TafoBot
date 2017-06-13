package me.technick6425.tafobot.command;

import me.technick6425.tafobot.TafoBot;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Message;

public class CommandGame extends Command {
	private TafoBot tafoBot;

	public CommandGame(TafoBot tafoBot) {
		this.tafoBot = tafoBot;
	}

	@Override
	public void execute(Message message, String... args) {
		assertOwner(message);

		if(args.length == 0) {
			message.getTextChannel().sendMessage("Didn't include a game.").queue();
			return;
		}

		String game = condenseArgs(args);
		Game target;
		Game previous = tafoBot.getJda().getPresence().getGame();

		if(previous.getType() == Game.GameType.DEFAULT) {
			target = Game.of(game);
		} else {
			target = Game.of(game, previous.getUrl());
		}

		tafoBot.getJda().getPresence().setGame(target);
	}
}
