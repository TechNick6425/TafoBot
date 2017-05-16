package me.technick6425.tafobot.command;

import me.technick6425.tafobot.TafoBot;
import me.technick6425.tafobot.data.Player;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;

public class CommandScore extends Command {
	private TafoBot tafoBot;

	public CommandScore(TafoBot tafoBot) {
		this.tafoBot = tafoBot;
	}

	@Override
	public void execute(Message message, String... args) {
		if(message.getMentionedUsers().size() == 0) {
			message.getTextChannel().sendMessage("Why don't you try that again with a mentioned user.");
			return;
		}

		Player p = tafoBot.mongoDBManager.FindPlayer(message.getGuild().getMember(message.getMentionedUsers().get(0)));

		message.getTextChannel().sendMessage(new EmbedBuilder()
				.setTitle(p.member.getEffectiveName(), null)
				.setDescription(p.eloScore + "")
				.build()).queue();
	}
}
