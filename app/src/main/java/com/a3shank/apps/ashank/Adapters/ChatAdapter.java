package com.a3shank.apps.ashank.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.a3shank.apps.ashank.R;
import com.a3shank.apps.ashank.models.ChatModel;
import com.a3shank.apps.ashank.models.ConstantsAshank;

import java.util.List;

/**
 * Created by Sadokey on 12/22/2016.
 */

public class ChatAdapter extends BaseAdapter {

    Context mContext;
    List<ChatModel> chatModels;
    Typeface myTypeface;

    public ChatAdapter(Context mContext, List<ChatModel> chatModels) {
        this.mContext = mContext;
        this.chatModels = chatModels;

        myTypeface = Typeface.createFromAsset(mContext.getAssets(), "arabic2.otf");
    }

    @Override
    public int getCount() {
        return chatModels.size();
    }

    @Override
    public Object getItem(int position) {
        return chatModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (chatModels.get(position).getMsgSenderID().equals(ConstantsAshank.CURRENT_USER_ID)) {
            convertView = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.chat_item_sent, parent, false);

            ChatModel mChatModel = chatModels.get(position);
//            TextView msgDateTextView = (TextView) convertView.findViewById(R.id.date_txtView);
            TextView msgTextView = (TextView) convertView.findViewById(R.id.msg_txtView);
            msgTextView.setTypeface(myTypeface);
            TextView msgSendTextView = (TextView) convertView.findViewById(R.id.sending_txtView);
            msgSendTextView.setTypeface(myTypeface);
            TextView msgSenderTextView = (TextView) convertView.findViewById(R.id.rcv_txtView);
            msgSenderTextView.setTypeface(myTypeface);
//            msgDateTextView.setText(mChatModel.getTimestampCreatedLong()+"");
            msgTextView.setText(mChatModel.getMsgText());
            msgSendTextView.setText("Sent");
            msgSenderTextView.setText(mChatModel.getMsgSenderName());
        } else {
            convertView = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.chat_item_rcv, parent, false);
            ChatModel mChatModel = chatModels.get(position);
            TextView msgDateTextView = (TextView) convertView.findViewById(R.id.date_txtView);
            TextView msgTextView = (TextView) convertView.findViewById(R.id.msg_txtView);
            msgTextView.setTypeface(myTypeface);
            TextView msgSendTextView = (TextView) convertView.findViewById(R.id.sending_txtView);
            msgSendTextView.setTypeface(myTypeface);
            TextView msgSenderTextView = (TextView) convertView.findViewById(R.id.rcv_txtView);
            msgSenderTextView.setTypeface(myTypeface);
//            msgDateTextView.setText(mChatModel.getTimestampCreatedLong()+"");
            msgTextView.setText(mChatModel.getMsgText());
            msgSendTextView.setText("recived");
            msgSenderTextView.setText(mChatModel.getMsgSenderName());
        }
        return convertView;


    }
}
