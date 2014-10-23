
package com.android.daob.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.android.doctor_appointment_online_booking.R;

public class DoctorDetailActivity extends BaseActivity implements OnClickListener {

    Button btnViewCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_details_layout);
        init();
    }

    void init() {
        btnViewCalendar = (Button) findViewById(R.id.btn_view_calendar);
        btnViewCalendar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intentCalendar = new Intent(DoctorDetailActivity.this, CalendarActivity.class);
        startActivity(intentCalendar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_right_menu, menu);
        menu.getItem(0).setTitle(getResources().getString(R.string.home));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.top_right_button:
                finish();
                Intent intentHome = new Intent(this, PatientHomeActivity.class);
                startActivity(intentHome);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
