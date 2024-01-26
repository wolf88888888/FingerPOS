package com.wolf.fingerpos;


import android.app.Application;

import com.wolf.fingerpos.database.POSMigration;
import com.wolf.fingerpos.database.ReceiptTable;
import com.wolf.fingerpos.database.SoldItemTable;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class POSApplication extends Application {
    public static Realm mRealmDB;

    @Override
    public void onCreate() {
        super.onCreate();
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("com.wolf.fingerpos.realm")
                //.encryptionKey()
                .schemaVersion(7)
                .migration(new POSMigration())
                .build();
        //Realm.setDefaultConfiguration(config);
        mRealmDB = Realm.getInstance(config);
        //Realm.getDefaultInstance();

//        RealmQuery<SoldItemTable> query0 = POSApplication.mRealmDB.where(SoldItemTable.class);
//        final RealmResults<SoldItemTable> result0 = query0.findAll();
//
//        RealmQuery<ReceiptTable> query1 = POSApplication.mRealmDB.where(ReceiptTable.class);
//        final RealmResults<ReceiptTable> result1 = query1.findAll();
//        mRealmDB.executeTransaction(new Realm.Transaction() {
//            @Override
//            public void execute(Realm realm) {
//                result0.deleteAllFromRealm();
//                result1.deleteAllFromRealm();
//            }
//        });

//        RealmQuery<ReceiptTable> queryReceipts = POSApplication.mRealmDB.where(ReceiptTable.class);
//        final RealmResults<ReceiptTable> resultReceipts = queryReceipts.findAll();
//
//        for (int i = 0; i < resultReceipts.size(); i ++) {
//            for (int j = 0; j < resultReceipts.get(i).getListSold().size(); j ++) {
//
//                SoldItemTable item = resultReceipts.get(i).getListSold().get(j);
//
//                POSApplication.mRealmDB.beginTransaction();
//
//                //item.setReceipt_id(resultReceipts.get(i).getId());
//                //item.setPay_time(resultReceipts.get(i).getPay_time());
//                item.setPay_type(resultReceipts.get(i).getPay_type());
//
//                POSApplication.mRealmDB.commitTransaction();
//            }
//        }
    }
}
