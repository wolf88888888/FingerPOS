package com.wolf.fingerpos.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Settings {
    private static SharedPreferences sSharedPreferences;
    private static final String KEY_DEFAULT_OPERATION_MODE = "default_operation_mode";
    private static final String KEY_DEFAULT_SALES_SCREEN = "default_sales_screen";
    private static final String KEY_ACCOUNT_VALIDATION = "account_validation";
    private static final String KEY_ACCOUNT_VEINFINGER_VALIDATION_MODE = "account_veinfinger_validation_mode";
    private static final String KEY_ACCOUNT_VEINFINGER_VALIDATION_TYPE = "account_veinfinger_validation_type";
    private static final String KEY_SERVER_IP = "server_ip";
    private static final String KEY_SERVER_PORT = "server_port";
    private static final String KEY_SERVER_URL = "server_url";
    private static final String KEY_AUTH_ID = "auth_id";
    private static final String KEY_AUTH_PASS = "auth_pass";
    private static final String KEY_SYNC_MODE = "sync_mode";
    private static final String KEY_LAST_RECEIPT = "last_receipt";


    public static SharedPreferences getPreferences(Context context) {
        if (sSharedPreferences == null) {
            sSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        }
        return sSharedPreferences;
    }

    public static void setDefaultOperationMode(Context context, DEFAULT_OPERATION_MODE mode){
        getPreferences(context).edit().putInt(KEY_DEFAULT_OPERATION_MODE, mode.getValue()).apply();
    }

    public static DEFAULT_OPERATION_MODE getDefaultOperationMode(Context context){
        int mode = getPreferences(context).getInt(KEY_DEFAULT_OPERATION_MODE, DEFAULT_OPERATION_MODE.OFFLINE.getValue());
        return DEFAULT_OPERATION_MODE.fromValue(mode);
    }

    public static void setDefaultSalesScreen(Context context, DEFAULT_SALES_SCREEN screen){
        getPreferences(context).edit().putInt(KEY_DEFAULT_SALES_SCREEN, screen.getValue()).apply();
    }

    public static DEFAULT_SALES_SCREEN getDefaultSalesScreen(Context context){
        int screen = getPreferences(context).getInt(KEY_DEFAULT_SALES_SCREEN, DEFAULT_SALES_SCREEN.Pay_N_Go.getValue());
        return DEFAULT_SALES_SCREEN.fromValue(screen);
    }

    public static void setAccountValidation(Context context, ACCOUNT_VALIDATION validation){
        getPreferences(context).edit().putInt(KEY_ACCOUNT_VALIDATION, validation.getValue()).apply();
    }

    public static ACCOUNT_VALIDATION getAccountValidation(Context context){
        int validation = getPreferences(context).getInt(KEY_ACCOUNT_VALIDATION, ACCOUNT_VALIDATION.Account_and_PIN.getValue());
        return ACCOUNT_VALIDATION.fromValue(validation);
    }

    public static void setValidationMode(Context context, ACCOUNT_VEINFINGER_VALIDATION_MODE mode){
        getPreferences(context).edit().putInt(KEY_ACCOUNT_VEINFINGER_VALIDATION_MODE, mode.getValue()).apply();
    }

    public static ACCOUNT_VEINFINGER_VALIDATION_MODE getValidationMode(Context context){
        int mode = getPreferences(context).getInt(KEY_ACCOUNT_VEINFINGER_VALIDATION_MODE, ACCOUNT_VEINFINGER_VALIDATION_MODE.OFFLINE.getValue());
        return ACCOUNT_VEINFINGER_VALIDATION_MODE.fromValue(mode);
    }


    public static void setValidationType(Context context, ACCOUNT_VEINFINGER_VALIDATION_TYPE type){
        getPreferences(context).edit().putInt(KEY_ACCOUNT_VEINFINGER_VALIDATION_TYPE, type.getValue()).apply();
    }

    public static ACCOUNT_VEINFINGER_VALIDATION_TYPE getValidationType(Context context){
        int type = getPreferences(context).getInt(KEY_ACCOUNT_VEINFINGER_VALIDATION_TYPE, ACCOUNT_VEINFINGER_VALIDATION_TYPE.Identification_1_N.getValue());
        return ACCOUNT_VEINFINGER_VALIDATION_TYPE.fromValue(type);
    }

    public static void setServerIP(Context context, String server_ip) {
        getPreferences(context).edit().putString(KEY_SERVER_IP, server_ip).apply();
    }

    public static String getServerIP(Context context) {
        return getPreferences(context).getString(KEY_SERVER_IP, "");
    }

    public static void setServerPort(Context context, String port) {
        getPreferences(context).edit().putString(KEY_SERVER_PORT, port).apply();
    }

    public static String getServerPort(Context context) {
        return getPreferences(context).getString(KEY_SERVER_PORT, "");
    }

    public static void setServerURL(Context context, String url) {
        getPreferences(context).edit().putString(KEY_SERVER_URL, url).apply();
    }

    public static String getServerURL(Context context) {
        return getPreferences(context).getString(KEY_SERVER_URL, "");
    }

    public static void setServerAuthID(Context context, String auth_id) {
        getPreferences(context).edit().putString(KEY_AUTH_ID, auth_id).apply();
    }

    public static String getServerAuthID(Context context) {
        return getPreferences(context).getString(KEY_AUTH_ID, "");
    }

    public static void setServerAuthPass(Context context, String auth_pass) {
        getPreferences(context).edit().putString(KEY_AUTH_PASS, auth_pass).apply();
    }

    public static String getServerAuthPass(Context context) {
        return getPreferences(context).getString(KEY_AUTH_PASS, "");
    }

    public static void setServerSyncMode(Context context, SERVER_SYNC_MODE mode) {
        getPreferences(context).edit().putInt(KEY_SYNC_MODE, mode.getValue()).apply();
    }

    public static SERVER_SYNC_MODE getServerSyncMode(Context context) {
        return SERVER_SYNC_MODE.fromValue(getPreferences(context).getInt(KEY_SYNC_MODE, SERVER_SYNC_MODE.Auto.getValue()));
    }

//    public static int getLastReceipt(Context context){
//        return getPreferences(context).getInt(KEY_LAST_RECEIPT, 0);
//    }
//
//    public static void setLastReceipt(Context context, int last) {
//        SharedPreferences.Editor editor = getPreferences(context).edit();
//        editor.putInt(KEY_LAST_RECEIPT, last);
//        editor.apply();
//    }

    public enum DEFAULT_OPERATION_MODE {
        ONLINE(0), OFFLINE(1);

        private final int value;
        DEFAULT_OPERATION_MODE(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static DEFAULT_OPERATION_MODE fromValue(int value) {
            for (DEFAULT_OPERATION_MODE type : DEFAULT_OPERATION_MODE.values()) {
                if (type.value == value) {
                    return type;
                }
            }
            return null;
        }
    }

    public enum DEFAULT_SALES_SCREEN {
        Pay_N_Go(0), Touch_N_Go(1);

        private final int value;
        DEFAULT_SALES_SCREEN(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static DEFAULT_SALES_SCREEN fromValue(int value) {
            for (DEFAULT_SALES_SCREEN type : DEFAULT_SALES_SCREEN.values()) {
                if (type.value == value) {
                    return type;
                }
            }
            return null;
        }
    }

    public enum ACCOUNT_VALIDATION {
        Account_and_PIN(0), Account_and_VeinFinger(1), VeinFinger_Only(2), RFIDCard_and_PIN(3), RFID_Card_Only(4), EMV_Card_and_PIN(5);

        private final int value;
        ACCOUNT_VALIDATION(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static ACCOUNT_VALIDATION fromValue(int value) {
            for (ACCOUNT_VALIDATION type : ACCOUNT_VALIDATION.values()) {
                if (type.value == value) {
                    return type;
                }
            }
            return null;
        }
    }

    public enum ACCOUNT_VEINFINGER_VALIDATION_MODE {
        ONLINE(0), OFFLINE(1);

        private final int value;
        ACCOUNT_VEINFINGER_VALIDATION_MODE(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static ACCOUNT_VEINFINGER_VALIDATION_MODE fromValue(int value) {
            for (ACCOUNT_VEINFINGER_VALIDATION_MODE type : ACCOUNT_VEINFINGER_VALIDATION_MODE.values()) {
                if (type.value == value) {
                    return type;
                }
            }
            return null;
        }
    }

    public enum ACCOUNT_VEINFINGER_VALIDATION_TYPE {
        Verification_1_1(0), Identification_1_N(1);

        private final int value;
        ACCOUNT_VEINFINGER_VALIDATION_TYPE(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static ACCOUNT_VEINFINGER_VALIDATION_TYPE fromValue(int value) {
            for (ACCOUNT_VEINFINGER_VALIDATION_TYPE type : ACCOUNT_VEINFINGER_VALIDATION_TYPE.values()) {
                if (type.value == value) {
                    return type;
                }
            }
            return null;
        }
    }

    public enum SERVER_SYNC_MODE {
        Auto(0), Manual(1), Semi_Auto(1);

        private final int value;
        SERVER_SYNC_MODE(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static SERVER_SYNC_MODE fromValue(int value) {
            for (SERVER_SYNC_MODE type : SERVER_SYNC_MODE.values()) {
                if (type.value == value) {
                    return type;
                }
            }
            return null;
        }
    }

}
