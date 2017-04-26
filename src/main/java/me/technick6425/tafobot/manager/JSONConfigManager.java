package me.technick6425.tafobot.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;

public class JSONConfigManager extends ConfigManager {
	private Class configClass;
	private Object config;

	public JSONConfigManager(Object app, String name, String folder, Class configClass) {
		super(app, name, folder);
		this.configClass = configClass;
		reloadConfig();
	}

	@Override
	public Object getConfig() {
		return config;
	}

	@Override
	public void reloadConfig() {
		getConfigFile();
		try {
			Gson gson = new Gson();
			config = gson.fromJson(new FileReader(file), configClass);
			saveConfig();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void saveConfig() {
		try {
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			Writer writer = new FileWriter(file);
			String json = gson.toJson(config);
			writer.write(json);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void createConfig() {
		try {
			Files.copy(getApp().getClass().getClassLoader().getResourceAsStream(name), file.toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

