package tw.chan.billy.accounting;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {

    private int mLeft, mAmountSpent, mBudget, mWarningAmount;
    private boolean show_warn_dialog, please_update;
    private static final String ENTER = "tw.chan.billy.enter";
    private static final String LEFT = "tw.chan.billy.left";
    private static final String AMOUNT_SPENT = "tw.chan.billy.as";
    private static final String BUDGET = "tw.chan.billy.bud";
    private static final String WARN = "tw.chan.billy.wrn";
    private static final String SPEND_FILE = "spend.txt";
    public static final int showExReqCode = 3;
    public static final int settingReqCode = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState != null){
            show_warn_dialog = savedInstanceState.getBoolean(ENTER);
            mLeft = savedInstanceState.getInt(LEFT);
            mAmountSpent = savedInstanceState.getInt(AMOUNT_SPENT);
            mBudget = savedInstanceState.getInt(BUDGET);
            mWarningAmount = savedInstanceState.getInt(WARN);
            updateViews();
        }
        else{
            // check whether everything is new
            File f = new File(getFilesDir(), SettingActivity.USER_FNAME);
            show_warn_dialog = !f.exists();

            if(!show_warn_dialog){
                // previously used, just retrieve from files
                getDataFromFileAndUpdate();
            }
        }
        please_update = false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        // in order to show dialog at the beginning,
        // alert dialog is made after super.onStart().
        if (show_warn_dialog) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Personal data not set\nPlease create one.")
                    .setTitle(R.string.warning)
                    .setPositiveButton(R.string.choice_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            show_warn_dialog = false;
                            please_update = true;
                        }
                    })
                    .setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            show_warn_dialog = false;
                            please_update = true;
                            Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                            startActivity(intent);
                        }
                    });
            builder.show();
            mAmountSpent = 0;
        }
    }

    @Override
    protected void onResume() {
        Log.v(getClass().getSimpleName(), "onResume");
        if(please_update) {
            getDataFromFileAndUpdate();
            please_update = false;
        }
        super.onResume();
    }

    @Override
    protected void onStop() {
        if(!show_warn_dialog){
            // store the amount of remaining money to file
            try{
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(openFileOutput(SPEND_FILE, MODE_PRIVATE)));
                writer.write(String.valueOf(mAmountSpent));
                writer.write('\n');
                writer.close();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(ENTER, show_warn_dialog);
        outState.putInt(LEFT, mLeft);
        outState.putInt(AMOUNT_SPENT, mAmountSpent);
        outState.putInt(WARN, mWarningAmount);
        outState.putInt(BUDGET, mBudget);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

        // user may update entries in ShowExpenseActivity
        // get result from this and update views in MainActivity
        if(requestCode == showExReqCode){
            if(resultCode == Activity.RESULT_OK && data != null){
                mAmountSpent = data.getIntExtra(ShowExpenseActivity.SUM, 0);
                mLeft = mBudget - mAmountSpent;
                updateViews();
            }
        }

        // user may delete data
        // do corresponding work here
        // because SettingActivity sets "please_update" to true
        // write the result directly to file
        else if(requestCode == settingReqCode){
            if(resultCode == Activity.RESULT_OK){
                try{
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(openFileOutput(SPEND_FILE, MODE_PRIVATE)));
                    bw.write("0\n");
                    bw.close();
                }catch(IOException e){
                    Log.e(getClass().getSimpleName(), e.getMessage());
                }
            }
        }
    }

    // onclick callback for Options in mainActivity
    public void launchAnotherActivity(View v){
        Intent intent = new Intent();
        boolean start_activity = false;
        int req_code = -1;
        switch(v.getId()){
            case R.id.setting_opt:
                intent.setClass(this, SettingActivity.class);
                start_activity = true;
                please_update = true;
                req_code = settingReqCode;
                break;
            case R.id.list_opt:
                intent.setClass(this, ShowExpenseActivity.class);
                start_activity = true;
                req_code = showExReqCode;
                break;
            case R.id.stat_opt:
                intent.setClass(this, StatsActivity.class);
                start_activity = true;
                break;
            case R.id.add_opt:
                AddEntryDialogFragment fragment = new AddEntryDialogFragment();
                fragment.show(getSupportFragmentManager(), "main");
                break;
        }
        if(start_activity) startActivityForResult(intent, req_code);
    }

    // update two numbers in mainActivity
    private void updateViews(){
        TextView tv = findViewById(R.id.money_spent_txt);
        tv.setText(Integer.toString(mAmountSpent));
        tv = findViewById(R.id.money_left_txt);
        tv.setText(Integer.toString(mLeft));
        if(mLeft < 0)
            tv.setBackgroundResource(android.R.color.holo_red_dark);
        else if(mLeft <= mWarningAmount)
            tv.setBackgroundColor(Color.YELLOW);
        else tv.setBackgroundColor(Color.WHITE);
    }

    // retrieve user setting data from file
    // and update to member values
    private void getDataFromFileAndUpdate(){
        try{
            FileInputStream fis = openFileInput(SettingActivity.USER_FNAME);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            String b = br.readLine();
            mBudget = Integer.parseInt(b);
            mWarningAmount = Integer.parseInt(br.readLine());
            fis.close();
            br.close();
            br = new BufferedReader(new InputStreamReader(openFileInput(SPEND_FILE)));
            String money_spend = br.readLine();
            mAmountSpent = Integer.parseInt(money_spend);
            br.close();
            mLeft = mBudget - mAmountSpent;
            updateViews();
        }
        catch (IOException e){
            Log.w(getClass().getSimpleName(), e.getMessage());
        }
    }

    public void updateAmountAfterAdd(int amount){
        mAmountSpent += amount;
        mLeft = mBudget - mAmountSpent;
        updateViews();
    }
}
