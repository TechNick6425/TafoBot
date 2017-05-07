package me.technick6425.tafobot;

import me.technick6425.tafobot.command.*;
import me.technick6425.tafobot.config.Config;
import me.technick6425.tafobot.listener.MessageListener;
import me.technick6425.tafobot.manager.CommandManager;
import me.technick6425.tafobot.manager.JSONConfigManager;
import me.technick6425.tafobot.manager.MongoDBManager;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

import javax.security.auth.login.LoginException;
import java.util.Date;

public class TafoBot {
	public static TafoBot instance;

	private JSONConfigManager configManager;
	public Config config;

	public MongoDBManager mongoDBManager;
	public CommandManager commandManager;
	private JDA jda;

	public final long startTime = new Date().getTime();
	public int messagesReceived = 0;

	public TafoBot() {
		instance = this;

		configManager = new JSONConfigManager(this, "tafobot.json", "config", Config.class);
		commandManager = new CommandManager(this);

		configManager.reloadConfig();
		config = (Config) configManager.getConfig();

		mongoDBManager = new MongoDBManager(this);

		commandManager.registerCommand(new CommandStats(this), "stats");
		commandManager.registerCommand(new CommandReport(this), "report", "match");
		commandManager.registerCommand(new CommandCharacter(this), "character", "matchups");
		commandManager.registerCommand(new CommandStage(this), "stage");
		commandManager.registerCommand(new CommandRemove(this), "remove");

		try {
			jda = new JDABuilder(AccountType.BOT)
					.setToken(config.discord.token)
					.setAutoReconnect(true)
					.addEventListener(new MessageListener(this))
					.buildAsync();

			jda.getPresence().setGame(Game.of("Number Crunching"));
		} catch(LoginException | RateLimitedException e) {
			e.printStackTrace();
		}
	}

	public void reload() {
		configManager.reloadConfig();
		config = (Config) configManager.getConfig();
		mongoDBManager.reload();
	}

	public static JDA getJda() {
		return instance.jda;
	}
}
