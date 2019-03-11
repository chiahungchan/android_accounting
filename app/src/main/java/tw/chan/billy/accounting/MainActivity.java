package tw.chan.billy.accounting;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState != null){
            // TODO: restore number on views
        }
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
