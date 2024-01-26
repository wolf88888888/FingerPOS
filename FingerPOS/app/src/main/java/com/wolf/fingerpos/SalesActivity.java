package com.wolf.fingerpos;

import android.content.Intent;
import android.drm.DrmStore;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.wolf.fingerpos.Fragment.PayNFragment;
import com.wolf.fingerpos.Fragment.TouchNFragment;
import com.wolf.fingerpos.utils.GlobalVariable;
import com.wolf.fingerpos.utils.Utils;

import org.w3c.dom.Text;

public class SalesActivity extends AppCompatActivity {

    private int nCurrentFragment = 0;
    PayNFragment payNFragment;
    TouchNFragment touchNFragment;
    Button btnChangeService;

    TextView tvDate;
    TextView tvUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sales);
        initWidget();
    }

    public void initWidget() {
        btnChangeService = (Button) findViewById(R.id.btnChangeService);
        tvDate = (TextView)findViewById(R.id.tvDate);
        tvDate.setText(Utils.convertDate(System.currentTimeMillis(), Utils.DATE_FORMAT));

        tvUserID = (TextView)findViewById(R.id.tvUserID);
        tvUserID.setText(GlobalVariable.getInstance(this).g_userid);
        refreshFragment();
    }

    public void refreshFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (nCurrentFragment == 0) {
            if (payNFragment == null) {
                payNFragment = PayNFragment.newInstance();
                ft.add(R.id.flContainer, payNFragment);
            }
            else {
                ft.replace(R.id.flContainer, payNFragment);
            }
            btnChangeService.setText("Touch N Go Service");
        }
        else {
            if (touchNFragment == null) {
                touchNFragment = TouchNFragment.newInstance();
                ft.add(R.id.flContainer, touchNFragment);
            }
            else {
                ft.replace(R.id.flContainer, touchNFragment);
            }
            btnChangeService.setText("Pay N Go Service");
        }
        ft.commit();
    }

    public void onFragmentChange(View v) {
        if (nCurrentFragment == 0) {
            nCurrentFragment = 1;
        }
        else {
            nCurrentFragment = 0;
        }
        refreshFragment();
    }

    public void onBack(View v) {
        finish();
    }

    public void onReports(View v) {
        Intent intent = new Intent(SalesActivity.this, ReportsActivity.class);
        startActivity(intent);
    }
}
