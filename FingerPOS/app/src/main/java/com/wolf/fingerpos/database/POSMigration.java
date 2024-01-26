package com.wolf.fingerpos.database;

import io.realm.DynamicRealm;
import io.realm.DynamicRealmObject;
import io.realm.FieldAttribute;
import io.realm.RealmMigration;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;

public class POSMigration implements RealmMigration{
    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        // DynamicRealm exposes an editable schema
        RealmSchema schema = realm.getSchema();
        if (oldVersion == 1) {
            schema.get("MenuTable")
                    .addField("code_tmp", String.class)
                    .transform(new RealmObjectSchema.Function() {
                        @Override
                        public void apply(DynamicRealmObject obj) {
                            obj.setString("code_tmp", String.valueOf(obj.getInt("code")));
                        }
                    })
                    .removeField("code")
                    .renameField("code_tmp", "code");
            oldVersion++;
        }
        if (oldVersion == 2) {
            schema.create("SoldItemTable")
                    .addField("id", long.class, FieldAttribute.PRIMARY_KEY)
                    .addField("code", String.class)
                    .addField("qty", int.class);

            schema.create("ReceiptTable")
                    .addField("id", long.class, FieldAttribute.PRIMARY_KEY)
                    .addRealmListField("listSold", schema.get("SoldItemTable"))
                    .addField("total", float.class)
                    .addField("trans", float.class)
                    .addField("pay_type", int.class)
                    .addField("pay_time", long.class)
                    .addField("account_id", String.class);
            oldVersion++;
        }
        if (oldVersion == 3) {
            schema.get("ReceiptTable")
                    .addField("user_id", String.class);
            oldVersion++;
        }
        if (oldVersion == 4) {
            schema.get("ReceiptTable")
                    .renameField("account_id", "type_ref");
            oldVersion++;
        }

        if (oldVersion == 5) {
            schema.get("SoldItemTable")
                    .addField("receipt_id", long.class);
            oldVersion++;
        }

        if (oldVersion == 6) {
            schema.get("SoldItemTable")
                    .addField("pay_time", long.class);
            oldVersion++;
        }

        if (oldVersion == 7) {
            schema.get("SoldItemTable")
                    .addField("pay_type", long.class);
            oldVersion++;
        }
        //refer https://realm.io/docs/java/latest/#migrations

        //https://github.com/realm/realm-java/tree/master/examples/migrationExample
    }
}
