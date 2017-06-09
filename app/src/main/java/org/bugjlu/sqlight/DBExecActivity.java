package org.bugjlu.sqlight;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.bugjlu.sqlight.dbexec_handlers.IDBExecHandler;
import org.bugjlu.sqlight.dbexec_handlers.SQLiteHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBExecActivity extends AppCompatActivity {

    Intent startIntent;
    String dbname, dbfile;
    IDBExecHandler execHandler;
    EditText stmtText;
    ScrollView stmtView;
    ListView resultView;
    //TODO: test start
    List<Map<String, String>> dbList;
    //TODO: test end

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dbexec);
        stmtText = (EditText) findViewById(R.id.stmtText);
        stmtView = (ScrollView) findViewById(R.id.stmtView);
        resultView = (ListView) findViewById(R.id.resultList);

        //TODO: test start
        dbList = new ArrayList<> ();
        resultView.setAdapter(new SimpleAdapter(this, dbList, R.layout.row_execlist,
                new String[] {"title"},
                new int[] {R.id.rowTitle}));
        HashMap m = new HashMap<String, String>();
        m.put("title", "db1");
        m.put("info", "db1.db");
        dbList.add(m);
        resultView.setAdapter(resultView.getAdapter());
        //TODO: test end

        stmtText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_UP) && (keyCode == KeyEvent.KEYCODE_BACK)) {
                    stmtText.clearFocus();
                    return true;
                }
                return false;
            }
        });
        stmtText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Animation animation = new AlphaAnimation(1.0f, 0.0f);
                    animation.setDuration(100);
                    resultView.setAnimation(animation);
                    resultView.setVisibility(View.GONE);
                } else {
                    resultView.setVisibility(View.VISIBLE);
                    Animation animation = new AlphaAnimation(0.0f, 1.0f);
                    animation.setDuration(100);
                    resultView.setAnimation(animation);
                }
            }
        });
        stmtText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    execDB();
                    return true;
                }
                return false;
            }
        });
        ((Button) findViewById(R.id.execButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                execDB();
            }
        });
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
    protected void execDB() {
        String result = execHandler.exec(stmtText.getText().toString());
        stmtText.clearFocus();
        ((InputMethodManager) DBExecActivity.this
                .getSystemService(Context.INPUT_METHOD_SERVICE)).
                hideSoftInputFromWindow(stmtText.getWindowToken(), 0);
        //TODO: test start
        HashMap m = new HashMap<String, String>();
        m.put("title", result);
        dbList.add(m);
        resultView.setAdapter(resultView.getAdapter());
        //TODO: test end
    }
//    protected
}
