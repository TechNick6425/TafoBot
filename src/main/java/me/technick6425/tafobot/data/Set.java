package me.technick6425.tafobot.data;

import net.dv8tion.jda.core.entities.User;

public class Set {
	public User winner;
	public User loser;

	public Set() {}
	public Set(User winner, User loser) {
		this.winner = winner;
		this.loser = loser;
	}
}
