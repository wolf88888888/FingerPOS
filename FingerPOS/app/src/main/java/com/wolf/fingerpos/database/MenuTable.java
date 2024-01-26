package com.wolf.fingerpos.database;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class MenuTable extends RealmObject {
    @PrimaryKey
    private long index;

    private String code;
    private String desc;
    private float price;
    private boolean mon;
    private boolean tue;
    private boolean wed;
    private boolean thu;
    private boolean fri;
    private boolean sat;
    private boolean sun;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setMon(boolean mon) {
        this.mon = mon;
    }

    public void setTue(boolean tue) {
        this.tue = tue;
    }

    public void setWed(boolean wed) {
        this.wed = wed;
    }

    public void setThu(boolean thu) {
        this.thu = thu;
    }

    public void setFri(boolean fri) {
        this.fri = fri;
    }

    public void setSat(boolean sat) {
        this.sat = sat;
    }

    public void setSun(boolean sun) {
        this.sun = sun;
    }

    public boolean getMon() {
        return mon;
    }

    public boolean getTue() {
        return tue;
    }

    public boolean getWed() {
        return wed;
    }

    public boolean getThu() {
        return thu;
    }

    public boolean getFri() {
        return fri;
    }

    public boolean getSat() {
        return sat;
    }

    public boolean getSun() {
        return sun;
    }
}
