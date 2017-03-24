package com.movhaul.driver;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.movhaul.driver.R;
import com.rey.material.widget.Button;
import com.rey.material.widget.LinearLayout;
import com.sloop.fonts.FontsManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sqindia on 22-10-2016.
 */

public class Forgot_Mobile extends Activity {
    LinearLayout btn_back;
    EditText edtxt_email;
    String str_email;
    TextInputLayout flt_email;
    Button btn_submit;
    ProgressBar progresss;
    Snackbar snackbar, snack_wifi;
    ProgressDialog mProgressDialog;
    TextView tv_snack;
    Config config;
    Typeface tf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_phone);
        FontsManager.initFormAssets(this, "fonts/lato.ttf");       //initialization
        FontsManager.changeFonts(this);

        Intent idg  =getIntent();


        flt_email = (TextInputLayout) findViewById(R.id.float_email);
        btn_back = (LinearLayout) findViewById(R.id.layout_back);
        edtxt_email = (EditText) findViewById(R.id.editTextEmail);
        btn_submit = (Button) findViewById(R.id.btn_submit);


        config = new Config();
        tf = Typeface.createFromAsset(getAssets(), "fonts/lato.ttf");
        flt_email.setTypeface(tf);

        mProgressDialog = new ProgressDialog(Forgot_Mobile.this);
        mProgressDialog.setTitle(getString(R.string.loading));
        mProgressDialog.setMessage(getString(R.string.wait));
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCancelable(false);



        snackbar = Snackbar
                .make(findViewById(R.id.top), R.string.network, Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        tv_snack = (android.widget.TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        tv_snack.setTextColor(Color.WHITE);
        tv_snack.setTypeface(tf);

        if (!config.isConnected(Forgot_Mobile.this)) {
            snackbar.show();
            tv_snack.setText(R.string.connect);
        }




        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str_email = edtxt_email.getText().toString().trim();

                if (!(str_email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(str_email).matches())) {
                   /* Intent i = new Intent(Forgot_Mobile.this, LoginOtpActivity.class);
                    startActivity(i);
                    finish();*/

                    new forgot_mobile().execute();


                } else {
                   // edtxt_email.setError("Enter a valid email address!");
                    snackbar.show();
                    tv_snack.setText(R.string.va_em);
                    edtxt_email.requestFocus();
                }
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Forgot_Mobile.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }




    public class forgot_mobile extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e("tag","reg_preexe");
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            String json = "", jsonStr = "";

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("driver_email", str_email);
                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest(Config.WEB_URL + "driveremailotp", json);

            } catch (Exception e) {
                Log.e("InputStream", e.getLocalizedMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("tag","tag"+s);
            mProgressDialog.dismiss();


            if (s != null) {
                try {
                    JSONObject jo = new JSONObject(s);
                    String status = jo.getString("status");
                    String msg = jo.getString("message");
                    Log.d("tag", "<-----Status----->" + status);
                    if (status.equals("true")) {


                        // String sus_txt = "Thank you for Signing Up MoveHaul.";

                        //Toast.makeText(getApplicationContext(),sus_txt,Toast.LENGTH_LONG).show();

                        Intent i = new Intent(Forgot_Mobile.this, LoginOtpActivity.class);
                        i.putExtra("for","email");
                        i.putExtra("data",str_email);
                        startActivity(i);
                        finish();


                    } else if (status.equals("false")) {


                        if (msg.contains("Register with Movehaul first to Generate OTP")) {

                           // Toast.makeText(getApplicationContext(),"Mobile Number Not Registered",Toast.LENGTH_LONG).show();

                            snackbar.show();
                            tv_snack.setText(R.string.no_nm);

                        }
                        else if (msg.contains("Error Occured[object Object]")) {

                            Intent i = new Intent(Forgot_Mobile.this, LoginOtpActivity.class);
                            i.putExtra("for","email");
                            i.putExtra("data",str_email);
                            startActivity(i);
                            finish();

                        }
                        else  {

                           // Toast.makeText(getApplicationContext(),"Please Try Again Later",Toast.LENGTH_LONG).show();
                            snackbar.show();
                            //tv_snack.setText("Mobile Number Not Registered");
                        }


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("tag","nt"+e.toString());
                  //  Toast.makeText(getApplicationContext(),"Network Errror0",Toast.LENGTH_LONG).show();
                    snackbar.show();
                }
            } else {
               // Toast.makeText(getApplicationContext(),"Network Errror1",Toast.LENGTH_LONG).show();
                snackbar.show();
            }

        }

    }






    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(Forgot_Mobile.this, LoginActivity.class);
        startActivity(i);
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
