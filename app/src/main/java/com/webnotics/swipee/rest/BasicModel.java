package com.webnotics.swipee.rest;

import com.google.gson.JsonObject;

public class BasicModel {
      boolean status;
      int code;
      JsonObject data;
      String message;

    public BasicModel(boolean status, int code, JsonObject data, String message) {
        this.status = status;
        this.code = code;
        this.data = data;
        this.message = message;
    }

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

    public JsonObject getData() {
        return data;
    }

    public void setData(JsonObject data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
