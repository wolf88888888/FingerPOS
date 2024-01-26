package com.wolf.fingerpos;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.wolf.fingerpos.Fragment.ReportsItemFragment;
import com.wolf.fingerpos.Fragment.ReportsSummaryFragment;
import com.wolf.fingerpos.database.ReceiptTable;

import io.realm.RealmQuery;

public class ReportsInfoActivity extends AppCompatActivity {
    public static final String FROM_DURATION = "from_duration";
    public static final String TO_DURATION = "to_duration";

    long from_duration;
    long to_duration;

    int nCurrentFragment = 0;
    ReportsSummaryFragment fragmentReportsSummary;
    ReportsItemFragment fragmentReportsItem;
    Button btnSummary, btnItems;
    LinearLayout llSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_reports_info);

        from_duration = getIntent().getLongExtra(FROM_DURATION, 0);
        to_duration = getIntent().getLongExtra(TO_DURATION, 0);
        initWidget();
    }

    public void initWidget() {
        btnSummary = (Button)findViewById(R.id.btnSummary);
        btnItems = (Button)findViewById(R.id.btnItems);
        llSearch = (LinearLayout)findViewById(R.id.llSearch);
        refreshFragment();
    }

    public void refreshFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (nCurrentFragment == 0) {
            if (fragmentReportsSummary == null) {
                fragmentReportsSummary = ReportsSummaryFragment.newInstance();
                fragmentReportsSummary.setDuration(from_duration, to_duration);
                ft.add(R.id.flContainer, fragmentReportsSummary);
            }
            else {
                ft.replace(R.id.flContainer, fragmentReportsSummary);
            }
            btnSummary.setBackgroundResource(R.drawable.button_lightblue0);
            btnSummary.setTextColor(0xffff0000);
            btnItems.setBackgroundResource(R.drawable.button_deepblue);
            btnItems.setTextColor(0xffffffff);
            llSearch.setVisibility(View.INVISIBLE);
        }
        else {
            if (fragmentReportsItem == null) {
                fragmentReportsItem = ReportsItemFragment.newInstance();
                ft.add(R.id.flContainer, fragmentReportsItem);
                fragmentReportsItem.setDuration(from_duration, to_duration);
            }
            else {
                ft.replace(R.id.flContainer, fragmentReportsItem);
            }
            btnSummary.setBackgroundResource(R.drawable.button_deepblue);
            btnSummary.setTextColor(0xffffffff);
            btnItems.setBackgroundResource(R.drawable.button_lightblue0);
            btnItems.setTextColor(0xffff0000);
            llSearch.setVisibility(View.INVISIBLE);
        }
        ft.commit();
    }


    public void onOK(View v){

    }

    public void onBack(View v){
        finish();
    }

    public void onCancel(View v){
        finish();
    }

    public void onSummary(View v){
        nCurrentFragment = 0;
        refreshFragment();
    }

    public void onItems(View v){
        nCurrentFragment = 1;
        refreshFragment();
    }
}
