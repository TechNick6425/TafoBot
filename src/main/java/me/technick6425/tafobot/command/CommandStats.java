package me.technick6425.tafobot.command;

import me.technick6425.tafobot.TafoBot;
import me.technick6425.tafobot.data.Character;
import me.technick6425.tafobot.data.Match;
import me.technick6425.tafobot.data.Stage;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import me.technick6425.tafobot.util.DateUtils;

import java.awt.*;
import java.util.ArrayList;

public class CommandStats extends Command {
	private TafoBot tafoBot;

	public CommandStats(TafoBot tafoBot) {
		this.tafoBot = tafoBot;
	}

	@Override
	public void execute(Message message, String... args) {
		Runtime runtime = Runtime.getRuntime();

		EmbedBuilder embed = new EmbedBuilder().setTitle("Stats", "https://jarvis.will.sr").setColor(Color.GREEN);

		ArrayList<Character> chars = new ArrayList<Character>();
		ArrayList<Stage> stages = new ArrayList<Stage>();

		for(Match m : tafoBot.mongoDBManager.GetMatches()) {
			if(!chars.contains(m.p1)) chars.add(m.p1);
			if(!chars.contains(m.p2)) chars.add(m.p2);
			if(!stages.contains(m.stage)) stages.add(m.stage);
		}

		embed.addField("Uptime", DateUtils.formatDateDiff(tafoBot.startTime), true);
		embed.addField("Ping", message.getJDA().getPing() + "ms", true);
		embed.addField("Guilds", message.getJDA().getGuilds().size() + "", true);
		embed.addField("Text channels", message.getJDA().getTextChannels().size() + "", true);
		embed.addField("Voice channels", message.getJDA().getVoiceChannels().size() + "", true);
		embed.addField("Users", message.getJDA().getUsers().size() + "", true);
		embed.addField("Messages sent", tafoBot.messagesReceived + "", true);
		embed.addField("Threads", Thread.activeCount() + "", true);
		embed.addField("Memory", ((runtime.totalMemory() - runtime.freeMemory()) / (1024 * 1024)) + "MB / " + (runtime.maxMemory() / (1024 * 1024)) + "MB", true);
		embed.addField("Total Matches", tafoBot.mongoDBManager.GetMatches().size() + "", true);
		embed.addField("Unique Characters", chars.size() + "", true);
		embed.addField("Unique Stages", stages.size() + "", true);

		message.getChannel().sendMessage(embed.build()).queue();
	}
}
