package molkky.android.model;

import java.util.List;

import android.content.Context;
import android.database.Cursor;

public class JoueursRepository extends Repository {

	public JoueursRepository(Context c){
		sqLiteOpenHelper = new JoueurOpenHelper(c, null);
	}
	
	@Override
	public List<?> GetAll() {
		Cursor cursor = maBDD.query(JoueurOpenHelper.JOUEUR_TABLE_NAME, 
									new String[] {	JoueurOpenHelper.COLUMN_ID,
													JoueurOpenHelper.COLUMN_NAME}, null, null, null, null, null);
		return ConvertCursorToListObject(cursor);
	}

	@Override
	public Joueur GetById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void Save(Object entite) {
		// TODO Auto-generated method stub

	}

	@Override
	public void Update(Object entite) {
		// TODO Auto-generated method stub

	}

	@Override
	public void Delete(int id) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<?> ConvertCursorToListObject(Cursor c) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object ConvertCursorToObject(Cursor c) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object ConvertCursorToOneObject(Cursor c) {
		// TODO Auto-generated method stub
		return null;
	}

}
