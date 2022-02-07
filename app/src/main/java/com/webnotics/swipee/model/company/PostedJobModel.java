package com.webnotics.swipee.model.company;

import java.util.ArrayList;

public class PostedJobModel {

    boolean status;
    int code;
    String message;

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

    DataObj data;

    public DataObj getData() {
        return data;
    }

    public void setData(DataObj data) {
        this.data = data;
    }

    public static class DataObj {
        public ArrayList<Data> getActive_jobs() {
            return active_jobs;
        }

        public void setActive_jobs(ArrayList<Data> active_jobs) {
            this.active_jobs = active_jobs;
        }

        ArrayList<Data>  active_jobs;
        ArrayList<Data>  closed_jobs;
        ArrayList<Data>  inactive_jobs;
        ArrayList<Data>  filter_jobs=new ArrayList<>();

        public ArrayList<Data> getFilter_jobs() {
            return filter_jobs;
        }

        public void setFilter_jobs(ArrayList<Data> filter_jobs) {
            this.filter_jobs = filter_jobs;
        }

        public ArrayList<Data> getInactive_jobs() {
            return inactive_jobs;
        }

        public void setInactive_jobs(ArrayList<Data> inactive_jobs) {
            this.inactive_jobs = inactive_jobs;
        }

        public ArrayList<Data> getClosed_jobs() {
            return closed_jobs;
        }

        public void setClosed_jobs(ArrayList<Data> closed_jobs) {
            this.closed_jobs = closed_jobs;
        }
    }
    public static class Data {

        String job_post_id;
        String company_name;
        String user_id;
        String job_title;
        String job_experience;
        String job_city;
        String job_state;
        String job_country;
        String job_skills;
        String job_post_date;
        String job_status;
        String is_hiring_closed;
        String job_type;
        String job_degree;
        String job_salery;
        String job_status_type;
        String hiring_status_type;
        int jobTypeCount;
        boolean isClose=false;

        public String getJob_status_type() {
            return job_status_type;
        }

        public void setJob_status_type(String job_status_type) {
            this.job_status_type = job_status_type;
        }

        public String getHiring_status_type() {
            return hiring_status_type;
        }

        public void setHiring_status_type(String hiring_status_type) {
            this.hiring_status_type = hiring_status_type;
        }

        public boolean isClose() {
            return isClose;
        }

        public void setClose(boolean close) {
            isClose = close;
        }

        public int getJobTypeCount() {
            return jobTypeCount;
        }

        public void setJobTypeCount(int jobTypeCount) {
            this.jobTypeCount = jobTypeCount;
        }

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

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
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

        public String getJob_post_date() {
            return job_post_date;
        }

        public void setJob_post_date(String job_post_date) {
            this.job_post_date = job_post_date;
        }

        public String getJob_status() {
            return job_status;
        }

        public void setJob_status(String job_status) {
            this.job_status = job_status;
        }

        public String getIs_hiring_closed() {
            return is_hiring_closed;
        }

        public void setIs_hiring_closed(String is_hiring_closed) {
            this.is_hiring_closed = is_hiring_closed;
        }

        public String getJob_type() {
            return job_type;
        }

        public void setJob_type(String job_type) {
            this.job_type = job_type;
        }

        public String getJob_degree() {
            return job_degree;
        }

        public void setJob_degree(String job_degree) {
            this.job_degree = job_degree;
        }

        public String getJob_salery() {
            return job_salery;
        }

        public void setJob_salery(String job_salery) {
            this.job_salery = job_salery;
        }


    }
}
