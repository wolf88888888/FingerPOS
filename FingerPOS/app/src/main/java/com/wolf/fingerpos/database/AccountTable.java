package com.wolf.fingerpos.database;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class AccountTable extends RealmObject {
    @PrimaryKey
    private long index;

    private String id;
    private String first_name;
    private String last_name;
    private String national_id;
    private int type;
    private int status;

    private String finger0;
    private String finger1;
    private String vein0;
    private String vein1;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getNational_id() {
        return national_id;
    }

    public void setNational_id(String national_id) {
        this.national_id = national_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getFinger0() {
        return finger0;
    }

    public void setFinger0(String finger0) {
        this.finger0 = finger0;
    }

    public String getFinger1() {
        return finger1;
    }

    public void setFinger1(String finger1) {
        this.finger1 = finger1;
    }

    public String getVein0() {
        return vein0;
    }

    public void setVein0(String vein0) {
        this.vein0 = vein0;
    }

    public String getVein1() {
        return vein1;
    }

    public void setVein1(String vein1) {
        this.vein1 = vein1;
    }
}
