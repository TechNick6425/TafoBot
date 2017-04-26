package me.technick6425.tafobot.manager;

import me.technick6425.tafobot.TafoBot;
import me.technick6425.tafobot.command.Command;
import net.dv8tion.jda.core.entities.Message;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class CommandManager {
	private TafoBot tafoBot;
	private HashMap<String[],Command> commands = new HashMap<>();

	public CommandManager(TafoBot tafoBot) {
		this.tafoBot = tafoBot;
	}

	public void registerCommand(Command command, String... aliases) {
		commands.put(aliases, command);
	}

	public void executeCommand(Message message) {

		String string = message.getRawContent().substring(1);
		if (string.equals("")) return;

		List<String> parts = Arrays.asList(string.split(" "));

		String command = parts.get(0).toLowerCase();
		String[] args = parts.subList(1, parts.size()).toArray(new String[parts.size() - 1]);

		executeCommand(command, message, args);
	}

	public void executeCommand(String command, Message message, String... args) {
		for (String[] aliases : commands.keySet()) {
			for(String alias : aliases) {
				if(command.equals(alias)) {
					try {
						commands.get(aliases).execute(message, args);
					} catch (Exception e) {
						System.out.println(e.getMessage());
					}
					return;
				}
			}
		}
	}

}
