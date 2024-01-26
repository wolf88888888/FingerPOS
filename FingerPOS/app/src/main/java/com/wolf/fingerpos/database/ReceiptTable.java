package com.wolf.fingerpos.database;


import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;



public class ReceiptTable extends RealmObject {
    @PrimaryKey
    private long id;

    private RealmList<SoldItemTable> listSold;
    float total;
    float trans;
    int pay_type;//0:cash 1:credit 2:mpesa, 3:acc_pre 4:acc_post
    long pay_time;
    String type_ref;
    String user_id;

    public long getId() {
        return id;
    }

    public void setListSold(RealmList<SoldItemTable> listSold) {
        this.listSold = listSold;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public void setTrans(float trans) {
        this.trans = trans;
    }

    public void setPay_type(int pay_type) {
        this.pay_type = pay_type;
    }

    public void setPay_time(long pay_time) {
        this.pay_time = pay_time;
    }

    public void setType_ref(String type_ref) {
        this.type_ref = type_ref;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public RealmList<SoldItemTable> getListSold() {
        return listSold;
    }

    public float getTotal() {
        return total;
    }

    public float getTrans() {
        return trans;
    }

    public int getPay_type() {
        return pay_type;
    }

    public long getPay_time() {
        return pay_time;
    }

    public String getType_ref() {
        return type_ref;
    }

    public String getUser_id() {
        return user_id;
    }
}
