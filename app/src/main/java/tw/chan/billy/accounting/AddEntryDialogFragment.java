package tw.chan.billy.accounting;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class AddEntryDialogFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle(R.string.add_title)
                .setPositiveButton(R.string.choice_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO: update database
                        AlertDialog ad = (AlertDialog) dialog;
                        String amount_str = ((EditText)ad.findViewById(R.id.amount_diag)).getText().toString();
                        String item_spend_str = ((EditText)ad.findViewById(R.id.item_spend_diag)).getText().toString();
                        String desc_str = ((EditText)ad.findViewById(R.id.desc_diag)).getText().toString();
                        DbProcessTask task = new DbProcessTask(getContext());
                    }
                })
                .setNegativeButton(R.string.choice_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setView(R.layout.fragment_add_entry_dialog);
        return builder.create();
    }
}
