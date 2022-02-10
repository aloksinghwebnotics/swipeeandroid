package com.webnotics.swipee.room_database;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "collegeList",
        indices = {@Index(value = "university_college_id", unique = true)})
public class College_model {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "university_college_id")
    private String university_college_id;

    @ColumnInfo(name = "university_college_name")
    private String university_college_name;

   @ColumnInfo(name = "selected")
    private int selected;

    public College_model(String university_college_id, String university_college_name, int selected) {
        this.university_college_id = university_college_id;
        this.university_college_name = university_college_name;
        this.selected = selected;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUniversity_college_id() {
        return university_college_id;
    }

    public void setUniversity_college_id(String university_college_id) {
        this.university_college_id = university_college_id;
    }

    public String getUniversity_college_name() {
        return university_college_name;
    }

    public void setUniversity_college_name(String university_college_name) {
        this.university_college_name = university_college_name;
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    @Override
    public String toString() {
        return "College_model{" +
                "id=" + id +
                ", university_college_id='" + university_college_id + '\'' +
                ", university_college_name='" + university_college_name + '\'' +
                ", selected=" + selected +
                '}';
    }
}
