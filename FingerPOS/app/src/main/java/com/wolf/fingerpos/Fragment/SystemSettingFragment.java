package com.wolf.fingerpos.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;

import com.wolf.fingerpos.Dialog.SystemConfirmDialog;
import com.wolf.fingerpos.R;
import com.wolf.fingerpos.database.Settings;

public class SystemSettingFragment extends Fragment implements View.OnClickListener, SystemConfirmDialog.SystemConfirmDialogListener {
    private RadioButton rbDefaultOperationModeOnline, rbDefaultOperationModeOffline;
    private RadioButton rbPayNGo, rbTouchNGo;
    private RadioButton rbAccount_PIN, rbRFID_PIN, rbAccount_VeinFinger, rbRFID, rbVeinFinger, rbEMVCard_PIN;
    private RadioButton rbValidModeOnline, rbValidModeOffline;
    private RadioButton rbVerification_1_1, rbIdentification_1_n;
    private RadioButton rbAuto, rbManual, rbSemiAuto;

    private EditText etServerIP, etPort, etURL, etAuthID, etAuthPass;

    public static SystemSettingFragment newInstance() {
        Bundle bundle = new Bundle();

        SystemSettingFragment fragment = new SystemSettingFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        // Inflate the xml file for the fragment
        return inflater.inflate(R.layout.fragment_system_setting, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        rbDefaultOperationModeOnline = view.findViewById(R.id.rbDefaultOperationModeOnline);
        rbDefaultOperationModeOffline = view.findViewById(R.id.rbDefaultOperationModeOffline);

        rbPayNGo = view.findViewById(R.id.rbPayNGo);
        rbTouchNGo = view.findViewById(R.id.rbTouchNGo);

        rbAccount_PIN = view.findViewById(R.id.rbAccount_PIN);
        rbRFID_PIN = view.findViewById(R.id.rbRFID_PIN);
        rbAccount_VeinFinger = view.findViewById(R.id.rbAccount_VeinFinger);
        rbRFID = view.findViewById(R.id.rbRFID);
        rbVeinFinger = view.findViewById(R.id.rbVeinFinger);
        rbEMVCard_PIN = view.findViewById(R.id.rbEMVCard_PIN);

        rbValidModeOnline = view.findViewById(R.id.rbValidModeOnline);
        rbValidModeOffline = view.findViewById(R.id.rbValidModeOffline);

        rbVerification_1_1 = view.findViewById(R.id.rbVerification_1_1);
        rbIdentification_1_n = view.findViewById(R.id.rbIdentification_1_n);

        rbAuto = view.findViewById(R.id.rbAuto);
        rbManual = view.findViewById(R.id.rbManual);
        rbSemiAuto = view.findViewById(R.id.rbSemiAuto);

        etServerIP = view.findViewById(R.id.etServerIP);
        etPort = view.findViewById(R.id.etPort);
        etURL = view.findViewById(R.id.etURL);
        etAuthID = view.findViewById(R.id.etAuthID);
        etAuthPass = view.findViewById(R.id.etAuthPass);

        view.findViewById(R.id.btnSetDefaultOperation).setOnClickListener(this);
        view.findViewById(R.id.btnSetSyncServer).setOnClickListener(this);
        view.findViewById(R.id.btnSetAccountValidation).setOnClickListener(this);
        view.findViewById(R.id.btnSetAccountVeinFingerValidation).setOnClickListener(this);

        initWidget();
    }

    private void initWidget() {
        Settings.DEFAULT_OPERATION_MODE operation_mode = Settings.getDefaultOperationMode(getContext());

//        rbDefaultOperationModeOnline.setChecked(false);
//        rbDefaultOperationModeOffline.setChecked(false);
        if (operation_mode == Settings.DEFAULT_OPERATION_MODE.ONLINE) {
            rbDefaultOperationModeOnline.setChecked(true);
        }
        else {
            rbDefaultOperationModeOffline.setChecked(true);
        }

//        rbPayNGo.setChecked(false);
//        rbTouchNGo.setChecked(false);
        Settings.DEFAULT_SALES_SCREEN sales_screen = Settings.getDefaultSalesScreen(getContext());
        if (sales_screen == Settings.DEFAULT_SALES_SCREEN.Pay_N_Go) {
            rbPayNGo.setChecked(true);
        }
        else {
            rbTouchNGo.setChecked(true);
        }

        rbAccount_PIN.setChecked(false);
        rbAccount_VeinFinger.setChecked(false);
        rbVeinFinger.setChecked(false);
        rbRFID_PIN.setChecked(false);
        rbRFID.setChecked(false);
        rbEMVCard_PIN.setChecked(false);

        Settings.ACCOUNT_VALIDATION account_validation = Settings.getAccountValidation(getContext());

        if (account_validation == Settings.ACCOUNT_VALIDATION.Account_and_PIN) {
            rbAccount_PIN.setChecked(true);
        }
        else if (account_validation == Settings.ACCOUNT_VALIDATION.Account_and_VeinFinger) {
            rbAccount_VeinFinger.setChecked(true);
        }
        else if (account_validation == Settings.ACCOUNT_VALIDATION.VeinFinger_Only) {
            rbVeinFinger.setChecked(true);
        }
        else if (account_validation == Settings.ACCOUNT_VALIDATION.RFIDCard_and_PIN) {
            rbRFID_PIN.setChecked(true);
        }
        else if (account_validation == Settings.ACCOUNT_VALIDATION.RFID_Card_Only) {
            rbRFID.setChecked(true);
        }
        else if (account_validation == Settings.ACCOUNT_VALIDATION.EMV_Card_and_PIN) {
            rbEMVCard_PIN.setChecked(true);
        }

        Settings.ACCOUNT_VEINFINGER_VALIDATION_MODE account_veinfinger_validation_mode = Settings.getValidationMode(getContext());
//        rbValidModeOnline.setChecked(false);
//        rbValidModeOffline.setChecked(false);

        if (account_veinfinger_validation_mode == Settings.ACCOUNT_VEINFINGER_VALIDATION_MODE.ONLINE) {
            rbValidModeOnline.setChecked(true);
        }
        else {
            rbValidModeOffline.setChecked(true);
        }

        Settings.ACCOUNT_VEINFINGER_VALIDATION_TYPE account_veinfinger_validation_type = Settings.getValidationType(getContext());
//        rbVerification_1_1.setChecked(false);
//        rbIdentification_1_n.setChecked(false);
        if (account_veinfinger_validation_type == Settings.ACCOUNT_VEINFINGER_VALIDATION_TYPE.Verification_1_1) {
            rbVerification_1_1.setChecked(true);
        }
        else {
            rbIdentification_1_n.setChecked(true);
        }

        etServerIP.setText(Settings.getServerIP(getContext()));
        etPort.setText(Settings.getServerPort(getContext()));
        etURL.setText(Settings.getServerURL(getContext()));
        etAuthID.setText(Settings.getServerAuthID(getContext()));
        etAuthPass.setText(Settings.getServerAuthPass(getContext()));

        Settings.SERVER_SYNC_MODE sync_mode = Settings.getServerSyncMode(getContext());
//        rbAuto.setChecked(false);
//        rbManual.setChecked(false);
//        rbSemiAuto.setChecked(false);
        if (sync_mode == Settings.SERVER_SYNC_MODE.Auto) {
            rbAuto.setChecked(true);
        }
        else if (sync_mode == Settings.SERVER_SYNC_MODE.Manual) {
            rbManual.setChecked(true);
        }
        else if (sync_mode == Settings.SERVER_SYNC_MODE.Semi_Auto) {
            rbSemiAuto.setChecked(true);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    void setSetting(){
        if (currentSetting == -1)
            return;
        switch (currentSetting) {
            case 0:
                if (rbDefaultOperationModeOnline.isChecked()) {
                    Settings.setDefaultOperationMode(getContext(), Settings.DEFAULT_OPERATION_MODE.ONLINE);
                }
                else {
                    Settings.setDefaultOperationMode(getContext(), Settings.DEFAULT_OPERATION_MODE.OFFLINE);
                }

                if (rbPayNGo.isChecked()) {
                    Settings.setDefaultSalesScreen(getContext(), Settings.DEFAULT_SALES_SCREEN.Pay_N_Go);
                }
                else {
                    Settings.setDefaultSalesScreen(getContext(), Settings.DEFAULT_SALES_SCREEN.Touch_N_Go);
                }
                break;
            case 1:
                Settings.setServerIP(getContext(), etServerIP.getText().toString());
                Settings.setServerPort(getContext(), etPort.getText().toString());
                Settings.setServerURL(getContext(), etURL.getText().toString());
                Settings.setServerAuthID(getContext(), etAuthID.getText().toString());
                Settings.setServerAuthPass(getContext(), etAuthPass.getText().toString());

                if (rbAuto.isChecked()) {
                    Settings.setServerSyncMode(getContext(), Settings.SERVER_SYNC_MODE.Auto);
                }
                else if (rbManual.isChecked()) {
                    Settings.setServerSyncMode(getContext(), Settings.SERVER_SYNC_MODE.Manual);
                }
                else {
                    Settings.setServerSyncMode(getContext(), Settings.SERVER_SYNC_MODE.Semi_Auto);
                }
                break;
            case 2:
                if (rbAccount_PIN.isChecked()) {
                    Settings.setAccountValidation(getContext(), Settings.ACCOUNT_VALIDATION.Account_and_PIN);
                }
                else if (rbAccount_VeinFinger.isChecked()) {
                    Settings.setAccountValidation(getContext(), Settings.ACCOUNT_VALIDATION.Account_and_VeinFinger);
                }
                else if (rbVeinFinger.isChecked()) {
                    Settings.setAccountValidation(getContext(), Settings.ACCOUNT_VALIDATION.VeinFinger_Only);
                }
                else if (rbRFID_PIN.isChecked()) {
                    Settings.setAccountValidation(getContext(), Settings.ACCOUNT_VALIDATION.RFIDCard_and_PIN);
                }
                else if (rbRFID.isChecked()) {
                    Settings.setAccountValidation(getContext(), Settings.ACCOUNT_VALIDATION.RFID_Card_Only);
                }
                else {
                    Settings.setAccountValidation(getContext(), Settings.ACCOUNT_VALIDATION.EMV_Card_and_PIN);
                }
                break;
            case 3:
                if (rbValidModeOnline.isChecked()){
                    Settings.setValidationMode(getContext(),Settings.ACCOUNT_VEINFINGER_VALIDATION_MODE.ONLINE);
                }
                else {
                    Settings.setValidationMode(getContext(),Settings.ACCOUNT_VEINFINGER_VALIDATION_MODE.OFFLINE);
                }

                if (rbVerification_1_1.isChecked()) {
                    Settings.setValidationType(getContext(), Settings.ACCOUNT_VEINFINGER_VALIDATION_TYPE.Verification_1_1);
                }
                else {
                    Settings.setValidationType(getContext(), Settings.ACCOUNT_VEINFINGER_VALIDATION_TYPE.Identification_1_N);
                }
                break;
        }
    }

    int currentSetting = -1; //0 btnSetDefaultOperation, 1 btnSetSyncServer, 2 btnSetAccountValidation, 3 btnSetAccountVeinFingerValidation
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSetDefaultOperation: {
                currentSetting = 0;
                break;
            }
            case R.id.btnSetSyncServer: {
                currentSetting = 1;
                break;
            }
            case R.id.btnSetAccountValidation: {
                currentSetting = 2;
                break;
            }
            case R.id.btnSetAccountVeinFingerValidation: {
                currentSetting = 3;
                break;
            }
        }
        SystemConfirmDialog dialog = new SystemConfirmDialog(getContext(), this);
        dialog.show();
    }

    @Override
    public void onSystemConfirmDialogListenerOK() {
        setSetting();
    }

    @Override
    public void onSystemConfirmDialogListenerCancel() {
        currentSetting = -1;
        initWidget();
    }
}
