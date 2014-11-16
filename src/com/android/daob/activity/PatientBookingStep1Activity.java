/**
 * 
 */
package com.android.daob.activity;

import com.android.daob.utils.Constants;
import com.android.doctor_appointment_online_booking.R;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;


public class PatientBookingStep1Activity extends BaseActivity implements
OnClickListener {

	public static String TAG = PatientBookingStep1Activity.class
			.getSimpleName();
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.);
		init();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getDashboard();
	}
	
	private void getDashboard() {
		// TODO Auto-generated method stub
		
	}

	void init() {
//		lvMeeting = (ListView) findViewById(R.id.lv_meeting_today);
//		btnMeetingNotApproved = (Button) findViewById(R.id.btn_not_approved);
//		btnMeetingNotApproved.setOnClickListener(this);
//		btnNextMeeting = (Button) findViewById(R.id.btn_next_meeting);
//		btnNextMeeting.setOnClickListener(this);
//		getDashboard();
	}
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}
	

}
