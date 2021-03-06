package com.movhaul.driver;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.movhaul.driver.R;
import com.sloop.fonts.FontsManager;

import java.util.ArrayList;

/**
 * Created by SQINDIA on 10/27/2016.
 */

public class CustomerReviewsAdapter extends BaseAdapter {

    LayoutInflater inflater;
    Context context;
    Typeface tf;
    ArrayList<String> myList;
    Activity act;

    public CustomerReviewsAdapter(Activity activity, ArrayList array_list) {
        this.context = activity.getApplicationContext();
        this.myList = array_list;
        inflater = LayoutInflater.from(this.context);
        act =activity;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        final MyViewHolder mViewHolder;
        FontsManager.initFormAssets(act, "fonts/lato.ttf");       //initialization
        FontsManager.changeFonts(act);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.customer_review_adapter, viewGroup, false);
            mViewHolder = new MyViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }
        RatingBar ratingBar1 = (RatingBar) convertView.findViewById(R.id.ratingbar);
        LayerDrawable layerDrawable1 = (LayerDrawable) ratingBar1.getProgressDrawable();
        DrawableCompat.setTint(DrawableCompat.wrap(layerDrawable1.getDrawable(0)),
                context.getResources().getColor(R.color.light_grey));  // Empty star
        DrawableCompat.setTint(DrawableCompat.wrap(layerDrawable1.getDrawable(1)),
                context.getResources().getColor(R.color.gold)); // Partial star
        DrawableCompat.setTint(DrawableCompat.wrap(layerDrawable1.getDrawable(2)),
                context.getResources().getColor(R.color.gold));
        return convertView;
    }
    private class MyViewHolder {
        public MyViewHolder(View item) {
        }
    }
}
