package com.webnotics.swipee.rest;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.util.Log;
import android.widget.Toast;


import com.webnotics.swipee.UrlManager.AppController;

/**
 * Created by  Developer
 */

public class

Rest {
    public Context ctx;
    public ProgressDialog dialog;

    public Rest(Context ctx) {
        this.ctx = ctx;
        init();
    }

    public static void printLog(String msg) {
        Log.e("The Lotter", msg);
    }



    private void init() {
        dialog = new ProgressDialog(ctx);
        dialog.setMessage("");
        dialog.setCancelable(false);

    }

    public  boolean isInterentAvaliable() {
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable() && cm.getActiveNetworkInfo().isConnected();
    }

    public  void AlertForInternet() {
        AlertDialog.Builder alert = new AlertDialog.Builder(ctx);
        alert.setMessage("Internet not available");
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dismissProgressdialog();
                AppController.dismissProgressdialog();
            }
        });
        alert.show();
    }

    public void ShowDialogue(String message) {
        dialog.setMessage(message);
        dialog.show();
    }

    public void dismissProgressdialog() {
        try {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        } catch (Exception e) {
            Rest.printLog("" + e);
        }
    }


    public void showToast(String msg) {
        Toast.makeText(ctx,msg,Toast.LENGTH_SHORT).show();
    }

}