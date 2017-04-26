package me.technick6425.tafobot.manager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public abstract class ConfigManager {
	private Object app;

	protected String name;
	protected File file;
	protected File dataFolder;

	protected ConfigManager(Object app, String name, String folder) {
		this.app = app;
		this.name = name;
		dataFolder = new File(folder);
	}

	public abstract Object getConfig();
	public abstract void reloadConfig();
	public abstract void saveConfig();

	public Object getApp() {
		return app;
	}

	protected void getConfigFile() {
		if (!dataFolder.exists()) {
			dataFolder.mkdirs();
		}

		file = new File(dataFolder, name);

		if (!file.exists()) {
			createConfig();
		}
	}

	public void replaceConfig() {
		deleteConfig();
		reloadConfig();
	}

	public void createConfig() {
		try {
			Files.copy(app.getClass().getClassLoader().getResourceAsStream(name), file.toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void deleteConfig() {
		file.delete();
	}
}
