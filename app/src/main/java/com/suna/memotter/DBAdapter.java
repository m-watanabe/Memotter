package com.suna.memotter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBAdapter {

    static final String DATABASE_NAME = "memotter.db";
    static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "tweets";
    public static final String COL_ID = "_id";
    public static final String COL_TYPE = "type";
    public static final String COL_TWEET_TEXT = "tweet_text";
    public static final String COL_TWEET_ID = "tweet_id";
    public static final String COL_LATITUDE = "latitude";
    public static final String COL_LONGITUDE = "longitude";
    public static final String COL_CREATE_AT = "created_at";
    public static final String COL_CLIENT_NAME = "source";
    public static final String COL_IN_REPLY_TO = "in_reply_to_status_id";
    public static final String COL_USER_SCREEN_NAME = "user_screen_name";
    public static final String COL_USER_NAME = "user_name";
    public static final String COL_USER_ID = "user_id";
    public static final String COL_IMAGE = "user_profile_image";
    public static final String COL_IMAGE_MINI = "user_profile_image_mini";
    public static final String COL_IMAGE_NORMAL = "user_profile_image_normal";
    public static final String COL_IMAGE_BIGGER = "user_profile_image_bigger";

    protected final Context context;
    protected DatabaseHelper dbHelper;
    protected SQLiteDatabase db;

    public DBAdapter(Context context){
        this.context = context;
        dbHelper = new DatabaseHelper(this.context);
    }

    //
    // SQLiteOpenHelper
    //

    private static class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(
                    "CREATE TABLE " + TABLE_NAME + " ("
                            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                            + COL_TYPE + " TEXT NOT NULL,"
                            + COL_TWEET_TEXT + " TEXT NOT NULL,"
                            + COL_TWEET_ID + " TEXT,"
                            + COL_LATITUDE + " TEXT,"
                            + COL_LONGITUDE + " TEXT,"
                            + COL_CREATE_AT + " TEXT NOT NULL,"
                            + COL_CLIENT_NAME + " TEXT NOT NULL,"
                            + COL_IN_REPLY_TO + " TEXT,"
                            + COL_USER_SCREEN_NAME + " TEXT NOT NULL,"
                            + COL_USER_NAME + " TEXT NOT NULL,"
                            + COL_USER_ID + " TEXT,"
                            + COL_IMAGE + " TEXT NOT NULL,"
                            + COL_IMAGE_MINI + " TEXT NOT NULL,"
                            + COL_IMAGE_NORMAL + " TEXT NOT NULL,"
                            + COL_IMAGE_BIGGER + " TEXT NOT NULL);");
        }

        @Override
        public void onUpgrade(
                SQLiteDatabase db,
                int oldVersion,
                int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }

    }

    //
    // Adapter Methods
    //

    public DBAdapter open() {
        db = dbHelper.getWritableDatabase();
        return this;
    }


    public void close(){
        dbHelper.close();
    }

    //
    // App Methods
    //

    public boolean deleteAllTweets(){
        return db.delete(TABLE_NAME, null, null) > 0;
    }

    public boolean deleteTweet(int id){
        return db.delete(TABLE_NAME, COL_ID + "=" + id, null) > 0;
    }

    public Cursor getAllTweetsDB(){
        return db.query(TABLE_NAME, null, null, null, null, null, null);
    }

    public List<TweetDataRow> getAllTweets(){
        List<TweetDataRow> objects = new ArrayList<TweetDataRow>();

        this.open();

        Cursor c = db.query(TABLE_NAME, null, null, null, null, null, null);

        if (c.moveToLast()) {
            do {
                TweetDataRow item = new TweetDataRow();
                item.setColId(c.getInt(c.getColumnIndex(DBAdapter.COL_ID)));
                item.setType(c.getString(c.getColumnIndex(DBAdapter.COL_TYPE)));
                item.setTweetText(c.getString(c.getColumnIndex(DBAdapter.COL_TWEET_TEXT)));
                item.setTweetId(c.getString(c.getColumnIndex(DBAdapter.COL_TWEET_ID)));
                item.setLatitude(c.getString(c.getColumnIndex(DBAdapter.COL_LATITUDE)));
                item.setLongitude(c.getString(c.getColumnIndex(DBAdapter.COL_LONGITUDE)));
                item.setCreate_at(c.getString(c.getColumnIndex(DBAdapter.COL_CREATE_AT)));
                item.setClient_name(c.getString(c.getColumnIndex(DBAdapter.COL_CLIENT_NAME)));
                item.setIn_reply_to_status_id(c.getString(c.getColumnIndex(DBAdapter.COL_IN_REPLY_TO)));
                item.setUser_screen_name(c.getString(c.getColumnIndex(DBAdapter.COL_USER_SCREEN_NAME)));
                item.setUser_name(c.getString(c.getColumnIndex(DBAdapter.COL_USER_NAME)));
                item.setUser_id(c.getString(c.getColumnIndex(DBAdapter.COL_USER_ID)));
                item.setUser_profile_image_url(c.getString(c.getColumnIndex(DBAdapter.COL_IMAGE)));
                item.setUser_profile_image_url_mini(c.getString(c.getColumnIndex(DBAdapter.COL_IMAGE_MINI)));
                item.setUser_profile_image_url_normal(c.getString(c.getColumnIndex(DBAdapter.COL_IMAGE_NORMAL)));
                item.setUser_profile_image_url_bigger(c.getString(c.getColumnIndex(DBAdapter.COL_IMAGE_BIGGER)));
                objects.add(item);
            } while (c.moveToPrevious());
        }

        this.close();

        return(objects);
    }


    public void saveTweet(TweetDataRow item){
        ContentValues values = new ContentValues();
        values.put(COL_TYPE, item.getType());
        values.put(COL_TWEET_TEXT, item.getTweetText());
        values.put(COL_TWEET_ID, item.getTweetId());
        values.put(COL_LATITUDE, item.getLatitude());
        values.put(COL_LONGITUDE, item.getLongitude());
        values.put(COL_CREATE_AT, item.getCreate_at());
        values.put(COL_CLIENT_NAME, item.getClient_name());
        values.put(COL_IN_REPLY_TO, item.getIn_reply_to_status_id());
        values.put(COL_USER_SCREEN_NAME, item.getUser_screen_name());
        values.put(COL_USER_NAME, item.getUser_name());
        values.put(COL_USER_ID, item.getUser_id());
        values.put(COL_IMAGE, item.getUser_profile_image_url());
        values.put(COL_IMAGE_MINI, item.getUser_profile_image_url_mini());
        values.put(COL_IMAGE_NORMAL, item.getUser_profile_image_url_normal());
        values.put(COL_IMAGE_BIGGER, item.getUser_profile_image_url_bigger());
        db.insertOrThrow(TABLE_NAME, null, values);
    }
}
