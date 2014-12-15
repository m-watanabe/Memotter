package com.suna.memotter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    private Activity mActivity;
    private static TweetDataRow itemData  = new TweetDataRow();
    private final static String FROM_TWICCA = "jp.r246.twicca.ACTION_SHOW_TWEET";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mActivity = this;

        Intent i = getIntent();

        if (i != null && i.getAction().toString().equals(FROM_TWICCA)) {
            // intentから起動
            // TODO
            // ここは後で別メソッドに移行する
            // intentのデータをDBに保存するようにする
            // 本文の解析は？
            itemData.setTweetText(i.getStringExtra("android.intent.extra.TEXT"));
            itemData.setTweetId(i.getStringExtra("id"));
            itemData.setLatitude(i.getStringExtra("latitude"));
            itemData.setLongitude(i.getStringExtra("longitude"));
            itemData.setLongitude(i.getStringExtra("longitude"));
            itemData.setCreate_at(i.getStringExtra("created_at"));
            itemData.setClient_name(i.getStringExtra("source"));
            itemData.setIn_reply_to_status_id(i.getStringExtra("in_reply_to_status_id"));
            itemData.setUser_screen_name(i.getStringExtra("user_screen_name"));
            itemData.setUser_name(i.getStringExtra("user_name"));
            itemData.setUser_id(i.getStringExtra("user_id"));
        } else {
            // そのまま起動
        }


    }

    @Override
    public void onResume() {
        super.onResume();

        // TODO
        // ここはDBからROWデータを取ってくるメソッドに変更する
        // 非同期がいいな。
        List<TweetDataRow> objects = new ArrayList<TweetDataRow>();
        objects.add(itemData);

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
}
