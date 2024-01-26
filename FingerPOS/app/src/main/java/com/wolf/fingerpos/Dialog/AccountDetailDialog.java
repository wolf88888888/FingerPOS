package com.wolf.fingerpos.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.wolf.fingerpos.R;

public class AccountDetailDialog extends Dialog implements View.OnClickListener{
    EditText etAccountNumber, etPIN;
    AccountDetailDialogListener listener;
    public AccountDetailDialog(Context context, AccountDetailDialogListener listener) {
        super(context, R.style.DialogPrompt);
        this.setContentView(R.layout.dialog_account_detail);
        this.listener = listener;
        initWidget();
    }

    void initWidget(){
        findViewById(R.id.btnOK).setOnClickListener(this);
        findViewById(R.id.btnCancel).setOnClickListener(this);

        etAccountNumber = (EditText)findViewById(R.id.etAccountNumber);
        etPIN = (EditText)findViewById(R.id.etPIN);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnOK) {
            listener.onAccountDetailDialogOK(etAccountNumber.getText().toString(), etPIN.getText().toString());
        }
        this.dismiss();
    }

    public interface AccountDetailDialogListener {
        void onAccountDetailDialogOK(String accountID, String pin);
    }
}
