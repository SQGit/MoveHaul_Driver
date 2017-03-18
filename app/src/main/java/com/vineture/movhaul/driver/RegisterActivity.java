package com.vineture.movhaul.driver;


import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gun0912.tedpicker.ImagePickerActivity;
import com.rey.material.widget.Button;
import com.rey.material.widget.LinearLayout;
import com.sloop.fonts.FontsManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;





public class RegisterActivity extends Activity {


    public final static int REQUEST_CODE = 1;
    LinearLayout btn_back,lt_add_photo;
    Button btn_submit, btn_verify;
    TextView tv_register,tv_snack;
    EditText et_name, et_email, et_mobile, et_lic_name, et_lic_no, et_lic_mobile, et_lic_exp;
    String str_email, str_mobile, str_name, str_lic_name, str_lic_no, str_lic_mobile, str_lic_exp, str_lic_photo;
    TextInputLayout til_name, til_email, til_mobile, til_lic_no, til_lic_mobile, til__lic_name, til_lic_exp;
    android.widget.LinearLayout lt_first, lt_second;
    ArrayList<String> selectedPhotos = new ArrayList<>();
    ImageView iv_close;
    Dialog dialog2;
    Button d2_btn_ok;
    TextView d2_tv_dialog1,d2_tv_dialog2,d2_tv_dialog3,d2_tv_dialog4;
    ImageView btn_close,iv_driver_lic;
    Snackbar snackbar;
    Typeface tf;
    View view_lic;
    Config config;
    ProgressDialog mProgressDialog;
    String vec_type;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    ImageView iv_truck, iv_bus,iv_road_assit;
    android.widget.LinearLayout lt_filter_dialog;
    ArrayList<Uri> image_uris;

    public static int getDeviceHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int height = display.getHeight();
        return height;
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.vineture.movhaul.driver.R.layout.activity_register);
        FontsManager.initFormAssets(this, "fonts/lato.ttf");
        FontsManager.changeFonts(this);
        tf = Typeface.createFromAsset(getAssets(), "fonts/lato.ttf");
        config = new Config();

        insertDummyContactWrapper();

        mProgressDialog = new ProgressDialog(RegisterActivity.this);
        mProgressDialog.setTitle(getString(com.vineture.movhaul.driver.R.string.loading));
        mProgressDialog.setMessage(getString(com.vineture.movhaul.driver.R.string.wait));
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCancelable(false);




        btn_back = (LinearLayout) findViewById(com.vineture.movhaul.driver.R.id.layout_back);
        btn_submit = (Button) findViewById(com.vineture.movhaul.driver.R.id.btn_submit);
        tv_register = (TextView) findViewById(com.vineture.movhaul.driver.R.id.text_register);
        et_name = (EditText) findViewById(com.vineture.movhaul.driver.R.id.edittext_name);
        et_email = (EditText) findViewById(com.vineture.movhaul.driver.R.id.edittext_mail);
        et_mobile = (EditText) findViewById(com.vineture.movhaul.driver.R.id.edittext_phone);
        til_email = (TextInputLayout) findViewById(com.vineture.movhaul.driver.R.id.float_email);
        til_mobile = (TextInputLayout) findViewById(com.vineture.movhaul.driver.R.id.float_mobile);
        til_name = (TextInputLayout) findViewById(com.vineture.movhaul.driver.R.id.float_name);

        et_lic_name = (EditText) findViewById(com.vineture.movhaul.driver.R.id.edittext_lic_name);
        et_lic_no = (EditText) findViewById(com.vineture.movhaul.driver.R.id.edittext_lic_no);
        et_lic_mobile = (EditText) findViewById(com.vineture.movhaul.driver.R.id.edittext_lic_phone);
        et_lic_exp = (EditText) findViewById(com.vineture.movhaul.driver.R.id.edittext_lic_exp);
        til_lic_no = (TextInputLayout) findViewById(com.vineture.movhaul.driver.R.id.til_lic_no);
        til_lic_mobile = (TextInputLayout) findViewById(com.vineture.movhaul.driver.R.id.til_lic_phone);
        til__lic_name = (TextInputLayout) findViewById(com.vineture.movhaul.driver.R.id.til_lic_name);
        til_lic_exp = (TextInputLayout) findViewById(com.vineture.movhaul.driver.R.id.til_lic_exp);

        btn_verify = (Button) findViewById(com.vineture.movhaul.driver.R.id.btn_verify);

        lt_first = (android.widget.LinearLayout) findViewById(com.vineture.movhaul.driver.R.id.first_layout);
        lt_second = (android.widget.LinearLayout) findViewById(com.vineture.movhaul.driver.R.id.second_layout);
        lt_add_photo = (LinearLayout) findViewById(com.vineture.movhaul.driver.R.id.layout_lic_image);

        iv_close = (ImageView) findViewById(com.vineture.movhaul.driver.R.id.imageview_close);
        iv_driver_lic = (ImageView) findViewById(com.vineture.movhaul.driver.R.id.imageview_driver_lic);

        view_lic = findViewById(com.vineture.movhaul.driver.R.id.view_driver_lic);

        lt_second.setVisibility(View.GONE);

        Typeface type = Typeface.createFromAsset(getAssets(), "fonts/lato.ttf");
        til_email.setTypeface(type);
        til_mobile.setTypeface(type);
        til_name.setTypeface(type);

        final int height = getDeviceHeight(RegisterActivity.this);


        lt_filter_dialog = (android.widget.LinearLayout) findViewById(com.vineture.movhaul.driver.R.id.filter_dialog);
        lt_filter_dialog.setVisibility(View.VISIBLE);

        iv_truck = (ImageView) findViewById(com.vineture.movhaul.driver.R.id.image_truck);
        iv_bus = (ImageView) findViewById(com.vineture.movhaul.driver.R.id.image_bus);
        iv_road_assit = (ImageView) findViewById(com.vineture.movhaul.driver.R.id.image_roadside_assistance);

        et_name.setEnabled(false);
        et_email.setEnabled(false);
        et_mobile.setEnabled(false);
        btn_submit.setEnabled(false);

        iv_bus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TranslateAnimation anim_btn_t2b = new TranslateAnimation(0, 0, 0, height);
                anim_btn_t2b.setDuration(500);
                vec_type = "Bus";
                lt_filter_dialog.setAnimation(anim_btn_t2b);
                lt_filter_dialog.setVisibility(View.GONE);

                et_name.setEnabled(true);
                et_email.setEnabled(true);
                et_mobile.setEnabled(true);
                btn_submit.setEnabled(true);

            }
        });


        iv_truck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TranslateAnimation anim_btn_t2b = new TranslateAnimation(0, 0, 0, height);
                anim_btn_t2b.setDuration(500);
                vec_type = "Truck";
                lt_filter_dialog.setAnimation(anim_btn_t2b);
                lt_filter_dialog.setVisibility(View.GONE);

                et_name.setEnabled(true);
                et_email.setEnabled(true);
                et_mobile.setEnabled(true);
                btn_submit.setEnabled(true);

            }
        });


        iv_road_assit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TranslateAnimation anim_btn_t2b = new TranslateAnimation(0, 0, 0, height);
                anim_btn_t2b.setDuration(500);
                vec_type = "Road";
                lt_filter_dialog.setAnimation(anim_btn_t2b);
                lt_filter_dialog.setVisibility(View.GONE);

                et_name.setEnabled(true);
                et_email.setEnabled(true);
                et_mobile.setEnabled(true);
                btn_submit.setEnabled(true);

            }
        });



        snackbar = Snackbar
                .make(findViewById(com.vineture.movhaul.driver.R.id.top), com.vineture.movhaul.driver.R.string.network, Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        tv_snack = (android.widget.TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        tv_snack.setTextColor(Color.WHITE);
        tv_snack.setTypeface(tf);

        if (!config.isConnected(RegisterActivity.this)) {
            snackbar.show();
            tv_snack.setText(com.vineture.movhaul.driver.R.string.connect);
        }


        dialog2 = new Dialog(RegisterActivity.this);
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog2.setCancelable(false);
        dialog2.setContentView(com.vineture.movhaul.driver.R.layout.driver_bidding_confirm);
        d2_btn_ok = (Button) dialog2.findViewById(com.vineture.movhaul.driver.R.id.button_ok);
        btn_close = (ImageView) dialog2.findViewById(com.vineture.movhaul.driver.R.id.button_close);
        d2_tv_dialog1 = (TextView) dialog2.findViewById(com.vineture.movhaul.driver.R.id.textView_1);
        d2_tv_dialog2 = (TextView) dialog2.findViewById(com.vineture.movhaul.driver.R.id.textView_2);
        d2_tv_dialog3 = (TextView) dialog2.findViewById(com.vineture.movhaul.driver.R.id.textView_3);
        d2_tv_dialog4 = (TextView) dialog2.findViewById(com.vineture.movhaul.driver.R.id.textView_4);

        d2_tv_dialog1.setTypeface(type);
        d2_tv_dialog2.setTypeface(type);
        d2_tv_dialog3.setTypeface(type);
        d2_tv_dialog4.setTypeface(type);
        d2_btn_ok.setTypeface(type);

        d2_tv_dialog1.setText(com.vineture.movhaul.driver.R.string.success);
        d2_tv_dialog2.setText(com.vineture.movhaul.driver.R.string.thanks);
        d2_tv_dialog3.setText(com.vineture.movhaul.driver.R.string.verf);
        d2_tv_dialog4.setVisibility(View.GONE);
        btn_close.setVisibility(View.GONE);

        d2_btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog2.dismiss();
                Intent i = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(i);
                finish();

            }
        });


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                str_email = et_email.getText().toString().trim();
                str_mobile = et_mobile.getText().toString().trim();
                str_name = et_name.getText().toString().trim();

                if (!(str_name.isEmpty())) {
                    if (!(str_email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(str_email).matches())) {
                        if (!(str_mobile.isEmpty() || str_mobile.length() < 9)) {

                            TranslateAnimation anim_btn_b2t = new TranslateAnimation(0, 0, height, 0);
                            anim_btn_b2t.setDuration(500);

                            lt_second.setAnimation(anim_btn_b2t);
                            lt_second.setVisibility(View.VISIBLE);

                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    lt_first.setVisibility(View.GONE);
                                }
                            }, 600);

                            str_email = et_email.getText().toString().trim();
                            str_mobile = et_mobile.getText().toString().trim();
                            str_name = et_name.getText().toString().trim();

                        } else {
                            snackbar.show();
                            tv_snack.setText(com.vineture.movhaul.driver.R.string.va_ph);
                            et_mobile.requestFocus();
                        }
                    } else {
                        snackbar.show();
                        tv_snack.setText(com.vineture.movhaul.driver.R.string.va_ma);
                        et_email.requestFocus();
                    }
                } else {
                    snackbar.show();
                    tv_snack.setText(com.vineture.movhaul.driver.R.string.va_us);
                    et_name.requestFocus();
                }

            }
        });


        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                str_lic_name = et_lic_name.getText().toString().trim();
                str_lic_no = et_lic_no.getText().toString().trim();
                str_lic_mobile = et_lic_mobile.getText().toString().trim();
                str_lic_exp = et_lic_exp.getText().toString().trim();



                if (!(str_lic_name.isEmpty())) {
                    if (!(str_lic_no.isEmpty())) {
                        if (!(str_lic_mobile.isEmpty() || str_lic_mobile.length() < 9)) {

                            if ((str_lic_photo != null)) {

                                str_lic_name = et_lic_name.getText().toString().trim();
                                str_lic_no = et_lic_no.getText().toString().trim();
                                str_lic_mobile = et_lic_mobile.getText().toString().trim();
                                str_lic_exp = et_lic_exp.getText().toString().trim();


                               new register_driver().execute();
                            } else {

                                snackbar.show();
                                tv_snack.setText(com.vineture.movhaul.driver.R.string.up_lc);
                            }

                        } else {
                           // et_lic_mobile.setError("Enter valid phone number");

                            snackbar.show();
                            tv_snack.setText(com.vineture.movhaul.driver.R.string.va_se);

                            et_lic_mobile.requestFocus();
                        }
                    } else {
                       // et_lic_no.setError("Enter a valid email address!");
                        snackbar.show();
                        tv_snack.setText(com.vineture.movhaul.driver.R.string.va_lc);
                        et_lic_no.requestFocus();
                    }
                } else {
                    //et_lic_name.setError("Enter a Name!");
                    snackbar.show();
                    tv_snack.setText(com.vineture.movhaul.driver.R.string.va_fn);
                    et_lic_name.requestFocus();
                }
            }
        });


        lt_add_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                com.gun0912.tedpicker.Config config = new com.gun0912.tedpicker.Config();
                config.setSelectionMin(1);
                config.setSelectionLimit(1);
                config.setCameraHeight(com.vineture.movhaul.driver.R.dimen.app_camera_height);

                config.setCameraBtnBackground(com.vineture.movhaul.driver.R.drawable.round_rd);

                config.setToolbarTitleRes(com.vineture.movhaul.driver.R.string.img_vec_lic);
                config.setSelectedBottomHeight(com.vineture.movhaul.driver.R.dimen.bottom_height);

                ImagePickerActivity.setConfig(config);
                Intent intent = new Intent(RegisterActivity.this, com.gun0912.tedpicker.ImagePickerActivity.class);
                startActivityForResult(intent, REQUEST_CODE);




            }
        });

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                TranslateAnimation anim_btn_t2b = new TranslateAnimation(0, 0, 0, height);
                anim_btn_t2b.setDuration(500);
                lt_first.setVisibility(View.VISIBLE);
                lt_second.setAnimation(anim_btn_t2b);
                lt_second.setVisibility(View.GONE);


            }
        });


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        List<String> photos = null;
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {



                image_uris = data.getParcelableArrayListExtra(com.gun0912.tedpicker.ImagePickerActivity.EXTRA_IMAGE_URIS);
                Log.e("tag", "12345" + image_uris);
                selectedPhotos.clear();
                if (image_uris != null) {
                    str_lic_photo = image_uris.get(0).toString();
                    Glide.with(RegisterActivity.this).load(new File(str_lic_photo)).centerCrop().into(iv_driver_lic);
                    snackbar.show();
                    tv_snack.setText(com.vineture.movhaul.driver.R.string.dr);
                    view_lic.setVisibility(View.GONE);
                }

        }
    }

    public class register_driver extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e("tag", "driver_register");
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            String json = "", jsonStr = "";

            try {

                String responseString = null;
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(Config.WEB_URL + "driversignup");

                httppost.setHeader("driver_name", str_name);
                httppost.setHeader("driver_mobile_pri", "+91" + str_mobile);
                httppost.setHeader("driver_email", str_email);
                httppost.setHeader("driver_licence_name", str_lic_name);
                httppost.setHeader("vehicle_type", vec_type);
                httppost.setHeader("driver_mobile_sec", "+91" + str_lic_mobile);
                httppost.setHeader("driver_licence_number", str_lic_no);
                httppost.setHeader("driver_experience", str_lic_exp);


                try {

                    MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

                    File sourceFile = new File(str_lic_photo);
                    Log.e("tagtag3", "" + sourceFile);
                    entity.addPart("driverlicence", new FileBody(sourceFile, "image/jpeg"));
                    httppost.setEntity(entity);
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity r_entity = response.getEntity();
                    Log.e("tagurl","ur:"+Config.WEB_URL + "driversignup");
                    Log.e("tag","headers:"+httppost.getAllHeaders().toString());
                    int statusCode = response.getStatusLine().getStatusCode();
                    Log.e("tagtag", response.getStatusLine().toString());
                    if (statusCode == 200) {
                        responseString = EntityUtils.toString(r_entity);
                    } else {
                        responseString = "Error occurred! Http Status Code: "
                                + statusCode;
                    }
                } catch (ClientProtocolException e) {
                    responseString = e.toString();
                    Log.e("tagerr0: ", e.toString());
                } catch (IOException e) {
                    responseString = e.toString();
                    Log.e("tagerr1: ", e.toString());
                }
                return responseString;


            } catch (Exception e) {
                Log.e("tagerr2:", e.toString());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("tag", "tagtag" + s);

                mProgressDialog.dismiss();

            if (s != null) {
                try {
                    JSONObject jo = new JSONObject(s);
                    String status = jo.getString("status");
                    String msg = jo.getString("message");
                    Log.d("tag", "<-----Status----->" + status);

                    if(status.equals("true")){
                        dialog2.show();
                    }
                    else{
                        if(msg.contains("Error OccuredError: ER_DUP_ENTRY: Duplicate entry")) {
                            snackbar.show();
                            tv_snack.setText(com.vineture.movhaul.driver.R.string.us_al);
                        }
                        else{
                            snackbar.show();
                            tv_snack.setText(com.vineture.movhaul.driver.R.string.network);
                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("tag", "tagnt: " + e.toString());

                    snackbar.show();
                         tv_snack.setText(com.vineture.movhaul.driver.R.string.network);
                }
            } else {
                snackbar.show();
                     tv_snack.setText(com.vineture.movhaul.driver.R.string.network);
            }
        }
    }



    private void insertDummyContactWrapper() {
        List<String> permissionsNeeded = new ArrayList<String>();

        final List<String> permissionsList = new ArrayList<>();
        if (addPermission(permissionsList, android.Manifest.permission.ACCESS_FINE_LOCATION))
            permissionsNeeded.add("GPS");
        if (addPermission(permissionsList, android.Manifest.permission.CAMERA))
            permissionsNeeded.add("Read Contacts");
        if (addPermission(permissionsList, android.Manifest.permission.WRITE_EXTERNAL_STORAGE))
            permissionsNeeded.add("Write Contacts");

        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                // Need Rationale
                String message = "You need to grant access to " + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                            REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                }


                return;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            }
        }

    }


    private boolean addPermission(List<String> permissionsList, String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permission);
                if (!shouldShowRequestPermissionRationale(permission))
                    return true;
            }
        }
        return false;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<String, Integer>();

                perms.put(android.Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);

                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                if (perms.get(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && perms.get(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);


                } else {
                    insertDummyContactWrapper();
                    Toast.makeText(RegisterActivity.this, com.vineture.movhaul.driver.R.string.perm_de, Toast.LENGTH_SHORT)
                            .show();
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


}