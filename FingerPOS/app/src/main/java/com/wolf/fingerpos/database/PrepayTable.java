package com.wolf.fingerpos.database;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class PrepayTable extends RealmObject {
    @PrimaryKey
    private long index;

    private String account_id;
    private int type;
    private float amount;
    private long from;
    private long to;
    private long create_date;
    private boolean morning;
    private boolean ten_oclock;
    private boolean lunch;
    private boolean evening;
    private boolean supper;

    public String getAccount_id() {
        return account_id;
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public long getFrom() {
        return from;
    }

    public void setFrom(long from) {
        this.from = from;
    }

    public long getTo() {
        return to;
    }

    public void setTo(long to) {
        this.to = to;
    }

    public long getCreate_date() {
        return create_date;
    }

    public void setCreate_date(long create_date) {
        this.create_date = create_date;
    }

    public boolean getMorning() {
        return morning;
    }

    public void setMorning(boolean morning) {
        this.morning = morning;
    }

    public boolean getTen_oclock() {
        return ten_oclock;
    }

    public void setTen_oclock(boolean ten_oclock) {
        this.ten_oclock = ten_oclock;
    }

    public boolean getLunch() {
        return lunch;
    }

    public void setLunch(boolean lunch) {
        this.lunch = lunch;
    }

    public boolean getEvening() {
        return evening;
    }

    public void setEvening(boolean evening) {
        this.evening = evening;
    }

    public boolean getSupper() {
        return supper;
    }

    public void setSupper(boolean supper) {
        this.supper = supper;
    }
}
