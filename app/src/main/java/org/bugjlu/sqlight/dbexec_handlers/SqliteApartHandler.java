package org.bugjlu.sqlight.dbexec_handlers;

import android.content.Context;
import android.database.Cursor;

import org.sqlite.database.sqlite.SQLiteDatabase;
import org.sqlite.database.sqlite.SQLiteException;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by HYec on 2017/10/6.
 */

public class SQLiteApartHandler implements IDBExecHandler {

    String dbfile;
    Context context;
    SQLiteDatabase db;

    public static SQLiteApartHandler openSQLiteDB(Context context, String dbfile) {
        return new SQLiteApartHandler(context, dbfile);
    }

    private SQLiteApartHandler(Context context, String dbfile) {
        this.context = context;
        this.dbfile = dbfile;
        startDB(context, dbfile);
    }

    @Override
    public void startDB(Context context, String dbfile) {
        db = SQLiteDatabase.openOrCreateDatabase(dbfile, null);
    }

    @Override
    public void close() {
        if (db != null) {
            db.close();
            db = null;
        }
    }

    @Override
    public DBExecResult exec(String stmt) {
        try {
            db.execSQL(stmt);
            return new DBExecResult("Statement Execute Success.\n");
        } catch (SQLiteException e) {
            if (e.getMessage().contains("Queries can be performed using SQLiteDatabase query or rawQuery methods only.")) {
                return new DBExecResult(execQuery(stmt));
            } else {
                return new DBExecResult(e.getMessage() + "\n");
            }
        }
    }

    @Override
    public String getDBProduct() {
        return "SQLiteApart";
    }

    private String[][] execQuery(String stmt) {
        List<String[]> results = new LinkedList<>();

        Cursor cursor = db.rawQuery(stmt, null);
        String[] names = cursor.getColumnNames();
        results.add(names);

        List<String> column;
        while (cursor.moveToNext()) {
            column = new LinkedList<>();

            for (String i : names) {

                column.add(cursor.getString(cursor.getColumnIndex(i)));
            }

            results.add(column.toArray(new String[names.length]));
        }
        cursor.close();
        return results.toArray(new String[names.length][results.size()]);
    }


}
