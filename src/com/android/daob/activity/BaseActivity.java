
package com.android.daob.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.android.daob.utils.SessionManager;
import com.android.doctor_appointment_online_booking.R;

public class BaseActivity extends Activity {

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = new SessionManager(getApplicationContext());
    }

    @Override
    protected void onResume() {
        sessionManager.checkLogin();
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btn_logout:
                sessionManager.logoutUser();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
