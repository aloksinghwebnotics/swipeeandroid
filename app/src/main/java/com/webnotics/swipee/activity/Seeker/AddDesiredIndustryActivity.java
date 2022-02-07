package com.webnotics.swipee.activity.Seeker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.webnotics.swipee.CustomUi.FlowLayout;
import com.webnotics.swipee.CustomUi.PopinsRegularTextView;
import com.webnotics.swipee.R;
import com.webnotics.swipee.UrlManager.AppController;
import com.webnotics.swipee.UrlManager.Config;
import com.webnotics.swipee.adapter.seeeker.AddSkillAdapter;
import com.webnotics.swipee.interfaces.AddSkillInterface;
import com.webnotics.swipee.model.AddSkillsModel;
import com.webnotics.swipee.rest.SwipeeApiClient;
import com.webnotics.swipee.rest.Rest;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddDesiredIndustryActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher, AddSkillInterface {
    Context mContext;
    Rest rest;
    private JsonObject responseBody = new JsonObject();
    ArrayList<AddSkillsModel> mArrayListSkills;
    ListView mListView;
    AddSkillAdapter skilladapter;
    FlowLayout flowlay;
    View.OnClickListener btnClickListener;
    AddSkillInterface addSkillsInterface;
    ArrayList<String> mArrayListdesiredindustries, mArrayListid, mArrayListName;
    AddSkillsModel model;
    EditText et_search;
    NestedScrollView kdkdkdkd;
    ImageView iv_back;
    TextView tv_save, tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_skill);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getColor(R.color.white));

        mContext = this;
        rest = new Rest(mContext);

        flowlay = findViewById(R.id.flowlayout);
        mListView = findViewById(R.id.mlistview);
        et_search = findViewById(R.id.et_search);
        kdkdkdkd = findViewById(R.id.kdkdkdkd);
        iv_back = findViewById(R.id.iv_back);
        tv_save = findViewById(R.id.tv_save);
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText("Add Industry");

        if (rest.isInterentAvaliable()) {
            AppController.ShowDialogue("", mContext);
            callDesIndustryList();
        } else {
            rest.AlertForInternet();
        }
        tv_save.setOnClickListener(this);
        iv_back.setOnClickListener(this);

        et_search.addTextChangedListener(this);
        mArrayListdesiredindustries = new ArrayList<>();
        mArrayListid = new ArrayList<>();
        mArrayListName = new ArrayList<>();
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
                    mArrayListName.remove(z);
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

    }

    private void callDesIndustryList() {
        SwipeeApiClient.swipeeServiceInstance().getDesiredIndustry(Config.GetUserToken()).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                AppController.dismissProgressdialog();
                if (response.code() == 200 && response.body() != null) {
                    responseBody = response.body();
                    if (response.body().get("code").getAsInt() == 203) {
                        rest.showToast(response.body().get("message").getAsString());
                        AppController.loggedOut(mContext);
                        finish();
                    } else if (response.body().get("code").getAsInt() == 200) {
                        JsonArray mArrayListData = responseBody.has("data") ? responseBody.get("data").getAsJsonArray() : new JsonArray();
                        for (int i = 0; i < mArrayListData.size(); i++) {
                            model = new AddSkillsModel();
                            model.setSkill_id(mArrayListData.get(i).getAsJsonObject().get("industry_id").getAsString());
                            model.setSkill_name(mArrayListData.get(i).getAsJsonObject().get("industry_name").getAsString());
                            model.setSelected(false);
                            mArrayListSkills.add(model);
                        }
                        skilladapter = new AddSkillAdapter(mContext, mArrayListSkills, addSkillsInterface);
                        mListView.setAdapter(skilladapter);
                    } else rest.showToast("Something went wrong");


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
        setSkillSelected(data, id);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_save:
                if (mArrayListid.size() > 3) {
                    rest.showToast("Please select maximum 3");
                } else if (mArrayListid.size() == 0) {
                    rest.showToast("Please select at least 1");
                } else {
                    mContext.startActivity(new Intent(mContext, AddJobPreferences.class).putExtra("from", "desired").putStringArrayListExtra("StringArrayList", mArrayListid)
                            .putStringArrayListExtra("StringArrayName", mArrayListName));
                }
                break;
            case R.id.iv_back:
                finish();
                break;

            default:
                break;
        }
    }

    private void setSkillSelected(String data, String id) {

        LinearLayout linearLayout = new LinearLayout(mContext);
        LinearLayout linearLayoutF = new LinearLayout(mContext);
        FlowLayout.LayoutParams layoutParams = new FlowLayout.LayoutParams(
                FlowLayout.LayoutParams.WRAP_CONTENT, FlowLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams layoutParamsF = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, (int) (mContext.getResources().getDisplayMetrics().density * 36));
        layoutParamsF.setMargins((int) (mContext.getResources().getDisplayMetrics().density * 4), (int) (mContext.getResources().getDisplayMetrics().density * 5), (int) (mContext.getResources().getDisplayMetrics().density * 4), (int) (mContext.getResources().getDisplayMetrics().density * 5));
        linearLayoutF.setLayoutParams(layoutParams);
        linearLayout.setLayoutParams(layoutParamsF);
        linearLayout.setPadding((int) (mContext.getResources().getDisplayMetrics().density * 4), 0, (int) (mContext.getResources().getDisplayMetrics().density * 4), 0);
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
        bt.setLayoutParams(layoutParams1);
        layoutParams1.setMargins((int) (mContext.getResources().getDisplayMetrics().density * 6), 0, (int) (mContext.getResources().getDisplayMetrics().density * 32), 0);
        bt.setLayoutParams(layoutParams1);
        ImageView imageView = new ImageView(mContext);
        imageView.setImageResource(R.drawable.ic_group_99);
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams2.setMargins(-(int) (mContext.getResources().getDisplayMetrics().density * 26), 0, 0, 0);
        imageView.setLayoutParams(layoutParams2);
        imageView.getLayoutParams().height = (int) (mContext.getResources().getDisplayMetrics().density * 22);
        imageView.getLayoutParams().width = (int) (mContext.getResources().getDisplayMetrics().density * 22);

        linearLayout.addView(bt);
        linearLayout.addView(imageView);
        linearLayoutF.addView(linearLayout);

        linearLayoutF.setOnClickListener(btnClickListener);
        linearLayoutF.setTag(data);
        flowlay.addView(linearLayoutF);
        mArrayListdesiredindustries.add(data);
        mArrayListid.add(id);
        mArrayListName.add(data);
        if (flowlay.getMeasuredHeight() > 400) {
            kdkdkdkd.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 424));
        } else
            kdkdkdkd.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }


}