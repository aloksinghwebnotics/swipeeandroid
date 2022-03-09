package com.webnotics.swipee.activity.company;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;
import com.webnotics.swipee.R;
import com.webnotics.swipee.UrlManager.AppController;
import com.webnotics.swipee.UrlManager.Config;
import com.webnotics.swipee.adapter.company.FeaturedPlanAdapter;
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

public class FeaturedPlan extends AppCompatActivity implements PaymentResultWithDataListener {

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
    private int job_post_id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_featured_plan);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getColor(R.color.white));
        mContext = this;
        rest = new Rest(mContext);

        rv_plan = findViewById(R.id.rv_plan);
        tv_title = findViewById(R.id.tv_title);
        tv_amount = findViewById(R.id.tv_amount);
        tv_period = findViewById(R.id.tv_period);
        tv_pay = findViewById(R.id.tv_pay);
        /*
         * Preload payment resources
         */
        Checkout.preload(mContext.getApplicationContext());

        if (getIntent() != null) job_post_id = getIntent().getIntExtra("job_post_id", 0);
        if (rest.isInterentAvaliable()) {
            AppController.ShowDialogue("", mContext);
            callPlanList();
        } else rest.AlertForInternet();

        tv_pay.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(package_id)) {
                AppController.ShowDialogue("", mContext);
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(ParaName.KEYTOKEN, Config.GetUserToken());
                hashMap.put(ParaName.KEY_PACKAGEPRICE, String.valueOf(package_price * 100));
                SwipeeApiClient.swipeeServiceInstance().getOrderId(hashMap).enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                        AppController.dismissProgressdialog();
                        if (response.code() == 200 && response.body() != null) {
                            if (response.body().get("code").getAsInt() == 203) {
                                rest.showToast(response.body().get("message").getAsString());
                                AppController.loggedOut(mContext);

                            } else if (response.body().get("code").getAsInt() == 200 && response.body().get("status").getAsBoolean()) {
                                JsonObject data = response.body().getAsJsonObject("data");
                                String order_id = data.has("order_id") ? data.get("order_id").getAsString() : "";
                                if (!TextUtils.isEmpty(order_id)) {
                                    Payment payment = new Payment();
                                    payment.startPayment(FeaturedPlan.this, order_id, package_name, package_price);
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        AppController.dismissProgressdialog();
                    }
                });
            }

        });
    }


    private void callPlanList() {
        SwipeeApiClient.swipeeServiceInstance().featuredPlanList(Config.GetUserToken()).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                AppController.dismissProgressdialog();
                if (response.code() == 200 && response.body() != null) {
                    JsonObject responseBody = response.body();
                    boolean status = responseBody.has("status") && responseBody.get("status").getAsBoolean();
                    if (response.body().get("code").getAsInt() == 203) {
                        rest.showToast(response.body().get("message").getAsString());
                        AppController.loggedOut(mContext);
                        finish();
                    } else if (response.body().get("code").getAsInt() == 200 && status) {
                        JsonArray data = responseBody.has("data") ? responseBody.get("data").getAsJsonArray() : new JsonArray();
                        if (data.size() > 0) {
                            FeaturedPlanAdapter planAdapter = new FeaturedPlanAdapter(FeaturedPlan.this, data);
                            rv_plan.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                            rv_plan.setNestedScrollingEnabled(false);
                            rv_plan.setAdapter(planAdapter);
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


    //////call on payment done
    private void publishFeaturedJob(String id) {
        AppController.ShowDialogue("", mContext);
        SwipeeApiClient.swipeeServiceInstance().publishFeaturedJob(Config.GetUserToken(), String.valueOf(job_post_id), "Y").enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                AppController.dismissProgressdialog();
                if (response.code() == 200 && response.body() != null) {
                    JsonObject respo = response.body();
                    if (respo.get("code").getAsInt() == 200 && respo.get("status").getAsBoolean()) {
                        rest.showToast(respo.get("message").getAsString());
                        startActivity(new Intent(mContext, CompanyHomeActivity.class).putExtra("from", JobPostRule.class.getSimpleName()));
                        finish();
                    } else if (respo.get("code").getAsInt() == 203) {
                        rest.showToast(respo.get("message").getAsString());
                        AppController.loggedOut(mContext);
                        finish();
                    } else {
                        rest.showToast(respo.get("message").getAsString());
                        startActivity(new Intent(mContext, CompanyHomeActivity.class).putExtra("from", JobPostRule.class.getSimpleName()));
                        finish();
                    }
                }


            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                AppController.dismissProgressdialog();
            }
        });
    }
    /*
     *//**
     * The name of the function has to be
     * onPaymentSuccess
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     *//*
    @SuppressWarnings("unused")
    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            Toast.makeText(this, "Payment Successful", Toast.LENGTH_SHORT).show();


                HashMap<String, String> hashMap = new HashMap();
                hashMap.put(ParaName.KEYTOKEN, Config.GetUserToken());
                hashMap.put(ParaName.KEY_TRANSACTIONID, razorpayPaymentID);
                hashMap.put(ParaName.KEY_PAYMENTSTATUS, "Completed");
                hashMap.put(ParaName.KEY_PACKAGETYPE, package_type);
                hashMap.put(ParaName.KEY_PACKAGENAME, package_name);
            hashMap.put(ParaName.KEY_ORDERID, paymentData.getOrderId());
            hashMap.put(ParaName.KEY_PAYSIGN, paymentData.getSignature());
                hashMap.put(ParaName.KEY_PACKAGEPRICE, String.valueOf(package_price));
                hashMap.put(ParaName.KEY_PACKAGEID, package_id);
                AppController.ShowDialogue("", mContext);
                SwipeeApiClient.swipeeServiceInstance().orderFeatureJob(hashMap).enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                        AppController.dismissProgressdialog();

                        if (response.code() == 200 && response.body() != null) {
                            JsonObject responseBody = response.body();
                            boolean status = responseBody.has("status") && responseBody.get("status").getAsBoolean();
                            if (response.body().get("code").getAsInt() == 203) {
                                rest.showToast(response.body().get("message").getAsString());
                                AppController.loggedOut(mContext);
                                finish();
                            } else if (response.body().get("code").getAsInt() == 200 && status) {
                                publishFeaturedJob(String.valueOf(job_post_id));
                            }

                        } else {
                            rest.showToast("Something went wrong");
                        }

                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        AppController.dismissProgressdialog();
                    }
                });

        } catch (Exception e) {
            Log.e("RazorPay", "Exception in onPaymentSuccess", e);
        }
    }

    */

    /**
     * The name of the function has to be
     * onPaymentError
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     *//*
    @SuppressWarnings("unused")
    @Override
    public void onPaymentError(int code, String response) {
        Log.d("RazorPay", "Exception in onPaymentError " + response);
        try {
            Toast.makeText(this, "Payment failed", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("RazorPay", "Exception in onPaymentError", e);
        }
    }*/
    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {
        try {

            HashMap<String, String> hashMap = new HashMap();
            hashMap.put(ParaName.KEY_TRANSACTIONID, paymentData.getPaymentId());
            hashMap.put(ParaName.KEY_PAYMENTSTATUS, "Completed");
            hashMap.put(ParaName.KEY_PACKAGETYPE, package_type);
            hashMap.put(ParaName.KEY_PACKAGENAME, package_name);
            hashMap.put(ParaName.KEY_ORDERID, paymentData.getOrderId());
            hashMap.put(ParaName.KEY_PAYSIGN, paymentData.getSignature());
            hashMap.put(ParaName.KEY_PACKAGEPRICE, String.valueOf(package_price));
            hashMap.put(ParaName.KEY_PACKAGEID, package_id);

            hashMap.put(ParaName.KEY_ISFEATURED, "Y");
            JSONObject jsonObject = new JSONObject(hashMap);
            Config.SetTransaction(jsonObject.toString());

            hashMap.put(ParaName.KEYTOKEN, Config.GetUserToken());
            hashMap.remove(ParaName.KEY_ISFEATURED);

            AppController.ShowDialogue("", mContext);
            SwipeeApiClient.swipeeServiceInstance().orderFeatureJob(hashMap).enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                    AppController.dismissProgressdialog();

                    if (response.code() == 200 && response.body() != null) {
                        JsonObject responseBody = response.body();
                        boolean status = responseBody.has("status") && responseBody.get("status").getAsBoolean();
                        if (response.body().get("code").getAsInt() == 203) {
                            rest.showToast(response.body().get("message").getAsString());
                            if (JobPostRule.instance!=null)
                                JobPostRule.instance.finish();
                            AppController.loggedOut(mContext);
                            finish();
                        } else if (response.body().get("code").getAsInt() == 200 && status) {
                            rest.showToast(response.body().get("message").getAsString());
                            Config.SetTransaction("");
                            if (JobPostRule.instance!=null)
                                JobPostRule.instance.finish();
                            publishFeaturedJob(String.valueOf(job_post_id));
                        } else if (response.body().get("code").getAsInt() == 402) {
                            Config.SetTransaction("");
                            rest.showToast(response.body().get("message").getAsString());
                        }

                    } else {
                        rest.showToast("Something went wrong");
                    }

                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    AppController.dismissProgressdialog();
                }
            });

        } catch (Exception e) {
            Log.e("RazorPay", "Exception in onPaymentSuccess", e);
        }

    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {

    }
}

