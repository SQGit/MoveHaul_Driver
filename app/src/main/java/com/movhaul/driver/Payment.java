package com.movhaul.driver;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import com.movhaul.driver.R;
import com.rey.material.widget.LinearLayout;
import com.rey.material.widget.ListView;
import com.sloop.fonts.FontsManager;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by sqindia on 01-11-2016.
 */

public class Payment extends Activity {
    ListView lv_payment_list;
    LinearLayout btn_back;
    ImageView btn_date,btn_close;
    EditText et_date;
    final static int DATE_PICKER_ID1 = 1111;
    int year;
    int month;
    int day;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment);
        FontsManager.initFormAssets(this, "fonts/lato.ttf");       //initialization
        FontsManager.changeFonts(this);

        lv_payment_list = (ListView) findViewById(R.id.listview_payment);
        btn_back = (LinearLayout) findViewById(R.id.layout_back);
        et_date = (EditText) findViewById(R.id.editTextDate);
        btn_date = (ImageView) findViewById(R.id.iv_btn_date);
        btn_close = (ImageView) findViewById(R.id.iv_btn_close);

        final ArrayList<String> payment_arlist = new ArrayList<>();
       // ht_arlist = new ArrayList<>();

        Payment_Adapter adapter = new Payment_Adapter(Payment.this, payment_arlist);

        lv_payment_list.setAdapter(adapter);
        final Calendar c1 = Calendar.getInstance();
        year = c1.get(Calendar.YEAR);
        month = c1.get(Calendar.MONTH);
        day = c1.get(Calendar.DAY_OF_MONTH);
        // dt = cal.toLocaleString();
        //  txtDate.setText(dt.toString());
        btn_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(DATE_PICKER_ID1);
                // et_date.setText("23-10-2016");
            }
        });
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_date.setText("");
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent i = new Intent(Payment.this,DashboardNavigation.class);
                startActivity(i);*/
                finish();
            }
        });
    }
    @Override

    protected Dialog onCreateDialog(int id) {

        switch (id) {
            case DATE_PICKER_ID1:
                return new DatePickerDialog(this, pickerListener1, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener pickerListener1 = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;
            et_date.setText(new StringBuilder().append(day).append("-").append(month + 1).append("-").append(year).append(""));
        }
    };
    @Override
    public void onBackPressed() {
        /*super.onBackPressed();
        Intent i = new Intent(Payment.this,DashboardNavigation.class);
        startActivity(i);*/
        finish();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }
}
