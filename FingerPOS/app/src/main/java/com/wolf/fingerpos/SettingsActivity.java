package com.wolf.fingerpos;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.wolf.fingerpos.Fragment.SystemSettingFragment;
import com.wolf.fingerpos.Fragment.UsersSettingFragment;
import com.wolf.fingerpos.database.MenuTable;

import io.realm.RealmQuery;

public class SettingsActivity extends AppCompatActivity {
    Button btnUser;
    Button btnSystem;
    int nCurrentFragment = 0;
    UsersSettingFragment userFragment;
    SystemSettingFragment systemFragment;
    LinearLayout llSearch;
    EditText etSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_setting);
        initWidget();
    }

    void initWidget(){
        btnUser = (Button) findViewById(R.id.btnUser);
        btnSystem = (Button) findViewById(R.id.btnSystem);
        llSearch = (LinearLayout)findViewById(R.id.llSearch);
        etSearch = (EditText)findViewById(R.id.etSearch);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    onSearch(s.toString());
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        refreshFragment();
    }

    void onSearch(String strKey) {
        if (nCurrentFragment == 0)
            userFragment.onSearch(strKey);
    }

    @Override
    protected void onResume() {
        super.onResume();
        etSearch.setText("");
    }

    public void refreshFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (nCurrentFragment == 0) {
            if (userFragment == null) {
                userFragment = UsersSettingFragment.newInstance();
                ft.add(R.id.flContainer, userFragment);
            }
            else {
                ft.replace(R.id.flContainer, userFragment);
            }
            btnUser.setBackgroundResource(R.drawable.button_lightblue0);
            btnUser.setTextColor(0xffff0000);
            btnSystem.setBackgroundResource(R.drawable.button_deepblue);
            btnSystem.setTextColor(0xffffffff);
            llSearch.setVisibility(View.VISIBLE);
        }
        else {
            if (systemFragment == null) {
                systemFragment = SystemSettingFragment.newInstance();
                ft.add(R.id.flContainer, systemFragment);
            }
            else {
                ft.replace(R.id.flContainer, systemFragment);
            }
            btnUser.setBackgroundResource(R.drawable.button_deepblue);
            btnUser.setTextColor(0xffffffff);
            btnSystem.setBackgroundResource(R.drawable.button_lightblue0);
            btnSystem.setTextColor(0xffff0000);
            llSearch.setVisibility(View.GONE);
        }
        ft.commit();
    }

    public void onBack(View v) {
        finish();
    }

    public void onUser(View v) {
        nCurrentFragment = 0;
        refreshFragment();
    }

    public void onSystem(View v) {
        nCurrentFragment = 1;
        refreshFragment();
    }

}
