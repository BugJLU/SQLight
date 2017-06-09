package org.bugjlu.sqlight.dbexec_handlers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

/**
 * Created by mac on 2017/5/26.
 */

public class SQLiteHandler implements IDBExecHandler {

    String dbfile;
    Context context;
    SQLiteDatabase db;

    public static SQLiteHandler openSQLiteDB(Context context, String dbfile) {
        return new SQLiteHandler(context, dbfile);
    }

    private SQLiteHandler(Context context, String dbfile) {
        this.context = context;
        this.dbfile = dbfile;
        startDB(context, dbfile);
    }

    @Override
    public void startDB(Context context, String dbfile) {
        db = context.openOrCreateDatabase(dbfile, Context.MODE_PRIVATE, null);
    }

    @Override
    public void close() {
        if (db != null) {
            db.close();
            db = null;
        }
    }

    @Override
    public String exec(String stmt) {
        try {
            db.execSQL(stmt);
            return "Statement: \"" + stmt +"\" Execute Success.";
        } catch (SQLiteException e) {
            if (e.getMessage().contains("Queries can be performed using SQLiteDatabase query or rawQuery methods only.")) {
                return execQuery(stmt);
            } else {
                return e.getMessage();
            }
        }
    }

    private String execQuery(String stmt) {
        StringBuilder result = new StringBuilder("");
        Cursor cursor = db.rawQuery(stmt, null);
        String[] names = cursor.getColumnNames();
        for (String i : names) {
            result.append(i+"\t|");
        }
        result.append("\n");
        while (cursor.moveToNext()) {
            for (String i : names) {
                result.append(cursor.getString(cursor.getColumnIndex(i))+"\t|");
            }
            result.append("\n");
        }
        return result.toString();
    }
}
