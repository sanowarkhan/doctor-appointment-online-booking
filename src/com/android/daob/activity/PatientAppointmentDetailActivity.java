package com.android.daob.activity;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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

public class PatientAppointmentDetailActivity extends BaseActivity {
	public static String TAG = PatientAppointmentDetailActivity.class
			.getSimpleName();

	String url = Constants.URL + "getAppointmentInfo/";

	String cancelApp = Constants.URL + "patientCancelAppointment/";

	TextView tvDoctorName, tvDate, tvTime, tvLocation, tvStatus, tvNote,
			tvPreNote;
	Button btnCancel;
	String appId = "";

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.patient_appointment_detail_layout);
		tvDoctorName = (TextView) findViewById(R.id.tv_patient_app_doctor);
		tvDate = (TextView) findViewById(R.id.tv_patient_app_date);
		tvTime = (TextView) findViewById(R.id.tv_patient_app_start_time);
		tvLocation = (TextView) findViewById(R.id.tv_patient_app_location);
		tvStatus = (TextView) findViewById(R.id.tv_patient_app_status);
		tvNote = (TextView) findViewById(R.id.tv_patient_app_note);
		tvPreNote = (TextView) findViewById(R.id.tv_patient_app_preDesc);
		btnCancel = (Button) findViewById(R.id.btn_patient_detail_cancel_app);
		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final String urlUpdate = cancelApp + appId;
				AlertDialog.Builder builder1 = new AlertDialog.Builder(
						PatientAppointmentDetailActivity.this);
				final EditText input = new EditText(
						PatientAppointmentDetailActivity.this);
				input.setInputType(InputType.TYPE_CLASS_TEXT);
				builder1.setView(input);
				builder1.setTitle(getApplicationContext().getResources()
						.getString(R.string.reason_canceled));

				builder1.setPositiveButton(getApplicationContext()
						.getResources().getString(R.string.btn_cancel),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();
							}
						});
				builder1.setNegativeButton(getApplicationContext()
						.getResources().getString(R.string.cancled_app),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
								imm.hideSoftInputFromWindow(
										input.getWindowToken(), 0);
								HashMap<String, String> updateStatus = new HashMap<String, String>();
								updateStatus.put("status", "canceled");
								updateStatus.put("message", input.getText()
										.toString());
								String content = PatientAppointmentDetailActivity.this
										.getResources().getString(
												R.string.processing);
								showProgressDialog(content, false);
								JsonObjectRequest jsonObbjectReq = new JsonObjectRequest(
										Method.PUT, urlUpdate, new JSONObject(
												updateStatus),
										new Listener<JSONObject>() {

											@Override
											public void onResponse(
													JSONObject response) {
												// TODO Auto-generated
												// method stub
												try {
													if (response.getString(
															"message").equals(
															"success")) {
														tvStatus.setText(getApplicationContext()
																.getResources()
																.getString(
																		R.string.status_canceled));
														tvStatus.setTextColor(getApplicationContext()
																.getResources()
																.getColor(
																		R.color.app_canceled));
														btnCancel
																.setVisibility(View.GONE);
														Toast.makeText(
																PatientAppointmentDetailActivity.this,
																getResources()
																		.getString(
																				R.string.update_success),
																Toast.LENGTH_SHORT)
																.show();
													} else {
														Toast.makeText(
																PatientAppointmentDetailActivity.this,
																"Cuộc hẹn đã được cập nhật trước đó",
																Toast.LENGTH_SHORT)
																.show();
													}
												} catch (JSONException e) {
													// TODO Auto-generated catch
													// block
													e.printStackTrace();
												}
												closeProgressDialog();
											}

										}, new Response.ErrorListener() {
											@Override
											public void onErrorResponse(
													VolleyError arg0) {
												// TODO Auto-generated
												// method stub
												VolleyLog.e(TAG, "Error: "
														+ arg0.getMessage());
											}

										});
								AppController.getInstance().addToRequestQueue(
										jsonObbjectReq, "update to canceled");
							}
						});

				builder1.show();

			}
		});
		init(getIntent().getExtras());
		getAppoimentInfo();
	}

	void init(Bundle bun) {
		appId = bun.getString("appointmentId");
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
									"date"));
							tvTime.setText(jsonArr.getJSONObject(0).getString(
									"startTime"));
							tvLocation.setText(jsonArr.getJSONObject(0)
									.getString("location"));
							String status = jsonArr.getJSONObject(0).getString(
									"status");
							if (status.equalsIgnoreCase(Constants.STATUS_NEW)) {
								tvStatus.setText(getApplicationContext()
										.getResources().getString(
												R.string.status_new));
								tvStatus.setTextColor(PatientAppointmentDetailActivity.this
										.getResources().getColor(
												R.color.app_new));
								btnCancel.setVisibility(View.VISIBLE);
							} else if (status
									.equalsIgnoreCase(Constants.STATUS_CONFIRMED)) {
								tvStatus.setText(getApplicationContext()
										.getResources().getString(
												R.string.status_confirmed));
								tvStatus.setTextColor(PatientAppointmentDetailActivity.this
										.getResources().getColor(
												R.color.app_confirmed));
								btnCancel.setVisibility(View.VISIBLE);
							} else if (status
									.equalsIgnoreCase(Constants.STATUS_DONE)) {
								tvStatus.setText(getApplicationContext()
										.getResources().getString(
												R.string.status_done));
								tvStatus.setTextColor(PatientAppointmentDetailActivity.this
										.getResources().getColor(
												R.color.app_done));
							} else if (status
									.equalsIgnoreCase(Constants.STATUS_CANCELED)) {
								tvStatus.setText(getApplicationContext()
										.getResources().getString(
												R.string.status_canceled));
								tvStatus.setTextColor(PatientAppointmentDetailActivity.this
										.getResources().getColor(
												R.color.app_canceled));
							} else if (status
									.equalsIgnoreCase(Constants.STATUS_REJECTED)) {
								tvStatus.setText(getApplicationContext()
										.getResources().getString(
												R.string.status_rejected));
								tvStatus.setTextColor(PatientAppointmentDetailActivity.this
										.getResources().getColor(
												R.color.app_rejected));
							} else if (status
									.equalsIgnoreCase(Constants.STATUS_MISSED)) {
								tvStatus.setText(getApplicationContext()
										.getResources().getString(
												R.string.status_missed));
								tvStatus.setTextColor(PatientAppointmentDetailActivity.this
										.getResources().getColor(
												R.color.app_missed));
							}
							if (jsonArr.getJSONObject(0).getString("note")
									.isEmpty()
									|| jsonArr.getJSONObject(0)
											.getString("note")
											.equals("undefined")) {
								tvNote.setText("không có");
							} else {
								tvNote.setText(jsonArr.getJSONObject(0)
										.getString("note"));
							}
							if (jsonArr.getJSONObject(0)
									.getString("preDescription").isEmpty()
									|| jsonArr.getJSONObject(0)
											.getString("preDescription")
											.equals("undefined")) {
								tvPreNote.setText("không có");
							} else {
								tvPreNote.setText(jsonArr.getJSONObject(0)
										.getString("preDescription"));
							}
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
