
package com.android.daob.activity;

import com.android.doctor_appointment_online_booking.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class NextMeetingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.next_meeting_layout);
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
                Intent intentHome = new Intent(this, DoctorHomeActivity.class);
                startActivity(intentHome);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}