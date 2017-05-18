package me.technick6425.tafobot.command;

import me.technick6425.tafobot.TafoBot;
import me.technick6425.tafobot.data.Character;
import me.technick6425.tafobot.data.Match;
import me.technick6425.tafobot.data.Stage;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;

import java.awt.Color;
import java.text.DecimalFormat;
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
			b.setDescription("I don't have enough data to get accurate statistics (" + used + "/100). Please be patient while I gather more data.");
			message.getTextChannel().sendMessage(b.build()).queue();
			return;
		}

		DecimalFormat df = new DecimalFormat("###.##");
		b.addField("Win Rate", String.valueOf(df.format((double) won / (won + lose) * 100)), false);
		b.addField("Usage Rate", String.valueOf(df.format((double) used / matchesList.size() * 100)), true);

		if(used < 500) {
			b.addField("Matchups/Stages", "I don't have enough data to evaluate these stats (" + used + "/500). Please be patient while I gather more data.", false);
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
				chars.get(chars.size() - 1).readable_name + " (" + df.format(matchups.get(chars.get(chars.size() - 1)) * 100) + "%)\n" +
						chars.get(chars.size() - 2).readable_name + " (" + df.format(matchups.get(chars.get(chars.size() - 2)) * 100) + "%\n" +
						chars.get(chars.size() - 3).readable_name + " (" + df.format(matchups.get(chars.get(chars.size() - 3)) * 100) + "%)", false);

		b.addField("Worst Matchups",
				chars.get(0).readable_name + " (" + df.format(matchups.get(chars.get(0)) * 100) + "%)\n" +
						chars.get(1).readable_name + " (" + df.format(matchups.get(chars.get(1)) * 100) + "%)\n" +
						chars.get(2).readable_name + " (" + df.format(matchups.get(chars.get(2)) * 100) + "%)\n", true);

		Map<Stage,Double> stageFloatMap = new HashMap<>();

		for(Stage s : Stage.values()) {
			int swon = 0;
			int slose = 0;

			for(Match m : matchesList) {
				if(m.p1.id == c.id && m.stage.id == s.id) {
					if(m.p1winner) {
						swon++;
					} else {
						slose++;
					}
				} else if(m.p2.id == c.id && m.stage.id == s.id) {
					if(!m.p1winner) {
						swon++;
					} else {
						slose++;
					}
				}
			}

			stageFloatMap.put(s, (double) swon / (swon + slose));
		}

		stageFloatMap = sortByValue(stageFloatMap);
		List<Stage> stages = (List<Stage>) stageFloatMap.keySet();

		b.addField("Best Stages",
				stages.get(stages.size() - 1).readable_name + " (" + df.format(stageFloatMap.get(stages.get(stages.size() - 1)) * 100) + "%)\n" +
						stages.get(stages.size() - 2).readable_name + " (" + df.format(stageFloatMap.get(stages.get(stages.size() - 2)) * 100) + "%)\n" +
						stages.get(stages.size() - 3).readable_name + " (" + df.format(stageFloatMap.get(stages.get(stages.size() - 3)) * 100) + "%)", false);

		b.addField("Worst Stages",
				stages.get(0).readable_name + " (" + df.format(stageFloatMap.get(stages.get(0)) * 100) + "%)\n" +
						stages.get(1).readable_name + " (" + df.format(stageFloatMap.get(stages.get(1)) * 100) + "%)\n" +
						stages.get(2).readable_name + " (" + df.format(stageFloatMap.get(stages.get(2)) * 100) + "%)", true);

		message.getTextChannel().sendMessage(b.build()).queue();
	}
}
