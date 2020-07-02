package home.thiru.templatemessaging;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "history.db";

    public DBHelper(Context context) {
        super(context, "history.db", (CursorFactory)null, 1);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE history (id INTEGER PRIMARY KEY AUTOINCREMENT, template text,date text,time text,name text,send text)");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS history");
        this.onCreate(db);
    }

    public boolean insert(String template, String date, String time, String name, String send) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("template", template);
        contentValues.put("date", date);
        contentValues.put("time", time);
        contentValues.put("name", name);
        contentValues.put("send", send);
        db.insert("history", (String)null, contentValues);
        return true;
    }

    public ArrayList<String> getAlltemplate() {
        ArrayList<String> array_list = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM history", (String[])null);
        res.moveToFirst();

        while(!res.isAfterLast()) {
            array_list.add(res.getString(res.getColumnIndex("template")));
            res.moveToNext();
        }

        return array_list;
    }

    public ArrayList<String> getAlltime() {
        ArrayList<String> array_list = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM history", (String[])null);
        res.moveToFirst();

        while(!res.isAfterLast()) {
            array_list.add(res.getString(res.getColumnIndex("time")));
            res.moveToNext();
        }

        return array_list;
    }

    public ArrayList<String> getAlldate() {
        ArrayList<String> array_list = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM history", (String[])null);
        res.moveToFirst();

        while(!res.isAfterLast()) {
            array_list.add(res.getString(res.getColumnIndex("date")));
            res.moveToNext();
        }

        return array_list;
    }

    public ArrayList<String> getAllname() {
        ArrayList<String> array_list = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM history", (String[])null);
        res.moveToFirst();

        while(!res.isAfterLast()) {
            array_list.add(res.getString(res.getColumnIndex("name")));
            res.moveToNext();
        }

        return array_list;
    }

    public ArrayList<String> getAllsend() {
        ArrayList<String> array_list = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM history", (String[])null);
        res.moveToFirst();

        while(!res.isAfterLast()) {
            array_list.add(res.getString(res.getColumnIndex("send")));
            res.moveToNext();
        }

        return array_list;
    }

    public void delete() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("history", (String)null, (String[])null);
    }
}
