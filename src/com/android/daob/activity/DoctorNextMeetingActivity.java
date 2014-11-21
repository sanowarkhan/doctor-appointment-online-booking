
package com.android.daob.activity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import com.android.daob.application.AppController;
import com.android.daob.model.DoctorAppointmentModel;
import com.android.daob.utils.Constants;
import com.android.doctor_appointment_online_booking.R;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonArrayRequest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class DoctorNextMeetingActivity extends BaseActivity {
	public static String TAG = DoctorHomeActivity.class.getSimpleName();
    String url = Constants.URL + "doctorAllAppointments/" + MainActivity.username;
    String urlUpdate = Constants.URL +"doctorUpdateAppointments/";

	ListView lvMeeting = (ListView) findViewById(R.id.lv_next_meeting);

	ArrayAdapter<DoctorAppointmentModel> listDoctorAppointmentAdapter;

	ArrayList<DoctorAppointmentModel> listDoctorAppointmentModels = new ArrayList<DoctorAppointmentModel>();
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_next_meeting_layout);
        init();
    }
    void init() {
    	lvMeeting = (ListView) findViewById(R.id.lv_next_meeting);
    	getDashboard();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_right_menu, menu);
        menu.getItem(0).setTitle(getResources().getString(R.string.home));
        menu.getItem(0).setIcon(this.getResources().getDrawable(R.drawable.ic_action_go_to_today));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.top_right_button:
                finish();
                Intent intentHome = new Intent(this, DoctorHomeActivity.class);
                startActivity(intentHome);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    void getDashboard() {
		String tag_json_getDashboard = "json_getDashboard_req";
		String content = DoctorNextMeetingActivity.this.getResources().getString(
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
								dam.setNotes(jsonArr.getJSONObject(i)
										.getString("notes"));
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
			super(DoctorNextMeetingActivity.this,
					R.layout.doctor_all_appoinment_items_layout, listData);
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
						.inflate(R.layout.doctor_all_appoinment_items_layout,
								parent, false);
				holder = new ViewHolder();
				holder.tvPatientName = (TextView) convertView
						.findViewById(R.id.tv_all_patient_name);
				holder.tvLocation = (TextView) convertView
						.findViewById(R.id.tv_all_location_name);
				holder.tvDate = (TextView) convertView
						.findViewById(R.id.tv_all_date_appointment);
				holder.tvStartTime = (TextView) convertView
						.findViewById(R.id.tv_all_start_time);
				holder.tvStatus = (TextView) convertView
						.findViewById(R.id.tv_all_appointment_status);
				holder.tvNotes = (TextView) convertView
						.findViewById(R.id.tv_all_appointment_notes);
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
			holder.tvNotes.setText(dam.getNotes());

			if (dam.getStatus().equalsIgnoreCase(Constants.STATUS_NEW)) {
				holder.tvStatus.setText(getApplicationContext().getResources()
						.getString(R.string.status_new));
			} else if (dam.getStatus().equalsIgnoreCase(
					Constants.STATUS_CONFIRMED)) {
				holder.tvStatus.setText(getApplicationContext().getResources()
						.getString(R.string.status_confirmed));
			} else if (dam.getStatus().equalsIgnoreCase(Constants.STATUS_DONE)) {
				holder.tvStatus.setText(getApplicationContext().getResources()
						.getString(R.string.status_done));
			} else if (dam.getStatus().equalsIgnoreCase(
					Constants.STATUS_CANCELED)) {
				holder.tvStatus.setText(getApplicationContext().getResources()
						.getString(R.string.status_canceled));
			} else {
				holder.tvStatus.setText(getApplicationContext().getResources()
						.getString(R.string.reject_app));
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

		TextView tvNotes;

	}

}
