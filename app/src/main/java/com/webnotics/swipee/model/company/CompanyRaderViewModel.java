package com.webnotics.swipee.model.company;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class CompanyRaderViewModel {

       public boolean  status;
       public int code;

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

    public String message;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Data data;
    public static class Data{
        public ArrayList<Advert_Companies> getAdvert_companies() {
            return advert_companies;
        }

        public void setAdvert_companies(ArrayList<Advert_Companies> advert_companies) {
            this.advert_companies = advert_companies;
        }

        public ArrayList<Advert_Companies> advert_companies;

        public static class Advert_Companies implements Parcelable{

                    String  company_id;

            protected Advert_Companies(Parcel in) {
                company_id = in.readString();
                company_name = in.readString();
                company_logo = in.readString();
                company_website = in.readString();
            }

            public static final Creator<Advert_Companies> CREATOR = new Creator<Advert_Companies>() {
                @Override
                public Advert_Companies createFromParcel(Parcel in) {
                    return new Advert_Companies(in);
                }

                @Override
                public Advert_Companies[] newArray(int size) {
                    return new Advert_Companies[size];
                }
            };

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

                public String getCompany_website() {
                    return company_website;
                }

                public void setCompany_website(String company_website) {
                    this.company_website = company_website;
                }

                String company_name;
                String company_logo;
                String company_website;

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel parcel, int i) {
                parcel.writeString(company_id);
                parcel.writeString(company_name);
                parcel.writeString(company_logo);
                parcel.writeString(company_website);
            }
        }

        public ArrayList<User_Listing> getUsers_listing() {
            return users_listing;
        }

        public void setUsers_listing(ArrayList<User_Listing> users_listing) {
            this.users_listing = users_listing;
        }

        public ArrayList<User_Listing> users_listing;

        public static  class User_Listing implements Parcelable {
          public String user_id;
            public String first_name;
            public String last_name;
            public String user_profile;
            public String email;
            public String degree_name;
            public String mobile_no;
            public String phone_code;
            public String country;
            public String state;
            public String city;
            public String user_age;
            public String user_dob;
            public String gender;
            public String match_id;
            public String company_match_status;
            public String user_match_status;
            public String job_id;
            public String radius_range;
            public String user_radius;
            public String latitude;
            public String longitude;

            protected User_Listing(Parcel in) {
                user_id = in.readString();
                first_name = in.readString();
                last_name = in.readString();
                user_profile = in.readString();
                email = in.readString();
                degree_name = in.readString();
                mobile_no = in.readString();
                country = in.readString();
                state = in.readString();
                city = in.readString();
                phone_code = in.readString();
                user_age = in.readString();
                user_dob = in.readString();
                gender = in.readString();
                match_id = in.readString();
                company_match_status = in.readString();
                user_match_status = in.readString();
                job_id = in.readString();
                radius_range = in.readString();
                user_radius = in.readString();
                latitude = in.readString();
                longitude = in.readString();
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(user_id);
                dest.writeString(first_name);
                dest.writeString(last_name);
                dest.writeString(user_profile);
                dest.writeString(email);
                dest.writeString(degree_name);
                dest.writeString(mobile_no);
                dest.writeString(country);
                dest.writeString(state);
                dest.writeString(city);
                dest.writeString(phone_code);
                dest.writeString(user_age);
                dest.writeString(user_dob);
                dest.writeString(gender);
                dest.writeString(match_id);
                dest.writeString(company_match_status);
                dest.writeString(user_match_status);
                dest.writeString(job_id);
                dest.writeString(radius_range);
                dest.writeString(user_radius);
                dest.writeString(latitude);
                dest.writeString(longitude);
            }

            @Override
            public int describeContents() {
                return 0;
            }

            public static final Creator<User_Listing> CREATOR = new Creator<User_Listing>() {
                @Override
                public User_Listing createFromParcel(Parcel in) {
                    return new User_Listing(in);
                }

                @Override
                public User_Listing[] newArray(int size) {
                    return new User_Listing[size];
                }
            };

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

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public String getDegree_name() {
                return degree_name;
            }

            public void setDegree_name(String degree_name) {
                this.degree_name = degree_name;
            }

            public String getMobile_no() {
                return mobile_no;
            }

            public void setMobile_no(String mobile_no) {
                this.mobile_no = mobile_no;
            }

            public String getPhone_code() {
                return phone_code;
            }

            public void setPhone_code(String phone_code) {
                this.phone_code = phone_code;
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

            public String getUser_age() {
                return user_age;
            }

            public void setUser_age(String user_age) {
                this.user_age = user_age;
            }

            public String getUser_dob() {
                return user_dob;
            }

            public void setUser_dob(String user_dob) {
                this.user_dob = user_dob;
            }

            public String getGender() {
                return gender;
            }

            public void setGender(String gender) {
                this.gender = gender;
            }

            public String getMatch_id() {
                return match_id;
            }

            public void setMatch_id(String match_id) {
                this.match_id = match_id;
            }

            public String getCompany_match_status() {
                return company_match_status;
            }

            public void setCompany_match_status(String company_match_status) {
                this.company_match_status = company_match_status;
            }

            public String getUser_match_status() {
                return user_match_status;
            }

            public void setUser_match_status(String user_match_status) {
                this.user_match_status = user_match_status;
            }

            public String getJob_id() {
                return job_id;
            }

            public void setJob_id(String job_id) {
                this.job_id = job_id;
            }

            public String getRadius_range() {
                return radius_range;
            }

            public void setRadius_range(String radius_range) {
                this.radius_range = radius_range;
            }

            public String getUser_radius() {
                return user_radius;
            }

            public void setUser_radius(String user_radius) {
                this.user_radius = user_radius;
            }

            public String getLatitude() {
                return latitude;
            }

            public void setLatitude(String latitude) {
                this.latitude = latitude;
            }

            public String getLongitude() {
                return longitude;
            }

            public void setLongitude(String longitude) {
                this.longitude = longitude;
            }
        }

    }

}
