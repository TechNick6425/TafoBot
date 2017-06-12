package me.technick6425.tafobot.service;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.HashMap;

public class LogService {
	public LogService() {
		logEntries = new HashMap<>();
	}

	private HashMap<TextChannel,TextChannel> logEntries;

	public void logChannel(TextChannel src, TextChannel dst) {
		if(!logEntries.containsKey(src)) logEntries.put(src, dst);
	}

	public void onMessage(Message message) {
		if(logEntries.containsKey(message.getTextChannel())) {
			TextChannel dst = logEntries.get(message.getTextChannel());

			dst.sendMessage(message.getAuthor().getName() + "#" + message.getAuthor().getDiscriminator() + ": " + message.getContent()).queue();
		}
	}
}
