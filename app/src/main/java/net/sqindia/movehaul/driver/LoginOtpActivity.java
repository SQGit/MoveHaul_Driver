package net.sqindia.movehaul.driver;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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

import com.google.firebase.iid.FirebaseInstanceId;
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
    String str_otppin, str_for, str_data, str_URL, fcm_id;
    private View view;

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


        Log.e("tag", "data:" + str_for);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(LoginOtpActivity.this);
        editor = sharedPreferences.edit();
        Typeface tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/lato.ttf");

        //str_phone = getIntent.getStringExtra("phone");

        //fcm_id = sharedPreferences.getString("fcm_id","");

        fcm_id = FirebaseInstanceId.getInstance().getToken();

        Log.e("tag", fcm_id);

        mProgressDialog = new ProgressDialog(LoginOtpActivity.this);
        mProgressDialog.setTitle("Loading..");
        mProgressDialog.setMessage("Please wait");
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


    /*    ReceiveSmsBroadcastReceiver.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String messageText) {
                Log.e("Text",messageText);

                char[] cArray = messageText.toCharArray();

                et_otp1.setText(String.valueOf(cArray[0]));
                et_otp2.setText(String.valueOf(cArray[1]));
                et_otp3.setText(String.valueOf(cArray[2]));
                et_otp4.setText(String.valueOf(cArray[3]));

                Toast.makeText(LoginOtpActivity.this,"Message: "+messageText,Toast.LENGTH_LONG).show();
            }
        });*/


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginOtpActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });


        et_otp1.addTextChangedListener(new LoginOtpActivity(et_otp1));
        et_otp2.addTextChangedListener(new LoginOtpActivity(et_otp2));
        et_otp3.addTextChangedListener(new LoginOtpActivity(et_otp3));
        et_otp4.addTextChangedListener(new LoginOtpActivity(et_otp4));

        et_otp4.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (et_otp4.getText().toString().length() == 1) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
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


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*Intent i = new Intent(LoginOtpActivity.this, DashboardNavigation.class);
                startActivity(i);
                finish();*/

                /*editor.putString("login","success");
                editor.commit();*/


                if (et_otp1.getText().toString().isEmpty()) {
                    et_otp1.requestFocus();
                    tv_snack.setText("Enter Otp");
                    snackbar.show();
                } else {
                    if (et_otp2.getText().toString().isEmpty()) {
                        et_otp2.requestFocus();
                        tv_snack.setText("Enter Otp");
                        snackbar.show();
                    } else {
                        if (et_otp3.getText().toString().isEmpty()) {
                            et_otp3.requestFocus();
                            tv_snack.setText("Enter Otp");
                            snackbar.show();
                        } else {
                            if (et_otp4.getText().toString().isEmpty()) {
                                et_otp4.requestFocus();
                                tv_snack.setText("Enter Otp");
                                snackbar.show();
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

    public void updateList(final String smsMessage) {

        Log.e("tag", "ss" + smsMessage);

        /*char[] cArray = smsMessage.toCharArray();

        et_otp1.setText(String.valueOf(cArray[0]));
        et_otp2.setText(String.valueOf(cArray[1]));
        et_otp3.setText(String.valueOf(cArray[2]));
        et_otp4.setText(String.valueOf(cArray[3]));

        Toast.makeText(LoginOtpActivity.this,"Message: "+smsMessage,Toast.LENGTH_LONG).show();*/
    }

    public void receiveSms(String message) {
        Log.e("tag", "msgd4" + message);

        // Toast.makeText(getApplicationContext(), "toast::" + message, Toast.LENGTH_LONG).show();

        try {


            char[] cArray = message.toCharArray();

            et_otp1.setText(String.valueOf(cArray[0]));
            et_otp2.setText("4");
            et_otp3.setText(cArray[2]);
            et_otp4.setText(cArray[3]);


        } catch (Exception e) {
            Log.e("tag", "asd: " + e.toString());
        }


       /* ComponentName receiver = new ComponentName(this, ReceiveSmsBroadcastReceiver.class);

        PackageManager pm = this.getPackageManager();
        pm.setComponentEnabledSetting(receiver,

                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,

                PackageManager.DONT_KILL_APP);

        Toast.makeText(this, "Disabledbroadcseceiver", Toast.LENGTH_SHORT).show();*/


    }

    public void recivedSms(String message) {
        try {
            Log.e("tag", "asd: " + message);

            char[] cArray = message.toCharArray();

            et_otp1.setText(String.valueOf(cArray[cArray.length - 4]));
            et_otp2.setText(String.valueOf(cArray[cArray.length - 3]));
            et_otp3.setText(String.valueOf(cArray[cArray.length - 2]));
            et_otp4.setText(String.valueOf(cArray[cArray.length - 1]));


        } catch (Exception e) {
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

                if (editable.length() == 0) {
                    et_otp1.requestFocus();
                } else if (editable.length() == 1) {
                    et_otp2.requestFocus();
                }

                break;
            case R.id.editext_otp2:

                if (editable.length() == 0) {
                    et_otp1.requestFocus();
                } else if (editable.length() == 1) {
                    et_otp3.requestFocus();
                }

                break;
            case R.id.editext_otp3:

                if (editable.length() == 0) {
                    et_otp2.requestFocus();


                } else if (editable.length() == 1) {
                    et_otp4.requestFocus();
                }
                break;
            case R.id.editext_otp4:

                if (editable.length() == 0) {
                    et_otp3.requestFocus();
                }
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

                    jsonObject.accumulate("driver_mobile", "+91" + str_data);
                    jsonObject.accumulate("driver_otp", str_otppin);
                    jsonObject.accumulate("fcm_id", fcm_id);
                    url = "driver/mobilelogin";
                } else {

                    jsonObject.accumulate("driver_email", str_data);
                    jsonObject.accumulate("driver_otp", str_otppin);
                    url = "driver/emaillogin";
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
                            if (jo.getString("driver_image") != "null") {
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
            Log.e("tag", "reg_preexe");
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            String json = "", jsonStr = "", url;

            try {
                JSONObject jsonObject = new JSONObject();


                if (str_for.equals("phone")) {

                    jsonObject.accumulate("driver_mobile", "+91" + str_data);
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
