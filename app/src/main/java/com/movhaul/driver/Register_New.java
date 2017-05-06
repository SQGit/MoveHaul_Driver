package com.movhaul.driver;

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
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gun0912.tedpicker.ImagePickerActivity;
import com.hbb20.CountryCodePicker;
import com.rey.material.widget.Button;
import com.rey.material.widget.ImageView;
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

/**
 * Created by Salman on 02-05-2017.
 */

public class Register_New extends AppCompatActivity {
    public final static int REQUEST_CODE = 1;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 32;
    public static String[] ar_service_types, ar_service_areas, ar_experience;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    Button btn_submit;
    int status, adi_sts;
    LinearLayout lt_first, lt_second, lt_additional, lt_service_types, lt_serviceareas, lt_experience, lt_add_photo;
    TextView tv_header_txt;
    ScrollView scrollView;
    com.rey.material.widget.TextView tv_additional, tv_additional_hide;
    CountryCodePicker ccp;
    Typeface tf;
    Snackbar snackbar;
    TextView tv_snack;
    Config config;
    ArrayList<Uri> image_uris;
    ProgressDialog mProgressDialog;
    TextInputLayout til_service_type, til_service_areas, til_username, til_mobile, til_email, til_address, til_emg_contact;
    LinearLayout lt_route_local, lt_route_state, lt_govt_local, lt_govt_state;
    ImageButton ib_route_local, ib_route_state, ib_govt_local, ib_govt_state;
    ArrayList<String> selectedPhotos = new ArrayList<>();
    String str_lic_photo;
    android.widget.ImageView iv_driver_lic;
    View view_lic;
    com.rey.material.widget.LinearLayout btn_back;
    String str_username,str_vec_type, str_phone, str_email, str_address, str_experience, str_service_type, str_service_route, str_service_areas, str_service_govt,str_mobile_prefix;
    EditText et_name, et_phone, et_email, et_address, et_service_type, et_service_areas, et_experience;
    Dialog dialog2;
    Button d2_btn_ok;
    TextView d2_tv_dialog1, d2_tv_dialog2, d2_tv_dialog3, d2_tv_dialog4;
    android.widget.ImageView btn_close;
    EditText et_ref1,et_ref2,et_ref3,et_service_pref;
    String str_ref1,str_ref2,str_ref3,service_areas_pref;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_new);

        FontsManager.initFormAssets(Register_New.this, "fonts/lato.ttf");
        FontsManager.changeFonts(Register_New.this);
        tf = Typeface.createFromAsset(getAssets(), "fonts/lato.ttf");

        config = new Config();

        mProgressDialog = new ProgressDialog(Register_New.this);
        mProgressDialog.setTitle(getString(R.string.loading));
        mProgressDialog.setMessage(getString(R.string.wait));
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCancelable(false);

        insertDummyContactWrapper();

        snackbar = Snackbar
                .make(findViewById(R.id.top), R.string.network, Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        tv_snack = (android.widget.TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        tv_snack.setTextColor(Color.WHITE);
        tv_snack.setTypeface(tf);

        if (!config.isConnected(Register_New.this)) {
            snackbar.show();
            tv_snack.setText(R.string.connect);
        }


        btn_submit = (Button) findViewById(R.id.btn_submit);
        lt_first = (LinearLayout) findViewById(R.id.layout_one);
        lt_second = (LinearLayout) findViewById(R.id.layout_two);
        tv_header_txt = (TextView) findViewById(R.id.text_register);

        til_service_type = (TextInputLayout) findViewById(R.id.textinput_servicetype);
        til_service_areas = (TextInputLayout) findViewById(R.id.textinput_service_areas);

        til_username = (TextInputLayout) findViewById(R.id.float_name);
        til_mobile = (TextInputLayout) findViewById(R.id.float_mobile);
        til_email = (TextInputLayout) findViewById(R.id.float_email);
        til_address = (TextInputLayout) findViewById(R.id.float_address);
        til_emg_contact = (TextInputLayout) findViewById(R.id.float_mobile_emg);
        lt_serviceareas = (LinearLayout) findViewById(R.id.layout_serviceareas);
        et_service_type = (EditText) findViewById(R.id.editTextGoodsType);
        et_service_areas = (EditText) findViewById(R.id.edittext_serviceareas);
        scrollView = (ScrollView) findViewById(R.id.scrollview);
        tv_additional = (com.rey.material.widget.TextView) findViewById(R.id.textview_additional);
        lt_additional = (LinearLayout) findViewById(R.id.layout_additional);
        tv_additional_hide = (com.rey.material.widget.TextView) findViewById(R.id.textview_additional1);
        lt_route_local = (LinearLayout) findViewById(R.id.route_local);
        lt_route_state = (LinearLayout) findViewById(R.id.route_state);
        lt_govt_local = (LinearLayout) findViewById(R.id.govt_local);
        lt_govt_state = (LinearLayout) findViewById(R.id.govt_state);
        ib_route_local = (ImageButton) findViewById(R.id.image_route_local);
        ib_route_state = (ImageButton) findViewById(R.id.image_route_state);
        ib_govt_local = (ImageButton) findViewById(R.id.image_govt_local);
        ib_govt_state = (ImageButton) findViewById(R.id.image_govt_state);
        lt_service_types = (LinearLayout) findViewById(R.id.layout_servicetypes);
        lt_experience = (LinearLayout) findViewById(R.id.layout_experience);
        et_experience = (EditText) findViewById(R.id.edittext_experience);
        lt_add_photo = (LinearLayout) findViewById(R.id.layout_lic_image);
        iv_driver_lic = (android.widget.ImageView) findViewById(R.id.imageview_driver_lic);
        ccp = (CountryCodePicker) findViewById(R.id.ccp);
        view_lic = findViewById(R.id.view_driver_lic);
        btn_back = (com.rey.material.widget.LinearLayout) findViewById(R.id.layout_back);

        et_name = (EditText) findViewById(R.id.edittext_name);
        et_phone = (EditText) findViewById(R.id.edittext_phone);
        et_email = (EditText) findViewById(R.id.edittext_mail);
        et_address = (EditText) findViewById(R.id.edittext_address);

        et_ref1 = (EditText) findViewById(R.id.edittext_ref1);
        et_ref2 = (EditText) findViewById(R.id.edittext_ref2);
        et_ref3 = (EditText) findViewById(R.id.edittext_ref3);
        et_service_pref = (EditText) findViewById(R.id.edittext_service_areas_prefs);


        ar_service_types = new String[]{"Haulage-Local", "Haulage-Interstate", "Service Truck-Party Van", "Service Truck-Cooling van", "Service Truck-Gas", "Tow truck", "Bus Rental-Charter"};
        ar_service_areas = new String[]{"50-100 miles", "100-200 miles", "200+ miles"};
        ar_experience = new String[]{" 1 ", " 2 ", " 3 ", " 4 ", "5+"};
        str_mobile_prefix = "+234";

        lt_first.setVisibility(View.VISIBLE);
        lt_second.setVisibility(View.GONE);
        lt_additional.setVisibility(View.INVISIBLE);


        til_service_type.setTypeface(tf);
        til_service_areas.setTypeface(tf);
        til_username.setTypeface(tf);
        til_mobile.setTypeface(tf);
        til_email.setTypeface(tf);
        til_address.setTypeface(tf);
        til_emg_contact.setTypeface(tf);
        ccp.setTypeFace(tf);




        dialog2 = new Dialog(Register_New.this);
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog2.setCancelable(false);
        dialog2.setContentView(R.layout.driver_bidding_confirm);
        d2_btn_ok = (Button) dialog2.findViewById(R.id.button_ok);
        btn_close = (android.widget.ImageView) dialog2.findViewById(R.id.button_close);
        d2_tv_dialog1 = (TextView) dialog2.findViewById(R.id.textView_1);
        d2_tv_dialog2 = (TextView) dialog2.findViewById(R.id.textView_2);
        d2_tv_dialog3 = (TextView) dialog2.findViewById(R.id.textView_3);
        d2_tv_dialog4 = (TextView) dialog2.findViewById(R.id.textView_4);

        d2_tv_dialog1.setTypeface(tf);
        d2_tv_dialog2.setTypeface(tf);
        d2_tv_dialog3.setTypeface(tf);
        d2_tv_dialog4.setTypeface(tf);
        d2_btn_ok.setTypeface(tf);

        d2_tv_dialog1.setText(R.string.success);
        d2_tv_dialog2.setText(R.string.thanks);
        d2_tv_dialog3.setText(R.string.verf);
        d2_tv_dialog4.setVisibility(View.GONE);
        btn_close.setVisibility(View.GONE);

        d2_btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog2.dismiss();
                Intent i = new Intent(Register_New.this, LoginActivity.class);
                startActivity(i);
                finish();

            }
        });


        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                str_mobile_prefix = ccp.getSelectedCountryCodeWithPlus();
                Log.e("tag", "flg_ccp" + ccp.getSelectedCountryCodeWithPlus());
            }
        });

        lt_route_local.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ib_route_local.setBackground(getResources().getDrawable(R.mipmap.select_tick));
                ib_route_state.setBackground(getResources().getDrawable(R.mipmap.unselecting_tick));
                str_service_route = "Local";
                return false;
            }
        });
        lt_route_state.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ib_route_state.setBackground(getResources().getDrawable(R.mipmap.select_tick));
                ib_route_local.setBackground(getResources().getDrawable(R.mipmap.unselecting_tick));
                str_service_route = "Interstate";
                return false;
            }
        });

        lt_govt_local.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ib_govt_local.setBackground(getResources().getDrawable(R.mipmap.select_tick));
                ib_govt_state.setBackground(getResources().getDrawable(R.mipmap.unselecting_tick));
                str_service_govt = "Local";
                return false;
            }
        });

        lt_govt_state.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ib_govt_state.setBackground(getResources().getDrawable(R.mipmap.select_tick));
                ib_govt_local.setBackground(getResources().getDrawable(R.mipmap.unselecting_tick));
                str_service_govt = "Interstate";
                return false;
            }
        });


        lt_service_types.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

/*
                AlertDialog.Builder builder = new AlertDialog.Builder(Register_New.this);

                builder.setItems(ar_service_types, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // The 'which' argument contains the index position
                                // of the selected item
                                et_service_type.setText(ar_service_types[which]);
                                dialog.dismiss();
                            }
                        });

                AlertDialog alertDialog = builder.create();
                TextView textView = (TextView) alertDialog.getWindow().findViewById(android.R.id.message);
               // textView.setTypeface(tf);
                alertDialog.show();*/


                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Register_New.this);
                LayoutInflater inflater = getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.dialog_service_type, null);
                dialogBuilder.setView(dialogView);

             /*   final TextView edt = (TextView) dialogView.findViewById(R.id.textview_type);

                edt.setTypeface(tf);*/

                final AlertDialog b = dialogBuilder.create();
                LinearLayout myRoot = (LinearLayout) dialogView.findViewById(R.id.layout_top);
                LinearLayout a = null;

                for (int i = 0; i < ar_service_types.length; i++) {

                    a = new LinearLayout(Register_New.this);
                    LinearLayout.LayoutParams para = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    para.setMargins(5, 20, 5, 20);
                    a.setOrientation(LinearLayout.HORIZONTAL);
                    a.setLayoutParams(para);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(60, 60);
                    params.gravity = Gravity.CENTER;
                    ImageView imageView = new ImageView(Register_New.this);
                    imageView.setImageResource(R.drawable.button_change);
                    imageView.setLayoutParams(params);
                    TextView tss = new TextView(Register_New.this);
                    tss.setText(ar_service_types[i]);
                    LinearLayout.LayoutParams paramsQ = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    paramsQ.gravity = Gravity.CENTER;
                    tss.setLayoutParams(paramsQ);
                    tss.setTextSize(16);
                    tss.setTextColor(getResources().getColor(R.color.textColor));
                    tss.setTypeface(tf);

                    View vres = new View(Register_New.this);
                    LinearLayout.LayoutParams paramss = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
                    vres.setLayoutParams(paramss);
                    vres.setBackgroundColor(getResources().getColor(R.color.greyIcon));

                    a.addView(imageView);
                    a.addView(tss);
                    myRoot.addView(a);
                    myRoot.addView(vres);

                    final int k = i;

                    a.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.e("tag", "a:" + ar_service_types[k]);
                            b.dismiss();
                            et_service_type.setText(ar_service_types[k]);
                            str_service_type = ar_service_types[k];
                            if(ar_service_types[k].contains("Bus")){
                                str_vec_type = "Bus";
                            Log.e("tag0",str_vec_type);}
                            else if(ar_service_types[k].contains("Tow")){
                                str_vec_type = "Road";
                                Log.e("tag1",str_vec_type);}
                            else{
                                str_vec_type = "Truck";
                                Log.e("tag2",str_vec_type);}
                        }
                    });


                }


                b.show();


            }
        });

        lt_serviceareas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Register_New.this);
                LayoutInflater inflater = getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.dialog_service_type, null);
                dialogBuilder.setView(dialogView);
                final AlertDialog b = dialogBuilder.create();
                LinearLayout myRoot = (LinearLayout) dialogView.findViewById(R.id.layout_top);
                LinearLayout a = null;

                for (int i = 0; i < ar_service_areas.length; i++) {

                    a = new LinearLayout(Register_New.this);
                    LinearLayout.LayoutParams para = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    para.setMargins(5, 20, 5, 20);
                    a.setOrientation(LinearLayout.HORIZONTAL);
                    a.setLayoutParams(para);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(60, 60);
                    params.gravity = Gravity.CENTER;
                    ImageView imageView = new ImageView(Register_New.this);
                    imageView.setImageResource(R.drawable.button_change);
                    imageView.setLayoutParams(params);
                    TextView tss = new TextView(Register_New.this);
                    tss.setText(ar_service_areas[i]);
                    LinearLayout.LayoutParams paramsQ = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    paramsQ.gravity = Gravity.CENTER;
                    tss.setLayoutParams(paramsQ);
                    tss.setTextSize(16);
                    tss.setTextColor(getResources().getColor(R.color.textColor));
                    tss.setTypeface(tf);

                    View vres = new View(Register_New.this);
                    LinearLayout.LayoutParams paramss = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
                    vres.setLayoutParams(paramss);
                    vres.setBackgroundColor(getResources().getColor(R.color.vie_bor));

                    a.addView(imageView);
                    a.addView(tss);
                    myRoot.addView(a);
                    myRoot.addView(vres);

                    final int k = i;

                    a.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.e("tag", "a:" + ar_service_areas[k]);
                            b.dismiss();
                            et_service_areas.setText(ar_service_areas[k]);
                            str_service_areas = ar_service_areas[k];
                        }
                    });
                }
                b.show();
            }
        });


        lt_experience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Register_New.this);
                LayoutInflater inflater = getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.dialog_service_type, null);
                dialogBuilder.setView(dialogView);
                final AlertDialog b = dialogBuilder.create();
                LinearLayout myRoot = (LinearLayout) dialogView.findViewById(R.id.layout_top);
                LinearLayout a = null;

                for (int i = 0; i < ar_experience.length; i++) {

                    a = new LinearLayout(Register_New.this);
                    LinearLayout.LayoutParams para = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    para.setMargins(5, 20, 5, 20);
                    a.setOrientation(LinearLayout.HORIZONTAL);
                    a.setLayoutParams(para);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(60, 60);
                    params.gravity = Gravity.CENTER;
                    ImageView imageView = new ImageView(Register_New.this);
                    imageView.setImageResource(R.drawable.button_change);
                    imageView.setLayoutParams(params);
                    TextView tss = new TextView(Register_New.this);
                    tss.setText(ar_experience[i]);
                    LinearLayout.LayoutParams paramsQ = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    paramsQ.gravity = Gravity.CENTER;
                    tss.setLayoutParams(paramsQ);
                    tss.setTextSize(16);
                    tss.setTextColor(getResources().getColor(R.color.textColor));
                    tss.setTypeface(tf);

                    View vres = new View(Register_New.this);
                    LinearLayout.LayoutParams paramss = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
                    vres.setLayoutParams(paramss);
                    vres.setBackgroundColor(getResources().getColor(R.color.vie_bor));

                    a.addView(imageView);
                    a.addView(tss);
                    myRoot.addView(a);
                    myRoot.addView(vres);

                    final int k = i;

                    a.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.e("tag", "a:" + ar_experience[k]);
                            b.dismiss();
                            et_experience.setText(ar_experience[k]);
                            str_experience = ar_experience[k];
                        }
                    });
                }
                b.show();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (lt_second.getVisibility() == View.GONE) {
                    str_username = et_name.getText().toString().trim();
                    str_phone = et_phone.getText().toString().trim();
                    str_email = et_email.getText().toString().trim();
                    str_address = et_address.getText().toString().trim();
                    str_experience = et_experience.getText().toString().trim();

                    if (!(str_username.isEmpty() || str_username.length() < 4)) {
                        if (!(str_phone.isEmpty() || str_phone.length() < 10)) {
                            if (!(str_email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(str_email).matches())) {
                                if (!(str_address.isEmpty() || str_address.length() < 7)) {
                                    if (!(str_experience.isEmpty())) {

                                        str_username = et_name.getText().toString().trim();
                                        str_phone = et_phone.getText().toString().trim();
                                        str_email = et_email.getText().toString().trim();
                                        str_address = et_address.getText().toString().trim();
                                        str_experience = et_experience.getText().toString().trim();

                                        lt_first.setVisibility(View.GONE);
                                        lt_second.setVisibility(View.VISIBLE);
                                        btn_submit.setText("Register");
                                        tv_header_txt.setText("Business Info");

                                    } else {
                                        snackbar.show();
                                        tv_snack.setText("Enter Experience");

                                    }

                                } else {

                                    snackbar.show();
                                    tv_snack.setText("Enter Valid Address");
                                    et_address.requestFocus();


                                }

                            } else {

                                snackbar.show();
                                tv_snack.setText(R.string.va_ma);
                                et_email.requestFocus();
                            }
                        } else {
                            snackbar.show();
                            tv_snack.setText(R.string.va_ph);
                            et_phone.requestFocus();

                        }
                    } else {
                        snackbar.show();
                        tv_snack.setText(R.string.va_us);
                        et_name.requestFocus();
                    }
                } else {

                 /*   lt_first.setVisibility(View.VISIBLE);
                    lt_second.setVisibility(View.GONE);
                    btn_submit.setText("Next");
                    tv_header_txt.setText("User Info");*/


                    str_service_type = et_service_type.getText().toString().trim();
                    //str_service_route = et_phone.getText().toString().trim();
                   str_service_areas = et_service_areas.getText().toString().trim();
                    // str_service_govt = et_address.getText().toString().trim();
                    str_experience = et_experience.getText().toString().trim();

                    if (!(str_service_type.isEmpty())) {
                        if ((str_service_route!= null)) {
                            if (!(str_service_areas.isEmpty())) {
                                if ((str_service_govt!= null)) {
                                    if ((str_lic_photo != null)) {

                                        new register_driver().execute();

                                    } else {
                                        snackbar.show();
                                        tv_snack.setText(R.string.up_lc);
                                    }
                                } else {
                                    snackbar.show();
                                    tv_snack.setText("Enter Local Government");
                                }
                            } else {
                                snackbar.show();
                                tv_snack.setText("Enter Service Areas");
                            }
                        } else {
                            snackbar.show();
                            tv_snack.setText("Enter Primary Route Coverage");
                        }
                    } else {
                        snackbar.show();
                        tv_snack.setText("Enter Service Type");
                    }


                }


            }
        });

        tv_additional.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                lt_additional.setVisibility(View.VISIBLE);
                scrollView.fullScroll(View.FOCUS_DOWN);
                tv_additional.setVisibility(View.GONE);
            }
        });

        tv_additional_hide.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {

                scrollView.fullScroll(View.FOCUS_UP);
                lt_additional.setVisibility(View.INVISIBLE);
                tv_additional.setVisibility(View.VISIBLE);


            }
        });


        lt_add_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    Log.e("tag", "permission Not granted");


                    ActivityCompat.requestPermissions(Register_New.this,
                            new String[]{android.Manifest.permission.CAMERA},
                            MY_PERMISSIONS_REQUEST_CAMERA);

                } else {
                    com.gun0912.tedpicker.Config config = new com.gun0912.tedpicker.Config();
                    config.setSelectionMin(1);
                    config.setSelectionLimit(1);
                    config.setCameraHeight(R.dimen.app_camera_height);
                    config.setCameraBtnBackground(R.drawable.round_rd);
                    config.setToolbarTitleRes(R.string.img_vec_lic);
                    config.setSelectedBottomHeight(R.dimen.bottom_height);
                    ImagePickerActivity.setConfig(config);
                    Intent intent = new Intent(Register_New.this, com.gun0912.tedpicker.ImagePickerActivity.class);
                    startActivityForResult(intent, REQUEST_CODE);
                }

            }
        });



                /*if (status == 0) {
                    status = 1;
                    lt_first.setVisibility(View.GONE);
                    lt_second.setVisibility(View.VISIBLE);
                    btn_submit.setText("Register");
                    tv_header_txt.setText("Business Info");
                } else {
                    status = 0;
                    lt_first.setVisibility(View.VISIBLE);
                    lt_second.setVisibility(View.GONE);
                    btn_submit.setText("Next");
                    tv_header_txt.setText("User Info");
                }*/


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }

    @Override
    public void onBackPressed() {

        if (lt_second.getVisibility() == View.VISIBLE) {
            lt_first.setVisibility(View.VISIBLE);
            lt_second.setVisibility(View.GONE);
            btn_submit.setText("Next");
            tv_header_txt.setText("User Info");
        } else {
            finish();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        List<String> photos = null;
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {


            image_uris = data.getParcelableArrayListExtra(com.gun0912.tedpicker.ImagePickerActivity.EXTRA_IMAGE_URIS);
            Log.e("tag", "12345" + image_uris);
            selectedPhotos.clear();
            if (image_uris != null) {
                str_lic_photo = image_uris.get(0).toString();
                Glide.with(Register_New.this).load(new File(str_lic_photo)).centerCrop().into(iv_driver_lic);
                snackbar.show();
                tv_snack.setText(R.string.dr);
                view_lic.setVisibility(View.GONE);
            }

        }
    }

    private void insertDummyContactWrapper() {
        List<String> permissionsNeeded = new ArrayList<String>();

        final List<String> permissionsList = new ArrayList<>();
        if (addPermission(permissionsList, android.Manifest.permission.ACCESS_FINE_LOCATION))
            permissionsNeeded.add("GPS");
        if (addPermission(permissionsList, android.Manifest.permission.CAMERA))
            permissionsNeeded.add("Take Camera");
        if (addPermission(permissionsList, android.Manifest.permission.WRITE_EXTERNAL_STORAGE))
            permissionsNeeded.add("storage");
        if (addPermission(permissionsList, android.Manifest.permission.READ_CONTACTS))
            permissionsNeeded.add("Read Contacts");

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
                perms.put(android.Manifest.permission.READ_CONTACTS, PackageManager.PERMISSION_GRANTED);

                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                if (perms.get(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && perms.get(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        && perms.get(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(android.Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {

                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);


                } else {
                    insertDummyContactWrapper();
                    Toast.makeText(Register_New.this, R.string.perm_de, Toast.LENGTH_SHORT)
                            .show();
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }



    public class register_driver extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e("tag", "driver_register");
            mProgressDialog.show();

            str_ref1 = et_ref1.getText().toString().trim();
            str_ref2 = et_ref2.getText().toString().trim();
            str_ref3 = et_ref3.getText().toString().trim();
            service_areas_pref = et_service_pref.getText().toString().trim();


        }

        @Override
        protected String doInBackground(String... strings) {

            String json = "", jsonStr = "";

            try {

                String responseString = null;
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(Config.WEB_URL + "driversignup");

                httppost.setHeader("driver_name", str_username);
                httppost.setHeader("driver_mobile_pri", str_mobile_prefix + str_phone);
                httppost.setHeader("driver_email", str_email);
                httppost.setHeader("driver_address", str_address);
                httppost.setHeader("driver_experience", str_experience);

                httppost.setHeader("service_type", str_service_type);
                httppost.setHeader("primary_route", str_service_route);
                httppost.setHeader("service_areas_distance", str_service_areas);
                httppost.setHeader("local_government", str_service_govt);

                httppost.setHeader("vehicle_type", str_vec_type);

                if(str_ref1 != null && (!(str_ref1.isEmpty()))){
                    Log.e("tag","1notnull");
                    httppost.setHeader("reference1", str_ref1);
                }
                else{
                    Log.e("tag","1null");
                }
                if(str_ref2 != null && (!(str_ref2.isEmpty()))){
                    Log.e("tag","2notnull");
                    httppost.setHeader("reference2", str_ref2);
                }
                else{
                    Log.e("tag","2null");
                }
                if(str_ref3 != null && (!(str_ref3.isEmpty()))){
                    Log.e("tag","3notnull");
                    httppost.setHeader("reference3", str_ref3);
                }

                if(service_areas_pref != null && (!(service_areas_pref.isEmpty()))){
                    Log.e("tag","3notnull");
                    httppost.setHeader("service_areas", service_areas_pref);
                }

               // httppost.setHeader("driver_licence_name", str_lic_name);

              //  httppost.setHeader("driver_mobile_sec", str_mobile_prefix + str_lic_mobile);
               // httppost.setHeader("driver_licence_number", str_lic_no);



                try {

                    MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

                    File sourceFile = new File(str_lic_photo);
                    Log.e("tagtag3", "" + sourceFile);
                    entity.addPart("driverlicence", new FileBody(sourceFile, "image/jpeg"));
                    httppost.setEntity(entity);
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity r_entity = response.getEntity();
                    Log.e("tagurl", "ur:" + Config.WEB_URL + "driversignup");
                    Log.e("tag", "headers:" + httppost.getAllHeaders().toString());
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

                    if (status.equals("true")) {
                        dialog2.show();
                    } else {
                        if (msg.contains("Error OccuredError: ER_DUP_ENTRY: Duplicate entry")) {
                            snackbar.show();
                            tv_snack.setText(R.string.us_al);
                        } else {
                            snackbar.show();
                            tv_snack.setText(R.string.network);
                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("tag", "tagnt: " + e.toString());

                    snackbar.show();
                    tv_snack.setText(R.string.network);
                }
            } else {
                snackbar.show();
                tv_snack.setText(R.string.network);
            }
        }
    }









}







