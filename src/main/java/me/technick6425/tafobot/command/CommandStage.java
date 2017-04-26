package me.technick6425.tafobot.command;

import me.technick6425.tafobot.TafoBot;
import me.technick6425.tafobot.data.Character;
import me.technick6425.tafobot.data.Match;
import me.technick6425.tafobot.data.Stage;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;

import java.awt.*;
import java.util.*;
import java.util.List;

public class CommandStage extends Command {
	private TafoBot tafoBot;

	public CommandStage(TafoBot tafoBot) {
		this.tafoBot = tafoBot;
	}

	@Override
	public void execute(Message message, String... args) {
		if(args.length != 1) {
			message.getTextChannel().sendMessage(new EmbedBuilder()
					.setTitle("Error!", null)
					.setDescription("You did not include a stage name, or included too many arguments.")
					.setColor(Color.RED)
					.build()).queue();
			return;
		}

		Stage s;
		if((s = Stage.getByName(args[0])) == null) {
			message.getTextChannel().sendMessage(new EmbedBuilder()
					.setTitle("Error!", null)
					.setDescription("I couldn't find that character name in my database.")
					.setColor(Color.RED)
					.build()).queue();
			return;
		}

		java.util.List<Match> matchesList = tafoBot.mongoDBManager.GetMatches();
		EmbedBuilder b = new EmbedBuilder();
		b.setTitle(s.readable_name, null);

		int used = 0;

		for(Match m : matchesList) {
			if(m.stage.id == s.id) used++;
		}

		if(matchesList.size() < 100) {
			b.setDescription("I don't have enough data to get accurate statistics. Please be patient while I gather more data.");
			message.getTextChannel().sendMessage(b.build()).queue();
			return;
		}

		b.addField("Usage Rate", String.valueOf((float) used / matchesList.size()), true);

		if(used < 500) {
			b.addField("Matchups/Stages", "I don't have enough data to evaluate these stats. Please be patient while I gather more data.", false);
			message.getTextChannel().sendMessage(b.build()).queue();
			return;
		}

		Map<Character,Float> winRates = new HashMap<>();

		for(Character ch : Character.values()) {
			int mwon = 0;
			int mlose = 0;

			for(Match m : matchesList) {
				if(m.p1.id == ch.id && m.stage.id == s.id) {
					if(m.p1winner) {
						mwon++;
					} else {
						mlose++;
					}
				} else if(m.p2.id == ch.id && m.stage.id == s.id) {
					if(!m.p1winner) {
						mwon++;
					} else {
						mlose++;
					}
				}
			}

			winRates.put(ch, (float) mwon / (mwon + mlose));
		}

		winRates = sortByValue(winRates);
		List<Character> chars = (List<Character>) winRates.keySet();

		b.addField("Best For",
				chars.get(chars.size() - 1).readable_name + " (" + winRates.get(chars.get(chars.size() - 1)) + "%)\n" +
						chars.get(chars.size() - 2).readable_name + " (" + winRates.get(chars.get(chars.size() - 2)) + "%\n" +
						chars.get(chars.size() - 3).readable_name + " (" + winRates.get(chars.get(chars.size() - 3)) + "%)", false);

		b.addField("Worst For",
				chars.get(0).readable_name + " (" + winRates.get(chars.get(0)) + "%)\n" +
						chars.get(1).readable_name + " (" + winRates.get(chars.get(1)) + "%)\n" +
						chars.get(2).readable_name + " (" + winRates.get(chars.get(2)) + "%)\n", true);

		message.getTextChannel().sendMessage(b.build()).queue();
	}
}
