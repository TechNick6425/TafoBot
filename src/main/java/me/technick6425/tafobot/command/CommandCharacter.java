package me.technick6425.tafobot.command;

import me.technick6425.tafobot.TafoBot;
import me.technick6425.tafobot.data.Character;
import me.technick6425.tafobot.data.Match;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;

import java.awt.Color;
import java.util.*;

public class CommandCharacter extends Command {
	private TafoBot tafoBot;

	public CommandCharacter(TafoBot tafoBot) {
		this.tafoBot = tafoBot;
	}

	@Override
	public void execute(Message message, String... args) {
		if(args.length != 1) {
			message.getTextChannel().sendMessage(new EmbedBuilder()
					.setTitle("Error!", null)
					.setDescription("You did not include a character name, or included too many arguments.")
					.setColor(Color.RED)
					.build()).queue();
			return;
		}

		Character c;
		if((c = Character.getByName(args[0])) == null) {
			message.getTextChannel().sendMessage(new EmbedBuilder()
					.setTitle("Error!", null)
					.setDescription("I couldn't find that character name in my database.")
					.setColor(Color.RED)
					.build()).queue();
			return;
		}

		List<Match> matchesList = tafoBot.mongoDBManager.GetMatches();
		EmbedBuilder b = new EmbedBuilder();
		b.setTitle(c.readable_name, null);

		int won = 0;
		int lose = 0;
		int used = 0;

		for(Match m : matchesList) {
			if(m.p1.id == c.id) {
				used++;
				if(m.p1winner) {
					won++;
				} else {
					lose++;
				}
			} else if(m.p2.id == c.id) {
				used++;
				if(!m.p1winner) {
					won++;
				} else {
					lose++;
				}
			}
		}

		if(used < 100) {
			b.setDescription("I don't have enough data to get accurate statistics. Please be patient while I gather more data.");
			message.getTextChannel().sendMessage(b.build()).queue();
			return;
		}

		b.addField("Win Rate", String.valueOf((float) won / (won + lose)), false);
		b.addField("Usage Rate", String.valueOf((float) used / matchesList.size()), true);

		if(matchesList.size() < 500) {
			b.addField("Matchups/Stages", "I don't have enough data to evaluate these stats. Please be patient while I gather more data.", false);
			message.getTextChannel().sendMessage(b.build()).queue();
			return;
		}

		Map<Character,Float> matchups = new HashMap<>();

		for(Character ch : Character.values()) {
			int mwon = 0;
			int mlose = 0;

			for(Match m : matchesList) {
				if(m.p1.id == c.id && m.p2.id == ch.id) {
					if(m.p1winner) {
						mwon++;
					} else {
						mlose++;
					}
				} else if(m.p2.id == c.id && m.p1.id == ch.id) {
					if(!m.p1winner) {
						mwon++;
					} else {
						mlose++;
					}
				}
			}

			matchups.put(ch, (float) mwon / (mwon + mlose));
		}

		matchups = sortByValue(matchups);
		List<Character> chars = (List<Character>) matchups.keySet();

		b.addField("Best Matchups",
				chars.get(chars.size() - 1).readable_name + " (" + matchups.get(chars.get(chars.size() - 1)) + "%)\n" +
						chars.get(chars.size() - 2).readable_name + " (" + matchups.get(chars.get(chars.size() - 2)) + "%\n" +
						chars.get(chars.size() - 3).readable_name + " (" + matchups.get(chars.get(chars.size() - 3)) + "%)", false);

		b.addField("Worst Matchups",
				chars.get(0).readable_name + " (" + matchups.get(chars.get(0)) + "%)\n" +
						chars.get(1).readable_name + " (" + matchups.get(chars.get(1)) + "%)\n" +
						chars.get(2).readable_name + " (" + matchups.get(chars.get(2)) + "%)\n", true);

		message.getTextChannel().sendMessage(b.build()).queue();
	}
}
