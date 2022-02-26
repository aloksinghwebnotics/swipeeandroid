package com.webnotics.swipee.fragments.seeker;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.webnotics.swipee.CustomUi.FlowLayout;
import com.webnotics.swipee.CustomUi.PopinsRegularTextView;
import com.webnotics.swipee.R;
import com.webnotics.swipee.UrlManager.AppController;
import com.webnotics.swipee.UrlManager.Config;
import com.webnotics.swipee.activity.Seeker.AddCityActivity;
import com.webnotics.swipee.activity.Seeker.AddStateActivity;
import com.webnotics.swipee.activity.Seeker.SeekerHomeActivity;
import com.webnotics.swipee.adapter.seeeker.AddSkillAdapter;
import com.webnotics.swipee.fragments.Basefragment;
import com.webnotics.swipee.interfaces.AddSkillInterface;
import com.webnotics.swipee.model.AddSkillsModel;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileInfoScreen extends Basefragment implements View.OnClickListener, TextWatcher, AddSkillInterface {
    private static final int REQUEST_IMAGE_CAPTURE = 243;
    private static final int RESULT_CROP = 87;
    @SuppressLint("StaticFieldLeak")
    public static ProfileInfoScreen instance;
    ImageView iv_shaddowleft, iv_shaddowright, iv_pickimg, iv_userpickimg, iv_resume,iv_pickvideo,iv_uservideoimg;
    LinearLayout rl_name, rl_birthday, rl_pick, rl_about, rl_resume,rl_video,rl_location;
    RelativeLayout rl_skill;
    TextView tv_next, tv_back, tv_year, tv_month, tv_date, tv_pickimg,tv_resumename,tv_country,tv_state,tv_city;
    EditText et_name, et_about,et_lname;
    Context mContext;
    LinearLayout ll_date, ll_month, ll_year;
    private Rest rest;
    private static final int IMG_RESULT = 1;
    private static final int SELECT_VIDEO = 212;

    public Uri imageUri;
    private JsonObject responseBody = new JsonObject();
    private final int DOCUMENT_FILE_REQUEST = 342;
    public String name = "";
    public String lname = "";
    public String dob = "";
    public String about = "";
    private String picturePath = "";
    private Uri selectedFileUri;
    ArrayList<AddSkillsModel> mArrayListSkills;
    ListView mListView;
    AddSkillAdapter skilladapter;
    FlowLayout flowlay;
    View.OnClickListener btnClickListener;
    AddSkillInterface addSkillsInterface;
    ArrayList<String> mArrayListdesiredindustries, mArrayListid;
    AddSkillsModel model;
    EditText et_search;
    NestedScrollView kdkdkdkd;
    private String selectedVideoPath="";
    private RelativeLayout intent_sheet;
    private BottomSheetBehavior bottomsheet_intent;
    private String stateId="0";
    private String cityId="0";
    private String countryId="101";
    private File tempfile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.profile_info_screen, container, false);
        mContext = getActivity();
        rest = new Rest(mContext);
        instance=this;

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
        iv_shaddowleft = rootView.findViewById(R.id.iv_shaddowleft);
        iv_shaddowright = rootView.findViewById(R.id.iv_shaddowright);
        rl_name = rootView.findViewById(R.id.rl_name);
        rl_birthday = rootView.findViewById(R.id.rl_birthday);
        rl_pick = rootView.findViewById(R.id.rl_pick);
        rl_about = rootView.findViewById(R.id.rl_about);
        rl_resume = rootView.findViewById(R.id.rl_resume);
        rl_skill = rootView.findViewById(R.id.rl_skill);
        tv_next = rootView.findViewById(R.id.tv_next);
        tv_back = rootView.findViewById(R.id.tv_back);
        tv_year = rootView.findViewById(R.id.tv_year);
        tv_month = rootView.findViewById(R.id.tv_month);
        tv_date = rootView.findViewById(R.id.tv_date);
        et_name = rootView.findViewById(R.id.et_name);
        et_lname = rootView.findViewById(R.id.et_lname);
        ll_date = rootView.findViewById(R.id.ll_date);
        ll_month = rootView.findViewById(R.id.ll_month);
        ll_year = rootView.findViewById(R.id.ll_year);
        iv_pickimg = rootView.findViewById(R.id.iv_pickimg);
        tv_pickimg = rootView.findViewById(R.id.tv_pickimg);
        iv_userpickimg = rootView.findViewById(R.id.iv_userpickimg);
        et_about = rootView.findViewById(R.id.et_about);
        iv_resume = rootView.findViewById(R.id.iv_resume);
        flowlay = rootView.findViewById(R.id.flowlayout);
        mListView = rootView.findViewById(R.id.mlistview);
        et_search = rootView.findViewById(R.id.et_search);
        kdkdkdkd = rootView.findViewById(R.id.kdkdkdkd);
        tv_resumename = rootView.findViewById(R.id.tv_resumename);
        rl_video = rootView.findViewById(R.id.rl_video);
        iv_pickvideo = rootView.findViewById(R.id.iv_pickvideo);
        iv_uservideoimg = rootView.findViewById(R.id.iv_uservideoimg);
        tv_country = rootView.findViewById(R.id.tv_country);
        tv_state = rootView.findViewById(R.id.tv_state);
        tv_city = rootView.findViewById(R.id.tv_city);
        rl_location = rootView.findViewById(R.id.rl_location);
//Via string array
        tv_next.setOnClickListener(this);
        tv_back.setOnClickListener(this);
        ll_year.setOnClickListener(this);
        ll_month.setOnClickListener(this);
        ll_date.setOnClickListener(this);
        iv_pickimg.setOnClickListener(this);
        iv_resume.setOnClickListener(this);
        iv_pickvideo.setOnClickListener(this);
        tv_city.setOnClickListener(this);
        tv_state.setOnClickListener(this);
        et_name.setText(Config.GetFName());
        et_lname.setText(Config.GetLName());

        industryBottomSheet(rootView);
        if (rest.isInterentAvaliable()) {
            AppController.ShowDialogue("", mContext);
            callSkillList();
        } else {
            rest.AlertForInternet();
        }
        et_search.addTextChangedListener(this);
        mArrayListdesiredindustries = new ArrayList<>();
        mArrayListid = new ArrayList<>();
        addSkillsInterface = this;
        mArrayListSkills = new ArrayList<>();
        btnClickListener = v -> {
            // TODO Auto-generated method stub
            FlowLayout ll = (FlowLayout) v.getParent();

            for (int j = 0; j < mArrayListSkills.size(); j++) {
                if (v.getTag().toString().equalsIgnoreCase(mArrayListSkills.get(j).getSkill_name())) {
                    AddSkillsModel model1 = new AddSkillsModel();
                    model1.setSkill_name(mArrayListSkills.get(j).getSkill_name());
                    model1.setSelected(false);
                    model1.setSkill_id(mArrayListSkills.get(j).getSkill_id());
                    mArrayListSkills.set(j, model1);
                    if (skilladapter != null)
                        skilladapter.notifyDataSetChanged();
                }
            }
            for (int z = 0; z < mArrayListdesiredindustries.size(); z++) {
                if (v.getTag().toString().equalsIgnoreCase(mArrayListdesiredindustries.get(z))) {
                    mArrayListdesiredindustries.remove(z);
                    mArrayListid.remove(z);
                    if (skilladapter != null)
                        skilladapter.getFilter().filter("");
                    et_search.setText("");
                }
            }
            ll.removeView(v);
            if (flowlay.getMeasuredHeight() > 400) {
                kdkdkdkd.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 424));
            } else
                kdkdkdkd.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        };


        return rootView;
    }

    @Override
    public int setContentView() {
        return R.layout.profile_info_screen;
    }

    @Override
    protected void backPressed() {
        callBackPressed();
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.tv_next:
                if (rl_name.getVisibility() == View.VISIBLE) {
                    if (TextUtils.isEmpty(et_name.getText().toString()) || et_name.getText().toString().replaceAll(" ", "").length() <= 0) {
                        rest.showToast("Please enter your first name.");
                    } else if (TextUtils.isEmpty(et_lname.getText().toString()) || et_lname.getText().toString().replaceAll(" ", "").length() <= 0) {
                        rest.showToast("Please enter your last name.");

                    } else {
                        rl_name.setVisibility(View.GONE);
                        rl_location.setVisibility(View.VISIBLE);
                        iv_shaddowleft.setVisibility(View.GONE);
                        iv_shaddowright.setVisibility(View.GONE);
                        rl_video.setVisibility(View.GONE);
                        tv_back.setVisibility(View.VISIBLE);
                    }
                }else if (rl_location.getVisibility() == View.VISIBLE) {
                if (TextUtils.isEmpty(tv_state.getText().toString()) ||TextUtils.isEmpty(stateId)|| stateId.equalsIgnoreCase("0") || tv_state.getText().toString().replaceAll(" ", "").length() <= 0) {
                    rest.showToast("Please select state.");
                } else if (TextUtils.isEmpty(tv_city.getText().toString())||TextUtils.isEmpty(cityId)|| cityId.equalsIgnoreCase("0") || tv_city.getText().toString().replaceAll(" ", "").length() <= 0) {
                    rest.showToast("Please select city.");
                } else {
                    rl_location.setVisibility(View.GONE);
                    rl_birthday.setVisibility(View.VISIBLE);
                    iv_shaddowleft.setVisibility(View.GONE);
                    iv_shaddowright.setVisibility(View.GONE);
                    rl_video.setVisibility(View.GONE);
                    tv_back.setVisibility(View.VISIBLE);
                }
            } else if (rl_birthday.getVisibility() == View.VISIBLE) {
                    if (!TextUtils.isEmpty(tv_date.getText().toString()) && !TextUtils.isEmpty(tv_month.getText().toString()) && !TextUtils.isEmpty(tv_year.getText().toString())) {
                        rl_birthday.setVisibility(View.GONE);
                        rl_pick.setVisibility(View.VISIBLE);
                        iv_shaddowleft.setVisibility(View.VISIBLE);
                        iv_shaddowright.setVisibility(View.VISIBLE);

                        tv_back.setVisibility(View.VISIBLE);
                        dob = tv_year.getText().toString() + "/" + tv_month.getText().toString() + "/" + tv_date.getText().toString();
                    } else {
                        rest.showToast("Please select your date of birth");
                    }


                } else if (rl_pick.getVisibility() == View.VISIBLE) {
                    if (picturePath != null && !TextUtils.isEmpty(picturePath.toString())) {
                        rl_pick.setVisibility(View.GONE);
                        rl_video.setVisibility(View.VISIBLE);
                        iv_shaddowleft.setVisibility(View.VISIBLE);
                        iv_shaddowright.setVisibility(View.VISIBLE);
                        tv_back.setVisibility(View.VISIBLE);
                    } else rest.showToast("Please select profile image");
                } else if (rl_video.getVisibility() == View.VISIBLE) {
                        rl_video.setVisibility(View.GONE);
                        rl_about.setVisibility(View.VISIBLE);
                        iv_shaddowleft.setVisibility(View.GONE);
                        iv_shaddowright.setVisibility(View.VISIBLE);
                        tv_back.setVisibility(View.VISIBLE);

                } else if (rl_about.getVisibility() == View.VISIBLE) {
                    if (!TextUtils.isEmpty(et_about.getText().toString()) && (et_about.getText().toString().replaceAll(" ", "").length() > 0)) {
                        rl_about.setVisibility(View.GONE);
                        rl_skill.setVisibility(View.VISIBLE);
                        iv_shaddowleft.setVisibility(View.GONE);
                        iv_shaddowright.setVisibility(View.GONE);
                        tv_back.setVisibility(View.VISIBLE);
                        about = et_about.getText().toString();
                    } else rest.showToast("Please write somethings about yourself.");

                } else if (rl_skill.getVisibility() == View.VISIBLE) {
                    if (mArrayListid.size() > 0) {
                        rl_skill.setVisibility(View.GONE);
                        rl_resume.setVisibility(View.VISIBLE);
                        iv_shaddowleft.setVisibility(View.VISIBLE);
                        iv_shaddowright.setVisibility(View.VISIBLE);
                        tv_back.setVisibility(View.VISIBLE);
                    } else {
                        rest.showToast("Please select at least 1 skill");
                    }

                } else if (rl_resume.getVisibility() == View.VISIBLE) {
                    if (selectedFileUri != null) {
                        MultipartBody.Part body1 = null;
                        MultipartBody.Part body2 = null;
                        File file = new File(picturePath);
                        if (file != null && file.exists()) {
                            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                            body1 = MultipartBody.Part.createFormData(ParaName.KEY_PROFILEIMAGE, file.getName(), requestFile);
                        }
                        if (selectedVideoPath != null && !TextUtils.isEmpty(selectedVideoPath.toString())) {
                            File file1 = new File(selectedVideoPath);
                            if ( file1.exists()){
                                RequestBody requestFile1 = RequestBody.create(MediaType.parse("multipart/form-data"), file1);
                                body2 = MultipartBody.Part.createFormData(ParaName.KEY_COMPANYVIDEO, file1.getName(), requestFile1);
                            }
                        }
                        if (rest.isInterentAvaliable()) {
                            AppController.ShowDialogue("", mContext);
                            callAllDataService(selectedFileUri, body1,body2);
                        } else rest.AlertForInternet();
                    } else rest.showToast("Please upload your resume");

                }
                break;
            case R.id.ll_date:
            case R.id.ll_month:
            case R.id.ll_year:
                setDate();
                break;
            case R.id.tv_back:
                callBackPressed();
                break;
            case R.id.iv_pickimg:
                callProfilePick();
                break;

                case R.id.iv_pickvideo:
                callVideoPick();
                break;
            case R.id.iv_resume:
                callResume();
                break;

            case R.id.tv_state:
                    startActivity(new Intent(mContext, AddStateActivity.class).putExtra("state_id",stateId).putExtra("from", ProfileInfoScreen.class.getSimpleName()));
                    break;

            case R.id.tv_city:
                        if (!TextUtils.isEmpty(stateId) && !stateId.equalsIgnoreCase("0"))
                            startActivity(new Intent(mContext, AddCityActivity.class).putExtra("city_id", cityId).putExtra("id", stateId).putExtra("from", ProfileInfoScreen.class.getSimpleName()));
                        else rest.showToast("Select State First");
                    break;
            default:
                break;
        }
    }
    public  void setCityData(String cityName, String cityId) {
        tv_city.setText(cityName);
        this.cityId = cityId;
    }

    public void setStateData(String stateName, String stateId) {
        tv_city.setText("");
        tv_state.setText(stateName);
        this.stateId = stateId;
        cityId = "0";
    }
    private void callResume() {
        String[] supportedMimeTypes = {"application/pdf"};
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType(supportedMimeTypes[0]);
        intent.putExtra(Intent.EXTRA_MIME_TYPES, supportedMimeTypes);
        startActivityForResult(intent, DOCUMENT_FILE_REQUEST);
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
        tv_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                } catch (ActivityNotFoundException e) {
                    // display error state to the user
                }
                bottomsheet_intent.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
        tv_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(intent, IMG_RESULT);
                bottomsheet_intent.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomsheet_intent.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

    }
    private void callVideoPick() {
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, SELECT_VIDEO);
    }

    private void callBackPressed() {


        if (rl_name.getVisibility() == View.VISIBLE) {
            getActivity().finish();
        } else if (rl_location.getVisibility() == View.VISIBLE) {
            rl_location.setVisibility(View.GONE);
            rl_name.setVisibility(View.VISIBLE);
            iv_shaddowleft.setVisibility(View.GONE);
            iv_shaddowright.setVisibility(View.GONE);
            tv_back.setVisibility(View.GONE);
        } else if (rl_birthday.getVisibility() == View.VISIBLE) {
            rl_birthday.setVisibility(View.GONE);
            rl_location.setVisibility(View.VISIBLE);
            iv_shaddowleft.setVisibility(View.GONE);
            iv_shaddowright.setVisibility(View.GONE);
            tv_back.setVisibility(View.VISIBLE);
        }  else if (rl_pick.getVisibility() == View.VISIBLE) {
            rl_pick.setVisibility(View.GONE);
            rl_birthday.setVisibility(View.VISIBLE);
            iv_shaddowleft.setVisibility(View.GONE);
            iv_shaddowright.setVisibility(View.GONE);
        }else if (rl_video.getVisibility() == View.VISIBLE) {
            rl_video.setVisibility(View.GONE);
            rl_pick.setVisibility(View.VISIBLE);
            iv_shaddowleft.setVisibility(View.GONE);
            iv_shaddowright.setVisibility(View.GONE);
        } else if (rl_about.getVisibility() == View.VISIBLE) {
            rl_about.setVisibility(View.GONE);
            rl_video.setVisibility(View.VISIBLE);
            iv_shaddowleft.setVisibility(View.VISIBLE);
            iv_shaddowright.setVisibility(View.VISIBLE);
        } else if (rl_skill.getVisibility() == View.VISIBLE) {
            rl_skill.setVisibility(View.GONE);
            rl_about.setVisibility(View.VISIBLE);
            iv_shaddowleft.setVisibility(View.GONE);
            iv_shaddowright.setVisibility(View.VISIBLE);
        } else if (rl_resume.getVisibility() == View.VISIBLE) {
            rl_resume.setVisibility(View.GONE);
            rl_skill.setVisibility(View.VISIBLE);
            iv_shaddowleft.setVisibility(View.GONE);
            iv_shaddowright.setVisibility(View.GONE);
        }
    }

    private void setDate() {
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(mContext,
                (view, year, monthOfYear, dayOfMonth) -> {
                    tv_date.setText(String.valueOf(dayOfMonth));
                    tv_month.setText(String.valueOf(monthOfYear + 1));
                    tv_year.setText(String.valueOf(year));

                }, mYear, mMonth, mDay);

        String dateCurrent = Calendar.getInstance().getTime().toString();
        SimpleDateFormat formatCurrent = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy");
        try
        {
            Date myDate = formatCurrent.parse(dateCurrent);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(myDate);
            calendar.add(Calendar.YEAR , -11 );
            datePickerDialog.getDatePicker().setMaxDate(calendar.getTime().getTime());

        }
        catch ( Exception e )
        {
            e.printStackTrace();

        }
        datePickerDialog.show();
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


        } else if (resultCode == RESULT_OK && requestCode == DOCUMENT_FILE_REQUEST) {
            if (data == null) {

                return;
            }
            selectedFileUri = data.getData();
            tv_resumename.setText("Resume.pdf");

            //
        }else if (resultCode==RESULT_OK && requestCode==SELECT_VIDEO){
            String selectedVideo = getPath(data.getData());
            double size=    getFileSizeMegaBytes(new File( selectedVideo ));
            if (size<=15){
                selectedVideoPath=selectedVideo;
                Glide
                        .with( mContext )
                        .load( Uri.fromFile( new File( selectedVideoPath ) ) )
                        .transform(new MultiTransformation(new CenterCrop(),new RoundedCorners((int) (mContext.getResources().getDisplayMetrics().density*16))))
                        .into( iv_uservideoimg );
                iv_pickvideo.setVisibility(View.GONE);

            }else rest.showToast("Please select video max 15 MB.");
        }
        else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

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
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpeg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        return image;
    }
    private static double getFileSizeMegaBytes(File file) {
        return (double) file.length() / (1024 * 1024);
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
    private void callAllDataService(Uri selectedFileUri, MultipartBody.Part body1, MultipartBody.Part body2) {
        String MULTIPART_FORM_DATA = "multipart/form-data";
        HashMap<String, RequestBody> map = new HashMap<>();
        map.put(ParaName.KEYTOKEN, RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), Config.GetUserToken()));
        map.put(ParaName.KEY_FNAME, RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), et_name.getText().toString()));
        map.put(ParaName.KEY_LNAME, RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), et_lname.getText().toString()));
        map.put(ParaName.KEY_DOB, RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), dob));
        map.put(ParaName.KEY_PSUMMARY, RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), about));
        map.put(ParaName.KEY_STATE, RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), stateId));
        map.put(ParaName.KEY_CITY, RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), cityId));
        map.put(ParaName.KEY_COUNTRY, RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), countryId));
        map.put(ParaName.KEY_ISEPROFILEUPDATED, RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), "N"));
        map.put(ParaName.KEY_SKILLID, RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), mArrayListid.toString()));
        SwipeeApiClient.swipeeServiceInstance().saveBasicInfo(map, body1,body2).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {

                if (response.code() == 200 && response.body() != null) {
                    if (response.body().get("status").getAsBoolean()) {
                         Config.SetName(name);
                        new UploadFile(selectedFileUri, mContext).execute();
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

    private void callSkillList() {
        SwipeeApiClient.swipeeServiceInstance().getSkill().enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                AppController.dismissProgressdialog();
                if (response.code() == 200 && response.body() != null) {
                    responseBody = response.body();
                    JsonArray mArrayListData = responseBody.has("data")?responseBody.get("data").getAsJsonArray():new JsonArray();
                    for (int i = 0; i < mArrayListData.size(); i++) {
                        model = new AddSkillsModel();
                        model.setSkill_id(mArrayListData.get(i).getAsJsonObject().get("skill_id").getAsString());
                        model.setSkill_name(mArrayListData.get(i).getAsJsonObject().get("skill_name").getAsString());
                        model.setSelected(false);
                        mArrayListSkills.add(model);
                    }
                    skilladapter = new AddSkillAdapter(mContext, mArrayListSkills, addSkillsInterface);
                    mListView.setAdapter(skilladapter);
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
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (skilladapter != null)
            skilladapter.getFilter().filter(s.toString().trim());
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.toString().length() == 0) {
            //  close.setVisibility(View.GONE);
        } else {
            //  close.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void skill(String data, String id) {
        LinearLayout linearLayout = new LinearLayout(mContext);
        LinearLayout linearLayoutF = new LinearLayout(mContext);
        FlowLayout.LayoutParams layoutParams = new FlowLayout.LayoutParams(
                FlowLayout.LayoutParams.WRAP_CONTENT, FlowLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams layoutParamsF = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, (int) (mContext.getResources().getDisplayMetrics().density*32));
        layoutParamsF.setMargins((int) (mContext.getResources().getDisplayMetrics().density*4), (int) (mContext.getResources().getDisplayMetrics().density*5),(int) (mContext.getResources().getDisplayMetrics().density*4), (int) (mContext.getResources().getDisplayMetrics().density*5));
        linearLayoutF.setLayoutParams(layoutParams);
        linearLayout.setLayoutParams(layoutParamsF);
        linearLayout.setPadding((int) (mContext.getResources().getDisplayMetrics().density*4), 0, (int) (mContext.getResources().getDisplayMetrics().density*4), 0);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayoutF.setOnClickListener(btnClickListener);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setBackgroundResource(R.drawable.primary_semiround_bg);
        PopinsRegularTextView bt = new PopinsRegularTextView(mContext);
        bt.setText(data);
        bt.setAllCaps(false);
        bt.setTag(data);
        bt.setMaxLines(1);
        bt.setEllipsize(TextUtils.TruncateAt.END);

        bt.setTextSize(12f);
        bt.setTextColor(getResources().getColor(R.color.white));
        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams1.setMargins((int) (mContext.getResources().getDisplayMetrics().density*6), 0, (int) (mContext.getResources().getDisplayMetrics().density*32), 0);
        bt.setLayoutParams(layoutParams1);
        ImageView imageView = new ImageView(mContext);
        imageView.setImageResource(R.drawable.ic_group_99);
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams2.setMargins(-(int) (mContext.getResources().getDisplayMetrics().density*26), 0, 0, 0);
        imageView.setLayoutParams(layoutParams2);
        imageView.getLayoutParams().height = (int) (mContext.getResources().getDisplayMetrics().density*22);
        imageView.getLayoutParams().width = (int) (mContext.getResources().getDisplayMetrics().density*22);

        linearLayout.addView(bt);
        linearLayout.addView(imageView);
        linearLayoutF.addView(linearLayout);
        linearLayoutF.setOnClickListener(btnClickListener);
        linearLayoutF.setTag(data);
        flowlay.addView(linearLayoutF);
        mArrayListdesiredindustries.add(data);
        mArrayListid.add(id);
        if (flowlay.getMeasuredHeight() > 400) {
            kdkdkdkd.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 424));
        } else
            kdkdkdkd.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    @SuppressLint("StaticFieldLeak")
    public class UploadFile extends AsyncTask<Object, String, String> {
        String file_name = "";
        Uri uri;
        @SuppressLint("StaticFieldLeak")
        Context context;

        public UploadFile(Uri uri, Context context) {
            this.uri = uri;
            this.context = context;
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
                URL url = new URL(ApiUrls.BASE_URL + "auth/user_cv_skill");
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
                outputStream.writeBytes("Content-Disposition: form-data; name=\"user_token\"" + lineEnd);
                outputStream.writeBytes(lineEnd);
                outputStream.writeBytes(Config.GetUserToken());
                outputStream.writeBytes(lineEnd);

                outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                outputStream.writeBytes("Content-Disposition: form-data; name=\"skill_ids\"" + lineEnd);
                outputStream.writeBytes(lineEnd);
                outputStream.writeBytes(mArrayListid.toString());
                outputStream.writeBytes(lineEnd);

                outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                outputStream.writeBytes("Content-Disposition: form-data; name=\"is_profile_updated\"" + lineEnd);
                outputStream.writeBytes(lineEnd);
                outputStream.writeBytes("Y");
                outputStream.writeBytes(lineEnd);

                outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                outputStream.writeBytes("Content-Disposition: form-data; name=\"user_cv\";filename=\"" + "Resume.pdf" + "\"" + lineEnd);
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
            AppController.dismissProgressdialog();
            JsonParser parser = new JsonParser();
            JsonObject json = (JsonObject) parser.parse(s);
            try {
                if (json != null) {
                    if (json.has("status"))
                        if (json.get("status").getAsBoolean()) {
                            Config.SetIsProfileUpdate("Y");
                            Config.setRemember(true);
                            Config.SetIsUserLogin(true);
                            Config.SetIsSeeker(true);
                            mContext.startActivity(new Intent(mContext, SeekerHomeActivity.class));
                            getActivity().finish();
                        }
                    if (json.has("message"))
                        Toast.makeText(context, json.get("message").getAsString(), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception ignored) {
            }


            super.onPostExecute(s);
        }
    }

}
