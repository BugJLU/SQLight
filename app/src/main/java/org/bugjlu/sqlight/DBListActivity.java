package org.bugjlu.sqlight;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBListActivity extends AppCompatActivity {

    DBDataHandler dbHandler;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private ListView mainList;
    private List<Map<String, String>> dbList;

    protected void removeDB(String dbname) {
        dbHandler.removeList(dbname);
        refreshList();
    }

    protected void addDB(String dbname) throws Exception {
        // TODO: to be finished.
        dbHandler.addList(dbname, dbname + ".db");
        refreshList();
    }

    protected void refreshList() {
        dbList.clear();
        dbList.addAll(dbHandler.getList());
        mainList.setAdapter(mainList.getAdapter());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dblist);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dbList = new ArrayList<>();
        dbHandler = new DBDataHandler();
        mainList = (ListView) findViewById(R.id.mainList);
        mainList.setAdapter(new SimpleAdapter(this, dbList, R.layout.row_dblist,
                new String[]{"title"},
                new int[]{R.id.rowTitle}));
        refreshList();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText et = new EditText(DBListActivity.this);
                et.setSingleLine();
                new AlertDialog.Builder(DBListActivity.this).setTitle("数据库名称：").setIcon(null)
                        .setView(et)
                        .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String dbn = et.getText().toString();
                                if (!dbn.equals("")) {
                                    try {
                                        addDB(dbn);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        })
                        .setNegativeButton(R.string.button_cancel, null)
                        .show();
            }
        });

        mainList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            @SuppressWarnings("unchecked")
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> map = (HashMap<String, String>) mainList.getItemAtPosition(position);
                String dbname = map.get("title");
                String dbfile = map.get("info");
                Intent intent = new Intent();
                intent.putExtra("title", dbname);
                intent.putExtra("info", dbfile);
                intent.setClass(DBListActivity.this, DBExecActivity.class);
                startActivity(intent);
            }
        });


        mainList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            @SuppressWarnings("unchecked")
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                HashMap<String, String> map = (HashMap<String, String>) parent.getItemAtPosition(position);

                final String dbname = map.get("title");

                new AlertDialog.Builder(DBListActivity.this)
                        .setTitle("Delete \"" + dbname + "\"?")
                        .setNegativeButton("Cancel", null)
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                removeDB(dbname);
                            }
                        }).show();

                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dblist, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            SharedPreferences sp = getSharedPreferences("sqlight", MODE_PRIVATE);
            String engine = sp.getString("engine", "sqlite");

            switch (engine) {
                case "sqlite":
                default:
                    sp.edit().putString("engine", "sqlite_apart").apply();
                    Toast.makeText(this, "Switched to SQLite_Apart", Toast.LENGTH_SHORT).show();
                    break;
                case "sqlite_apart":
                    sp.edit().putString("engine", "sqlite").apply();
                    Toast.makeText(this, "Switched to SQLite", Toast.LENGTH_SHORT).show();
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }

    private class DBDataHandler {
        SQLiteDatabase db;

        DBDataHandler() {
            db = openOrCreateDatabase(".sqlightsaves.db", Context.MODE_PRIVATE, null);
            db.execSQL("create table if not exists dbinfo(title varchar(10), info varchar(20) not null, primary key (title), unique(info));");
        }

        List<Map<String, String>> getList() {
            List<Map<String, String>> list = new ArrayList<>();
            //TODO:
            String stmt = "select title,info from dbinfo ;";
            Cursor cursor = db.rawQuery(stmt, null);
            String[] names = cursor.getColumnNames();
            String str;
            while (cursor.moveToNext()) {
                Map<String, String> res = new HashMap<>();
                for (String i : names) {
                    str = cursor.getString(cursor.getColumnIndex(i));
                    res.put(i, str);
                }
                list.add(res);
            }
            cursor.close();
            return list;
        }

        void addList(String dbname, String dbfile) throws Exception {
            try {
                db.execSQL("INSERT INTO dbinfo(title, info) VALUES(?, ?);", new Object[]{dbname, dbfile});
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Database \"" + dbname + "\" already exists.",
                        Toast.LENGTH_SHORT).show();
            }
        }

        void removeList(String dbname) {
            db.execSQL("DELETE FROM dbinfo WHERE title = ? ;", new Object[]{dbname});
        }
    }
// TODO: JNI
//    /**
//     * A native method that is implemented by the 'native-lib' native library,
//     * which is packaged with this application.
//     */
//    public native String stringFromJNI();
//
//    // Used to load the 'native-lib' library on application startup.
//    static {
//        System.loadLibrary("native-lib");
//    }
}
