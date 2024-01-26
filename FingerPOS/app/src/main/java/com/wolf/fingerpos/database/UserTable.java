package com.wolf.fingerpos.database;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class UserTable extends RealmObject {
    @PrimaryKey
    private long index;

    private String id;
    private String name;
    private int type;
    private int status;
    private String avatar;
    private boolean super_user;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public boolean getSuper_user() {
        return super_user;
    }

    public void setSuper_user(boolean super_user) {
        this.super_user = super_user;
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
