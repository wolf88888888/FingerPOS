package com.wolf.fingerpos.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.EditText;

import com.wolf.fingerpos.R;

public class MPESADetailDialog extends Dialog implements View.OnClickListener{
    EditText etPhoneNumber, etTransactionNumber;
    MPESADetailDialogListener listener;

    public MPESADetailDialog(Context context, MPESADetailDialogListener listener) {
        super(context, R.style.DialogPrompt);
        this.setContentView(R.layout.dialog_mpesa_detail);
        this.listener = listener;
        initWidget();
    }

    void initWidget(){
        etPhoneNumber = (EditText) findViewById(R.id.etPhoneNumber);
        etTransactionNumber = (EditText) findViewById(R.id.etTransactionNumber);
        findViewById(R.id.btnOK).setOnClickListener(this);
        findViewById(R.id.btnCancel).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnOK) {
            listener.onMPESADetailDialogOK(etTransactionNumber.getText().toString());
        }
        this.dismiss();
    }

    public interface MPESADetailDialogListener {
        void onMPESADetailDialogOK(String strTransactionNumber);
    }
}
