package com.webnotics.swipee.payment;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.webnotics.swipee.R;
import com.webnotics.swipee.UrlManager.Config;

import org.json.JSONObject;

import java.util.HashMap;

public class Payment  {

    public void startPayment(Activity activity, String package_name,
                             int package_price) {

        final Checkout co = new Checkout();

        try {
            JSONObject options = new JSONObject();
            options.put("name", "Swipee");
            options.put("description", package_name);
            options.put("send_sms_hash", true);
            options.put("allow_rotation", true);
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "INR");
            options.put("amount", (package_price * 100));

            JSONObject preFill = new JSONObject();
            preFill.put("email", Config.GetEmail());
            preFill.put("contact", Config.GetMobileNo());

            options.put("prefill", preFill);

            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
    }
}
