package com.fleuret.finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by James Fleuret on 10/7/2015.
 */
public class FeedDB {
    // database constants
    public static final String DB_NAME = "rssfeeds.db";
    public static final int    DB_VERSION = 1;

     // feeds table constants
    public static final String FEEDS_TABLE = "feeds";

    public static final String FEED_ID = "_id";
    public static final int    FEED_ID_COL = 0;

    public static final String FEED_TITLE = "title";
    public static final int    FEED_TITLE_COL = 2;

    public static final String FEED_URL = "url";
    public static final int    FEED_URL_COL = 3;


    // CREATE and DROP TABLE statements
    public static final String CREATE_FEEDS_TABLE =
            "CREATE TABLE " + FEEDS_TABLE + " (" +
                    FEED_ID         + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    FEED_TITLE      + " TEXT UNIQUE, " +
                    FEED_URL        + " TEXT);";

    public static final String DROP_FEEDS_TABLE = "DROP TABLE IF EXISTS " + FEEDS_TABLE;

    private static class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context, String name,
                        SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // create rssfeed table
            db.execSQL(CREATE_FEEDS_TABLE);

            // two default feeds from Wired Magazine
            db.execSQL("INSERT INTO feeds VALUES ('Wired - Top Stories', 'http://feeds.wired.com/wired/index')");
            db.execSQL("INSERT INTO feeds VALUES ('Wired - Tech', 'http://www.wired.com/category/gear/feed/')");

        }

        @Override
        public void onUpgrade(SQLiteDatabase db,
                              int oldVersion, int newVersion) {

            Log.d("RssFeeds", "Upgrading db from version "
                    + oldVersion + " to " + newVersion);

            Log.d("Task list", "Deleting all data!");
            db.execSQL(FeedDB.DROP_FEEDS_TABLE);
            onCreate(db);
        }
    }

    // database object and database helper object
    private SQLiteDatabase db;
    private DBHelper dbHelper;

    // constructor
    public FeedDB(Context context) {
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

    // public methods

    public ArrayList<Feed> getTasks(String listName) {

        this.openReadableDB();
        Cursor cursor = db.query(FEEDS_TABLE, null, null, null, null, null, null);
        ArrayList<Feed> tasks = new ArrayList<Feed>();
        while (cursor.moveToNext()) {
            tasks.add(getFeedFromCursor(cursor));
        }
        if (cursor != null)
            cursor.close();
        this.closeDB();
        return tasks;
    }

    private static Feed getFeedFromCursor(Cursor cursor) {
        if (cursor == null || cursor.getCount() == 0){
            return null;
        }
        else {
            try {
                Feed feed = new Feed(cursor.getString(FEED_TITLE_COL), cursor.getString(FEED_URL_COL), cursor.getInt(FEED_ID_COL));
                return feed;
            }
            catch(Exception e) {
                return null;
            }
        }
    }

    public long insertFeed(Feed feed) {
        ContentValues cv = new ContentValues();
        cv.put(FEED_TITLE, feed.getTitle());
        cv.put(FEED_URL, feed.getUrl());

        this.openWriteableDB();
        long rowID = db.insert(FEEDS_TABLE, null, cv);
        this.closeDB();

        return rowID;
    }

    public int updateFeed(Feed feed) {
        ContentValues cv = new ContentValues();
        cv.put(FEED_ID, feed.getId());
        cv.put(FEED_TITLE, feed.getTitle());
        cv.put(FEED_URL, feed.getUrl());

        String where = FEED_ID + "= ?";
        String[] whereArgs = { String.valueOf(feed.getId()) };

        this.openWriteableDB();
        int rowCount = db.update(FEEDS_TABLE, cv, where, whereArgs);
        this.closeDB();

        return rowCount;
    }

    public int deleteFeed(long id) {
        String where = FEED_ID + "= ?";
        String[] whereArgs = { String.valueOf(id) };

        this.openWriteableDB();
        int rowCount = db.delete(FEEDS_TABLE, where, whereArgs);
        this.closeDB();

        return rowCount;
    }
}
