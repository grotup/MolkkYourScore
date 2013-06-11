package model;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public abstract class Repository implements IRepository{
	protected SQLiteDatabase maBDD;
	protected SQLiteOpenHelper sqLiteOpenHelper;
    /**
     * Constructeur par défaut
     */
    public Repository() {
    }
    /**
     * Ouverture de la connection
     */
    public void Open() {
        maBDD = sqLiteOpenHelper.getWritableDatabase();
    }
    /**
     * Fermeture de la connection
     */
    public void Close() {
        maBDD.close();
    }
}

