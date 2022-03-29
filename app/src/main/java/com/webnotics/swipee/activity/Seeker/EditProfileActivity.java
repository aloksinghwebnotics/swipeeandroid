package com.webnotics.swipee.activity.Seeker;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.JsonObject;
import com.webnotics.swipee.R;
import com.webnotics.swipee.UrlManager.AppController;
import com.webnotics.swipee.UrlManager.Config;
import com.webnotics.swipee.rest.ParaName;
import com.webnotics.swipee.rest.Rest;
import com.webnotics.swipee.rest.SwipeeApiClient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_IMAGE_CAPTURE = 231;
    private static final int RESULT_CROP = 121;
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 153;
    private static final int SELECT_VIDEO = 102;
    private final ArrayList<String> genderList = new ArrayList<>();
    Spinner spn_gender;
    TextView tv_dob, tv_country, tv_state, tv_city, tv_save, tv_email, tv_phone, tv_upload;
    CircleImageView civ_profile;
    EditText et_fname, et_lname;
    ImageView iv_back, iv_edit, iv_userpickvideo;
    Context mContext;
    Rest rest;
    private String stateId = "";
    private String cityId = "";
    private final String countryId = "101";
    private String picturePath = "";
    private static final int IMG_RESULT = 123;
    private int genderId = 0;
    private BottomSheetBehavior bottomsheet_intent;
    private File tempfile;
    private String selectedVideoPath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getColor(R.color.white));
        mContext = this;
        rest = new Rest(mContext);
        tv_dob = findViewById(R.id.tv_dob);
        tv_country = findViewById(R.id.tv_country);
        tv_state = findViewById(R.id.tv_state);
        tv_city = findViewById(R.id.tv_city);
        tv_save = findViewById(R.id.tv_save);
        civ_profile = findViewById(R.id.civ_profile);
        et_fname = findViewById(R.id.et_fname);
        et_lname = findViewById(R.id.et_lname);
        tv_email = findViewById(R.id.et_email);
        tv_phone = findViewById(R.id.et_phone);
        iv_back = findViewById(R.id.iv_back);
        iv_edit = findViewById(R.id.iv_edit);
        tv_upload = findViewById(R.id.tv_upload);
        iv_userpickvideo = findViewById(R.id.iv_userpickvideo);

        et_fname.setText(Config.GetFName());
        et_lname.setText(Config.GetLName());
        tv_email.setText(Config.GetEmail());
        tv_phone.setText(Config.GetMobileNo());
        tv_city.setText(Config.GetCityName());
        tv_state.setText(Config.GetStateName());
        tv_country.setText(Config.GetCountryName());
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) !=
                PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE_ASK_PERMISSIONS);
        }

        tv_dob.setText(Config.GetDob().replaceAll("-", "/"));
        stateId = Config.GetStateId();
        cityId = Config.GetCityId();
        if (getIntent() != null) {
            String videoUrl = getIntent().getStringExtra("videoUrl") != null ? getIntent().getStringExtra("videoUrl") : "";
            if (!TextUtils.isEmpty(videoUrl)) {
                Glide
                        .with(mContext)
                        .load(videoUrl)
                        .transform(new MultiTransformation(new CenterCrop(), new RoundedCorners((int) (mContext.getResources().getDisplayMetrics().density * 16))))
                        .into(iv_userpickvideo);

                iv_userpickvideo.setVisibility(View.VISIBLE);
            }

        }

        spn_gender = findViewById(R.id.spn_gender);
        genderList.add(0, "Select Gender");
        genderList.add(1, "Female");
        genderList.add(2, "Male");
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<String>(this, R.layout.simple_spinner_item, genderList);
        genderAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spn_gender.setAdapter(genderAdapter);

        if (Config.GetGender().equalsIgnoreCase("Female")) spn_gender.setSelection(1);
        else if (Config.GetGender().equalsIgnoreCase("Male")) spn_gender.setSelection(2);
        else spn_gender.setSelection(0);

        tv_save.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        civ_profile.setOnClickListener(this);
        tv_state.setOnClickListener(this);
        tv_city.setOnClickListener(this);
        tv_dob.setOnClickListener(this);
        iv_edit.setOnClickListener(this);
        tv_upload.setOnClickListener(this);
        Glide.with(mContext)
                .load(Config.GetPICKURI())
                .apply(new RequestOptions().placeholder(R.drawable.ic_profile_select).error(R.drawable.ic_profile_select))
                .into(civ_profile);
        spn_gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                genderId = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        industryBottomSheet();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_save:
                if (TextUtils.isEmpty(et_fname.getText().toString()))
                    rest.showToast("Enter first name");
                else if (TextUtils.isEmpty(et_lname.getText().toString())) rest.showToast("Enter last name");
                else if (genderId == 0) rest.showToast("Select gender");
                else if (TextUtils.isEmpty(tv_dob.getText().toString())) rest.showToast("Select DOB");
                else if (TextUtils.isEmpty(countryId)) rest.showToast("Select country");
                else if (TextUtils.isEmpty(stateId)) rest.showToast("Select state");
                else if (TextUtils.isEmpty(cityId)) rest.showToast("Select city");
                else {
                    MultipartBody.Part body1 = null;
                    MultipartBody.Part video = null;
                    File file = new File(picturePath);
                    if (file.exists()) {
                        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                        body1 = MultipartBody.Part.createFormData(ParaName.KEY_PROFILEIMAGE, file.getName(), requestFile);
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
                        setProfile(body1, video);
                    } else rest.AlertForInternet();
                }
                break;
            case R.id.tv_city:
                if (!TextUtils.isEmpty(stateId))
                    startActivity(new Intent(mContext, AddCityActivity.class).putExtra("id", stateId).putExtra("city_id", cityId).putExtra("from", EditProfileActivity.class.getSimpleName()));
                else rest.showToast("Select state first");
                break;
            case R.id.tv_state:
                startActivity(new Intent(mContext, AddStateActivity.class).putExtra("state_id", stateId).putExtra("from", EditProfileActivity.class.getSimpleName()));
                break;
            case R.id.tv_dob:
                setDate();
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.civ_profile:
                if (!TextUtils.isEmpty(picturePath)) AppController.callFullImage(mContext, picturePath);
                else if (!TextUtils.isEmpty(Config.GetPICKURI())) AppController.callFullImage(mContext, Config.GetPICKURI());

                break;
            case R.id.iv_edit:
                callProfilePick();
                break;
            case R.id.tv_upload:
                callVideoPick();
                break;

            default:
                break;
        }
    }

    private void setDate() {
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(mContext,
                (view, year, monthOfYear, dayOfMonth) -> {
                    int mothfinal = monthOfYear + 1;
                    tv_dob.setText(year + "/" + mothfinal + "/" + dayOfMonth);
                }, mYear, mMonth, mDay);

        String dateCurrent = Calendar.getInstance().getTime().toString();
        SimpleDateFormat formatCurrent = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy");
        try {
            Date myDate = formatCurrent.parse(dateCurrent);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(myDate);
            calendar.add(Calendar.YEAR, -11);
            datePickerDialog.getDatePicker().setMaxDate(calendar.getTime().getTime());

        } catch (Exception e) {
            e.printStackTrace();
        }
        datePickerDialog.show();
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

    private void callProfilePick() {

        bottomsheet_intent.setState(BottomSheetBehavior.STATE_EXPANDED);

    }

    private void callVideoPick() {
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, SELECT_VIDEO);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == IMG_RESULT) {
            Uri imageUri = data.getData();
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
            } else rest.showToast("Please select video max 15 MB.");
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
                    civ_profile.setImageBitmap(selectedBitmap);
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
                        if (pictureFile != null) picturePath = pictureFile.getAbsolutePath();
                    } catch (FileNotFoundException e) {
                    } catch (IOException e) {
                    }
                } catch (Exception e) {
                    if (tempfile != null) {
                        picturePath = tempfile.getAbsolutePath();
                        Glide.with(mContext)
                                .load(picturePath)
                                .into(civ_profile);
                    }
                }

            }
        }
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
            if (tempfile != null) {
                picturePath = tempfile.getAbsolutePath();
                Glide.with(mContext)
                        .load(picturePath)
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

    private void setProfile(MultipartBody.Part body1, MultipartBody.Part video) {
        String MULTIPART_FORM_DATA = "multipart/form-data";
        HashMap<String, RequestBody> map = new HashMap<>();
        map.put(ParaName.KEYTOKEN, RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), Config.GetUserToken()));
        map.put(ParaName.KEY_FNAME, RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), et_fname.getText().toString()));
        map.put(ParaName.KEY_LNAME, RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), et_lname.getText().toString()));
        map.put(ParaName.KEY_DOB, RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), tv_dob.getText().toString()));
        map.put(ParaName.KEY_CITY, RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), cityId));
        map.put(ParaName.KEY_COUNTRY, RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), countryId));
        map.put(ParaName.KEY_GENDERID, RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), String.valueOf(genderId)));
        map.put(ParaName.KEY_STATE, RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), stateId));
        SwipeeApiClient.swipeeServiceInstance().updateProfile(map, body1, video).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                AppController.dismissProgressdialog();
                if (response.code() == 200 && response.body() != null) {
                    if (response.body().get("code").getAsInt() == 203) {
                        rest.showToast(response.body().get("message").getAsString());
                        AppController.loggedOut(mContext);
                        finish();
                    } else if (response.body().get("status").getAsBoolean()) {
                        rest.showToast(response.body().get("message").getAsString());
                        finish();
                    }
                } else rest.showToast("Something went wrong");
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                AppController.dismissProgressdialog();
            }
        });

    }

}