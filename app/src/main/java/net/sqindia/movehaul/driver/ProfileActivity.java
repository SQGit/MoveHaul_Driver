package net.sqindia.movehaul.driver;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.rey.material.widget.LinearLayout;
import com.sloop.fonts.FontsManager;

/**
 * Created by SQINDIA on 11/8/2016.
 */
public class ProfileActivity extends Activity {

    LinearLayout btn_back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);

        FontsManager.initFormAssets(this, "fonts/lato.ttf");
        FontsManager.changeFonts(this);

        btn_back = (LinearLayout) findViewById(R.id.layout_back);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent i = new Intent(Payment.this,DashboardNavigation.class);
                startActivity(i);*/
                finish();
            }
        });


    }

    @Override
    public void onBackPressed() {
        /*super.onBackPressed();
        Intent i = new Intent(Payment.this,DashboardNavigation.class);
        startActivity(i);*/
        finish();
    }
}
