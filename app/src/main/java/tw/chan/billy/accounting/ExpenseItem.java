package tw.chan.billy.accounting;

import android.util.Log;

final class ExpenseItem {

    private long mID;
    private String mDate, mAmount, mClAss, mItem, mDesc;

    public ExpenseItem(long ID, String date, String amount, String clAss, String item, String desc){
        mID = ID;
        mDate = date;
        mAmount = amount;
        mClAss = clAss;
        mItem = item;
        mDesc = desc;
    }

    public long getmID() {
        return mID;
    }

    public String getmAmount() {
        return mAmount;
    }

    public String getmDate() {
        return mDate;
    }

    public String getmDesc() {
        return mDesc;
    }

    public String getmClAss() {
        return mClAss;
    }

    public String getmItem() {
        return mItem;
    }

    public void setmAmount(String amount){
        try{
            Integer.parseInt(amount);
            mAmount = amount;
        }catch (NumberFormatException e){
            Log.e("ExpenseItem", "Number format error");
        }
    }

    public void setmDesc(String desc){
        mDesc = desc;
    }

    public void setmItem(String item){
        if(!item.isEmpty()){
            mItem = item;
        }
        else Log.e("ExpenseItem", "Item is empty");
    }

    public void setmDate(String date){
        if(!date.isEmpty()){
            mDate = date;
        }
        else Log.e("ExpenseItem", "Date is empty");
    }
}
