package com.android.daob.activity;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.daob.application.AppController;
import com.android.daob.model.DoctorAppointmentModel;
import com.android.daob.utils.Constants;
import com.android.doctor_appointment_online_booking.R;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

public class DoctorHomeActivity extends BaseActivity implements OnClickListener {

    Button btnNextMeeting, btnMeetingNotApproved;
    public static String TAG = DoctorHomeActivity.class.getSimpleName();
    String url = Constants.URL + "doctorDashboard/" + MainActivity.username;
    String urlUpdate = Constants.URL +"doctorUpdateAppointments/";

	ListView lvMeeting;
	TextView listEmpty;

	ArrayAdapter<DoctorAppointmentModel> listDoctorAppointmentAdapter;

	ArrayList<DoctorAppointmentModel> listDoctorAppointmentModels = new ArrayList<DoctorAppointmentModel>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.doctor_home_layout);
		init();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getDashboard();
	}
	
	void init() {
		lvMeeting = (ListView) findViewById(R.id.lv_meeting_today);
//		listEmpty = (TextView) findViewById(R.id.lv_empty);
//		lvMeeting.setEmptyView(listEmpty);
		btnMeetingNotApproved = (Button) findViewById(R.id.btn_not_approved);
		btnMeetingNotApproved.setOnClickListener(this);
		btnNextMeeting = (Button) findViewById(R.id.btn_next_meeting);
		btnNextMeeting.setOnClickListener(this);
		lvMeeting.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				final DoctorAppointmentModel dam = (DoctorAppointmentModel) parent
						.getItemAtPosition(position);
				if(dam.getStatus().equalsIgnoreCase(Constants.STATUS_NEW)){
					final Dialog dialog = new Dialog(DoctorHomeActivity.this);
					dialog.setContentView(R.layout.doctor_update_status_dialog);
					dialog.setTitle(DoctorHomeActivity.this
							.getResources().getString(R.string.message_confirm_reject));
					Button dialogConfrim = (Button) dialog.findViewById(R.id.doctor_dialog_confirm);
					dialogConfrim.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							String urlReq = urlUpdate + dam.getId();
							HashMap<String, String> updateStatus = new HashMap<String, String>();
							updateStatus.put("status", "confirmed");
							JsonObjectRequest jsonObjectReq = new JsonObjectRequest(
									Method.PUT, urlReq, new JSONObject(updateStatus),
									new Listener<JSONObject>() {

										@Override
										public void onResponse(
												JSONObject response) {
											try {
													if(response.getString("message").equals("success")){
													getDashboard();
													Toast.makeText(DoctorHomeActivity.this, getResources().getString(R.string.update_success), Toast.LENGTH_SHORT).show();
													dialog.dismiss();
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
					
					Button dialogReject = (Button) dialog.findViewById(R.id.doctor_dialog_reject);
					dialogReject.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							String urlReq = urlUpdate + dam.getId();
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
													getDashboard();
													Toast.makeText(DoctorHomeActivity.this, getResources().getString(R.string.update_success), Toast.LENGTH_SHORT).show();
													dialog.dismiss();
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
					Button dialogClose = (Button) dialog.findViewById(R.id.doctor_dialog_close);
					dialogClose.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							dialog.dismiss();
						}
					});
					dialog.show();
					
				}
				else if(dam.getStatus().equalsIgnoreCase(Constants.STATUS_CONFIRMED)){
					final Dialog dialog = new Dialog(DoctorHomeActivity.this);
					dialog.setContentView(R.layout.doctor_dialog_after_confirm);
					dialog.setTitle(DoctorHomeActivity.this
							.getResources().getString(R.string.message_done_cancel));
					Button dialogCancel = (Button) dialog.findViewById(R.id.doctor_dialog_canceled);
					dialogCancel.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							String urlReq = urlUpdate + dam.getId();
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
													getDashboard();
													Toast.makeText(DoctorHomeActivity.this, getResources().getString(R.string.update_success), Toast.LENGTH_SHORT).show();
													dialog.dismiss();
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
					
					Button dialogDone = (Button) dialog.findViewById(R.id.doctor_dialog_done);
					dialogDone.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							String urlReq = urlUpdate + dam.getId();
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
													getDashboard();
													Toast.makeText(DoctorHomeActivity.this, getResources().getString(R.string.update_success), Toast.LENGTH_SHORT).show();
													dialog.dismiss();
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
					Button dialogClose = (Button) dialog.findViewById(R.id.doctor_dialog_miss);
					dialogClose.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							dialog.dismiss();
						}
					});
					dialog.show();
				}
				return true;
			}
			
		});
		lvMeeting.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				final DoctorAppointmentModel dam = (DoctorAppointmentModel) parent
						.getItemAtPosition(position);
				Intent i = new Intent(DoctorHomeActivity.this, DoctorAppointmentDetailActivity.class);
				Bundle bun = new Bundle();
		        bun.putInt("appointmentId", dam.getId());
				i.putExtras(bun);
				startActivity(i);
			}
			
		});
		
		getDashboard();
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
			Intent nmIntent = new Intent(DoctorHomeActivity.this,
					DoctorNextMeetingActivity.class);
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
	
	

	void getDashboard() {
		String tag_json_getDashboard = "json_getDashboard_req";
		String content = DoctorHomeActivity.this.getResources().getString(
				R.string.loading);
		showProgressDialog(content, false);

		JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url,
				new Listener<JSONArray>() {

					@Override
					public void onResponse(JSONArray jsonArr) {
						listDoctorAppointmentModels.clear();
						for (int i = 0; i < jsonArr.length(); i++) {
							DoctorAppointmentModel dam = new DoctorAppointmentModel();
							try {
								dam.setPatientName(jsonArr.getJSONObject(i)
										.getString("patientName"));
								dam.setLocation(jsonArr.getJSONObject(i)
										.getString("location"));
								dam.setDate(jsonArr.getJSONObject(i).getString(
										"date"));
								dam.setStartTime(jsonArr.getJSONObject(i)
										.getString("startTime"));
								dam.setStatus(jsonArr.getJSONObject(i)
										.getString("status"));
								if (jsonArr.getJSONObject(i)
										.getString("preDescription").isEmpty()) {
									dam.setNotes("không có");	
								}
								else {
									dam.setNotes(jsonArr.getJSONObject(i)
											.getString("preDescription"));
								}
								dam.setId(jsonArr.getJSONObject(i).getInt("id"));
								listDoctorAppointmentModels.add(dam);
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
						if (listDoctorAppointmentModels != null
								&& listDoctorAppointmentModels.size() > 0) {
							Log.i("aa", "listDoctorAppointmentModels:"
									+ listDoctorAppointmentModels.size());
							listDoctorAppointmentAdapter = new DoctorAppViewAdapter(
									listDoctorAppointmentModels);

							lvMeeting.setAdapter(listDoctorAppointmentAdapter);
							listDoctorAppointmentAdapter.notifyDataSetChanged();
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
				tag_json_getDashboard);
	}

	class DoctorAppViewAdapter extends ArrayAdapter<DoctorAppointmentModel> {
		ViewHolder holder;

		ArrayList<DoctorAppointmentModel> listData;

		public DoctorAppViewAdapter(ArrayList<DoctorAppointmentModel> listData) {
			super(DoctorHomeActivity.this,
					R.layout.doctor_home_meeting_item_layout, listData);
			this.listData = listData;
		}

		public ArrayList<DoctorAppointmentModel> getListData() {
			return listData;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) getContext()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater
						.inflate(R.layout.doctor_home_meeting_item_layout,
								parent, false);
				holder = new ViewHolder();
				holder.tvPatientName = (TextView) convertView
						.findViewById(R.id.tv_patient_name);
				holder.tvLocation = (TextView) convertView
						.findViewById(R.id.tv_location_name);
				holder.tvDate = (TextView) convertView
						.findViewById(R.id.tv_date_appointment);
				holder.tvStartTime = (TextView) convertView
						.findViewById(R.id.tv_start_time);
				holder.tvStatus = (TextView) convertView
						.findViewById(R.id.tv_appointment_status);
//				holder.tvNotes = (TextView) convertView
//						.findViewById(R.id.tv_appointment_notes);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			DoctorAppointmentModel dam = listData.get(position);

			holder.tvPatientName.setText(dam.getPatientName());
			holder.tvLocation.setText(dam.getLocation());
			holder.tvDate.setText(dam.getDate());
			holder.tvStartTime.setText(dam.getStartTime());
			holder.tvStatus.setText(dam.getStatus());
//			holder.tvNotes.setText(dam.getNotes());

			if (dam.getStatus().equalsIgnoreCase(Constants.STATUS_NEW)) {
				holder.tvStatus.setText(DoctorHomeActivity.this.getResources()
						.getString(R.string.status_new));
				holder.tvStatus.setTextColor(DoctorHomeActivity.this
						.getResources().getColor(R.color.app_new));
			} else if (dam.getStatus().equalsIgnoreCase(
					Constants.STATUS_CONFIRMED)) {
				holder.tvStatus.setText(DoctorHomeActivity.this.getResources()
						.getString(R.string.status_confirmed));
				holder.tvStatus.setTextColor(DoctorHomeActivity.this
						.getResources().getColor(R.color.app_confirmed));
			} else if (dam.getStatus().equalsIgnoreCase(Constants.STATUS_DONE)) {
				holder.tvStatus.setText(DoctorHomeActivity.this.getResources()
						.getString(R.string.status_done));
				holder.tvStatus.setTextColor(DoctorHomeActivity.this
						.getResources().getColor(R.color.app_done));
			} else if (dam.getStatus().equalsIgnoreCase(
					Constants.STATUS_CANCELED)) {
				holder.tvStatus.setText(DoctorHomeActivity.this.getResources()
						.getString(R.string.status_canceled));
				holder.tvStatus.setTextColor(DoctorHomeActivity.this
						.getResources().getColor(R.color.app_canceled));
			} else if (dam.getStatus().equalsIgnoreCase(
					Constants.STATUS_REJECTED)){
				holder.tvStatus.setText(DoctorHomeActivity.this.getResources()
						.getString(R.string.status_rejected));
				holder.tvStatus.setTextColor(DoctorHomeActivity.this
						.getResources().getColor(R.color.app_rejected));
			} else if (dam.getStatus().equalsIgnoreCase(
					Constants.STATUS_MISSED)){
				holder.tvStatus.setText(DoctorHomeActivity.this.getResources()
						.getString(R.string.status_missed));
				holder.tvStatus.setTextColor(DoctorHomeActivity.this
						.getResources().getColor(R.color.app_missed));
			}

			return convertView;
		}
	}

	private class ViewHolder {
		TextView tvPatientName;

		TextView tvLocation;

		TextView tvDate;

		TextView tvStartTime;

		TextView tvStatus;

//		TextView tvNotes;

	}
}
