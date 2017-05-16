package me.technick6425.tafobot.command;

import me.technick6425.tafobot.TafoBot;
import me.technick6425.tafobot.data.Player;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;

import java.awt.*;
import java.util.List;

public class CommandMatch extends Command {
	private TafoBot tafoBot;

	public CommandMatch(TafoBot tafoBot) {
		this.tafoBot = tafoBot;
	}

	@Override
	public void execute(Message message, String... args) {
		if(!tafoBot.mongoDBManager.IsGuildAllowed(message.getGuild())) {
			message.getTextChannel().sendMessage(new EmbedBuilder()
					.setTitle("Error", null)
					.setColor(Color.RED)
					.setDescription("This server is not on the approved servers list.")
					.build()).queue();
			return;
		}
		checkUserPermission(message, Permission.MESSAGE_MANAGE);

		if(message.getMentionedUsers().size() != 2) {
			message.getTextChannel().sendMessage("You either mentioned too many users, or not enough. Remember to only mention the winner of the set, and the loser, in that order.").queue();
			return;
		}

		List<User> userList = message.getMentionedUsers();

		Player winner;
		Player loser;

		if((winner = tafoBot.mongoDBManager.FindPlayer(message.getGuild().getMember(userList.get(0)))) == null) winner = tafoBot.mongoDBManager.CreateNewPlayer(message.getGuild().getMember(userList.get(0)));
		if((loser  = tafoBot.mongoDBManager.FindPlayer(message.getGuild().getMember(userList.get(1)))) == null) loser  = tafoBot.mongoDBManager.CreateNewPlayer(message.getGuild().getMember(userList.get(1)));

		int r1 = winner.eloScore;
		int r2 = loser.eloScore;

		double R1 = Math.pow(10, (double)winner.eloScore / 400);
		double R2 = Math.pow(10, (double)loser.eloScore  / 400);

		double E1 = R1 / (R1 + R2);
		double E2 = R2 / (R1 + R2);

		double S1 = 1;
		double S2 = 0;

		r1 = (int) (r1 + tafoBot.config.eloK * (S1 - E1));
		r2 = (int) (r2 + tafoBot.config.eloK * (S2 - E2));

		winner.eloScore = r1;
		loser.eloScore = r2;

		tafoBot.mongoDBManager.UpdatePlayer(winner);
		tafoBot.mongoDBManager.UpdatePlayer(loser);

		message.getTextChannel().sendMessage(new EmbedBuilder()
				.setTitle("Added!", null)
				.setColor(Color.GREEN)
				.addField(winner.member.getEffectiveName() + "'s new score", winner.eloScore + "", false)
				.addField(loser.member.getEffectiveName() + "'s new score", loser.eloScore + "", false)
				.build()).queue();
	}
}
