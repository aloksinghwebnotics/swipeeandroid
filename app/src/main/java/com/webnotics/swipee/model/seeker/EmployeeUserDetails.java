package com.webnotics.swipee.model.seeker;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class EmployeeUserDetails {
     boolean status;
     int code;
     String message;
    public Data data;
    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }


     public static   class Data {
         public UserProfileData user_profile_data;
         public User_Videos user_videos;
         public String user_carrier_objective;
         public String profile_image_status;
         public User_Resumes user_resumes;
         public ArrayList<User_Key_Skills> user_key_skills;
         public UserProfileData getUser_profile_data() {
             return user_profile_data;
         }

         public void setUser_profile_data(UserProfileData user_profile_data) {
             this.user_profile_data = user_profile_data;
         }

         public static class UserProfileData {
             String user_id;
             String first_name;

             public String getUser_id() {
                 return user_id;
             }

             public void setUser_id(String user_id) {
                 this.user_id = user_id;
             }

             public String getFirst_name() {
                 return first_name;
             }

             public void setFirst_name(String first_name) {
                 this.first_name = first_name;
             }

             public String getMiddle_name() {
                 return middle_name="";
             }

             public void setMiddle_name(String middle_name) {
                 this.middle_name = middle_name;
             }

             public String getLast_name() {
                 return last_name;
             }

             public void setLast_name(String last_name) {
                 this.last_name = last_name;
             }

             public String getUser_profile() {
                 return user_profile;
             }

             public void setUser_profile(String user_profile) {
                 this.user_profile = user_profile;
             }

             public String getEmail() {
                 return email;
             }

             public void setEmail(String email) {
                 this.email = email;
             }

             public String getMobile_no() {
                 return mobile_no;
             }

             public void setMobile_no(String mobile_no) {
                 this.mobile_no = mobile_no;
             }

             public String getPhone_code() {
                 return phone_code;
             }

             public void setPhone_code(String phone_code) {
                 this.phone_code = phone_code;
             }

             public String getCountry() {
                 return country;
             }

             public void setCountry(String country) {
                 this.country = country;
             }

             public String getState() {
                 return state;
             }

             public void setState(String state) {
                 this.state = state;
             }

             public String getCity() {
                 return city;
             }

             public void setCity(String city) {
                 this.city = city;
             }

             public String getUser_age() {
                 return user_age;
             }

             public void setUser_age(String user_age) {
                 this.user_age = user_age;
             }

             public String getUser_dob() {
                 return user_dob;
             }

             public void setUser_dob(String user_dob) {
                 this.user_dob = user_dob;
             }

             public String getWork_experience() {
                 return work_experience;
             }

             public void setWork_experience(String work_experience) {
                 this.work_experience = work_experience;
             }

             public String getGender() {
                 return gender;
             }

             public void setGender(String gender) {
                 this.gender = gender;
             }

             public String getEmail_verify() {
                 return email_verify;
             }

             public void setEmail_verify(String email_verify) {
                 this.email_verify = email_verify;
             }

             public String getMobile_verify() {
                 return mobile_verify;
             }

             public void setMobile_verify(String mobile_verify) {
                 this.mobile_verify = mobile_verify;
             }

             public String getUser_token() {
                 return user_token;
             }

             public void setUser_token(String user_token) {
                 this.user_token = user_token;
             }

             String middle_name;
             String last_name;
             String user_profile;
             String email;
             String mobile_no;
             String phone_code;
             String country;
             String state;
             String city;
             String user_age;
             String user_dob;
             String work_experience;
             String gender;
             String email_verify;
             String mobile_verify;
             String user_token;
             String city_id;
             String state_id;
             String user_video="";
             String latitude, longitude;

             public String getUser_video() {
                 return user_video;
             }

             public void setUser_video(String user_video) {
                 this.user_video = user_video;
             }

             public String getCity_id() {
                 return city_id;
             }

             public void setCity_id(String city_id) {
                 this.city_id = city_id;
             }

             public String getState_id() {
                 return state_id;
             }

             public void setState_id(String state_id) {
                 this.state_id = state_id;
             }

             public String getLatitude() {
                 return latitude;
             }

             public void setLatitude(String latitude) {
                 this.latitude = latitude;
             }

             public String getLongitude() {
                 return longitude;
             }

             public void setLongitude(String longitude) {
                 this.longitude = longitude;
             }

             public int getDefault_radius() {
                 return default_radius;
             }

             public void setDefault_radius(int default_radius) {
                 this.default_radius = default_radius;
             }

             int    default_radius;

         }

         public User_Videos getUser_videos() {
             return user_videos;
         }

         public void setUser_videos(User_Videos user_videos) {
             this.user_videos = user_videos;
         }



         public class User_Videos {
             public String getVideo_id() {
                 return video_id;
             }

             public void setVideo_id(String video_id) {
                 this.video_id = video_id;
             }

             public String getVideo_title() {
                 return video_title;
             }

             public void setVideo_title(String video_title) {
                 this.video_title = video_title;
             }

             public String getVideo_file() {
                 return video_file;
             }

             public void setVideo_file(String video_file) {
                 this.video_file = video_file;
             }

             public String getVideo_file_link() {
                 return video_file_link;
             }

             public void setVideo_file_link(String video_file_link) {
                 this.video_file_link = video_file_link;
             }

             String video_id, video_title, video_file, video_file_link;
         }

         public String getUser_carrier_objective() {
             return user_carrier_objective;
         }

         public void setUser_carrier_objective(String user_carrier_objective) {
             this.user_carrier_objective = user_carrier_objective;
         }



         public String getProfile_image_status() {
             return profile_image_status;
         }

         public void setProfile_image_status(String profile_image_status) {
             this.profile_image_status = profile_image_status;
         }

         public User_Resumes getUser_resumes() {
             return user_resumes;
         }

         public void setUser_resumes(User_Resumes user_resumes) {
             this.user_resumes = user_resumes;
         }



         public static class User_Resumes {
             String cv_id;
             String cv_title;
             String cv_file;
             String cv_file_link;
             public String getCv_id() {
                 return cv_id;
             }

             public void setCv_id(String cv_id) {
                 this.cv_id = cv_id;
             }

             public String getCv_title() {
                 return cv_title;
             }

             public void setCv_title(String cv_title) {
                 this.cv_title = cv_title;
             }

             public String getCv_file() {
                 return cv_file;
             }

             public void setCv_file(String cv_file) {
                 this.cv_file = cv_file;
             }

             public String getCv_file_link() {
                 return cv_file_link;
             }

             public void setCv_file_link(String cv_file_link) {
                 this.cv_file_link = cv_file_link;
             }


         }

         public ArrayList<User_Key_Skills> getUser_key_skills() {
             return user_key_skills;
         }

         public void setUser_key_skills(ArrayList<User_Key_Skills> user_key_skills) {
             this.user_key_skills = user_key_skills;
         }



         public class User_Key_Skills {
             public String getSkill_id() {
                 return skill_id;
             }

             public void setSkill_id(String skill_id) {
                 this.skill_id = skill_id;
             }

             public String getSkill_name() {
                 return skill_name;
             }

             public void setSkill_name(String skill_name) {
                 this.skill_name = skill_name;
             }

             String  skill_id, skill_name;


         }

         public ArrayList<User_Languages> getUser_languages() {
             return user_languages;
         }

         public void setUser_languages(ArrayList<User_Languages> user_languages) {
             this.user_languages = user_languages;
         }

         public ArrayList<User_Languages> user_languages;
         public static class User_Languages{

             public User_Languages(String language_id, String language_name) {
                 this.language_id = language_id;
                 this.language_name = language_name;
             }

             public String getLanguage_id() {
                 return language_id;
             }

             public void setLanguage_id(String language_id) {
                 this.language_id = language_id;
             }

             public String getLanguage_name() {
                 return language_name;
             }

             public void setLanguage_name(String language_name) {
                 this.language_name = language_name;
             }

             public String  language_id, language_name;
         }


         public ArrayList<User_Job_Type> getUser_job_type() {
             return user_job_types;
         }

         public void setUser_job_type(ArrayList<User_Job_Type> user_job_types) {
             this.user_job_types = user_job_types;
         }

         public ArrayList<User_Job_Type> user_job_types;
         public static class User_Job_Type{

             public User_Job_Type(String job_type_id, String job_type_name) {
                 this.job_type_id = job_type_id;
                 this.job_type_name = job_type_name;
             }

             public String getJob_type_id() {
                 return job_type_id;
             }

             public void setJob_type_id(String job_type_id) {
                 this.job_type_id = job_type_id;
             }

             public String getJob_type_name() {
                 return job_type_name;
             }

             public void setJob_type_name(String job_type_name) {
                 this.job_type_name = job_type_name;
             }

             public String  job_type_id, job_type_name;

             }

         public ArrayList<UserWorkExperience> getUser_work_experience() {
             return user_work_experience;
         }

         public void setUser_work_experience(ArrayList<UserWorkExperience> user_work_experience) {
             this.user_work_experience = user_work_experience;
         }

         public ArrayList<UserWorkExperience> user_work_experience;
        public static class  UserWorkExperience implements Parcelable {

            public UserWorkExperience(Parcel in) {
                user_experience_id = in.readString();
                company_name = in.readString();
                experience_title = in.readString();
                work_from = in.readString();
                work_to = in.readString();
                currently_working = in.readString();
                description = in.readString();
                start_date = in.readString();
                end_date = in.readString();
                functional_id = in.readString();
            }



            public static final Creator<UserWorkExperience> CREATOR = new Creator<UserWorkExperience>() {
                @Override
                public UserWorkExperience createFromParcel(Parcel in) {
                    return new UserWorkExperience(in);
                }

                @Override
                public UserWorkExperience[] newArray(int size) {
                    return new UserWorkExperience[size];
                }
            };

            public String getUser_experience_id() {
                return user_experience_id;
            }

            public void setUser_experience_id(String user_experience_id) {
                this.user_experience_id = user_experience_id;
            }

            public String getCompany_name() {
                return company_name;
            }

            public void setCompany_name(String company_name) {
                this.company_name = company_name;
            }

            public String getExperience_title() {
                return experience_title;
            }

            public void setExperience_title(String experience_title) {
                this.experience_title = experience_title;
            }

            public String getWork_from() {
                return work_from;
            }

            public void setWork_from(String work_from) {
                this.work_from = work_from;
            }

            public String getWork_to() {
                return work_to;
            }

            public void setWork_to(String work_to) {
                this.work_to = work_to;
            }

            public String getCurrently_working() {
                return currently_working;
            }

            public void setCurrently_working(String currently_working) {
                this.currently_working = currently_working;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String  user_experience_id;
            public String company_name;
            public String experience_title;
            public String work_from;
            public String work_to;
            public String currently_working;
            public String description;

            public String getFunctional_id() {
                return functional_id;
            }

            public void setFunctional_id(String functional_id) {
                this.functional_id = functional_id;
            }

            public  String functional_id;

            public String getStart_date() {
                return start_date;
            }

            public void setStart_date(String start_date) {
                this.start_date = start_date;
            }

            public String getEnd_date() {
                return end_date;
            }

            public void setEnd_date(String end_date) {
                this.end_date = end_date;
            }

            public String start_date;
            public String end_date;

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel parcel, int i) {
                parcel.writeString(user_experience_id);
                parcel.writeString(company_name);
                parcel.writeString(experience_title);
                parcel.writeString(work_from);
                parcel.writeString(work_to);
                parcel.writeString(currently_working);
                parcel.writeString(description);
                parcel.writeString(start_date);
                parcel.writeString(end_date);
                parcel.writeString(functional_id);

            }

            public UserWorkExperience(String user_experience_id, String company_name, String experience_title, String work_from, String work_to, String currently_working, String description, String functional_id, String start_date, String end_date) {
                this.user_experience_id = user_experience_id;
                this.company_name = company_name;
                this.experience_title = experience_title;
                this.work_from = work_from;
                this.work_to = work_to;
                this.currently_working = currently_working;
                this.description = description;
                this.functional_id = functional_id;
                this.start_date = start_date;
                this.end_date = end_date;
            }
        }

         public ArrayList<User_Eductaion> getUser_eductaion() {
             return user_eductaion;
         }

         public void setUser_eductaion(ArrayList<User_Eductaion> user_eductaion) {
             this.user_eductaion = user_eductaion;
         }

         public ArrayList<User_Eductaion> user_eductaion;
         public static class User_Eductaion implements  Parcelable{
                     String  user_education_id;




             public User_Eductaion(Parcel in) {
                 user_education_id = in.readString();
                 college_university_name = in.readString();
                 degree_level = in.readString();
                 degree_name = in.readString();
                 from = in.readString();
                 to = in.readString();
                 currently_pursuing = in.readString();
                 college_university_id = in.readString();
                 degree_level_id = in.readString();
                 degree_type_id = in.readString();
                 start_date = in.readString();
                 end_date = in.readString();
             }

             @Override
             public void writeToParcel(Parcel dest, int flags) {
                 dest.writeString(user_education_id);
                 dest.writeString(college_university_name);
                 dest.writeString(degree_level);
                 dest.writeString(degree_name);
                 dest.writeString(from);
                 dest.writeString(to);
                 dest.writeString(currently_pursuing);
                 dest.writeString(college_university_id);
                 dest.writeString(degree_level_id);
                 dest.writeString(degree_type_id);
                 dest.writeString(start_date);
                 dest.writeString(end_date);
             }

             @Override
             public int describeContents() {
                 return 0;
             }

             public static final Creator<User_Eductaion> CREATOR = new Creator<User_Eductaion>() {
                 @Override
                 public User_Eductaion createFromParcel(Parcel in) {
                     return new User_Eductaion(in);
                 }

                 @Override
                 public User_Eductaion[] newArray(int size) {
                     return new User_Eductaion[size];
                 }
             };

             public String getUser_education_id() {
                return user_education_id;
            }

            public void setUser_education_id(String user_education_id) {
                this.user_education_id = user_education_id;
            }

            public String getCollege_university_name() {
                return college_university_name;
            }

            public void setCollege_university_name(String college_university_name) {
                this.college_university_name = college_university_name;
            }

            public String getDegree_level() {
                return degree_level;
            }

            public void setDegree_level(String degree_level) {
                this.degree_level = degree_level;
            }

            public String getDegree_name() {
                return degree_name;
            }

            public void setDegree_name(String degree_name) {
                this.degree_name = degree_name;
            }

            public String getFrom() {
                return from;
            }

            public void setFrom(String from) {
                this.from = from;
            }

            public String getTo() {
                return to;
            }

            public void setTo(String to) {
                this.to = to;
            }

            public String getCurrently_pursuing() {
                return currently_pursuing;
            }

            public void setCurrently_pursuing(String currently_pursuing) {
                this.currently_pursuing = currently_pursuing;
            }

            String college_university_name;
            String degree_level;
            String degree_name;
            String from;
            String to;
            String currently_pursuing;


             public User_Eductaion(String user_education_id, String college_university_name, String degree_level, String degree_name, String from, String to, String currently_pursuing, String college_university_id, String degree_level_id, String degree_type_id, String start_date, String end_date) {
                 this.user_education_id = user_education_id;
                 this.college_university_name = college_university_name;
                 this.degree_level = degree_level;
                 this.degree_name = degree_name;
                 this.from = from;
                 this.to = to;
                 this.currently_pursuing = currently_pursuing;
                 this.college_university_id = college_university_id;
                 this.degree_level_id = degree_level_id;
                 this.degree_type_id = degree_type_id;
                 this.start_date = start_date;
                 this.end_date = end_date;
             }

             public String getCollege_university_id() {
                 return college_university_id;
             }

             public void setCollege_university_id(String college_university_id) {
                 this.college_university_id = college_university_id;
             }

             public String getDegree_level_id() {
                 return degree_level_id;
             }

             public void setDegree_level_id(String degree_level_id) {
                 this.degree_level_id = degree_level_id;
             }

             public String getDegree_type_id() {
                 return degree_type_id;
             }

             public void setDegree_type_id(String degree_type_id) {
                 this.degree_type_id = degree_type_id;
             }

             public String getStart_date() {
                 return start_date;
             }

             public void setStart_date(String start_date) {
                 this.start_date = start_date;
             }

             public String getEnd_date() {
                 return end_date;
             }

             public void setEnd_date(String end_date) {
                 this.end_date = end_date;
             }

             String college_university_id, degree_level_id, degree_type_id,
                    start_date, end_date;
        }

         public ArrayList<User_Preferences> getUser_preferences() {
             return user_preferences;
         }

         public void setUser_preferences(ArrayList<User_Preferences> user_preferences) {
             this.user_preferences = user_preferences;
         }

         public ArrayList<User_Preferences> user_preferences;
         public static class User_Preferences{

             public User_Preferences(String preference_id, ArrayList<Location_Data> location_data, ArrayList<Industry_Data> industry_data, Expected_Salary expected_salary) {
                 this.preference_id = preference_id;
                 this.location_data = location_data;
                 this.industry_data = industry_data;
                 this.expected_salary = expected_salary;
             }

             public String getPreference_id() {
                 return preference_id;
             }

             public void setPreference_id(String preference_id) {
                 this.preference_id = preference_id;
             }

             public String  preference_id;

             public ArrayList<Location_Data> getLocation_data() {
                 return location_data;
             }

             public void setLocation_data(ArrayList<Location_Data> location_data) {
                 this.location_data = location_data;
             }

             public ArrayList<Location_Data> location_data;
             public static class Location_Data{

                 public Location_Data(String location_id, String location_name) {
                     this.location_id = location_id;
                     this.location_name = location_name;
                 }

                 public String location_id;

                 public String getLocation_id() {
                     return location_id;
                 }

                 public void setLocation_id(String location_id) {
                     this.location_id = location_id;
                 }

                 public String getLocation_name() {
                     return location_name;
                 }

                 public void setLocation_name(String location_name) {
                     this.location_name = location_name;
                 }

                 public String location_name;
             }

             public ArrayList<Industry_Data> getIndustry_data() {
                 return industry_data;
             }

             public void setIndustry_data(ArrayList<Industry_Data> industry_data) {
                 this.industry_data = industry_data;
             }

             public ArrayList<Industry_Data> industry_data;

             public static class Industry_Data{

                 public Industry_Data(String industry_id, String industry_name) {
                     this.industry_id = industry_id;
                     this.industry_name = industry_name;
                 }

                 public String getIndustry_id() {
                     return industry_id;
                 }

                 public void setIndustry_id(String industry_id) {
                     this.industry_id = industry_id;
                 }

                 public String getIndustry_name() {
                     return industry_name;
                 }

                 public void setIndustry_name(String industry_name) {
                     this.industry_name = industry_name;
                 }

                 public String industry_id, industry_name;
             }

             public Expected_Salary getExpected_salary() {
                 return expected_salary;
             }

             public void setExpected_salary(Expected_Salary expected_salary) {
                 this.expected_salary = expected_salary;
             }

             public Expected_Salary expected_salary;
             public static class Expected_Salary{
                 public Expected_Salary(String expected_salary_number, String expected_salary_words) {
                     this.expected_salary_number = expected_salary_number;
                     this.expected_salary_words = expected_salary_words;
                 }

                 public String getExpected_salary_number() {
                     return expected_salary_number;
                 }

                 public void setExpected_salary_number(String expected_salary_number) {
                     this.expected_salary_number = expected_salary_number;
                 }

                 public String getExpected_salary_words() {
                     return expected_salary_words;
                 }

                 public void setExpected_salary_words(String expected_salary_words) {
                     this.expected_salary_words = expected_salary_words;
                 }
                 public  String  expected_salary_number, expected_salary_words;
             }
        }
     }
}
