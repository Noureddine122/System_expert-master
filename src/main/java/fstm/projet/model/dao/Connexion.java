package fstm.projet.model.dao;

import com.mongodb.DB;
import com.mongodb.MongoClient;


public class Connexion {

	public static DB getConnex() {
		DB baseDb=null;
		MongoClient client=new MongoClient("localhost",27017);

		baseDb=client.getDB("DBDiagnostic");

		return baseDb;
		
	}
}
