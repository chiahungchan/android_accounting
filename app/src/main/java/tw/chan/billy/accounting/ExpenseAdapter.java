package tw.chan.billy.accounting;

import android.content.Context;
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
import java.util.Comparator;
import java.util.TreeSet;

public class ExpenseAdapter extends RecyclerView.Adapter {

    private ArrayList<ExpenseItem> list;
    public static final String POSITION = "tw.chan.billy.pos";
    private WeakReference<FragmentManager> mManager;
    private boolean in_action_mode;
    private TreeSet<ExpenseItem> set;

    public ExpenseAdapter(ArrayList<ExpenseItem> arrayList, FragmentManager manager, boolean action_mode){
        list = arrayList;
        mManager = new WeakReference<>(manager);
        in_action_mode = action_mode;
        set = new TreeSet<>(new Comparator<ExpenseItem>() {
            @Override
            public int compare(ExpenseItem o1, ExpenseItem o2) {
                long id1 = o1.getmID(), id2 = o2.getmID();
                return Long.compare(id1, id2);
            }
        });
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ExpenseItem curr = list.get(i);
        ExpenseAdapter.ViewHolder vholder = (ExpenseAdapter.ViewHolder)viewHolder;
        vholder.mDateTv.setText(curr.getmDate());
        vholder.mAmountTv.setText(curr.getmAmount());
        vholder.mItemTv.setText(curr.getmItem());
        Context c = ((ViewHolder) viewHolder).mRootView.getContext();
        if(set.contains(curr))
            ((ViewHolder) viewHolder).mRootView.setBackgroundResource(R.color.select_bg);
        else{
            ((ViewHolder) viewHolder).mRootView.setBackgroundResource((i%2 == 0) ? R.color.clickable_color : android.R.color.white);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.expense_list_item, viewGroup, false);
        return new ExpenseAdapter.ViewHolder(itemView);
    }

    public void setInActionMode(boolean mode){
        in_action_mode = mode;
        if(!mode) {
            set.clear();
            notifyDataSetChanged(); // in order to change background color
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView mDateTv, mItemTv, mAmountTv;
        public View mRootView;
        public ViewHolder(View v){
            super(v);
            mItemTv = v.findViewById(R.id.item_short_desc);
            mAmountTv = v.findViewById(R.id.item_value);
            mDateTv = v.findViewById(R.id.item_date);
            mRootView = v;
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            if(pos != RecyclerView.NO_POSITION){
                ExpenseItem curr = list.get(pos);
                if(in_action_mode){
                    if(set.contains(curr)){
                        set.remove(curr);
                        v.setBackgroundResource((pos%2 == 0) ? R.color.clickable_color : android.R.color.white);
                    }
                    else{
                        set.add(curr);
                        v.setBackgroundResource(R.color.select_bg);
                    }
                }
                else{
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
    }

}
