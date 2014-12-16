package com.suna.memotter;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    public Activity mActivity;
    private final static String FROM_TWICCA = "jp.r246.twicca.ACTION_SHOW_TWEET";
    private final static String TWEET_TEXT = "android.intent.extra.TEXT";
    private final static String TWEET_ID = "id";
    private final static String LATITUDE = "latitude";
    private final static String LONGITUDE = "longitude";
    private final static String CREATE_AT = "created_at";
    private final static String CLIENT_NAME = "source";
    private final static String IN_REPLY_TO = "in_reply_to_status_id";
    private final static String USER_SCREEN_NAME = "user_screen_name";
    private final static String USER_NAME = "user_name";
    private final static String USER_ID = "user_id";
    private final static String IMAGE_URL = "user_profile_image_url";
    private final static String IMAGE_URL_MINI = "user_profile_image_url_mini";
    private final static String IMAGE_URL_NORMAL = "user_profile_image_url_normal";
    private final static String IMAGE_URL_BIGGER = "user_profile_image_url_bigger";
    static DBAdapter dbAdapter;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mActivity = this;
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mDrawerToggle = new ActionBarDrawerToggle(mActivity, mDrawerLayout, R.string.app_name, R.string.app_name);
        mDrawerToggle.setDrawerIndicatorEnabled(false);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        dbAdapter = new DBAdapter(this);
        Intent i = getIntent();

        if (i != null && i.getAction().toString().equals(FROM_TWICCA)) {
            // intentから起動
            setDataFromTwicca(i);
        } else {
            // そのまま起動
        }


    }

    @Override
    public void onResume() {
        super.onResume();

        setTweetList();
    }

    private void setTweetList() {
        List<TweetDataRow> objects = new ArrayList<TweetDataRow>();

        dbAdapter.open();
        Cursor c = dbAdapter.getAllTweets();

        if (c.moveToFirst()) {
            do {
                TweetDataRow item = new TweetDataRow();
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
            } while (c.moveToNext());
        }

        dbAdapter.close();

        TweetListAdapter adapater = new TweetListAdapter(mActivity, 0, objects);
        ListView list = (ListView) mActivity.findViewById(R.id.tweet_list);
        list.setAdapter(adapater);
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void deleteAllData(View v) {
        dbAdapter.open();
        dbAdapter.deleteAllTweets();
        dbAdapter.close();
    }

    private void setDataFromTwicca(Intent intent) {
        TweetDataRow itemData = new TweetDataRow();

        itemData.setTweetText(intent.getStringExtra(TWEET_TEXT));
        itemData.setTweetId(intent.getStringExtra(TWEET_ID));
        itemData.setLatitude(intent.getStringExtra(LATITUDE));
        itemData.setLongitude(intent.getStringExtra(LONGITUDE));
        itemData.setCreate_at(intent.getStringExtra(CREATE_AT));
        itemData.setClient_name(intent.getStringExtra(CLIENT_NAME));
        itemData.setIn_reply_to_status_id(intent.getStringExtra(IN_REPLY_TO));
        itemData.setUser_screen_name(intent.getStringExtra(USER_SCREEN_NAME));
        itemData.setUser_name(intent.getStringExtra(USER_NAME));
        itemData.setUser_id(intent.getStringExtra(USER_ID));
        itemData.setUser_profile_image_url(intent.getStringExtra(IMAGE_URL));
        itemData.setUser_profile_image_url_mini(intent.getStringExtra(IMAGE_URL_MINI));
        itemData.setUser_profile_image_url_normal(intent.getStringExtra(IMAGE_URL_NORMAL));
        itemData.setUser_profile_image_url_bigger(intent.getStringExtra(IMAGE_URL_BIGGER));

        dbAdapter.open();
        dbAdapter.saveTweet(itemData);
        dbAdapter.close();
    }


}
