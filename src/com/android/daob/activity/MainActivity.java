
package com.android.daob.activity;

import java.util.HashMap;

import com.android.daob.utils.SessionManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends Activity {

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        sessionManager = new SessionManager(getApplicationContext());
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetails();
        String role = user.get(SessionManager.KEY_ROLE);
        if (role != null) {
            Intent i;
            if (role.equalsIgnoreCase("0")) { // redirect to patient activity
                i = new Intent(MainActivity.this, PatientHomeActivity.class);
            } else if (role.equalsIgnoreCase("1")) { // redirect to doctor
                                                     // activity
                i = new Intent(MainActivity.this, DoctorHomeActivity.class);
            } else { // redirect to doctor login activity
                i = new Intent(MainActivity.this, LoginActivity.class);
            }
            startActivity(i);
        }
    }

}
