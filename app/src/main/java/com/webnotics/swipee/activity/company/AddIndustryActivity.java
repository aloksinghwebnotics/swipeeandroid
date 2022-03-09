package com.webnotics.swipee.activity.company;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.webnotics.swipee.R;
import com.webnotics.swipee.adapter.company.AddIndustryAdapter;
import com.webnotics.swipee.fragments.company.PostJobFragments;
import com.webnotics.swipee.model.company.CommonModel;
import com.webnotics.swipee.rest.Rest;

import java.util.ArrayList;

public class AddIndustryActivity extends AppCompatActivity implements View.OnClickListener {
    public String name = "";
    RecyclerView rv_stateList;
    ImageView iv_back;
    TextView tv_next, tv_title;
    Context mContext;
    Rest rest;
    public String id = "";
    EditText et_search;
    ArrayList<CommonModel> commonModels = new ArrayList<>();
    private AddIndustryAdapter stateAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_industry);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getColor(R.color.white));
        mContext = this;
        rest = new Rest(mContext);

        rv_stateList = findViewById(R.id.rv_stateList);
        tv_next = findViewById(R.id.tv_next);
        iv_back = findViewById(R.id.iv_back);
        et_search = findViewById(R.id.et_search);
        tv_title = findViewById(R.id.tv_title);

        iv_back.setOnClickListener(this);
        tv_next.setOnClickListener(this);
        if (getIntent() != null) {
            String from = getIntent().getStringExtra("from");
            if (from.equalsIgnoreCase("industry")) {
                tv_title.setText("Add Industry");
                if (PostJobFragments.instance != null)
                    if (PostJobFragments.instance.commonModels.size() > 0) {
                        commonModels.addAll(PostJobFragments.instance.commonModels);
                        rv_stateList.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                        stateAdapter = new AddIndustryAdapter(AddIndustryActivity.this, commonModels);
                        rv_stateList.setAdapter(stateAdapter);
                        rv_stateList.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
                    }
            } else if (from.equalsIgnoreCase("location")) {
                tv_title.setText("Add Location");
                if (PostJobFragments.instance != null)
                    if (PostJobFragments.instance.commonModelsLocation.size() > 0) {
                        commonModels.addAll(PostJobFragments.instance.commonModelsLocation);
                        rv_stateList.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                        stateAdapter = new AddIndustryAdapter(AddIndustryActivity.this, commonModels);
                        rv_stateList.setAdapter(stateAdapter);
                        rv_stateList.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
                    }
            }
        }

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (stateAdapter != null)
                    stateAdapter.getFilter().filter(s.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_next:

                if (TextUtils.isEmpty(id)) rest.showToast("Select State");
                else {
                    if (getIntent() != null) {
                        if (getIntent().getStringExtra("from").equalsIgnoreCase("industry")) {
                            if (PostJobFragments.instance != null)
                                PostJobFragments.instance.setIndustry(name, id);
                        } else if (getIntent().getStringExtra("from").equalsIgnoreCase("location")) {
                            if (PostJobFragments.instance != null)
                                PostJobFragments.instance.setLocation(name, id);
                        }
                        finish();
                    }


                }
                break;

            default:
                break;
        }

    }
}