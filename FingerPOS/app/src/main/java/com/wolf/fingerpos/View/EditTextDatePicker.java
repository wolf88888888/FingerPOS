package com.wolf.fingerpos.View;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.wolf.fingerpos.utils.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class EditTextDatePicker implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    EditText _editText;
    private int _day;
    private int _month;
    private int _year;
    private int _hour;
    private int _minute;
    private Activity _activity;
    public long currentMilliSec = 0;

    private boolean isSet = false;
    private boolean _isTime = false;
    private boolean _isOpen = false;
    private Calendar _calendar;

    public EditTextDatePicker(Activity activity, int editTextViewID)
    {
        this._editText = (EditText)activity.findViewById(editTextViewID);
        this._editText.setOnClickListener(this);
        this._activity = activity;
    }

    public void setTimable(boolean isTime) {
        this._isTime = isTime;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        setDate(year, monthOfYear, dayOfMonth);
    }

    public void clear(){
        currentMilliSec = System.currentTimeMillis();
        if (!_isTime) {
            _editText.setText(Utils.convertDate(currentMilliSec, Utils.DATE_FORMAT));
        }
        else {
            _editText.setText(Utils.convertDate(currentMilliSec, Utils.DATE_TIME_FORMAT));
        }
    }

    public void setDate(int year, int monthOfYear, int dayOfMonth) {
        isSet = true;
        _year = year;
        _month = monthOfYear;
        _day = dayOfMonth;
        if (_isTime && !_isOpen) {
            _isOpen = true;
            TimePickerDialog timePickerDialog = new TimePickerDialog(_activity, this, 0, 0, false);
            timePickerDialog.show();
        }
        else {
            updateDisplay();
        }
    }
    @Override
    public void onClick(View v) {
        _calendar = Calendar.getInstance(TimeZone.getDefault());

        DatePickerDialog dialog = null;
        if (!isSet) {
            dialog = new DatePickerDialog(_activity, this,
                    _calendar.get(Calendar.YEAR), _calendar.get(Calendar.MONTH),
                    _calendar.get(Calendar.DAY_OF_MONTH));
        }
        else {
            dialog = new DatePickerDialog(_activity, this, _year, _month, _day);

        }
        dialog.show();

    }

    public long getCurrentDate() {
        return currentMilliSec;
    }

    // updates the date in the birth date EditText
    private void updateDisplay() {
        currentMilliSec = System.currentTimeMillis();
        if (!_isTime) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");

            try {
                Date date = sdf.parse(_year + "-" + (_month + 1) + "-" + _day);
                currentMilliSec = date.getTime();

            } catch (ParseException e) {
                e.printStackTrace();
            }
            _editText.setText(Utils.convertDate(currentMilliSec, Utils.DATE_FORMAT));
        }
        else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d HH:mm");
            try {
                String strDate = _year + "-" + (_month + 1) + "-" + _day + " " + String.format("%2d", _hour) + ":" +  String.format("%2d", _minute);
                Date date = sdf.parse(strDate);
                currentMilliSec = date.getTime();

            } catch (ParseException e) {
                e.printStackTrace();
            }
            _editText.setText(Utils.convertDate(currentMilliSec, Utils.DATE_TIME_FORMAT));
        }
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        _isOpen = false;
        _hour = hourOfDay;
        _minute = minute;
        updateDisplay();
    }
}