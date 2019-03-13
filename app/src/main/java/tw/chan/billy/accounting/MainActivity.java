package tw.chan.billy.accounting;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private int mLeft, mAmount;
    private String setting_file;
    private boolean first_enter;
    private static final String ENTER = "tw.chan.billy.enter";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState != null){
            // TODO: restore number on views
            first_enter = savedInstanceState.getBoolean(ENTER);
        }
        else{
            first_enter = true;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        File f = new File(getFilesDir(), SettingActivity.USER_FNAME);
        try{
            if(!f.exists()){
                if(first_enter) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("Personal data not set\nPlease create one.")
                            .setTitle("Warning")
                            .setPositiveButton(R.string.choice_ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    first_enter = false;
                                }
                            })
                            .setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    first_enter = false;
                                    Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                                    startActivity(intent);
                                }
                            });
                    builder.show();
                }
            }
            else{
                // TODO: retrieve user data and show on UI
            }
        }
        catch (Exception e){
            Log.w(getClass().getSimpleName(), e.getMessage());
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(ENTER, first_enter);
        super.onSaveInstanceState(outState);
    }

    public void launchAnotherActivity(View v){
        Intent intent = new Intent();
        boolean start_activity = false;
        switch(v.getId()){
            case R.id.setting_opt:
                intent.setClass(this, SettingActivity.class);
                start_activity = true;
                break;
            case R.id.list_opt:
                intent.setClass(this, ShowExpenseActivity.class);
                start_activity = true;
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
        if(start_activity) startActivity(intent);
    }
}
