package molkky.android.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class JoueurOpenHelper extends SQLiteOpenHelper{
	// Version de la base
	public  static final int DATABASE_VERSION = 1;
	// Nom de la base
	public  static final String MOLKKY_BASE = "molkky.db";
	// Nom de la table
	public  static final String JOUEUR_TABLE_NAME = "Joueurs";
	// Description des colonnes
	public  static final String COLUMN_NAME = "nom";
	public  static final int NUM_COLUMN_NAME = 0;
	public  static final String COLUMN_ID = "id";
	public  static final int NUM_COLUMN_ID = 1;
	// Requête de création
	public  static final String REQUETE_CREATION_JOUEURS = "CREATE TABLE" + JOUEUR_TABLE_NAME + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
															COLUMN_NAME + " TEXT NOT NULL);";
	
	public JoueurOpenHelper(Context context, CursorFactory factory) {
		super(context, MOLKKY_BASE, factory, DATABASE_VERSION);
	}

	
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(REQUETE_CREATION_JOUEURS);
	}

	
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Lorsque l'on change le numéro de version de la base on supprime la
		// table puis on la recrée
		if (newVersion > DATABASE_VERSION) {
			db.execSQL("DROP TABLE " + JOUEUR_TABLE_NAME + ";");
			onCreate(db);
       }
	}

}
