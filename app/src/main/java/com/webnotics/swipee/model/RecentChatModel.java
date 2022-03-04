package com.webnotics.swipee.model;

import java.util.ArrayList;

public class RecentChatModel {
    boolean status;
    int code;
    String message;
    ArrayList<Data> data;

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

    public ArrayList<Data> getData() {
        return data;
    }

    public void setData(ArrayList<Data> data) {
        this.data = data;
    }

    public static class Data {
        String appointment_id;
        String appointment_number;
        String user_id;
        String company_id;
        String first_name;
        String last_name;
        String user_profile;
        String msg_id;
        String msg_type;
        String msg_sender_id;
        String msg_receiver_id;
        String company_action;
        String apply_id;
        String company_name="";
        String last_msg_content;
        int unseen_msg_count;
        String msg_created_at;

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

        public String getMsg_id() {
            return msg_id;
        }

        public void setMsg_id(String msg_id) {
            this.msg_id = msg_id;
        }

        public String getMsg_type() {
            return msg_type;
        }

        public void setMsg_type(String msg_type) {
            this.msg_type = msg_type;
        }

        public String getMsg_sender_id() {
            return msg_sender_id;
        }

        public void setMsg_sender_id(String msg_sender_id) {
            this.msg_sender_id = msg_sender_id;
        }

        public String getMsg_receiver_id() {
            return msg_receiver_id;
        }

        public void setMsg_receiver_id(String msg_receiver_id) {
            this.msg_receiver_id = msg_receiver_id;
        }

        public String getCompany_action() {
            return company_action;
        }

        public void setCompany_action(String company_action) {
            this.company_action = company_action;
        }

        public String getApply_id() {
            return apply_id;
        }

        public void setApply_id(String apply_id) {
            this.apply_id = apply_id;
        }

        public String getLast_msg_content() {
            return last_msg_content;
        }

        public void setLast_msg_content(String last_msg_content) {
            this.last_msg_content = last_msg_content;
        }

        public int getUnseen_msg_count() {
            return unseen_msg_count;
        }

        public void setUnseen_msg_count(int unseen_msg_count) {
            this.unseen_msg_count = unseen_msg_count;
        }

        public String getMsg_created_at() {
            return msg_created_at;
        }

        public void setMsg_created_at(String msg_created_at) {
            this.msg_created_at = msg_created_at;
        }
    }

}
