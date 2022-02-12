package com.webnotics.swipee.UrlManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;


/**
 * Created by Developer on 10/23/2021.
 */

public class Config {
    @SuppressLint("StaticFieldLeak")
    private static Context mContext;
    public static final String CONFIG_FILE = "Swipee";

    private static SharedPreferences preference = null;
    private static SharedPreferences.Editor editor;

    public static final String COUNTRY_NAME = "CONNAME";
    public static final String COUNTRY_ID = "CONID";

    public static final String STATE_NAME = "SNAME";
    public static final String STATE_ID = "SID";


    public static final String CITY_NAME = "CNAME";
    public static final String CITY_ID = "CID";

    public static final String IsProfileUpdate = "IsProfileUpdate";


    public static final String ID = "ID";
    public static final String NAME = "NAME";
    public static final String F_NAME = "F_NAME";
    public static final String DOB = "DOB";
    public static final String L_NAME = "L_NAME";
    public static final String EMAIL = "EMAIL";
    public static final String MOBILENO = "MOBILENO";
    public static final String PHONECODE = "PHONECODE";
    public static final String COUNTRY = "COUNTRY";
    public static final String Lat = "Lat";
    public static final String DATETIME = "DateTime";
    public static final String LOCATION = "LocationDb";
    public static final String Longg = "Longg";
    public static final String Radiuss = "Radiuss";
    public static final String GENDER = "GENDER";
    public static final String IS_SEEKER = "IS_SEEKER";
    public static final String IS_NOTIFICATION_MUTE = "IS_NOTIFICATION_MUTE";
    public static final String LEFTPOST = "left_post";
    public static final String LEFTAPPLY = "left_apply";


    public static final String STATE = "STATE";
    public static final String CITY = "CITY";
    public static final String EMAIL_VERIFY = "EMAIL_VERIFY";
    public static final String MOBILE_VERIFY = "MOBILE_VERIFY";
    public static final String USER_TOKEN = "USER_TOKEN";
    public static final String IS_USER_LOGIN = "IS_USER_LOGIN";
    public static final String JOBINDUSTRY = "JOBINDUSTRY";
    public static final String JOBINDUSTRYID = "JOBINDUSTRYID";
    public static final String COMPANYNAME = "COMPANYNAME";
    // broadcast receiver intent filters
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";

    public static final String CVID = "cv_id";
    public static final String CVTITLE = "SKILLId";
    public static final String CVFILE = "cv_file";
    public static final String CVFILELINK = "cv_file_link";
    public static final String PACKAGEEXP = "package_expire";
    public static final String PICKURI = "pick_uri";
    public static final String PACKAGEID = "package_iidd";
    public static final String SocialLogin = "SocialLogin";
    public static final String REMEMBER = "InstaCarrier.remember";
    public static final String TRANSACTION = "Swipee.transaction";


    // id to handle the notification in the notification tray
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;

    public static final String SHARED_PREF = "ah_firebase";

    public static void init(Context mContext) {
        Config.mContext = mContext;
        Config.preference = mContext.getSharedPreferences(CONFIG_FILE, Context.MODE_PRIVATE);
        Config.editor = preference.edit();
    }

    public static void clear(Context mContext) {
        Config.preference = mContext.getSharedPreferences(CONFIG_FILE, Context.MODE_PRIVATE);
        Config.editor = preference.edit();
        editor.clear();
        editor.commit();

    }


    public static void setMuteNotification(boolean mute) {
        editor.putBoolean(IS_NOTIFICATION_MUTE, mute);
        editor.commit();
    }

    public static boolean isMute() {
        return preference.getBoolean(IS_NOTIFICATION_MUTE, false);
    }

    public static String GetCollegeRefreshDate() {
        return preference.getString(DATETIME, "");
    }

    public static void SetCollegeRefreshDate(String date) {
        editor.putString(DATETIME, date);
        editor.commit();
    }


    public static String GetLocationRefreshDate() {
        return preference.getString(LOCATION, "");
    }

    public static void SetLocationRefreshDate(String loc) {
        editor.putString(LOCATION, loc);
        editor.commit();
    }


    public static void SetIsSeeker(boolean IsSeeker) {
        editor.putBoolean(IS_SEEKER, IsSeeker);
        editor.commit();
    }

    public static boolean isSeeker() {
        return preference.getBoolean(IS_SEEKER, true);
    }

    public static void setRemember(boolean remember) {
        editor.putBoolean(REMEMBER, remember);
        editor.commit();
    }

    public static boolean isRemember() {
        return preference.getBoolean(REMEMBER, false);
    }


    public static String GetLat() {
        return preference.getString(Lat, "0");
    }

    public static void SetLat(String lat) {
        editor.putString(Lat, lat);
        editor.commit();
    }

    public static String GetPackageId() {
        return preference.getString(PACKAGEID, "");
    }

    public static void SetPackageId(String uri) {
        editor.putString(PACKAGEID, uri);
        editor.commit();
    }

    public static void SetPICKURI(String uri) {
        editor.putString(PICKURI, uri);

    }

    public static String GetPICKURI() {
        return preference.getString(PICKURI, "");
    }


    public static String GetLongg() {
        return preference.getString(Longg, "0");
    }

    public static void SetLongg(String longg) {
        editor.putString(Longg, longg);
        editor.commit();
    }

    public static int GetLeftPostCount() {
        return preference.getInt(LEFTPOST, 0);
    }

    public static void SetLeftPostCount(int count) {
        editor.putInt(LEFTPOST, count);
        editor.commit();
    }

    public static int GetLeftApplyCount() {
        return preference.getInt(LEFTAPPLY, 0);
    }

    public static void SetLeftApplyCount(int count) {
        editor.putInt(LEFTAPPLY, count);
        editor.commit();
    }

    public static String GetRadiuss() {
        return preference.getString(Radiuss, null);
    }

    public static void SetRadiuss(String longg) {
        editor.putString(Radiuss, longg);
        editor.commit();
    }

    public static String GetIsProfileUpdate() {
        return preference.getString(IsProfileUpdate, null);
    }

    public static void SetIsProfileUpdate(String isProfileUpdate) {
        editor.putString(IsProfileUpdate, isProfileUpdate);
        editor.commit();
    }


    public static String GetCVID() {
        return preference.getString(CVID, "");
    }

    public static void SetCVID(String cvid) {
        editor.putString(CVID, cvid);
        editor.commit();
    }


    public static String GetCVTITLE() {
        return preference.getString(CVTITLE, null);
    }

    public static void SetCVTITLE(String cvtitle) {
        editor.putString(CVTITLE, cvtitle);
        editor.commit();
    }

    public static String GetCVFILE() {
        return preference.getString(CVFILE, null);
    }

    public static void SetCVFILE(String cvfile) {
        editor.putString(CVFILE, cvfile);
        editor.commit();
    }


    public static String GetCVFILELINK() {
        return preference.getString(CVFILELINK, null);
    }

    public static void SetCVFILELINK(String cvfilelink) {
        editor.putString(CVFILELINK, cvfilelink);
        editor.commit();
    }

    public static String GetPACKAGEEXP() {
        return preference.getString(PACKAGEEXP, "Y");
    }

    public static void SetPACKAGEEXP(String packageexp) {
        editor.putString(PACKAGEEXP, packageexp);
        editor.commit();
    }


    public static boolean isEmailValid(CharSequence email) {
        boolean b = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        return b;
    }

    public static void SetCountryName(String CountryName) {
        editor.putString(COUNTRY_NAME, CountryName);
        editor.commit();
    }


    public static String GetCountryName() {
        return preference.getString(COUNTRY_NAME, "India");
    }

    public static void SetCountryId(String CountryId) {
        editor.putString(COUNTRY_ID, CountryId);
        editor.commit();
    }

    public static String GetCountryId() {
        return preference.getString(COUNTRY_ID, "101");
    }


    public static String GeCompanyName() {
        return preference.getString(COMPANYNAME, "");
    }

    public static void SetCompanyName(String companyname) {
        editor.putString(COMPANYNAME, companyname);
        editor.commit();
    }


    public static void SetStateName(String StateName) {
        editor.putString(STATE_NAME, StateName);
        editor.commit();
    }

    public static String GetStateName() {
        return preference.getString(STATE_NAME, "");
    }

    public static void SetStateId(String StateId) {
        editor.putString(STATE_ID, StateId);
        editor.commit();
    }

    public static String GetStateId() {
        return preference.getString(STATE_ID, "");
    }

    public static void SetCityName(String CityName) {
        editor.putString(CITY_NAME, CityName);
        editor.commit();
    }

    public static String GetCityName() {
        return preference.getString(CITY_NAME, "");
    }

    public static void SetCityId(String CityId) {
        editor.putString(CITY_ID, CityId);
        editor.commit();
    }

    public static String GetCityId() {
        return preference.getString(CITY_ID, "");
    }


    public static void SetGender(String Gender) {
        editor.putString(GENDER, Gender);
        editor.commit();
    }

    public static String GetGender() {
        return preference.getString(GENDER, "");
    }


    public static void SetId(String id) {
        editor.putString(ID, id);
        editor.commit();
    }

    public static String GetId() {
        return preference.getString(ID, null);
    }


    public static void SetName(String name) {
        editor.putString(NAME, name);
        editor.commit();
    }

    public static String GetName() {
        return preference.getString(NAME, "");
    }


    public static void SetFName(String fname) {
        editor.putString(F_NAME, fname);
        editor.commit();
    }

    public static String GetFName() {
        return preference.getString(F_NAME, "");
    }

    public static void SetDob(String Dob) {
        editor.putString(DOB, Dob);
        editor.commit();
    }

    public static String GetDob() {
        return preference.getString(DOB, "");
    }


    public static void SetLName(String lname) {
        editor.putString(L_NAME, lname);
        editor.commit();
    }

    public static String GetLName() {
        return preference.getString(L_NAME, "");
    }


    public static void SetEmail(String email) {
        editor.putString(EMAIL, email);
        editor.commit();
    }

    public static String GetEmail() {
        return preference.getString(EMAIL, "");
    }


    public static void SetMobileNo(String mobileno) {
        editor.putString(MOBILENO, mobileno);
        editor.commit();
    }

    public static String GetMobileNo() {
        return preference.getString(MOBILENO, "");
    }


    public static void SetPhoneCode(String phonecode) {
        editor.putString(PHONECODE, phonecode);
        editor.commit();
    }

    public static String GetPhoneCode() {
        return preference.getString(PHONECODE, "+91");
    }

    public static void SetCountry(String country) {
        editor.putString(COUNTRY, country);
        editor.commit();
    }

    public static String GetCountry() {
        return preference.getString(COUNTRY, null);
    }

    public static void SetState(String state) {
        editor.putString(STATE, state);
        editor.commit();
    }

    public static String GetState() {
        return preference.getString(STATE, null);
    }

    public static void SetCity(String city) {
        editor.putString(CITY, city);
        editor.commit();
    }

    public static String GetCity() {
        return preference.getString(CITY, null);
    }


    public static void SetEmailVERIFY(boolean email_verify) {
        editor.putBoolean(EMAIL_VERIFY, email_verify);
        editor.commit();
    }

    public static boolean isEmailVERIFY() {
        return preference.getBoolean(EMAIL_VERIFY, false);
    }

    public static void SetMobileVERIFY(boolean mobile_verify) {
        editor.putBoolean(MOBILE_VERIFY, mobile_verify);
        editor.commit();
    }

    public static boolean GetMobileVERIFY() {
        return preference.getBoolean(MOBILE_VERIFY, false);
    }

    public static void SetUserToken(String utoken) {
        editor.putString(USER_TOKEN, utoken);
        editor.commit();
    }

    public static String GetUserToken() {
        return preference.getString(USER_TOKEN, "");
    }

    public static void SetIsUserLogin(boolean isuer) {
        editor.putBoolean(IS_USER_LOGIN, isuer);
        editor.commit();
    }

    public static boolean GetIsUserLogin() {
        return preference.getBoolean(IS_USER_LOGIN, false);
    }

    public static void SetIndustry(String jobindustry) {
        editor.putString(JOBINDUSTRY, jobindustry);
        editor.commit();
    }

    public static String GetIndustry() {
        return preference.getString(JOBINDUSTRY, null);
    }

    public static void SetIndustryId(String jobindustryid) {
        editor.putString(JOBINDUSTRYID, jobindustryid);
        editor.commit();
    }

    public static String GetIndustryId() {
        return preference.getString(JOBINDUSTRYID, null);
    }


    public static void SetTransaction(String data) {
        editor.remove(TRANSACTION);
        editor.putString(TRANSACTION, data);
        editor.commit();
    }

    public static String GetTransaction() {
        return preference.getString(TRANSACTION, null);
    }


}
