package com.webnotics.swipee.UrlManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;


/**
 * Created by Developer on 10/23/2021.
 */

public class Config {
    public  static  final  String paypalconfig  =   "AaKqob2gRAgUC6uCf9tHftRoG4z_W6j2PpqvUM4Myodfg5aiw1CRnr6g006XO5z7F5npQuXyMNFj8Y7P";
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

    public static final String AUSERID = "auserid";
    public static final String AID = "aid";
    public static final String IsProfileUpdate = "IsProfileUpdate";
    public static final String ANo = "ano";



    public static final String ID = "ID";
    public static final String NAME = "NAME";
    public static final String F_NAME = "F_NAME";
    public static final String DOB = "DOB";
    public static final String M_NAME = "M_NAME";
    public static final String L_NAME = "L_NAME";
    public static final String PROFILE = "PROFILE";
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
    public static final String JOBLOC = "JOBLOC";
    public static final String JOBLOCID = "JOBLOCID";
    public static final String JOBINDUSTRY = "JOBINDUSTRY";
    public static final String JOBINDUSTRYID = "JOBINDUSTRYID";
    public static final String TYPE = "TYPE";
    public static final String LANGUAGEEE = "LANGUAGEEE";
    public static final String PHONECODE1 = "PHONECODE1";
    public static final String PHONECODE2 = "PHONECODE2";
    public static final String COMPANYNAME = "COMPANYNAME";
    public static final String FILTERTYPE = "FILTERTYPE";
    // broadcast receiver intent filters
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";

    public static final String JOBTYPE = "JOBTYPE";
    public static final String LOCTYPE = "LOCTYPE";
    public static final String DISTANCETYPE = "DISTANCETYPE";
    public static final String DESIGNATIONTYPE = "DESIGNATION";

    public static final String QUALIFICATIONTYPE = "QUALIFICATIONTYPE";
    public static final String EXPERIENCETYPE = "EXPERIENCETYPE";
    public static final String INDUSTRYTYPE = "INDUSTRYTYPE";
    public static final String SALARYTYPE = "SALARYTYPE";


    public static final String JobLocationEmpId = "JobLocationEmpId";
    public static final String JobLocationEmpName = "JobLocationEmpName";
    public static final String JobTitleEmpId = "JobTitleEmpId";
    public static final String JobTitleEmpName = "JobTitleEmpName";

    public static final String SKILL = "SKILL";
    public static final String SKILLId = "SKILLId";

    public static final String CVID = "cv_id";
    public static final String CVTITLE = "SKILLId";
    public static final String CVFILE = "cv_file";
    public static final String CVFILELINK = "cv_file_link";
    public static final String PACKAGEEXP = "package_expire";
    public static final String PICKURI = "pick_uri";
    public static final String SocialLogin = "SocialLogin";
    public static final String REMEMBER = "InstaCarrier.remember";




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

    public static String GetCollegeRefreshDate(){
        return preference.getString(DATETIME, "");
    }

    public static void SetCollegeRefreshDate(String date) {
        editor.putString(DATETIME, date);
        editor.commit();
    }


    public static String GetLocationRefreshDate(){
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

    public static boolean isRemember(){
        return preference.getBoolean(REMEMBER, false);
    }

    public static String GetAUserId(){
        return preference.getString(AUSERID, null);
    }

    public static void SetAUserId(String id) {
        editor.putString(AUSERID, id);
        editor.commit();
    }

    public static String GetLat(){
        return preference.getString(Lat, "0");
    }

    public static void SetLat(String lat) {
        editor.putString(Lat, lat);
        editor.commit();
    }

    public static String GetPICKURI(){
        return preference.getString(PICKURI, "");
    }

    public static void SetPICKURI(String uri) {
        editor.putString(PICKURI, uri);
        editor.commit();
    }
    public static String GetLongg(){
        return preference.getString(Longg, "0");
    }

    public static void SetLongg(String longg) {
        editor.putString(Longg, longg);
        editor.commit();
    }
 public static int GetLeftPostCount(){
        return preference.getInt(LEFTPOST, 0);
    }

    public static void SetLeftPostCount(int count) {
        editor.putInt(LEFTPOST, count);
        editor.commit();
    }

    public static int GetLeftApplyCount(){
        return preference.getInt(LEFTAPPLY, 0);
    }

    public static void SetLeftApplyCount(int count) {
        editor.putInt(LEFTAPPLY, count);
        editor.commit();
    }
    public static boolean isSocialLogin(){
        return preference.getBoolean(SocialLogin, false);
    }

    public static void SetSocialLogin(boolean isSocialLogin) {
        editor.putBoolean(SocialLogin, isSocialLogin);
        editor.commit();
    }

    public static String GetRadiuss(){
        return preference.getString(Radiuss, null);
    }

    public static void SetRadiuss(String longg) {
        editor.putString(Radiuss, longg);
        editor.commit();
    }

    public static String GetAId(){
        return preference.getString(AID, null);
    }

    public static void SetAId(String id) {
        editor.putString(AID, id);
        editor.commit();
    }

    public static String GetIsProfileUpdate(){
        return preference.getString(IsProfileUpdate, null);
    }

    public static void SetIsProfileUpdate(String isProfileUpdate) {
        editor.putString(IsProfileUpdate, isProfileUpdate);
        editor.commit();
    }



    public static String GetAno (){
        return preference.getString(ANo, null);
    }

    public static void SetAno(String id) {
        editor.putString(ANo, id);
        editor.commit();
    }




    public static String GetLocationEmpId(){
        return preference.getString(JobLocationEmpId, null);
    }

    public static void SetLocationEmpId(String empid) {
        editor.putString(JobLocationEmpId, empid);
        editor.commit();
    }


    public static String GetCVID(){
        return preference.getString(CVID, "");
    }

    public static void SetCVID(String cvid) {
        editor.putString(CVID, cvid);
        editor.commit();
    }


    public static String GetCVTITLE(){
        return preference.getString(CVTITLE, null);
    }

    public static void SetCVTITLE(String cvtitle) {
        editor.putString(CVTITLE, cvtitle);
        editor.commit();
    }

    public static String GetCVFILE(){
        return preference.getString(CVFILE, null);
    }

    public static void SetCVFILE(String cvfile) {
        editor.putString(CVFILE, cvfile);
        editor.commit();
    }


    public static String GetCVFILELINK(){
        return preference.getString(CVFILELINK, null);
    }

    public static void SetCVFILELINK(String cvfilelink) {
        editor.putString(CVFILELINK, cvfilelink);
        editor.commit();
    }
 public static String GetPACKAGEEXP(){
        return preference.getString(PACKAGEEXP, "Y");
    }

    public static void SetPACKAGEEXP(String packageexp) {
        editor.putString(PACKAGEEXP, packageexp);
        editor.commit();
    }



    public static String GetLocationEmpName(){
        return preference.getString(JobLocationEmpName, null);
    }

    public static void SetLocationEmpName(String empname) {
        editor.putString(JobLocationEmpName, empname);
        editor.commit();
    }



    public static String GetJobTitileId(){
        return preference.getString(JobTitleEmpId, null);
    }

    public static void SetJobTitileId(String id) {
        editor.putString(JobTitleEmpId, id);
        editor.commit();
    }

    public static String GetJobTitileName(){
        return preference.getString(JobTitleEmpName, null);
    }

    public static void SetJobTitileName(String nm) {
        editor.putString(JobTitleEmpName, nm);
        editor.commit();
    }

    public static String GetIndustryType() {
        return preference.getString(INDUSTRYTYPE, null);
    }

    public static void SetIndustryType(String disype) {
        editor.putString(INDUSTRYTYPE, disype);
        editor.commit();
    }




    public static String GetSalaryType() {
        return preference.getString(SALARYTYPE, null);
    }

    public static void SetSalaryType(String disype) {
        editor.putString(SALARYTYPE, disype);
        editor.commit();
    }


    public static String GetDistanceType() {
        return preference.getString(DISTANCETYPE, null);
    }

    public static void SetDistanceType(String disype) {
        editor.putString(DISTANCETYPE, disype);
        editor.commit();
    }


    public static String GetQualificationType() {
        return preference.getString(QUALIFICATIONTYPE, null);
    }

    public static void SetQualificationType(String qype) {
        editor.putString(QUALIFICATIONTYPE, qype);
        editor.commit();
    }



    public static String GetExperienceType() {
        return preference.getString(EXPERIENCETYPE, null);
    }

    public static void SetExperienceType(String expype) {
        editor.putString(EXPERIENCETYPE, expype);
        editor.commit();
    }



    public static String GetDesignationType() {
        return preference.getString(DESIGNATIONTYPE, null);
    }

    public static void SetDesignationType(String disype) {
        editor.putString(DESIGNATIONTYPE, disype);
        editor.commit();
    }

    public static String GetJobType() {
        return preference.getString(JOBTYPE, null);
    }

    public static void SetJobType(String jobtype) {
        editor.putString(JOBTYPE, jobtype);
        editor.commit();
    }


    public static String GetLocType() {
        return preference.getString(LOCTYPE, null);
    }

    public static void SetLocType(String loctype) {
        editor.putString(LOCTYPE, loctype);
        editor.commit();
    }

    public static String GetFilterType() {
        return preference.getString(FILTERTYPE, null);
    }

    public static void SetFilterType(String filtertype) {
        editor.putString(FILTERTYPE, filtertype);
        editor.commit();
    }


    public static boolean isEmailValid( CharSequence email) {
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


    public static void SetMName(String mname) {
        editor.putString(M_NAME, mname);
        editor.commit();
    }
    public static String GetMName() {
        return preference.getString(M_NAME, null);
    }

    public static void SetLName(String lname) {
        editor.putString(L_NAME, lname);
        editor.commit();
    }
    public static String GetLName() {
        return preference.getString(L_NAME, "");
    }


    public static void SetProfile(String profile) {
        editor.putString(PROFILE, profile);
        editor.commit();
    }
    public static String GetProfile() {
        return preference.getString(PROFILE, null);
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

    public static void SetPhoneCode1(String phonecode1) {
        editor.putString(PHONECODE1, phonecode1);
        editor.commit();
    }
    public static String GetPhoneCode1() {
        return preference.getString(PHONECODE1, null);
    }

    public static void SetPhoneCode2(String phonecode2) {
        editor.putString(PHONECODE2, phonecode2);
        editor.commit();
    }
    public static String GetPhoneCode2() {
        return preference.getString(PHONECODE2, null);
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

    public static void SetType(String type) {
        editor.putString(TYPE, type);
        editor.commit();
    }
    public static String GetType() {
        return preference.getString(TYPE, null);
    }

    public static void SetJobLoc(String jobloc) {
        editor.putString(JOBLOC, jobloc);
        editor.commit();
    }
    public static String GetJobLoc() {
        return preference.getString(JOBLOC, null);
    }

    public static void SetJobLocId(String joblocid) {
        editor.putString(JOBLOCID, joblocid);
        editor.commit();
    }
    public static String GetJobLocId() {
        return preference.getString(JOBLOCID, null);
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

    public static void SetSkill(String skill) {
        editor.putString(SKILL, skill);
        editor.commit();
    }
    public static String GetSKill() {
        return preference.getString(SKILL, null);
    }


    public static void SetSkillId(String skill) {
        editor.putString(SKILLId, skill);
        editor.commit();
    }
    public static String GetSKillId() {
        return preference.getString(SKILLId, null);
    }


    public static void SetLang(String languagee) {
        editor.putString(LANGUAGEEE, languagee);
        editor.commit();
    }
    public static String GetLang() {
        return preference.getString(LANGUAGEEE, null);
    }


}
