package org.bugjlu.sqlight.dbexec_handlers;

import android.content.Context;

import java.io.Closeable;

/**
 * Created by mac on 2017/5/26.
 */

public interface IDBExecHandler extends Closeable {
    void startDB(Context context, String dbname);
    void close();
    DBExecResult exec(String stmt);
    String getDBProduct();
}
