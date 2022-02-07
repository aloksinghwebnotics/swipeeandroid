package com.webnotics.swipee.model.company;

import java.util.ArrayList;

public class CompanyUserListing {

      boolean  status;
      int code;

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

    String message;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    Data data;
    public class Data{
       public int left_package_days;
        public int left_swipes;
        public String package_id;


        public String getPackage_id() {
            return package_id;
        }

        public void setPackage_id(String package_id) {
            this.package_id = package_id;
        }

        public int getLeft_swipes() {
            return left_swipes;
        }

        public void setLeft_swipes(int left_swipes) {
            this.left_swipes = left_swipes;
        }

        public int getLeft_package_days() {
            return left_package_days;
        }

        public void setLeft_package_days(int left_package_days) {
            this.left_package_days = left_package_days;
        }

        public int getLeft_posted_jobs() {
            return left_posted_jobs;
        }

        public void setLeft_posted_jobs(int left_posted_jobs) {
            this.left_posted_jobs = left_posted_jobs;
        }

        public int left_posted_jobs;
        public int getCompany_notification_counter() {
            return company_notification_counter;
        }

        public void setCompany_notification_counter(int company_notification_counter) {
            this.company_notification_counter = company_notification_counter;
        }

        public  int company_notification_counter;
        public ArrayList<Userslisting> getUsers_listing() {
            return users_listing;
        }

        public void setUsers_listing(ArrayList<Userslisting> users_listing) {
            this.users_listing = users_listing;
        }

        ArrayList<Userslisting> users_listing;

        public class Userslisting{
             String user_id;

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
                return middle_name;
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

            public String getDegree_name() {
                return degree_name;
            }

            public void setDegree_name(String degree_name) {
                this.degree_name = degree_name;
            }

            public String[] getSkill_name() {
                return skill_name;
            }

            public void setSkill_name(String[] skill_name) {
                this.skill_name = skill_name;
            }

            String first_name;
            String middle_name;
            String last_name;
            String user_profile;
            String email;
            String degree_name;
            String skill_name[];

           String  mobile_no;
            String phone_code;
            String country;
            String state;
            String job_id;

            public String getJob_id() {
                return job_id;
            }

            public void setJob_id(String job_id) {
                this.job_id = job_id;
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

            public String getGender() {
                return gender;
            }

            public void setGender(String gender) {
                this.gender = gender;
            }

            String city;
            String user_age;
            String user_dob;
            String gender;
            String match_id;

            public String getMatch_id() {
                return match_id;
            }

            public void setMatch_id(String match_id) {
                this.match_id = match_id;
            }

            public String getCompany_match_status() {
                return company_match_status;
            }

            public void setCompany_match_status(String company_match_status) {
                this.company_match_status = company_match_status;
            }

            public String getUser_match_status() {
                return user_match_status;
            }

            public void setUser_match_status(String user_match_status) {
                this.user_match_status = user_match_status;
            }

            String company_match_status;
            String user_match_status;


        }

    }


}
