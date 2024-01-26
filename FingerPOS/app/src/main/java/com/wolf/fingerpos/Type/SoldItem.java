package com.wolf.fingerpos.Type;

import com.wolf.fingerpos.database.SoldItemTable;

import io.realm.RealmList;
import io.realm.RealmResults;

public class SoldItem {
    public int id;
    public String code;
    public String desc;
    public float price;
    public int qty_stk;
    public int qty_sold;
    public int qty_bal;
    public float stk_value;
    public float sold_value;
    public float bal_value;
    public RealmResults<SoldItemTable> resultItems;

    public SoldItem(String code, String desc, float price, int qty_stk, int qty_sold, int qty_bal, float stk_value, float sold_value, float bal_value) {
        this.code = code;
        this.desc = desc;
        this.price = price;
        this.qty_stk = qty_stk;
        this.qty_sold = qty_sold;
        this.qty_bal = qty_bal;
        this.stk_value = stk_value;
        this.sold_value = sold_value;
        this.bal_value = bal_value;
    }
}
