package me.technick6425.tafobot.manager;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import me.technick6425.tafobot.TafoBot;
import me.technick6425.tafobot.data.Character;
import me.technick6425.tafobot.data.Match;
import me.technick6425.tafobot.data.Player;
import me.technick6425.tafobot.data.Stage;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.*;

public class MongoDBManager {
	private TafoBot tafoBot;

	private MongoClient mongoClient;
	private MongoDatabase mongoDatabase;

	public MongoDBManager(TafoBot tafoBot) {
		this.tafoBot = tafoBot;
		reload();
	}

	public void reload() {
		mongoClient = new MongoClient(tafoBot.config.mongoDB.host, tafoBot.config.mongoDB.port);
		mongoDatabase = mongoClient.getDatabase(tafoBot.config.mongoDB.database);
	}

	public String RegisterMatch(Match match) {
		Document d = new Document("player1", match.p1.id).append("player2", match.p2.id).append("stage", match.stage.id).append("winner", match.p1winner);
		MongoCollection<Document> collection = mongoDatabase.getCollection("matches");
		collection.insertOne(d);
		return ((ObjectId)d.get("_id")).toHexString();
	}

	public List<Match> GetMatches() {
		MongoCollection<Document> collection = mongoDatabase.getCollection("matches");

		ArrayList<Document> documents = new ArrayList<>();
		collection.find().into(documents);

		ArrayList<Match> matches = new ArrayList<>();
		for (Document d: documents) {
			matches.add(new Match(Character.getById(d.getInteger("player1")), Character.getById(d.getInteger("player2")), Stage.getById(d.getInteger("stage")), d.getBoolean("winner")));
		}
		return matches;
	}

	public boolean RemoveMatch(String id) {
		MongoCollection<Document> collection = mongoDatabase.getCollection("matches");

		BasicDBObject d = new BasicDBObject();
		d.put("_id", new ObjectId(id));

		DeleteResult res = collection.deleteMany(d);
		if(res.getDeletedCount() == 0 || !res.wasAcknowledged()) return false;

		return true;
	}

	public void RegisterGuild(Guild g) {
		MongoCollection<Document> collection = mongoDatabase.getCollection("allowed_guilds");
		collection.insertOne(new Document("guild_id", g.getId()));
	}

	public boolean RemoveGuild(Guild g) {
		MongoCollection<Document> collection = mongoDatabase.getCollection("allowed_guilds");

		BasicDBObject d = new BasicDBObject();
		d.put("guild_id", g.getId());

		DeleteResult res = collection.deleteMany(d);
		if(res.getDeletedCount() == 0 || !res.wasAcknowledged()) return false;

		return true;
	}

	public boolean IsGuildAllowed(Guild g) {
		MongoCollection<Document> collection = mongoDatabase.getCollection("allowed_guilds");
		BasicDBObject d = new BasicDBObject();
		d.put("guild_id", g.getId());

		return collection.find(d).first() != null;
	}

	public Player CreateNewPlayer(Member member) {
		System.out.println("Creating player for " + member.getEffectiveName() + " on " + member.getGuild().getName());

		Player p = new Player(member);
		Document d = new Document("guild_id", member.getGuild().getId()).append("user_id", member.getUser().getId()).append("elo", p.eloScore);

		MongoCollection<Document> collection = mongoDatabase.getCollection("players");
		collection.insertOne(d);

		return p;
	}

	public Player FindPlayer(Member member) {
		MongoCollection<Document> collection = mongoDatabase.getCollection("players");

		BasicDBObject d = new BasicDBObject();
		d.put("guild_id", member.getGuild().getId());
		d.put("user_id", member.getUser().getId());

		ArrayList<Document> documents = new ArrayList<>();
		collection.find(d).into(documents);

		if(documents.size() > 0) {
			Document doc = documents.get(0);
			return new Player(member, doc.getInteger("elo"));
		} else {
			return null;
		}
	}

	public void UpdatePlayer(Player player) {
		MongoCollection<Document> collection = mongoDatabase.getCollection("players");
		UpdateResult res = collection.updateOne(and(eq("guild_id", player.member.getGuild().getId()), eq("user_id", player.member.getUser().getId())), new Document("$set", new Document("elo", player.eloScore)));
		if(res.getModifiedCount() != 1 || !res.wasAcknowledged()) throw new RuntimeException("Could not update player." + (res.getModifiedCount() != 1 ? " Didn't modify any records." : " Request was not acknowledged."));
	}

	public List<Player> GetAllPlayers(Guild guild) {
		MongoCollection<Document> collection = mongoDatabase.getCollection("players");

		BasicDBObject filter = new BasicDBObject();
		filter.put("guild_id", guild.getId());

		ArrayList<Document> documents = new ArrayList<>();
		collection.find(filter).into(documents);

		ArrayList<Player> players = new ArrayList<>();
		for(Document d : documents) {
			players.add(new Player(guild.getMemberById(d.getString("user_id")), d.getInteger("elo")));
		}

		return players;
	}
}
