package com.swipee.in.activity;


import static com.swipee.in.rest.ParaName.KEY_APPOINTMENTID;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.swipee.in.R;
import com.swipee.in.UrlManager.AppController;
import com.swipee.in.UrlManager.Config;
import com.swipee.in.UrlManager.FileUtils;
import com.swipee.in.activity.Seeker.SeekerHomeActivity;
import com.swipee.in.activity.company.CompanyHomeActivity;
import com.swipee.in.activity.company.UserDetail;
import com.swipee.in.adapter.ChatMessageAdapter;
import com.swipee.in.model.ChatModel;
import com.swipee.in.rest.ApiUrls;
import com.swipee.in.rest.ParaName;
import com.swipee.in.rest.Rest;
import com.swipee.in.rest.SwipeeApiClient;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_ASK_PERMISSION_CAMERA = 157;
    private static final int REQUEST_CODE_ASK_PERMISSIONS_DOC = 131;
    private static final int REQUEST_CODE_ASK_PERMISSIONS_GALLERY = 111;
    private static final int REQUEST_IMAGE_CAPTURE = 212;
    private static final int IMG_RESULT = 111;
    private static final int DOCUMENT_FILE_REQUEST = 214;
    private RelativeLayout rl_scroll;
    private BottomSheetBehavior bottomsheet_intent;
    @SuppressLint("StaticFieldLeak")
    public static ChatActivity instance;
    ImageView iv_back;
    CircleImageView iv_profile;
    ImageView iv_camera, iv_send, iv_doc, iv_scroll,iv_refresh,iv_online_offline;
    TextView tv_name, tv_action, tv_newmsgcount,tv_typing;
    Context mContext;
    Rest rest;
    private final ArrayList<ChatModel.Data> mainChatList = new ArrayList<>();
    private final ArrayList<ChatModel.Data> mainChatListTemp = new ArrayList<>();
    private final ArrayList<ChatModel.Data> tempListForSeen = new ArrayList<>();
    ChatMessageAdapter chatMessageAdapter;
    RecyclerView rv_chat;
    private String msg_id = "";
    private String appointment_id = "";
    private LinearLayoutManager linearLayoutManager;
    private String user_id = "";
    private String name = "";
    private String job_id = "";
    private String apply_id = "";
    private String receiver = "";
    private String sender = "";
    private String senderRole = "";
    private String receiverRole = "";
    private EditText et_msg;
    private int recentMsg = 0;
    public String appointment_number="";
    public boolean isPause=false;
    public boolean isScroll=false;
    private Handler handler;
    private Runnable runnable;
    private boolean isTyping=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getColor(R.color.white));

        mContext = this;
        instance = this;
        isPause=false;
        isScroll=false;
        rest = new Rest(mContext);
        tv_action = findViewById(R.id.tv_action);
        tv_name = findViewById(R.id.tv_name);
        iv_profile = findViewById(R.id.iv_profile);
        iv_back = findViewById(R.id.iv_back);
        rv_chat = findViewById(R.id.rv_chat);
        et_msg = findViewById(R.id.et_msg);
        iv_camera = findViewById(R.id.iv_camera);
        iv_send = findViewById(R.id.iv_send);
        iv_doc = findViewById(R.id.iv_doc);
        iv_scroll = findViewById(R.id.iv_scroll);
        rl_scroll = findViewById(R.id.rl_scroll);
        tv_newmsgcount = findViewById(R.id.tv_newmsgcount);
        iv_refresh = findViewById(R.id.iv_refresh);
        tv_typing = findViewById(R.id.tv_typing);
        iv_online_offline = findViewById(R.id.iv_online_offline);
        iv_refresh.setVisibility(View.GONE);
        RelativeLayout rl_main=findViewById(R.id.rl_main);


        chatMessageAdapter = new ChatMessageAdapter(mContext, mainChatListTemp);
        linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        rv_chat.setLayoutManager(linearLayoutManager);
        rv_chat.setAdapter(chatMessageAdapter);
        if (getIntent() != null) {
            String image = getIntent().getStringExtra("image");
            String action = getIntent().getStringExtra("action");
            name = getIntent().getStringExtra("name");
            msg_id = getIntent().getStringExtra("msg_id");
            appointment_id = getIntent().getStringExtra("appointment_id");
            user_id = getIntent().getStringExtra("user_id");
            job_id = getIntent().getStringExtra("job_id");
            apply_id = getIntent().hasExtra("apply_id")?getIntent().getStringExtra("apply_id"):"";
            appointment_number = getIntent().getStringExtra("appointment_number");

            receiver = user_id;
            sender = Config.GetId();
            senderRole = Config.isSeeker() ? "3" : "2";
            receiverRole = Config.isSeeker() ? "2" : "3";

            Glide.with(mContext).load(image).error(R.drawable.ic_profile_select)
                    .placeholder(R.drawable.ic_profile_select)
                    .into(iv_profile);
            tv_name.setText(name);
            if (!Config.isSeeker()) {
                tv_action.setText(action);
                tv_action.setVisibility(View.GONE);
            } else {
                tv_action.setVisibility(View.GONE);
            }
            if (rest.isInterentAvaliable()) {
                AppController.ShowDialogue("", mContext);
                getChat(appointment_id, "0");
                HashMap<String,String> hashMap=new HashMap<>();
                hashMap.put(ParaName.KEY_UID,receiver);
                hashMap.put(ParaName.KEY_ROLEID,receiverRole);
                sendOnlineOffline(hashMap);
            }
        }


        iv_back.setOnClickListener(v -> backPressed());

        iv_profile.setOnClickListener(v -> callProfile());
        tv_name.setOnClickListener(v -> callProfile());

        iv_doc.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                    PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                    PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_ASK_PERMISSIONS_DOC);
            } else callFile();
        });
        iv_camera.setOnClickListener(v -> bottomsheet_intent.setState(BottomSheetBehavior.STATE_EXPANDED));

        iv_send.setOnClickListener(v -> {

            if (!TextUtils.isEmpty(et_msg.getText().toString()) && !TextUtils.isEmpty(et_msg.getText().toString().replaceAll(" ", "")))
                sendMsg("text", null, "", "https://swipee.in/files/chat/");
        });

        industryBottomSheet();
        iv_scroll.setOnClickListener(v -> {
            if (linearLayoutManager != null) {
                linearLayoutManager.scrollToPosition(mainChatListTemp.size() - 1);
            }
            recentMsg = 0;
            tv_newmsgcount.setText("");
            rl_scroll.setVisibility(View.GONE);
            tv_newmsgcount.setVisibility(View.GONE);
            setSeenAfterScroll();
        });

        rl_main.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = rl_main.getRootView().getHeight() - rl_main.getHeight();
                if (heightDiff > dpToPx(ChatActivity.this, 200)) { // if more than 200 dp, it's probably a keyboard...
                }else {
                    if (isTyping){
                        if (handler!=null){
                            handler.removeCallbacks(runnable);
                            handler=null;
                            runnable=null;
                        }
                        handler=new Handler();
                        runnable= () -> {
                          if (isTyping){
                              isTyping=false;
                              HashMap<String,String> hashMap=new HashMap<>();
                              hashMap.put(ParaName.KEYTOKEN,Config.GetUserToken());
                              hashMap.put(ParaName.KEY_APPOINTMENTID,appointment_id);
                              hashMap.put(ParaName.KEY_ISTYPING,"N");
                              sendIsTyping(hashMap);
                          }
                        };
                        handler.postDelayed(runnable,100);
                    }
                }
            }
        });
        et_msg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!isTyping){
                    isTyping=true;
                    HashMap<String,String> hashMap=new HashMap<>();
                    hashMap.put(ParaName.KEYTOKEN,Config.GetUserToken());
                    hashMap.put(ParaName.KEY_APPOINTMENTID,appointment_id);
                    hashMap.put(ParaName.KEY_ISTYPING,"Y");
                    sendIsTyping(hashMap);
                }
                if (handler!=null){
                    handler.removeCallbacks(runnable);
                    handler=null;
                    runnable=null;
                }
                 handler=new Handler();
                 runnable= () -> {
                     isTyping=false;
                     HashMap<String,String> hashMap=new HashMap<>();
                     hashMap.put(ParaName.KEYTOKEN,Config.GetUserToken());
                     hashMap.put(ParaName.KEY_APPOINTMENTID,appointment_id);
                     hashMap.put(ParaName.KEY_ISTYPING,"N");
                     sendIsTyping(hashMap);
                 };
                handler.postDelayed(runnable,4000);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    public static float dpToPx(Context context, float valueInDp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
    }

    private void sendMsg(String msg_type, MultipartBody.Part file, String name, String path) {
        String msgId=mainChatListTemp.size()>0?mainChatListTemp.get(mainChatListTemp.size()-1).getMsg_id():"0";
        String MULTIPART_FORM_DATA = "multipart/form-data";
        HashMap<String, RequestBody> map = new HashMap<>();
        map.put(KEY_APPOINTMENTID, RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), appointment_id));
        map.put(ParaName.KEY_SENDERID, RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), sender));
        map.put(ParaName.KEY_RECEIVERID1, RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), receiver));
        map.put(ParaName.KEY_SENDERROLEID, RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), senderRole));
        map.put(ParaName.KEY_RECVRROLEID, RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), receiverRole));
        map.put(ParaName.KEY_MSGTYPE, RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), msg_type));
        map.put(ParaName.KEY_MSGID, RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), msgId));
        map.put(ParaName.KEY_MSGCONTENT, RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), et_msg.getText().toString().trim()));
        sendMessage(map, file);

        if (linearLayoutManager != null) {
            linearLayoutManager.scrollToPosition(mainChatListTemp.size() - 1);
        }
        rl_scroll.setVisibility(View.GONE);
        tv_newmsgcount.setVisibility(View.GONE);
        et_msg.setText("");
        recentMsg = 0;
    }


    private void callProfile() {
        if (Config.isSeeker())
            startActivity(new Intent(mContext, CompanyProfile.class).putExtra("company_id", user_id));
        else
            mContext.startActivity(new Intent(mContext, UserDetail.class).putExtra("from", ChatActivity.class.getSimpleName()).
                    putExtra("apply_id", apply_id).putExtra("id", user_id).putExtra("job_id", job_id).putExtra("name", name));

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
    protected void onResume() {
        isPause=false;
        if (tempListForSeen.size() > 0) {
            String receiver_id = "";
            ArrayList<String>  ids=new ArrayList<>();
            for (int i = tempListForSeen.size() - 1; i >= 0; i--) {
                if (Config.GetId().equalsIgnoreCase(tempListForSeen.get(i).getMsg_receiver_id())) {
                    ChatModel.Data chatData = tempListForSeen.get(i);
                    if (!chatData.getIs_seen().equalsIgnoreCase("1")){
                        ids.add(ids.size(),chatData.getMsg_id());
                        receiver_id=chatData.getMsg_receiver_id();
                    }
                }
            }

            ////
            if (!isPause && !isScroll){
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(ParaName.KEY_MSGID, ids.toString());
                hashMap.put(ParaName.KEY_RECEIVERID, receiver_id);
                setMsgSeen(hashMap, ids);
            }

            //
        }
        super.onResume();

    }

    private void getChat(String appointmentId, String msgId) {
        iv_refresh.startAnimation(AnimationUtils.loadAnimation(mContext,R.anim.rotate));
        SwipeeApiClient.swipeeServiceInstance().getChat(Config.GetUserToken(), appointmentId, msgId).enqueue(new Callback<ChatModel>() {

            @Override
            public void onResponse(@NonNull Call<ChatModel> call, @NonNull Response<ChatModel> response) {
                iv_refresh.clearAnimation();
                iv_refresh.setVisibility(View.GONE);
                AppController.dismissProgressdialog();
                if (response.code() == 200 && response.body() != null) {
                    ChatModel responceBody = response.body();
                    if (responceBody.getCode() == 203) {
                        rest.showToast(responceBody.getMessage());
                        AppController.loggedOut(mContext);
                        finish();
                    } else if (responceBody.getCode() == 200 && responceBody.isStatus()) {
                       setDataTolist(responceBody.getData(),msgId);

                    }

                } else rest.showToast("Something went wrong");

            }

            @Override
            public void onFailure(@NonNull Call<ChatModel> call, @NonNull Throwable t) {
                AppController.dismissProgressdialog();
                iv_refresh.clearAnimation();
                iv_refresh.setVisibility(View.GONE);
            }
        });
    }

    private void setDataTolist(ArrayList<ChatModel.Data> data, String msgId) {
        if (mainChatList.size()>0){
            ArrayList<ChatModel.Data> temp=new ArrayList<>();
            for (int i = 0; i < data.size(); i++) {
                boolean isGarbage=false;
                for (int j=0;j<mainChatList.size();j++){
                    if (mainChatList.get(j).getMsg_id().equalsIgnoreCase(data.get(i).getMsg_id())){
                        isGarbage=true;
                        break;
                    }
                }
                if (!isGarbage)
                    temp.add(temp.size(),data.get(i));
            }
            mainChatList.addAll(mainChatList.size(), temp);
        }else {
            mainChatList.addAll(mainChatList.size(),data);
        }
        Collections.sort(mainChatList, new SortByMsgId());
        ArrayList<ChatModel.Data> listTemp = new ArrayList<>(mainChatListTemp);
        recentMsg = recentMsg + (mainChatList.size() - mainChatListTemp.size());
        mainChatListTemp.clear();
        mainChatListTemp.addAll(mainChatList);
        if (mainChatListTemp.size()>0){
            if (mainChatListTemp.size()>1){
                mainChatListTemp.get(0).setShowDate(true);
                for (int k=1;k<mainChatListTemp.size();k++){
                    String date=mainChatListTemp.get(k-1).getMsg_created_at();
                    String dateOld=mainChatListTemp.get(k).getMsg_created_at();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    SimpleDateFormat formatdate = new SimpleDateFormat("yyyy-MM-dd");
                    String date1="";
                    String datetoshow="";
                    try {
                        Date   dateFinal = format.parse(date);
                        Date dateFinalOld = format.parse(dateOld);
                        date1 = formatdate.format(dateFinal);
                        datetoshow = formatdate.format(dateFinalOld);
                        if (!date1.equalsIgnoreCase(datetoshow)){
                            mainChatListTemp.get(k).setShowDate(true);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }else mainChatListTemp.get(0).setShowDate(true);
        }
        if (chatMessageAdapter != null)
            chatMessageAdapter.notifyDataSetChanged();

        rl_scroll.setVisibility(View.GONE);
        if (msgId.equalsIgnoreCase("0") && mainChatListTemp.size() > 0) {
            rv_chat.addOnScrollListener(new CustomScrollListener());
            if (linearLayoutManager != null) {
                linearLayoutManager.scrollToPosition(mainChatListTemp.size() - 1);
            }
            recentMsg = 0;
            tv_newmsgcount.setVisibility(View.GONE);
        } else if (msgId.equalsIgnoreCase("0")) {
            rv_chat.addOnScrollListener(new CustomScrollListener());
            tv_newmsgcount.setVisibility(View.GONE);
            recentMsg = 0;
        } else if (mainChatListTemp.size() > 0) {
            if (recentMsg > 0) {
                tv_newmsgcount.setText(MessageFormat.format("{0}", recentMsg>99?"99+":recentMsg));
                tv_newmsgcount.setVisibility(View.VISIBLE);
                if (listTemp.size() > 0) {
                    if (linearLayoutManager.findLastVisibleItemPosition() == listTemp.size() - 1) {
                        if (linearLayoutManager != null)
                            linearLayoutManager.scrollToPosition(mainChatListTemp.size() - 1);
                        tv_newmsgcount.setVisibility(View.GONE);
                        recentMsg = 0;
                    } else {
                        if (mainChatListTemp.size() > 0) {
                            if (linearLayoutManager.findLastVisibleItemPosition() != mainChatListTemp.size() - 1) {
                                rl_scroll.setVisibility(View.VISIBLE);
                                tv_newmsgcount.setVisibility(View.VISIBLE);
                                isScroll=true;
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
                    if (linearLayoutManager != null)
                        linearLayoutManager.scrollToPosition(mainChatListTemp.size() - 1);
                    tv_newmsgcount.setVisibility(View.GONE);
                    recentMsg = 0;
                }
            }
        }
        if (mainChatListTemp.size() > 0) {
            String receiver_id = "";
            ArrayList<String>  ids=new ArrayList<>();
            for (int i = mainChatListTemp.size() - 1; i >= 0; i--) {
                if (Config.GetId().equalsIgnoreCase(mainChatListTemp.get(i).getMsg_receiver_id())) {
                    ChatModel.Data chatData = mainChatListTemp.get(i);
                    if (!chatData.getIs_seen().equalsIgnoreCase("1")){
                        ids.add(ids.size(),chatData.getMsg_id());
                        receiver_id=chatData.getMsg_receiver_id();
                    }
                }
            }

            ////
            if (!isPause && !isScroll){
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(ParaName.KEY_MSGID, ids.toString());
                hashMap.put(ParaName.KEY_RECEIVERID, receiver_id);
                setMsgSeen(hashMap, ids);
            }

            //
        }
    }

    private void setMsgSeen(HashMap<String, String> hashMap, ArrayList<String> ids) {
        SwipeeApiClient.swipeeServiceInstance().setMsgSeen(hashMap).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.code() == 200 && response.body() != null) {
                    if (response.body().get("code").getAsInt() == 203) {
                        rest.showToast(response.body().get("message").getAsString());
                        AppController.loggedOut(mContext);
                        finish();
                    }else if (response.body().get("code").getAsInt() == 200){
                        try {
                            ArrayList<ChatModel.Data> arrayList=new ArrayList<>();
                            arrayList.addAll(tempListForSeen);
                            int size=arrayList.size();
                            for (int i = 0; i < size; i++) {
                                if (ids.contains(arrayList.get(i).getMsg_id())) arrayList.remove(i);
                            }
                            tempListForSeen.clear();
                            tempListForSeen.addAll(arrayList);
                        }catch (Exception e){}

                    }
                }

            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                Log.d("ssssssss", "" + t.getLocalizedMessage());
            }
        });
    }


    private void sendMessage(HashMap<String, RequestBody> map, MultipartBody.Part file) {

        SwipeeApiClient.swipeeServiceInstance().sendChat(map, file).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.code() == 200 && response.body() != null) {
                    if (response.body().get("code").getAsInt() == 203) {
                        rest.showToast(response.body().get("message").getAsString());
                        AppController.loggedOut(mContext);
                        finish();
                    }else  if (response.body().get("code").getAsInt() == 200) {
                        JsonArray data=response.body().has("data")?response.body().get("data").isJsonArray()
                                ?response.body().getAsJsonArray("data"):new JsonArray():new JsonArray();
                        if (data.size()>0){
                            ArrayList<ChatModel.Data> chatData=new ArrayList<>();
                            for (int i=0;i<data.size();i++){
                                JsonObject dataObj=data.get(i).getAsJsonObject();
                                String msg_id=dataObj.has("msg_id")?dataObj.get("msg_id").getAsString():"";
                                String appointment_id=dataObj.has("appointment_id")?dataObj.get("appointment_id").getAsString():"";
                                String msg_sender_id=dataObj.has("msg_sender_id")?dataObj.get("msg_sender_id").getAsString():"";
                                String msg_receiver_id=dataObj.has("msg_receiver_id")?dataObj.get("msg_receiver_id").getAsString():"";
                                String msg_content=dataObj.has("msg_content")?dataObj.get("msg_content").getAsString():"";
                                String msg_filename=dataObj.has("msg_filename")?dataObj.get("msg_filename").getAsString():"";
                                String msg_filename_org=dataObj.has("msg_filename_org")?dataObj.get("msg_filename_org").getAsString():"";
                                String msg_filesize=dataObj.has("msg_filesize")?dataObj.get("msg_filesize").getAsString():"0";
                                String msg_type=dataObj.has("msg_type")?dataObj.get("msg_type").getAsString():"";
                                String is_seen=dataObj.has("is_seen")?dataObj.get("is_seen").getAsString():"";
                                String created_at=dataObj.has("created_at")?dataObj.get("created_at").getAsString():"";
                                String time=dataObj.has("time")?dataObj.get("time").getAsString():"";
                                String msg_created_at=dataObj.has("msg_created_at")?dataObj.get("msg_created_at").getAsString():"";
                                ChatModel.Data chatDataModel=new ChatModel.Data(msg_id,appointment_id,msg_sender_id,msg_receiver_id,msg_content
                                        ,msg_filename,msg_filename_org,Integer.parseInt(msg_filesize),msg_type,is_seen,created_at,time,msg_created_at,false);
                                   chatData.add(chatData.size(),chatDataModel);
                            }
                            if (mainChatList.size()>0){
                                ArrayList<ChatModel.Data> temp=new ArrayList<>();
                                for (int i = 0; i < chatData.size(); i++) {
                                    boolean isGarbage=false;
                                    for (int j=0;j<mainChatList.size();j++){
                                        if (mainChatList.get(j).getMsg_id().equalsIgnoreCase(chatData.get(i).getMsg_id())){
                                            isGarbage=true;
                                            break;
                                        }
                                    }
                                    if (!isGarbage)
                                        temp.add(temp.size(),chatData.get(i));
                                }
                                mainChatList.addAll(mainChatList.size(), temp);
                                mainChatListTemp.addAll(mainChatListTemp.size(), temp);
                            }else {
                                mainChatList.addAll(mainChatList.size(), chatData);
                                mainChatListTemp.addAll(mainChatListTemp.size(), chatData);
                            }
                            Collections.sort(mainChatList, new SortByMsgId());
                            Collections.sort(mainChatListTemp, new SortByMsgId());
                          //  mainChatListTemp.sort(new SortByMsgId());
                            if (mainChatListTemp.size()>0){
                                if (mainChatListTemp.size()>1){
                                    mainChatListTemp.get(0).setShowDate(true);
                                    for (int k=1;k<mainChatListTemp.size();k++){
                                        String date=mainChatListTemp.get(k-1).getMsg_created_at();
                                        String dateOld=mainChatListTemp.get(k).getMsg_created_at();
                                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                        SimpleDateFormat formatdate = new SimpleDateFormat("yyyy-MM-dd");
                                        String date1="";
                                        String datetoshow="";
                                        try {
                                            Date   dateFinal = format.parse(date);
                                            Date dateFinalOld = format.parse(dateOld);
                                            date1 = formatdate.format(dateFinal);
                                            datetoshow = formatdate.format(dateFinalOld);
                                            if (!date1.equalsIgnoreCase(datetoshow)){
                                                mainChatListTemp.get(k).setShowDate(true);
                                            }
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }else mainChatListTemp.get(0).setShowDate(true);
                            }
                            if (chatMessageAdapter != null) {
                                chatMessageAdapter.notifyDataSetChanged();
                            }
                            if (linearLayoutManager != null) {
                                linearLayoutManager.scrollToPosition(mainChatListTemp.size() - 1);
                            }
                            rl_scroll.setVisibility(View.GONE);
                            tv_newmsgcount.setVisibility(View.GONE);
                            et_msg.setText("");
                            recentMsg = 0;
                        }


                    }
                }

            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                Log.d("ssssssss", "" + t.getLocalizedMessage());
            }
        });
    }

    private void callFile() {
        /*String[] supportedMimeTypes = {"application/pdf"};
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType(supportedMimeTypes[0]);
        intent.putExtra(Intent.EXTRA_MIME_TYPES, supportedMimeTypes);
        startActivityForResult(intent, DOCUMENT_FILE_REQUEST);*/


        String[] supportedMimeTypes = {"application/pdf", "image/jpeg", "image/jpg",  "text/plain", "application/msword",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "image/bmp"};
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_MIME_TYPES, supportedMimeTypes);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    DOCUMENT_FILE_REQUEST);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(mContext, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
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
                    pictureFile = createImageFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {

                    if (pictureFile != null) {
                        if (pictureFile.exists()) {
                            FileOutputStream fos = new FileOutputStream(pictureFile);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);
                            fos.close();
                            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), pictureFile);
                            MultipartBody.Part body1 = MultipartBody.Part.createFormData(ParaName.KEY_MSGFILE, pictureFile.getName(), requestFile);
                            callSendImage(body1, pictureFile.getName(), pictureFile.getAbsolutePath(),bitmap);
                        }
                    }
                } catch (FileNotFoundException e) {
                } catch (IOException e) {}
            }


        } else if (resultCode == RESULT_OK && requestCode == DOCUMENT_FILE_REQUEST) {
            if (data == null) {

                return;
            }
            Uri selectedFileUri = data.getData();
            String filePath = null;
            String docName="";
            try {
                filePath = FileUtils.getPath(mContext, selectedFileUri);
                if (filePath != null) {
                    File file = new File(filePath);
                     docName = file.getName();

                }
            } catch (Exception e) {
                try {
                    String mimeType = mContext.getContentResolver().getType(selectedFileUri);
                    if (mimeType.equalsIgnoreCase("application/pdf")) {
                        String extension = ".pdf";
                        String name = "File_" + Calendar.getInstance().getTimeInMillis();
                        docName = name + extension;
                    } else if (mimeType.equalsIgnoreCase("application/msword")) {
                        String extension = ".doc";
                        String name = "File_" + Calendar.getInstance().getTimeInMillis();
                        docName = name + extension;
                    } else if (mimeType.equalsIgnoreCase("application/vnd.openxmlformats-officedocument.wordprocessingml.document")) {
                        String extension = ".docx";
                        String name = "File_" + Calendar.getInstance().getTimeInMillis();
                        docName = name + extension;
                    }
                } catch (Exception e1) {}
            }


            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ChatActivity.this);
            // set title
            alertDialogBuilder.setTitle("");
            // set dialog message
            String finalDocName = docName;
            alertDialogBuilder
                    .setMessage("Send \"" + docName + "\" to " + "\"" + name + "\"?")
                    .setCancelable(false)
                    .setPositiveButton("OK", (dialog, id) -> {

                        new UploadFile(selectedFileUri, mContext, finalDocName).execute();

                        if (linearLayoutManager != null)
                            linearLayoutManager.scrollToPosition(mainChatListTemp.size() - 1);
                        rl_scroll.setVisibility(View.GONE);
                        tv_newmsgcount.setVisibility(View.GONE);
                        recentMsg = 0;
                        et_msg.setText("");

                    })
                    .setNegativeButton("CANCEL", (dialog, id) -> dialog.cancel());

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();


            //
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            File pictureFile = null;
            try {
                pictureFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (pictureFile != null) {
                    if (pictureFile != null && pictureFile.exists()) {
                        FileOutputStream fos = new FileOutputStream(pictureFile);
                        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 99, fos);
                        fos.close();
                        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), pictureFile);
                        MultipartBody.Part body1 = MultipartBody.Part.createFormData(ParaName.KEY_MSGFILE, pictureFile.getName(), requestFile);
                        callSendImage(body1, pictureFile.getName(), pictureFile.getAbsolutePath(),imageBitmap);
                    }
                }
            } catch (FileNotFoundException e) {
            } catch (IOException e) {}
        }

    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String imageFileName = "img_" + Calendar.getInstance().getTimeInMillis();
        File storageDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpeg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        return image;
    }

    private void industryBottomSheet() {
        RelativeLayout intent_sheet = findViewById(R.id.intent_sheet);
        bottomsheet_intent = BottomSheetBehavior.from(intent_sheet);
        TextView tv_camera = intent_sheet.findViewById(R.id.tv_camera);
        TextView tv_gallery = intent_sheet.findViewById(R.id.tv_gallery);
        TextView tv_cancel = intent_sheet.findViewById(R.id.tv_cancel);
        intent_sheet.setOnClickListener(v1->bottomsheet_intent.setState(BottomSheetBehavior.STATE_COLLAPSED));
        tv_camera.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) !=
                    PackageManager.PERMISSION_GRANTED||ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                    PackageManager.PERMISSION_GRANTED||ActivityCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                    PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                                Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_ASK_PERMISSION_CAMERA);
            } else{
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                } catch (ActivityNotFoundException e) {
                    // display error state to the user
                }
            }
            bottomsheet_intent.setState(BottomSheetBehavior.STATE_COLLAPSED);
        });
        tv_gallery.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                    PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                    PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_ASK_PERMISSIONS_GALLERY);
            } else{
                Intent intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, IMG_RESULT);
            }
            bottomsheet_intent.setState(BottomSheetBehavior.STATE_COLLAPSED);
        });
        tv_cancel.setOnClickListener(v -> bottomsheet_intent.setState(BottomSheetBehavior.STATE_COLLAPSED));

    }

    public void setReadReceipt(String appointment, String message_id,boolean isSeen) {
        runOnUiThread(() -> {
            if (appointment_number.equalsIgnoreCase(appointment)){
                for (int i=0;i<mainChatListTemp.size();i++){
                    if (mainChatListTemp.get(i).getMsg_id().equalsIgnoreCase(message_id)){
                        mainChatListTemp.get(i).setIs_seen(isSeen?"1":"2");
                        chatMessageAdapter.notifyItemChanged(i);
                        break;
                    }
                }
            }
        });

    }

    public void callNewMsg(ArrayList<ChatModel.Data> chatData) {

        runOnUiThread(() -> {
            if (isPause ||isScroll)
            tempListForSeen.addAll(chatData);
            setDataTolist(chatData,chatData.get(0).getMsg_id());
        });
    }

    @Override
    protected void onPause() {
        isPause=true;
        super.onPause();
    }

    public void setTyping(String appointment, String typing_status) {
        runOnUiThread(() -> {
            if (appointment_number.equalsIgnoreCase(appointment))
                tv_typing.setVisibility(typing_status.equalsIgnoreCase("Y") ? View.VISIBLE : View.INVISIBLE);
        });
    }

    @SuppressLint("StaticFieldLeak")
    public static class UploadFile extends AsyncTask<Object, String, String> {
        String file_name = "";
        Uri uri;
        @SuppressLint("StaticFieldLeak")
        Context context;

        public UploadFile(Uri uri, Context context, String file_name) {
            this.uri = uri;
            this.context = context;
            this.file_name = file_name;
        }

        @Override
        protected String doInBackground(Object[] parms) {
            String result = null;
            try {

                String lineEnd = "\r\n";
                String twoHyphens = "--";
                String boundary = "*****";
                int bytesRead, bytesAvailable, bufferSize;
                byte[] buffer;
                int maxBufferSize = 1024 * 1024;
                //todo change URL as per client ( MOST IMPORTANT )
                URL url = new URL(ApiUrls.BASE_URL + ApiUrls.URL_SENDMSG);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                // Allow Inputs &amp; Outputs.
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setUseCaches(false);


                // Set HTTP method to POST.
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                FileInputStream fileInputStream;
                DataOutputStream outputStream;
                outputStream = new DataOutputStream(connection.getOutputStream());

                outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                outputStream.writeBytes("Content-Disposition: form-data; name=\"" + ParaName.KEY_APPOINTMENTID + "\"" + lineEnd);
                outputStream.writeBytes(lineEnd);
                outputStream.writeBytes(instance.appointment_id);
                outputStream.writeBytes(lineEnd);

                outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                outputStream.writeBytes("Content-Disposition: form-data; name=\"" + ParaName.KEY_SENDERID + "\"" + lineEnd);
                outputStream.writeBytes(lineEnd);
                outputStream.writeBytes(instance.sender);
                outputStream.writeBytes(lineEnd);

                outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                outputStream.writeBytes("Content-Disposition: form-data; name=\"" + ParaName.KEY_RECEIVERID1 + "\"" + lineEnd);
                outputStream.writeBytes(lineEnd);
                outputStream.writeBytes(instance.receiver);
                outputStream.writeBytes(lineEnd);

                outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                outputStream.writeBytes("Content-Disposition: form-data; name=\"" + ParaName.KEY_SENDERROLEID + "\"" + lineEnd);
                outputStream.writeBytes(lineEnd);
                outputStream.writeBytes(instance.senderRole);
                outputStream.writeBytes(lineEnd);

                outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                outputStream.writeBytes("Content-Disposition: form-data; name=\"" + ParaName.KEY_RECVRROLEID + "\"" + lineEnd);
                outputStream.writeBytes(lineEnd);
                outputStream.writeBytes(instance.receiverRole);
                outputStream.writeBytes(lineEnd);

                outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                outputStream.writeBytes("Content-Disposition: form-data; name=\"" + ParaName.KEY_MSGTYPE + "\"" + lineEnd);
                outputStream.writeBytes(lineEnd);
                outputStream.writeBytes("document");
                outputStream.writeBytes(lineEnd);

                outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                outputStream.writeBytes("Content-Disposition: form-data; name=\"" + ParaName.KEY_MSGCONTENT + "\"" + lineEnd);
                outputStream.writeBytes(lineEnd);
                outputStream.writeBytes("");
                outputStream.writeBytes(lineEnd);


                outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                outputStream.writeBytes("Content-Disposition: form-data; name=\"" + ParaName.KEY_MSGID + "\"" + lineEnd);
                outputStream.writeBytes(lineEnd);
                outputStream.writeBytes(instance.mainChatListTemp.size()>0?instance.mainChatListTemp.get(ChatActivity.instance.mainChatListTemp.size()-1).getMsg_id():"0");
                outputStream.writeBytes(lineEnd);

                outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                outputStream.writeBytes("Content-Disposition: form-data; name=\"" + ParaName.KEY_MSGFILE + "\";filename=\"" + file_name + "\"" + lineEnd);
                outputStream.writeBytes(lineEnd);

                InputStream inputStream = context.getContentResolver().openInputStream(uri);
                bytesAvailable = inputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // Read file
                bytesRead = inputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {
                    outputStream.write(buffer, 0, bufferSize);
                    bytesAvailable = inputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = inputStream.read(buffer, 0, bufferSize);
                }
                outputStream.writeBytes(lineEnd);
                outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                int serverResponseCode = connection.getResponseCode();

                if (serverResponseCode == 200) {
                    StringBuilder s_buffer = new StringBuilder();
                    InputStream is = new BufferedInputStream(connection.getInputStream());
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    String inputLine;
                    while ((inputLine = br.readLine()) != null) {
                        s_buffer.append(inputLine);
                    }
                    result = s_buffer.toString();
                }
                inputStream.close();
                outputStream.flush();
                outputStream.close();
                if (result != null) Log.d("result_for upload", result);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        protected void onPostExecute(String s) {
            try {
                JsonParser parser = new JsonParser();
                JsonObject json = (JsonObject) parser.parse(s);
                if (json != null) {
                    if (json.has("data")){
                        JsonArray data=json.has("data")?json.get("data").isJsonArray()
                                ?json.getAsJsonArray("data"):new JsonArray():new JsonArray();
                        if (data.size()>0){
                            ArrayList<ChatModel.Data> chatData=new ArrayList<>();
                            for (int i=0;i<data.size();i++){
                                JsonObject dataObj=data.get(i).getAsJsonObject();
                                String msg_id=dataObj.has("msg_id")?dataObj.get("msg_id").getAsString():"";
                                String appointment_id=dataObj.has("appointment_id")?dataObj.get("appointment_id").getAsString():"";
                                String msg_sender_id=dataObj.has("msg_sender_id")?dataObj.get("msg_sender_id").getAsString():"";
                                String msg_receiver_id=dataObj.has("msg_receiver_id")?dataObj.get("msg_receiver_id").getAsString():"";
                                String msg_content=dataObj.has("msg_content")?dataObj.get("msg_content").getAsString():"";
                                String msg_filename=dataObj.has("msg_filename")?dataObj.get("msg_filename").getAsString():"";
                                String msg_filename_org=dataObj.has("msg_filename_org")?dataObj.get("msg_filename_org").getAsString():"";
                                String msg_filesize=dataObj.has("msg_filesize")?dataObj.get("msg_filesize").getAsString():"0";
                                String msg_type=dataObj.has("msg_type")?dataObj.get("msg_type").getAsString():"";
                                String is_seen=dataObj.has("is_seen")?dataObj.get("is_seen").getAsString():"";
                                String created_at=dataObj.has("created_at")?dataObj.get("created_at").getAsString():"";
                                String time=dataObj.has("time")?dataObj.get("time").getAsString():"";
                                String msg_created_at=dataObj.has("msg_created_at")?dataObj.get("msg_created_at").getAsString():"";
                                ChatModel.Data chatDataModel=new ChatModel.Data(msg_id,appointment_id,msg_sender_id,msg_receiver_id,msg_content
                                        ,msg_filename,msg_filename_org,Integer.parseInt(msg_filesize),msg_type,is_seen,created_at,time,msg_created_at,false);
                                chatData.add(chatData.size(),chatDataModel);
                            }
                            if (instance.mainChatList.size()>0){
                                ArrayList<ChatModel.Data> temp=new ArrayList<>();
                                for (int i = 0; i < chatData.size(); i++) {
                                    boolean isGarbage=false;
                                    for (int j=0;j<instance.mainChatList.size();j++){
                                        if (instance.mainChatList.get(j).getMsg_id().equalsIgnoreCase(chatData.get(i).getMsg_id())){
                                            isGarbage=true;
                                            break;
                                        }
                                    }
                                    if (!isGarbage) temp.add(temp.size(),chatData.get(i));
                                }
                                instance.mainChatList.addAll(instance.mainChatList.size(), temp);
                                instance.mainChatListTemp.addAll(instance.mainChatListTemp.size(), temp);
                            }else {
                                instance.mainChatList.addAll(instance.mainChatList.size(), chatData);
                                instance.mainChatListTemp.addAll(instance.mainChatListTemp.size(), chatData);
                            }

                            if (instance.mainChatListTemp.size()>0){
                                if (instance.mainChatListTemp.size()>1){
                                    instance.mainChatListTemp.get(0).setShowDate(true);
                                    for (int k=1;k<instance.mainChatListTemp.size();k++){
                                        String date=instance.mainChatListTemp.get(k-1).getMsg_created_at();
                                        String dateOld=instance.mainChatListTemp.get(k).getMsg_created_at();
                                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                        SimpleDateFormat formatdate = new SimpleDateFormat("yyyy-MM-dd");
                                        String date1="";
                                        String datetoshow="";
                                        try {
                                            Date   dateFinal = format.parse(date);
                                            Date dateFinalOld = format.parse(dateOld);
                                            date1 = formatdate.format(dateFinal);
                                            datetoshow = formatdate.format(dateFinalOld);
                                            if (!date1.equalsIgnoreCase(datetoshow)){
                                                instance.mainChatListTemp.get(k).setShowDate(true);
                                            }
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }else instance.mainChatListTemp.get(0).setShowDate(true);
                            }
                            if (instance.chatMessageAdapter != null) {
                                instance.chatMessageAdapter.notifyDataSetChanged();
                            }
                            if (instance.linearLayoutManager != null) {
                                instance.linearLayoutManager.scrollToPosition(instance.mainChatListTemp.size() - 1);
                            }
                            instance.rl_scroll.setVisibility(View.GONE);
                            instance.tv_newmsgcount.setVisibility(View.GONE);
                            instance.et_msg.setText("");
                            instance.recentMsg = 0;
                        }
                    }
                }
            } catch (Exception ignored) {}
            super.onPostExecute(s);
        }
    }

    public class CustomScrollListener extends RecyclerView.OnScrollListener {
        public CustomScrollListener() {}

        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            switch (newState) {
                case RecyclerView.SCROLL_STATE_IDLE:
                    System.out.println("The RecyclerView is not scrolling");
                    break;
                case RecyclerView.SCROLL_STATE_DRAGGING:
                    System.out.println("Scrolling now");
                    break;
                case RecyclerView.SCROLL_STATE_SETTLING:
                    System.out.println("Scroll Settling");
                    break;
            }

        }

        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {

            if (dy > 0) {
                setScrolledData();
            } else if (dy < 0) {
                setScrolledData();
            } else {
                System.out.println("No Vertical Scrolled");
            }
        }
    }

    void setScrolledData() {
        if (mainChatListTemp.size() > 0) {
            if (linearLayoutManager.findLastVisibleItemPosition() != mainChatListTemp.size() - 1) {
                rl_scroll.setVisibility(View.VISIBLE);
                isScroll=true;
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

   private void setSeenAfterScroll(){
       isPause=false;
       isScroll=false;
       if (tempListForSeen.size() > 0) {
           String receiver_id = "";
           ArrayList<String>  ids=new ArrayList<>();
           for (int i = tempListForSeen.size() - 1; i >= 0; i--) {
               if (Config.GetId().equalsIgnoreCase(tempListForSeen.get(i).getMsg_receiver_id())) {
                   ChatModel.Data chatData = tempListForSeen.get(i);
                   if (!chatData.getIs_seen().equalsIgnoreCase("1")){
                       ids.add(ids.size(),chatData.getMsg_id());
                       receiver_id=chatData.getMsg_receiver_id();
                   }
               }
           }

           ////
           if (!isPause && !isScroll){
               HashMap<String, String> hashMap = new HashMap<>();
               hashMap.put(ParaName.KEY_MSGID, ids.toString());
               hashMap.put(ParaName.KEY_RECEIVERID, receiver_id);
               setMsgSeen(hashMap,ids);
           }

           //
       }
   }

    public void callSendImage(MultipartBody.Part file, String name, String path, Bitmap bitmap) {
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
            sendMsg("image", file, name, path);
        });

        mImage.setImageBitmap(bitmap);

        try {
            mDIalog.show();
        } catch (Exception ignored) {}

    }

    @Override
    protected void onDestroy() {
        if (handler!=null){
            handler.removeCallbacks(runnable);
            handler=null;
            runnable=null;
        }

        instance=null;
        super.onDestroy();
    }

     static class SortByMsgId implements Comparator<ChatModel.Data> {

         public int compare(ChatModel.Data s1, ChatModel.Data s2) {
             int a = Integer.parseInt(s1.getMsg_id());
             int b = Integer.parseInt(s2.getMsg_id());
             if (a == b) return 0;
             else if (a > b) return 1;
             else return -1;
         }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case REQUEST_CODE_ASK_PERMISSION_CAMERA:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    try {
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    } catch (ActivityNotFoundException e) {
                        // display error state to the user
                    }
                } else  if (grantResults[0] != PackageManager.PERMISSION_GRANTED && grantResults[1] != PackageManager.PERMISSION_GRANTED && grantResults[2] != PackageManager.PERMISSION_GRANTED) {
                    AppController.PermissionSettingPopup(mContext, "Requires Permission",getString(R.string.all_for_camera_chat),"Cancel",true);
                } else  if (grantResults[1] != PackageManager.PERMISSION_GRANTED && grantResults[2] != PackageManager.PERMISSION_GRANTED) {
                    AppController.PermissionSettingPopup(mContext, "Storage permission required",getString(R.string.storage_for_camera_chat),"Cancel",true);
                }else if (grantResults[0] != PackageManager.PERMISSION_GRANTED ) {
                    PermissionSettingPopup(mContext, "Camera permission required",getString(R.string.camera_for_camera_chat),"Open Gallery",true);
                }
                break;

            case REQUEST_CODE_ASK_PERMISSIONS_GALLERY:
                Log.d("grantResults.length", String.valueOf(grantResults.length));
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED ) {
                    Intent intent = new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, IMG_RESULT);
                } else  if (grantResults[0] != PackageManager.PERMISSION_GRANTED && grantResults[1] != PackageManager.PERMISSION_GRANTED ) {
                    AppController.PermissionSettingPopup(mContext, "Storage permission required",getString(R.string.storage_image_chat),"Cancel",true);
                } else  if (grantResults[1] != PackageManager.PERMISSION_GRANTED) {
                    AppController.PermissionSettingPopup(mContext, "Storage permission required",getString(R.string.storage_image_chat),"Cancel",true);
                }else {
                    AppController.PermissionSettingPopup(mContext, "Storage permission required",getString(R.string.storage_image_chat),"Cancel",true);
                }

                break;

            case REQUEST_CODE_ASK_PERMISSIONS_DOC:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED ) {
                    callFile();
                } else  if (grantResults[0] != PackageManager.PERMISSION_GRANTED && grantResults[1] != PackageManager.PERMISSION_GRANTED ) {
                     AppController.PermissionSettingPopup(mContext, "Storage permission required",getString(R.string.storage_doc_chat),"Cancel",true);
                } else  if (grantResults[1] != PackageManager.PERMISSION_GRANTED) {
                    AppController.PermissionSettingPopup(mContext, "Storage permission required",getString(R.string.storage_doc_chat),"Cancel",true);
                }else {
                    AppController.PermissionSettingPopup(mContext, "Storage permission required",getString(R.string.storage_doc_chat),"Cancel",true);
                }

                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }
    public boolean PermissionSettingPopup(Context mContext,String title,String msg,String cancletxt,boolean isCancel){

        boolean action=false;
        Dialog progressdialog = new Dialog(mContext);
        progressdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressdialog.setContentView(R.layout.permission_popup);
        progressdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        progressdialog.getWindow().setAttributes(lp);
        TextView tv_msg= progressdialog.findViewById(R.id.tv_msg);
        TextView tv_title= progressdialog.findViewById(R.id.title);
        TextView tv_cancel= progressdialog.findViewById(R.id.tv_cancel);
        View view1= progressdialog.findViewById(R.id.view1);
        progressdialog.setCancelable(true);
        progressdialog.setCanceledOnTouchOutside(false);
        tv_msg.setText(msg);
        tv_title.setText(title);
        tv_cancel.setText(cancletxt);
        if (isCancel){
            tv_cancel.setVisibility(View.VISIBLE);
            view1.setVisibility(View.VISIBLE);
        }else {
            tv_cancel.setVisibility(View.GONE);
            view1.setVisibility(View.GONE);
        }

        progressdialog.findViewById(R.id.tv_yes).setOnClickListener(v1 -> {
            progressdialog.dismiss();
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", mContext.getPackageName(), null);
            intent.setData(uri);
            mContext.startActivity(intent);
        });
        progressdialog.findViewById(R.id.tv_cancel).setOnClickListener(v12 ->{
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                    PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                    PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_ASK_PERMISSIONS_GALLERY);
            } else{
                Intent intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, IMG_RESULT);
            }
            progressdialog.dismiss();
        });
        try {
            progressdialog.show();
        }catch (Exception ignored){}
        return action;
    }
    private void sendIsTyping(HashMap<String,String> hashMap) {
        SwipeeApiClient.swipeeServiceInstance().sendIsTyping(hashMap).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
            }
        });
    }

    private void sendOnlineOffline(HashMap<String,String> hashMap) {
        SwipeeApiClient.swipeeServiceInstance().getOnlineOffline(hashMap).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.code() == 200 && response.body() != null) {
                    if (response.body().get("code").getAsInt() == 200 && response.body().get("status").getAsBoolean()) {
                               JsonObject online_statusObj=response.body().getAsJsonObject("data");
                               String online_status=online_statusObj.has("online_status")?online_statusObj.get("online_status").getAsString():"";
                        iv_online_offline.setImageResource(online_status.equalsIgnoreCase("Online")?R.drawable.circle_green_bg:R.drawable.gray_semiround_bg);
                    }
                }

            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
            }
        });

    }
}