package com.wolf.fingerpos;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.wolf.fingerpos.database.AccountTable;
import com.wolf.fingerpos.database.PrepayTable;
import com.wolf.fingerpos.utils.GlobalVariable;
import com.wolf.fingerpos.utils.Utils;

import java.util.Calendar;
import java.util.Date;

import io.realm.RealmQuery;
import io.realm.RealmResults;

public class AccountManageActivity extends AppCompatActivity {
    public final static String MANAGE_TYPE = "manage_type";
    public final static int NEW_ACCOUNT = 1;
    public final static int EDIT_ACCOUNT = 2;
    public final static String KEY_ACCOUNTID = "key_accountid";

    private int manage_type = NEW_ACCOUNT;

    EditText etAccountID, etFirstName, etOtherName, etNationalID;
    Spinner spCustomType, spStatus;
    Spinner spAccountType;
    EditText etBalance;

    private String accountid_edit;
    private AccountTable account_edit = null;
    private PrepayTable prepay_info = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_account_manage);

        manage_type = getIntent().getIntExtra(MANAGE_TYPE, NEW_ACCOUNT);
        if (manage_type == EDIT_ACCOUNT) {
            accountid_edit = getIntent().getStringExtra(KEY_ACCOUNTID);

            RealmQuery<AccountTable> queryUsers = POSApplication.mRealmDB.where(AccountTable.class);
            queryUsers.equalTo("id", accountid_edit);
            RealmResults<AccountTable> resultAccount = queryUsers.findAll();
            if (resultAccount.size() == 0) {
                Toast.makeText(this, "Cannot find account. Please try again.", Toast.LENGTH_LONG).show();
                finish();
            }

            account_edit = resultAccount.get(0);

            RealmQuery<PrepayTable> queryPrepay = POSApplication.mRealmDB.where(PrepayTable.class);
            queryPrepay.equalTo("account_id", accountid_edit);
            RealmResults<PrepayTable> resultPrepay = queryPrepay.findAll();
            if (resultPrepay.size() != 0) {
                prepay_info = resultPrepay.get(0);
            }
        }
        initWidget();
    }

    void initWidget(){
        etAccountID = (EditText) findViewById(R.id.etAccountID);
        etFirstName = (EditText) findViewById(R.id.etFirstName);
        etOtherName = (EditText) findViewById(R.id.etOtherName);
        etNationalID = (EditText) findViewById(R.id.etNationalID);
        etBalance = (EditText) findViewById(R.id.etBalance);

        spCustomType = (Spinner) findViewById(R.id.spCustomType);
        spStatus = (Spinner) findViewById(R.id.spStatus);
        spAccountType = (Spinner) findViewById(R.id.spAccountType);

        // Creating adapter for spinner
        ArrayAdapter<String> adapterCustomType = new ArrayAdapter<String>(this, R.layout.simple_spinner_item, GlobalVariable.getInstance(AccountManageActivity.this).g_arrayCustomType);
        // Drop down layout style - list view with radio button
        adapterCustomType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spCustomType.setAdapter(adapterCustomType);

        // Creating adapter for spinner
        ArrayAdapter<String> adapterStatus = new ArrayAdapter<String>(this, R.layout.simple_spinner_item, GlobalVariable.getInstance(AccountManageActivity.this).g_arrayStatus);
        // Drop down layout style - list view with radio button
        adapterStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spStatus.setAdapter(adapterStatus);

        // Creating adapter for spinner
        ArrayAdapter<String> adapterAccountType = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, GlobalVariable.getInstance(AccountManageActivity.this).g_arrayAccountType);
        // Drop down layout style - list view with radio button
        adapterAccountType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spAccountType.setAdapter(adapterAccountType);


        if (account_edit != null) {
            etAccountID.setText(account_edit.getId());
            etFirstName.setText(account_edit.getFirst_name());
            etOtherName.setText(account_edit.getLast_name());
            etNationalID.setText(account_edit.getNational_id());
            spCustomType.setSelection(account_edit.getType());
            spStatus.setSelection(account_edit.getStatus());

            if (prepay_info != null) {
                spAccountType.setSelection(prepay_info.getType());
                etBalance.setText(String.valueOf(prepay_info.getAmount()));
            }

            etAccountID.setEnabled(false);
//            etAccountID.setFocusable(false);
//            etAccountID.setFocusableInTouchMode(false);
        }
    }

    public void onBack(View v) {
        finish();
    }

    public void onSave(View v) {
        if (manage_type != EDIT_ACCOUNT) {
            //Toast.makeText(this, "Don't exist user. Please press New button to create user.", Toast.LENGTH_LONG).show();

            if (etAccountID.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please input UserID.", Toast.LENGTH_LONG).show();
                return;
            }

            if (etFirstName.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please input First Name", Toast.LENGTH_LONG).show();
            }

            if (etOtherName.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please input Other Name", Toast.LENGTH_LONG).show();
            }


//        if (etNationalID.getText().toString().isEmpty()) {
//            Toast.makeText(this, "Please input National ID", Toast.LENGTH_LONG).show();
//        }
            POSApplication.mRealmDB.beginTransaction();

            AccountTable account = POSApplication.mRealmDB.createObject(AccountTable.class, System.currentTimeMillis());

            account.setId(etAccountID.getText().toString());
            account.setFirst_name(etFirstName.getText().toString());
            account.setLast_name(etOtherName.getText().toString());
            account.setNational_id(etNationalID.getText().toString());
            account.setType(spCustomType.getSelectedItemPosition());
            account.setStatus(spStatus.getSelectedItemPosition());
//
//        POSApplication.mRealmDB.commitTransaction();
//
//        POSApplication.mRealmDB.beginTransaction();

            if (!etBalance.getText().toString().isEmpty()) {
                float fBalance = 0.0f;
                try {
                    fBalance = Float.valueOf(etBalance.getText().toString());
                    PrepayTable prepay = POSApplication.mRealmDB.createObject(PrepayTable.class, System.currentTimeMillis());
                    prepay.setAccount_id(account.getId());
                    prepay.setType(spAccountType.getSelectedItemPosition());
                    prepay.setAmount(fBalance);

                    Date currentDate = new Date();
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(currentDate);
                    calendar.add(Calendar.MONTH, 1);
                    Date nextDate = calendar.getTime();

                    prepay.setCreate_date(currentDate.getTime());
                    prepay.setFrom(currentDate.getTime());
                    prepay.setTo(nextDate.getTime());
                    prepay.setMorning(true);
                    prepay.setTen_oclock(true);
                    prepay.setLunch(true);
                    prepay.setEvening(true);
                    prepay.setSupper(true);
                } catch (NumberFormatException ex) {
                    ex.printStackTrace();
                }
            }

            POSApplication.mRealmDB.commitTransaction();
            setResult(RESULT_OK);
            finish();
            return;
        }

        float fBalance = 0.0f;
        try {
            fBalance = Float.valueOf(etBalance.getText().toString());
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        }

        POSApplication.mRealmDB.beginTransaction();

        account_edit.setId(etAccountID.getText().toString());
        account_edit.setFirst_name(etFirstName.getText().toString());
        account_edit.setLast_name(etOtherName.getText().toString());
        account_edit.setNational_id(etNationalID.getText().toString());
        account_edit.setType(spCustomType.getSelectedItemPosition());
        account_edit.setStatus(spStatus.getSelectedItemPosition());

        if (prepay_info == null){
            prepay_info = POSApplication.mRealmDB.createObject(PrepayTable.class, System.currentTimeMillis());

            prepay_info.setAccount_id(etAccountID.getText().toString());

            Date currentDate = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(currentDate);
            calendar.add(Calendar.MONTH, 1);
            Date nextDate = calendar.getTime();

            prepay_info.setCreate_date(currentDate.getTime());
            prepay_info.setFrom(currentDate.getTime());
            prepay_info.setTo(nextDate.getTime());
            prepay_info.setMorning(true);
            prepay_info.setTen_oclock(true);
            prepay_info.setLunch(true);
            prepay_info.setEvening(true);
            prepay_info.setSupper(true);
        }
        prepay_info.setType(spAccountType.getSelectedItemPosition());
        prepay_info.setAmount(fBalance);

        POSApplication.mRealmDB.commitTransaction();
        setResult(RESULT_OK);
        finish();
    }


    public void onNew(View v) {
        if (manage_type != NEW_ACCOUNT) {
            Toast.makeText(this, "Already exist account. Please press Save button to save info.", Toast.LENGTH_LONG).show();
            return;
        }

        etAccountID.setText("");
        etFirstName.setText("");
        etOtherName.setText("");
        etNationalID.setText("");
        etBalance.setText("");
    }

    public void onClose(View v) {
        finish();
    }
}
