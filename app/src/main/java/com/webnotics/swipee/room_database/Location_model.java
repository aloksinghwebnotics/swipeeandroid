package com.webnotics.swipee.room_database;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "locationList",
        indices = {@Index(value = "location_id", unique = true)})
public class Location_model {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "location_id")
    private String location_id;

    @ColumnInfo(name = "location_name")
    private String location_name;

 @ColumnInfo(name = "state_name")
    private String state_name;

   @ColumnInfo(name = "selected")
    private int selected;

    public Location_model(String location_id, String location_name, String state_name, int selected) {
        this.location_id = location_id;
        this.location_name = location_name;
        this.state_name = state_name;
        this.selected = selected;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getState_name() {
        return state_name;
    }

    public void setState_name(String state_name) {
        this.state_name = state_name;
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    @Override
    public String toString() {
        return "Location_model{" +
                "id=" + id +
                ", location_id='" + location_id + '\'' +
                ", location_name='" + location_name + '\'' +
                ", state_name='" + state_name + '\'' +
                ", selected=" + selected +
                '}';
    }
}
