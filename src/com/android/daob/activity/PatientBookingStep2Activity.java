package com.android.daob.activity;

import java.io.UnsupportedEncodingException;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.daob.utils.Constants;
import com.android.daob.utils.GlobalStorage;
import com.android.doctor_appointment_online_booking.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class PatientBookingStep2Activity extends BaseActivity implements
		OnClickListener {

	public static String TAG = PatientBookingStep2Activity.class
			.getSimpleName();

	private String url = Constants.URL + "processStep2";

	EditText txtConfirmKey;

	TextView tvMeetingDate, tvDoctorName, tvLocation, tvName, tvOld, tvGender,
			tvPhone, tvAddress;
	Button btnFinishBooking;

	int serverConfirmKey;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.patient_booking_step2_layout);
		serverConfirmKey = getIntent().getExtras()
				.getInt(Constants.CONFIRM_KEY);
		btnFinishBooking = (Button) findViewById(R.id.btn_finish_booking);
		btnFinishBooking.setOnClickListener(this);
		txtConfirmKey = (EditText) findViewById(R.id.tb_confim_key);
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		tvDoctorName = (TextView) findViewById(R.id.tv_meeting_doctor);
		tvDoctorName.setText(PatientBookingStep1Activity.doctorname);
		tvMeetingDate = (TextView) findViewById(R.id.tv_meeting_date);
		tvMeetingDate.setText(PatientBookingStep1Activity.meetingDate);
		tvLocation = (TextView) findViewById(R.id.tv_meeting_location);
		tvLocation.setText(PatientBookingStep1Activity.location);

//		tvMeetingDate.setText(pbm.getDelPatName());
//		tvLocation.setText(pbm.getLocation());
		tvDoctorName.setText("Nguyễn Trọng Phát");

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.top_right_menu, menu);
		menu.getItem(0).setTitle(getResources().getString(R.string.home));
		menu.getItem(0).setIcon(
				this.getResources().getDrawable(
						R.drawable.ic_action_go_to_today));
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public void onClick(View arg0) {
		if (serverConfirmKey == Integer.parseInt(txtConfirmKey.getText()
				.toString())) {
			AsyncHttpClient client = new AsyncHttpClient();
			StringEntity entity = null;
			try {
				entity = new StringEntity(GlobalStorage.sTempBooking.toString());
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
			client.post(this, url, entity, "application/json", new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode, Header[] headers,
						JSONObject response) {
					try {
						if ("success".equals(response.getString("status"))) {
							// Go home
							Log.i(TAG, "Appointment booked");
							GlobalStorage.sTempBooking = null;
							Intent i = new Intent(PatientBookingStep2Activity.this, PatientHomeActivity.class);
							startActivity(i);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}

				@Override
				public void onFailure(int statusCode, Header[] headers,
						String responseString, Throwable throwable) {
					Log.e("booking", responseString);
				}
			});
			// JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
			// Method.POST, url, GlobalStorage.sTempBooking,
			// new Listener<JSONObject>() {
			//
			// @Override
			// public void onResponse(JSONObject response) {
			// try {
			// if ("success".equals(response
			// .getString("status"))) {
			// // Go home
			// Log.i(TAG, "Appointment booked");
			// }
			// } catch (JSONException e) {
			// e.printStackTrace();
			// }
			//
			// }
			// }, new Response.ErrorListener() {
			// @Override
			// public void onErrorResponse(VolleyError error) {
			// Log.e(TAG, "Error: sadfadf");
			// Log.e(TAG, "Error: " + error.getMessage());
			// }
			// }) {
			//
			// };
			// AppController.getInstance().addToRequestQueue(jsonObjectRequest,
			// "json_booking_req");
		}
	}

}
