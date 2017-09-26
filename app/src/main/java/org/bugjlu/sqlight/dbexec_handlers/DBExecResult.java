package org.bugjlu.sqlight.dbexec_handlers;

import java.io.Serializable;

/**
 * Created by HYec on 2017/9/26.
 */

public class DBExecResult implements Serializable {

    private boolean isQuery;

    private String[][] queryResult;

    private String execResult;

    public boolean isQuery() {
        return isQuery;
    }

    public String[][] getQueryResult() {
        return queryResult;
    }

    public String getExecResult() {
        return execResult;
    }

    public DBExecResult(String execResult) {
        this.isQuery = false;
        this.execResult = execResult;
    }

    public DBExecResult(String[][] queryResult) {
        this.isQuery = true;
        this.queryResult = queryResult;
    }

    @Override
    public String toString() {
        if (this.isQuery) {
            StringBuilder sb = new StringBuilder();
            for (String[] s : queryResult) {
                for (String i : s) {
                    sb.append(i).append("\t|");
                }
                sb.append("\n");
            }
            return sb.toString();
        } else {
            return execResult;
        }
    }
}
