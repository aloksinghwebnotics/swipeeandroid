package com.webnotics.swipee.activity;

import static com.webnotics.swipee.rest.ParaName.KEY_APPOINTMENTID;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.JsonObject;
import com.webnotics.swipee.R;
import com.webnotics.swipee.UrlManager.AppController;
import com.webnotics.swipee.UrlManager.Config;
import com.webnotics.swipee.activity.Seeker.SeekerHomeActivity;
import com.webnotics.swipee.activity.company.CompanyHomeActivity;
import com.webnotics.swipee.activity.company.UserDetail;
import com.webnotics.swipee.adapter.ChatMessageAdapter;
import com.webnotics.swipee.model.ChatModel;
import com.webnotics.swipee.rest.ApiUrls;
import com.webnotics.swipee.rest.ParaName;
import com.webnotics.swipee.rest.Rest;
import com.webnotics.swipee.rest.SwipeeApiClient;

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

    private static final int REQUEST_IMAGE_CAPTURE = 212;
    private static final int IMG_RESULT = 111;
    private static final int DOCUMENT_FILE_REQUEST = 214;
    private RelativeLayout rl_scroll;
    private BottomSheetBehavior bottomsheet_intent;
    @SuppressLint("StaticFieldLeak")
    public static ChatActivity instance;
    ImageView iv_back;
    CircleImageView iv_profile;
    ImageView iv_camera, iv_send, iv_doc, iv_scroll,iv_refresh;
    TextView tv_name, tv_action, tv_newmsgcount;
    Context mContext;
    Rest rest;
    private final ArrayList<ChatModel.Data> mainChatList = new ArrayList<>();
    private final ArrayList<ChatModel.Data> mainChatListTemp = new ArrayList<>();
    ChatMessageAdapter chatMessageAdapter;
    RecyclerView rv_chat;
    private String msg_id = "";
    private String appointment_id = "";
    private LinearLayoutManager linearLayoutManager;
    private String user_id = "";
    private String name = "";
    private String job_id = "";
    private String receiver = "";
    private String sender = "";
    private String senderRole = "";
    private String receiverRole = "";
    private EditText et_msg;
    private int recentMsg = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getColor(R.color.white));

        mContext = this;
        instance = this;
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
        iv_refresh.setVisibility(View.GONE);


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
            if (!Config.isSeeker()) {
                tv_action.setText(action);
                tv_action.setVisibility(View.GONE);
            } else {
                tv_action.setVisibility(View.GONE);
            }
            if (rest.isInterentAvaliable()) {
                AppController.ShowDialogue("", mContext);
                getChat(appointment_id, "0");
            }
        }


        iv_back.setOnClickListener(v -> backPressed());

        iv_profile.setOnClickListener(v -> callProfile());
        tv_name.setOnClickListener(v -> callProfile());

        iv_doc.setOnClickListener(v -> callFile());
        iv_camera.setOnClickListener(v -> bottomsheet_intent.setState(BottomSheetBehavior.STATE_EXPANDED));

        iv_send.setOnClickListener(v -> {

            if (!TextUtils.isEmpty(et_msg.getText().toString()) && !TextUtils.isEmpty(et_msg.getText().toString().replaceAll(" ", "")))
                sendMsg("text", null, "", "https://swipee.in/files/chat/");
        });

        iv_refresh.setOnClickListener(v -> {
            if (mainChatList.size() > 0)
                getChat(appointment_id, mainChatList.get(mainChatList.size() - 1).getMsg_id());
             else   getChat(appointment_id, "0");
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
        });
    }

    private void sendMsg(String msg_type, MultipartBody.Part file, String name, String path) {
        String MULTIPART_FORM_DATA = "multipart/form-data";
        HashMap<String, RequestBody> map = new HashMap<>();
        map.put(KEY_APPOINTMENTID, RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), appointment_id));
        map.put(ParaName.KEY_SENDERID, RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), sender));
        map.put(ParaName.KEY_RECEIVERID1, RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), receiver));
        map.put(ParaName.KEY_SENDERROLEID, RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), senderRole));
        map.put(ParaName.KEY_RECVRROLEID, RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), receiverRole));
        map.put(ParaName.KEY_MSGTYPE, RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), msg_type));
        map.put(ParaName.KEY_MSGCONTENT, RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), et_msg.getText().toString().trim()));
        sendMessage(map, file);
        String date = Calendar.getInstance().getTime().toString();
        SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy");
        SimpleDateFormat formatout = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat formatdate = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formattime = new SimpleDateFormat("hh:mm:ss aa");
        Date dateFinal;
        String date1 = "";
        String time = "";
        String dateOnly = "";
        try {
            dateFinal = format.parse(date);
            date1 = formatout.format(dateFinal);
            time = formattime.format(dateFinal);
            dateOnly = formatdate.format(dateFinal);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        ChatModel.Data chatmodel = new ChatModel.Data("0", appointment_id, sender, receiver, et_msg.getText().toString(), path, name, 0, msg_type, "0", dateOnly, time, date1);
        mainChatListTemp.add(mainChatListTemp.size(), chatmodel);
        if (chatMessageAdapter != null)
            chatMessageAdapter.notifyItemInserted(mainChatListTemp.size());
        if (linearLayoutManager != null) {
            linearLayoutManager.scrollToPosition(mainChatListTemp.size() - 1);
        }
        rl_scroll.setVisibility(View.GONE);
        tv_newmsgcount.setVisibility(View.GONE);
        et_msg.setText("");
        recentMsg = 0;
    }

    private void callProfile() {
        if (Config.isSeeker()) {
            startActivity(new Intent(mContext, CompanyProfile.class).putExtra("company_id", user_id));
        } else {
            mContext.startActivity(new Intent(mContext, UserDetail.class).putExtra("from", ChatActivity.class.getSimpleName()).
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
    protected void onResume() {
        super.onResume();

    }

    private void getChat(String appointmentId, String msgId) {
        iv_refresh.startAnimation(AnimationUtils.loadAnimation(mContext,R.anim.rotate));
        SwipeeApiClient.swipeeServiceInstance().getChat(Config.GetUserToken(), appointmentId, msgId).enqueue(new Callback<ChatModel>() {

            @Override
            public void onResponse(@NonNull Call<ChatModel> call, @NonNull Response<ChatModel> response) {
                iv_refresh.clearAnimation();
                iv_refresh.setVisibility(View.VISIBLE);
                AppController.dismissProgressdialog();
                if (response.code() == 200 && response.body() != null) {
                    ChatModel responceBody = response.body();
                    if (responceBody.getCode() == 203) {
                        rest.showToast(responceBody.getMessage());
                        AppController.loggedOut(mContext);
                        finish();
                    } else if (responceBody.getCode() == 200 && responceBody.isStatus()) {
                        mainChatList.addAll(mainChatList.size(), responceBody.getData());
                        ArrayList<ChatModel.Data> listTemp = new ArrayList<>(mainChatListTemp);
                        recentMsg = recentMsg + (mainChatList.size() - mainChatListTemp.size());
                        mainChatListTemp.clear();
                        mainChatListTemp.addAll(mainChatList);
                        if (chatMessageAdapter != null) {
                            chatMessageAdapter.notifyDataSetChanged();
                        }
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
                                        if (linearLayoutManager != null) {
                                            linearLayoutManager.scrollToPosition(mainChatListTemp.size() - 1);
                                        }
                                        tv_newmsgcount.setVisibility(View.GONE);
                                        recentMsg = 0;
                                    } else {
                                        if (mainChatListTemp.size() > 0) {
                                            if (linearLayoutManager.findLastVisibleItemPosition() != mainChatListTemp.size() - 1) {
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
                                    if (linearLayoutManager != null) {
                                        linearLayoutManager.scrollToPosition(mainChatListTemp.size() - 1);
                                    }
                                    tv_newmsgcount.setVisibility(View.GONE);
                                    recentMsg = 0;
                                }
                            }
                        }
                        if (mainChatList.size() > 0) {
                            for (int i = mainChatList.size() - 1; i >= 0; i--) {
                                if (Config.GetId().equalsIgnoreCase(mainChatList.get(i).getMsg_receiver_id())) {
                                    ChatModel.Data chatData = mainChatList.get(i);
                                    String receiver_id = chatData.getMsg_receiver_id();
                                    String msg_id = chatData.getMsg_id();
                                    HashMap<String, String> hashMap = new HashMap<>();
                                    hashMap.put(ParaName.KEY_MSGID, msg_id);
                                    hashMap.put(ParaName.KEY_RECEIVERID, receiver_id);
                                    setMsgSeen(hashMap);
                                    break;
                                }
                            }
                        }

                    }

                } else rest.showToast("Something went wrong");

            }

            @Override
            public void onFailure(@NonNull Call<ChatModel> call, @NonNull Throwable t) {
                AppController.dismissProgressdialog();
                iv_refresh.clearAnimation();
                iv_refresh.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setMsgSeen(HashMap<String, String> hashMap) {
        SwipeeApiClient.swipeeServiceInstance().setMsgSeen(hashMap).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.code() == 200 && response.body() != null) {
                    if (response.body().get("code").getAsInt() == 203) {
                        rest.showToast(response.body().get("message").getAsString());
                        AppController.loggedOut(mContext);
                        finish();
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
        String[] supportedMimeTypes = {"application/pdf"};
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType(supportedMimeTypes[0]);
        intent.putExtra(Intent.EXTRA_MIME_TYPES, supportedMimeTypes);
        startActivityForResult(intent, DOCUMENT_FILE_REQUEST);
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
                    FileOutputStream fos = new FileOutputStream(pictureFile);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 40, fos);
                    fos.close();
                    if (pictureFile != null) {
                        if (pictureFile.exists()) {
                            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), pictureFile);
                            MultipartBody.Part body1 = MultipartBody.Part.createFormData(ParaName.KEY_MSGFILE, pictureFile.getName(), requestFile);
                            sendMsg("image", body1, pictureFile.getName(), pictureFile.getAbsolutePath());
                        }

                    }
                } catch (FileNotFoundException e) {
                } catch (IOException e) {
                }
            }


        } else if (resultCode == RESULT_OK && requestCode == DOCUMENT_FILE_REQUEST) {
            if (data == null) {

                return;
            }
            Uri selectedFileUri = data.getData();
            String docName = "file_" + Calendar.getInstance().getTimeInMillis() + ".pdf";
            new UploadFile(selectedFileUri, mContext, docName).execute();
            String date = Calendar.getInstance().getTime().toString();
            SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy");
            SimpleDateFormat formatout = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            SimpleDateFormat formatdate = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat formattime = new SimpleDateFormat("hh:mm:ss aa");
            Date dateFinal;
            String date1 = "";
            String time = "";
            String dateOnly = "";
            try {
                dateFinal = format.parse(date);
                date1 = formatout.format(dateFinal);
                time = formattime.format(dateFinal);
                dateOnly = formatdate.format(dateFinal);

            } catch (ParseException e) {
                e.printStackTrace();
            }

            ChatModel.Data chatmodel = new ChatModel.Data("0", appointment_id, sender, receiver, et_msg.getText().toString(), selectedFileUri.getEncodedPath(), docName, 0, "document", "0", dateOnly, time, date1);
            mainChatListTemp.add(mainChatListTemp.size(), chatmodel);
            if (chatMessageAdapter != null)
                chatMessageAdapter.notifyItemInserted(mainChatListTemp.size());
            if (linearLayoutManager != null) {
                linearLayoutManager.scrollToPosition(mainChatListTemp.size() - 1);
            }
            rl_scroll.setVisibility(View.GONE);
            tv_newmsgcount.setVisibility(View.GONE);
            recentMsg = 0;
            et_msg.setText("");
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
                FileOutputStream fos = new FileOutputStream(pictureFile);
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 70, fos);
                fos.close();
                if (pictureFile != null) {
                    if (pictureFile != null && pictureFile.exists()) {

                        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), pictureFile);
                        MultipartBody.Part body1 = MultipartBody.Part.createFormData(ParaName.KEY_MSGFILE, pictureFile.getName(), requestFile);
                        sendMsg("image", body1, pictureFile.getName(), pictureFile.getAbsolutePath());
                    }

                }
            } catch (FileNotFoundException e) {
            } catch (IOException e) {
            }
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
                if (result != null) {
                    Log.d("result_for upload", result);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {


            super.onPostExecute(s);
        }
    }

    public class CustomScrollListener extends RecyclerView.OnScrollListener {
        public CustomScrollListener() {
        }

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
            if (dx > 0) {
                System.out.println("Scrolled Right");
            } else if (dx < 0) {
                System.out.println("Scrolled Left");
            } else {
                System.out.println("No Horizontal Scrolled");
            }

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
}