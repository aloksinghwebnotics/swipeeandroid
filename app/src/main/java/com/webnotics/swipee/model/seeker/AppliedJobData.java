package com.webnotics.swipee.model.seeker;

public class AppliedJobData {
    String job_post_id;
    String company_name;
    String company_logo;
    String job_title;
    String job_experience;
    String job_type;
    String job_city;
    String job_state;
    String job_country;
    String job_skills;
    String user_job_status;
    String company_action_status;

    public AppliedJobData(String job_post_id, String company_name, String company_logo, String job_title, String job_experience, String job_type, String job_city,
                          String job_state, String job_country, String job_skills, String user_job_status, String company_action_status) {
        this.job_post_id = job_post_id;
        this.company_name = company_name;
        this.company_logo = company_logo;
        this.job_title = job_title;
        this.job_experience = job_experience;
        this.job_type = job_type;
        this.job_city = job_city;
        this.job_state = job_state;
        this.job_country = job_country;
        this.job_skills = job_skills;
        this.user_job_status = user_job_status;
        this.company_action_status = company_action_status;
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

    public String getJob_type() {
        return job_type;
    }

    public void setJob_type(String job_type) {
        this.job_type = job_type;
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

    public String getCompany_action_status() {
        return company_action_status;
    }

    public void setCompany_action_status(String company_action_status) {
        this.company_action_status = company_action_status;
    }
}
