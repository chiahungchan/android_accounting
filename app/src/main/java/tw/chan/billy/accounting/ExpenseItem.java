package tw.chan.billy.accounting;

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
}
