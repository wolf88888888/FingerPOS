package com.wolf.fingerpos.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import com.wolf.fingerpos.R;

public class CancelAlertDialog extends Dialog implements View.OnClickListener {
    private CancelAlertDialogListener listener;
    public CancelAlertDialog(Context context, CancelAlertDialogListener listener) {
        super(context, R.style.DialogPrompt);
        this.setContentView(R.layout.dialog_cancel_alert);
        this.listener = listener;
        initWidget();
    }

    void initWidget(){
        findViewById(R.id.btnOK).setOnClickListener(this);
        findViewById(R.id.btnCancel).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        this.dismiss();
        if (view.getId() == R.id.btnOK && listener != null) {
            listener.onCancelOk();
        }
    }

    public interface CancelAlertDialogListener {
        void onCancelOk();
    }
}
