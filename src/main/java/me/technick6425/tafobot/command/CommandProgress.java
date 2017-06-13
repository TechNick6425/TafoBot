package me.technick6425.tafobot.command;

import me.technick6425.tafobot.TafoBot;
import me.technick6425.tafobot.command.Command;
import me.technick6425.tafobot.data.Character;
import me.technick6425.tafobot.data.Match;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandProgress extends Command {
	private TafoBot tafoBot;

	public CommandProgress(TafoBot tafoBot) {
		this.tafoBot = tafoBot;
	}

	@Override
	public void execute(Message message, String... args) {
		message.getTextChannel().sendMessage("Compiling progress (this may take a while)...").queue((m) -> {
			EmbedBuilder b = new EmbedBuilder();

			List<Match> matches = tafoBot.mongoDBManager.GetMatches();

			Map<Character,Integer> charMatches = new HashMap<>();

			int overall_progress = 0;
			int total_matches = 0;

			for(Character c : Character.values()) {
				total_matches = total_matches + 100;
				int used = 0;

				for(Match match : matches) {
					if(match.p1.id == c.id || match.p2.id == c.id) used++;
				}
				overall_progress = overall_progress + (used < 100 ? used : 100);
				charMatches.put(c, used);
			}

			charMatches = sortByValue(charMatches);
			Character[] cArray = new Character[charMatches.size()];
			Object[] keySet = charMatches.keySet().toArray();

			for(int i = 0; i < cArray.length; i++) {
				cArray[i] = (Character) keySet[i];
			}

			b.addField("Overall Progress", overall_progress + "/" + total_matches, false);

			b.addField("Most Developed Characters", "1st: " + cArray[cArray.length - 1].readable_name + " (" + charMatches.get(cArray[cArray.length - 1]) + ")" +
					"\n2nd: " + cArray[cArray.length - 2].readable_name + " (" + charMatches.get(cArray[cArray.length - 2]) + ")" +
					"\n3rd: " + cArray[cArray.length - 3].readable_name + " (" + charMatches.get(cArray[cArray.length - 3]) + ")", false);

			b.addField("Least Developed Characters", "1st: " + cArray[0].readable_name + " (" + charMatches.get(cArray[0]) + ")" +
					"\n2nd: " + cArray[1].readable_name + " (" + charMatches.get(cArray[1]) + ")" +
					"\n3rd: " + cArray[2].readable_name + " (" + charMatches.get(cArray[2]) + ")", false);

			m.editMessage(b.build()).queue();
		});
	}
}
