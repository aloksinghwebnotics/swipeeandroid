package com.webnotics.swipee.model.company;

import java.util.ArrayList;

public class CompanyAppointmentModel {

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
        public ArrayList<Data> getAppointment_data() {
            return appointment_data;
        }

        public void setAppointment_data(ArrayList<Data> appointment_data) {
            this.appointment_data = appointment_data;
        }

        ArrayList<Data>  appointment_data;



    }
    public static class Data {

        String appointment_id;
        String appointment_number;
        String user_id;
        String first_name;
        String last_name;
        String profile_image;
        String country;
        String state;
        String city;
        String appointment_type;
        String appointment_date;
        String appointment_start_at;
        String appointment_end_at;
        String job_id;
        String job_title;
        String company_action;
        String apply_id="";
        String appointment_date_time="";
        ArrayList<String> skill_name;


        public String getAppointment_date_time() {
            return appointment_date_time;
        }

        public void setAppointment_date_time(String appointment_date_time) {
            this.appointment_date_time = appointment_date_time;
        }

        public String getApply_id() {
            return apply_id;
        }

        public void setApply_id(String apply_id) {
            this.apply_id = apply_id;
        }

        public String getAppointment_id() {
            return appointment_id;
        }

        public void setAppointment_id(String appointment_id) {
            this.appointment_id = appointment_id;
        }

        public String getJob_id() {
            return job_id;
        }

        public void setJob_id(String job_id) {
            this.job_id = job_id;
        }

        public String getJob_title() {
            return job_title;
        }

        public void setJob_title(String job_title) {
            this.job_title = job_title;
        }


        public String getAppointment_number() {
            return appointment_number;
        }

        public void setAppointment_number(String appointment_number) {
            this.appointment_number = appointment_number;
        }

        public String getAppointment_type() {
            return appointment_type;
        }

        public void setAppointment_type(String appointment_type) {
            this.appointment_type = appointment_type;
        }

        public String getAppointment_date() {
            return appointment_date;
        }

        public void setAppointment_date(String appointment_date) {
            this.appointment_date = appointment_date;
        }

        public String getAppointment_start_at() {
            return appointment_start_at;
        }

        public void setAppointment_start_at(String appointment_start_at) {
            this.appointment_start_at = appointment_start_at;
        }

        public String getAppointment_end_at() {
            return appointment_end_at;
        }

        public void setAppointment_end_at(String appointment_end_at) {
            this.appointment_end_at = appointment_end_at;
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

        public String getProfile_image() {
            return profile_image;
        }

        public void setProfile_image(String profile_image) {
            this.profile_image = profile_image;
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

        public String getCompany_action() {
            return company_action;
        }

        public void setCompany_action(String company_action) {
            this.company_action = company_action;
        }

        public ArrayList<String> getSkill_name() {
            return skill_name;
        }

        public void setSkill_name(ArrayList<String> skill_name) {
            this.skill_name = skill_name;
        }
    }

}
