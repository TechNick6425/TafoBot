package me.technick6425.tafobot.data;

import me.technick6425.tafobot.TafoBot;
import net.dv8tion.jda.core.entities.Member;

public class Player {
	public Member member;
	public int eloScore;

	public Player() {}

	public Player(Member member) {
		this.member = member;
		this.eloScore = TafoBot.instance.config.startingEloScore;
	}

	public Player(Member member, int eloScore) {
		this.member = member;
		this.eloScore = eloScore;
	}
}
