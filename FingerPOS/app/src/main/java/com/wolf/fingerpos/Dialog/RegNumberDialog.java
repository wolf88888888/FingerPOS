package com.wolf.fingerpos.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.EditText;

import com.wolf.fingerpos.R;

public class RegNumberDialog extends Dialog implements View.OnClickListener {
    RegNumberDialogListener listener;
    EditText etAccountID;
    public RegNumberDialog(Context context, RegNumberDialogListener listener) {
        super(context, R.style.DialogPrompt);
        this.setContentView(R.layout.dialog_reg_number);
        this.listener = listener;
        initWidget();
    }

    void initWidget(){
        etAccountID = (EditText)findViewById(R.id.etAccountID);
        findViewById(R.id.btnOK).setOnClickListener(this);
        findViewById(R.id.btnCancel).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnOK) {
            if (etAccountID.getText().toString().isEmpty())
                return;
            listener.onRegID_OK(etAccountID.getText().toString());
        }
        this.dismiss();
    }

    public interface RegNumberDialogListener {
        void onRegID_OK(String accountID);
    }
}
