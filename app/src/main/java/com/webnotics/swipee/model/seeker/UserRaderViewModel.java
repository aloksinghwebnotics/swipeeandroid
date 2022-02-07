package com.webnotics.swipee.model.seeker;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class UserRaderViewModel {

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

        public ArrayList<Companies_Listing> getCompanies_Listing() {
            return companies_listing;
        }

        public void setCompanies_Listing(ArrayList<Companies_Listing> companies_listing) {
            this.companies_listing = companies_listing;
        }

        public ArrayList<Companies_Listing> companies_listing;

        public static  class Companies_Listing implements Parcelable {
          public String company_id;
            public String first_name;
            public String last_name;
            public String company_name;
            public String company_email;
            public String company_logo;
            public String company_address;
            public String country;
            public String state;
            public String city;
            public String company_pincode;
            public String phone_code;
            public String mobile;

            protected Companies_Listing(Parcel in) {
                company_id = in.readString();
                first_name = in.readString();
                last_name = in.readString();
                company_name = in.readString();
                company_email = in.readString();
                company_logo = in.readString();
                company_address = in.readString();
                country = in.readString();
                state = in.readString();
                city = in.readString();
                company_pincode = in.readString();
                phone_code = in.readString();
                mobile = in.readString();
                Company_radius = in.readString();
                industry_name = in.readString();
                company_size = in.readString();
                company_founded = in.readString();
                is_email_verify = in.readString();
                is_mobile_verify = in.readString();
                radius_range = in.readString();
                company_latitude = in.readString();
                company_longitude = in.readString();
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(company_id);
                dest.writeString(first_name);
                dest.writeString(last_name);
                dest.writeString(company_name);
                dest.writeString(company_email);
                dest.writeString(company_logo);
                dest.writeString(company_address);
                dest.writeString(country);
                dest.writeString(state);
                dest.writeString(city);
                dest.writeString(company_pincode);
                dest.writeString(phone_code);
                dest.writeString(mobile);
                dest.writeString(Company_radius);
                dest.writeString(industry_name);
                dest.writeString(company_size);
                dest.writeString(company_founded);
                dest.writeString(is_email_verify);
                dest.writeString(is_mobile_verify);
                dest.writeString(radius_range);
                dest.writeString(company_latitude);
                dest.writeString(company_longitude);
            }

            @Override
            public int describeContents() {
                return 0;
            }

            public static final Creator<Companies_Listing> CREATOR = new Creator<Companies_Listing>() {
                @Override
                public Companies_Listing createFromParcel(Parcel in) {
                    return new Companies_Listing(in);
                }

                @Override
                public Companies_Listing[] newArray(int size) {
                    return new Companies_Listing[size];
                }
            };

            public String getCompany_radius() {
                return Company_radius;
            }

            public void setCompany_radius(String company_radius) {
                Company_radius = company_radius;
            }

            public  String Company_radius;

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

            public String getIs_email_verify() {
                return is_email_verify;
            }

            public void setIs_email_verify(String is_email_verify) {
                this.is_email_verify = is_email_verify;
            }

            public String getIs_mobile_verify() {
                return is_mobile_verify;
            }

            public void setIs_mobile_verify(String is_mobile_verify) {
                this.is_mobile_verify = is_mobile_verify;
            }



            public String getCompany_latitude() {
                return company_latitude;
            }

            public void setCompany_latitude(String company_latitude) {
                this.company_latitude = company_latitude;
            }

            public String getCompany_longitude() {
                return company_longitude;
            }

            public void setCompany_longitude(String company_longitude) {
                this.company_longitude = company_longitude;
            }

            public String industry_name;
            public String company_size;
            public String company_founded;
            public String is_email_verify;
            public String is_mobile_verify;

            public String getRadius_range() {
                return radius_range;
            }

            public void setRadius_range(String radius_range) {
                this.radius_range = radius_range;
            }

            public String radius_range;
            public String company_latitude;
            public String company_longitude;
        }

    }

}
