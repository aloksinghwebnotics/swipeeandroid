package com.webnotics.swipee.rest;


import com.google.gson.JsonObject;
import com.webnotics.swipee.model.ChatModel;
import com.webnotics.swipee.model.RecentChatModel;
import com.webnotics.swipee.model.TwillioDetailModel;
import com.webnotics.swipee.model.company.AppointmentDetailModel;
import com.webnotics.swipee.model.company.CompanyAppointmentModel;
import com.webnotics.swipee.model.company.CompanyProfileModel;
import com.webnotics.swipee.model.company.CompanyRaderViewModel;
import com.webnotics.swipee.model.company.CompanyUserListing;
import com.webnotics.swipee.model.company.PostedJobModel;
import com.webnotics.swipee.model.seeker.AppointmentModel;
import com.webnotics.swipee.model.seeker.CityModel;
import com.webnotics.swipee.model.seeker.EmployeeUserDetails;
import com.webnotics.swipee.model.seeker.FilterModel;
import com.webnotics.swipee.model.seeker.JobTypeModel;
import com.webnotics.swipee.model.seeker.NotificationModel;
import com.webnotics.swipee.model.seeker.SeekerAppointmentDetailModel;
import com.webnotics.swipee.model.seeker.StateModel;
import com.webnotics.swipee.model.seeker.UserJobListing;
import com.webnotics.swipee.model.seeker.UserRaderViewModel;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by  Developer
 */

public interface RestService {


    @Headers({
            "Accept: application/json",
            "Content-Type: application/x-www-form-urlencoded"
    })
    @FormUrlEncoded
    @POST(ApiUrls.URL_LOGIN)
    Call<JsonObject> loginUser(@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST(ApiUrls.URL_USERSOCIALLOGIN)
    Call<JsonObject> socialLoginUser(@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST(ApiUrls.URL_COMPANYLOGIN)
    Call<JsonObject> loginCompany(@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST(ApiUrls.URL_COMPANYSOCIALLOGIN)
    Call<JsonObject> socialLoginCompany(@FieldMap HashMap<String, String> hashMap);


    @FormUrlEncoded
    @POST(ApiUrls.URL_SIGNUP)
    Call<JsonObject> signUpUser(@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST(ApiUrls.URL_COMPANYREGISTER)
    Call<JsonObject> signUpCompany(@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST(ApiUrls.URL_VERIFYEMAILOTP)
    Call<JsonObject> verifyEmailOtp(@Field(ParaName.KEYTOKEN) String userToken, @Field(ParaName.KEY_EMAILOTP) String otp);

    @FormUrlEncoded
    @POST(ApiUrls.URL_COMPANYEMAILVERIFY)
    Call<JsonObject> verifyCompanyEmailOtp(@Field(ParaName.KEYTOKEN) String userToken, @Field(ParaName.KEY_EMAILOTP) String otp);

    @FormUrlEncoded
    @POST(ApiUrls.URL_EDITEMAIL)
    Call<JsonObject> verifyChangeEmail(@Field(ParaName.KEYTOKEN) String userToken, @Field(ParaName.KEY_EMAIL) String otp);

    @FormUrlEncoded
    @POST(ApiUrls.URL_COMPANYEMAILEDIT)
    Call<JsonObject> verifyChangeCompanyEmail(@Field(ParaName.KEYTOKEN) String userToken, @Field(ParaName.KEY_EMAIL) String otp);

    @FormUrlEncoded
    @POST(ApiUrls.URL_VERIFYMOBILEOTP)
    Call<JsonObject> verifyMobileOtp(@Field(ParaName.KEYTOKEN) String userToken, @Field(ParaName.KEY_MOBILEOTP) String otp);

    @FormUrlEncoded
    @POST(ApiUrls.URL_COMPANYMOBILEVERIFY)
    Call<JsonObject> verifyCompanyMobileOtp(@Field(ParaName.KEYTOKEN) String userToken, @Field(ParaName.KEY_MOBILEOTP) String otp);

    @FormUrlEncoded
    @POST(ApiUrls.URL_EDITEMOBILE)
    Call<JsonObject> verifyChangeMobile(@Field(ParaName.KEYTOKEN) String userToken, @Field(ParaName.KEY_MOBILENO) String mobile, @Field(ParaName.KEY_PHONECODE) String phone_code);

    @FormUrlEncoded
    @POST(ApiUrls.URL_COMPANYMOBILEEDIT)
    Call<JsonObject> verifyChangeCompanyMobile(@Field(ParaName.KEYTOKEN) String userToken, @Field(ParaName.KEY_MOBILENO) String mobile, @Field(ParaName.KEY_PHONECODE) String phone_code);

    @Multipart
    @POST(ApiUrls.URL_BASICINFO)
    Call<JsonObject> saveBasicInfo(@PartMap() Map<String, RequestBody> partMap, @Part MultipartBody.Part pick, @Part MultipartBody.Part video);

    @Multipart
    @POST(ApiUrls.URL_COMPANYINFOSAVE)
    Call<JsonObject> saveCompanyInfo(@PartMap() Map<String, RequestBody> partMap, @Part MultipartBody.Part pick, @Part MultipartBody.Part pick1/*,@Part("upload") RequestBody name*/);

    @GET(ApiUrls.URL_SKILLLIST)
    Call<JsonObject> getSkill();

    @GET(ApiUrls.URL_COLLEGELIST)
    Call<JsonObject> getCollege();

    @GET(ApiUrls.URL_DEGREELIST)
    Call<JsonObject> getDegree();

    @GET(ApiUrls.URL_LOACATIONLIST)
    Call<JsonObject> getLocation(@Query(ParaName.KEYTOKEN) String token);

    @GET(ApiUrls.URL_INDUSRTYLIST)
    Call<JsonObject> getDesiredIndustry(@Query(ParaName.KEYTOKEN) String token);

    @GET(ApiUrls.URL_STATELIST)
    Call<StateModel> getStateList(@Query(ParaName.KEYTOKEN) String token, @Query(ParaName.KEY_COUNTRYID) String stateId);

    @GET(ApiUrls.URL_CITYLIST)
    Call<CityModel> getCityList(@Query(ParaName.KEYTOKEN) String token, @Query(ParaName.KEY_STATEID) String stateId);

    @GET(ApiUrls.URL_USERACCDETAIL)
    Call<EmployeeUserDetails> userdetail(@Query(ParaName.KEYTOKEN) String token);

    @GET(ApiUrls.URL_LANGUAGE)
    Call<JsonObject> getLanguage(@Query(ParaName.KEYTOKEN) String token);

    @GET(ApiUrls.URL_JOBTYPE)
    Call<JobTypeModel> getJobType(@Query(ParaName.KEYTOKEN) String token);


    @GET(ApiUrls.URL_JOBTITLE)
    Call<JsonObject> getJobTitle();

    @GET(ApiUrls.URL_SALERYLIST)
    Call<JsonObject> getSalary();

    /////lohggggg
    @Multipart
    @POST(ApiUrls.URL_UPDATEPROFILE)
    Call<JsonObject> updateProfile(@PartMap() Map<String, RequestBody> partMap, @Part MultipartBody.Part pick, @Part MultipartBody.Part video);

    @FormUrlEncoded
    @POST(ApiUrls.URL_ADDLANGUAGE)
    Call<JsonObject> addLanguage(@Field(ParaName.KEYTOKEN) String userToken, @Field(ParaName.KEY_LANGIDS) String otp);

    @FormUrlEncoded
    @POST(ApiUrls.URL_ADDJOBTYPE)
    Call<JsonObject> addJobType(@Field(ParaName.KEYTOKEN) String userToken, @Field(ParaName.KEY_JOBTYPEID) String otp);

    @FormUrlEncoded
    @POST(ApiUrls.URL_ADDOBJECTIVE)
    Call<JsonObject> addObjective(@Field(ParaName.KEYTOKEN) String userToken, @Field(ParaName.KEY_OBJECTIVE) String otp);

    @FormUrlEncoded
    @POST(ApiUrls.URL_ADDSKILL)
    Call<JsonObject> addSkill(@Field(ParaName.KEYTOKEN) String userToken, @Field(ParaName.KEY_SKILL) String otp);

    @FormUrlEncoded
    @POST(ApiUrls.URL_ADDEDUCATION)
    Call<JsonObject> addEducation(@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST(ApiUrls.URL_ADDEXPERIENCE)
    Call<JsonObject> addWorExp(@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST(ApiUrls.URL_ADDJOBPREF)
    Call<JsonObject> addJobPreference(@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST(ApiUrls.URL_PACKAGELIST)
    Call<JsonObject> getPackageList(@Field(ParaName.KEYTOKEN) String token);

    @GET(ApiUrls.URL_COMPANYPACKAGELIST)
    Call<JsonObject> getCompanyPackageList(@Query(ParaName.KEYTOKEN) String token);

    @FormUrlEncoded
    @POST(ApiUrls.URL_CONTACTUS)
    Call<JsonObject> postContact(@Field(ParaName.KEYTOKEN) String token, @Field(ParaName.KEY_EMAIL) String email, @Field(ParaName.KEY_PHONECODE) String phoneCode,
                                 @Field(ParaName.KEY_MOBILENO) String mobileNo, @Field(ParaName.KEY_MESSAGE) String msg);

    @FormUrlEncoded
    @POST(ApiUrls.URL_COMPANYCONTACTUS)
    Call<JsonObject> postCompanyContact(@Field(ParaName.KEYTOKEN) String token, @Field(ParaName.KEY_EMAIL) String email, @Field(ParaName.KEY_PHONECODE) String phoneCode,
                                        @Field(ParaName.KEY_MOBILENO) String mobileNo, @Field(ParaName.KEY_MESSAGE) String msg);

    @GET(ApiUrls.URL_APPLIEDJOB)
    Call<JsonObject> getAppliedJobs(@Query(ParaName.KEYTOKEN) String token);

    @GET(ApiUrls.URL_FEATUREDJOB)
    Call<JsonObject> getFeaturedJobs(@Query(ParaName.KEYTOKEN) String token);

    @GET(ApiUrls.URL_SAVEDJOB)
    Call<JsonObject> getSavedJobs(@Query(ParaName.KEYTOKEN) String token);

    @GET(ApiUrls.URL_LIKEDJOB)
    Call<JsonObject> getLikedJobs(@Query(ParaName.KEYTOKEN) String token);

    @GET(ApiUrls.URL_LIKEDUSER)
    Call<JsonObject> getLikedUser(@Query(ParaName.KEYTOKEN) String token);

    @GET(ApiUrls.URL_SINGLEJOBDETAIL)
    Call<JsonObject> getSingleJobData(@Query(ParaName.KEYTOKEN) String token, @Query(ParaName.KEY_JOBPOSTID) String jobId, @Query(ParaName.KEY_LOGINTYPE) String logintype);

    @GET(ApiUrls.URL_USERDETAIL)
    Call<JsonObject> getSingleUserData(@Query(ParaName.KEYTOKEN) String token, @Query(ParaName.KEY_APLLYID) String applyId, @Query(ParaName.KEY_USERID) String userId);

    @FormUrlEncoded
    @POST(ApiUrls.URL_USERJOBSWIPE)
    Call<JsonObject> postJobAcceptReject(@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST(ApiUrls.URL_COMPANYSWIPE)
    Call<JsonObject> postUSerAcceptReject(@FieldMap HashMap<String, String> hashMap);

    @GET(ApiUrls.URL_USERHOMELISTING)
    Call<UserJobListing> getHomeList(@Query(ParaName.KEYTOKEN) String token);

    @GET(ApiUrls.URL_COMPANYHOMEDATA)
    Call<CompanyUserListing> getCompanyHomeList(@Query(ParaName.KEYTOKEN) String token);

    @GET(ApiUrls.URL_USERFILTER)
    Call<FilterModel> getFilterData(@Query(ParaName.KEYTOKEN) String token);

    @GET(ApiUrls.URL_COMPANYFILTER)
    Call<FilterModel> getCompanyFilterData(@Query(ParaName.KEYTOKEN) String token);

    @FormUrlEncoded
    @POST(ApiUrls.URL_POSTUSERFILTER)
    Call<UserJobListing> postFilterSearch(@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST(ApiUrls.URL_COMPANYSEARCH)
    Call<CompanyUserListing> postCompanyFilterSearch(@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST(ApiUrls.URL_SAVEJOB)
    Call<JsonObject> postSaveJob(@FieldMap HashMap<String, String> hashMap);

    @GET(ApiUrls.URL_USERAPPOINTMENTLIST)
    Call<AppointmentModel> getUserAppointmentData(@Query(ParaName.KEYTOKEN) String token, @Query(ParaName.KEY_ISTYPE) String KEY_ISTYPE);

    @GET(ApiUrls.URL_COMPANYAPOINTMENTLIST)
    Call<CompanyAppointmentModel> getCompanyAppointmentData(@Query(ParaName.KEYTOKEN) String token);

    @GET(ApiUrls.URL_USERNOTIFICATION)
    Call<NotificationModel> getUserNotification(@Query(ParaName.KEYTOKEN) String token);

    @GET(ApiUrls.URL_COMPANYNOTIFICATION)
    Call<NotificationModel> getCompanyNotification(@Query(ParaName.KEYTOKEN) String token);

    @GET(ApiUrls.URL_MATCHEDJOBLIST)
    Call<JsonObject> getMatchedJobList(@Query(ParaName.KEYTOKEN) String token);

    @GET(ApiUrls.URL_MATCHEDUSER)
    Call<JsonObject> getMatchedUserList(@Query(ParaName.KEYTOKEN) String token);

    @FormUrlEncoded
    @POST(ApiUrls.URL_USERRADAR)
    Call<UserRaderViewModel> getUserRadar(@Field(ParaName.KEYTOKEN) String token, @Field(ParaName.KEY_INDUSTRY) String industry, @Field(ParaName.KEY_RADIUSRANGE) String radius);

    @FormUrlEncoded
    @POST(ApiUrls.URL_COMPANYRADARSEARCH)
    Call<CompanyRaderViewModel> getCompanyRadar(@Field(ParaName.KEYTOKEN) String token, @Field(ParaName.KEY_FUNCTIONALID) String industry, @Field(ParaName.KEY_RADIUSRANGE) String radius);

    @FormUrlEncoded
    @POST(ApiUrls.URL_USERAPPLYJOB)
    Call<JsonObject> postApplyJob(@FieldMap HashMap<String, String> hashMap);


    @GET(ApiUrls.URL_COMPANYJOBLIST)
    Call<JsonObject> getCompanyJobList(@Query(ParaName.KEYTOKEN) String token, @Query(ParaName.KEY_COMPANYID) String company);

    @GET(ApiUrls.URL_COMPANYDETAIL)
    Call<CompanyProfileModel> companyDetail(@Query(ParaName.KEYTOKEN) String token);

    @GET(ApiUrls.URL_COMPANYDETAILFORSEEKER)
    Call<CompanyProfileModel> companyDetailById(@Query(ParaName.KEYTOKEN) String token, @Query(ParaName.KEY_COMPANYID) String cId);

    @GET(ApiUrls.URL_COMPANYSize)
    Call<JsonObject> companySize();

    @FormUrlEncoded
    @POST(ApiUrls.URL_USERLOGOUT)
    Call<JsonObject> userLogout(@Field(ParaName.KEYTOKEN) String token);

    @FormUrlEncoded
    @POST(ApiUrls.URL_COMPANYACTION)
    Call<JsonObject> companyAction(@Field(ParaName.KEYTOKEN) String token, @Field(ParaName.KEY_APLLYID) String applyId, @Field(ParaName.KEY_COMPANYACTION) String companyAction);

    @FormUrlEncoded
    @POST(ApiUrls.URL_COMPANYAPOINTMENTSLOT)
    Call<JsonObject> getAppointmentSlot(@Field(ParaName.KEYTOKEN) String token, @Field(ParaName.KEY_APPOINTMENTDATE) String date, @Field(ParaName.KEY_UID) String userId);

    @FormUrlEncoded
    @POST(ApiUrls.URL_CREATEAPPOINTMENT)
    Call<JsonObject> createAppointment(@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST(ApiUrls.URL_USERRESCHEDULEAPPOINTMENT)
    Call<JsonObject> rescheduleAppointment(@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST(ApiUrls.URL_APPOINTMENTSTATUS)
    Call<JsonObject> setAppointmentStatus(@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST(ApiUrls.URL_USERAPPOINTMENTSTATUS)
    Call<JsonObject> setUserAppointmentStatus(@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST(ApiUrls.URL_POSTEDJOBSTATUS)
    Call<JsonObject> setPostedStatus(@FieldMap HashMap<String, String> hashMap);

    @GET(ApiUrls.URL_JOBPOSTONE)
    Call<JsonObject> getPostJobStepOne(@Query(ParaName.KEYTOKEN) String token);

    @GET(ApiUrls.URL_JOBPOSTTWO)
    Call<JsonObject> getPostJobStepTwo(@Query(ParaName.KEYTOKEN) String token);

    @GET(ApiUrls.URL_JOBPOSTFIVE)
    Call<JsonObject> getPostJobStepFive(@Query(ParaName.KEYTOKEN) String token);


    @FormUrlEncoded
    @POST(ApiUrls.URL_SAVEJOBPOST)
    Call<JsonObject> savePostJob(@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST(ApiUrls.URL_RECENTCHAT)
    Call<RecentChatModel> getRecentChat(@Field(ParaName.KEYTOKEN) String token);

    @FormUrlEncoded
    @POST(ApiUrls.URL_RESENDMOBILE)
    Call<JsonObject> resendMobileOtp(@Field(ParaName.KEYTOKEN) String token, @Field(ParaName.KEY_MOBILENO) String mobile, @Field(ParaName.KEY_PHONECODE) String phonecode);

    @FormUrlEncoded
    @POST(ApiUrls.URL_VERIFYCOMPANYMOBILE)
    Call<JsonObject> companyMobileResend(@Field(ParaName.KEYTOKEN) String token, @Field(ParaName.KEY_MOBILENO) String mobile, @Field(ParaName.KEY_PHONECODE) String phonecode);


    @FormUrlEncoded
    @POST(ApiUrls.URL_RESENDEMAIL)
    Call<JsonObject> resendEmailOtp(@Field(ParaName.KEYTOKEN) String token, @Field(ParaName.KEY_EMAIL) String mobile);

    @FormUrlEncoded
    @POST(ApiUrls.URL_SEEKERFORGOTPASS)
    Call<JsonObject> forgotSeeker(@Field(ParaName.KEY_EMAIL) String mobile);

    @FormUrlEncoded
    @POST(ApiUrls.URL_RECRUITERFORGOTPASS)
    Call<JsonObject> forgotRecruiter(@Field(ParaName.KEY_EMAIL) String mobile);


    @GET(ApiUrls.URL_DELETEEDUCATION)
    Call<JsonObject> deleteEducation(@Query(ParaName.KEYTOKEN) String token, @Query(ParaName.KEY_USEREDUCATIONID) String educationId);


    @GET(ApiUrls.URL_DELETEEXPERIENCE)
    Call<JsonObject> deleteExperience(@Query(ParaName.KEYTOKEN) String token, @Query(ParaName.KEY_USEREXPID) String educationId);

    @FormUrlEncoded
    @POST(ApiUrls.URL_CANCELAPPLICATION)
    Call<JsonObject> cancelJobApplication(@Field(ParaName.KEYTOKEN) String token, @Field(ParaName.KEY_APLLYID) String applyId);

    @FormUrlEncoded
    @POST(ApiUrls.URL_BLOCKJOB)
    Call<JsonObject> blockJob(@Field(ParaName.KEYTOKEN) String token, @Field(ParaName.KEY_APLLYID) String applyId, @Field(ParaName.KEY_JOBID) String jobId);

    @FormUrlEncoded
    @POST(ApiUrls.URL_BLOCKUSER)
    Call<JsonObject> blockUser(@Field(ParaName.KEYTOKEN) String token, @Field(ParaName.KEY_UID) String uid);

    @FormUrlEncoded
    @POST(ApiUrls.URL_REPORTJOB)
    Call<JsonObject> reportJob(@Field(ParaName.KEYTOKEN) String token, @Field(ParaName.KEY_APLLYID) String applyId, @Field(ParaName.KEY_JOBID) String jobId, @Field(ParaName.KEY_REPORTEDISSUE) String issue);

    @FormUrlEncoded
    @POST(ApiUrls.URL_REPORTUSER)
    Call<JsonObject> reportUser(@Field(ParaName.KEYTOKEN) String token, @Field(ParaName.KEY_UID) String applyId, @Field(ParaName.KEY_REPORTEDISSUE) String issue);

    @GET(ApiUrls.URL_POSTEDJOBSBYFILTER)
    Call<PostedJobModel> postedJobs(@Query(ParaName.KEYTOKEN) String token, @Query(ParaName.KEY_FILTER) String filter);


    @FormUrlEncoded
    @POST(ApiUrls.URL_JOBPOSTRULE)
    Call<JsonObject> jobpostrule(@Field(ParaName.KEYTOKEN) String token, @Field(ParaName.KEY_JOBPOSTID) String id);

    @FormUrlEncoded
    @POST(ApiUrls.URL_PUBLISHJOB)
    Call<JsonObject> publishJob(@Field(ParaName.KEYTOKEN) String token, @Field(ParaName.KEY_JOBPOSTID) String id, @Field(ParaName.KEY_ISFEATURED) String is_featured);

    @FormUrlEncoded
    @POST(ApiUrls.URL_APPOINTMENTSLOTFORUSER)
    Call<JsonObject> getSeekerAppointmentSlot(@Field(ParaName.KEYTOKEN) String token, @Field(ParaName.KEY_APPOINTMENTDATE) String date, @Field(ParaName.KEY_COMPANYID) String cID);

    @FormUrlEncoded
    @POST(ApiUrls.URL_GETCHAT)
    Call<ChatModel> getChat(@Field(ParaName.KEYTOKEN) String token, @Field(ParaName.KEY_APPOINTMENTID) String id, @Field(ParaName.KEY_MSGID) String msg_id);

    @GET(ApiUrls.URL_SETMSGSEEN)
    Call<JsonObject> setMsgSeen(@QueryMap HashMap<String, String> hashMap);


    @Multipart
    @POST(ApiUrls.URL_SENDMSG)
    Call<JsonObject> sendChat(@PartMap() Map<String, RequestBody> partMap, @Part MultipartBody.Part pick);


    @FormUrlEncoded
    @POST(ApiUrls.URL_ORDERFEATUREJOB)
    Call<JsonObject> orderFeatureJob(@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST(ApiUrls.URL_PUBLISHFEATUREJOB)
    Call<JsonObject> publishFeaturedJob(@Field(ParaName.KEYTOKEN) String token, @Field(ParaName.KEY_JOBPOSTID) String id, @Field(ParaName.KEY_ISFEATURED) String is_featured);

    @GET(ApiUrls.URL_FEATUREDLIST)
    Call<JsonObject> featuredPlanList(@Query(ParaName.KEYTOKEN) String token);

    @GET(ApiUrls.URL_APPOINTMENTDETAIL)
    Call<AppointmentDetailModel> getAppointmentDetail(@Query(ParaName.KEYTOKEN) String token, @Query(ParaName.KEY_APPOINTMENTID) String id);

    @FormUrlEncoded
    @POST(ApiUrls.URL_STARTVEDIO)
    Call<TwillioDetailModel> getStartVideo(@Field(ParaName.KEYTOKEN) String token, @Field(ParaName.KEY_APPOINTMENTID) String id, @Field(ParaName.KEY_UID) String uid, @Field(ParaName.KEY_APPOINTMENTNUMBER) String appointmentno);

    @FormUrlEncoded
    @POST(ApiUrls.URL_SEEKERSTARTVEDIO)
    Call<TwillioDetailModel> getSeekerStartVideo(@Field(ParaName.KEYTOKEN) String token, @Field(ParaName.KEY_APPOINTMENTID) String id, @Field(ParaName.KEY_COMPANYID) String uid, @Field(ParaName.KEY_APPOINTMENTNUMBER) String appointmentno);

    @FormUrlEncoded
    @POST(ApiUrls.URL_STARTAUDIO)
    Call<TwillioDetailModel> getStartAudio(@Field(ParaName.KEYTOKEN) String token, @Field(ParaName.KEY_APPOINTMENTID) String id, @Field(ParaName.KEY_UID) String uid, @Field(ParaName.KEY_APPOINTMENTNUMBER) String appointmentno);

    @FormUrlEncoded
    @POST(ApiUrls.URL_SEEKERSTARTAUDIO)
    Call<TwillioDetailModel> getSeekerStartAudio(@Field(ParaName.KEYTOKEN) String token, @Field(ParaName.KEY_APPOINTMENTID) String id, @Field(ParaName.KEY_COMPANYID) String uid, @Field(ParaName.KEY_APPOINTMENTNUMBER) String appointmentno);

    @FormUrlEncoded
    @POST(ApiUrls.URL_USERREJECTVIDEOCALL)
    Call<JsonObject> userRejectVideo(@Field(ParaName.KEYTOKEN) String token, @Field(ParaName.KEY_APPOINTMENTID) String id);

    @FormUrlEncoded
    @POST(ApiUrls.URL_COMPANYREJECTVIDEOCALL)
    Call<JsonObject> companyRejectVideo(@Field(ParaName.KEYTOKEN) String token, @Field(ParaName.KEY_APPOINTMENTID) String id);

    @FormUrlEncoded
    @POST(ApiUrls.URL_USERREJECTAUDIOCALL)
    Call<JsonObject> userRejectAudio(@Field(ParaName.KEYTOKEN) String token, @Field(ParaName.KEY_APPOINTMENTID) String id);

    @FormUrlEncoded
    @POST(ApiUrls.URL_COMPANYREJECTAUDIOCALL)
    Call<JsonObject> companyRejectAudio(@Field(ParaName.KEYTOKEN) String token, @Field(ParaName.KEY_APPOINTMENTID) String id);

    @GET(ApiUrls.URL_SEEKERAPPOINTMENTDETAIL)
    Call<SeekerAppointmentDetailModel> getSeekerAppointmentDetail(@Query(ParaName.KEYTOKEN) String token, @Query(ParaName.KEY_APPOINTMENTID) String id);


    //Naushad

    @FormUrlEncoded
    @POST(ApiUrls.URL_EMPLOYER_ORDER_PACKAGE)
    Call<JsonObject> setRecruiterTransaction(@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST(ApiUrls.URL_USER_ORDER_PACKAGE)
    Call<JsonObject> setUserTransaction(@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST(ApiUrls.URL_PACKAGE_ORDERID)
    Call<JsonObject> getOrderId(@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST(ApiUrls.URL_FCMSETTING)
    Call<JsonObject> setNotificationSetting(@FieldMap HashMap<String, String> hashMap);
}

