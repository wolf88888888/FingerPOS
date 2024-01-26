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
import android.widget.Toast;

import com.wolf.fingerpos.database.UserTable;
import com.wolf.fingerpos.utils.GlobalVariable;

import java.util.Arrays;
import java.util.List;

import io.realm.RealmQuery;
import io.realm.RealmResults;

public class UserManageActivity extends AppCompatActivity {
    public final static String MANAGE_TYPE = "manage_type";
    public final static int NEW_USER = 1;
    public final static int EDIT_USER = 2;
    public final static String KEY_USERID = "key_userid";

    private int manage_type = NEW_USER;

    private EditText etUserID, etUsername;
    private Spinner spUserType, spStatus;
    private CheckBox cbSuperUser;
    private List<String> arrayStatus = null;
    private String userid_edit;
    private UserTable user_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_manage);

        manage_type = getIntent().getIntExtra(UserManageActivity.MANAGE_TYPE, NEW_USER);
        if (manage_type == EDIT_USER) {
            userid_edit = getIntent().getStringExtra(UserManageActivity.KEY_USERID);

            RealmQuery<UserTable> queryUsers = POSApplication.mRealmDB.where(UserTable.class);
            queryUsers.equalTo("id", userid_edit);
            RealmResults<UserTable> resultUsers = queryUsers.findAll();
            if (resultUsers.size() == 0) {
                Toast.makeText(this, "Cannot find user. Please try again.", Toast.LENGTH_LONG).show();
                finish();
            }

            user_edit = resultUsers.get(0);
        }
        initWidget();
    }

    void initWidget(){
        etUserID = (EditText) findViewById(R.id.etUserID);
        etUsername = (EditText) findViewById(R.id.etUsername);
        spUserType = (Spinner) findViewById(R.id.spUserType);
        spStatus = (Spinner) findViewById(R.id.spStatus);
        cbSuperUser = (CheckBox)findViewById(R.id.cbSuperUser);

        // Creating adapter for spinner
        ArrayAdapter<String> adapterUserType = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, GlobalVariable.getInstance(this).g_arrayUserType);
        // Drop down layout style - list view with radio button
        adapterUserType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spUserType.setAdapter(adapterUserType);

        arrayStatus = Arrays.asList(getResources().getStringArray(R.array.status));
        // Creating adapter for spinner
        ArrayAdapter<String> adapterStatus = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayStatus);
        // Drop down layout style - list view with radio button
        adapterStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spStatus.setAdapter(adapterStatus);

        if (user_edit != null) {
            etUserID.setText(user_edit.getId());
            etUsername.setText(user_edit.getName());
            spUserType.setSelection(user_edit.getType());
            spStatus.setSelection(user_edit.getStatus());
            cbSuperUser.setChecked(user_edit.getSuper_user());

            etUserID.setEnabled(false);
            //etUserID.setFocusable(false);
            //etUserID.setFocusableInTouchMode(false);
        }
    }

    public void onBack(View v) {
        finish();
    }

    public void onSave(View v) {
        if (manage_type != EDIT_USER) {
            //Toast.makeText(this, "Don't exist user. Please press New button to create user.", Toast.LENGTH_LONG).show();
            if (etUserID.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please input UserID.", Toast.LENGTH_LONG).show();
                return;
            }
            if (etUsername.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please input Username", Toast.LENGTH_LONG).show();
            }

            POSApplication.mRealmDB.beginTransaction();

            UserTable user = POSApplication.mRealmDB.createObject(UserTable.class, System.currentTimeMillis());

            user.setId(etUserID.getText().toString());
            user.setName(etUsername.getText().toString());
            user.setSuper_user(cbSuperUser.isChecked());
            user.setType(spUserType.getSelectedItemPosition());

            POSApplication.mRealmDB.commitTransaction();

            setResult(RESULT_OK);
            finish();
            return;
        }
        POSApplication.mRealmDB.beginTransaction();

        user_edit.setId(etUserID.getText().toString());
        user_edit.setName(etUsername.getText().toString());
        user_edit.setSuper_user(cbSuperUser.isChecked());
        user_edit.setType(spUserType.getSelectedItemPosition());

        POSApplication.mRealmDB.commitTransaction();
        setResult(RESULT_OK);
        finish();
    }

    public void onNew(View v) {
        if (manage_type != NEW_USER) {
            Toast.makeText(this, "Already exist user. Please press Save button to save info.", Toast.LENGTH_LONG).show();
            return;
        }

        etUserID.setText("");
        etUsername.setText("");
        cbSuperUser.setChecked(true);
        spUserType.setSelection(0);
        spStatus.setSelection(0);
    }

    public void onClose(View v) {
        finish();
    }
}
