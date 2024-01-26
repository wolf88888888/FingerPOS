package com.wolf.fingerpos.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.wolf.fingerpos.R;

public class CashDetailDialog extends Dialog implements View.OnClickListener {
    CashDetailDialogListener listener;
    EditText etAmount;
    public CashDetailDialog(Context context, CashDetailDialogListener listener) {
        super(context, R.style.DialogPrompt);
        this.setContentView(R.layout.dialog_cash_detail);
        this.listener = listener;
        initWidget();
    }

    void initWidget(){
        findViewById(R.id.btnOK).setOnClickListener(this);
        findViewById(R.id.btnCancel).setOnClickListener(this);
        etAmount = (EditText) findViewById(R.id.etAmount);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnOK) {
            if (etAmount.getText().toString().isEmpty()) {
                Toast.makeText(getContext(), "Please input amount.", Toast.LENGTH_SHORT).show();
                return;
            }
            listener.onCashDialogOK(Float.valueOf(etAmount.getText().toString()));
        }
        this.dismiss();
    }

    public interface CashDetailDialogListener {
        public void onCashDialogOK(float amount);
    }
}
