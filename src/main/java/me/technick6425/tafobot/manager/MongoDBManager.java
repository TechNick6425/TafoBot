package me.technick6425.tafobot.manager;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import me.technick6425.tafobot.TafoBot;
import me.technick6425.tafobot.data.Character;
import me.technick6425.tafobot.data.Match;
import me.technick6425.tafobot.data.Stage;
import net.dv8tion.jda.core.entities.Guild;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
}
