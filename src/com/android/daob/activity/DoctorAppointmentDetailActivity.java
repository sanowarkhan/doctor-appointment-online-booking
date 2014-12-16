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

public class DoctorAppointmentDetailActivity extends BaseActivity {
	public static String TAG = PatientAppointmentDetailActivity.class
			.getSimpleName();

	String url = Constants.URL + "getAppointmentInfo/";

	String urlUpdate = Constants.URL + "doctorUpdateAppointments/";

	String urlMarkBusy = Constants.URL + "markBusyTime";

	TextView tvPatientName, tvDate, tvTime, tvLocation, tvStatus, tvNote;
	Button btnConfirm, btnReject, btnCancel, btnMissed;

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
				requestServer("confirmed", "", tvStatus);
				tvStatus.setText(getApplicationContext().getResources()
						.getString(R.string.status_confirmed));
				tvStatus.setTextColor(DoctorAppointmentDetailActivity.this
						.getResources().getColor(R.color.app_confirmed));
				btnConfirm.setVisibility(View.GONE);
				btnReject.setVisibility(View.GONE);
				btnCancel.setVisibility(View.VISIBLE);
			}
		});
		btnReject = (Button) findViewById(R.id.btn_reject_app);
		btnReject.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						DoctorAppointmentDetailActivity.this);
				builder.setTitle(getApplicationContext().getResources()
						.getString(R.string.reason_rejected));

				final EditText input = new EditText(
						DoctorAppointmentDetailActivity.this);
				input.setInputType(InputType.TYPE_CLASS_TEXT);
				builder.setView(input);

				builder.setPositiveButton(getApplicationContext()
						.getResources().getString(R.string.btn_cancel),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();
							}
						});
				builder.setNegativeButton(getApplicationContext()
						.getResources().getString(R.string.reject_app),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
								imm.hideSoftInputFromWindow(
										input.getWindowToken(), 0);
								String message = input.getText().toString();
								requestServer("rejected", message, tvStatus);

								AlertDialog.Builder builder1 = new AlertDialog.Builder(
										DoctorAppointmentDetailActivity.this);
								builder1.setTitle(getApplicationContext()
										.getResources().getString(
												R.string.create_busy_time));

								builder1.setPositiveButton(
										getApplicationContext().getResources()
												.getString(R.string.btn_no),
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												dialog.cancel();
											}
										});
								builder1.setNegativeButton(
										getApplicationContext().getResources()
												.getString(R.string.btn_yes),
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												markBusyTime();
											}
										});

								builder1.show();

								btnConfirm.setVisibility(View.GONE);
								btnReject.setVisibility(View.GONE);
								tvStatus.setText(getApplicationContext().getResources()
										.getString(R.string.status_rejected));
								tvStatus.setTextColor(DoctorAppointmentDetailActivity.this
										.getResources().getColor(R.color.app_rejected));
								Toast.makeText(
										DoctorAppointmentDetailActivity.this,
										getResources().getString(
												R.string.update_success),
										Toast.LENGTH_SHORT).show();
							}
						});

				builder.show();

			}
		});
		btnCancel = (Button) findViewById(R.id.btn_cancel_app);
		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				AlertDialog.Builder builder = new AlertDialog.Builder(
						DoctorAppointmentDetailActivity.this);
				builder.setTitle(getApplicationContext().getResources()
						.getString(R.string.reason_canceled));

				final EditText input = new EditText(
						DoctorAppointmentDetailActivity.this);
				input.setInputType(InputType.TYPE_CLASS_TEXT);
				builder.setView(input);

				builder.setPositiveButton(getApplicationContext()
						.getResources().getString(R.string.btn_cancel),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();
							}
						});
				builder.setNegativeButton(getApplicationContext()
						.getResources().getString(R.string.cancled_app),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
								imm.hideSoftInputFromWindow(
										input.getWindowToken(), 0);
								String message = input.getText().toString();
								requestServer("canceled", message, tvStatus);
								AlertDialog.Builder builder1 = new AlertDialog.Builder(
										DoctorAppointmentDetailActivity.this);
								builder1.setTitle(getApplicationContext()
										.getResources().getString(
												R.string.create_busy_time));

								builder1.setPositiveButton(
										getApplicationContext().getResources()
												.getString(R.string.btn_no),
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												dialog.cancel();
											}
										});
								builder1.setNegativeButton(
										getApplicationContext().getResources()
												.getString(R.string.btn_yes),
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												markBusyTime();
											}
										});

								builder1.show();
								tvStatus.setText(getApplicationContext().getResources()
										.getString(R.string.status_canceled));
								tvStatus.setTextColor(DoctorAppointmentDetailActivity.this
										.getResources().getColor(R.color.app_canceled));
								btnCancel.setVisibility(View.GONE);
								Toast.makeText(
										DoctorAppointmentDetailActivity.this,
										getResources().getString(
												R.string.update_success),
										Toast.LENGTH_SHORT).show();
							}
						});
				builder.show();
			}
		});
		btnMissed = (Button) findViewById(R.id.btn_miss_app);
		btnMissed.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				requestServer("missed", "", tvStatus);
				tvStatus.setText(getApplicationContext().getResources()
						.getString(R.string.status_missed));
				tvStatus.setTextColor(DoctorAppointmentDetailActivity.this
						.getResources().getColor(R.color.app_missed));
				btnMissed.setVisibility(View.GONE);
			}
		});
		init(getIntent().getExtras());
		getAppoimentInfo();
	}

	void init(Bundle bun) {
		appId = bun.getInt("appointmentId");
	}

	void markBusyTime() {
		HashMap<String, String> markBusy = new HashMap<String, String>();
		markBusy.put("appointmentId", "" + appId);
		String content = DoctorAppointmentDetailActivity.this.getResources()
				.getString(R.string.processing);
		showProgressDialog(content, false);
		JsonObjectRequest jsonObjectReq = new JsonObjectRequest(Method.POST,
				urlMarkBusy, new JSONObject(markBusy),
				new Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject arg0) {
						// TODO Auto-generated method stub
						try {
							if (arg0.getString("message").equals("success")) {
								Toast.makeText(
										DoctorAppointmentDetailActivity.this,
										"Đã cài đặt bận cho khoảng thời gian này",
										Toast.LENGTH_SHORT).show();
							} else {
								Toast.makeText(
										DoctorAppointmentDetailActivity.this,
										"Cài đặt không thành công",
										Toast.LENGTH_SHORT).show();
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						closeProgressDialog();
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
				"markBusyTime");
	}

	void requestServer(final String status, String message, TextView tv) {
		String urlReq = urlUpdate + appId;
		HashMap<String, String> updateStatus = new HashMap<String, String>();
		updateStatus.put("status", status);
		updateStatus.put("message", message);
		String content = DoctorAppointmentDetailActivity.this.getResources()
				.getString(R.string.processing);
		showProgressDialog(content, false);
		JsonObjectRequest jsonObjectReq = new JsonObjectRequest(Method.PUT,
				urlReq, new JSONObject(updateStatus),
				new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						try {
							if (response.getString("message").equals("success")) {
								if (status.equals("missed")) {
									tvStatus.setText(getApplicationContext()
											.getResources().getString(
													R.string.status_missed));
									tvStatus.setTextColor(DoctorAppointmentDetailActivity.this
											.getResources().getColor(
													R.color.app_missed));
								} else if (status.equals("confirmed")) {
									tvStatus.setText(getApplicationContext()
											.getResources().getString(
													R.string.status_confirmed));
									tvStatus.setTextColor(DoctorAppointmentDetailActivity.this
											.getResources().getColor(
													R.color.app_confirmed));
								} else if (status.equals("canceled")) {
									tvStatus.setText(getApplicationContext()
											.getResources().getString(
													R.string.status_canceled));
									tvStatus.setTextColor(DoctorAppointmentDetailActivity.this
											.getResources().getColor(
													R.color.app_canceled));
								} else if (status.equals("rejected")) {
									tvStatus.setText(getApplicationContext()
											.getResources().getString(
													R.string.status_rejected));
									tvStatus.setTextColor(DoctorAppointmentDetailActivity.this
											.getResources().getColor(
													R.color.app_rejected));
								}

								Toast.makeText(
										DoctorAppointmentDetailActivity.this,
										getResources().getString(
												R.string.update_success),
										Toast.LENGTH_SHORT).show();
							} else {
								Toast.makeText(
										DoctorAppointmentDetailActivity.this,
										"Cuộc hẹn đã được cập nhật trạng thái này từ trước",
										Toast.LENGTH_SHORT).show();
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						closeProgressDialog();
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
				"updateAppointment");
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
									"date"));
							tvTime.setText(jsonArr.getJSONObject(0).getString(
									"startTime"));
							tvLocation.setText(jsonArr.getJSONObject(0)
									.getString("location"));
							tvStatus.setText(jsonArr.getJSONObject(0)
									.getString("status"));
							String status = jsonArr.getJSONObject(0).getString(
									"status");
							if (status.equalsIgnoreCase(Constants.STATUS_NEW)) {
								tvStatus.setText(getApplicationContext()
										.getResources().getString(
												R.string.status_new));
								tvStatus.setTextColor(DoctorAppointmentDetailActivity.this
										.getResources().getColor(
												R.color.app_new));
								btnReject.setVisibility(View.VISIBLE);
								btnConfirm.setVisibility(View.VISIBLE);
							} else if (status
									.equalsIgnoreCase(Constants.STATUS_CONFIRMED)) {
								tvStatus.setText(getApplicationContext()
										.getResources().getString(
												R.string.status_confirmed));
								tvStatus.setTextColor(DoctorAppointmentDetailActivity.this
										.getResources().getColor(
												R.color.app_confirmed));
								btnCancel.setVisibility(View.VISIBLE);
							} else if (status
									.equalsIgnoreCase(Constants.STATUS_DONE)) {
								tvStatus.setText(getApplicationContext()
										.getResources().getString(
												R.string.status_done));
								tvStatus.setTextColor(DoctorAppointmentDetailActivity.this
										.getResources().getColor(
												R.color.app_done));
								btnMissed.setVisibility(View.VISIBLE);
							} else if (status
									.equalsIgnoreCase(Constants.STATUS_CANCELED)) {
								tvStatus.setText(getApplicationContext()
										.getResources().getString(
												R.string.status_canceled));
								tvStatus.setTextColor(DoctorAppointmentDetailActivity.this
										.getResources().getColor(
												R.color.app_canceled));
							} else if (status
									.equalsIgnoreCase(Constants.STATUS_REJECTED)) {
								tvStatus.setText(getApplicationContext()
										.getResources().getString(
												R.string.status_rejected));
								tvStatus.setTextColor(DoctorAppointmentDetailActivity.this
										.getResources().getColor(
												R.color.app_rejected));
							} else if (status
									.equalsIgnoreCase(Constants.STATUS_MISSED)) {
								tvStatus.setText(getApplicationContext()
										.getResources().getString(
												R.string.status_missed));
								tvStatus.setTextColor(DoctorAppointmentDetailActivity.this
										.getResources().getColor(
												R.color.app_missed));
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
