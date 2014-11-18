package com.android.daob.activity;

import com.android.daob.utils.Constants;
import com.android.doctor_appointment_online_booking.R;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

public class PatientBookingStep2Activity extends BaseActivity implements OnClickListener {
	
	public static String TAG = PatientBookingStep2Activity.class.getSimpleName();
    
    private String url = Constants.URL + "processStep2";
    
    EditText txtConfirmKey;

    TextView tvMeetingDate, tvDoctorName, tvLocation, tvName, tvOld, tvGender, tvPhone, tvAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_booking_step2_layout);
        init();
//        init(getIntent().getExtras());
    }
    
	private void init() {
		// TODO Auto-generated method stub
		tvName = (TextView) findViewById(R.id.tv_patient_name);
		tvOld = (TextView) findViewById(R.id.tv_patient_old);
		tvGender = (TextView) findViewById(R.id.tv_patient_gender);
		tvPhone = (TextView) findViewById(R.id.tv_patient_phone);
		tvAddress = (TextView) findViewById(R.id.tv_patient_address);
		tvDoctorName = (TextView) findViewById(R.id.tv_meeting_doctor);
		tvMeetingDate = (TextView) findViewById(R.id.tv_meeting_date);
		tvLocation = (TextView) findViewById(R.id.tv_meeting_location);
		
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_right_menu, menu);
        menu.getItem(0).setTitle(getResources().getString(R.string.home));
        menu.getItem(0).setIcon(this.getResources().getDrawable(R.drawable.ic_action_go_to_today));
        return super.onCreateOptionsMenu(menu);
    }

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}

}
