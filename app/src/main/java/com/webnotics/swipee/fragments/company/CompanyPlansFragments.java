package com.webnotics.swipee.fragments.company;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
import com.webnotics.swipee.adapter.company.CompanyPlanAdapter;
import com.webnotics.swipee.fragments.Basefragment;
import com.webnotics.swipee.rest.ApiUrls;
import com.webnotics.swipee.rest.Rest;
import com.webnotics.swipee.rest.SwipeeApiClient;

import java.math.BigDecimal;
import java.text.MessageFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CompanyPlansFragments extends Basefragment implements View.OnClickListener {
    //Paypal intent request code to track onActivityResult method
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.plan_screen, container, false);
        mContext=getActivity();
        rest=new Rest(mContext);
        if (rest.isInterentAvaliable()){
            AppController.ShowDialogue("",mContext);
            callPlanList();
        }else rest.AlertForInternet();

        rv_plan=rootView.findViewById(R.id.rv_plan);
        tv_title=rootView.findViewById(R.id.tv_title);
        tv_amount=rootView.findViewById(R.id.tv_amount);
        tv_period=rootView.findViewById(R.id.tv_period);
        tv_pay=rootView.findViewById(R.id.tv_pay);


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
                switch (view.getId()){
                    case R.id.tv_pay:

                        callPaypal(String.valueOf(package_price),package_id);
                        break;

                    default:break;
                }


    }

    private void callPlanList() {
        SwipeeApiClient.swipeeServiceInstance().getCompanyPackageList(Config.GetUserToken()).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                AppController.dismissProgressdialog();
                if (response.code() == 200 && response.body() != null) {
                    JsonObject responseBody = response.body();
                    boolean status= responseBody.has("status") && responseBody.get("status").getAsBoolean();
                    if (response.body().get("code").getAsInt()==203){
                        rest.showToast(response.body().get("message").getAsString());
                        AppController.loggedOut(mContext);
                        getActivity().finish();
                    }else
                    if (response.body().get("code").getAsInt()==200 &&status){
                        JsonArray data=responseBody.has("data")?responseBody.get("data").getAsJsonArray():new JsonArray();
                        if (data.size()>0){
                            CompanyPlanAdapter planAdapter=new CompanyPlanAdapter(CompanyPlansFragments.this,data);
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
        tv_pay.setOnClickListener(this);

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
}
