package com.webnotics.swipee.fragments.seeker;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.webnotics.swipee.R;
import com.webnotics.swipee.UrlManager.AppController;
import com.webnotics.swipee.UrlManager.Config;
import com.webnotics.swipee.activity.Seeker.SeekerHomeActivity;
import com.webnotics.swipee.adapter.seeeker.PlanAdapter;
import com.webnotics.swipee.fragments.Basefragment;
import com.webnotics.swipee.payment.Payment;
import com.webnotics.swipee.rest.ParaName;
import com.webnotics.swipee.rest.Rest;
import com.webnotics.swipee.rest.SwipeeApiClient;

import org.json.JSONObject;

import java.text.MessageFormat;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PlansFragments extends Basefragment implements View.OnClickListener {

    public String package_id = "";
    public String package_name = "";
    public int package_price = 0;
    public int is_purchase = 0;
    public String package_type = "";
    public String post_limit = "";
    private Rest rest;
    Context mContext;
    RecyclerView rv_plan;
    TextView tv_title, tv_amount, tv_period, tv_pay;
    @SuppressLint("StaticFieldLeak")
    public static PlansFragments instance;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.plan_screen, container, false);
        mContext = getActivity();
        rest = new Rest(mContext);
        instance = this;

        if (rest.isInterentAvaliable()) {
            AppController.ShowDialogue("", mContext);
            callPlanList();
        } else rest.AlertForInternet();

        rv_plan = rootView.findViewById(R.id.rv_plan);
        tv_title = rootView.findViewById(R.id.tv_title);
        tv_amount = rootView.findViewById(R.id.tv_amount);
        tv_period = rootView.findViewById(R.id.tv_period);
        tv_pay = rootView.findViewById(R.id.tv_pay);

        tv_pay.setOnClickListener(this);

        /*
         * Preload payment resources
         */
        Checkout.preload(mContext.getApplicationContext());

        return rootView;
    }

    @Override
    public int setContentView() {
        return R.layout.plan_screen;
    }

    @Override
    protected void backPressed() {
        getActivity().finish();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.tv_pay) {
            if (Config.GetPACKAGEEXP().equalsIgnoreCase("N"))
            callPlanPopup();
            else callOrderID();

        }
    }

    private void callPlanPopup() {
        Dialog progressdialog = new Dialog(mContext);
        progressdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressdialog.setContentView(R.layout.cancel_appointment_popup);
        progressdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        progressdialog.getWindow().setAttributes(lp);
        TextView tv_title = progressdialog.findViewById(R.id.tv_title);
        TextView tv_detail = progressdialog.findViewById(R.id.tv_detail);
        tv_title.setText(getString(R.string.app_name));
        tv_detail.setText(getString(R.string.planmsg));

        progressdialog.findViewById(R.id.tv_yes).setOnClickListener(v -> {
            progressdialog.dismiss();
          callOrderID();


        });
        progressdialog.findViewById(R.id.tv_cancel).setOnClickListener(v -> progressdialog.dismiss());
        try {
            progressdialog.show();
        } catch (Exception e) {
        }

    }

    private void callOrderID() {
        if (!TextUtils.isEmpty(package_id)){
            AppController.ShowDialogue("",mContext);
            HashMap<String,String> hashMap=new HashMap<>();
            hashMap.put(ParaName.KEYTOKEN,Config.GetUserToken());
            hashMap.put(ParaName.KEY_PACKAGEPRICE, String.valueOf(package_price*100));
            SwipeeApiClient.swipeeServiceInstance().getOrderId(hashMap).enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                    AppController.dismissProgressdialog();
                    if (response.code()==200 && response.body()!=null){
                        if (response.body().get("code").getAsInt()==203){
                            rest.showToast(response.body().get("message").getAsString());
                            AppController.loggedOut(mContext);

                        }else if (response.body().get("code").getAsInt()==200 && response.body().get("status").getAsBoolean()){
                            JsonObject data=response.body().getAsJsonObject("data");
                            String order_id=data.has("order_id")?data.get("order_id").getAsString():"";
                            if (!TextUtils.isEmpty(order_id)){
                                Payment payment = new Payment();
                                payment.startPayment(getActivity(), order_id,package_name, package_price);
                            }

                        }

                    }
                }

                @Override
                public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                    AppController.dismissProgressdialog();
                }
            });

        }
    }


    public void setTransactionData(PaymentData paymentData) {

        HashMap<String, String> hashMap = new HashMap();

        hashMap.put(ParaName.KEY_TRANSACTIONID, paymentData.getPaymentId());
        hashMap.put(ParaName.KEY_ORDERID, paymentData.getOrderId());
        hashMap.put(ParaName.KEY_PAYSIGN, paymentData.getSignature());
        hashMap.put(ParaName.KEY_PACKAGETYPE, package_type);
        hashMap.put(ParaName.KEY_PAYMENTSTATUS, "Completed");
        hashMap.put(ParaName.KEY_PACKAGEPRICE, String.valueOf(package_price));
        hashMap.put(ParaName.KEY_PACKAGEID, package_id);

        hashMap.put(ParaName.KEY_ISFEATURED, "N");
        JSONObject jsonObject = new JSONObject(hashMap);
        Config.SetTransaction(jsonObject.toString());

        hashMap.put(ParaName.KEYTOKEN, Config.GetUserToken());
        hashMap.remove(ParaName.KEY_ISFEATURED);
       AppController.ShowDialogue("", mContext);
       callSavePayment(hashMap);
    }

    private void callSavePayment(HashMap<String, String> hashMap) {


        SwipeeApiClient.swipeeServiceInstance().setUserTransaction(hashMap).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull retrofit2.Response<JsonObject> response) {

                AppController.dismissProgressdialog();

                if (response.code() == 200 && response.body() != null) {
                    JsonObject responseBody = response.body();
                    boolean status = responseBody.has("status") && responseBody.get("status").getAsBoolean();
                    if (response.body().get("code").getAsInt() == 203) {
                        rest.showToast(response.body().get("message").getAsString());
                        AppController.loggedOut(mContext);
                        getActivity().finish();
                    } else if (response.body().get("code").getAsInt() == 200 && status) {
                        Config.SetTransaction("");
                        startActivity(new Intent(mContext, SeekerHomeActivity.class));
                    }else if (response.body().get("code").getAsInt() == 402) {
                        Config.SetTransaction("");
                        rest.showToast(response.body().get("message").getAsString());
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

    private void callPlanList() {
        SwipeeApiClient.swipeeServiceInstance().getPackageList(Config.GetUserToken()).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                AppController.dismissProgressdialog();
                if (response.code() == 200 && response.body() != null) {
                    JsonObject responseBody = response.body();
                    boolean status = responseBody.has("status") && responseBody.get("status").getAsBoolean();
                    if (response.body().get("code").getAsInt() == 203) {
                        rest.showToast(response.body().get("message").getAsString());
                        AppController.loggedOut(mContext);
                        getActivity().finish();
                    } else if (status) {
                        JsonObject data = responseBody.has("data") ? responseBody.get("data").getAsJsonObject() : new JsonObject();
                        JsonArray package_listing = data.has("package_listing") ? data.get("package_listing").getAsJsonArray() : new JsonArray();
                        if (package_listing.size() > 0) {
                            PlanAdapter planAdapter = new PlanAdapter(PlansFragments.this, package_listing);
                            rv_plan.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                            rv_plan.setNestedScrollingEnabled(false);
                            rv_plan.setAdapter(planAdapter);
                        }
                    }
                } else rest.showToast("Something went wrong");

            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {

                AppController.dismissProgressdialog();
            }
        });
    }

    public void setPlan(int is_purchase, int package_price, String package_id, String package_name, String package_type, String post_limit) {
        this.is_purchase = is_purchase;
        this.package_id = package_id;
        this.package_price = package_price;
        this.package_name = package_name;
        this.package_type = package_type;
        this.post_limit = post_limit;

        tv_pay.setVisibility(View.VISIBLE);
        tv_title.setText(package_name);
        tv_period.setText("/month");
        tv_amount.setText(MessageFormat.format("Rs {0}", package_price));

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        instance = null;
    }
}
