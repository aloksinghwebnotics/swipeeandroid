package com.webnotics.swipee.model.seeker;

import java.util.ArrayList;

public class JobTypeModel {
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

    public boolean status;
   public int code;
   public String message;
   public ArrayList<Data> data;
   public  class  Data{
      public  String job_type_id;

       public String getJob_type_id() {
           return job_type_id;
       }

       public void setJob_type_id(String job_type_id) {
           this.job_type_id = job_type_id;
       }

       public String getJob_type_name() {
           return job_type_name;
       }

       public void setJob_type_name(String job_type_name) {
           this.job_type_name = job_type_name;
       }

       public String job_type_name;
   }
}
