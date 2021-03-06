package org.bugjlu.sqlight;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.bugjlu.sqlight.dbexec_handlers.DBExecResult;
import org.bugjlu.sqlight.dbexec_handlers.IDBExecHandler;
import org.bugjlu.sqlight.dbexec_handlers.SQLiteApartHandler;
import org.bugjlu.sqlight.dbexec_handlers.SQLiteHandler;

public class DBExecActivity extends AppCompatActivity {

    Intent startIntent;
    String dbname, dbfile;
    IDBExecHandler execHandler;
    EditText stmtText;
    EditText resultText;
    ScrollView stmtView, resultView;
    Button execButton;
    String prefix;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dbexec);
        stmtText = (EditText) findViewById(R.id.stmtText);
        stmtView = (ScrollView) findViewById(R.id.stmtView);
        resultView = (ScrollView) findViewById(R.id.resultView);
        resultText = (EditText) findViewById(R.id.resultText);
        resultText.setSelection(resultText.getText().length(), resultText.getText().length());
        execButton = (Button) findViewById(R.id.execButton);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        stmtText.clearFocus();
        execButton.requestFocus();

        new KeyboardChangeListener(this).setKeyBoardListener(new KeyboardChangeListener.KeyBoardListener() {
            @Override
            public void onKeyboardChange(boolean isShow, int keyboardHeight) {
                if (isShow) {
                    Animation animation = new AlphaAnimation(1.0f, 0.0f);
                    animation.setDuration(100);
                    resultView.setAnimation(animation);
                    resultView.setVisibility(View.GONE);
                    findViewById(R.id.clrResBtn).setVisibility(View.GONE);
                } else {
                    resultView.setVisibility(View.VISIBLE);
                    Animation animation = new AlphaAnimation(0.0f, 1.0f);
                    animation.setDuration(100);
                    resultView.setAnimation(animation);
                    findViewById(R.id.clrResBtn).setVisibility(View.VISIBLE);
                }
            }
        });

        stmtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DBExecActivity.this, "view clicked", Toast.LENGTH_SHORT);
                stmtText.performClick();
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

        findViewById(R.id.execButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                execDB();
            }
        });
        findViewById(R.id.clrSqlBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stmtText.setText(null);
            }
        });
        findViewById(R.id.clrResBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultText.setText(null);
            }
        });

        startIntent = getIntent();
        Bundle bundle = startIntent.getExtras();
        dbname = bundle.getString("title");
        dbfile = bundle.getString("info");
        connectDB(dbfile);
        prefix = execHandler.getDBProduct() + "$ ";
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disconnectDB();
    }

    protected void connectDB(String dbfile) {

        SharedPreferences sp = getSharedPreferences("sqlight", MODE_PRIVATE);
        String engine = sp.getString("engine", "sqlite");

        switch (engine) {
            case "sqlite":
            default:
                execHandler = SQLiteHandler.openSQLiteDB(this, dbfile);
                break;
            case "sqlite_apart":
                execHandler = SQLiteApartHandler.openSQLiteDB(this, dbfile);
        }

        //execHandler = SQLiteApartHandler.openSQLiteDB(this, dbfile);
    }

    protected void disconnectDB() {
        execHandler.close();
    }

    protected void execDB() {
        String stmt = stmtText.getText().toString();
        stmt = stmt.replace("\n", " ");
        DBExecResult result = execHandler.exec(stmt);
        stmtText.clearFocus();
        ((InputMethodManager) DBExecActivity.this
                .getSystemService(Context.INPUT_METHOD_SERVICE)).
                hideSoftInputFromWindow(stmtText.getWindowToken(), 0);
        resultText.append(prefix + stmt + "\n");

        if (result.isQuery()) {
            Intent intent = new Intent();
            intent.setClass(DBExecActivity.this,
                    DBResultActivity.class);
            intent.putExtra("execResult", result);
            startActivity(intent);

            resultText.append("result was shown in table view.\n");
        } else {
            resultText.append(result + "\n");
        }


    }
}
