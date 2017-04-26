package me.technick6425.tafobot.manager;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import me.technick6425.tafobot.TafoBot;
import me.technick6425.tafobot.data.Character;
import me.technick6425.tafobot.data.Stage;
import org.bson.Document;

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

	public void RegisterMatch(Character player1, Character player2, Stage stage, boolean p1winner) {
		MongoCollection<Document> collection = mongoDatabase.getCollection("matches");
		collection.insertOne(new Document("player1", player1.id).append("player2", player2.id).append("stage", stage.id).append("winner", p1winner));
	}
}
