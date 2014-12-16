package com.suna.memotter;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class TweetListAdapter extends ArrayAdapter<TweetDataRow> {
    private LayoutInflater Inf;
    private Context mContext;

    int FLAGS =
            DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE |
            DateUtils.FORMAT_SHOW_WEEKDAY | DateUtils.FORMAT_SHOW_YEAR |
            DateUtils.FORMAT_ABBREV_ALL;

    public TweetListAdapter(Context context, int textViewResourceId, List<TweetDataRow> objects) {
        super(context, textViewResourceId, objects);
        mContext = context;
        Inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TweetDataRow item = (TweetDataRow) getItem(position);

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
        String timeString = item.getCreate_at();
        create_at.setText(DateUtils.formatDateTime(mContext, Long.parseLong(timeString), FLAGS));

        TextView client_name;
        client_name = (TextView) convertView.findViewById(R.id.client_name);
        client_name.setText(item.getClient_name() + " から");

        ImageView imageView;
        imageView  = (ImageView) convertView.findViewById(R.id.user_profile_image);
        Picasso.with(mContext).setLoggingEnabled(true);
        Picasso.with(mContext).load(item.getUser_profile_image_url()).error(R.drawable.ic_launcher).fit().into(imageView);

        return convertView;
    }
}