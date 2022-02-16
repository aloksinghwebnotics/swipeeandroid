package com.webnotics.swipee.chat;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.twilio.conversations.Message;
import com.webnotics.swipee.R;
import com.webnotics.swipee.UrlManager.Config;

import org.jetbrains.annotations.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainChatActivity extends AppCompatActivity implements QuickstartConversationsManagerListener {

    public final static String TAG = "TwilioConversations";

    // Update this identity for each individual user, for instance after they login
    private String identity = "CONVERSATIONS_USER";

    private MessagesAdapter messagesAdapter;

    private EditText et_msg;
    ImageView iv_send,iv_profile,iv_back;
    TextView tv_name;

    private final QuickstartConversationsManager quickstartConversationsManager = new QuickstartConversationsManager();
    private String name="";
    private String msg_id="";
    private String appointment_id="";
    private String user_id="";
    private String job_id="";
    private String receiver="";
    private String sender="";
    private String senderRole="";
    private String receiverRole="";

    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getColor(R.color.white));
        mContext=this;
        quickstartConversationsManager.setListener(this);

        RecyclerView recyclerView = findViewById(R.id.rv_chat);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        identity= "info";
        // for a chat app, show latest messages at the bottom
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        messagesAdapter = new MessagesAdapter();
        recyclerView.setAdapter(messagesAdapter);

        tv_name = findViewById(R.id.tv_name);
        iv_profile = findViewById(R.id.iv_profile);
        et_msg = findViewById(R.id.et_msg);
        iv_send = findViewById(R.id.iv_send);
        iv_back = findViewById(R.id.iv_back);
        if (getIntent() != null) {
            String image = getIntent().getStringExtra("image");
            String action = getIntent().getStringExtra("action");
            name = getIntent().getStringExtra("name");
            msg_id = getIntent().getStringExtra("msg_id");
            appointment_id = getIntent().getStringExtra("appointment_id");
            user_id = getIntent().getStringExtra("user_id");
            job_id = getIntent().getStringExtra("job_id");
            String receiver_id = getIntent().getStringExtra("receiver_id");
            String sender_id = getIntent().getStringExtra("sender_id");
            if (user_id.equalsIgnoreCase(sender_id)) {
                receiver = sender_id;
                sender = receiver_id;
            } else if (user_id.equalsIgnoreCase(receiver_id)) {
                sender = sender_id;
                receiver = receiver_id;
            }

            senderRole = Config.isSeeker() ? "2" : "3";
            receiverRole = Config.isSeeker() ? "3" : "2";

            Glide.with(mContext).load(image).error(R.drawable.ic_profile_select)
                    .placeholder(R.drawable.ic_profile_select)
                    .into(iv_profile);
            tv_name.setText(name);

        }


        iv_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String messageBody = et_msg.getText().toString();
                if (messageBody.length() > 0) {
                    quickstartConversationsManager.sendMessage(messageBody);
                }
            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               onBackPressed();
            }
        });

        // Token Method 1 - supplied from strings.xml as the test_access_token
         quickstartConversationsManager.initializeWithAccessToken(this, getString(R.string.test_access_token));

        // Token Method 2 - retrieve the access token from a web server or Twilio Function
        //retrieveTokenFromServer();
    }

    private void retrieveTokenFromServer() {
        quickstartConversationsManager.retrieveAccessTokenFromServer(this, identity, new TokenResponseListener() {
            @Override
            public void receivedTokenResponse(boolean success, @Nullable Exception exception) {
                if (success) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // need to modify user interface elements on the UI thread
                            setTitle(identity);
                        }
                    });
                }
                else {
                    String errorMessage = getString(R.string.error_retrieving_access_token);
                    if (exception != null) {
                        errorMessage = errorMessage + " " + exception.getLocalizedMessage();
                    }
                    Log.d(MainChatActivity.TAG, "Running Fine");
                }
            }
        });
    }

    @Override
    public void receivedNewMessage() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // need to modify user interface elements on the UI thread
                messagesAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void reloadMessages() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // need to modify user interface elements on the UI thread
                messagesAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void messageSentCallback() {
        MainChatActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // need to modify user interface elements on the UI thread
                et_msg.setText("");
            }
        });
    }

    class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MyViewHolder> {

        public  class MyViewHolder extends RecyclerView.ViewHolder {

            TextView item, docfilename, dateago,dateagoImg,dateagoDoc;
            RelativeLayout rl_image, videolay, doclay;
            LinearLayout mainlay,ll_main2,textlay;
            ImageView img, videothumb, playyyy, pdfdoc;

            public MyViewHolder(View view) {
                super(view);

                item = view.findViewById(R.id.textView3);
                img = view.findViewById(R.id.img);
                videothumb = view.findViewById(R.id.videothumb);
                mainlay  =   view.findViewById(R.id.mainlay);
                textlay  =   view.findViewById(R.id.textlay);
                playyyy  =   view.findViewById(R.id.playyyy);
                docfilename  =   view.findViewById(R.id.docfilename);
                pdfdoc       =   view.findViewById(R.id.pdfdoc);
                videolay   =   view.findViewById(R.id.videolay);
                doclay   =   view.findViewById(R.id.doclay);
                dateago   =   view.findViewById(R.id.dateago);
                rl_image   =   view.findViewById(R.id.rl_image);
                ll_main2   =   view.findViewById(R.id.ll_main2);
                dateagoImg   =   view.findViewById(R.id.dateagoImg);
                dateagoDoc   =   view.findViewById(R.id.dateagoDoc);

            }
        }

        MessagesAdapter() {

        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chatmessagerowitems, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            Message message = quickstartConversationsManager.getMessages().get(position);

            String messageText = String.format("%s",message.getBody());
            holder.item.setText(messageText);
            holder.textlay.setVisibility(View.VISIBLE);
            holder.rl_image.setVisibility(View.GONE);
            holder.videolay.setVisibility(View.GONE);
            holder.doclay.setVisibility(View.GONE);
            holder.dateago.setVisibility(View.VISIBLE);

            Log.d("pparsedate",message.getDateCreated());

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            SimpleDateFormat formatout = new SimpleDateFormat("dd MMM hh:mm aa");
            Date dateFinal;
            String date1="";
            try {
                dateFinal = format.parse( message.getDateCreated());
                date1=  formatout.format(dateFinal);
                holder.dateago.setText(date1);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            holder.item.setTextColor(getColor(R.color.white));
            holder.item.setLinkTextColor(getColor(R.color.white));
            holder.dateago.setTextColor(getColor(R.color.white_light));
            holder.ll_main2.setBackgroundResource(R.drawable.ic_receiver_bubble);
            holder.mainlay.setGravity(Gravity.END);
            holder.ll_main2.setGravity(Gravity.END);
            holder.textlay.setGravity(Gravity.END);
               // holder.dateago.setCompoundDrawablesWithIntrinsicBounds(null,null,mContext.getDrawable(R.drawable.ic_double_tick),null);
                holder.dateago.setCompoundDrawablesWithIntrinsicBounds(null,null,getDrawable(R.drawable.ic_single_tick),null);



        }

        @Override
        public int getItemCount() {
            return quickstartConversationsManager.getMessages().size();
        }
    }
}
