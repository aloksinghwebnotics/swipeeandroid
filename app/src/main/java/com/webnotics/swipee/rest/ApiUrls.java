package com.webnotics.swipee.rest;

/**
 * Created by
 */

public class ApiUrls {

  public static final String BASE_URL = "https://swipee.in/app_api/";
  public static final String POLICY_URL = "https://swipee.in/pages/privacyPolicy";
  public static final String TERMS_URL = "https://swipee.in/pages/terms";

  //User Apis
  public static final String URL_LOGIN = "auth/user_login";
  public static final String URL_SIGNUP = "auth/user_register";
  public static final String URL_VERIFYEMAILOTP = "users/email_verify_otp";
  public static final String URL_VERIFYMOBILEOTP = "users/mobile_verify_otp";
  public static final String URL_EDITEMAIL = "users/user_edit_email";
  public static final String URL_EDITEMOBILE = "users/user_edit_mobile";
  public static final String URL_SKILLLIST = "helper/skills_list";
  public static final String URL_BASICINFO = "auth/user_register_profile_step_two";
  public static final String URL_USERACCDETAIL = "users/user_account_details";
  public static final String URL_STATELIST = "helper/state_list";
  public static final String URL_CITYLIST = "helper/city_list";
  public static final String URL_UPDATEPROFILE = "users/update_user_profile";
  public static final String URL_LANGUAGE = "helper/language_list";
  public static final String URL_JOBTYPE = "helper/jobtype_list";
  public static final String URL_ADDLANGUAGE = "users/user_add_update_languages";
  public static final String URL_ADDJOBTYPE = "users/user_add_update_jobtype";
  public static final String URL_ADDRESUME = "users/user_add_update_cv";
  public static final String URL_ADDOBJECTIVE= "users/user_add_update_objective";
  public static final String URL_ADDSKILL= "users/user_add_update_skills";
  public static final String URL_COLLEGELIST= "helper/college_university_list";
  public static final String URL_DEGREELIST= "helper/degree_list";
  public static final String URL_ADDEDUCATION= "users/user_add_update_education";
  public static final String URL_ADDEXPERIENCE= "users/user_add_update_experience";
  public static final String URL_JOBTITLE= "helper/functional_list";
  public static final String URL_SALERYLIST= "helper/salary_list";
  public static final String URL_LOACATIONLIST= "helper/prefer_location";
  public static final String URL_INDUSRTYLIST= "helper/industry_list";
  public static final String URL_ADDJOBPREF= "users/user_add_update_jobpreference";
  public static final String URL_PACKAGELIST= "packages/users_package_list";
  public static final String URL_CONTACTUS= "users/user_contact_us";
  public static final String URL_APPLIEDJOB= "employeejobs/user_applied_joblist";
  public static final String URL_SAVEDJOB= "employeejobs/user_saved_joblist";
  public static final String URL_LIKEDJOB= "employeejobs/user_accepted_job_listing";
  public static final String URL_SINGLEJOBDETAIL= "employeejobs/single_job_data";
  public static final String URL_USERJOBSWIPE= "users/user_job_swipe";
  public static final String URL_USERHOMELISTING= "employeejobs/user_home_job_listing";
  public static final String URL_USERFILTER= "helper/user_filters";
  public static final String URL_POSTUSERFILTER= "search/user_search";
  public static final String URL_SAVEJOB= "employeejobs/user_job_save";
  public static final String URL_USERAPPOINTMENTLIST= "appointments/user_appointment_listing";
  public static final String URL_USERNOTIFICATION= "users/user_notifications_list";
  public static final String URL_USERAPPLYJOB= "employeejobs/user_job_apply";
  public static final String URL_MATCHEDJOBLIST= "users/common_accepted_company_listing";
  public static final String URL_USERRADAR= "search/user_radar_search";
  public static final String URL_COMPANYJOBLIST= "employeejobs/company_posted_jobs";
  public static final String URL_USERLOGOUT= "users/logout";
  public static final String URL_FEATUREDJOB= "employeejobs/user_featured_jobs";
  public static final String URL_USERSOCIALLOGIN= "auth/user_social_login";
  public static final String URL_PACKAGE_ORDERID= "packages/create_package_ordernumber";

  /////
  public static final String URL_COMPANYLOGIN= "auth/company_login";
  public static final String URL_COMPANYSOCIALLOGIN= "auth/company_social_login";
  public static final String URL_COMPANYREGISTER= "auth/company_register";
  public static final String URL_COMPANYEMAILVERIFY= "companies/email_verify_otp";
  public static final String URL_COMPANYEMAILEDIT= "companies/company_edit_email";
  public static final String URL_COMPANYMOBILEVERIFY= "companies/mobile_verify_otp";
  public static final String URL_COMPANYMOBILEEDIT= "companies/company_edit_mobile";
  public static final String URL_COMPANYINFOSAVE= "companies/add_update_company_info";
  public static final String URL_COMPANYDETAIL= "companies/company_data";
  public static final String URL_COMPANYDETAILFORSEEKER= "employeejobs/recruiter_details";
  public static final String URL_COMPANYSize= "helper/company_employees_options";
  public static final String URL_COMPANYPACKAGELIST= "packages/employer_package_list";
  public static final String URL_COMPANYCONTACTUS= "companies/company_contact_us";
  public static final String URL_LIKEDUSER= "companies/company_accepted_users_listing";
  public static final String URL_MATCHEDUSER= "companies/common_accepted_users_listing";
  public static final String URL_USERDETAIL= "companies/single_user_data";
  public static final String URL_COMPANYACTION= "recruiterjobs/company_action";
  public static final String URL_COMPANYNOTIFICATION= "companies/company_notifications_list";
  public static final String URL_COMPANYAPOINTMENTLIST= "appointments/employer_appointment_listing";
  public static final String URL_COMPANYAPOINTMENTSLOT= "appointments/employer_appointment_open_slots";
  public static final String URL_CREATEAPPOINTMENT= "appointments/employer_book_appointment";
  public static final String URL_APPOINTMENTSTATUS= "appointments/company_appointment_status";
  public static final String URL_POSTEDJOBSTATUS= "companies/company_hide_posted_jobs";
  public static final String URL_COMPANYRADARSEARCH= "search/employer_radar_search";
  public static final String URL_COMPANYFILTER= "helper/employer_filters";
  public static final String URL_COMPANYHOMEDATA= "companies/company_users_listing";
  public static final String URL_COMPANYSEARCH= "search/employer_search";
  public static final String URL_COMPANYSWIPE = "companies/company_user_swipe";
  public static final String URL_JOBPOSTONE = "helper/employer_jobpost_stepone";
  public static final String URL_JOBPOSTTWO = "helper/employer_jobpost_steptwo";
  public static final String URL_JOBPOSTFIVE = "helper/employer_jobpost_stepfive";
  public static final String URL_SAVEJOBPOST = "recruiterjobs/save_posted_job";
  public static final String URL_RECENTCHAT = "chat/recentChats";
  public static final String URL_RESENDMOBILE = "users/verify_user_mobile";
  public static final String URL_RESENDEMAIL = "users/verify_user_email";
  public static final String URL_SEEKERFORGOTPASS = "users/user_forgot_password";
  public static final String URL_RECRUITERFORGOTPASS = "companies/company_forgot_password";
  public static final String URL_DELETEEDUCATION = "users/delete_user_education";
  public static final String URL_DELETEEXPERIENCE = "users/delete_work_experience";
  public static final String URL_CANCELAPPLICATION = "employeejobs/job_cancel_action";
  public static final String URL_BLOCKJOB = "employeejobs/job_block_action";
  public static final String URL_REPORTJOB = "employeejobs/job_report_action";
  public static final String URL_POSTEDJOBSBYFILTER = "companies/company_posted_jobs_filter";
  public static final String URL_JOBPOSTRULE = "recruiterjobs/show_job_post_rules";
  public static final String URL_PUBLISHJOB = "recruiterjobs/publish_posted_job";
  public static final String URL_GETCHAT = "chat/getMessages";
  public static final String URL_USERRESCHEDULEAPPOINTMENT = "appointments/user_book_appointment";
  public static final String URL_BLOCKUSER = "companies/user_block_action";
  public static final String URL_REPORTUSER = "companies/company_report_action";
  public static final String URL_SETMSGSEEN = "chat/setMsgSeen";
  public static final String URL_SENDMSG = "chat/sendMessage";
  public static final String URL_VERIFYCOMPANYMOBILE = "verify_company_mobile";
  public static final String URL_APPOINTMENTSLOTFORUSER = "appointments/company_appointment_open_slots";
  public static final String URL_USERAPPOINTMENTSTATUS = "appointments/user_appointment_status";
  public static final String URL_ORDERFEATUREJOB = "packages/employer_order_featuredjob";
  public static final String URL_FEATUREDLIST = "packages/employer_featuredpackage_list";
  public static final String URL_PUBLISHFEATUREJOB = "recruiterjobs/publish_featured_job";
  public static final String URL_STARTVEDIO = "appointments/company_start_video";
  public static final String URL_SEEKERSTARTVEDIO = "appointments/user_start_video";
  public static final String URL_APPOINTMENTDETAIL = "appointments/employer_appointment_detail";
  public static final String URL_SEEKERAPPOINTMENTDETAIL = "appointments/employee_appointment_detail";
  public static final String URL_USERREJECTVIDEOCALL = "appointments/user_reject_video";
  public static final String URL_COMPANYREJECTVIDEOCALL = "appointments/company_reject_video";
  public static final String URL_USERREJECTAUDIOCALL = "appointments/user_reject_audio";
  public static final String URL_COMPANYREJECTAUDIOCALL = "appointments/company_reject_audio";
  public static final String URL_STARTAUDIO = "appointments/company_start_audio";
  public static final String URL_SEEKERSTARTAUDIO = "appointments/user_start_audio";
  public static final String URL_FCMSETTING = "auth/fcm_settings";




  public static final String PAYPALURGMENT = "https://www.paypal.com/webapps/mpp/ua/useragreement-full";
  public static final String PAYPALPOLICY="https://www.paypal.com/webapps/mpp/ua/privacy-full";

    // Naushad
   public static final String URL_APPOINTMENT_ACCESS_TOKEN_CHAT = "appointments/chat_channel_users";
   //public static final String URL_APPOINTMENT_ACCESS_TOKEN_CHAT = "appointments/company_start_chat";
    public static final String URL_EMPLOYER_ORDER_PACKAGE= "packages/employer_order_package";
    public static final String URL_USER_ORDER_PACKAGE= "packages/user_order_package";



}