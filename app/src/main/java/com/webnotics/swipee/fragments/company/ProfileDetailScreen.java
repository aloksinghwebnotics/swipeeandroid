package com.webnotics.swipee.fragments.company;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
import com.webnotics.swipee.activity.company.CompanyHomeActivity;
import com.webnotics.swipee.fragments.Basefragment;
import com.webnotics.swipee.rest.ParaName;
import com.webnotics.swipee.rest.Rest;
import com.webnotics.swipee.rest.SwipeeApiClient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileDetailScreen extends Basefragment implements View.OnClickListener {

    private static final int RESULT_CROP = 321;
    private static final int REQUEST_IMAGE_CAPTURE = 211;
    ImageView iv_shaddowleft, iv_shaddowright, iv_pickimg, iv_userpickimg,iv_userpickvideo;
    LinearLayout rl_name, rl_pick,rl_about;
    TextView tv_next, tv_back,tv_pickimg,tv_heycompany,tv_uploadvedio,et_industry,et_companysize,tv_country, tv_state, tv_city;
    EditText et_name,et_website,et_founded,et_pincode,et_address;
    Context mContext;
    private Rest rest;
    private static final int SELECT_VIDEO = 212;
    private static final int IMG_RESULT = 1;
    public Uri imageUri;
    public String name = "";
    private String picturePath = "";
    private String selectedVideoPath;
    @SuppressLint("StaticFieldLeak")
    public static ProfileDetailScreen instance;
    private String industryId="";
    private JsonArray company_size=new JsonArray();
    private Dialog progressdialog;
    private String stateId = "";
    private String cityId = "";
    private String countryId = "101";
    private File tempfile;
    private RelativeLayout intent_sheet;
    private BottomSheetBehavior bottomsheet_intent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.profile_detail_screen, container, false);
        mContext = getActivity();
        rest = new Rest(mContext);
        instance=this;

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

        iv_shaddowleft = rootView.findViewById(R.id.iv_shaddowleft);
        iv_shaddowright = rootView.findViewById(R.id.iv_shaddowright);
        rl_name = rootView.findViewById(R.id.rl_name);
        rl_pick = rootView.findViewById(R.id.rl_pick);
        rl_about = rootView.findViewById(R.id.rl_about);
        tv_next = rootView.findViewById(R.id.tv_next);
        tv_back = rootView.findViewById(R.id.tv_back);
        tv_heycompany = rootView.findViewById(R.id.tv_heycompany);
        et_industry = rootView.findViewById(R.id.et_industry);
        et_companysize = rootView.findViewById(R.id.et_companysize);
        et_website = rootView.findViewById(R.id.et_website);
        et_founded = rootView.findViewById(R.id.et_founded);
        tv_uploadvedio = rootView.findViewById(R.id.tv_uploadvedio);
        iv_userpickvideo = rootView.findViewById(R.id.iv_userpickvideo);
        iv_userpickvideo.setVisibility(View.GONE);
        tv_country = rootView.findViewById(R.id.tv_country);
        tv_state = rootView.findViewById(R.id.tv_state);
        tv_city = rootView.findViewById(R.id.tv_city);
        et_pincode = rootView.findViewById(R.id.et_pincode);
        et_address = rootView.findViewById(R.id.et_address);
        et_name = rootView.findViewById(R.id.et_name);
        iv_pickimg = rootView.findViewById(R.id.iv_pickimg);
        tv_pickimg = rootView.findViewById(R.id.tv_pickimg);
        iv_userpickimg = rootView.findViewById(R.id.iv_userpickimg);
        tv_country.setText("India");
      // et_name.setText(Config.GeCompanyName());

        tv_next.setOnClickListener(this);
        tv_back.setOnClickListener(this);
        iv_pickimg.setOnClickListener(this);
        tv_uploadvedio.setOnClickListener(this);
        et_industry.setOnClickListener(this);
        et_companysize.setOnClickListener(this);
        tv_city.setOnClickListener(this);
        tv_state.setOnClickListener(this);

        if (rest.isInterentAvaliable()){
            AppController.ShowDialogue("",mContext);
            callCompanySize();
        }else rest.AlertForInternet();
        industryBottomSheet(rootView);

        return rootView;
    }

    @Override
    public int setContentView() {
        return R.layout.profile_detail_screen;
    }

    @Override
    protected void backPressed() {
        callBackPressed();
    }

    public  void setCityData(String cityName, String cityId) {
        tv_city.setText(cityName);
        this.cityId = cityId;
    }
    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.tv_next:
                if (rl_name.getVisibility() == View.VISIBLE) {
                    if (!TextUtils.isEmpty(et_name.getText().toString()) && et_name.getText().toString().replaceAll(" ", "").length() > 0) {
                        rl_name.setVisibility(View.GONE);
                        rl_about.setVisibility(View.VISIBLE);
                        rl_pick.setVisibility(View.GONE);
                        iv_shaddowleft.setVisibility(View.GONE);
                        iv_shaddowright.setVisibility(View.GONE);
                        tv_back.setVisibility(View.VISIBLE);
                        name = et_name.getText().toString();
                        tv_heycompany.setText("Hey "+name+" !");
                    } else {
                        rest.showToast("Enter company name");
                    }

                } else if (rl_about.getVisibility() == View.VISIBLE) {
                    if (TextUtils.isEmpty(et_industry.getText().toString()) || et_industry.getText().toString().replaceAll(" ", "").length() == 0) {
                               rest.showToast("Select industry");
                    } else if (TextUtils.isEmpty(et_companysize.getText().toString()) || et_companysize.getText().toString().replaceAll(" ", "").length() == 0) {
                        rest.showToast("Select company size");
                    }else if (TextUtils.isEmpty(et_website.getText().toString()) || et_website.getText().toString().replaceAll(" ", "").length() == 0) {
                        rest.showToast("Enter company website");
                    }else if (TextUtils.isEmpty(et_address.getText().toString()) || et_address.getText().toString().replaceAll(" ", "").length() == 0) {
                        rest.showToast("Enter company address");
                    }else if (TextUtils.isEmpty(et_pincode.getText().toString()) || et_pincode.getText().toString().replaceAll(" ", "").length() == 0) {
                        rest.showToast("Enter 6 digit pincode");
                    }else if (et_pincode.getText().toString().replaceAll(" ", "").length() !=6 ) {
                        rest.showToast("Enter 6 digit pincode");
                    }else if (TextUtils.isEmpty(et_founded.getText().toString()) || et_founded.getText().toString().replaceAll(" ", "").length() == 0) {
                        rest.showToast("Enter established year");
                    }else if (et_founded.getText().toString().replaceAll(" ", "").length()!=4) {
                        rest.showToast("Enter 4 digit established year");
                    }else if (TextUtils.isEmpty(tv_country.getText().toString())){
                        rest.showToast("Select country");
                    }else if (TextUtils.isEmpty(tv_state.getText().toString())){
                        rest.showToast("Select state");
                    }else if (TextUtils.isEmpty(tv_city.getText().toString())){
                        rest.showToast("Select city");
                    } else {
                        rl_about.setVisibility(View.GONE);
                        rl_pick.setVisibility(View.VISIBLE);
                        iv_shaddowleft.setVisibility(View.VISIBLE);
                        iv_shaddowright.setVisibility(View.VISIBLE);
                        rl_name.setVisibility(View.GONE);
                        tv_back.setVisibility(View.VISIBLE);
                    }

                } else if (rl_pick.getVisibility() == View.VISIBLE) {
                    if (picturePath != null && !TextUtils.isEmpty(picturePath.toString())) {
                            MultipartBody.Part image = null;
                            MultipartBody.Part video = null;
                            File file = new File(picturePath);
                        if (selectedVideoPath != null && !TextUtils.isEmpty(selectedVideoPath)) {
                            File file1 = new File(selectedVideoPath);
                            if ( file1.exists()){
                                RequestBody requestFile1 = RequestBody.create(MediaType.parse("multipart/form-data"), file1);
                                video = MultipartBody.Part.createFormData(ParaName.KEY_COMPANYVIDEO, file1.getName(), requestFile1);
                            }
                        }
                            if (file != null && file.exists()) {
                                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                                image = MultipartBody.Part.createFormData(ParaName.KEY_COMPANYLOGO, file.getName(), requestFile);
                                if (rest.isInterentAvaliable()) {
                                    AppController.ShowDialogue("", mContext);
                                    callAllDataService(image,video);
                                } else rest.AlertForInternet();
                            }
                    } else rest.showToast("Select profile image");
                }
                break;

            case R.id.tv_back:
                callBackPressed();
                break;

                case R.id.et_companysize:
                    callCompanySizePopup();
                break;
            case R.id.iv_pickimg:
                callProfilePick();
                break;
            case R.id.tv_uploadvedio:
                callVideoPick();
                break;
            case R.id.et_industry:
     Intent intent = new Intent(mContext, SearchIndustryActivity.class);
     startActivity(intent);
                break;
            case R.id.tv_city:
                if (!TextUtils.isEmpty(stateId) && !stateId.equalsIgnoreCase("0"))
                    startActivity(new Intent(mContext, AddCityActivity.class).putExtra("id", stateId).putExtra("city_id", cityId).putExtra("from", ProfileDetailScreen.class.getSimpleName()));
                else rest.showToast("Select state first");
                break;
            case R.id.tv_state:
                startActivity(new Intent(mContext, AddStateActivity.class).putExtra("state_id",stateId).putExtra("from",ProfileDetailScreen.class.getSimpleName()));
                break;
            default:
                break;
        }
    }

    private void callVideoPick() {
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, SELECT_VIDEO);
    }


    private void callProfilePick() {
        bottomsheet_intent.setState(BottomSheetBehavior.STATE_EXPANDED);
    }
    private void industryBottomSheet(View roorview) {
        intent_sheet = roorview.findViewById(R.id.intent_sheet);
        bottomsheet_intent = BottomSheetBehavior.from(intent_sheet);
        TextView tv_camera=intent_sheet.findViewById(R.id.tv_camera);
        TextView tv_gallery=intent_sheet.findViewById(R.id.tv_gallery);
        TextView tv_cancel=intent_sheet.findViewById(R.id.tv_cancel);
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
    private void callBackPressed() {

        if (rl_name.getVisibility()==View.VISIBLE){
            getActivity().finish();
        }else if (rl_about.getVisibility()==View.VISIBLE){
            rl_name.setVisibility(View.VISIBLE);
            rl_about.setVisibility(View.GONE);
            rl_pick.setVisibility(View.GONE);
            iv_shaddowleft.setVisibility(View.GONE);
            iv_shaddowright.setVisibility(View.GONE);
            tv_back.setVisibility(View.GONE);
        }else if (rl_pick.getVisibility()==View.VISIBLE){
            rl_name.setVisibility(View.GONE);
            rl_about.setVisibility(View.VISIBLE);
            rl_pick.setVisibility(View.GONE);
            iv_shaddowleft.setVisibility(View.GONE);
            iv_shaddowright.setVisibility(View.GONE);
            tv_back.setVisibility(View.VISIBLE);
        }
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
             tempfile=new File( cursor.getString(columnIndex));
            performCrop(tempfile);
            cursor.close();


        } else if (resultCode==RESULT_OK && requestCode==SELECT_VIDEO){
            String selectedVideo = getPath(data.getData());
        double size=    getFileSizeMegaBytes(new File( selectedVideo ));
        if (size<=15){
            selectedVideoPath=selectedVideo;
            Glide
                    .with( mContext )
                    .load( Uri.fromFile( new File( selectedVideoPath ) ) )
                    .transform(new MultiTransformation(new CenterCrop(),new RoundedCorners((int) (mContext.getResources().getDisplayMetrics().density*16))))
                    .into( iv_userpickvideo );
            iv_userpickvideo.setVisibility(View.VISIBLE);
        }else rest.showToast("Please select video max 15 MB.");
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
                    picturePath=pictureFile.getAbsolutePath();
                    Glide.with(mContext)
                            .load(picturePath)
                            .apply(RequestOptions.bitmapTransform(new RoundedCorners((int) (mContext.getResources().getDisplayMetrics().density * (24)))))
                            .into(iv_userpickimg);
                    iv_pickimg.setVisibility(View.GONE);
                    tv_pickimg.setVisibility(View.GONE);
                }
            } catch (FileNotFoundException e) {
            } catch (IOException e) {
            }
        }

        if (requestCode == RESULT_CROP ) {
            if(resultCode ==RESULT_OK){
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
                            picturePath=pictureFile.getAbsolutePath();
                            Glide.with(mContext)
                                    .load(picturePath)
                                    .apply(RequestOptions.bitmapTransform(new RoundedCorners((int) (mContext.getResources().getDisplayMetrics().density * (24)))))
                                    .into(iv_userpickimg);
                            iv_pickimg.setVisibility(View.GONE);
                            tv_pickimg.setVisibility(View.GONE);
                        }
                    } catch (FileNotFoundException e) {
                    } catch (IOException e) {
                    }
                }catch (Exception e){
                    if (tempfile != null) {
                        picturePath=tempfile.getAbsolutePath();
                        Glide.with(mContext)
                                .load(picturePath)
                                .apply(RequestOptions.bitmapTransform(new RoundedCorners((int) (mContext.getResources().getDisplayMetrics().density * (24)))))
                                .into(iv_userpickimg);
                        iv_pickimg.setVisibility(View.GONE);
                        tv_pickimg.setVisibility(View.GONE);
                    }
                }

            }
        }
    }
    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = mContext.getContentResolver().query(uri, projection, null, null, null);
        if(cursor!=null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        else return null;
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
                picturePath=tempfile.getAbsolutePath();
                Glide.with(mContext)
                        .load(picturePath)
                        .apply(RequestOptions.bitmapTransform(new RoundedCorners((int) (mContext.getResources().getDisplayMetrics().density * (24)))))
                        .into(iv_userpickimg);
                iv_pickimg.setVisibility(View.GONE);
                tv_pickimg.setVisibility(View.GONE);
            }
        }
    }
    private File createImageFile() throws IOException {
        // Create an image file name

        String imageFileName = "JPEG_" + "profile" + "_swipee";
        File storageDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        return File.createTempFile(
                imageFileName,
                ".jpeg",
                storageDir
        );
    }
    private void callAllDataService(MultipartBody.Part body1,MultipartBody.Part body2) {
        String MULTIPART_FORM_DATA = "multipart/form-data";
        HashMap<String, RequestBody> map = new HashMap<>();
        map.put(ParaName.KEYTOKEN, RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), Config.GetUserToken()));
        map.put(ParaName.KEY_COMPANYADD, RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), et_address.getText().toString()));
        map.put(ParaName.KEY_COUNTRY, RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), countryId));
        map.put(ParaName.KEY_STATE, RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), stateId));
        map.put(ParaName.KEY_CITY, RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), cityId));
        map.put(ParaName.KEY_COMPANYPIN, RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), et_pincode.getText().toString()));
        map.put(ParaName.KEY_INDUSTRY, RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), industryId));
        map.put(ParaName.KEY_COMPANYEMPCOUNT, RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), et_companysize.getText().toString()));
        map.put(ParaName.KEY_COMPANYSTABLISHED, RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), et_founded.getText().toString()));
        map.put(ParaName.KEY_COMPANYNAME, RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), name));
        map.put(ParaName.KEY_ISEPROFILEUPDATED, RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), "Y"));
        map.put(ParaName.KEY_COMPANYWEBSITE, RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), et_website.getText().toString()));
        SwipeeApiClient.swipeeServiceInstance().saveCompanyInfo(map, body1,body2).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                AppController.dismissProgressdialog();
                if (response.code() == 200 && response.body() != null) {
                    if (response.body().get("status").getAsBoolean()) {
                        Config.SetIsProfileUpdate("Y");
                        Config.setRemember(true);
                        Config.SetIsUserLogin(true);
                        Config.SetIsSeeker(false);
                        mContext.startActivity(new Intent(mContext, CompanyHomeActivity.class));
                        getActivity().finish();
                    } else{
                        rest.showToast(response.body().get("message").getAsString());
                    }
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

    private static double getFileSizeMegaBytes(File file) {
        return (double) file.length() / (1024 * 1024);
    }

    public void selectIndustry(String name, String id) {
        if (et_industry!=null){
            et_industry.setText(name);
            industryId=id;
        }
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

        RecyclerView rv_sizelist=progressdialog.findViewById(R.id.rv_sizelist);
        rv_sizelist.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
       SizeAdapter sizeAdapter=new SizeAdapter();
        rv_sizelist.setAdapter(sizeAdapter);
        rv_sizelist.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));

        try {
            progressdialog.show();
        }catch (Exception ignored){}


    }

    public void setStateData(String stateName, String stateId) {
        tv_city.setText("");
        tv_state.setText(stateName);
        this.stateId = stateId;
        cityId = "";
    }

    public class SizeAdapter extends RecyclerView.Adapter<SizeAdapter.MyViewHolder> {


        public SizeAdapter() {

            // TODO Auto-generated constructor stub

        }


        @NonNull
        @Override
        public SizeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.child_state, parent, false);

            return new SizeAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull SizeAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
            holder.tv_item.setText(company_size.get(position).getAsString());
            holder.radioButton.setClickable(false);
            holder.tv_item.setOnClickListener(v -> {
                et_companysize.setText(company_size.get(position).getAsString());
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

        public  class MyViewHolder extends RecyclerView.ViewHolder {

            PopinsRegularTextView tv_item;
            RadioButton radioButton;

            public MyViewHolder(View view) {
                super(view);

                tv_item = view.findViewById(R.id.tv_item);
                radioButton = view.findViewById(R.id.radioButton);

            }
        }


    }
    private void callCompanySize() {
        SwipeeApiClient.swipeeServiceInstance().companySize().enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                AppController.dismissProgressdialog();
                if (response.code() == 200 && response.body() != null) {
                    JsonObject responseBody = response.body();
                    if (!responseBody.isJsonNull()){
                        if (responseBody.get("status").getAsBoolean()){
                            JsonObject data= responseBody.get("data").getAsJsonObject();
                            company_size=data.isJsonNull()? new JsonArray():data.has("company_size")?data.get("company_size").getAsJsonArray():new JsonArray();
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
}
