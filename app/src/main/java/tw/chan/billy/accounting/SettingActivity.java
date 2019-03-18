package tw.chan.billy.accounting;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class SettingActivity extends AppCompatActivity {

    public static final String USER_FNAME = "user_settings.txt";
    private ArrayList<EditText> editViews;
    private int viewIds[] = {R.id.monthly_budget_edit, R.id.warning_below_edit};
    private boolean set_zero;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        editViews = new ArrayList<>();
        set_zero = false;

        // get editTexts for further processing
        for(int id: viewIds){
            editViews.add((EditText) findViewById(id));
        }

        // if setting file exists, fill editTexts with values
        File f = new File(getFilesDir(), USER_FNAME);
        if(f.exists()) {
            try {
                FileInputStream settingFileInput = openFileInput(USER_FNAME);
                BufferedReader reader = new BufferedReader(new InputStreamReader(settingFileInput));
                int res[] = {R.id.monthly_budget_edit, R.id.warning_below_edit};
                for (int id: res) {
                    EditText ipt = findViewById(id);
                    ipt.setText(reader.readLine());
                }
                settingFileInput.close();
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if(notBlankField()) {
            int monthly = Integer.parseInt(editViews.get(0).getText().toString());
            int warning = Integer.parseInt(editViews.get(1).getText().toString());
            if(monthly - warning < 0) {
                // it's impossible that warning value is greater than monthly budget
                // so I check these values here.
                Toast.makeText(this, "Value in \"Warning\" cannot be greater than that in \"Monthly budget\"",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(this, "Your setting saved!", Toast.LENGTH_SHORT).show();
            try{
                // save the inputted data to setting file
                FileOutputStream settingFileOutput = openFileOutput(USER_FNAME, MODE_PRIVATE);
                StringBuilder s = new StringBuilder();
                s.append(editViews.get(0).getText().toString())
                        .append('\n')
                        .append(editViews.get(1).getText().toString());
                settingFileOutput.write(s.toString().getBytes());
                settingFileOutput.close();
                if(set_zero)
                    setResult(Activity.RESULT_OK);
            }catch (IOException e){
                e.printStackTrace();
            }
            super.onBackPressed();
        }
        else Toast.makeText(this, "Input fields cannot left blank", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
            //NavUtils.navigateUpFromSameTask(this);
        }
        return true;
    }

    private boolean notBlankField(){
        // return true if any of editText is blank, false otherwise
        for(int i = 0; i < editViews.size(); i++){
            if(editViews.get(i).getText().toString().length() == 0){
                return false;
            }
        }
        return true;
    }

    public void clearDb(View v){
        if(v.getId() == R.id.clear_db_btn){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final View k = v;
            builder.setTitle("Warning")
                    .setMessage("All entries will be DELETED!\nAre you sure?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DbProcessTask task = new DbProcessTask(k.getContext(), null, null);
                            task.execute(DbProcessTask.DELETE, null);
                            set_zero = true;
                            Toast.makeText(k.getContext(), "All entries deleted", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }
    }
}
