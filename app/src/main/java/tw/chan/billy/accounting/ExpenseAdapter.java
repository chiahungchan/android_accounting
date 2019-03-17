package tw.chan.billy.accounting;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class ExpenseAdapter extends RecyclerView.Adapter {

    private ArrayList<ExpenseItem> list;

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView mDateTv, mItemTv, mAmountTv;
        public ViewHolder(View v){
            super(v);
            mItemTv = v.findViewById(R.id.item_short_desc);
            mAmountTv = v.findViewById(R.id.item_value);
            mDateTv = v.findViewById(R.id.item_date);
        }
    }

    public ExpenseAdapter(ArrayList<ExpenseItem> arrayList){
        list = arrayList;
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
