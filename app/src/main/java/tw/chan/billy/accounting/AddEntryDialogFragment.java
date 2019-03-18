package tw.chan.billy.accounting;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class AddEntryDialogFragment extends DialogFragment {
    private long rowID;
    private int pos;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View v = inflater.inflate(R.layout.fragment_add_entry_dialog, null);
        Bundle bundle = getArguments();
        if(bundle != null){
            // invoked from ShowExpenseActivity
            // editText must be filled
            String amount = bundle.getString(ExpenseDbHelper.DbColumns.AMOUNT);
            String item = bundle.getString(ExpenseDbHelper.DbColumns.ITEM);
            String desc = bundle.getString(ExpenseDbHelper.DbColumns.DESC);
            rowID = bundle.getLong(ExpenseDbHelper.DbColumns._ID);
            pos = bundle.getInt(ExpenseAdapter.POSITION);
            EditText et = v.findViewById(R.id.amount_diag);
            et.setText(amount);
            et = v.findViewById(R.id.item_spend_diag);
            et.setText(item);
            et = v.findViewById(R.id.desc_diag);
            et.setText(desc);
            builder.setTitle("Edit");
        }
        else builder.setTitle(R.string.add_title);
        builder.setPositiveButton(R.string.choice_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog ad = (AlertDialog) dialog;
                        String amount_str = ((EditText)ad.findViewById(R.id.amount_diag)).getText().toString();
                        String item_spend_str = ((EditText)ad.findViewById(R.id.item_spend_diag)).getText().toString();
                        String desc_str = ((EditText)ad.findViewById(R.id.desc_diag)).getText().toString();
                        Calendar today = Calendar.getInstance();
                        String date_str = String.valueOf(today.get(Calendar.MONTH)+1) + "/" + String.valueOf(today.get(Calendar.DAY_OF_MONTH));
                        if(amount_str.length() == 0 || item_spend_str.length() == 0){
                            Toast.makeText(getContext(), "Blank field detected!\nNo updates", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Activity launchActivity = getActivity();
                        DbProcessTask task = new DbProcessTask(launchActivity, null, null);
                        if(launchActivity instanceof MainActivity){
                            // insert into DB
                            task.execute(DbProcessTask.INSERT, date_str, amount_str, null, item_spend_str, desc_str);
                            ((MainActivity)launchActivity).updateAmountAfterAdd(Integer.parseInt(amount_str));
                        }
                        else if(launchActivity instanceof ShowExpenseActivity){
                            task.execute(DbProcessTask.UPDATE, date_str, amount_str, null, String.valueOf(rowID),item_spend_str, desc_str);
                            ((ShowExpenseActivity)launchActivity).updateList(pos, amount_str, item_spend_str, desc_str, date_str);
                        }
                    }
                })
                .setNegativeButton(R.string.choice_cancel,null)
                .setView(v);

        return builder.create();
    }
}
