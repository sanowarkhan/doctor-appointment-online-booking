package com.android.daob.activity;

import org.json.JSONArray;
import org.json.JSONException;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.daob.application.AppController;
import com.android.daob.utils.Constants;
import com.android.doctor_appointment_online_booking.R;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonArrayRequest;

public class PatientAppointmentDetailActivity extends BaseActivity {
	public static String TAG = PatientAppointmentDetailActivity.class
			.getSimpleName();

	String url = Constants.URL + "getAppointmentInfo/";

	String cancelApp = Constants.URL + "patientCancelAppointment/";

	TextView tvDoctorName, tvDate, tvTime, tvLocation, tvStatus, tvNote;
	int appId = 0;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.patient_appointment_detail_layout);
		tvDoctorName = (TextView) findViewById(R.id.tv_patient_app_doctor);
		tvDate = (TextView) findViewById(R.id.tv_patient_app_date);
		tvTime = (TextView) findViewById(R.id.tv_patient_app_start_time);
		tvLocation = (TextView) findViewById(R.id.tv_patient_app_location);
		tvStatus = (TextView) findViewById(R.id.tv_patient_app_status);
		tvNote = (TextView) findViewById(R.id.tv_patient_app_note);
		init(getIntent().getExtras());
		getAppoimentInfo();
	}
	
	void init(Bundle bun) {
		appId = bun.getInt("appointmentId");
	}
	
	void getAppoimentInfo() {
		String tag_json_getAppInfo = "json_getAppInfo_req";
		String content = PatientAppointmentDetailActivity.this.getResources()
				.getString(R.string.loading);
		showProgressDialog(content, false);
		url = url + appId;
		Log.i("appInfo: ", url);
		JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url,
				new Listener<JSONArray>() {

					@Override
					public void onResponse(JSONArray jsonArr) {
						try {
							tvDoctorName.setText(jsonArr.getJSONObject(0)
									.getString("doctorName"));
							tvDate.setText(jsonArr.getJSONObject(0).getString(
									"location"));
							tvTime.setText(jsonArr.getJSONObject(0).getString(
									"date"));
							tvLocation.setText(jsonArr.getJSONObject(0)
									.getString("startTime"));
							tvStatus.setText(jsonArr.getJSONObject(0)
									.getString("status"));
							tvNote.setText(jsonArr.getJSONObject(0).getString("note"));
						} catch (JSONException e) {
							e.printStackTrace();
						}
						closeProgressDialog();
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						VolleyLog.e(TAG, "Error: " + error.getMessage());
						closeProgressDialog();
					}
				});
		AppController.getInstance().addToRequestQueue(jsonArrayRequest,
				tag_json_getAppInfo);
	}
}
