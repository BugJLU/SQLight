package org.bugjlu.sqlight;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.net.Uri;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;


public class DBListActivity extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private ListView mainList;
    private List<Map<String, String>> dbList;
    DBDataHandler dbHandler;

    private class DBDataHandler {
        SQLiteDatabase db;
        public DBDataHandler() {
            db = openOrCreateDatabase(".sqlightsaves.db", Context.MODE_PRIVATE, null);
            db.execSQL("create table if not exists dbinfo(title varchar(10), info varchar(20) not null, primary key (title), unique(info));");
        }
        public List<Map<String, String>> getList() {
            List<Map<String, String>> list = new ArrayList<>();
            //TODO:
            String stmt = "select title,info from dbinfo ;";
            Cursor cursor = db.rawQuery(stmt, null);
            String[] names = cursor.getColumnNames();
            String str ;
            while (cursor.moveToNext()) {
                Map<String, String> res = new HashMap<>();
                for (String i : names) {
                    str = cursor.getString(cursor.getColumnIndex(i));
                    res.put(i, str);
                }
                list.add(res);
            }
            return list;
        }
        public void addList (String dbname, String dbfile)throws Exception {
//            db.execSQL("insert into dbinfo values('"+dbname+"','"+dbfile+"');");
            try{
                db.execSQL("INSERT INTO dbinfo(title, info) VALUES(?, ?);", new Object[]{dbname, dbfile});
            }catch(Exception e){
                Toast.makeText(getApplicationContext(), "所建数据库已存在",
                        Toast.LENGTH_SHORT).show();
            }
        }
        public void removeList(String dbname) {
//            db.execSQL("delete from dbinfo where title = '"+dbname+"';");
            db.execSQL("DELETE FROM dbinfo WHERE title = ? ;", new Object[]{dbname});
        }
    }
    protected void removeDB(String dbname) {
        dbHandler.removeList(dbname);
        refreshList();
    }
    protected void addDB(String dbname) throws Exception {
        // TODO: to be finished.
        dbHandler.addList(dbname, dbname+".db");
        refreshList();
    }
    protected boolean dbExist(String dbname) {
        // TODO: to be finished.
        return false;
    }
    protected void refreshList() {
        // TODO: This is currently a test code, to be finished.
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
                new String[] {"title"},
                new int[] {R.id.rowTitle}));
        refreshList();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText et = new EditText(DBListActivity.this);
                new AlertDialog.Builder(DBListActivity.this).setTitle("数据库名称：").setIcon(null)
                        .setView(et)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String dbn = et.getText().toString();
                                if (dbn == null || dbn.equals("")) return;
                                else try {
                                    addDB(dbn);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });
        mainList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> map = (HashMap<String, String>) mainList.getItemAtPosition(position);
                String dbname = map.get("title"),
                        dbfile = map.get("info");
                Intent intent = new Intent();
                intent.putExtra("title", dbname);
                intent.putExtra("info", dbfile);
                intent.setClass(DBListActivity.this, DBExecActivity.class);
                startActivity(intent);
            }
        });

        mainList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                HashMap<String, String> map = (HashMap<String, String>) parent.getItemAtPosition(position);

                final String dbname = map.get("title");
                String dbfile = map.get("info");

                new AlertDialog.Builder(DBListActivity.this)
                        .setTitle("Delete " + dbname + "?")
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                removeDB(dbname);
                            }
                        }).show();

                return true;
            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("DBList Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
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
