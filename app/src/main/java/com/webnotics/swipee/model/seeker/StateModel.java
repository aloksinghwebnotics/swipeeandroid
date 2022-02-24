package com.webnotics.swipee.model.seeker;

import java.util.ArrayList;

public class StateModel {

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
          public String getCountry_id() {
              return country_id;
          }

          public void setCountry_id(String country_id) {
              this.country_id = country_id;
          }

          public String getState_id() {
              return state_id;
          }

          public void setState_id(String state_id) {
              this.state_id = state_id;
          }

          public String getState_name() {
              return state_name;
          }

          public void setState_name(String state_name) {
              this.state_name = state_name;
          }

          String  country_id, state_id, state_name;
          boolean selected=false;

          public boolean isSelected() {
              return selected;
          }

          public void setSelected(boolean selected) {
              this.selected = selected;
          }
      }
}
