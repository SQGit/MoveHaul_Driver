package com.movhaul.driver;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.rey.material.widget.Button;
import com.rey.material.widget.ImageView;
import com.sloop.fonts.FontsManager;

/**
 * Created by Salman on 02-05-2017.
 */

public class Register_New extends AppCompatActivity {
    Button btn_submit;
    int status, adi_sts;
    LinearLayout lt_first, lt_second, lt_additional, lt_service_types,lt_serviceareas,lt_experience;
    TextView tv_header_txt;
    ScrollView scrollView;
    com.rey.material.widget.TextView tv_additional,tv_additional_hide;
    public static String[] sting,ar_service_areas,ar_experience;
    //static Context context = this;
    Typeface tf;
    EditText et_service_type,et_service_areas,et_experience;
    TextInputLayout til_service_type,til_service_areas,til_username,til_mobile,til_email,til_address,til_emg_contact;
    LinearLayout lt_route_local,lt_route_state,lt_govt_local,lt_govt_state;
    ImageButton ib_route_local,ib_route_state,ib_govt_local,ib_govt_state;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_new);

        FontsManager.initFormAssets(Register_New.this, "fonts/lato.ttf");
        FontsManager.changeFonts(Register_New.this);
        tf = Typeface.createFromAsset(getAssets(), "fonts/lato.ttf");

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


        lt_first.setVisibility(View.VISIBLE);
        lt_second.setVisibility(View.GONE);
        lt_additional.setVisibility(View.INVISIBLE);

        sting = new String[]{"Haulage-Local", "Haulage-Interstate", "Service Truck-Party Van", "Service Truck-Cooling van", "Service Truck-Gas", "Tow truck", "Bus Rental-Charter"};

        ar_service_areas = new String[]{"50-100 miles", "100-200 miles", "200+ miles"};
        ar_experience = new String[]{" 1 "," 2 "," 3 "," 4 ","5+"};

        til_service_type.setTypeface(tf);
        til_service_areas.setTypeface(tf);
        til_username.setTypeface(tf);
        til_mobile.setTypeface(tf);
        til_email.setTypeface(tf);
        til_address.setTypeface(tf);
        til_emg_contact.setTypeface(tf);

        lt_route_local.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ib_route_local.setBackground(getResources().getDrawable(R.mipmap.select_tick));
                ib_route_state.setBackground(getResources().getDrawable(R.mipmap.unselecting_tick));
                return false;
            }
        });
        lt_route_state.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ib_route_state.setBackground(getResources().getDrawable(R.mipmap.select_tick));
                ib_route_local.setBackground(getResources().getDrawable(R.mipmap.unselecting_tick));
                return false;
            }
        });

        lt_govt_local.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ib_govt_local.setBackground(getResources().getDrawable(R.mipmap.select_tick));
                ib_govt_state.setBackground(getResources().getDrawable(R.mipmap.unselecting_tick));
                return false;
            }
        });

        lt_govt_state.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ib_govt_state.setBackground(getResources().getDrawable(R.mipmap.select_tick));
                ib_govt_local.setBackground(getResources().getDrawable(R.mipmap.unselecting_tick));
                return false;
            }
        });



        lt_service_types.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

/*
                AlertDialog.Builder builder = new AlertDialog.Builder(Register_New.this);

                builder.setItems(sting, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // The 'which' argument contains the index position
                                // of the selected item
                                et_service_type.setText(sting[which]);
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

                for(int i = 0;i<sting.length;i++) {

                    a = new LinearLayout(Register_New.this);
                    LinearLayout.LayoutParams para = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
                    para.setMargins(5,20,5,20);
                    a.setOrientation(LinearLayout.HORIZONTAL);
                    a.setLayoutParams(para);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(60,60);
                    params.gravity = Gravity.CENTER;
                    ImageView imageView = new ImageView(Register_New.this);
                    imageView.setImageResource(R.drawable.button_change);
                    imageView.setLayoutParams(params);
                    TextView tss = new TextView(Register_New.this);
                    tss.setText(sting[i]);
                    LinearLayout.LayoutParams paramsQ = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
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

                    final int k =i;

                    a.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.e("tag","a:"+sting[k]);
                            b.dismiss();
                            et_service_type.setText(sting[k]);
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

                for(int i = 0;i<ar_service_areas.length;i++) {

                    a = new LinearLayout(Register_New.this);
                    LinearLayout.LayoutParams para = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
                    para.setMargins(5,20,5,20);
                    a.setOrientation(LinearLayout.HORIZONTAL);
                    a.setLayoutParams(para);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(60,60);
                    params.gravity = Gravity.CENTER;
                    ImageView imageView = new ImageView(Register_New.this);
                    imageView.setImageResource(R.drawable.button_change);
                    imageView.setLayoutParams(params);
                    TextView tss = new TextView(Register_New.this);
                    tss.setText(ar_service_areas[i]);
                    LinearLayout.LayoutParams paramsQ = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
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

                    final int k =i;

                    a.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.e("tag","a:"+ar_service_areas[k]);
                            b.dismiss();
                            et_service_areas.setText(ar_service_areas[k]);
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

                for(int i = 0;i<ar_experience.length;i++) {

                    a = new LinearLayout(Register_New.this);
                    LinearLayout.LayoutParams para = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
                    para.setMargins(5,20,5,20);
                    a.setOrientation(LinearLayout.HORIZONTAL);
                    a.setLayoutParams(para);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(60,60);
                    params.gravity = Gravity.CENTER;
                    ImageView imageView = new ImageView(Register_New.this);
                    imageView.setImageResource(R.drawable.button_change);
                    imageView.setLayoutParams(params);
                    TextView tss = new TextView(Register_New.this);
                    tss.setText(ar_experience[i]);
                    LinearLayout.LayoutParams paramsQ = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
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

                    final int k =i;

                    a.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.e("tag","a:"+ar_experience[k]);
                            b.dismiss();
                            et_experience.setText(ar_experience[k]);
                        }
                    });
                }
                b.show();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (status == 0) {
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
                }
            }
        });

        tv_additional.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    lt_additional.setVisibility(View.VISIBLE);
                    scrollView.fullScroll(View.FOCUS_DOWN);
                tv_additional.setVisibility(View.GONE);




            }
        });

        tv_additional_hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollView.fullScroll(View.FOCUS_UP);
                lt_additional.setVisibility(View.INVISIBLE);
                tv_additional.setVisibility(View.VISIBLE);
            }
        });


    }



}







