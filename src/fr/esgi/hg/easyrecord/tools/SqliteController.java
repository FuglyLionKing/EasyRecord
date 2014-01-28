package fr.esgi.hg.easyrecord.tools;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Damian
 */
public class SqliteController extends SQLiteOpenHelper {

    public SqliteController(Context context) {
        super(context, "easyrecord.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query;
        query = "CREATE TABLE record ( " +
                "id VARCHAR PRIMARY KEY , " +
                "title VARCHAR, " +
                "creationDate TEXT," +
                "modificationDate TEXT," +
                "comment TEXT)";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }

    public void insertRecord(Record record) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", record.path);
        values.put("title", record.title);
        values.put("creationDate", record.creationDate);
        values.put("modificationDate", record.modificationDate);
        values.put("comment", record.comment);
        if (database != null) {
            database.insert("record", null, values);
            database.close();
        }
    }

    public int updateRecord(Record record) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", record.title);
        values.put("comment", record.comment);
        if (database != null) {
            return database.update("record", values, "id" + " = ?", new String[] { record.path });
        }
        return 0;
    }

    public Record selectRecord(String id) {
        SQLiteDatabase database = this.getReadableDatabase();
        String selectQuery = "SELECT title, creationDate, modificationDate, comment FROM record WHERE id='"+id+"'";
        Cursor cursor = null;
        if (database != null) {
            cursor = database.rawQuery(selectQuery, null);
        }
        Record record = new Record();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                record.path = id;
                record.title =  cursor.getString(0);
                record.creationDate =  cursor.getString(1);
                record.modificationDate =  cursor.getString(2);
                record.comment =  cursor.getString(3);
            } else return null;
        }
        return record;
    }
}