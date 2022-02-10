package com.webnotics.swipee.model;

public class TwillioDetailModel {
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
            String appointment_id;
            String room_name;
            String room_sid;
            String company_id;
            String company_access_token;
            String user_id;
            String user_access_token;
            String created_role_id;


        public String getAppointment_id() {
            return appointment_id;
        }

        public void setAppointment_id(String appointment_id) {
            this.appointment_id = appointment_id;
        }

        public String getRoom_name() {
            return room_name;
        }

        public void setRoom_name(String room_name) {
            this.room_name = room_name;
        }

        public String getRoom_sid() {
            return room_sid;
        }

        public void setRoom_sid(String room_sid) {
            this.room_sid = room_sid;
        }

        public String getCompany_id() {
            return company_id;
        }

        public void setCompany_id(String company_id) {
            this.company_id = company_id;
        }

        public String getCompany_access_token() {
            return company_access_token;
        }

        public void setCompany_access_token(String company_access_token) {
            this.company_access_token = company_access_token;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getUser_access_token() {
            return user_access_token;
        }

        public void setUser_access_token(String user_access_token) {
            this.user_access_token = user_access_token;
        }

        public String getCreated_role_id() {
            return created_role_id;
        }

        public void setCreated_role_id(String created_role_id) {
            this.created_role_id = created_role_id;
        }
    }
}
