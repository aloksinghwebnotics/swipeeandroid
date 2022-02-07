package com.webnotics.swipee.model.seeker;

import java.util.ArrayList;

public class AppointmentModel {

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
        String job_id;
        String job_title;
        String job_city;
        String job_state;
        String job_country;
        String job_skills;
        String appointment_number;
        String company_id;
        String company_name;
        String company_logo;
        String appointment_type;
        String appointment_date;
        String appointment_start_at;
        String appointment_end_at;

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

        public String getAppointment_number() {
            return appointment_number;
        }

        public void setAppointment_number(String appointment_number) {
            this.appointment_number = appointment_number;
        }

        public String getCompany_id() {
            return company_id;
        }

        public void setCompany_id(String company_id) {
            this.company_id = company_id;
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
    }

}
