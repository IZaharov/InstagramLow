package com.zakharov.instagramlow;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class LikeController<InitOrCreate> {
    Context _context;
    String dbName = "sample";
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

    public String SelectURLString(String url){
        String str = "";
        query = db.rawQuery("SELECT "+ columnName +" FROM "+ dbName +" where "+ columnName +" = '"+ url +"';", null);
        if(query.moveToFirst()){
           str  = query.getString(0);
        }
        return str;
    }

    public void InsertIntoDataBase(String url){
        db.execSQL("INSERT INTO "+ dbName +" VALUES ('"+ url +"');");
    }

    public void CloseConnection(){
        query.close();
        db.close();
    }

}
