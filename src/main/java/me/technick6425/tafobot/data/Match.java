package me.technick6425.tafobot.data;

public class Match {
	public Character p1;
	public Character p2;
	public Stage stage;
	public boolean p1winner;

	public Match() {}

	public Match(Character p1, Character p2, Stage stage, boolean p1winner) {
		this.p1 = p1;
		this.p2 = p2;
		this.stage = stage;
		this.p1winner = p1winner;
	}
}
