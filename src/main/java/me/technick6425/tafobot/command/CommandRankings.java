package me.technick6425.tafobot.command;

import me.technick6425.tafobot.TafoBot;
import me.technick6425.tafobot.data.Player;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandRankings extends Command {
	private TafoBot tafoBot;

	public CommandRankings(TafoBot tafoBot) {
		this.tafoBot = tafoBot;
	}

	@Override
	public void execute(Message message, String... args) {
		List<Player> players = tafoBot.mongoDBManager.GetAllPlayers(message.getGuild());

		Map<Player,Integer> eloScores = new HashMap<>();
		for(Player p : players) {
			eloScores.put(p, p.eloScore);
		}

		eloScores = sortByValue(eloScores);

		List<Player> sortedPlayers = new ArrayList<>();
		for(Player p : eloScores.keySet()) {
			sortedPlayers.add(p);
		}

		EmbedBuilder b = new EmbedBuilder();

		for(int i = 0; i < (eloScores.size() < 10 ? eloScores.size() : 10); i++) {
			b.addField((i + 1) + ((i + 1) == 1 ? "st" : ((i + 1) == 2 ? "nd" : ((i + 1) == 3 ? "rd" : "th"))), sortedPlayers.get(eloScores.size() - 1 - i).member.getEffectiveName() + ": " + sortedPlayers.get(eloScores.size() - 1 - i).eloScore, true);
		}

		b.setTitle(message.getGuild().getName() + " Rankings", null);
		message.getTextChannel().sendMessage(b.build()).queue();
	}
}
