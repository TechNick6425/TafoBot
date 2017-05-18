package me.technick6425.tafobot.command;

import me.technick6425.tafobot.TafoBot;
import me.technick6425.tafobot.data.Set;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;

import java.util.List;

public class CommandSetCount extends Command {
	private TafoBot tafoBot;

	public CommandSetCount(TafoBot tafoBot) {
		this.tafoBot = tafoBot;
	}

	@Override
	public void execute(Message message, String... args) {
		if(message.getMentionedUsers().size() != 2) {
			message.getTextChannel().sendMessage("You either mentioned too many users, or not enough. Remember to only mention the winner of the set, and the loser, in that order.").queue();
			return;
		}

		List<User> userList = message.getMentionedUsers();

		List<Set> sets = tafoBot.mongoDBManager.GetAllSets();
		int p1win = 0;
		int p2win = 0;

		for(Set s : sets) {
			if(s.winner.getId().equals(userList.get(0).getId()) && s.loser .getId().equals(userList.get(1).getId())) p1win++;
			if(s.loser .getId().equals(userList.get(0).getId()) && s.winner.getId().equals(userList.get(1).getId())) p2win++;
		}

		message.getTextChannel().sendMessage(new EmbedBuilder()
				.setTitle(message.getGuild().getMember(userList.get(0)).getEffectiveName() + " vs. " + message.getGuild().getMember(userList.get(1)).getEffectiveName(), null)
				.setDescription(p1win + " - " + p2win)
				.build()).queue();
	}
}
