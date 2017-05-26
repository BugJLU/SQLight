package org.bugjlu.sqlight;

import android.content.Intent;
import android.test.ActivityUnitTestCase;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest extends ActivityUnitTestCase {
    public ExampleUnitTest(Class activityClass) {
        super(activityClass);
    }

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
//        Intent intent = new Intent();
//        intent.putExtra("title", "sqlighttest");
//        intent.putExtra("info", "sqlighttest.db");
//        intent.setClass(ExampleUnitTest.this, DBExecActivity.class);
//        startActivity(intent);
    }
}