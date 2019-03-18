package tw.chan.billy.accounting;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class ExpenseAdapter extends RecyclerView.Adapter {

    private ArrayList<ExpenseItem> list;
    public static final String POSITION = "tw.chan.billy.pos";
    private WeakReference<FragmentManager> mManager;

    public ExpenseAdapter(ArrayList<ExpenseItem> arrayList, FragmentManager manager){
        list = arrayList;
        mManager = new WeakReference<>(manager);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView mDateTv, mItemTv, mAmountTv;
        public ViewHolder(View v){
            super(v);
            mItemTv = v.findViewById(R.id.item_short_desc);
            mAmountTv = v.findViewById(R.id.item_value);
            mDateTv = v.findViewById(R.id.item_date);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            if(pos != RecyclerView.NO_POSITION){
                ExpenseItem curr = list.get(pos);

                // show edit dialog
                // pass text to fragment
                AddEntryDialogFragment dialog = new AddEntryDialogFragment();
                Bundle b = new Bundle();
                b.putString(ExpenseDbHelper.DbColumns.AMOUNT, curr.getmAmount());
                b.putString(ExpenseDbHelper.DbColumns.ITEM, curr.getmItem());
                b.putString(ExpenseDbHelper.DbColumns.DESC, curr.getmDesc());
                b.putInt(POSITION, pos);
                b.putLong(ExpenseDbHelper.DbColumns._ID, curr.getmID());
                dialog.setArguments(b);
                dialog.show(mManager.get(), "adapter");
            }
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.expense_list_item, viewGroup, false);
        return new ExpenseAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ExpenseItem curr = list.get(i);
        ExpenseAdapter.ViewHolder vholder = (ExpenseAdapter.ViewHolder)viewHolder;
        vholder.mDateTv.setText(curr.getmDate());
        vholder.mAmountTv.setText(curr.getmAmount());
        vholder.mItemTv.setText(curr.getmItem());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
