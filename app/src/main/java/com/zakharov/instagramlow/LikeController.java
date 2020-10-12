package com.zakharov.instagramlow;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class LikeController<InitOrCreate> {
    Context _context;
    String dbName = "sample5";
    String columnName = "image";
    SQLiteDatabase db;
    Cursor query;
    public LikeController(Context context) {
        _context = context;
        db = _context.openOrCreateDatabase("app.db", _context.MODE_PRIVATE, null);
    }

    public void CreateDataBase(){
        db.execSQL("CREATE TABLE IF NOT EXISTS "+ dbName +" ("+ columnName +" TEXT)");
    }

    public String SelectImageFromDataBase(String url){
        String str = "";
        query = db.rawQuery("SELECT "+ columnName +" FROM "+ dbName +" where "+ columnName +" = '"+ url +"';", null);
        if(query.moveToFirst()){
           str  = query.getString(0);
        }
        return str;
    }

    public String SelectAll(){
        String str = "";
        query = db.rawQuery("SELECT * FROM "+ dbName +";", null);
        if(query.moveToFirst()){
            do {
                    str += "\n" + query.getString(0);
            }
            while (query.moveToNext());
        }
        return str;
    }

    public void InsertIntoDataBase(String url){
        db.execSQL("INSERT INTO "+ dbName +" VALUES ('"+ url +"');");
    }

    public void DeleteFromDataBase(String url){
        db.execSQL("DELETE FROM "+ dbName +" WHERE "+ columnName +" = '"+ url +"';");
    }

    public boolean FindImageFromDataBase(String url){
        boolean isFinded = false;
        query = db.rawQuery("SELECT "+ columnName +" FROM "+ dbName +" where "+ columnName +" = '"+ url +"';", null);
        if(query.moveToFirst()){
            isFinded = !query.getString(0).isEmpty();
        }
        return isFinded;
    }

    public void CloseConnection(){
        query.close();
        db.close();
    }

}
