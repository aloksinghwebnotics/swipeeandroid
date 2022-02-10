package com.webnotics.swipee.activity.company;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.webnotics.swipee.R;
import com.webnotics.swipee.UrlManager.AppController;
import com.webnotics.swipee.UrlManager.Config;
import com.webnotics.swipee.adapter.company.FeaturedPlanAdapter;
import com.webnotics.swipee.rest.ApiUrls;
import com.webnotics.swipee.rest.Rest;
import com.webnotics.swipee.rest.SwipeeApiClient;

import java.math.BigDecimal;
import java.text.MessageFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeaturedPlan extends AppCompatActivity {
    public static final int PAYPAL_REQUEST_CODE = 123;
    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_PRODUCTION;
    private static final String CONFIG_CLIENT_ID =  Config.paypalconfig;


    private static final PayPalConfiguration config = new PayPalConfiguration()
            .environment(CONFIG_ENVIRONMENT).clientId(CONFIG_CLIENT_ID)
            // The following are only used in PayPalFuturePaymentActivity.
            .merchantName("E3DS8F2ZM3Z5U").merchantPrivacyPolicyUri(
                    Uri.parse(ApiUrls.PAYPALPOLICY))
            .merchantUserAgreementUri(Uri.parse(ApiUrls.PAYPALURGMENT));


    public String package_id="";
    public String package_name="";
    public int package_price=0;
    public int is_purchase=0;
    public String package_type="";
    public String post_limit="";
    private Rest rest;
    Context mContext;
    RecyclerView rv_plan;
    TextView tv_title,tv_amount,tv_period,tv_pay;
    private int job_post_id=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_featured_plan);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getColor(R.color.white));
        mContext=this;
        rest=new Rest(mContext);

        rv_plan=findViewById(R.id.rv_plan);
        tv_title=findViewById(R.id.tv_title);
        tv_amount=findViewById(R.id.tv_amount);
        tv_period=findViewById(R.id.tv_period);
        tv_pay=findViewById(R.id.tv_pay);

        if (getIntent() != null) {
            job_post_id = getIntent().getIntExtra("job_post_id", 0);
        }
        if (rest.isInterentAvaliable()){
            AppController.ShowDialogue("",mContext);
            callPlanList();
        }else rest.AlertForInternet();
    }


    private void callPlanList() {
        SwipeeApiClient.swipeeServiceInstance().featuredPlanList(Config.GetUserToken()).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                AppController.dismissProgressdialog();
                if (response.code() == 200 && response.body() != null) {
                    JsonObject responseBody = response.body();
                    boolean status= responseBody.has("status") && responseBody.get("status").getAsBoolean();
                    if (response.body().get("code").getAsInt()==203){
                        rest.showToast(response.body().get("message").getAsString());
                        AppController.loggedOut(mContext);
                        finish();
                    }else
                    if (response.body().get("code").getAsInt()==200 &&status){
                        JsonArray data=responseBody.has("data")?responseBody.get("data").getAsJsonArray():new JsonArray();
                        if (data.size()>0){
                            FeaturedPlanAdapter planAdapter=new FeaturedPlanAdapter(FeaturedPlan.this,data);
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
        this.is_purchase=is_purchase;
        this.package_id=package_id;
        this.package_price=package_price;
        this.package_name=package_name;
        this.package_type=package_type;
        this.post_limit=post_limit;

        tv_pay.setVisibility(View.VISIBLE);
        tv_title.setText(package_name);
        tv_period.setText("/month");
        tv_amount.setText(MessageFormat.format("Rs {0}", package_price));
        //tv_pay.setOnClickListener(this);

    }

    public void callPaypal(String price,String package_id){
        Intent intent = new Intent(mContext, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        mContext.startService(intent);
        PayPalPayment payment;
        //Creating a paypalpayment
        payment = new PayPalPayment(new BigDecimal(price), "INR",  30+" days duration",
                PayPalPayment.PAYMENT_INTENT_SALE);

        try {
            //Creating Paypal Payment activity intent
            intent = new Intent(mContext, PaymentActivity.class);

            //putting the paypal configuration to the intent
            intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

            //Puting paypal payment to the intent
            intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

            //Starting the intent activity for result
            //the request code will be used on the method onActivityResult
            startActivityForResult(intent, PAYPAL_REQUEST_CODE);
        }catch (Exception ignored){}
    }

//////call on payment done
    private void publishFeaturedJob(String id) {
        SwipeeApiClient.swipeeServiceInstance().publishFeaturedJob(Config.GetUserToken(), String.valueOf(job_post_id), "Y").enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                AppController.dismissProgressdialog();
                if (response.code() == 200 && response.body() != null) {
                    JsonObject respo = response.body();
                    if (respo != null)
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
}

