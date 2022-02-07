package com.webnotics.swipee.model.seeker;

public class FilterModelSelected {
    String name;
    String state="";
    String id;
    boolean selected;

    public boolean isDistance() {
        return distance;
    }

    public void setDistance(boolean distance) {
        this.distance = distance;
    }

    boolean distance=false;

    public FilterModelSelected(String name, String id,String state, boolean selected) {
        this.name = name;
        this.state = state;
        this.id = id;
        this.selected = selected;
    }

    public FilterModelSelected(String name, String id,String state, boolean selected, boolean distance) {
        this.name = name;
        this.state = state;
        this.id = id;
        this.selected = selected;
        this.distance = distance;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public FilterModelSelected() {
    }
}
