package com.webnotics.swipee.model.seeker;

import java.util.ArrayList;

public class UserJobListing {

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    String  message;
    boolean status;
    int code;

    public Data data;

    public class Data{
        public int left_swipes;
        public int left_apply_jobs;
        public String package_id;
        public String package_expire;


        public int getLeft_apply_jobs() {
            return left_apply_jobs;
        }

        public void setLeft_apply_jobs(int left_apply_jobs) {
            this.left_apply_jobs = left_apply_jobs;
        }

        public String getPackage_expire() {
            return package_expire;
        }

        public void setPackage_expire(String package_expire) {
            this.package_expire = package_expire;
        }

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

        public int getUser_notification_counter() {
            return user_notification_counter;
        }

        public void setUser_notification_counter(int user_notification_counter) {
            this.user_notification_counter = user_notification_counter;
        }

        public  int user_notification_counter;
        public ArrayList<Jobs_Listing> getJobs_listing() {
            return jobs_listing;
        }

        public void setJobs_listing(ArrayList<Jobs_Listing> jobs_listing) {
            this.jobs_listing = jobs_listing;
        }

        public ArrayList<Jobs_Listing> jobs_listing;

            public class Jobs_Listing{
                    String job_post_id;

                public String getCompany_id() {
                    return company_id;
                }

                public void setCompany_id(String company_id) {
                    this.company_id = company_id;
                }

                String company_id;
                String company_name;

                public String getJob_post_id() {
                    return job_post_id;
                }

                public void setJob_post_id(String job_post_id) {
                    this.job_post_id = job_post_id;
                }

                public String getCompany_name() {
                    return company_name;
                }

                public void setCompany_name(String company_name) {
                    this.company_name = company_name;
                }

                public String getCompany_logo() {
                    return company_logo;
                }

                public void setCompany_logo(String company_logo) {
                    this.company_logo = company_logo;
                }

                public String getJob_title() {
                    return job_title;
                }

                public void setJob_title(String job_title) {
                    this.job_title = job_title;
                }

                public String getJob_experience() {
                    return job_experience;
                }

                public void setJob_experience(String job_experience) {
                    this.job_experience = job_experience;
                }

                public String getJob_city() {
                    return job_city;
                }

                public void setJob_city(String job_city) {
                    this.job_city = job_city;
                }

                public String getJob_state() {
                    return job_state;
                }

                public void setJob_state(String job_state) {
                    this.job_state = job_state;
                }

                public String getJob_country() {
                    return job_country;
                }

                public void setJob_country(String job_country) {
                    this.job_country = job_country;
                }

                public String getJob_skills() {
                    return job_skills;
                }

                public void setJob_skills(String job_skills) {
                    this.job_skills = job_skills;
                }

                public String getUser_job_status() {
                    return user_job_status;
                }

                public void setUser_job_status(String user_job_status) {
                    this.user_job_status = user_job_status;
                }

                String company_logo;
                String job_title;
                String job_experience;
                String job_city;
                String job_state;
                String job_country;
                String job_skills;
                String user_job_status;

                public String getUser_savedjob_status() {
                    return user_savedjob_status;
                }

                public void setUser_savedjob_status(String user_savedjob_status) {
                    this.user_savedjob_status = user_savedjob_status;
                }

                String user_savedjob_status;

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

                String match_id, company_match_status, user_match_status;
            }
    }




}
