package com.webnotics.swipee.UrlManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.webnotics.swipee.R;
import com.webnotics.swipee.activity.LoginActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppController extends Application {

    public static final String TAG = AppController.class
            .getSimpleName();

    public static Dialog progressdialog;
    public static boolean activityVisible;

    @SuppressLint("StaticFieldLeak")
    private static AppController mInstance;


    @SuppressLint("StaticFieldLeak")
    private static AppController instance;

    public static AppController get() {
        return instance;
    }

    @SuppressLint("StaticFieldLeak")
    public static Context mContext;
    public void showToast(final String text) {
        showToast(text, Toast.LENGTH_SHORT);
    }

    public void showToast(final String text, final int duration) {
        Log.d("TwilioApplication", text);
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();
            }
        });
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        Config.init(this);
        mContext    =   this;
        AppController.instance = this;

    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }





    public static void ShowDialogue(String message, Context mContext) {

        try {

            progressdialog = new Dialog(mContext);
            progressdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            progressdialog.setContentView(R.layout.progress_bar);
            progressdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            progressdialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            progressdialog.setCancelable(false);
            if(progressdialog!=null){
                progressdialog.show();
            }

        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }
    public static void ShowDialogueWhite(String message, Context mContext) {

        try {

            progressdialog = new Dialog(mContext);
            progressdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            progressdialog.setContentView(R.layout.progress_bar_white);
            progressdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            progressdialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            progressdialog.setCancelable(false);
            if(progressdialog!=null){
                progressdialog.show();
            }

        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }

    public static void dismissProgressdialog() {
        if (progressdialog != null && progressdialog.isShowing()) {
            try {
                progressdialog.dismiss();
            } catch (Exception ignored) {
            }
        }
    }


    public static String capitalize(String capString){
        StringBuffer capBuffer = new StringBuffer();
        Matcher capMatcher = Pattern.compile("([a-z])([a-z]*)", Pattern.CASE_INSENSITIVE).matcher(capString);
        while (capMatcher.find()){
            capMatcher.appendReplacement(capBuffer, Objects.requireNonNull(capMatcher.group(1)).toUpperCase() + Objects.requireNonNull(capMatcher.group(2)).toLowerCase());
        }

        return capMatcher.appendTail(capBuffer).toString();
    }

    public static void hidekeyboard(){
        InputMethodManager imm = (InputMethodManager)  mInstance.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }
    /* Returns true if url is valid */
    public static boolean isValid(String url)
    {
        /* Try creating a valid URL */
        try {
            new URL(url).toURI();
            return true;
        }

        // If there was an Exception
        // while creating URL object
        catch (Exception e) {
            return false;
        }
    }




    public static void callFullImage(Context mContext,String imgurl){
        Dialog mDIalog = new Dialog(mContext, android.R.style.Theme_Translucent_NoTitleBar);
        mDIalog.setContentView(R.layout.imagepreview);

        Window window = mDIalog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        mDIalog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ImageView mImage = mDIalog.findViewById(R.id.imageView3);
        ImageView mImageClose = mDIalog.findViewById(R.id.closeee);
        mImageClose.setOnClickListener(view1 -> mDIalog.dismiss());
        if (imgurl.length() != 0) {
            Glide.with(mContext)
                    .load(imgurl)
                    .into(mImage);
        }

        try {
            mDIalog.show();
        }catch (Exception ignored){}

    }

    public static void callResume(Context mContext, String imgurl){
        try {
            try {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(imgurl));
                browserIntent.setPackage("com.google.android.apps.docs");
                mContext.startActivity(browserIntent);
            }catch (Exception e){
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(imgurl));
                mContext.startActivity(browserIntent);
            }
        }catch (ActivityNotFoundException e){}

      /*  Dialog mDIalog = new Dialog(mContext, android.R.style.Theme_Translucent_NoTitleBar);
        mDIalog.setContentView(R.layout.pdfpreview);

        Window window = mDIalog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        mDIalog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        WebView webView = (WebView) mDIalog.findViewById(R.id.pdfView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setAllowFileAccessFromFileURLs(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.loadUrl("https://docs.google.com/gview?embedded=false&url=$"+imgurl);

        try {
            mDIalog.show();
        }catch (Exception ignored){}*/

    }
  /*  public static void callResume(Context mContext, String imgurl){
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

        AppController.mContext =mContext;
        ShowDialogue("",mContext);
        new DownloadFile().execute(imgurl);
    }*/
    public static void loggedOut(Context mContext){
        Intent intent = new Intent(mContext, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("EXIT", true);
        mContext.startActivity(intent);

    }


    public static class DownloadFile extends AsyncTask<String, Void, Integer> {
        String file_name = "";
        File sdcard = Environment.getExternalStorageDirectory();
        @Override
        protected Integer doInBackground(String... params) {
            // TODO Auto-generated method stub
            try {
                HttpURLConnection url_conn = null;
                byte[] bffr;

                long totalSize = 0;
                File directory = new File(
                        mContext.getFilesDir()
                                + "/xyz/download");
                directory.mkdirs();
                // 06-03 17:57:41.160: D/file name(6882):
                file_name = "";
                file_name = params[0];
                Log.d("file name", file_name);
                url_conn = (HttpURLConnection) (new URL(file_name)).openConnection();
                url_conn.setRequestMethod("GET");
                url_conn.setDoOutput(true);
                url_conn.connect();

                if (url_conn.getContentLength() > 0) {
                    File imgFile = new File(directory,"resume.pdf");
                    FileOutputStream fos = new FileOutputStream(imgFile);
                    InputStream is = url_conn.getInputStream();
                    totalSize = url_conn.getContentLength();
                    // Log.d("File Download Size ",totalSize+"");
                    long total = 0;
                    bffr = new byte[1024];
                    int bufferLength = 0;
                    while ((bufferLength = is.read(bffr)) > 0) {
                        total += bufferLength;
                        publishProgress("" + (int) ((total * 100) / totalSize));
                        fos.write(bffr, 0, bufferLength);
                    }
                    fos.close();
                } else
                    Log.w(file_name, "FILE NOT FOUND");
                return 0;
            } catch (Exception e) {
                e.printStackTrace();
                return -1;
            }

        }

        private void publishProgress(String... process) {
            // TODO Auto-generated method stub
        }

        protected void onPostExecute(Integer unused) {
            Log.d("after downloading file ", "file downloaded ");
            switch (unused) {
                case 0:
                    /*File directory = new File(
                            mContext.getFilesDir()
                                    + "/xyz/download");
                    Intent install = new Intent(Intent.ACTION_VIEW);
                    install.setDataAndType(Uri.fromFile(new File(directory,"resume")),
                            "MIME-TYPE");
                    install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                   AppController.mContext.startActivity(install);
*/
                    File directory = new File(
                            mContext.getFilesDir()
                                    + "/xyz/download/resume.pdf");

                    MimeTypeMap map = MimeTypeMap.getSingleton();
                    String ext = MimeTypeMap.getFileExtensionFromUrl(directory.getName());
                    String type = map.getMimeTypeFromExtension(ext);

                    if (type == null)
                        type = "*/*";

                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    Uri data = Uri.fromFile(directory);

                    intent.setDataAndType(data, type);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    AppController.mContext.startActivity(intent);
                    break;
            }
        }
    }
}
