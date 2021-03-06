package tw.chan.billy.accounting;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

public class ShowExpenseActivity extends AppCompatActivity {

    RecyclerView mRootView;
    ArrayList<ExpenseItem> mData;
    public static final String SUM = "tw.chan.billy.sum";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_expense);

        mData = new ArrayList<>();
        mRootView = findViewById(R.id.show_item_rv);
        mRootView.setLayoutManager(new LinearLayoutManager(this));
        ExpenseAdapter adapter = new ExpenseAdapter(mData, getSupportFragmentManager(), false);
        mRootView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // because asyncTask may last long
        // implemented in onStart() to prevent unresponsive UI
        DbProcessTask task = new DbProcessTask(this, null, null);
        task.execute(DbProcessTask.QUERY, null);
        try{
            Cursor result = task.get(5, TimeUnit.SECONDS);
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

    @Override
    public void onBackPressed() {

        // tell MainActivity total amount of money I spent
        String sel[] = {"sum("+ ExpenseDbHelper.DbColumns.AMOUNT+")"};
        DbProcessTask task = new DbProcessTask(this, sel, null);
        task.execute(DbProcessTask.QUERY, null);
        int sum = 0;
        try{
            Cursor db_result = task.get(5, TimeUnit.SECONDS);
            while(db_result.moveToNext()){
                sum = db_result.getInt(0);
            }
        }catch(Exception e){
            Log.e(getClass().getSimpleName(), e.getMessage());
        }

        Intent intent = new Intent();
        intent.putExtra(SUM, sum);
        setResult(Activity.RESULT_OK, intent);
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.select_delete:
                startActionMode(new ActionMode.Callback() {
                    @Override
                    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                        MenuInflater inflater = mode.getMenuInflater();
                        inflater.inflate(R.menu.show_action_mode, menu);
                        mode.setTitle(R.string.menu_sel_del);
                        RecyclerView.Adapter a = mRootView.getAdapter();
                        if(a instanceof ExpenseAdapter){
                            ((ExpenseAdapter) a).setInActionMode(true);
                        }
                        return true;
                    }

                    @Override
                    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                        return false;
                    }

                    @Override
                    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                        if(item.getItemId() == R.id.item_delete){

                            // delete the selected item(s)
                            RecyclerView.Adapter adapter = mRootView.getAdapter();
                            if(adapter instanceof ExpenseAdapter){
                                TreeSet<ExpenseItem> set = ((ExpenseAdapter) adapter).getSelectedSet();
                                if(set == null) return true;
                                for(Iterator<ExpenseItem> it = set.iterator(); it.hasNext();){
                                    ExpenseItem del_itm = it.next();
                                    DbProcessTask task = new DbProcessTask(mRootView.getContext(), null, null);
                                    int list_idx = mData.indexOf(del_itm);
                                    if(list_idx < 0){
                                        Log.e(getClass().getSimpleName(), "Error deleting item");
                                        return true;
                                    }
                                    task.execute(DbProcessTask.DELETE, String.valueOf(del_itm.getmID()));
                                    mData.remove(del_itm);
                                    adapter.notifyItemRemoved(list_idx);
                                }
                            }
                        }
                        return true;
                    }

                    @Override
                    public void onDestroyActionMode(ActionMode mode) {
                        RecyclerView.Adapter a = mRootView.getAdapter();
                        if(a instanceof ExpenseAdapter){
                            ((ExpenseAdapter) a).setInActionMode(false);
                        }
                    }
                });
                break;
        }
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
