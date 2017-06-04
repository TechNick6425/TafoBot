package me.technick6425.tafobot.command;

import me.technick6425.tafobot.TafoBot;
import me.technick6425.tafobot.data.Character;
import me.technick6425.tafobot.data.Match;
import me.technick6425.tafobot.data.Stage;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;

import java.awt.*;

public class CommandReport extends Command {
	private TafoBot tafoBot;

	public CommandReport(TafoBot tafoBot) {
		this.tafoBot = tafoBot;
	}

	@Override
	public void execute(Message message, String... args) {
		if(!tafoBot.mongoDBManager.IsGuildAllowed(message.getGuild())) {
			message.getTextChannel().sendMessage(new EmbedBuilder()
					.setTitle("Error", null)
					.setColor(Color.RED)
					.setDescription("This server is not on the approved servers list.")
					.build()).queue();
			return;
		}
		checkUserPermission(message, Permission.MESSAGE_MANAGE);

		if(args.length < 4) {
			message.getTextChannel().sendMessage(new EmbedBuilder()
					.addField("Command", tafoBot.config.commandPrefix + "report <p1 character> <p2 character> <stage> <winner: p1/p2> [silent]", true)
					.addField("Characters/Stages", "The name of the character/stage being played/played on as one word. Some abbreviations may work.", false)
					.addField("Winner", "Either P1 or P2, for whichever character won the game.", false).build()).queue();
			return;
		}

		boolean silent = args.length == 5 && args[4].equalsIgnoreCase("silent");

		Character[] chars = Character.values();
		Stage[] stages = Stage.values();

		Character p1 = null;
		Character p2 = null;
		Stage stage = null;

		// Eval P1
		for(Character c : chars) {
			for(String alias : c.aliases) {
				if(alias.equalsIgnoreCase(args[0])) {
					p1 = c;
					break;
				}
			}

			if(p1 != null) break;
		}

		if(p1 == null) {
			if(silent) {
				message.addReaction("\uD83D\uDC4E").queue();
			} else {
				message.getTextChannel().sendMessage(new EmbedBuilder()
						.setTitle("Error", null)
						.setDescription("Player 1's character did not match any characters in my database. Please try again.")
						.setColor(Color.RED)
						.build()).queue();
			}
			return;
		}

		// Eval P2
		for(Character c : chars) {
			for(String alias : c.aliases) {
				if(alias.equalsIgnoreCase(args[1])) {
					p2 = c;
					break;
				}
			}

			if(p2 != null) break;
		}

		if(p2 == null) {
			if(silent) {
				message.addReaction("\uD83D\uDC4E").queue();
			} else {
				message.getTextChannel().sendMessage(new EmbedBuilder()
						.setTitle("Error", null)
						.setDescription("Player 2's character did not match any characters in my database. Please try again.")
						.setColor(Color.RED)
						.build()).queue();
			}
			return;
		}

		for(Stage s : stages) {
			for(String alias : s.aliases) {
				if(alias.equalsIgnoreCase(args[2])) {
					stage = s;
					break;
				}
			}

			if(stage != null) break;
		}

		if(stage == null) {
			if(silent) {
				message.addReaction("\uD83D\uDC4E").queue();
			} else {
				message.getTextChannel().sendMessage(new EmbedBuilder()
						.setTitle("Error", null)
						.setDescription("The stage did not match any stages in my database. Please try again.")
						.setColor(Color.RED)
						.build()).queue();
			}
			return;
		}

		boolean p1winner = false;

		if(args[3].equalsIgnoreCase("p1")) p1winner = true;
		else if(args[3].equalsIgnoreCase("p2")) p1winner = false;
		else {
			if(silent) {
				message.addReaction("\uD83D\uDC4E").queue();
			} else {
				message.getTextChannel().sendMessage(new EmbedBuilder()
						.setTitle("Error", null)
						.setDescription("The winner did not match any of my search patterns. Please try again.")
						.setColor(Color.RED)
						.build()).queue();
			}
			return;
		}

		try {
			String id = tafoBot.mongoDBManager.RegisterMatch(new Match(p1, p2, stage, p1winner));

			if(silent) {
				message.getAuthor().openPrivateChannel().queue((channel) -> channel.sendMessage("Match ID: " + id).queue());
				message.addReaction("\uD83D\uDC4D").queue();
			} else {
				message.getTextChannel().sendMessage(new EmbedBuilder()
						.setTitle("Added!", null)
						.addField("Match ID", id, true)
						.addField("P1 Character", p1.readable_name, false)
						.addField("P2 Character", p2.readable_name, true)
						.addField("Stage", stage.readable_name, false)
						.addField("Winner", (p1winner ? "P1" : "P2"), true)
						.setColor(Color.GREEN)
						.build()).queue();
			}
		} catch(Exception e) {
			e.printStackTrace();
			if(silent) {
				message.addReaction("\uD83D\uDC4E").queue();
			} else {
				message.getTextChannel().sendMessage(new EmbedBuilder()
						.setTitle("Internal Error!", null)
						.setColor(Color.RED)
						.setDescription("There has been an internal error in the bot. Please contact " + tafoBot.getJda().getUserById(tafoBot.config.discord.owner).getAsMention() + " for assistance.")
						.addField("Notes for developers", e.getMessage(), false)
						.build()).queue();
			}
		}
	}
}
