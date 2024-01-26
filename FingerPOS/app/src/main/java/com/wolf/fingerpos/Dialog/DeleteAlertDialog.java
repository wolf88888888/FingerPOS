package com.wolf.fingerpos.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import com.wolf.fingerpos.R;

public class DeleteAlertDialog extends Dialog implements View.OnClickListener {
    private DeleteAlertDialogListener listener;
    public DeleteAlertDialog(Context context, DeleteAlertDialogListener listener) {
        super(context, R.style.DialogPrompt);
        this.setContentView(R.layout.dialog_delete_alert);
        this.listener = listener;
        initWidget();
    }

    void initWidget(){
        findViewById(R.id.btnOK).setOnClickListener(this);
        findViewById(R.id.btnCancel).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnOK:
                listener.onDeleteOk();
                break;
            case R.id.btnCancel:
                break;
        }
        this.dismiss();
    }



    public interface DeleteAlertDialogListener {
        void onDeleteOk();
    }
}
