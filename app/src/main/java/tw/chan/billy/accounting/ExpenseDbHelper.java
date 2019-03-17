package tw.chan.billy.accounting;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;


public class ExpenseDbHelper extends SQLiteOpenHelper {

    private static int DB_VERSION = 1;

    private static class DbColumns implements BaseColumns{
        public static final String TABLE_NAME = "expense";
        public static final String DATE = "date";
        public static final String AMOUNT = "amount";
        public static final String ITEM = "itm";
        public static final String DESC = "des";
        public static final String CLASS = "class";
    }

    public ExpenseDbHelper(Context context){
        super(context, DbColumns.TABLE_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query_str = "create table " + DbColumns.TABLE_NAME + " ( " + DbColumns._ID + " integer primary key, " +
                DbColumns.DATE + " text not null, " + DbColumns.AMOUNT + " integer not null, " + DbColumns.CLASS + " " +
                "text, " + DbColumns.ITEM + " text not null, " + DbColumns.DESC + " text)";
        db.execSQL(query_str);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // do nothing
    }

    public void dbInsert(String date, String amount, String clAss, String item, String desc){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = parseCV(date, amount, clAss, item, desc);
        long rowID = db.insert(DbColumns.TABLE_NAME, null, cv);
        if(rowID < 0) Log.e(getClass().getSimpleName(), "insert error");
        db.close();
    }
    public void dbUpdate(String date, String amount, String clAss, String rowID, String item, String desc){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = parseCV(date, amount, clAss, item, desc);
        String rowIDs[] = {rowID};
        int e = db.update(DbColumns.TABLE_NAME, cv, DbColumns._ID + " = ?", rowIDs);
        Log.v(getClass().getSimpleName(), String.valueOf(e));
        db.close();
    }
    public void dbDelete(String rowID){
        SQLiteDatabase db = getWritableDatabase();
        String rowIDs[] = { rowID };
        int e = db.delete(DbColumns.TABLE_NAME, DbColumns._ID + " = ?", rowIDs);
        Log.v(getClass().getSimpleName(), String.valueOf(e));
        db.close();
    }

    // TODO: query method

    private ContentValues parseCV(String date, String amount, String clAss, String item, String desc){
        ContentValues cv = new ContentValues();
        cv.put(DbColumns.DATE, date);
        cv.put(DbColumns.AMOUNT, Integer.parseInt(amount));
        if(clAss != null)
            cv.put(DbColumns.CLASS, clAss);
        cv.put(DbColumns.ITEM, item);
        if(desc != null)
            cv.put(DbColumns.DESC, desc);

        return cv;
    }
}
