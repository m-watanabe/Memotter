package com.suna.memotter;

import android.content.Context;
import android.text.Spannable;
import android.text.format.DateUtils;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class TweetListAdapter extends ArrayAdapter<TweetDataRow> {
    private LayoutInflater Inf;
    private Context mContext;
    private DBAdapter mDB = null;
    private TweetListAdapter mAdapter;

    int FLAGS =
            DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE |
                    DateUtils.FORMAT_SHOW_WEEKDAY | DateUtils.FORMAT_SHOW_YEAR |
                    DateUtils.FORMAT_ABBREV_ALL;

    private static class ViewHolder {
        TextView user_screen_name;
        TextView user_name;
        TextView user_id;
        TextView tweet_text;
        TextView create_at;
        TextView client_name;
        ImageView imageView;

        public ViewHolder(View view) {
            this.user_screen_name =  (TextView) view.findViewById(R.id.user_screen_name);
            this.user_name =  (TextView) view.findViewById(R.id.user_name);
            this.user_id =  (TextView) view.findViewById(R.id.user_id);
            this.tweet_text =  (TextView) view.findViewById(R.id.tweet_text);
            this.create_at =  (TextView) view.findViewById(R.id.create_at);
            this.client_name =  (TextView) view.findViewById(R.id.client_name);
            this.imageView =  (ImageView) view.findViewById(R.id.user_profile_image);
        }
    }

    public TweetListAdapter(Context context, int textViewResourceId, List<TweetDataRow> objects, DBAdapter DBAdapter) {
        super(context, textViewResourceId, objects);
        mContext = context;
        mDB = DBAdapter;
        mAdapter = this;
        Inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        TweetDataRow item = (TweetDataRow) getItem(position);

        if (null == convertView) {
            convertView = Inf.inflate(R.layout.tweet_list_row, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // CustomDataのデータをViewの各Widgetにセットする
        holder.user_screen_name.setText(item.getUser_screen_name());
        holder.user_name.setText(item.getUser_name());
        holder.user_id.setText(item.getUser_id());
        holder.tweet_text.setText(item.getTweetText());
        String timeString = item.getCreate_at();
        holder.create_at.setText(DateUtils.formatDateTime(mContext, Long.parseLong(timeString), FLAGS));
        holder.client_name.setText(item.getClient_name() + " から");
        Picasso.with(mContext).setLoggingEnabled(true);
        if(item.isTwicca()) {
            Picasso.with(mContext).load(item.getUser_profile_image_url()).error(R.drawable.ic_launcher).fit().into(holder.imageView);
        } else if(item.isWeb()) {
            Picasso.with(mContext).load(R.drawable.browser_icon).error(R.drawable.ic_launcher).fit().into(holder.imageView);
        }

        ImageView cardMenu = (ImageView) convertView.findViewById(R.id.card_menu);
        ImageView cardMenuDelete = (ImageView) convertView.findViewById(R.id.card_menu_delete);

        cardMenu menu = new cardMenu(position, convertView, getItem(position));
        cardMenu.setOnClickListener(menu);
        cardMenuDelete.setOnClickListener(menu);

        return convertView;
    }

    private String[] expandUrl(TextView tv) {
        // TODO 短縮URLの展開については後でちゃんと考える
        String[] expandedUrls = null;


        if(tv.getText() instanceof Spannable){
            URLSpan[] urls = tv.getUrls();
            int i = 0;
            expandedUrls = new String[urls.length];
            for (URLSpan urlSpan : urls) {
                expandedUrls[i] = urlSpan.getURL();
                i++;
            }
        }
        return expandedUrls;
    }

    private class cardMenu implements View.OnClickListener {
        private int mPosition = 0;
        private View mView = null;
        private TweetDataRow mItem = null;
        private boolean isDeleteMenu = false;

        cardMenu(int position, View view, TweetDataRow item) {
            mPosition = position;
            mView = view;
            mItem = item;
        }

        public void onClick(View v) {

            changeViewText();
            Button deleteCancel = (Button) mView.findViewById(R.id.delete_cancel);
            Button deleteOK = (Button) mView.findViewById(R.id.delete_ok);

            deleteCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeViewText();
                }

            });

            deleteOK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDB.open();
                    mDB.deleteTweet(mItem.getColId());
                    mDB.close();
                    mAdapter.remove(mItem);
                    mAdapter.notifyDataSetChanged();
                    changeViewText();
                }

            });
        }

        void changeViewText() {
            if (isDeleteMenu) {
                mView.findViewById(R.id.card_header_layout).setVisibility(View.VISIBLE);
                mView.findViewById(R.id.card_layout).setVisibility(View.VISIBLE);
                mView.findViewById(R.id.delete_msg_layout).setVisibility(View.GONE);
                isDeleteMenu = false;
            } else {
                int width = mView.findViewById(R.id.layout_root).getWidth();
                int height = mView.findViewById(R.id.layout_root).getHeight();

                mView.findViewById(R.id.card_header_layout).setVisibility(View.GONE);
                mView.findViewById(R.id.card_layout).setVisibility(View.GONE);
                mView.findViewById(R.id.delete_msg_layout).setVisibility(View.VISIBLE);

                mView.findViewById(R.id.layout_root).setMinimumHeight(height);

                isDeleteMenu = true;
            }
        }
    }


}