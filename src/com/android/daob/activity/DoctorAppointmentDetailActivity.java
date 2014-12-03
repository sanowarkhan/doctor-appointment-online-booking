package com.android.daob.activity;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.daob.application.AppController;
import com.android.daob.utils.Constants;
import com.android.doctor_appointment_online_booking.R;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

public class DoctorAppointmentDetailActivity extends BaseActivity {
	public static String TAG = PatientAppointmentDetailActivity.class
			.getSimpleName();

	String url = Constants.URL + "getAppointmentInfo/";

	String urlUpdate = Constants.URL + "doctorUpdateAppointments/";

	TextView tvPatientName, tvDate, tvTime, tvLocation, tvStatus, tvNote;
	Button btnConfirm, btnReject, btnCancel, btnDone;

	int appId = 0;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.doctor_apppointment_detail_layout);
		tvPatientName = (TextView) findViewById(R.id.tv_doctor_app_patient);
		tvDate = (TextView) findViewById(R.id.tv_doctor_app_date);
		tvTime = (TextView) findViewById(R.id.tv_doctor_app_start_time);
		tvLocation = (TextView) findViewById(R.id.tv_doctor_app_location);
		tvStatus = (TextView) findViewById(R.id.tv_doctor_app_status);
		tvNote = (TextView) findViewById(R.id.tv_doctor_app_note);
		btnConfirm = (Button) findViewById(R.id.btn_confirm_app);
		btnConfirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String urlReq = urlUpdate + appId;
				HashMap<String, String> updateStatus = new HashMap<String, String>();
				updateStatus.put("status", "confirmed");
				JsonObjectRequest jsonObjectReq = new JsonObjectRequest(
						Method.PUT, urlReq, new JSONObject(updateStatus),
						new Listener<JSONObject>() {

							@Override
							public void onResponse(JSONObject response) {
								try {
									if (response.getString("message").equals(
											"success")) {
										getAppoimentInfo();
										Toast.makeText(
												DoctorAppointmentDetailActivity.this,
												"Cập nhật thành công",
												Toast.LENGTH_SHORT).show();
									}
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}

						}, new Response.ErrorListener() {
							@Override
							public void onErrorResponse(VolleyError arg0) {
								// TODO Auto-generated
								// method stub
								VolleyLog.e(TAG, "Error: " + arg0.getMessage());
							}

						});
				AppController.getInstance().addToRequestQueue(jsonObjectReq,
						"update to confirm");
			}
		});
		btnReject = (Button) findViewById(R.id.btn_reject_app);
		btnReject.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String urlReq = urlUpdate + appId;
				HashMap<String, String> updateStatus = new HashMap<String, String>();
				updateStatus.put("status", "rejected");
				JsonObjectRequest jsonObjectReq = new JsonObjectRequest(
						Method.PUT, urlReq, new JSONObject(updateStatus),
						new Listener<JSONObject>() {

							@Override
							public void onResponse(
									JSONObject response) {
								try {
										if(response.getString("message").equals("success")){
											tvStatus.setText(getApplicationContext()
													.getResources().getString(
															R.string.status_rejected));
										Toast.makeText(DoctorAppointmentDetailActivity.this, getResources().getString(R.string.update_success), Toast.LENGTH_SHORT).show();
									}
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}

						}, new Response.ErrorListener() {
							@Override
							public void onErrorResponse(
									VolleyError arg0) {
								// TODO Auto-generated
								// method stub
								VolleyLog.e(TAG, "Error: " + arg0.getMessage());	
							}

						});
				AppController.getInstance().addToRequestQueue(jsonObjectReq,
						"update to reject");
			}
		});
		btnCancel = (Button) findViewById(R.id.btn_cancel_app);
		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String urlReq = urlUpdate + appId;
				HashMap<String, String> updateStatus = new HashMap<String, String>();
				updateStatus.put("status", "canceled");
				JsonObjectRequest jsonObjectReq = new JsonObjectRequest(
						Method.PUT, urlReq, new JSONObject(updateStatus),
						new Listener<JSONObject>() {

							@Override
							public void onResponse(
									JSONObject response) {
								try {
										if(response.getString("message").equals("success")){
											tvStatus.setText(getApplicationContext()
													.getResources().getString(
															R.string.status_canceled));
										Toast.makeText(DoctorAppointmentDetailActivity.this, getResources().getString(R.string.update_success), Toast.LENGTH_SHORT).show();
									}
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}

						}, new Response.ErrorListener() {
							@Override
							public void onErrorResponse(
									VolleyError arg0) {
								// TODO Auto-generated
								// method stub
								VolleyLog.e(TAG, "Error: " + arg0.getMessage());	
							}

						});
				AppController.getInstance().addToRequestQueue(jsonObjectReq,
						"update to confirm");
			}
		});
		btnDone = (Button) findViewById(R.id.btn_done_app);
		btnDone.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String urlReq = urlUpdate + appId;
				HashMap<String, String> updateStatus = new HashMap<String, String>();
				updateStatus.put("status", "done");
				JsonObjectRequest jsonObjectReq = new JsonObjectRequest(
						Method.PUT, urlReq, new JSONObject(updateStatus),
						new Listener<JSONObject>() {

							@Override
							public void onResponse(
									JSONObject response) {
								try {
										if(response.getString("message").equals("success")){
											tvStatus.setText(getApplicationContext()
													.getResources().getString(
															R.string.status_done));
										Toast.makeText(DoctorAppointmentDetailActivity.this, getResources().getString(R.string.update_success), Toast.LENGTH_SHORT).show();
									}
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}

						}, new Response.ErrorListener() {
							@Override
							public void onErrorResponse(
									VolleyError arg0) {
								// TODO Auto-generated
								// method stub
								VolleyLog.e(TAG, "Error: " + arg0.getMessage());	
							}

						});
				AppController.getInstance().addToRequestQueue(jsonObjectReq,
						"update to confirm");
			}
		});
		init(getIntent().getExtras());
		getAppoimentInfo();
	}

	void init(Bundle bun) {
		appId = bun.getInt("appointmentId");
	}

	void getAppoimentInfo() {
		String tag_json_getAppInfo = "json_getAppInfo_req";
		String content = DoctorAppointmentDetailActivity.this.getResources()
				.getString(R.string.loading);
		showProgressDialog(content, false);
		url = url + appId;
		Log.i("appInfo: ", url);
		JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url,
				new Listener<JSONArray>() {

					@Override
					public void onResponse(JSONArray jsonArr) {
						try {
							tvPatientName.setText(jsonArr.getJSONObject(0)
									.getString("patientName"));
							tvDate.setText(jsonArr.getJSONObject(0).getString(
									"location"));
							tvTime.setText(jsonArr.getJSONObject(0).getString(
									"date"));
							tvLocation.setText(jsonArr.getJSONObject(0)
									.getString("startTime"));
							tvStatus.setText(jsonArr.getJSONObject(0)
									.getString("status"));
							String status = jsonArr.getJSONObject(0).getString(
									"status");
							if (status.equalsIgnoreCase(Constants.STATUS_NEW)) {
								tvStatus.setText(getApplicationContext()
										.getResources().getString(
												R.string.status_new));
								tvStatus.setTextColor(DoctorAppointmentDetailActivity.this
										.getResources().getColor(R.color.red));
								btnCancel.setVisibility(View.GONE);
								btnDone.setVisibility(View.GONE);
							} else if (status
									.equalsIgnoreCase(Constants.STATUS_CONFIRMED)) {
								tvStatus.setText(getApplicationContext()
										.getResources().getString(
												R.string.status_confirmed));
								tvStatus.setTextColor(DoctorAppointmentDetailActivity.this
										.getResources().getColor(R.color.blue));
								btnReject.setVisibility(View.GONE);
								btnConfirm.setVisibility(View.GONE);
							} else if (status
									.equalsIgnoreCase(Constants.STATUS_DONE)) {
								tvStatus.setText(getApplicationContext()
										.getResources().getString(
												R.string.status_done));
								tvStatus.setTextColor(DoctorAppointmentDetailActivity.this
										.getResources().getColor(R.color.black));
								btnCancel.setVisibility(View.GONE);
								btnDone.setVisibility(View.GONE);
								btnReject.setVisibility(View.GONE);
								btnConfirm.setVisibility(View.GONE);
							} else if (status
									.equalsIgnoreCase(Constants.STATUS_CANCELED)) {
								tvStatus.setText(getApplicationContext()
										.getResources().getString(
												R.string.status_canceled));
								tvStatus.setTextColor(DoctorAppointmentDetailActivity.this
										.getResources().getColor(R.color.aqua));
								btnCancel.setVisibility(View.GONE);
								btnDone.setVisibility(View.GONE);
								btnReject.setVisibility(View.GONE);
								btnConfirm.setVisibility(View.GONE);
							} else if (status
									.equalsIgnoreCase(Constants.STATUS_REJECTED)) {
								tvStatus.setText(getApplicationContext()
										.getResources().getString(
												R.string.status_rejected));
								tvStatus.setTextColor(DoctorAppointmentDetailActivity.this
										.getResources().getColor(
												R.color.Brown_BurlyWood));
								btnCancel.setVisibility(View.GONE);
								btnDone.setVisibility(View.GONE);
								btnReject.setVisibility(View.GONE);
								btnConfirm.setVisibility(View.GONE);
							}

							tvNote.setText(jsonArr.getJSONObject(0).getString(
									"preDescription"));
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
