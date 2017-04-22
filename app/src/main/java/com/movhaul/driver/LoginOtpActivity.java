package com.movhaul.driver;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.movhaul.driver.R;
import com.rey.material.widget.Button;
import com.rey.material.widget.LinearLayout;
import com.sloop.fonts.FontsManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Modified by Salman on 24-10-2016.
 */

public class LoginOtpActivity extends Activity implements TextWatcher {
    static EditText et_otp1, et_otp2, et_otp3, et_otp4;
    private static LoginOtpActivity inst;
    LinearLayout btn_back;
    String str_phone;
    Button btn_submit;
    TextView tv_resendotp, tv_snack;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Snackbar snackbar;
    Config config;
    ProgressDialog mProgressDialog;
    Typeface tf;
    String str_otppin, str_for, str_data, str_prefix, fcm_id;
    private View view;
    String url_data,vec_type;
    public InputMethodManager imml;

    int a,b,c,d;
    InputMethodManager inputMethodManager;

    ReceiveSmsBroadcastReceiver receiver;

    private LoginOtpActivity(View view) {
        this.view = view;
    }

    public LoginOtpActivity() {

    }

    public static LoginOtpActivity instance() {
        return inst;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_otp_screen);
        FontsManager.initFormAssets(this, "fonts/lato.ttf");       //initialization
        FontsManager.changeFonts(this);
        config = new Config();

        Intent getIntent = getIntent();

        str_for = getIntent.getStringExtra("for");
        str_data = getIntent.getStringExtra("data");
        vec_type = getIntent.getStringExtra("vec_type");
        str_prefix = getIntent.getStringExtra("prefix");



        if(vec_type.equals("Bus")){
            url_data = "busdriver";
        }
        else if(vec_type.equals("Truck")){
            url_data = "truckdriver";
        }
        else{
            url_data = "assistance";
        }


        Log.e("tag", "data:" + str_for);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(LoginOtpActivity.this);
        editor = sharedPreferences.edit();
        Typeface tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/lato.ttf");

        //str_phone = getIntent.getStringExtra("phone");

        //fcm_id = sharedPreferences.getString("fcm_id","");

        fcm_id = FirebaseInstanceId.getInstance().getToken();

        Log.e("tag", fcm_id);

        mProgressDialog = new ProgressDialog(LoginOtpActivity.this);
        mProgressDialog.setTitle(getString(R.string.loading));
        mProgressDialog.setMessage(getString(R.string.wait));
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCancelable(false);


        snackbar = Snackbar
                .make(findViewById(R.id.top), "Network Error! Please Try Again Later.", Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        tv_snack = (android.widget.TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        tv_snack.setTextColor(Color.WHITE);
        tv_snack.setTypeface(tf);

        if (!config.isConnected(LoginOtpActivity.this)) {
            snackbar.show();
            tv_snack.setText("Please Connect Internet and Try again");
        }


        btn_back = (LinearLayout) findViewById(R.id.layout_back);

        et_otp1 = (EditText) findViewById(R.id.editext_otp1);
        et_otp2 = (EditText) findViewById(R.id.editext_otp2);
        et_otp3 = (EditText) findViewById(R.id.editext_otp3);
        et_otp4 = (EditText) findViewById(R.id.editext_otp4);

        btn_submit = (Button) findViewById(R.id.button_submit);
        tv_resendotp = (TextView) findViewById(R.id.textview_resendotp);


        /*IntentFilter filter = new IntentFilter();
        filter.addAction("SOME_ACTION");
        filter.addAction("SOME_OTHER_ACTION");
        receiver = new ReceiveSmsBroadcastReceiver();

        registerReceiver(receiver,filter);*/

        ReceiveSmsBroadcastReceiver.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String messageText) {
                Log.e("Text",messageText);

                String[] parts = messageText.trim().split("is");
                Log.e("tag", "part1: " + parts);
                String part1 = parts[0]; // 004
                String part2 = parts[1]; // 034556
                Log.e("tag", "part1: " + part1);
                Log.e("tag", "part2: " + part2);
                part2 = part2.trim();
               // cArray = part2.toCharArray();
                Log.e("tag", "partqq: " + part2);

                char[] cArray = part2.toCharArray();

                et_otp1.setText(String.valueOf(cArray[0]));
                et_otp2.setText(String.valueOf(cArray[1]));
                et_otp3.setText(String.valueOf(cArray[2]));
                et_otp4.setText(String.valueOf(cArray[3]));

                Toast.makeText(LoginOtpActivity.this,"Message: "+messageText,Toast.LENGTH_LONG).show();
            }
        });


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginOtpActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });


        imml = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        et_otp1.addTextChangedListener(new LoginOtpActivity(et_otp1));
        et_otp2.addTextChangedListener(new LoginOtpActivity(et_otp2));
        et_otp3.addTextChangedListener(new LoginOtpActivity(et_otp3));
        et_otp4.addTextChangedListener(new LoginOtpActivity(et_otp4));



       et_otp4.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if( keyCode != KeyEvent.KEYCODE_BACK && keyCode != KeyEvent.KEYCODE_DEL ) {


                    if (et_otp4.getText().toString().length() == 1) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        // imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                        imm.toggleSoftInput(0, 0);
                    }


                }
                else{
                    Log.e("tag","a"+(++d));
                    if(d>3){
                        et_otp3.requestFocus();
                        d=0;
                    }
                }

                return false;
            }
        });


        et_otp3.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                Log.e("tag", " code: " + keyCode + " event: " + event.getAction());

                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    Log.e("tag","a"+(++c));
                    if(c>3){
                        et_otp2.requestFocus();
                        c=0;
                    }

                }
                return false;
            }
        });

        et_otp2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                Log.e("tag", " code: " + keyCode + " event: " + event.getAction());

                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    Log.e("tag","a"+(++b));
                    if(b>3){
                        et_otp1.requestFocus();
                        b=0;
                    }

                }
                return false;
            }
        });



        tv_resendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_resendotp.setVisibility(View.GONE);
                new resend_otp().execute();

                //Toast.makeText(getApplicationContext(), "Otp Send to " + str_phone, Toast.LENGTH_LONG).show();
            }
        });


        inputMethodManager =
                (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (et_otp1.getText().toString().isEmpty()) {
                    et_otp1.requestFocus();
                    tv_snack.setText("Enter Otp");
                    snackbar.show();

                    inputMethodManager.toggleSoftInputFromWindow(
                            et_otp1.getApplicationWindowToken(),
                            InputMethodManager.SHOW_FORCED, 0);

                } else {
                    if (et_otp2.getText().toString().isEmpty()) {
                        et_otp2.requestFocus();
                        tv_snack.setText("Enter Otp");
                        snackbar.show();

                        inputMethodManager.toggleSoftInputFromWindow(
                                et_otp1.getApplicationWindowToken(),
                                InputMethodManager.SHOW_FORCED, 0);

                    } else {
                        if (et_otp3.getText().toString().isEmpty()) {
                            et_otp3.requestFocus();
                            tv_snack.setText("Enter Otp");
                            snackbar.show();

                            inputMethodManager.toggleSoftInputFromWindow(
                                    et_otp1.getApplicationWindowToken(),
                                    InputMethodManager.SHOW_FORCED, 0);

                        } else {
                            if (et_otp4.getText().toString().isEmpty()) {
                                et_otp4.requestFocus();
                                tv_snack.setText("Enter Otp");
                                snackbar.show();

                                inputMethodManager.toggleSoftInputFromWindow(
                                        et_otp1.getApplicationWindowToken(),
                                        InputMethodManager.SHOW_FORCED, 0);

                            } else {
                                str_otppin = et_otp1.getText().toString() + et_otp2.getText().toString() + et_otp3.getText().toString() + et_otp4.getText().toString();
                                Log.e("tag", "pin:" + str_otppin);



                                new otp_verify().execute();


                            }
                        }
                    }
                }


            }

        });


    }



    public void recivedSms(String message) {
        try {
            Log.e("tag", "asd: " + message);
            char[] cArray = new char[0];
            //"Your MoveHaul OTP is 4770"

            try {
                String[] parts = message.trim().split("is");
                Log.e("tag", "part1: " + parts);
                String part1 = parts[0]; // 004
                String part2 = parts[1]; // 034556
                Log.e("tag", "part1: " + part1);
                Log.e("tag", "part2: " + part2);
                part2 = part2.trim();
                cArray = part2.toCharArray();
                Log.e("tag", "partqq: " + part2);
            }
            catch (Exception e){
                Log.e("tag", "err: " + e.toString());
            }



            et_otp1.setText(String.valueOf(cArray[cArray.length - 4]));
            et_otp2.setText(String.valueOf(cArray[cArray.length - 3]));
            et_otp3.setText(String.valueOf(cArray[cArray.length - 2]));
            et_otp4.setText(String.valueOf(cArray[cArray.length - 1]));


        } catch (Exception e) {
            Log.e("tag", "Err: " + e.toString());
        }
    }
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }
    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }
    @Override
    public void afterTextChanged(Editable editable) {

        switch (view.getId()) {
            case R.id.editext_otp1:
                if (editable.length() == 1) {
                    et_otp2.requestFocus();
                } /*else if (editable.length() == 1) {
                    et_otp2.requestFocus();
                }*/
                break;
            case R.id.editext_otp2:
                if (editable.length() == 1) {
                    et_otp3.requestFocus();
                }/* else if (editable.length() == 1) {
                    et_otp3.requestFocus();
                }*/
                break;
            case R.id.editext_otp3:
                if (editable.length() == 1) {
                    et_otp4.requestFocus();
                } /*else if (editable.length() == 1) {
                    et_otp4.requestFocus();
                }*/
                break;
            case R.id.editext_otp4:
               /* if (editable.length() == 0) {
                    et_otp3.requestFocus();
                }*/


                break;
        }


    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(LoginOtpActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }
    public class otp_verify extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e("tag", "reg_preexe");
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            String json = "", jsonStr = "", url;

            try {
                JSONObject jsonObject = new JSONObject();


                Log.e("tag", str_for + " dd: " + str_data + str_otppin);

                if (str_for.equals("phone")) {

                    jsonObject.accumulate("driver_mobile",str_data);
                    jsonObject.accumulate("driver_otp", str_otppin);
                    jsonObject.accumulate("fcm_id", fcm_id);
                    url = url_data+"/mobilelogin";
                } else {

                    jsonObject.accumulate("driver_email", str_data);
                    jsonObject.accumulate("driver_otp", str_otppin);
                    jsonObject.accumulate("fcm_id", fcm_id);
                    url = url_data+"/emaillogin";
                }


                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest(Config.WEB_URL + url, json);

            } catch (Exception e) {
                Log.e("InputStream", e.getLocalizedMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("tag", "tag" + s);
            mProgressDialog.dismiss();


            if (s != null) {
                try {
                    JSONObject jo = new JSONObject(s);
                    String status = jo.getString("status");
                    String msg = jo.getString("message");
                    Log.d("tag", "<-----Status----->" + status);
                    if (status.equals("true")) {

                        editor.putString("id", jo.getString("driver_id"));
                        editor.putString("token", jo.getString("token"));
                        editor.putString("driver_name", jo.getString("driver_name"));
                        editor.putString("driver_mobile_prefix", str_prefix);
                        editor.putString("driver_mobile", jo.getString("driver_mobile"));
                        editor.putString("driver_mobile2", jo.getString("driver_mobile_sec"));
                        editor.putString("driver_email", jo.getString("driver_email"));
                        editor.putString("driver_verification", jo.getString("driver_verification"));
                        editor.putString("driver_status", jo.getString("driver_status"));
                        editor.putString("account_status", jo.getString("account_status"));
                        editor.putString("driver_licence_image", jo.getString("driver_licence_image"));
                        editor.putString("driver_id", jo.getString("fake_id"));
                        editor.putString("vec_type", jo.getString("vehicle_type"));
                        editor.putString("login", "success");
                        editor.commit();

                        if (jo.getString("vehicle_type").equals("Truck")) {
                            Log.e("tag","img: "+jo.getString("driver_image"));

                            if (!(jo.getString("driver_image").equals("null"))) {
                                editor.putString("driver_image", jo.getString("driver_image"));
                                editor.commit();
                            }
                            if (jo.has("truck_image_front")) {
                                editor.putString("truck_front", jo.getString("truck_image_front"));
                                editor.putString("truck_back", jo.getString("truck_image_back"));
                                editor.putString("truck_side", jo.getString("truck_image_side"));
                                editor.putString("truck_rc", jo.getString("truck_title_image1"));
                                editor.putString("truck_ins", jo.getString("truck_insurance_image1"));
                                editor.putString("driver_address", jo.getString("driver_address"));
                                editor.putString("profile", "success");
                                if (jo.getString("truck_title_image2") != "null")
                                    editor.putString("truck_rc1", jo.getString("truck_title_image2"));
                                if (jo.getString("truck_insurance_image2") != "null")
                                    editor.putString("truck_ins1", jo.getString("truck_insurance_image2"));
                                editor.commit();
                            }
                        }
                        else if (jo.getString("vehicle_type").equals("Bus")) {
                            if (jo.getString("driver_image") != "null") {
                                editor.putString("driver_image", jo.getString("driver_image"));
                                editor.commit();
                            }
                            if (jo.has("bus_image_front")) {
                                editor.putString("bus_front", jo.getString("bus_image_front"));
                                editor.putString("bus_inside", jo.getString("bus_image_back"));
                                editor.putString("bus_rc", jo.getString("bus_title_image1"));
                                editor.putString("bus_ins", jo.getString("bus_insurance_image1"));
                                editor.putString("driver_address", jo.getString("driver_address"));
                                editor.putString("profile", "success");
                                if (jo.getString("bus_title_image2") != "null")
                                    editor.putString("bus_rc1", jo.getString("bus_title_image2"));
                                if (jo.getString("bus_insurance_image2") != "null")
                                    editor.putString("bus_ins1", jo.getString("bus_insurance_image2"));
                                editor.commit();
                            }
                        }
                        else{

                            if (jo.getString("driver_image") != "null") {
                                editor.putString("driver_image", jo.getString("driver_image"));
                                editor.commit();
                            }
                            if (jo.has("assistance_image_front")) {
                                editor.putString("bus_front", jo.getString("assistance_image_front"));
                                editor.putString("bus_inside", jo.getString("assistance_image_back"));
                                editor.putString("bus_rc", jo.getString("assistance_title_image1"));
                                editor.putString("bus_ins", jo.getString("assistance_insurance_image1"));
                                editor.putString("driver_address", jo.getString("driver_address"));
                                editor.putString("profile", "success");
                                if (jo.getString("assistance_title_image2") != "null")
                                    editor.putString("bus_rc1", jo.getString("assistance_title_image2"));
                                if (jo.getString("assistance_insurance_image2") != "null")
                                    editor.putString("bus_ins1", jo.getString("bus_insurance_image2"));
                                editor.commit();
                            }





                        }


                        Intent i = new Intent(LoginOtpActivity.this, DashboardNavigation.class);
                        startActivity(i);
                        finish();


                    } else if (status.equals("false")) {


                        if (msg.contains("Authentication failed.Wrong Password")) {

                            tv_snack.setText("Otp Failed. Try Again Later");
                            snackbar.show();


                        } else {

                            tv_snack.setText("Network Error Please Try again Later.");
                            snackbar.show();
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("tag", "nt" + e.toString());
                    snackbar.show();
                }
            } else {
                snackbar.show();
            }

        }

    }
    public class resend_otp extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           // Log.e("tag", "reg_preexe");
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            String json = "", jsonStr = "", url;

            try {
                JSONObject jsonObject = new JSONObject();


                if (str_for.equals("phone")) {

                    jsonObject.accumulate("driver_mobile", str_data);
                    jsonObject.accumulate("driver_otp", str_otppin);
                    url = "drivermobileotp";
                } else {

                    jsonObject.accumulate("driver_email", str_data);
                    jsonObject.accumulate("driver_otp", str_otppin);
                    url = "driveremailotp";
                }

                json = jsonObject.toString();
                return jsonStr = HttpUtils.makeRequest(Config.WEB_URL + url, json);

            } catch (Exception e) {
                Log.e("InputStream", e.getLocalizedMessage());
                mProgressDialog.dismiss();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("tag", "tag" + s);
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
                        tv_snack.setText("Otp Send to " + str_data);
                        snackbar.show();


                    } else if (status.equals("false")) {


                        if (msg.contains("Error Occured[object Object]")) {


                        }


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("tag", "nt" + e.toString());
                    // Toast.makeText(getApplicationContext(),"Network Errror0",Toast.LENGTH_LONG).show();
                    snackbar.show();
                }
            } else {
                // Toast.makeText(getApplicationContext(),"Network Errror1",Toast.LENGTH_LONG).show();
                snackbar.show();
            }

        }

    }

}
