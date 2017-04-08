package com.movhaul.driver;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.rey.material.widget.Button;
import com.rey.material.widget.LinearLayout;
import com.sloop.fonts.FontsManager;

/**
 * Created by sqindia on 10-11-2016.
 */

public class Bank_details extends Activity {
    LinearLayout btn_back;
    Button btn_submit;
    TextInputLayout til_bank_name, til_rout_no, til_acc_no, til_reacc_no;
    EditText et_bank_name, et_routing_no, et_acc_no, et_reacc_no;
    Snackbar snackbar;
    TextView tv_snack;
    Typeface tf;
    ProgressDialog mProgressDialog;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String str_bank_name,str_routing_no,str_acc_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bank_details);
        FontsManager.initFormAssets(this, "fonts/lato.ttf");       //initialization
        FontsManager.changeFonts(this);
        tf = Typeface.createFromAsset(getAssets(), "fonts/lato.ttf");
        snackbar = Snackbar
                .make(findViewById(R.id.top), R.string.network, Snackbar.LENGTH_SHORT);
        View sbView = snackbar.getView();
        tv_snack = (android.widget.TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        tv_snack.setTextColor(Color.WHITE);
        tv_snack.setTypeface(tf);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Bank_details.this);
        editor = sharedPreferences.edit();

        mProgressDialog = new ProgressDialog(Bank_details.this);
        mProgressDialog.setTitle(getString(R.string.loading));
        mProgressDialog.setMessage(getString(R.string.wait));
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCancelable(false);

        btn_back = (LinearLayout) findViewById(R.id.layout_back);
        btn_submit = (Button) findViewById(R.id.btn_submit);

        et_bank_name = (EditText) findViewById(R.id.editTextBankName);
        et_routing_no = (EditText) findViewById(R.id.editTextRoutingNo);
        et_acc_no = (EditText) findViewById(R.id.editTextAccountNo);
        et_reacc_no = (EditText) findViewById(R.id.editTextReAccountNo);

        til_bank_name = (TextInputLayout) findViewById(R.id.float_bankname);
        til_rout_no = (TextInputLayout) findViewById(R.id.float_routingno);
        til_acc_no = (TextInputLayout) findViewById(R.id.float_accountno);
        til_reacc_no = (TextInputLayout) findViewById(R.id.float_reaccountno);

        til_acc_no.setTypeface(tf);
        til_rout_no.setTypeface(tf);
        til_acc_no.setTypeface(tf);
        til_reacc_no.setTypeface(tf);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), DashboardNavigation.class);
                startActivity(i);
            }
        });


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!et_bank_name.getText().toString().trim().isEmpty()) {
                    if (et_bank_name.getText().toString().trim().length() > 5) {
                        if (!et_routing_no.getText().toString().trim().isEmpty()) {
                            if (et_routing_no.getText().toString().trim().length() > 5) {
                                if (!et_acc_no.getText().toString().trim().isEmpty()) {
                                    if (et_acc_no.getText().toString().trim().length() > 5) {
                                        if (!et_reacc_no.getText().toString().trim().isEmpty()) {
                                            if (et_reacc_no.getText().toString().trim().length() > 5) {
                                                if (et_acc_no.getText().toString().trim().equals(et_reacc_no.getText().toString().trim())) {

                                                    str_bank_name =et_bank_name.getText().toString().trim();
                                                    str_acc_no =et_acc_no.getText().toString().trim();
                                                    str_routing_no =et_routing_no.getText().toString().trim();


                                                } else {
                                                    snackbar.show();
                                                    tv_snack.setText("Account Number not match");
                                                }
                                            } else {
                                                et_reacc_no.requestFocus();
                                                snackbar.show();
                                                tv_snack.setText("Enter Valid Account Number");
                                            }
                                        } else {
                                            et_reacc_no.requestFocus();
                                            snackbar.show();
                                            tv_snack.setText("Re Enter Account Number");
                                        }
                                    } else {
                                        et_acc_no.requestFocus();
                                        snackbar.show();
                                        tv_snack.setText("Enter Valid Account Number");
                                    }
                                } else {
                                    et_acc_no.requestFocus();
                                    snackbar.show();
                                    tv_snack.setText("Enter Account Number");
                                }
                            } else {
                                et_routing_no.requestFocus();
                                snackbar.show();
                                tv_snack.setText("Enter Valid Routing Number");
                            }
                        } else {
                            et_routing_no.requestFocus();
                            snackbar.show();
                            tv_snack.setText("Enter Routing Number");
                        }
                    } else {
                        et_bank_name.requestFocus();
                        snackbar.show();
                        tv_snack.setText("Enter Valid Bank Name");
                    }
                } else {
                    et_bank_name.requestFocus();
                    snackbar.show();
                    tv_snack.setText("Enter Bank Name");
                }


            }
        });

    }
}
