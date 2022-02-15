package com.webnotics.swipee.model.seeker;

import java.util.ArrayList;

public class FilterModel {
    boolean status;
    int code;

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

    String message;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    Data data;
    public static class Data{
        public ArrayList<JobType> getJob_type() {
            return job_type;
        }

        public void setJob_type(ArrayList<JobType> job_type) {
            this.job_type = job_type;
        }

        public ArrayList<JobType> job_type;
        public class JobType{
            String job_type_id, job_type_name;

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

            public boolean isSelected() {
                return selected;
            }

            public void setSelected(boolean selected) {
                this.selected = selected;
            }

            boolean selected;
        }

        public ArrayList<Location> getLocation() {
            return location;
        }

        public void setLocation(ArrayList<Location> location) {
            this.location = location;
        }

        ArrayList<Location> location;


        public static class Location{
            String  location_id, location_name,state_name;

            public String getState_name() {
                return state_name;
            }

            public void setState_name(String state_name) {
                this.state_name = state_name;
            }

            public String getLocation_id() {
                return location_id;
            }

            public void setLocation_id(String location_id) {
                this.location_id = location_id;
            }

            public String getLocation_name() {
                return location_name;
            }

            public void setLocation_name(String location_name) {
                this.location_name = location_name;
            }

            public boolean isSelected() {
                return selected;
            }

            public void setSelected(boolean selected) {
                this.selected = selected;
            }

            boolean selected;
        }


        public ArrayList<Distance> getDistance() {
            return distance;
        }

        public void setDistance(ArrayList<Distance> distance) {
            this.distance = distance;
        }

        public ArrayList<Distance>distance;

        public class Distance{
            String  distance_id, distance_name;

            public String getDistance_id() {
                return distance_id;
            }

            public void setDistance_id(String distance_id) {
                this.distance_id = distance_id;
            }

            public String getDistance_name() {
                return distance_name;
            }

            public void setDistance_name(String distance_name) {
                this.distance_name = distance_name;
            }

            public boolean isSelected() {
                return selected;
            }

            public void setSelected(boolean selected) {
                this.selected = selected;
            }

            boolean selected;
        }



        public ArrayList<Designation> getDesignation() {
            return functional_area;
        }

        public void setDesignation(ArrayList<Designation> designation) {
            this.functional_area = designation;
        }

        public ArrayList<Designation>functional_area;

        public class Designation{
            String  functional_id, functional_name;

            public String getDesignation_id() {
                return functional_id;
            }

            public void setDesignation_id(String designation_id) {
                this.functional_id = designation_id;
            }

            public String getDesignation_name() {
                return functional_name;
            }

            public void setDesignation_name(String designation_name) {
                this.functional_name = designation_name;
            }

            public boolean isSelected() {
                return selected;
            }

            public void setSelected(boolean selected) {
                this.selected = selected;
            }

            boolean selected;
        }







        public ArrayList<Qualification> getQualification() {
            return qualification;
        }

        public void setQualification(ArrayList<Qualification> qualification) {
            this.qualification = qualification;
        }

        public ArrayList<Qualification>qualification;

        public class Qualification{
            String  degree_id, degree_name;

            public String getDegree_id() {
                return degree_id;
            }

            public void setDegree_id(String degree_id) {
                this.degree_id = degree_id;
            }

            public String getDegree_name() {
                return degree_name;
            }

            public void setDegree_name(String degree_name) {
                this.degree_name = degree_name;
            }

            public boolean isSelected() {
                return selected;
            }

            public void setSelected(boolean selected) {
                this.selected = selected;
            }

            boolean selected;
        }






        public ArrayList<Experience> getExperience() {
            return experience;
        }

        public void setExperience(ArrayList<Experience> experience) {
            this.experience = experience;
        }

        public ArrayList<Experience>experience;

        public class Experience{
            String  experience_id, experience_name;

            public String getExperience_id() {
                return experience_id;
            }

            public void setExperience_id(String experience_id) {
                this.experience_id = experience_id;
            }

            public String getExperience_name() {
                return experience_name;
            }

            public void setExperience_name(String experience_name) {
                this.experience_name = experience_name;
            }

            public boolean isSelected() {
                return selected;
            }

            public void setSelected(boolean selected) {
                this.selected = selected;
            }

            boolean selected;
        }









        public ArrayList<Industry> getIndustry() {
            return industry;
        }

        public void setIndustry(ArrayList<Industry> industry) {
            this.industry = industry;
        }

        public ArrayList<Industry>industry;

        public class Industry{
            String  industry_id, industry_name;

            public String getIndustry_id() {
                return industry_id;
            }

            public void setIndustry_id(String industry_id) {
                this.industry_id = industry_id;
            }

            public String getIndustry_name() {
                return industry_name;
            }

            public void setIndustry_name(String industry_name) {
                this.industry_name = industry_name;
            }

            public boolean isSelected() {
                return selected;
            }

            public void setSelected(boolean selected) {
                this.selected = selected;
            }

            boolean selected;
        }


        public ArrayList<Salary> getSalary() {
            return salary;
        }

        public void setSalary(ArrayList<Salary> salary) {
            this.salary = salary;
        }

        public ArrayList<Salary> salary;

        public class Salary{
            String  range_id, salary_range;

            public String getRange_id() {
                return range_id;
            }

            public void setRange_id(String industry_id) {
                this.range_id = range_id;
            }

            public String getSalary_range() {
                return salary_range;
            }

            public void setSalary_range(String salary_range) {
                this.salary_range = salary_range;
            }

            public boolean isSelected() {
                return selected;
            }

            public void setSelected(boolean selected) {
                this.selected = selected;
            }

            boolean selected;
        }





    }


}
