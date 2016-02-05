package com.fleuret.tipcalculator;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by James Fleuret on 9/29/2015.
 */
public class TipDB {
    // database constants
    public static final String DB_NAME = "tipslist.db";
    public static final int    DB_VERSION = 1;

    // tip table constants
    public static final String TIP_TABLE = "tip";

    public static final String TIP_ID = "_id";
    public static final int    TIP_ID_COL = 0;

    public static final String BILL_DATE = "bill_date";
    public static final int    BILL_DATE_COL = 1;

    public static final String BILL_AMOUNT = "bill_amount";
    public static final int    BILL_AMOUNT_COL = 2;

    public static final String TIP_PERCENT = "tip_percent";
    public static final int    TIP_PERCENT_COL = 3;


    public static final String CREATE_TIP_TABLE = "CREATE TABLE " + TIP_TABLE + " (TIP_ID INTEGER PRIMARY KEY AUTOINCREMENT, "+ BILL_DATE + " INTEGER NOT NULL, " + BILL_AMOUNT + " REAL NOT NULL, " + TIP_PERCENT + " REAL NOT NULL);";



    private static class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context, String name,
                        SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // create tables
            db.execSQL(CREATE_TIP_TABLE);

            // insert default/test tips
            db.execSQL("INSERT INTO tip VALUES (0, 0, 21.54, .15)");
            db.execSQL("INSERT INTO tip VALUES (1, 0, 65.43, .18)");

        }

        @Override
        public void onUpgrade(SQLiteDatabase db,
                              int oldVersion, int newVersion) {

            Log.d("Task list", "Upgrading db from version "
                    + oldVersion + " to " + newVersion);

            //db.execSQL(TaskListDB.DROP_LIST_TABLE);
            //db.execSQL(TaskListDB.DROP_TASK_TABLE);
            onCreate(db);
        }
    }

    // database and database helper objects
    private SQLiteDatabase db;
    private DBHelper dbHelper;

    // constructor
    public TipDB(Context context) {
        dbHelper = new DBHelper(context, DB_NAME, null, DB_VERSION);
    }

    // private methods
    private void openReadableDB() {
        db = dbHelper.getReadableDatabase();
    }

    private void openWriteableDB() {
        db = dbHelper.getWritableDatabase();
    }

    private void closeDB() {
        if (db != null)
            db.close();
    }

    private void closeCursor(Cursor c){
        if(c != null)
            c.close();
    }

    // public methods
    public ArrayList<Tip> getTips() {
        ArrayList<Tip> tipList = new ArrayList<Tip>();
        openReadableDB();
        Cursor cursor = db.query(TIP_TABLE,
                null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            Tip tip = new Tip();
            tip.setId(cursor.getInt(TIP_ID_COL));
            tip.setDateMillis(cursor.getInt(BILL_DATE_COL));
            tip.setBillAmount(cursor.getFloat(BILL_AMOUNT_COL));
            tip.setTipPercent(cursor.getFloat(TIP_PERCENT_COL));

            tipList.add(tip);
        }
        closeCursor(cursor);
        closeDB();

        return tipList;
    }

//    public List getList(String name) {
//        String where = LIST_NAME + "= ?";
//        String[] whereArgs = { name };
//
//        openReadableDB();
//        Cursor cursor = db.query(LIST_TABLE, null,
//                where, whereArgs, null, null, null);
//        List list = null;
//        cursor.moveToFirst();
//        list = new List(cursor.getInt(LIST_ID_COL),
//                cursor.getString(LIST_NAME_COL));
//        closeCursor(cursor);
//        this.closeDB();
//
//        return list;
//    }
//
//    public ArrayList<Task> getTasks(String listName) {
//        String where =
//                TASK_LIST_ID + "= ? AND " +
//                        TASK_HIDDEN + "!=1";
//        int listID = getList(listName).getId();
//        String[] whereArgs = { Integer.toString(listID) };
//
//        this.openReadableDB();
//        Cursor cursor = db.query(TASK_TABLE, null,
//                where, whereArgs,
//                null, null, null);
//        ArrayList<Task> tasks = new ArrayList<Task>();
//        while (cursor.moveToNext()) {
//            tasks.add(getTaskFromCursor(cursor));
//        }
//        closeCursor(cursor);
//        this.closeDB();
//
//        return tasks;
//    }
//
//    public Task getTask(int id) {
//        String where = TASK_ID + "= ?";
//        String[] whereArgs = { Integer.toString(id) };
//
//        this.openReadableDB();
//        Cursor cursor = db.query(TASK_TABLE,
//                null, where, whereArgs, null, null, null);
//        cursor.moveToFirst();
//        Task task = getTaskFromCursor(cursor);
//        closeCursor(cursor);
//        this.closeDB();
//
//        return task;
//    }
//
//    private static Task getTaskFromCursor(Cursor cursor) {
//        if (cursor == null || cursor.getCount() == 0){
//            return null;
//        }
//        else {
//            try {
//                Task task = new Task(
//                        cursor.getInt(TASK_ID_COL),
//                        cursor.getInt(TASK_LIST_ID_COL),
//                        cursor.getString(TASK_NAME_COL),
//                        cursor.getString(TASK_NOTES_COL),
//                        cursor.getInt(TASK_COMPLETED_COL),
//                        cursor.getInt(TASK_HIDDEN_COL));
//                return task;
//            }
//            catch(Exception e) {
//                return null;
//            }
//        }
//    }
//
//    public long insertTask(Task task) {
//        ContentValues cv = new ContentValues();
//        cv.put(TASK_LIST_ID, task.getListId());
//        cv.put(TASK_NAME, task.getName());
//        cv.put(TASK_NOTES, task.getNotes());
//        cv.put(TASK_COMPLETED, task.getCompletedDate());
//        cv.put(TASK_HIDDEN, task.getHidden());
//
//        this.openWriteableDB();
//        long rowID = db.insert(TASK_TABLE, null, cv);
//        this.closeDB();
//
//        return rowID;
//    }
//
//    public int updateTask(Task task) {
//        ContentValues cv = new ContentValues();
//        cv.put(TASK_LIST_ID, task.getListId());
//        cv.put(TASK_NAME, task.getName());
//        cv.put(TASK_NOTES, task.getNotes());
//        cv.put(TASK_COMPLETED, task.getCompletedDate());
//        cv.put(TASK_HIDDEN, task.getHidden());
//
//        String where = TASK_ID + "= ?";
//        String[] whereArgs = { String.valueOf(task.getId()) };
//
//        this.openWriteableDB();
//        int rowCount = db.update(TASK_TABLE, cv, where, whereArgs);
//        this.closeDB();
//
//        return rowCount;
//    }
//
//    public int deleteTask(long id) {
//        String where = TASK_ID + "= ?";
//        String[] whereArgs = { String.valueOf(id) };
//
//        this.openWriteableDB();
//        int rowCount = db.delete(TASK_TABLE, where, whereArgs);
//        this.closeDB();
//
//        return rowCount;
//    }
}
