package com.webnotics.swipee.model.seeker;

import com.google.gson.JsonObject;

import java.util.ArrayList;

public class NotificationModel {

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

    public ArrayList<Data> getData() {
        return data;
    }

    public void setData(ArrayList<Data> data) {
        this.data = data;
    }

    ArrayList<Data>  data;
    public static class Data{
       String first_name;
       String last_name;
       String company_name="";
       String notification_text;
       String created_at;
       String notification_image;
       String is_read="";
       String is_seen="";
       JsonObject payload_data;


        public String getIs_read() {
            return is_read;
        }

        public void setIs_read(String is_read) {
            this.is_read = is_read;
        }

        public String getIs_seen() {
            return is_seen;
        }

        public void setIs_seen(String is_seen) {
            this.is_seen = is_seen;
        }

        public JsonObject getPayload_data() {
            return payload_data;
        }

        public void setPayload_data(JsonObject payload_data) {
            this.payload_data = payload_data;
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

        public String getNotification_text() {
            return notification_text;
        }

        public void setNotification_text(String notification_text) {
            this.notification_text = notification_text;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getNotification_image() {
            return notification_image;
        }

        public void setNotification_image(String notification_image) {
            this.notification_image = notification_image;
        }

        public String getCompany_name() {
            return company_name;
        }

        public void setCompany_name(String company_name) {
            this.company_name = company_name;
        }
    }
}
