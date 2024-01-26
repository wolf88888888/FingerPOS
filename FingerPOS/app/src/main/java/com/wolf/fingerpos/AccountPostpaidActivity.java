package com.wolf.fingerpos;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.wolf.fingerpos.Dialog.RegNumberDialog;
import com.wolf.fingerpos.View.EditTextDatePicker;
import com.wolf.fingerpos.database.AccountTable;
import com.wolf.fingerpos.database.PostpaidTable;
import com.wolf.fingerpos.database.PrepayTable;
import com.wolf.fingerpos.utils.GlobalVariable;
import com.wolf.fingerpos.utils.Utils;

import java.util.Arrays;
import java.util.List;

import io.realm.RealmQuery;
import io.realm.RealmResults;

public class AccountPostpaidActivity extends AppCompatActivity implements RegNumberDialog.RegNumberDialogListener {
    TextView tvID, tvFirstName, tvOtherName;
    EditText etAmount;
    Spinner spAccountType;
    private EditTextDatePicker etFrom;
    private EditTextDatePicker etTo;
    CheckBox cbMorning, cbTen, cbLunch, cbEvening, cbSupper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_account_postpaid);
        initWidget();
    }

    void initWidget(){
        tvID = (TextView)findViewById(R.id.tvID);
        tvFirstName = (TextView)findViewById(R.id.tvFirstName);
        tvOtherName = (TextView)findViewById(R.id.tvOtherName);
        etAmount = (EditText)findViewById(R.id.etAmount);

        etFrom = new EditTextDatePicker(this, R.id.etFrom); etFrom.clear();
        etTo = new EditTextDatePicker(this, R.id.etTo);     etTo.clear();

        spAccountType = (Spinner)findViewById(R.id.spAccountType);

        // Creating adapter for spinner
        ArrayAdapter<String> adapterAccountType = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, GlobalVariable.getInstance(this).g_arrayAccountType);
        // Drop down layout style - list view with radio button
        adapterAccountType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner

        cbMorning = (CheckBox)findViewById(R.id.cbMorning);
        cbTen = (CheckBox)findViewById(R.id.cbTen);
        cbLunch = (CheckBox)findViewById(R.id.cbLunch);
        cbEvening = (CheckBox)findViewById(R.id.cbEvening);
        cbSupper = (CheckBox)findViewById(R.id.cbSupper);
        spAccountType.setAdapter(adapterAccountType);
    }

    public void onBack(View v) {
        finish();
    }

    public void onGet(View v) {
        RegNumberDialog dialog = new RegNumberDialog(this, this);
        dialog.show();
    }

    AccountTable accountInfo = null;
    PrepayTable prepayInfo = null;
    PostpaidTable postpaidInfo = null;

    void loadUserInfo(String accountID) {
        RealmQuery<AccountTable> queryUsers = POSApplication.mRealmDB.where(AccountTable.class);
        queryUsers.equalTo("id", accountID);
        RealmResults<AccountTable> resultAccount = queryUsers.findAll();
        if (resultAccount.size() == 0) {
            Toast.makeText(this, "Cannot find account. Please try again.", Toast.LENGTH_LONG).show();
            return;
        }

        accountInfo = resultAccount.get(0);

        RealmQuery<PrepayTable> queryPrepay = POSApplication.mRealmDB.where(PrepayTable.class);
        queryPrepay.equalTo("account_id", accountID);
        RealmResults<PrepayTable> resultPrepay = queryPrepay.findAll();
        if (resultPrepay.size() != 0) {
            prepayInfo = resultPrepay.get(0);
        }

        RealmQuery<PostpaidTable> queryPostpaid = POSApplication.mRealmDB.where(PostpaidTable.class);
        queryPostpaid.equalTo("account_id", accountID);
        RealmResults<PostpaidTable> resultPostpaid = queryPostpaid.findAll();
        if (resultPostpaid.size() != 0) {
            postpaidInfo = resultPostpaid.get(0);
        }

        if (prepayInfo != null) {
            accountID = null;
            Toast.makeText(this, "This Account already has prepay.", Toast.LENGTH_LONG).show();
            return;
        }

        tvID.setText(accountInfo.getId());
        tvFirstName.setText(accountInfo.getFirst_name());
        tvOtherName.setText(accountInfo.getLast_name());
        if (postpaidInfo == null)
            return;
        spAccountType.setSelection(postpaidInfo.getType());
        etAmount.setText(String.format("%.2f", postpaidInfo.getAmount()));

        etFrom.setDate(Utils.getYear(postpaidInfo.getFrom()), Utils.getMon(postpaidInfo.getFrom()), Utils.getDay(postpaidInfo.getFrom()));
        etTo.setDate(Utils.getYear(postpaidInfo.getTo()), Utils.getMon(postpaidInfo.getTo()), Utils.getDay(postpaidInfo.getTo()));

        cbMorning.setChecked(postpaidInfo.getMorning());
        cbTen.setChecked(postpaidInfo.getMorning());
        cbLunch.setChecked(postpaidInfo.getMorning());
        cbEvening.setChecked(postpaidInfo.getMorning());
        cbSupper.setChecked(postpaidInfo.getMorning());
    }

    public void onSave(View v) {
        if (accountInfo == null) {
            //Toast.makeText(this, "Please select account first.", Toast.LENGTH_LONG).show();
            return;
        }

        float fBalance = 0.0f;
        try {
            fBalance = Float.valueOf(etAmount.getText().toString());
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        }

        POSApplication.mRealmDB.beginTransaction();

        if (postpaidInfo == null){
            postpaidInfo = POSApplication.mRealmDB.createObject(PostpaidTable.class, System.currentTimeMillis());
            postpaidInfo.setCredit(0);
            postpaidInfo.setPayment(0);
        }
        postpaidInfo.setAccount_id(accountInfo.getId());
        postpaidInfo.setType(spAccountType.getSelectedItemPosition());
        postpaidInfo.setAmount(fBalance);
        postpaidInfo.setFrom(etFrom.getCurrentDate());
        postpaidInfo.setTo(etFrom.getCurrentDate());

        postpaidInfo.setMorning(cbMorning.isChecked());
        postpaidInfo.setTen_oclock(cbTen.isChecked());
        postpaidInfo.setLunch(cbLunch.isChecked());
        postpaidInfo.setEvening(cbEvening.isChecked());
        postpaidInfo.setSupper(cbSupper.isChecked());

        POSApplication.mRealmDB.commitTransaction();
        setResult(RESULT_OK);
        finish();
    }

    public void onNew(View v) {
        //onSave(v);
        tvID.setText("");
        tvFirstName.setText("");
        tvOtherName.setText("");
        etAmount.setText("");
        etFrom.clear();
        etTo.clear();

        cbMorning.setChecked(true);
        cbTen.setChecked(true);
        cbLunch.setChecked(true);
        cbEvening.setChecked(true);
        cbSupper.setChecked(true);
    }

    public void onClose(View v) {
        finish();
    }

    @Override
    public void onRegID_OK(String accountID) {
        loadUserInfo(accountID);
    }
}
