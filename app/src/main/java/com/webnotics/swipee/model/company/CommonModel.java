package com.webnotics.swipee.model.company;

public class CommonModel {

    String id,name,level="";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    boolean selected;

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public CommonModel(String id, String name, boolean selected) {
        this.id = id;
        this.name = name;
        this.selected = selected;
    }

    public CommonModel(String id, String name, String level, boolean selected) {
        this.id = id;
        this.name = name;
        this.level = level;
        this.selected = selected;
    }

    public CommonModel() {
    }
}
