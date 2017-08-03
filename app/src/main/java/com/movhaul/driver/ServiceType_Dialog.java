package com.movhaul.driver;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rey.material.app.DialogFragment;

/**
 * Created by Salman on 03-05-2017.
 * not used
 */

public class ServiceType_Dialog extends DialogFragment {


    TextView[] myTextViews;
    LinearLayout linearLayout ;
    Context cont;
    String[] sting;

    public void ServiceType_Dialog(Context context,String[] sting){
        this.cont = context;
        this.sting = sting;
        myTextViews = new TextView[sting.length];
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.layout_dialog,container,false);
        // getDialog().setTitle("Sample");
        //TextView mDoneBtn = (TextView) mView.findViewById(R.id.textview);

        linearLayout = (LinearLayout) mView.findViewById(R.id.layout);

        for (int i = 0; i < sting.length; i++) {

            final TextView rowTextView = new TextView(cont);

            rowTextView.setLayoutParams(new LinearLayout.LayoutParams(             //select linearlayoutparam- set the width & height
                    ViewGroup.LayoutParams.MATCH_PARENT, 30));
            rowTextView.setTextColor(getResources().getColor(R.color.redColor));
            rowTextView.setPadding(5,5,5,5);
            rowTextView.setTextSize(30);

            // set some properties of rowTextView or something
            rowTextView.setText(sting[i]);
            Log.e("tag","daa: "+sting[i]);

            // add the textview to the linearlayout
            linearLayout.addView(rowTextView);

            // save a reference to the textview for later
            myTextViews[i] = rowTextView;
        }

//
        //   mDoneBtn.setOnClickListener(DoneAction);
        return mView;
    }

}
