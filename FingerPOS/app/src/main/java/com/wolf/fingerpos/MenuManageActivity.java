package com.wolf.fingerpos;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.wolf.fingerpos.database.MenuTable;

import io.realm.RealmQuery;
import io.realm.RealmResults;

public class MenuManageActivity extends AppCompatActivity {
    public final static String MANAGE_TYPE = "manage_type";
    public final static int NEW = 1;
    public final static int EDIT = 2;
    public final static String KEY_CODE = "key_code";
    public String menuCode_edit;
    public MenuTable menuEdit;

    private int manage_type = NEW;

    private EditText etCode, etDesc, etPrice;
    private CheckBox cbMon, cbTue, cbWed, cbThu, cbFri, cbSat, cbSun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_new_menu);

        manage_type = getIntent().getIntExtra(MANAGE_TYPE, NEW);
        if (manage_type == EDIT) {
            menuCode_edit = getIntent().getStringExtra(KEY_CODE);

            RealmQuery<MenuTable> queryMenu = POSApplication.mRealmDB.where(MenuTable.class);
            queryMenu.equalTo("code", menuCode_edit);
            RealmResults<MenuTable> resultMenu = queryMenu.findAll();
            if (resultMenu.size() == 0) {
                Toast.makeText(this, "Cannot find menu to edit. Please try again.", Toast.LENGTH_LONG).show();
                finish();
            }

            menuEdit = resultMenu.get(0);
        }
        initWidget();
    }

    void initWidget(){
        etCode = (EditText)findViewById(R.id.etCode);
        etDesc = (EditText)findViewById(R.id.etDesc);
        etPrice = (EditText)findViewById(R.id.etPrice);
        cbMon = (CheckBox)findViewById(R.id.cbMon);
        cbTue = (CheckBox)findViewById(R.id.cbTue);
        cbWed = (CheckBox)findViewById(R.id.cbWed);
        cbThu = (CheckBox)findViewById(R.id.cbThu);
        cbFri = (CheckBox)findViewById(R.id.cbFri);
        cbSat = (CheckBox)findViewById(R.id.cbSat);
        cbSun = (CheckBox)findViewById(R.id.cbSun);

        if (menuEdit != null) {
            etCode.setText(menuEdit.getCode());
            etDesc.setText(menuEdit.getDesc());
            etPrice.setText(String.valueOf(menuEdit.getPrice()));
            cbMon.setChecked(menuEdit.getMon());
            cbTue.setChecked(menuEdit.getThu());
            cbWed.setChecked(menuEdit.getWed());
            cbThu.setChecked(menuEdit.getThu());
            cbFri.setChecked(menuEdit.getFri());
            cbSat.setChecked(menuEdit.getSat());
            cbSun.setChecked(menuEdit.getSun());

            etCode.setEnabled(false);
            //etCode.setFocusable(false);
            //etCode.setFocusableInTouchMode(false);
        }
    }

    public void onBack(View v) {
        finish();
    }

    boolean checkEmpty() {
        if (etCode.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please input Code.", Toast.LENGTH_LONG).show();
        }

        if (etDesc.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please input Description", Toast.LENGTH_LONG).show();
        }

        if (etPrice.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please input Price", Toast.LENGTH_LONG).show();
        }
        else {
            return true;
        }
        return false;
    }

    public void onSave(View v) {
        if (manage_type != EDIT) {
            //Toast.makeText(this, "Don't exist item. Please press \"New\" to create item.", Toast.LENGTH_LONG).show();
            if (!checkEmpty())
                return;

            float fPrice = 0.0f;
            try {
                fPrice = Float.valueOf(etPrice.getText().toString());
            }catch (NumberFormatException ex) {
                ex.printStackTrace();
            }

            POSApplication.mRealmDB.beginTransaction();

            MenuTable menu = POSApplication.mRealmDB.createObject(MenuTable.class, System.currentTimeMillis());

            menu.setCode(etCode.getText().toString());
            menu.setDesc(etDesc.getText().toString());
            menu.setPrice(fPrice);
            menu.setMon(cbMon.isChecked());
            menu.setTue(cbTue.isChecked());
            menu.setWed(cbWed.isChecked());
            menu.setThu(cbThu.isChecked());
            menu.setFri(cbFri.isChecked());
            menu.setSat(cbSat.isChecked());
            menu.setSun(cbSun.isChecked());

            POSApplication.mRealmDB.commitTransaction();

            setResult(RESULT_OK);
            finish();
            return;
        }
        if (!checkEmpty())
            return;

        float fPrice = 0.0f;
        try {
            fPrice = Float.valueOf(etPrice.getText().toString());
        }catch (NumberFormatException ex) {
            ex.printStackTrace();
        }

        POSApplication.mRealmDB.beginTransaction();

        menuEdit.setCode(etCode.getText().toString());
        menuEdit.setDesc(etDesc.getText().toString());
        menuEdit.setPrice(fPrice);
        menuEdit.setMon(cbMon.isChecked());
        menuEdit.setTue(cbTue.isChecked());
        menuEdit.setWed(cbWed.isChecked());
        menuEdit.setThu(cbThu.isChecked());
        menuEdit.setFri(cbFri.isChecked());
        menuEdit.setSat(cbSat.isChecked());
        menuEdit.setSun(cbSun.isChecked());

        POSApplication.mRealmDB.commitTransaction();
        setResult(RESULT_OK);
        finish();
    }

    public void onNew(View v) {
        if (manage_type != NEW) {
            Toast.makeText(this, "Already exist item. Please press \"Save\" to save change.", Toast.LENGTH_LONG).show();
            return;
        }

        etCode.setText("");
        etDesc.setText("");
        etPrice.setText("");
        cbMon.setChecked(true);
        cbTue.setChecked(true);
        cbWed.setChecked(true);
        cbThu.setChecked(true);
        cbFri.setChecked(true);
        cbSat.setChecked(true);
        cbSun.setChecked(true);
    }

    public void onClose(View v) {
        finish();
    }
}
