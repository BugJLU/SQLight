package org.bugjlu.sqlight;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.bugjlu.sqlight.dbexec_handlers.IDBExecHandler;
import org.bugjlu.sqlight.dbexec_handlers.SQLiteHandler;

public class DBExecActivity extends AppCompatActivity {

    Intent startIntent;
    String dbname, dbfile;
    IDBExecHandler execHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dbexec);
        startIntent = getIntent();
        Bundle bundle = startIntent.getExtras();
        dbname = bundle.getString("title");
        dbfile = bundle.getString("info");
        connectDB(dbfile);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disconnectDB();
    }

    protected void connectDB(String dbfile) {
        execHandler = SQLiteHandler.openSQLiteDB(this, dbfile);
    }
    protected void disconnectDB() {
        execHandler.close();
    }
//    protected
}
