package com.webnotics.swipee.chat;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.twilio.conversations.ErrorInfo;
import com.twilio.conversations.MediaUploadListener;
import com.twilio.conversations.Message;
import com.webnotics.swipee.R;
import com.webnotics.swipee.UrlManager.AppController;
import com.webnotics.swipee.UrlManager.Config;
import com.webnotics.swipee.activity.CompanyProfile;
import com.webnotics.swipee.activity.Seeker.SeekerHomeActivity;
import com.webnotics.swipee.activity.company.CompanyHomeActivity;
import com.webnotics.swipee.activity.company.UserDetail;

import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

public class MainChatActivity extends AppCompatActivity implements QuickstartConversationsManagerListener {

    public final static String TAG = "TwilioConversations";

    // Update this identity for each individual user, for instance after they login
    private String identity = "CONVERSATIONS_USER";

    private MessagesAdapter messagesAdapter;

    private EditText et_msg;
    ImageView iv_send, iv_profile, iv_back;
    TextView tv_name;

    private final QuickstartConversationsManager quickstartConversationsManager = new QuickstartConversationsManager();
    private String name = "";
    private String msg_id = "";
    private String appointment_id = "";
    private String user_id = "";
    private String job_id = "";
     public String appointment_number = "";

    Context mContext;
    private LinearLayoutManager layoutManager;
    private ArrayList<Message> mainChatListTemp = new ArrayList<>();
    private ArrayList<Message> mainChatList = new ArrayList<>();


    private static final int REQUEST_IMAGE_CAPTURE = 212;
    private static final int IMG_RESULT = 111;
    private static final int DOCUMENT_FILE_REQUEST = 214;
    private RelativeLayout rl_scroll;
    private BottomSheetBehavior bottomsheet_intent;
    @SuppressLint("StaticFieldLeak")
    public static MainChatActivity instance;

    ImageView iv_camera, iv_doc, iv_scroll, iv_refresh;
    TextView tv_action, tv_newmsgcount;
    private int recentMsg = 0;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getColor(R.color.white));
        mContext = this;
        instance = this;
        quickstartConversationsManager.setListener(this);

        recyclerView = findViewById(R.id.rv_chat);

        layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);

        identity = Config.GetId();
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

        tv_action = findViewById(R.id.tv_action);
        iv_camera = findViewById(R.id.iv_camera);
        iv_doc = findViewById(R.id.iv_doc);
        iv_scroll = findViewById(R.id.iv_scroll);
        rl_scroll = findViewById(R.id.rl_scroll);
        tv_newmsgcount = findViewById(R.id.tv_newmsgcount);
        iv_refresh = findViewById(R.id.iv_refresh);
        iv_refresh.setVisibility(View.GONE);


        if (getIntent() != null) {
            String image = getIntent().getStringExtra("image");
            name = getIntent().getStringExtra("name");
            msg_id = getIntent().getStringExtra("msg_id");
            appointment_id = getIntent().getStringExtra("appointment_id");
            appointment_number = getIntent().getStringExtra("appointment_number");
            user_id = getIntent().getStringExtra("user_id");
            job_id = getIntent().getStringExtra("job_id");

            Glide.with(mContext).load(image).error(R.drawable.ic_profile_select)
                    .placeholder(R.drawable.ic_profile_select)
                    .into(iv_profile);
            tv_name.setText(name);

        }

        iv_send.setOnClickListener(view -> {
            String messageBody = et_msg.getText().toString();
            if (messageBody.replaceAll(" ", "").length() > 0) {
                if (layoutManager != null) {
                    layoutManager.scrollToPosition(mainChatListTemp.size() - 1);
                }
                quickstartConversationsManager.sendMessage(messageBody.trim());
                et_msg.setText("");
                rl_scroll.setVisibility(View.GONE);
                tv_newmsgcount.setVisibility(View.GONE);
                et_msg.setText("");
                recentMsg = 0;

            }
        });
        iv_back.setOnClickListener(view -> onBackPressed());


        iv_profile.setOnClickListener(v -> callProfile());
        tv_name.setOnClickListener(v -> callProfile());

        iv_doc.setOnClickListener(v -> callFile());
        iv_camera.setOnClickListener(v -> bottomsheet_intent.setState(BottomSheetBehavior.STATE_EXPANDED));


        industryBottomSheet();
        iv_scroll.setOnClickListener(v -> {
            if (layoutManager != null) {
                layoutManager.scrollToPosition(mainChatListTemp.size() - 1);
            }
            recentMsg = 0;
            tv_newmsgcount.setText("");
            rl_scroll.setVisibility(View.GONE);
            tv_newmsgcount.setVisibility(View.GONE);
        });


        // Token Method 1 - supplied from strings.xml as the test_access_token
        //quickstartConversationsManager.initializeWithAccessToken(this, getString(R.string.test_access_token));

        // Token Method 2 - retrieve the access token from a web server or Twilio Function
        retrieveTokenFromServer();
    }

    private void retrieveTokenFromServer() {
        AppController.ShowDialogue("", mContext);
        quickstartConversationsManager.retrieveAccessTokenFromServer(this, user_id, appointment_id, appointment_number, new TokenResponseListener() {
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
                } else {
                    AppController.dismissProgressdialog();
                    String errorMessage = getString(R.string.error_retrieving_access_token);
                    if (exception != null) {
                        errorMessage = errorMessage + " " + exception.getLocalizedMessage();
                    }
                    Log.d(MainChatActivity.TAG, "Running Fine");
                }
            }
        });
    }


    private void callFile() {
        String[] supportedMimeTypes = {"application/pdf"};
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType(supportedMimeTypes[0]);
        intent.putExtra(Intent.EXTRA_MIME_TYPES, supportedMimeTypes);
        startActivityForResult(intent, DOCUMENT_FILE_REQUEST);
    }

    private void industryBottomSheet() {
        RelativeLayout intent_sheet = findViewById(R.id.intent_sheet);
        bottomsheet_intent = BottomSheetBehavior.from(intent_sheet);
        TextView tv_camera = intent_sheet.findViewById(R.id.tv_camera);
        TextView tv_gallery = intent_sheet.findViewById(R.id.tv_gallery);
        TextView tv_cancel = intent_sheet.findViewById(R.id.tv_cancel);
        tv_camera.setOnClickListener(v -> {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            try {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            } catch (ActivityNotFoundException e) {
                // display error state to the user
            }
            bottomsheet_intent.setState(BottomSheetBehavior.STATE_COLLAPSED);
        });
        tv_gallery.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            startActivityForResult(intent, IMG_RESULT);
            bottomsheet_intent.setState(BottomSheetBehavior.STATE_COLLAPSED);
        });
        tv_cancel.setOnClickListener(v -> bottomsheet_intent.setState(BottomSheetBehavior.STATE_COLLAPSED));

    }

    @Override
    public void receivedNewMessage() {
        runOnUiThread(this::setDataToView);
    }

    @Override
    public void reloadMessages() {
        // need to modify user interface elements on the UI thread
        runOnUiThread(this::setDataToView);
    }

    @Override
    public void messageSentCallback() {
        runOnUiThread(() -> {
            et_msg.setText("");
        });
    }

    class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MyViewHolder> {

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView item, docfilename, dateago, dateagoImg, dateagoDoc;
            RelativeLayout rl_image, videolay, doclay;
            LinearLayout mainlay, ll_main2, textlay;
            ImageView img, videothumb, playyyy, pdfdoc;

            public MyViewHolder(View view) {
                super(view);

                item = view.findViewById(R.id.textView3);
                img = view.findViewById(R.id.img);
                videothumb = view.findViewById(R.id.videothumb);
                mainlay = view.findViewById(R.id.mainlay);
                textlay = view.findViewById(R.id.textlay);
                playyyy = view.findViewById(R.id.playyyy);
                docfilename = view.findViewById(R.id.docfilename);
                pdfdoc = view.findViewById(R.id.pdfdoc);
                videolay = view.findViewById(R.id.videolay);
                doclay = view.findViewById(R.id.doclay);
                dateago = view.findViewById(R.id.dateago);
                rl_image = view.findViewById(R.id.rl_image);
                ll_main2 = view.findViewById(R.id.ll_main2);
                dateagoImg = view.findViewById(R.id.dateagoImg);
                dateagoDoc = view.findViewById(R.id.dateagoDoc);

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
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            Message message = quickstartConversationsManager.getMessages().get(position);
            String messageText = String.format("%s", message.getBody());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            format.setTimeZone(TimeZone.getTimeZone("UTC"));
            SimpleDateFormat formatout = new SimpleDateFormat("dd MMM hh:mm aa");
            Calendar calendar = Calendar.getInstance();
            formatout.setTimeZone(calendar.getTimeZone());
            Date dateFinal;
            String date1 = "";
            try {
                dateFinal = format.parse(message.getDateCreated());
                date1 = formatout.format(dateFinal);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (message.getAttachedMedia().size() > 0) {

                if (message.getAttachedMedia().get(0).getContentType().equalsIgnoreCase("image")) {
                    holder.dateagoImg.setText(date1);
                    holder.textlay.setVisibility(View.GONE);
                    holder.rl_image.setVisibility(View.VISIBLE);
                    holder.videolay.setVisibility(View.GONE);
                    holder.doclay.setVisibility(View.GONE);
                    holder.dateagoImg.setVisibility(View.VISIBLE);
                    if (message.getAuthor().equalsIgnoreCase(Config.GetEmail()+"_"+Config.GetId())) {
                        try {
                            Glide.with(mContext).clear(holder.img);
                            message.getAttachedMedia().get(0).getTemporaryContentUrl(result -> {
                                try {
                                    Glide.with(mContext)
                                            .load(result)
                                            .transform(new MultiTransformation(new CenterCrop(), new RoundedCorners((int) (mContext.getResources().getDisplayMetrics().density * 8))))
                                            .into(holder.img);
                                } catch (Exception ignored) {
                                }
                            });
                        } catch (Exception ignored) {
                        }

                        holder.ll_main2.setBackgroundResource(R.drawable.ic_receiver_bubble);
                        holder.mainlay.setGravity(Gravity.END);
                        holder.ll_main2.setGravity(Gravity.END);
                        holder.dateagoImg.setGravity(Gravity.END);
                        holder.dateagoImg.setTextColor(mContext.getColor(R.color.white_light));

                    } else {
                        try {
                            Glide.with(mContext).clear(holder.img);
                            message.getAttachedMedia().get(0).getTemporaryContentUrl(result -> {
                                try {
                                    Glide.with(mContext)
                                            .load(result)
                                            .transform(new MultiTransformation(new CenterCrop(), new RoundedCorners((int) (mContext.getResources().getDisplayMetrics().density * 8))))
                                            .into(holder.img);
                                } catch (Exception ignored) {
                                }
                            });
                        } catch (Exception ignored) {
                        }

                        holder.mainlay.setGravity(Gravity.START);
                        holder.ll_main2.setGravity(Gravity.START);
                        holder.dateagoImg.setGravity(Gravity.START);
                        holder.dateagoImg.setTextColor(mContext.getColor(R.color.gray));
                        holder.ll_main2.setBackgroundResource(R.drawable.ic_sender_bubble);
                    }
                } else if (message.getAttachedMedia().get(0).getContentType().equalsIgnoreCase("application/pdf") || message.getAttachedMedia().get(0).getContentType().equalsIgnoreCase("pdf") || message.getAttachedMedia().get(0).getContentType().equalsIgnoreCase("doc")) {
                    holder.textlay.setVisibility(View.GONE);
                    holder.rl_image.setVisibility(View.GONE);
                    holder.videolay.setVisibility(View.GONE);
                    holder.doclay.setVisibility(View.VISIBLE);
                    holder.dateagoDoc.setVisibility(View.VISIBLE);
                    holder.dateagoDoc.setText(date1);
                    if (message.getAuthor().equalsIgnoreCase(Config.GetEmail()+"_"+Config.GetId())) {
                        if (Objects.requireNonNull(message.getAttachedMedia().get(0).getFilename()).contains(".pdf")) {
                            holder.docfilename.setText(message.getAttachedMedia().get(0).getFilename());
                            holder.pdfdoc.setImageResource(R.drawable.pdf);
                        } else {
                            holder.docfilename.setText(message.getAttachedMedia().get(0).getFilename());
                            holder.pdfdoc.setImageResource(R.drawable.doc);
                        }
                        holder.docfilename.setTextColor(mContext.getColor(R.color.white));
                        holder.dateagoDoc.setTextColor(mContext.getColor(R.color.white_light));
                        holder.ll_main2.setBackgroundResource(R.drawable.ic_receiver_bubble);
                        holder.mainlay.setGravity(Gravity.END);
                        holder.ll_main2.setGravity(Gravity.END);
                        holder.dateagoDoc.setGravity(Gravity.END);

                    } else {
                        if (Objects.requireNonNull(message.getAttachedMedia().get(0).getFilename()).contains(".pdf")) {
                            holder.docfilename.setText(message.getAttachedMedia().get(0).getFilename());
                            holder.pdfdoc.setImageResource(R.drawable.pdf);

                        } else {
                            holder.docfilename.setText(message.getAttachedMedia().get(0).getFilename());
                            holder.pdfdoc.setImageResource(R.drawable.doc);
                        }

                        holder.docfilename.setTextColor(mContext.getColor(R.color.black));
                        holder.dateagoDoc.setTextColor(mContext.getColor(R.color.gray));
                        holder.ll_main2.setBackgroundResource(R.drawable.ic_sender_bubble);
                        holder.mainlay.setGravity(Gravity.START);
                        holder.ll_main2.setGravity(Gravity.START);
                        holder.dateagoDoc.setGravity(Gravity.START);
                        holder.dateagoDoc.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    }
                } else {

                    holder.textlay.setVisibility(View.VISIBLE);
                    holder.rl_image.setVisibility(View.GONE);
                    holder.videolay.setVisibility(View.GONE);
                    holder.doclay.setVisibility(View.GONE);
                    holder.dateago.setVisibility(View.VISIBLE);
                    holder.dateago.setText(date1);

                    if (message.getAuthor().equalsIgnoreCase(Config.GetEmail()+"_"+Config.GetId())) {
                        holder.item.setTextColor(mContext.getColor(R.color.white));
                        holder.item.setLinkTextColor(mContext.getColor(R.color.white));
                        holder.dateago.setTextColor(mContext.getColor(R.color.white_light));
                        holder.ll_main2.setBackgroundResource(R.drawable.ic_receiver_bubble);
                        holder.mainlay.setGravity(Gravity.END);
                        holder.ll_main2.setGravity(Gravity.END);
                        holder.textlay.setGravity(Gravity.END);
                    } else {
                        holder.item.setTextColor(mContext.getColor(R.color.black));
                        holder.item.setLinkTextColor(mContext.getColor(R.color.black));
                        holder.dateago.setTextColor(mContext.getColor(R.color.gray));
                        holder.mainlay.setGravity(Gravity.START);
                        holder.ll_main2.setGravity(Gravity.START);
                        holder.textlay.setGravity(Gravity.START);
                        holder.ll_main2.setBackgroundResource(R.drawable.ic_sender_bubble);
                    }
                    holder.item.setText(messageText);
                }

            } else {

                holder.textlay.setVisibility(View.VISIBLE);
                holder.rl_image.setVisibility(View.GONE);
                holder.videolay.setVisibility(View.GONE);
                holder.doclay.setVisibility(View.GONE);
                holder.dateago.setVisibility(View.VISIBLE);
                holder.dateago.setText(date1);

                if (message.getAuthor().equalsIgnoreCase(Config.GetEmail()+"_"+Config.GetId())) {
                    holder.item.setTextColor(mContext.getColor(R.color.white));
                    holder.item.setLinkTextColor(mContext.getColor(R.color.white));
                    holder.dateago.setTextColor(mContext.getColor(R.color.white_light));
                    holder.ll_main2.setBackgroundResource(R.drawable.ic_receiver_bubble);
                    holder.mainlay.setGravity(Gravity.END);
                    holder.ll_main2.setGravity(Gravity.END);
                    holder.textlay.setGravity(Gravity.END);
                } else {
                    holder.item.setTextColor(mContext.getColor(R.color.black));
                    holder.item.setLinkTextColor(mContext.getColor(R.color.black));
                    holder.dateago.setTextColor(mContext.getColor(R.color.gray));
                    holder.mainlay.setGravity(Gravity.START);
                    holder.ll_main2.setGravity(Gravity.START);
                    holder.textlay.setGravity(Gravity.START);
                    holder.ll_main2.setBackgroundResource(R.drawable.ic_sender_bubble);

                }
                holder.item.setText(messageText);
            }


            holder.doclay.setOnClickListener(view -> {
                try {
                    message.getAttachedMedia().get(0).getTemporaryContentUrl(result -> AppController.callResume(mContext, result));
                } catch (Exception ignored) {
                }

            });
            holder.img.setOnClickListener(view -> {
                try {
                    message.getAttachedMedia().get(0).getTemporaryContentUrl(result -> {
                        if (MainChatActivity.instance!=null)
                        AppController.callFullImage(mContext, result);
                    });
                } catch (Exception ignored) {
                }
            });
        }

        @Override
        public int getItemCount() {
            return quickstartConversationsManager.getMessages().size();
        }
    }


    public class CustomScrollListener extends RecyclerView.OnScrollListener {
        public CustomScrollListener() {
        }

        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
        }

        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {

            if (dy > 0) {
                setScrolledData();
            } else if (dy < 0) {
                setScrolledData();
            }
        }


    }

    void setScrolledData() {
        if (mainChatListTemp.size() > 0) {
            if (layoutManager.findLastVisibleItemPosition() != mainChatListTemp.size() - 1) {
                rl_scroll.setVisibility(View.VISIBLE);
            } else {
                rl_scroll.setVisibility(View.GONE);
                tv_newmsgcount.setVisibility(View.GONE);
                recentMsg = 0;
            }
        } else {
            rl_scroll.setVisibility(View.GONE);
            tv_newmsgcount.setVisibility(View.GONE);
            recentMsg = 0;
        }

    }

    private void setDataToView() {
        mainChatList.clear();
        int size=mainChatListTemp.size();
        mainChatList.addAll(quickstartConversationsManager.getMessages());
        ArrayList<Message> listTemp = new ArrayList<>(mainChatListTemp);
        recentMsg = recentMsg + (mainChatList.size() - mainChatListTemp.size());
        mainChatListTemp.clear();
        mainChatListTemp.addAll(mainChatList);
        if (messagesAdapter != null) {
            messagesAdapter.notifyItemInserted(size);
        }
        rl_scroll.setVisibility(View.GONE);
        recyclerView.addOnScrollListener(new CustomScrollListener());
        if (mainChatListTemp.size() > 0) {
            if (recentMsg > 0) {
                tv_newmsgcount.setText(MessageFormat.format("{0}", recentMsg > 99 ? "99+" : recentMsg));
                tv_newmsgcount.setVisibility(View.VISIBLE);
                if (listTemp.size() > 0) {
                    if (layoutManager.findLastVisibleItemPosition() == listTemp.size() - 1) {
                        if (layoutManager != null) {
                            layoutManager.scrollToPosition(mainChatListTemp.size() - 1);
                        }
                        tv_newmsgcount.setVisibility(View.GONE);
                        recentMsg = 0;
                    } else {
                        if (mainChatListTemp.size() > 0) {
                            if (layoutManager.findLastVisibleItemPosition() != mainChatListTemp.size() - 1) {
                                rl_scroll.setVisibility(View.VISIBLE);
                                tv_newmsgcount.setVisibility(View.VISIBLE);
                            } else {
                                rl_scroll.setVisibility(View.GONE);
                                tv_newmsgcount.setVisibility(View.GONE);
                                recentMsg = 0;
                            }
                        } else {
                            rl_scroll.setVisibility(View.GONE);
                            tv_newmsgcount.setVisibility(View.GONE);
                            recentMsg = 0;
                        }
                    }

                } else {
                    if (layoutManager != null) {
                        layoutManager.scrollToPosition(mainChatListTemp.size() - 1);
                    }
                    tv_newmsgcount.setVisibility(View.GONE);
                    recentMsg = 0;
                }
            }
        }
    }


    private void callProfile() {
        if (Config.isSeeker()) {
            startActivity(new Intent(mContext, CompanyProfile.class).putExtra("company_id", user_id));
        } else {
            mContext.startActivity(new Intent(mContext, UserDetail.class).putExtra("from", MainChatActivity.class.getSimpleName()).
                    putExtra("id", user_id).putExtra("job_id", job_id).putExtra("name", name));
        }

    }

    @Override
    public void onBackPressed() {
        backPressed();
        super.onBackPressed();
    }

    public void backPressed() {
        if (Config.isSeeker()) {
            startActivity(new Intent(mContext, SeekerHomeActivity.class).putExtra("from", "chat"));
        } else
            startActivity(new Intent(mContext, CompanyHomeActivity.class).putExtra("from", "chat"));
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IMG_RESULT) {
            Uri imageUri = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = mContext.getContentResolver().query(imageUri, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            File tempfile = new File(cursor.getString(columnIndex));
            cursor.close();

            if (tempfile.exists()) {
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                Bitmap bitmap = BitmapFactory.decodeFile(tempfile.getAbsolutePath(), bmOptions);

                File pictureFile = null;
                try {
                    pictureFile = createImageFile(tempfile.getName());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                   /* FileOutputStream fos = new FileOutputStream(pictureFile);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 40, fos);
                    fos.close();*/

                    if (pictureFile != null && pictureFile.exists()) {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(pictureFile.getName().contains(".png") ? Bitmap.CompressFormat.PNG : Bitmap.CompressFormat.JPEG, 70, baos);
                        InputStream is = new ByteArrayInputStream(baos.toByteArray());
                        baos.close();
                        callSendImage(pictureFile.getName(), is, bitmap);
                    }
                } catch (FileNotFoundException ignored) {
                } catch (IOException e) {
                }
            }


        } else if (resultCode == RESULT_OK && requestCode == DOCUMENT_FILE_REQUEST) {
            if (data == null) {

                return;
            }
            Uri selectedFileUri = data.getData();
            String docName = "file_" + Calendar.getInstance().getTimeInMillis() + ".pdf";
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainChatActivity.this);

            // set title
            alertDialogBuilder.setTitle("");

            // set dialog message
            alertDialogBuilder
                    .setMessage("Send \"" + docName + "\" to " + "\"" + name + "\"?")
                    .setCancelable(false)
                    .setPositiveButton("OK", (dialog, id) -> {

                        try {
                            InputStream inputStream = mContext.getContentResolver().openInputStream(selectedFileUri);
                            quickstartConversationsManager.sendMedia(inputStream, "application/pdf", docName, new MediaUploadListener() {
                                @Override
                                public void onStarted() {

                                }

                                @Override
                                public void onProgress(long l) {

                                }

                                @Override
                                public void onCompleted(@NonNull String s) {

                                }

                                @Override
                                public void onFailed(@NonNull ErrorInfo errorInfo) {

                                }
                            });
                            if (layoutManager != null) {
                                layoutManager.scrollToPosition(mainChatListTemp.size() - 1);
                            }
                            rl_scroll.setVisibility(View.GONE);
                            tv_newmsgcount.setVisibility(View.GONE);
                            recentMsg = 0;
                            et_msg.setText("");
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                    })
                    .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();


            //
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            File pictureFile = null;
            try {
                pictureFile = createImageFile("");
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
               /* FileOutputStream fos = new FileOutputStream(pictureFile);
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 70, fos);
                fos.close();*/

                if (pictureFile != null) {
                    if (pictureFile.exists()) {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        imageBitmap.compress(pictureFile.getName().contains(".png") ? Bitmap.CompressFormat.PNG : Bitmap.CompressFormat.JPEG, 99, baos);
                        InputStream is = new ByteArrayInputStream(baos.toByteArray());
                        baos.close();
                        callSendImage(pictureFile.getName(), is, imageBitmap);
                    }

                }
            } catch (FileNotFoundException e) {
            } catch (IOException e) {
            }
        }

    }


    private File createImageFile(String name) throws IOException {
        // Create an image file name
        String imageFileName = "img_" + Calendar.getInstance().getTimeInMillis();
        File storageDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                name.contains(".png") ? ".png" : ".jpeg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        return image;
    }


    public void callSendImage(String name, InputStream inputStream, Bitmap bitmap) {
        Dialog mDIalog = new Dialog(mContext, android.R.style.Theme_Translucent_NoTitleBar);
        mDIalog.setContentView(R.layout.imagepreview);

        Window window = mDIalog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        mDIalog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ImageView mImage = mDIalog.findViewById(R.id.imageView3);
        ImageView mImageClose = mDIalog.findViewById(R.id.closeee);
        ImageView iv_send = mDIalog.findViewById(R.id.iv_send);
        iv_send.setVisibility(View.VISIBLE);
        mImageClose.setOnClickListener(view1 -> mDIalog.dismiss());
        iv_send.setOnClickListener(view1 -> {
            mDIalog.dismiss();
            quickstartConversationsManager.sendMedia(inputStream, "image", name, new MediaUploadListener() {
                @Override
                public void onStarted() {

                }

                @Override
                public void onProgress(long l) {

                }

                @Override
                public void onFailed(@NonNull ErrorInfo errorInfo) {

                }

                @Override
                public void onCompleted(@NonNull String s) {

                }
            });
            if (layoutManager != null) {
                layoutManager.scrollToPosition(mainChatListTemp.size() - 1);
            }
            rl_scroll.setVisibility(View.GONE);
            tv_newmsgcount.setVisibility(View.GONE);
            recentMsg = 0;
            et_msg.setText("");
        });

        mImage.setImageBitmap(bitmap);

        try {
            mDIalog.show();
        } catch (Exception ignored) {
        }

    }

    @Override
    protected void onDestroy() {
        instance=null;
        super.onDestroy();
    }
}