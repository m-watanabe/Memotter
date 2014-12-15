package com.suna.memotter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class TweetListAdapter extends ArrayAdapter<TweetDataRow> {
    private LayoutInflater Inf;

    public TweetListAdapter(Context context, int textViewResourceId, List<TweetDataRow> objects) {
        super(context, textViewResourceId, objects);

        Inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 特定の行(position)のデータを得る
        TweetDataRow item = (TweetDataRow) getItem(position);

        // convertViewは使い回しされている可能性があるのでnullの時だけ新しく作る
        if (null == convertView) {
            convertView = Inf.inflate(R.layout.tweet_list_row, null);
        }

        // CustomDataのデータをViewの各Widgetにセットする
        TextView user_screen_name;
        user_screen_name = (TextView) convertView.findViewById(R.id.user_screen_name);
        user_screen_name.setText(item.getUser_screen_name());

        TextView user_name;
        user_name = (TextView) convertView.findViewById(R.id.user_name);
        user_name.setText(item.getUser_name());

        TextView user_id;
        user_id = (TextView) convertView.findViewById(R.id.user_id);
        user_id.setText(item.getUser_id());

        TextView tweet_text;
        tweet_text = (TextView) convertView.findViewById(R.id.tweet_text);
        tweet_text.setText(item.getTweetText());

        TextView create_at;
        create_at = (TextView) convertView.findViewById(R.id.create_at);
        create_at.setText(item.getCreate_at());

        TextView client_name;
        client_name = (TextView) convertView.findViewById(R.id.client_name);
        client_name.setText(item.getClient_name() + " から");

        return convertView;
    }
}