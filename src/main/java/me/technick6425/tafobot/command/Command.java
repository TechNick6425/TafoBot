package me.technick6425.tafobot.command;

import me.technick6425.tafobot.TafoBot;
import me.technick6425.tafobot.exception.BotPermissionException;
import me.technick6425.tafobot.exception.UserPermissionException;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

import java.awt.Color;
import java.util.*;

public abstract class Command {
	public abstract void execute(Message message, String... args);

	protected void checkUserPermission(Message message, Permission permission) {
		if (!message.getGuild().getMember(message.getAuthor()).hasPermission(permission)) {
			sendFailureMessage(message, "You don't have permission for that (" + permission.getName() + ")");
			throw new UserPermissionException(permission, message.getGuild());
		}
	}

	protected void checkBotPermission(Message message, Permission permission) {
		if (!message.getGuild().getMember(message.getJDA().getSelfUser()).hasPermission(permission)) {
			sendFailureMessage(message, "I do not have the required permission (" + permission.getName() + ") for that");
			throw new BotPermissionException(permission, message.getGuild());
		}
	}

	protected User getMentionedUser(Message message, String... args) {
		if (message.getMentionedUsers().size() != 0) {
			return message.getMentionedUsers().get(0);
		}

		try {
			return TafoBot.getJda().getUserById(args[0]);
		} catch (ArrayIndexOutOfBoundsException e) {
			return null;
		}
	}

	protected String condenseArgs(String... args)
	{
		StringBuilder stringBuilder = new StringBuilder();

		for (String arg : args) {
			stringBuilder.append(arg).append(" ");
		}

		stringBuilder.trimToSize();
		return stringBuilder.toString();
	}

	protected void sendSuccessEmote(Message message) {
		message.addReaction("\uD83D\uDC4C").queue();
	}

	protected void sendSuccessMessage(TextChannel channel, String string, boolean delete, Message... messagesToDelete) {
		EmbedBuilder embed = new EmbedBuilder()
				.setTitle("Success!", null)
				.setColor(Color.GREEN)
				.setDescription(string);

		if (delete) {
			channel.sendMessage(embed.build()).queue(success -> {
				new Thread(() -> {
					try {
						Thread.sleep(3 * 1000);
					} catch (InterruptedException e) {
					}

					ArrayList<Message> messages = new ArrayList<>(Arrays.asList(messagesToDelete));
					messages.add(success);

					if (messages.size() == 1) {
						messages.get(0).delete().queue();
					} else {
						channel.deleteMessages(messages).queue();
					}
				}).start();
			});
		} else {
			channel.sendMessage(embed.build()).queue();
		}
	}

	protected void sendSuccessMessage(TextChannel channel, String string, Message... messagesToDelete) {
		sendSuccessMessage(channel, string, true, messagesToDelete);
	}

	protected void sendSuccessMessage(Message message, String string, boolean delete) {
		sendSuccessMessage(message.getTextChannel(), string, delete, message);
	}

	protected void sendSuccessMessage(Message message, String string) {
		sendSuccessMessage(message, string, true);
	}

	protected void sendFailureMessage(Message message, String string) {
		message.getChannel().sendMessage(new EmbedBuilder()
				.setTitle("Error", null)
				.setColor(Color.RED)
				.setDescription(string)
				.build()
		).queue();
	}

	public static String encodeString(String string) {
		StringBuilder builder = new StringBuilder();

		for (char c : string.toCharArray()) {
			if (c > 4095) {
				builder.append(String.format("\\u%X", (int) c));
			} else {
				builder.append(c);
			}
		}

		return builder.toString();
	}

	public static String decodeString(String string) {
		StringBuilder builder = new StringBuilder(string);

		while (builder.indexOf("\\u") != -1) {
			int index = builder.indexOf("\\u");
			String hex = builder.substring(index + 2, index + 6);
			int val = Integer.valueOf(hex, 16);

			builder.delete(index, index + 6);
			builder.insert(index, (char) val);
		}

		return builder.toString();
	}

	protected <K, V extends Comparable<? super V>> Map<K, V> sortByValue( Map<K, V> map ) {
		List<Map.Entry<K, V>> list = new LinkedList<>(map.entrySet());

		Collections.sort(list, Comparator.comparing(o -> (o.getValue())));

		Map<K, V> result = new LinkedHashMap<>();
		for(Map.Entry<K, V> entry : list)
		{
			result.put(entry.getKey(), entry.getValue());
		}

		return result;
	}

	protected void assertOwner(Message message) {
		if (!message.getAuthor().getId().equals(TafoBot.instance.config.discord.owner)) {
			sendFailureMessage(message, "You don't have permission for that (Bot Owner)");
			throw new UserPermissionException(Permission.UNKNOWN, message.getGuild());
		}
	}
}

