package com.webnotics.swipee.model;

import java.util.ArrayList;

public class ChatModel {
    boolean status;
    int code;
    ArrayList<Data> data;
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

    public ArrayList<Data> getData() {
        return data;
    }

    public void setData(ArrayList<Data> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public static class Data {

        String msg_id;
        String appointment_id;
        String msg_sender_id;
        String msg_receiver_id;
        String msg_content;
        String msg_filename;
        String msg_filename_org;
        int msg_filesize;
        String msg_type;
        String is_seen;
        String created_at;
        String time;
        String msg_created_at;

        public Data(String msg_id, String appointment_id, String msg_sender_id, String msg_receiver_id, String msg_content, String msg_filename, String msg_filename_org, int msg_filesize, String msg_type, String is_seen, String created_at, String time, String msg_created_at) {
            this.msg_id = msg_id;
            this.appointment_id = appointment_id;
            this.msg_sender_id = msg_sender_id;
            this.msg_receiver_id = msg_receiver_id;
            this.msg_content = msg_content;
            this.msg_filename = msg_filename;
            this.msg_filename_org = msg_filename_org;
            this.msg_filesize = msg_filesize;
            this.msg_type = msg_type;
            this.is_seen = is_seen;
            this.created_at = created_at;
            this.time = time;
            this.msg_created_at = msg_created_at;
        }

        public String getMsg_id() {
            return msg_id;
        }

        public void setMsg_id(String msg_id) {
            this.msg_id = msg_id;
        }

        public String getAppointment_id() {
            return appointment_id;
        }

        public void setAppointment_id(String appointment_id) {
            this.appointment_id = appointment_id;
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

        public String getMsg_content() {
            return msg_content;
        }

        public void setMsg_content(String msg_content) {
            this.msg_content = msg_content;
        }

        public String getMsg_filename() {
            return msg_filename;
        }

        public void setMsg_filename(String msg_filename) {
            this.msg_filename = msg_filename;
        }

        public String getMsg_filename_org() {
            return msg_filename_org;
        }

        public void setMsg_filename_org(String msg_filename_org) {
            this.msg_filename_org = msg_filename_org;
        }

        public int getMsg_filesize() {
            return msg_filesize;
        }

        public void setMsg_filesize(int msg_filesize) {
            this.msg_filesize = msg_filesize;
        }

        public String getMsg_type() {
            return msg_type;
        }

        public void setMsg_type(String msg_type) {
            this.msg_type = msg_type;
        }

        public String getIs_seen() {
            return is_seen;
        }

        public void setIs_seen(String is_seen) {
            this.is_seen = is_seen;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getMsg_created_at() {
            return msg_created_at;
        }

        public void setMsg_created_at(String msg_created_at) {
            this.msg_created_at = msg_created_at;
        }
    }
}
