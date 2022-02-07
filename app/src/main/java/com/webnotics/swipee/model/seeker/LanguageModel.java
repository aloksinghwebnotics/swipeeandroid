package com.webnotics.swipee.model.seeker;

import java.util.ArrayList;

public class LanguageModel {


      boolean status;
      int  code;
      public ArrayList<Data> data;

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

    public String message;
      public class Data{
           String language_id;

          public String getLanguage_id() {
              return language_id;
          }

          public void setLanguage_id(String language_id) {
              this.language_id = language_id;
          }

          public String getLanguage_name() {
              return language_name;
          }

          public void setLanguage_name(String language_name) {
              this.language_name = language_name;
          }

          String language_name;
      }

}
