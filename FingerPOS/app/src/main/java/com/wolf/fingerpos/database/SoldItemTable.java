package com.wolf.fingerpos.database;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class SoldItemTable extends RealmObject {
    @PrimaryKey
    private long id;
    private String code;
    private int qty;
    private long receipt_id;
    private long pay_time;
    private int pay_type;
    String type_ref;
    String user_id;

    public void setCode(String code) {
        this.code = code;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public void setReceipt_id(long receipt_id) {
        this.receipt_id = receipt_id;
    }

    public void setPay_time(long pay_time) {
        this.pay_time = pay_time;
    }

    public void setPay_type(int pay_type) {
        this.pay_type = pay_type;
    }

    public void setType_ref(String type_ref) {
        this.type_ref = type_ref;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCode() {
        return code;
    }

    public int getQty() {
        return qty;
    }

    public long getReceipt_id() {
        return receipt_id;
    }

    public long getPay_time() {
        return pay_time;
    }

    public int getPay_type() {
        return pay_type;
    }

    public String getType_ref() {
        return type_ref;
    }

    public String getUser_id() {
        return user_id;
    }
}
