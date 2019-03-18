package tw.chan.billy.accounting;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

public class DbProcessTask extends AsyncTask<String, Void, Cursor> {

    public static final String UPDATE = "upd";
    public static final String INSERT = "ins";
    public static final String DELETE = "del";
    public static final String QUERY = "que";
    private String LOG_TAG = getClass().getSimpleName();
    private ExpenseDbHelper mHelper;
    private String[] qColumns, qSelArg;

    public DbProcessTask(Context context, String[] c, String[] s){
        mHelper = new ExpenseDbHelper(context);
        qColumns = c;
        qSelArg = s;
    }

    @Override
    protected void onPostExecute(Cursor v) {
        // do nothing for now
    }

    @Override
    protected Cursor doInBackground(String... strings) {
        switch(strings[0]){
            case UPDATE:
                if(strings.length != 7){
                    Log.e(LOG_TAG, "update: argument number mismatch");
                    return null;
                }
                mHelper.dbUpdate(strings[1], strings[2], strings[3], strings[4], strings[5], strings[6]);
                //               date        amount      class       rowID       item        desc
                break;
            case INSERT:
                if(strings.length != 6){
                    Log.e(LOG_TAG, "insert: argument number mismatch");
                    return null;
                }
                mHelper.dbInsert(strings[1], strings[2], strings[3], strings[4], strings[5]);
                //               date        amount      class       item        desc
                break;
            case DELETE:
                if(strings.length != 2){
                    Log.e(LOG_TAG, "delete: argument number mismatch");
                    return null;
                }
                mHelper.dbDelete(strings[1]);
                //               rowID
                break;
            case QUERY:
                if(strings.length != 2){
                    Log.e(LOG_TAG, "delete: argument number mismatch");
                    return null;
                }
                return mHelper.dbQuery(qColumns, strings[1], qSelArg, null);
                //                               selection
        }
        return null;
    }
}
