package com.wolf.fingerpos.utils;

import android.content.Context;

import com.wolf.fingerpos.R;

import java.util.Arrays;
import java.util.List;

public class GlobalVariable {
    private static GlobalVariable instance = null;

    public List<String> g_arrayCustomType = null;
    public List<String> g_arrayStatus = null;
    public List<String> g_arrayAccountType = null;
    public List<String> g_arrayPayMode = null;
    public List<String> g_arrayUserType = null;
    public String g_userid = "";

    public static GlobalVariable getInstance(Context context) {
        if (instance == null) {
            instance = new GlobalVariable(context);
        }
        return instance;
    }

    private GlobalVariable(Context context) {
        g_arrayCustomType = Arrays.asList(context.getResources().getStringArray(R.array.customer_type));
        g_arrayStatus = Arrays.asList(context.getResources().getStringArray(R.array.status));
        g_arrayAccountType = Arrays.asList(context.getResources().getStringArray(R.array.account_types));
        g_arrayPayMode = Arrays.asList(context.getResources().getStringArray(R.array.pay_mode));
        g_arrayUserType = Arrays.asList(context.getResources().getStringArray(R.array.user_type));
    }

    public enum PAYMENT_TYPE {
        CASH(0), CREDIT(1), MPESA(2), ACC_PREPAY(3), ACC_POSTPAID(4), ETC(5);

        private final int value;
        PAYMENT_TYPE(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static PAYMENT_TYPE fromValue(int value) {
            for (PAYMENT_TYPE type : PAYMENT_TYPE.values()) {
                if (type.value == value) {
                    return type;
                }
            }
            return null;
        }
    }
}
