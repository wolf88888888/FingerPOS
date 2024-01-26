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

import com.wolf.fingerpos.Fragment.AccountsInfoFragment;
import com.wolf.fingerpos.Fragment.PostpaidFragment;
import com.wolf.fingerpos.Fragment.PrepayFragment;
import com.wolf.fingerpos.database.MenuTable;

import io.realm.RealmQuery;

public class AccountActivity extends AppCompatActivity {
    Button btnInfo;
    Button btnPrepayments;
    Button btnPostpayments;
    int nCurrentFragment = 0;
    AccountsInfoFragment infoFragment;
    PrepayFragment prepaymentsFragment;
    PostpaidFragment postpaymentsFragment;
    EditText etSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_account);
        initWidget();
    }

    void initWidget(){
        btnInfo = (Button) findViewById(R.id.btnInfo);
        btnPrepayments = (Button) findViewById(R.id.btnPrepayments);
        btnPostpayments = (Button) findViewById(R.id.btnPostpayments);

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
        switch (nCurrentFragment){
            case 0:
                infoFragment.onSearch(strKey);
                break;
            case 1:
                prepaymentsFragment.onSearch(strKey);
                break;
            case 2:
                postpaymentsFragment.onSearch(strKey);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        etSearch.setText("");
    }

    public void refreshFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (nCurrentFragment == 0) {
            if (infoFragment == null) {
                infoFragment = AccountsInfoFragment.newInstance();
                ft.add(R.id.flContainer, infoFragment);
            }
            else {
                ft.replace(R.id.flContainer, infoFragment);
                infoFragment.refresh();
            }
            btnInfo.setBackgroundResource(R.drawable.button_lightblue0);
            btnInfo.setTextColor(0xffff0000);
            btnPrepayments.setBackgroundResource(R.drawable.button_deepblue);
            btnPrepayments.setTextColor(0xffffffff);
            btnPostpayments.setBackgroundResource(R.drawable.button_deepblue);
            btnPostpayments.setTextColor(0xffffffff);
        }
        else if (nCurrentFragment == 1) {
            if (prepaymentsFragment == null) {
                prepaymentsFragment = PrepayFragment.newInstance();
                ft.add(R.id.flContainer, prepaymentsFragment);
            }
            else {
                ft.replace(R.id.flContainer, prepaymentsFragment);
                prepaymentsFragment.refresh();
            }
            btnInfo.setBackgroundResource(R.drawable.button_deepblue);
            btnInfo.setTextColor(0xffffffff);
            btnPrepayments.setBackgroundResource(R.drawable.button_lightblue0);
            btnPrepayments.setTextColor(0xffff0000);
            btnPostpayments.setBackgroundResource(R.drawable.button_deepblue);
            btnPostpayments.setTextColor(0xffffffff);
        }
        else {
            if (postpaymentsFragment == null) {
                postpaymentsFragment = PostpaidFragment.newInstance();
                ft.add(R.id.flContainer, postpaymentsFragment);
            }
            else {
                ft.replace(R.id.flContainer, postpaymentsFragment);
                postpaymentsFragment.refresh();
            }
            btnInfo.setBackgroundResource(R.drawable.button_deepblue);
            btnInfo.setTextColor(0xffffffff);
            btnPrepayments.setBackgroundResource(R.drawable.button_deepblue);
            btnPrepayments.setTextColor(0xffffffff);
            btnPostpayments.setBackgroundResource(R.drawable.button_lightblue0);
            btnPostpayments.setTextColor(0xffff0000);
        }
        ft.commit();
        etSearch.setText("");
    }

    public void onBack(View v) {
        finish();
    }

    public void onInfo(View v) {
        nCurrentFragment = 0;
        refreshFragment();
    }

    public void onPrepayments(View v) {
        nCurrentFragment = 1;
        refreshFragment();
    }

    public void onPostpayments(View v) {
        nCurrentFragment = 2;
        refreshFragment();
    }
}
