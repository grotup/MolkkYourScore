package model;

import java.util.List;

import android.database.Cursor;

public interface IRepository{
	
	public List GetAll();
    public Object GetById(int id);
    
    public void Save(Object entite);
    public void Update(Object entite);
    public void Delete(int id);
    
    public List ConvertCursorToListObject(Cursor c);
    public Object ConvertCursorToObject(Cursor c);
    public Object ConvertCursorToOneObject(Cursor c);
}
