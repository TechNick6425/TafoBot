package me.technick6425.tafobot.config;

public class Config {
	public class Discord {
		public String token;
		public String owner;
	}

	public class MongoDB {
		public String host;
		public int port;
		public String database;
	}

	public Discord discord;
	public MongoDB mongoDB;
	public String commandPrefix;
}
