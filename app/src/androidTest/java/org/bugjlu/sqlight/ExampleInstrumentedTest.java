package org.bugjlu.sqlight;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.provider.Settings;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.bugjlu.sqlight.dbexec_handlers.SQLiteHandler;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;
import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
//        SQLiteDatabase db = appContext.openOrCreateDatabase("sqlighttest.db", Context.MODE_PRIVATE, null);
////        db.execSQL("create table if not exists dbinfo(title varchar(10), info varchar(20) not null, primary key (title));");
////        db.execSQL("insert into dbinfo values (\"db1\", \"db1.db\")");
//////        db.close();
//////        db = openOrCreateDatabase("./test.db", null);
////        db.execSQL("insert into dbinfo values (\"db2\", \"db2.db\")");
//        try {
//            Cursor cursor = db.rawQuery("select * from dbinfo", null);
//            while (cursor.moveToNext()) {
//                Log.v("cursor", "cursor."+cursor.getString(0)+"   "+cursor.getString(1));
//            }
//        } catch (SQLiteException e) {
//            System.out.println("ERR!"+e.getMessage());
//            Log.v("ERR!", "ERR!"+e.getMessage());
////            Log.v("ERR!", "ERR!"+e.pr);
//            //"unknown error (code 0): Queries can be performed using SQLiteDatabase query or rawQuery methods only.";
//        }
//        db.close();
//        SQLiteHandler sqlHandler = SQLiteHandler.openSQLiteDB(appContext, "sqlighttest.db");
//        sqlHandler.exec("insert into dbinfo\n values (\"mydb2\",\n \"mydb2.db\")");
//        Log.v("handler", sqlHandler.exec("select * from dbinfo"));
//        sqlHandler.close();


        assertEquals("org.bugjlu.sqlight", appContext.getPackageName());
    }
}
