package org.bugjlu.sqlight;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.bugjlu.sqlight.dbexec_handlers.DBExecResult;

public class DBResultActivity extends AppCompatActivity {

    DBExecResult result;
    TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dbresult);




        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        result = (DBExecResult) bundle.getSerializable("execResult");

        tableLayout = (TableLayout) findViewById(R.id.resultTableView);
        tableLayout.removeAllViews();
        tableLayout.setShrinkAllColumns(true);

        int[] colors = { Color.WHITE, Color.rgb(219, 238, 244) };

        int n = 0;

        for(String[] s : result.getQueryResult()) {
            TableRow tableRow = new TableRow(DBResultActivity.this);
            tableRow.setBackgroundColor(colors[n % 2]);
            tableRow.setPadding(100, 50, 100, 50);

            for(String i : s) {
                TextView textView = new TextView(DBResultActivity.this);
                textView.setText(i);
                textView.setPadding(50, 0, 50, 0);

                tableRow.addView(textView);
            }

            tableLayout.addView(tableRow, new TableLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT, 1));

            n++;
        }

    }
}
