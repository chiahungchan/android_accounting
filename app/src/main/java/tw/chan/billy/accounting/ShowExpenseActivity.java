package tw.chan.billy.accounting;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class ShowExpenseActivity extends AppCompatActivity {

    RecyclerView mRootView;
    ArrayList<ExpenseItem> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_expense);

        mData = new ArrayList<>();
        mRootView = findViewById(R.id.show_item_rv);
        mRootView.setLayoutManager(new LinearLayoutManager(this));
        ExpenseAdapter adapter = new ExpenseAdapter(mData, getSupportFragmentManager());
        mRootView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        DbProcessTask task = new DbProcessTask(this, null, null);
        task.execute(DbProcessTask.QUERY, null);
        try{
            Cursor result = task.get(5000, TimeUnit.SECONDS);
            if(result != null){
                processDbData(result);
                ProgressBar pgb = findViewById(R.id.show_item_pb);
                pgb.setVisibility(View.GONE);
                ExpenseAdapter adapter = (ExpenseAdapter) mRootView.getAdapter();
                adapter.notifyDataSetChanged();
                mRootView.setVisibility(View.VISIBLE);
            }
        }catch(Exception e){
            e.printStackTrace();
            Toast.makeText(this, "Internal Error Occurred", Toast.LENGTH_SHORT).show();
            Log.e(getClass().getSimpleName(), e.getMessage());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.show_expense_menu, menu);
        return true;
    }

    private void processDbData(Cursor cursor){
        while(cursor.moveToNext()){
            long id = cursor.getLong(cursor.getColumnIndex(ExpenseDbHelper.DbColumns._ID));
            String date = cursor.getString(cursor.getColumnIndex(ExpenseDbHelper.DbColumns.DATE));
            String amount = cursor.getString(cursor.getColumnIndex(ExpenseDbHelper.DbColumns.AMOUNT));
            String item = cursor.getString(cursor.getColumnIndex(ExpenseDbHelper.DbColumns.ITEM));
            String desc = cursor.getString(cursor.getColumnIndex(ExpenseDbHelper.DbColumns.DESC));
            String clAss = cursor.getString(cursor.getColumnIndex(ExpenseDbHelper.DbColumns.CLASS));
            mData.add(new ExpenseItem(id, date, amount, clAss, item, desc));
        }
        cursor.close();
    }

    public void updateList(int pos, String amount, String item, String desc, String date){
        ExpenseItem e = mData.get(pos);
        e.setmAmount(amount);
        e.setmDesc(desc);
        e.setmItem(item);
        e.setmDate(date);
        mRootView.getAdapter().notifyItemChanged(pos);
    }
}
