package net.sqindia.movehaul.driver;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.rey.material.widget.Button;
import com.rey.material.widget.TextView;

import java.util.ArrayList;


/**
 * Created by Salman on 7/28/2016.
 */

public class Dialog_bidding_confirm extends Dialog {

    public Activity activity;
    Typeface tf;
    Button  d2_btn_ok;
    android.widget.TextView  d2_tv_dialog1, d2_tv_dialog2, d2_tv_dialog3, d2_tv_dialog4;
    Context context;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ArrayList<String> ar_goods_type = new ArrayList<>();
    ImageView btn_close2;



    public Dialog_bidding_confirm(Activity activity, ArrayList<String> ar_goods) {
        super(activity);
        this.activity = activity;
        this.ar_goods_type = ar_goods;


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_bidding_confirm);

        tf = Typeface.createFromAsset(activity.getAssets(), "fonts/lato.ttf");

        Intent getIntent = activity.getIntent();
        String name = getIntent.getStringExtra("name");


        d2_btn_ok = (Button) findViewById(R.id.button_ok);
        btn_close2 = (ImageView) findViewById(R.id.button_close);
        d2_tv_dialog1 = (android.widget.TextView) findViewById(R.id.textView_1);
        d2_tv_dialog2 = (android.widget.TextView) findViewById(R.id.textView_2);
        d2_tv_dialog3 = (android.widget.TextView) findViewById(R.id.textView_3);
        d2_tv_dialog4 = (android.widget.TextView) findViewById(R.id.textView_4);

        d2_tv_dialog1.setTypeface(tf);
        d2_tv_dialog2.setTypeface(tf);
        d2_tv_dialog3.setTypeface(tf);
        d2_tv_dialog4.setTypeface(tf);
        d2_btn_ok.setTypeface(tf);

        d2_tv_dialog2.setText("Your Bidding has been accepted by"+name);


        d2_tv_dialog4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getContext(), MyTrips.class);
                context.startActivity(i);
            }
        });

        btn_close2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), DashboardNavigation.class);
                context.startActivity(i);
            }
        });

        d2_btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), MyTrips.class);
                context.startActivity(i);
            }
        });

    }




}