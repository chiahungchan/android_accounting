package tw.chan.billy.accounting;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.lang.ref.WeakReference;

public class DbProcessTask extends AsyncTask<String, Void, Void> {

    public static final String UPDATE = "upd";
    public static final String INSERT = "ins";
    public static final String DELETE = "del";
    public static final String QUERY = "que";
    private WeakReference<Context> mCallingContext;
    private ExpenseDbHelper mHelper;

    public DbProcessTask(Context context){
        mCallingContext = new WeakReference<>(context);
        mHelper = new ExpenseDbHelper(mCallingContext.get());
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Toast.makeText(mCallingContext.get(), "database updated!", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected Void doInBackground(String... strings) {
        switch(strings[0]){
            case UPDATE:
                mHelper.dbUpdate(strings[1], strings[2], strings[3], strings[4], strings[5], strings[6]);
                break;
            case INSERT:
                mHelper.dbInsert(strings[1], strings[2], strings[3], strings[4], strings[5]);
                break;
            case DELETE:
                mHelper.dbDelete(strings[1]);
                break;
            case QUERY:
                // TODO
                break;
        }
        return null;
    }
}
