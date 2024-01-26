package com.wolf.fingerpos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.wolf.fingerpos.View.EditTextDatePicker;

import java.util.ArrayList;
import java.util.List;

public class ReportsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner spUsers;
    private EditTextDatePicker etFrom;
    private EditTextDatePicker etTo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_reports);
        initWidget();
    }

    void initWidget() {
        spUsers = (Spinner) findViewById(R.id.spUsers);
        spUsers.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categorieUsers = new ArrayList<>();
        categorieUsers.add("All Tellers");
        categorieUsers.add("All Cashier");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categorieUsers);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spUsers.setAdapter(dataAdapter);


        etFrom = new EditTextDatePicker(this, R.id.etFrom);
        etFrom.setTimable(true);
        etTo = new EditTextDatePicker(this, R.id.etTo);
        etTo.setTimable(true);
    }

    public void onOK(View v){
        if (etFrom.currentMilliSec == 0 || etTo.currentMilliSec == 0) {
            Toast.makeText(this, "Please input duration.", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(ReportsActivity.this, ReportsInfoActivity.class);
        intent.putExtra(ReportsInfoActivity.FROM_DURATION, etFrom.currentMilliSec);
        intent.putExtra(ReportsInfoActivity.TO_DURATION, etTo.currentMilliSec);
        startActivity(intent);
    }

    public void onBack(View v) {
        finish();
    }

    public void onCancel(View v){
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
