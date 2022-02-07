package com.webnotics.swipee.model.company;

public class LikedUserModel {
    String user_id;
    String first_name;
    String last_name;
    String user_profile;
    String skill_name;
    String country;
    String state;
    String city;
    String job_id;
    String apply_id="";

    public LikedUserModel(String user_id, String first_name, String last_name, String user_profile, String skill_name, String country, String state, String city, String job_id) {
        this.user_id = user_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.user_profile = user_profile;
        this.skill_name = skill_name;
        this.country = country;
        this.state = state;
        this.city = city;
        this.job_id = job_id;
    }
 public LikedUserModel(String user_id, String first_name, String last_name, String user_profile, String skill_name, String country, String state, String city, String job_id, String apply_id) {
        this.user_id = user_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.user_profile = user_profile;
        this.skill_name = skill_name;
        this.country = country;
        this.state = state;
        this.city = city;
        this.job_id = job_id;
        this.apply_id = apply_id;
    }

    public String getApply_id() {
        return apply_id;
    }

    public void setApply_id(String apply_id) {
        this.apply_id = apply_id;
    }

    public String getJob_id() {
        return job_id;
    }

    public void setJob_id(String job_id) {
        this.job_id = job_id;
    }

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

    public String getSkill_name() {
        return skill_name;
    }

    public void setSkill_name(String skill_name) {
        this.skill_name = skill_name;
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
}
