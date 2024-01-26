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

import io.realm.RealmQuery;
import io.realm.RealmResults;

public class AccountPrepayActivity extends AppCompatActivity implements RegNumberDialog.RegNumberDialogListener {
    private TextView tvID, tvFirstName, tvOtherName;
    private Spinner spAccountType;
    private EditText etAmount;
    private EditTextDatePicker etFrom, etTo;
    CheckBox cbMorning, cbTen, cbLunch, cbEvening, cbSupper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_account_prepaid);
        initWidget();
    }

    void initWidget(){
        tvID = (TextView)findViewById(R.id.tvID);
        tvFirstName = (TextView)findViewById(R.id.tvFirstName);
        tvOtherName = (TextView)findViewById(R.id.tvOtherName);

        etAmount = (EditText)findViewById(R.id.etAmount);
        etFrom = new EditTextDatePicker(this, R.id.etFrom); etFrom.clear();
        etTo = new EditTextDatePicker(this, R.id.etTo); etTo.clear();

        spAccountType = (Spinner)findViewById(R.id.spAccountType);

        // Creating adapter for spinner
        ArrayAdapter<String> adapterAccountType = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, GlobalVariable.getInstance(this).g_arrayAccountType);
        // Drop down layout style - list view with radio button
        adapterAccountType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spAccountType.setAdapter(adapterAccountType);

        cbMorning = (CheckBox)findViewById(R.id.cbMorning);
        cbTen = (CheckBox)findViewById(R.id.cbTen);
        cbLunch = (CheckBox)findViewById(R.id.cbLunch);
        cbEvening = (CheckBox)findViewById(R.id.cbEvening);
        cbSupper = (CheckBox)findViewById(R.id.cbSupper);
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
        RealmQuery<AccountTable> queryAccounts = POSApplication.mRealmDB.where(AccountTable.class);
        queryAccounts.equalTo("id", accountID);
        RealmResults<AccountTable> resultAccount = queryAccounts.findAll();
        if (resultAccount == null || resultAccount.size() == 0) {
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

        if (postpaidInfo != null) {
            accountID = null;
            Toast.makeText(this, "This Account already has postpaid.", Toast.LENGTH_LONG).show();
            return;
        }

        tvID.setText(accountInfo.getId());
        tvFirstName.setText(accountInfo.getFirst_name());
        tvOtherName.setText(accountInfo.getLast_name());
        if (prepayInfo == null)
            return;
        spAccountType.setSelection(prepayInfo.getType());
        etAmount.setText(String.format("%.2f", prepayInfo.getAmount()));

        etFrom.setDate(Utils.getYear(prepayInfo.getFrom()), Utils.getMon(prepayInfo.getFrom()), Utils.getDay(prepayInfo.getFrom()));
        etTo.setDate(Utils.getYear(prepayInfo.getTo()), Utils.getMon(prepayInfo.getTo()), Utils.getDay(prepayInfo.getTo()));

        cbMorning.setChecked(prepayInfo.getMorning());
        cbTen.setChecked(prepayInfo.getMorning());
        cbLunch.setChecked(prepayInfo.getMorning());
        cbEvening.setChecked(prepayInfo.getMorning());
        cbSupper.setChecked(prepayInfo.getMorning());
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

        if (prepayInfo == null){
            prepayInfo = POSApplication.mRealmDB.createObject(PrepayTable.class, System.currentTimeMillis());
            prepayInfo.setCreate_date(System.currentTimeMillis());
        }
        prepayInfo.setAccount_id(accountInfo.getId());
        prepayInfo.setType(spAccountType.getSelectedItemPosition());
        prepayInfo.setAmount(fBalance);
        prepayInfo.setFrom(etFrom.getCurrentDate());
        prepayInfo.setTo(etFrom.getCurrentDate());

        prepayInfo.setMorning(cbMorning.isChecked());
        prepayInfo.setTen_oclock(cbTen.isChecked());
        prepayInfo.setLunch(cbLunch.isChecked());
        prepayInfo.setEvening(cbEvening.isChecked());
        prepayInfo.setSupper(cbSupper.isChecked());

        POSApplication.mRealmDB.commitTransaction();
        setResult(RESULT_OK);
        finish();
    }

    public void onNew(View v) {
        accountInfo = null;
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
        //onSave(v);
    }

    public void onClose(View v) {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onRegID_OK(String accountID) {
        loadUserInfo(accountID);
    }
}
