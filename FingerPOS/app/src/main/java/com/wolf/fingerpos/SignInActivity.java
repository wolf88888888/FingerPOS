package com.wolf.fingerpos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.wolf.fingerpos.database.UserTable;
import com.wolf.fingerpos.utils.GlobalVariable;

import io.realm.RealmQuery;
import io.realm.RealmResults;

public class SignInActivity extends AppCompatActivity {

    EditText etUserid, etPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_in);
        initWidget();
    }

    void initWidget(){
        etUserid = (EditText)findViewById(R.id.etUserID);
        etPassword = (EditText)findViewById(R.id.etPassword);
    }

    public void onSignIn(View view) {
        GlobalVariable global = GlobalVariable.getInstance(this);
        global.g_userid = etUserid.getText().toString();

        if (!global.g_userid.isEmpty()) {
            RealmQuery<UserTable> queryUser = POSApplication.mRealmDB.where(UserTable.class);
            queryUser.equalTo("id", global.g_userid);
            RealmResults<UserTable> users = queryUser.findAll();
            if (users.size() == 0) {
                Toast.makeText(this, "Don't exist user, please try again.", Toast.LENGTH_LONG).show();
                return;
            }
        }

        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
