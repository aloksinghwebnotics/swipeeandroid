package com.webnotics.swipee.model.seeker;

public class SeekerAppointmentDetailModel {
    boolean status;
    int code;
    Data data;
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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {

        String company_id;
        String first_name;
        String last_name;
        String company_name;
        String company_email;
        String company_logo;
        String company_address;
        String country;
        String state;
        String city;
        String company_pincode;
        String phone_code;
        String mobile;
        String industry_name;
        String company_size;
        String company_founded;
        String appointment_id;
        String appointment_number;
        String appointment_type;
        String appointment_date;
        String appointment_start_at;
        String appointment_end_at;
        int is_live;


        public String getCompany_id() {
            return company_id;
        }

        public void setCompany_id(String company_id) {
            this.company_id = company_id;
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

        public String getCompany_name() {
            return company_name;
        }

        public void setCompany_name(String company_name) {
            this.company_name = company_name;
        }

        public String getCompany_email() {
            return company_email;
        }

        public void setCompany_email(String company_email) {
            this.company_email = company_email;
        }

        public String getCompany_logo() {
            return company_logo;
        }

        public void setCompany_logo(String company_logo) {
            this.company_logo = company_logo;
        }

        public String getCompany_address() {
            return company_address;
        }

        public void setCompany_address(String company_address) {
            this.company_address = company_address;
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

        public String getCompany_pincode() {
            return company_pincode;
        }

        public void setCompany_pincode(String company_pincode) {
            this.company_pincode = company_pincode;
        }

        public String getPhone_code() {
            return phone_code;
        }

        public void setPhone_code(String phone_code) {
            this.phone_code = phone_code;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getIndustry_name() {
            return industry_name;
        }

        public void setIndustry_name(String industry_name) {
            this.industry_name = industry_name;
        }

        public String getCompany_size() {
            return company_size;
        }

        public void setCompany_size(String company_size) {
            this.company_size = company_size;
        }

        public String getCompany_founded() {
            return company_founded;
        }

        public void setCompany_founded(String company_founded) {
            this.company_founded = company_founded;
        }

        public String getAppointment_id() {
            return appointment_id;
        }

        public void setAppointment_id(String appointment_id) {
            this.appointment_id = appointment_id;
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

        public int getIs_live() {
            return is_live;
        }

        public void setIs_live(int is_live) {
            this.is_live = is_live;
        }
    }
}
