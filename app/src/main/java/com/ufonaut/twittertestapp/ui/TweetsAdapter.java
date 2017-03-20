package com.ufonaut.twittertestapp.ui;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ufonaut.twittertestapp.Constants;
import com.ufonaut.twittertestapp.R;
import com.ufonaut.twittertestapp.api.model.HashTag;
import com.ufonaut.twittertestapp.api.model.MessageUrl;
import com.ufonaut.twittertestapp.api.model.Tweet;
import com.ufonaut.twittertestapp.api.model.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

class TweetsAdapter extends BaseAdapter {

    private static final String DATE_TIME_PATTERN = "EEE MMM dd HH:mm:ss Z yyyy";
    private static final String SHOW_DATE_TIME_PATTERN = "yyyy.MM.dd";

    private Context context;
    private List<Tweet> tweets = new ArrayList<>();
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_TIME_PATTERN, Locale.ENGLISH);
    private SimpleDateFormat showSimpleDateFormat = new SimpleDateFormat(SHOW_DATE_TIME_PATTERN, Locale.ENGLISH);

    TweetsAdapter(Context context, List<Tweet> tweets) {
        this.context = context;
        this.tweets = tweets;
    }

    @Override
    public int getCount() {
        return tweets.size();
    }

    @Override
    public Tweet getItem(int i) {
        return tweets.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        final ViewHolder viewHolder;

        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_tweet, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Tweet item = getItem(position);
        User user = item.getUser();
        String retweeted = user.getName() + Constants.BLANK + context.getString(R.string.retweeted);
        viewHolder.tvRetweeted.setVisibility(View.GONE);


        if (item.getRetweetedStatus() != null) {
            item = item.getRetweetedStatus();
            user = item.getUser();
            viewHolder.tvRetweeted.setVisibility(View.VISIBLE);
            viewHolder.tvRetweeted.setText(retweeted);
        }

        String screenName = user.getScreenName();
        String avatarUrl = Constants.PROFILE_PHOTO_PREFIX + screenName + Constants.PROFILE_PHOTO_SUFFIX;
        Glide.with(context).load(avatarUrl).into(viewHolder.ivAvatar);
        viewHolder.tvName.setText(user.getName());

        StringBuilder builder = new StringBuilder(screenName);
        builder.insert(0, context.getString(R.string.at));
        viewHolder.tvScreenName.setText(builder.toString());

        try {
            Date date = simpleDateFormat.parse(item.getCreatedAt());
            viewHolder.tvDate.setText(showSimpleDateFormat.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SpannableString text = getTextWithSpans(item);
        viewHolder.tvText.setText(text);
        viewHolder.tvText.setMovementMethod(LinkMovementMethod.getInstance());


        return convertView;
    }

    class ViewHolder {

        @BindView(R.id.ivAvatar)
        ImageView ivAvatar;
        @BindView(R.id.tvScreenName)
        TextView tvScreenName;
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.tvDate)
        TextView tvDate;
        @BindView(R.id.tvText)
        TextView tvText;
        @BindView(R.id.tvRetweeted)
        TextView tvRetweeted;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    private SpannableString getTextWithSpans(Tweet item) {
        SpannableString text = new SpannableString(item.getText());
        if (item.getEntities() == null) return text;

        if (item.getEntities().getHashTags() != null) {
            for (HashTag hashTag : item.getEntities().getHashTags()) {
                applySpanToText(text, hashTag.getIndices());
            }
        }

        if (item.getEntities().getUserMentions() != null) {
            for (User user : item.getEntities().getUserMentions()) {
                applySpanToText(text, user.getIndices());
            }
        }

        if (item.getEntities().getUrls() != null) {
            for (MessageUrl url : item.getEntities().getUrls()) {
                applySpanToText(text, url.getIndices());
            }
        }
        return text;
    }

    private void applySpanToText(SpannableString text, List<Integer> indices) {
        if (indices != null && indices.size() > 1)
            text.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View textView) {
                    Toast.makeText(context, R.string.span_text, Toast.LENGTH_SHORT).show();
                }
            }, indices.get(0), indices.get(1), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }
}
