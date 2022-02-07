package com.webnotics.swipee.activity.company;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.webnotics.swipee.CustomUi.PopinsRegularTextView;
import com.webnotics.swipee.R;
import com.webnotics.swipee.UrlManager.AppController;
import com.webnotics.swipee.UrlManager.Config;
import com.webnotics.swipee.activity.Seeker.AddCityActivity;
import com.webnotics.swipee.activity.Seeker.AddStateActivity;
import com.webnotics.swipee.activity.Seeker.SearchIndustryActivity;
import com.webnotics.swipee.fragments.company.CompanyProfileFragments;
import com.webnotics.swipee.rest.ParaName;
import com.webnotics.swipee.rest.Rest;
import com.webnotics.swipee.rest.SwipeeApiClient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompanyEditProfile extends AppCompatActivity implements View.OnClickListener {
    private static final int SELECT_VIDEO = 212;
    private static final int IMG_RESULT = 1;
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 61;
    private static final int RESULT_CROP = 21;
    private static final int REQUEST_IMAGE_CAPTURE = 421;
    Context mContext;
    Rest rest;
    ImageView iv_back, iv_edit, iv_userpickvideo;
    CircleImageView civ_profile;
    EditText et_companyname, et_website, et_founded, et_pincode, et_address;
    TextView tv_industry, tv_companysize, tv_mobile, tv_email, tv_upload, tv_country, tv_state, tv_city, tv_save;
    @SuppressLint("StaticFieldLeak")
    public static CompanyEditProfile instance;
    private String industryId = "";
    private JsonArray company_size = new JsonArray();
    private Dialog progressdialog;
    public Uri imageUri;
    private String picturePath = "";
    private String selectedVideoPath;
    private String stateId = "";
    private String cityId = "";
    private File tempfile;
    private BottomSheetBehavior bottomsheet_intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_edit_profile);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getColor(R.color.white));
        mContext = this;
        rest = new Rest(mContext);
        instance = this;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE_ASK_PERMISSIONS);
        }

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

        iv_back = findViewById(R.id.iv_back);
        iv_edit = findViewById(R.id.iv_edit);
        civ_profile = findViewById(R.id.civ_profile);
        et_companyname = findViewById(R.id.et_companyname);
        et_website = findViewById(R.id.et_website);
        et_founded = findViewById(R.id.et_founded);
        tv_industry = findViewById(R.id.tv_industry);
        tv_companysize = findViewById(R.id.tv_companysize);
        tv_mobile = findViewById(R.id.tv_mobile);
        tv_email = findViewById(R.id.tv_email);
        tv_upload = findViewById(R.id.tv_upload);
        tv_save = findViewById(R.id.tv_save);
        iv_userpickvideo = findViewById(R.id.iv_userpickvideo);
        tv_country = findViewById(R.id.tv_country);
        tv_state = findViewById(R.id.tv_state);
        tv_city = findViewById(R.id.tv_city);
        et_pincode = findViewById(R.id.et_pincode);
        et_address = findViewById(R.id.et_address);
        tv_city.setText(Config.GetCityName());
        tv_state.setText(Config.GetStateName());
        tv_country.setText(TextUtils.isEmpty(Config.GetCountryName()) ? "India" : Config.GetCountryName());
        tv_industry.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        iv_edit.setOnClickListener(this);
        tv_companysize.setOnClickListener(this);
        tv_upload.setOnClickListener(this);
        tv_save.setOnClickListener(this);
        tv_state.setOnClickListener(this);
        tv_city.setOnClickListener(this);
        et_companyname.setText(Config.GeCompanyName());
        tv_industry.setText(Config.GetIndustry());
        tv_mobile.setText(Config.GetMobileNo());
        tv_email.setText(Config.GetEmail());
        industryId = Config.GetIndustryId();
        stateId = Config.GetStateId();
        cityId = Config.GetCityId();
        if (getIntent() != null) {
            String company_size = getIntent().getStringExtra("company_size") != null ? getIntent().getStringExtra("company_size") : "";
            String website = getIntent().getStringExtra("website") != null ? getIntent().getStringExtra("website") : "";
            String founded = getIntent().getStringExtra("founded") != null ? getIntent().getStringExtra("founded") : "";
            String address = getIntent().getStringExtra("address") != null ? getIntent().getStringExtra("address") : "";
            String pincode = getIntent().getStringExtra("pincode") != null ? getIntent().getStringExtra("pincode") : "";
            tv_companysize.setText(company_size);
            et_website.setText(website);
            et_founded.setText(founded);
            et_address.setText(address);
            et_pincode.setText(pincode);
        }
        if (getIntent()!=null){
            String videoUrl=getIntent().getStringExtra("videoUrl")!=null?getIntent().getStringExtra("videoUrl"):"";
            if (!TextUtils.isEmpty(videoUrl)){
                Glide
                        .with(mContext)
                        .load(videoUrl)
                        .transform(new MultiTransformation(new CenterCrop(), new RoundedCorners((int) (mContext.getResources().getDisplayMetrics().density * 10))))
                        .into(iv_userpickvideo);

                iv_userpickvideo.setVisibility(View.VISIBLE);
            }

        }
        Glide.with(mContext)
                .load(Config.GetPICKURI())
                .apply(new RequestOptions().placeholder(R.drawable.ic_pick).error(R.drawable.ic_pick))
                .into(civ_profile);

        if (rest.isInterentAvaliable()) {
            AppController.ShowDialogue("", mContext);
            callCompanySize();
        } else rest.AlertForInternet();

        civ_profile.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(picturePath)) {
                AppController.callFullImage(mContext, picturePath);
            } else if (!TextUtils.isEmpty(Config.GetPICKURI()))
                AppController.callFullImage(mContext, Config.GetPICKURI());
        });

        industryBottomSheet();

    }

    private void callProfilePick() {

        bottomsheet_intent.setState(BottomSheetBehavior.STATE_EXPANDED);

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

            //CropImage.activity().start(EditProfileActivity.this);
        });
        tv_cancel.setOnClickListener(v -> bottomsheet_intent.setState(BottomSheetBehavior.STATE_COLLAPSED));

    }

    private void callCompanySize() {
        SwipeeApiClient.swipeeServiceInstance().companySize().enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                AppController.dismissProgressdialog();
                if (response.code() == 200 && response.body() != null) {
                    JsonObject responseBody = response.body();
                    if (!responseBody.isJsonNull()) {
                        if (responseBody.get("status").getAsBoolean()) {
                            JsonObject data = responseBody.get("data").getAsJsonObject();
                            company_size = data.isJsonNull() ? new JsonArray() : data.has("company_size") ? data.get("company_size").getAsJsonArray() : new JsonArray();
                        }

                    }
                } else {
                    rest.showToast("Something went wrong");
                }

            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {

                AppController.dismissProgressdialog();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_edit:
                callProfilePick();
                break;
            case R.id.tv_city:
                if (!TextUtils.isEmpty(stateId) && !stateId.equalsIgnoreCase("0"))
                    startActivity(new Intent(mContext, AddCityActivity.class).putExtra("id", stateId).putExtra("from", CompanyEditProfile.class.getSimpleName()));
                else rest.showToast("Select State First");
                break;
            case R.id.tv_state:
                startActivity(new Intent(mContext, AddStateActivity.class).putExtra("from", CompanyEditProfile.class.getSimpleName()));
                break;
            case R.id.tv_save:
                if (TextUtils.isEmpty(et_companyname.getText().toString())) {
                    rest.showToast("Enter Company Name");
                } else if (TextUtils.isEmpty(tv_industry.getText().toString())) {
                    rest.showToast("Select Industry");
                } else if (TextUtils.isEmpty(tv_companysize.getText().toString())) {
                    rest.showToast("Select Company Size");
                } else if (TextUtils.isEmpty(et_website.getText().toString())) {
                    rest.showToast("Enter Company Website");
                } else if (TextUtils.isEmpty(et_address.getText().toString())) {
                    rest.showToast("Enter Company Address");
                } else if (TextUtils.isEmpty(et_pincode.getText().toString())) {
                    rest.showToast("Enter Company Pincode");
                } else if (TextUtils.isEmpty(et_founded.getText().toString())) {
                    rest.showToast("Enter Foundation Year");
                } else if (TextUtils.isEmpty(tv_country.getText().toString())) {
                    rest.showToast("Select Country");
                } else if (TextUtils.isEmpty(tv_state.getText().toString())) {
                    rest.showToast("Select State");
                } else if (TextUtils.isEmpty(tv_city.getText().toString())) {
                    rest.showToast("Select City");
                } else {
                    MultipartBody.Part image = null;
                    MultipartBody.Part video = null;
                    if (picturePath != null && !TextUtils.isEmpty(picturePath)) {
                        File file = new File(picturePath);
                        if (file != null && file.exists()) {
                            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                            image = MultipartBody.Part.createFormData(ParaName.KEY_COMPANYLOGO, file.getName(), requestFile);
                        }
                    }
                    if (selectedVideoPath != null && !TextUtils.isEmpty(selectedVideoPath)) {

                        File file1 = new File(selectedVideoPath);
                        /*   ProgressRequestBody fileBody = new ProgressRequestBody(file1, this);*/
                        if (file1.exists()) {
                            RequestBody requestFile1 = RequestBody.create(MediaType.parse("multipart/form-data"), file1);
                            video = MultipartBody.Part.createFormData(ParaName.KEY_COMPANYVIDEO, file1.getName(), requestFile1);
                        }
                    }
                    if (rest.isInterentAvaliable()) {
                        AppController.ShowDialogue("", mContext);
                        callAllDataService(image, video);
                    } else rest.AlertForInternet();

                }

                break;
            case R.id.tv_companysize:
                callCompanySizePopup();
                break;
            case R.id.tv_upload:
                callVideoPick();
                break;
            case R.id.tv_industry:
                startActivity(new Intent(mContext, SearchIndustryActivity.class));
                break;

            default:
                break;
        }

    }

    private void callAllDataService(MultipartBody.Part image, MultipartBody.Part video) {
        String MULTIPART_FORM_DATA = "multipart/form-data";
        HashMap<String, RequestBody> map = new HashMap<>();
        map.put(ParaName.KEYTOKEN, RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), Config.GetUserToken()));
        map.put(ParaName.KEY_COMPANYADD, RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), et_address.getText().toString()));
        String countryId = "101";
        map.put(ParaName.KEY_COUNTRY, RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), countryId));
        map.put(ParaName.KEY_STATE, RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), stateId));
        map.put(ParaName.KEY_CITY, RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), cityId));
        map.put(ParaName.KEY_COMPANYPIN, RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), et_pincode.getText().toString()));
        map.put(ParaName.KEY_INDUSTRY, RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), industryId));
        map.put(ParaName.KEY_COMPANYEMPCOUNT, RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), tv_companysize.getText().toString()));
        map.put(ParaName.KEY_COMPANYSTABLISHED, RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), et_founded.getText().toString()));
        map.put(ParaName.KEY_COMPANYNAME, RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), et_companyname.getText().toString()));
        map.put(ParaName.KEY_COMPANYWEBSITE, RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), et_website.getText().toString()));
        SwipeeApiClient.swipeeServiceInstance().saveCompanyInfo(map, image, video).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                AppController.dismissProgressdialog();
                if (response.code() == 200 && response.body() != null) {
                    rest.showToast(response.body().get("message").getAsString());
                    if (response.body().get("code").getAsInt() == 200 && response.body().get("status").getAsBoolean()) {
                        if (CompanyProfileFragments.instance != null) {
                            CompanyProfileFragments.instance.hitService();
                        }
                        finish();
                    } else if (response.body().get("code").getAsInt() == 203) {
                        AppController.loggedOut(mContext);
                        finish();
                    } else AppController.dismissProgressdialog();
                } else {
                    AppController.dismissProgressdialog();
                    rest.showToast("Something went wrong");
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                AppController.dismissProgressdialog();
            }
        });

    }

    private void callVideoPick() {
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, SELECT_VIDEO);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IMG_RESULT) {
            imageUri = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = mContext.getContentResolver().query(imageUri, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            tempfile = new File(cursor.getString(columnIndex));
            performCrop(tempfile);
            cursor.close();

        } else if (resultCode == RESULT_OK && requestCode == SELECT_VIDEO) {
            String selectedVideo = getPath(data.getData());
            double size = getFileSizeMegaBytes(new File(selectedVideo));
            if (size <= 15) {
                selectedVideoPath = selectedVideo;
                Glide
                        .with(mContext)
                        .load(Uri.fromFile(new File(selectedVideoPath)))
                        .transform(new MultiTransformation(new CenterCrop(), new RoundedCorners((int) (mContext.getResources().getDisplayMetrics().density * 16))))
                        .into(iv_userpickvideo);

                iv_userpickvideo.setVisibility(View.VISIBLE);
            } else rest.showToast("Video must be lesser than 15MB");
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
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.close();
                if (pictureFile != null) {
                    picturePath = pictureFile.getAbsolutePath();
                    Glide.with(mContext)
                            .load(picturePath)
                            .apply(RequestOptions.bitmapTransform(new RoundedCorners((int) (mContext.getResources().getDisplayMetrics().density * (24)))))
                            .into(civ_profile);
                }
            } catch (FileNotFoundException e) {
            } catch (IOException e) {
            }
        }
        if (requestCode == RESULT_CROP) {
            if (resultCode == RESULT_OK) {
                try {
                    Bundle extras = data.getExtras();
                    Bitmap selectedBitmap = extras.getParcelable("data");
                    File pictureFile = null;
                    try {
                        pictureFile = createImageFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        FileOutputStream fos = new FileOutputStream(pictureFile);
                        selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                        fos.close();
                        if (pictureFile != null) {
                            picturePath = pictureFile.getAbsolutePath();
                            Glide.with(mContext)
                                    .load(picturePath)
                                    .error(R.drawable.ic_pick)
                                    .placeholder(R.drawable.ic_pick)
                                    .into(civ_profile);
                        }
                    } catch (FileNotFoundException e) {
                    } catch (IOException e) {
                    }
                } catch (Exception e) {
                    if (tempfile != null) {
                        picturePath = tempfile.getAbsolutePath();
                        Glide.with(mContext)
                                .load(picturePath)
                                .error(R.drawable.ic_pick)
                                .placeholder(R.drawable.ic_pick)
                                .into(civ_profile);
                    }

                }

            }
        }
    }

    private void performCrop(File picUri) {
        try {
            //Start Crop Activity

            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            // File f = new File(picUri);
            Uri contentUri = Uri.fromFile(picUri);

            cropIntent.setDataAndType(contentUri, "image/*");
            // set crop properties
            cropIntent.putExtra("crop", "true");
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            cropIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, RESULT_CROP);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            // display an error message
            if (tempfile != null) {
                picturePath = tempfile.getAbsolutePath();
                Glide.with(mContext)
                        .load(picturePath)
                        .error(R.drawable.ic_pick)
                        .placeholder(R.drawable.ic_pick)
                        .into(civ_profile);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String imageFileName = "JPEG_" + "profile" + "_swipee";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpeg",         /* suffix */
                storageDir      /* directory */
        );

        return image;
    }

    private static double getFileSizeMegaBytes(File file) {
        return (double) file.length() / (1024 * 1024);
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = mContext.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else return null;
    }

    private void callCompanySizePopup() {
        progressdialog = new Dialog(mContext);
        progressdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressdialog.setContentView(R.layout.company_size_popup);
        progressdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        progressdialog.getWindow().setAttributes(lp);

        RecyclerView rv_sizelist = progressdialog.findViewById(R.id.rv_sizelist);
        rv_sizelist.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        SizeAdapter sizeAdapter = new SizeAdapter(mContext);
        rv_sizelist.setAdapter(sizeAdapter);
        rv_sizelist.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
        try {
            progressdialog.show();
        } catch (Exception e) {
        }


    }

    public void selectIndustry(String name, String id) {
        if (tv_industry != null) {
            tv_industry.setText(name);
            industryId = id;
        }
    }

    public class SizeAdapter extends RecyclerView.Adapter<SizeAdapter.MyViewHolder> {

        Context mContext;

        public SizeAdapter(Context mContext) {
            // TODO Auto-generated constructor stub
        }


        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.child_state, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
            holder.tv_item.setText(company_size.get(position).getAsString());
            holder.radioButton.setVisibility(View.GONE);
            holder.tv_item.setOnClickListener(v -> {
                tv_companysize.setText(company_size.get(position).getAsString());
                progressdialog.dismiss();
            });
        }


        @Override
        public long getItemId(int pos) {

            return 0;
        }

        @Override
        public int getItemCount() {
            return company_size.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            PopinsRegularTextView tv_item;
            RadioButton radioButton;

            public MyViewHolder(View view) {
                super(view);

                tv_item = view.findViewById(R.id.tv_item);
                radioButton = view.findViewById(R.id.radioButton);

            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            String from = intent.getStringExtra("from") != null ? intent.getStringExtra("from") : "";
            if (from.equalsIgnoreCase("State")) {
                tv_city.setText("");
                tv_state.setText(intent.getStringExtra("name"));
                stateId = intent.getStringExtra("id");
                cityId = "";
            } else if (from.equalsIgnoreCase("City")) {
                tv_city.setText(intent.getStringExtra("name"));
                cityId = intent.getStringExtra("id");
            }
        }
    }

}