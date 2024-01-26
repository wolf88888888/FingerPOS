package com.wolf.fingerpos.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.EditText;

import com.wolf.fingerpos.R;

public class SystemConfirmDialog extends Dialog implements View.OnClickListener{
    SystemConfirmDialogListener listener;
    public SystemConfirmDialog(Context context, SystemConfirmDialogListener listener) {
        super(context, R.style.DialogPrompt);
        this.setContentView(R.layout.dialog_system_confirm);
        this.listener = listener;
        initWidget();
    }

    void initWidget(){
        findViewById(R.id.btnOK).setOnClickListener(this);
        findViewById(R.id.btnCancel).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnOK) {
            listener.onSystemConfirmDialogListenerOK();
        }
        else {
            listener.onSystemConfirmDialogListenerCancel();
        }
        this.dismiss();
    }

    public interface SystemConfirmDialogListener {
        void onSystemConfirmDialogListenerOK();
        void onSystemConfirmDialogListenerCancel();
    }
}
