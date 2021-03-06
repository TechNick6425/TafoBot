package me.technick6425.tafobot.data;

public enum Stage {
	SMASHVILLE(0, "Smashville", "smashville", "sv", "starterville"),
	BATTLEFIELD(1, "Battlefield", "battlefield", "bf"),
	FINALDESTINATION(2, "Final Destination", "finaldestination", "fd"),
	DREAMLAND(3, "Dreamland 64", "dreamland", "dl", "dl64", "banned"),
	LYLAT(4, "Lylat Cruise", "lylat", "lylatcruise", "pineapple"),
	TOWNANDCITY(5, "Town & City", "t&c", "towncity", "tac", "townandcity", "town&city", "tnc");

	public int id;
	public String readable_name;
	public String[] aliases;

	Stage(int id, String readable_name, String... aliases) {
		this.id = id;
		this.readable_name = readable_name;
		this.aliases = aliases;
	}

	public static Stage getById(int id) {
		for(Stage s : Stage.values()) {
			if(s.id == id) return s;
		}

		return null;
	}

	public static Stage getByName(String name) {
		for(Stage s : Stage.values()) {
			for(String alias : s.aliases) {
				if(name.equalsIgnoreCase(alias)) return s;
			}
		}

		return null;
	}
}
