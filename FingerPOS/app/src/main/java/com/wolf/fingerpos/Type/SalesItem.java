package com.wolf.fingerpos.Type;

public class SalesItem {
    public String desc;
    public String code;
    public int qty;
    public float price;
    public float total;

    public SalesItem(String code, String desc, int qty, float price, float total){
        this.code = code;
        this.desc = desc;
        this.qty = qty;
        this.price = price;
        this.total = total;
    }
}
