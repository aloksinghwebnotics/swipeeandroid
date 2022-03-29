package com.webnotics.swipee.payment;

import android.app.Activity;
import android.widget.Toast;

import com.razorpay.Checkout;
import com.webnotics.swipee.UrlManager.Config;

import org.json.JSONObject;

public class Payment  {

    public void startPayment(Activity activity, String order_id, String package_name,
                             int package_price) {

        final Checkout co = new Checkout();

        try {
            JSONObject options = new JSONObject();
            options.put("name", "Swipee");
            options.put("description", package_name);
            options.put("send_sms_hash", true);
            options.put("allow_rotation", false);
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://swipee.in/app_data/public/images/applogo.png");
         //   options.put("currency", "INR");
           // options.put("amount", (package_price * 400));
            options.put("order_id", order_id);
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
