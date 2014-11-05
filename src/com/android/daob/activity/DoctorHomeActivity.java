
package com.android.daob.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.android.doctor_appointment_online_booking.R;

public class DoctorHomeActivity extends BaseActivity implements OnClickListener {

    Button btnNextMeeting, btnMeetingNotApproved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_home_layout);
        init();
    }

    void init() {
        btnMeetingNotApproved = (Button) findViewById(R.id.btn_not_approved);
        btnMeetingNotApproved.setOnClickListener(this);
        btnNextMeeting = (Button) findViewById(R.id.btn_next_meeting);
        btnNextMeeting.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_not_approved:
                Intent naIntent = new Intent(DoctorHomeActivity.this,
                        DoctorMeetingNotApprovedActivity.class);
                startActivity(naIntent);
                break;

            case R.id.btn_next_meeting:
                Intent nmIntent = new Intent(DoctorHomeActivity.this, DoctorNextMeetingActivity.class);
                startActivity(nmIntent);
                break;

            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_right_menu, menu);
        MenuItem item = menu.findItem(R.id.top_right_button);
        item.setVisible(false);
        this.invalidateOptionsMenu();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}
