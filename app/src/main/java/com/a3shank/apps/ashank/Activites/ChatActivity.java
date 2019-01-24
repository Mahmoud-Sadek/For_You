package com.a3shank.apps.ashank.Activites;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.a3shank.apps.ashank.Adapters.ChatAdapter;
import com.a3shank.apps.ashank.Application_config.myApplication;
import com.a3shank.apps.ashank.R;
import com.a3shank.apps.ashank.models.ChatModel;
import com.a3shank.apps.ashank.models.Client;
import com.a3shank.apps.ashank.models.ConstantsAshank;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    DatabaseReference myMessagesDatabase;
    static Client model;
    GridView mChatGridView;
    List<ChatModel> chatModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.animation1, R.anim.animation2);
        setContentView(R.layout.activity_chat);
        myMessagesDatabase = myApplication.getDatabaseReference().child(ConstantsAshank.FIREBASE_LOCATION_MESSAGES).child(model.getClientId());

        mChatGridView = (GridView) findViewById(R.id.chat_gridView);
        myMessagesDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, ChatModel> results = dataSnapshot.getValue(new GenericTypeIndicator<HashMap<String, ChatModel>>() {
                });
                if (results != null) {
                    chatModels = new ArrayList<>(results.values());
                    ChatAdapter mChatAdapter = new ChatAdapter(getBaseContext(), chatModels);
                    mChatGridView.setAdapter(mChatAdapter);
                    mChatGridView.smoothScrollToPosition(chatModels.size());
                    mChatGridView.setSelection(chatModels.size());
                } else {
                    Toast.makeText(getBaseContext(), "No Messages Loaded", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Button sendButton = (Button) findViewById(R.id.btnSend);
        final EditText msgTxt = (EditText) findViewById(R.id.msg_txt);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = msgTxt.getText().toString().trim();
                if (msg != null) {
//                    HashMap<String, Object> timestampSent = new HashMap<>();
//                    timestampSent.put("timestamp", ServerValue.TIMESTAMP);
                    ChatModel chatModel = new ChatModel(msg, ConstantsAshank.CURRENT_USER_ID, ConstantsAshank.CURRENT_USER_NAME);
                    myMessagesDatabase.push().setValue(chatModel);
                    myMessagesDatabase.keepSynced(true);
                }
                msgTxt.setText("");
            }
        });
    }
}
