package com.webnotics.swipee.fcm;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.webnotics.swipee.R;
import com.webnotics.swipee.UrlManager.Config;
import com.webnotics.swipee.activity.AppointmentDetail;
import com.webnotics.swipee.activity.CompanyProfile;
import com.webnotics.swipee.activity.NotificationActivity;
import com.webnotics.swipee.call.FirstVideoActivity;
import com.webnotics.swipee.activity.JobDetail;
import com.webnotics.swipee.activity.SplashScreen;
import com.webnotics.swipee.activity.NotificationAppointmentAction;
import com.webnotics.swipee.activity.company.UserDetail;
import com.webnotics.swipee.call.AudioActivity;
import com.webnotics.swipee.call.VideoActivity;
import com.webnotics.swipee.chat.MainChatActivity;
import com.webnotics.swipee.fragments.company.CompanyChatFragments;
import com.webnotics.swipee.rest.ParaName;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 *
 */
@SuppressLint("MissingFirebaseInstanceTokenRefresh")
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());

        if (Config.GetIsUserLogin()&& Config.isRemember()){
            if (NotificationActivity.instance!=null){
                NotificationActivity.instance.callService();
            }
        }
        if (!Config.isMute() && Config.GetIsUserLogin()&& Config.isRemember()){
            if (remoteMessage.getData().size() > 0) {
                Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

                try {
                    JSONObject json = new JSONObject(remoteMessage.getData().toString());
                    // JSONObject json = new JSONObject(remoteMessage.getData());
                    handleDataMessage(json);
                } catch (Exception e) {
                    Log.e(TAG, "Exception: " + e.getMessage());
                }
            }else   if (remoteMessage.getNotification() != null) {
                Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
                handleNotification(remoteMessage.getNotification().getBody(),remoteMessage.getNotification().getTitle());
            }
        }



    }
    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
            if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                for (String activeProcess : processInfo.pkgList) {
                    if (activeProcess.equals(context.getPackageName())) {
                        isInBackground = false;
                    }
                }
            }
        }

        return isInBackground;
    }
    private void handleNotification(String message,String title) {
        if (!isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent notifyIntent = new Intent(this, SplashScreen.class);
// Set the Activity to start in a new, empty task
            notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
// Create the PendingIntent
            PendingIntent notifyPendingIntent = PendingIntent.getActivity(
                    this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                    getApplicationContext());
            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
            String CHANNEL_ID = "Swipee";
            inboxStyle.addLine(message);
            NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            CharSequence name = "Swipee";// The user-visible name of the channel.
            int importance = NotificationManager.IMPORTANCE_HIGH;
            Notification notification;
            @SuppressLint("WrongConstant") NotificationChannel mChannel = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationManager.createNotificationChannel(mChannel);
            }
            notification = mBuilder.setSmallIcon(R.mipmap.ic_launcher).setTicker("Swipee").setWhen(0)
                    .setAutoCancel(true)
                    .setContentTitle(title)
                    .setChannelId(CHANNEL_ID)
                    .setStyle(inboxStyle)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(notifyPendingIntent)
                    .setContentText(message)
                    .build();

            notificationManager.notify(Config.NOTIFICATION_ID, notification);
        } else {
            // If the app is in background, firebase itself handles the notification
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void handleDataMessage(JSONObject json) {
        Log.e(TAG, "push json: " + json.toString());

        try {
            JSONObject data = json.getJSONObject("data");
            //  JSONObject data = json;

            String title = data.getString("title");
            String message = data.getString("message");
            String timestamp = data.getString("timestamp");

            JSONObject payload = data.getJSONObject("payload");
            // JSONObject payload = new JSONObject(data.getString("payload"));
            String notify_category = payload.getString("notify_category");

            //////////////

            ///////////////////
            Intent resultIntent;
            if (notify_category.equalsIgnoreCase("chat_push_message")) {
                resultIntent = new Intent(getApplicationContext(), MainChatActivity.class);
                resultIntent.putExtra("image", payload.getString("user_profile"));
                resultIntent.putExtra("apply_id", payload.getString("apply_id"));
                resultIntent.putExtra("msg_id", payload.getString("msg_sender_id"));
                resultIntent.putExtra("name",  !Config.isSeeker()?payload.getString("first_name"):payload.getString("company_name"));
                resultIntent.putExtra("appointment_id",  payload.getString("appointment_id"));
                resultIntent.putExtra("appointment_number", payload.getString("appointment_number"));
                resultIntent.putExtra("user_id", Config.isSeeker()?payload.getString("company_id"):payload.getString("user_id"));
                resultIntent.putExtra("job_id", "");
                if (CompanyChatFragments.instance!=null) CompanyChatFragments.instance.getRecentChat();
                if (MainChatActivity.instance!=null){
                    if (!MainChatActivity.instance.appointment_number.equalsIgnoreCase(payload.getString("appointment_number")))
                        showNotificationMessage(false,getApplicationContext(), title, message, timestamp, resultIntent);
                }else showNotificationMessage(false,getApplicationContext(), title, message, timestamp, resultIntent);

            }else if (notify_category.equalsIgnoreCase("disconnect_call")) {
                if (FirstVideoActivity.instance!=null)
                    FirstVideoActivity.instance.rejectCall(payload.getString("appointment_id"));
                showNotificationMessage(true,getApplicationContext(), title, message, timestamp, new Intent());

            }else if (notify_category.equalsIgnoreCase("company_reject_audio_call")) {
                if (AudioActivity.instance!=null){
                    AudioActivity.instance.rejectCall(payload.getString("appointment_id"));
                    showNotificationMessage(false,getApplicationContext(), title, message, timestamp, new Intent());
                }

            }else if (notify_category.equalsIgnoreCase("user_reject_audio_call")) {
                if (AudioActivity.instance!=null){
                    AudioActivity.instance.rejectCall(payload.getString("appointment_id"));
                    showNotificationMessage(false,getApplicationContext(), title, message, timestamp, new Intent());
                }

            } else if (notify_category.equalsIgnoreCase("user_reject_video_call")) {
                if (VideoActivity.instance!=null){
                    VideoActivity.instance.rejectCall(payload.getString("appointment_id"));
                    showNotificationMessage(false,getApplicationContext(), title, message, timestamp, new Intent());
                }

            } else if (notify_category.equalsIgnoreCase("company_swap_profile")) {
                if (TextUtils.isEmpty(payload.getString("job_id"))){
                    resultIntent = new Intent(getApplicationContext(), CompanyProfile.class);
                    resultIntent.putExtra("company_id", payload.getString("company_id"));
                }else{
                    resultIntent = new Intent(getApplicationContext(), JobDetail.class);
                    resultIntent.putExtra("id", payload.getString("job_id"));
                    resultIntent.putExtra("from", "Notification");
                }
                // check for image attachment
                showNotificationMessage(false,getApplicationContext(), title, message, timestamp, resultIntent);
            } else if (notify_category.equalsIgnoreCase("user_swap_job")) {
                resultIntent = new Intent(getApplicationContext(), UserDetail.class);
                resultIntent.putExtra("job_id", payload.getString("job_id"));
                resultIntent.putExtra("id", payload.getString("user_id"));
                resultIntent.putExtra("from", "Notification");
                resultIntent.putExtra("name", "");
                 showNotificationMessage(false,getApplicationContext(), title, message, timestamp, resultIntent);
            }else if (notify_category.equalsIgnoreCase("appointment_reminder")) {
                resultIntent = new Intent(getApplicationContext(), AppointmentDetail.class);
                resultIntent.putExtra(ParaName.KEY_APPOINTMENTID, payload.getString("appointment_id"));
                 showNotificationMessage(false,getApplicationContext(), title, message, timestamp, resultIntent);
            }else if (notify_category.equalsIgnoreCase("user_applied_job")) {
                    if (Config.isSeeker()){
                        resultIntent = new Intent(getApplicationContext(), JobDetail.class);
                        resultIntent.putExtra("id", payload.getString("job_id"));
                        resultIntent.putExtra("apply_id", payload.getString("apply_id"));
                        resultIntent.putExtra("from", "Notification");
                    }else {
                        resultIntent = new Intent(getApplicationContext(), UserDetail.class);
                        resultIntent.putExtra("job_id", payload.getString("job_id"));
                        resultIntent.putExtra("id", payload.getString("user_id"));
                        resultIntent.putExtra("apply_id", payload.getString("apply_id"));
                        resultIntent.putExtra("from", "Notification");
                        resultIntent.putExtra("name", "");
                    }
                showNotificationMessage(false,getApplicationContext(), title, message, timestamp, resultIntent);
            }else if (notify_category.equalsIgnoreCase("bulk_job_post_notification")) {
                resultIntent = new Intent(getApplicationContext(), JobDetail.class);
                resultIntent.putExtra("id", payload.getString("job_id"));
                resultIntent.putExtra("from", "Notification");
                // check for image attachment
                showNotificationMessage(false,getApplicationContext(), title, message, timestamp, resultIntent);
            } else if (notify_category.equalsIgnoreCase("company_change_appointment_status")) {
                if (payload.has("appointment_status") && payload.getString("appointment_status").equalsIgnoreCase("A")){
                    resultIntent = new Intent(getApplicationContext(), AppointmentDetail.class);
                    resultIntent.putExtra(ParaName.KEY_APPOINTMENTID, payload.getString("appointment_id"));
                }else {
                    resultIntent = new Intent(getApplicationContext(), JobDetail.class);
                    resultIntent.putExtra("id", payload.getString("job_id"));
                }
                resultIntent.putExtra("from", "Notification");
                showNotificationMessage(false,getApplicationContext(), title, message, timestamp, resultIntent);


            } else if (notify_category.equalsIgnoreCase("user_change_appointment_status")) {
                if (payload.has("appointment_status") && payload.getString("appointment_status").equalsIgnoreCase("A")){
                    resultIntent = new Intent(getApplicationContext(), AppointmentDetail.class);
                    resultIntent.putExtra(ParaName.KEY_APPOINTMENTID, payload.getString("appointment_id"));
                    resultIntent.putExtra("from", "Notification");
                    showNotificationMessage(false,getApplicationContext(), title, message, timestamp, resultIntent);
                }else {
                    resultIntent = new Intent(getApplicationContext(), UserDetail.class);
                    resultIntent.putExtra("job_id", payload.getString("job_id"));
                    resultIntent.putExtra("id", payload.getString("user_id"));
                    resultIntent.putExtra("apply_id", payload.getString("apply_id"));
                    resultIntent.putExtra("from", "Notification");
                    resultIntent.putExtra("name", payload.getString("first_name"));
                    showNotificationMessage(false,getApplicationContext(), title, message, timestamp, resultIntent);
                }


            }else if (notify_category.equalsIgnoreCase("employer_booked_appointment")) {
                String dtStart = payload.getString("appointment_start_time");
                String end = payload.getString("appointment_end_time");
                SimpleDateFormat format = new SimpleDateFormat("hh:mm");
                SimpleDateFormat formatout = new SimpleDateFormat("hh:mm aa");
                Date date,dateend;
                String datefinal="";
                try {
                    date = format.parse(dtStart);
                    dateend = format.parse(end);
                    String date1=  formatout.format(date);
                    String dateend1=  formatout.format(dateend);
                    datefinal= payload.getString("appointment_date")+ "  "+(date1+ " - "+dateend1).toUpperCase(Locale.ROOT);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                resultIntent = new Intent(getApplicationContext(), NotificationAppointmentAction.class);
                resultIntent.putExtra("company_id", payload.getString("company_id"));
                resultIntent.putExtra("company_logo", payload.getString("company_logo"));
                resultIntent.putExtra("appointment_type", payload.getString("appointment_type"));
                resultIntent.putExtra("user_id", payload.getString("user_id"));
                resultIntent.putExtra("company_name", payload.getString("company_name"));
                resultIntent.putExtra("posted_by", payload.getString("company_name"));
                resultIntent.putExtra("appointment_number", payload.getString("appointment_number"));
                resultIntent.putExtra("appointment_id", payload.getString("appointment_id"));
                resultIntent.putExtra("company_country_name", payload.getString("country_name"));
                resultIntent.putExtra("company_state_name", payload.getString("state_name"));
                resultIntent.putExtra("company_city_name", payload.getString("city_name"));
                resultIntent.putExtra("notify_number", payload.getString("unique_notify_number"));
                resultIntent.putExtra("job_id", payload.getString("job_id"));
                resultIntent.putExtra("is_own_job", "Y");
                resultIntent.putExtra("date", datefinal);
                // check for image attachment
                showNotificationMessage(false,getApplicationContext(), title, message, timestamp, resultIntent);
            }else if (notify_category.equalsIgnoreCase("user_booked_appointment")) {
                String dtStart = payload.getString("appointment_start_time");
                String end = payload.getString("appointment_end_time");
                SimpleDateFormat format = new SimpleDateFormat("hh:mm");
                SimpleDateFormat formatout = new SimpleDateFormat("hh:mm aa");
                Date date,dateend;
                String datefinal="";
                try {
                    date = format.parse(dtStart);
                    dateend = format.parse(end);
                    String date1=  formatout.format(date);
                    String dateend1=  formatout.format(dateend);
                    datefinal= payload.getString("appointment_date")+ "  "+(date1+ " - "+dateend1).toUpperCase(Locale.ROOT);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                resultIntent = new Intent(getApplicationContext(), NotificationAppointmentAction.class);
                resultIntent.putExtra("company_id", payload.getString("company_id"));
                resultIntent.putExtra("company_logo", payload.getString("user_profile"));
                resultIntent.putExtra("appointment_type", payload.getString("appointment_type"));
                resultIntent.putExtra("user_id", payload.getString("user_id"));
                resultIntent.putExtra("company_name", payload.getString("first_name")+" "+payload.getString("last_name"));
                resultIntent.putExtra("posted_by", payload.getString("first_name")+" "+payload.getString("last_name"));
                resultIntent.putExtra("appointment_number", payload.getString("appointment_number"));
                resultIntent.putExtra("appointment_id", payload.getString("appointment_id"));
                resultIntent.putExtra("company_country_name", payload.getString("country_name"));
                resultIntent.putExtra("company_state_name", payload.getString("state_name"));
                resultIntent.putExtra("company_city_name", payload.getString("city_name"));
                resultIntent.putExtra("notify_number", payload.getString("unique_notify_number"));
                resultIntent.putExtra("job_id","");
                resultIntent.putExtra("is_own_job", "Y");
                resultIntent.putExtra("date", datefinal);
                // check for image attachment
                showNotificationMessage(false,getApplicationContext(), title, message, timestamp, resultIntent);
            }else if (notify_category.equalsIgnoreCase("video_call")|| notify_category.equalsIgnoreCase("audio_call")) {
                if (AudioActivity.instance==null && VideoActivity.instance==null){
                    resultIntent = new Intent(getApplicationContext(), FirstVideoActivity.class);
                    resultIntent.putExtra("user_access_token", payload.getString("user_access_token"));
                    resultIntent.putExtra("appointment_type", payload.getString("appointment_type"));
                    resultIntent.putExtra("room_name", payload.getString("room_name"));
                    resultIntent.putExtra("user_name", payload.getString("user_name"));
                    resultIntent.putExtra("user_mobile", payload.getString("user_mobile"));
                    resultIntent.putExtra("user_phone_code", payload.getString("user_phone_code"));
                    resultIntent.putExtra("user_profile", payload.getString("user_profile"));
                    resultIntent.putExtra("Aid", payload.getString("appointment_id"));
                    resultIntent.putExtra("role_id", payload.getString("role_id"));
                    resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                        if (VideoActivity.instance==null && AudioActivity.instance==null)
                            startActivity(resultIntent);
                    }else {
                        showNotificationMessage(false,getApplicationContext(), title, message, timestamp, resultIntent);
                    }
                }

            }


        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    /**
     * Showing notification with text only
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showNotificationMessage(boolean isCancel,Context context, String title, String message, String timeStamp, Intent intent) {
        NotificationUtils notificationUtils = new NotificationUtils(context);
        notificationUtils.showNotificationMessage(isCancel,title, message, timeStamp, intent);
    }

    /**
     * Showing notification with text and image
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showNotificationMessageWithBigImage(boolean isCancel,Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        NotificationUtils notificationUtils = new NotificationUtils(context);
        notificationUtils.showNotificationMessage(isCancel,title, message, timeStamp, intent, imageUrl);
    }
}
