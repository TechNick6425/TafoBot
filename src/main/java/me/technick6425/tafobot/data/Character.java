package me.technick6425.tafobot.data;

public enum Character {

	MARIO(0, "Mario", "mario", "actuallystupid"),
	LUIGI(1, "Luigi", "luigi"),
	PEACH(2, "Peach", "peach"),
	BOWSER(3, "Bowser", "bowser"),
	YOSHI(4, "Yoshi", "yoshi"),
	ROSA(5, "Rosalina & Luma", "rosalina", "rosa", "rosaluma"),
	BOWSERJR(6, "Bowser Jr.", "bowserjr"),
	WARIO(7, "Wario", "wario", "wah"),
	GAMEANDWATCH(8, "Game & Watch", "gamenwatch", "gameandwatch", "gnw", "g&w"),
	DONKEYKONG(9, "Donkey Kong", "donkeykong", "donkey", "dk"),
	DIDDYKONG(10, "Diddy Kong", "diddykong", "diddy", "bananas"),
	LINK(11, "Link", "link", "lonk"),
	ZELDA(12, "Zelda", "zelda"),
	SHEIK(13, "Sheik", "sheik"),
	GANON(14, "Ganondorf", "ganon", "ganondorf"),
	TOONLINK(15, "Toon Link", "toonlink", "tink"),
	SAMUS(16, "Samus", "samus"),
	ZEROSUIT(17, "Zero Suit Samus", "zerosuitsamus", "zerosuit", "zamus", "zss"),
	PIT(18, "Pit", "pit"),
	PALUTENA(19, "Palutena", "palutena", "palu"),
	MARTH(20, "Marth", "marth"),
	IKE(21, "Ike", "ike"),
	ROBIN(22, "Robin", "robin"),
	KIRBY(23, "Kirby", "kirby", "kirb"),
	DEDEDE(24, "King Dedede", "kingdedede", "dedede", "ddd"),
	METAKNIGHT(25, "Meta Knight", "metaknight", "meta"),
	MAC(26, "Little Mac", "littlemac", "mac"),
	FOX(27, "Fox", "fox", "toryah"),
	FALCO(28, "Falco", "falco", "flaco"),
	PIKACHU(29, "Pikachu", "pikachu", "pika"),
	CHARIZARD(30, "Charizard", "charizard", "charizord", "char"),
	LUCARIO(31, "Lucario", "lucario"),
	JIGGS(32, "Jigglypuff", "jigglypuff", "jiggs", "jigglybuff"),
	GRENINJA(33, "Greninja", "greninja", "ninja", "genji", "gren"),
	DUCKHUNT(34, "Duck Hunt", "duckhuntdog", "duckhuntduo", "dhd", "dh"),
	ROB(35, "R.O.B.", "rob", "r.o.b.", "walle"),
	NESS(36, "Ness", "ness", "bthrow", "pkfire"),
	FALCON(37, "Captain Falcon", "falcon", "captainfalcon", "s2j"),
	VILLAGER(38, "Villager", "villager", "villy"),
	OLIMAR(39, "Olimar", "olimar", "pikmin", "alph"),
	WIIFIT(40, "Wii Fit Trainer", "wiifit", "wiifittrainer", "wft"),
	DRMARIO(41, "Dr. Mario", "drmario", "doctor", "cocktor", "toptier"),
	DPIT(42, "Dark Pit", "dpit", "pittoo", "darkpit"),
	LUCINA(43, "Lucina", "lucina", "lucian", "girlmarth"),
	SHULK(44, "Shulk", "shulk", "shulkle", "monadoboy", "monandoboy"),
	PACMAN(45, "Pac-Man", "pacman", "pac"),
	MEGAMAN(46, "Mega Man", "megaman", "jumpnshoot"),
	SONIC(47, "Sonic", "sonic", "knuckles", "cuckles", "sanic", "coldsteel"),
	MIIBRAWLER(48, "Brawler", "brawler", "lightning"),
	MIISWORDFIGHTER(49, "Swordfighter", "swordfighter"),
	MIIGUNNER(50, "Gunner", "gunner", "shooty"),
	MEWTWO(51, "Mewtwo", "mewtwo", "mrmewtwo", "winston"),
	LUCAS(52, "Lucas", "lucas", "top20"),
	ROY(53, "Roy", "roy", "top10", "ourboy"),
	RYU(54, "Ryu", "ryu", "fadc", "sf"),
	CLOUD(55, "Cloud", "cloud", "limit", "ffvii", "adventchildren"),
	CORRIN(56, "Corrin", "corrin", "fates"),
	BAYO(57, "Bayonetta", "bayonetta", "bayo"),
	MIIBRAWLERCUSTOM(58, "Gunner (Custom)", "bralwercustom", "lightningcustom"),
	MIISWORDFIGHTERCUSTOM(59, "Swordfighter (Custom)", "swordfightercustom"),
	MIIGUNNERCUSTOM(60, "Gunner (Custom)", "gunnercustom", "shootycustom");

	public int id;
	public String readable_name;
	public String[] aliases;

	Character(int id, String readable_name, String... aliases) {
		this.id = id;
		this.readable_name = readable_name;
		this.aliases = aliases;
	}

	public static Character getById(int id) {
		for(Character c : Character.values()) {
			if(c.id == id) return c;
		}

		return null;
	}

	public static Character getByName(String name) {
		for(Character c : Character.values()) {
			for(String alias : c.aliases) {
				if(name.equalsIgnoreCase(alias)) return c;
			}
		}

		return null;
	}
}
