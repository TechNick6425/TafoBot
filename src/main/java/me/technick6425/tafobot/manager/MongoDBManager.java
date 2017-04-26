package me.technick6425.tafobot.manager;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import me.technick6425.tafobot.TafoBot;
import me.technick6425.tafobot.data.Character;
import me.technick6425.tafobot.data.Match;
import me.technick6425.tafobot.data.Stage;
import org.bson.Document;

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
		mongoDatabase = mongoClient.getDatabase("tafobot");
	}

	public void RegisterMatch(Match match) {
		MongoCollection<Document> collection = mongoDatabase.getCollection("matches");
		collection.insertOne(new Document("player1", match.p1.id).append("player2", match.p2.id).append("stage", match.stage.id).append("winner", match.p1winner));
	}

	public List<Match> GetMatches() {
		MongoCollection<Document> collection = mongoDatabase.getCollection("matches");

		ArrayList<Document> documents = new ArrayList<>();
		collection.find().into(documents);
		collection.find().into(documents);

		ArrayList<Match> matches = new ArrayList<>();

		for (Document d: documents) {
			matches.add(new Match(Character.getById(d.getInteger("player1")), Character.getById(d.getInteger("player2")), Stage.getById(d.getInteger("stage")), (d.getString("winner").equals("p1"))));
		}

		return matches;
	}
}
