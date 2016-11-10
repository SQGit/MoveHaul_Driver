package net.sqindia.movehaul.driver;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.rey.material.widget.LinearLayout;

/**
 * Created by sqindia on 10-11-2016.
 */

public class Bank_details extends Activity {
    LinearLayout btn_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bank_details);

        btn_back = (LinearLayout) findViewById(R.id.layout_back);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),DashboardNavigation.class);
                startActivity(i);
            }
        });
    }
}
