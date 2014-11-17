
package com.android.daob.activity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.daob.application.AppController;
import com.android.daob.model.DoctorFreeTimeModel;
import com.android.daob.utils.Constants;
import com.android.doctor_appointment_online_booking.R;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonArrayRequest;

public class PatientViewDoctorFreeTimeActivity extends BaseActivity implements OnClickListener {

    public static String TAG = PatientViewDoctorFreeTimeActivity.class.getSimpleName();
    String url = Constants.URL + "getDoctorFreeTime?id=1&date=18-11-2014" ;

	ListView lvDoctorFreeTime;
	ArrayAdapter<DoctorFreeTimeModel> listDoctorFreeTimeApdater;

	ArrayList<DoctorFreeTimeModel> listDoctorFreeTimeModel = new ArrayList<DoctorFreeTimeModel>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_view_doctor_free_time_layout);
        init();
    }

    void init() {
//        btnViewCalendar = (Button) findViewById(R.id.btn_view_calendar);
//        btnViewCalendar.setOnClickListener(this);
    	lvDoctorFreeTime = (ListView) findViewById(R.id.lvDoctorFreeTime);
    	
        getDoctorFreeTime();
    }

    private void getDoctorFreeTime() {
    	String tag_json_getDashboard = "json_getDashboard_req";
		String content = PatientViewDoctorFreeTimeActivity.this.getResources().getString(
				R.string.loading);
		showProgressDialog(content, false);

		JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url,
				new Listener<JSONArray>() {

					@Override
					public void onResponse(JSONArray jsonArr) {
						listDoctorFreeTimeModel.clear();
						for (int i = 0; i < jsonArr.length(); i++) {
							DoctorFreeTimeModel dft = new DoctorFreeTimeModel();
							try {
								dft.setLocation(jsonArr.getJSONObject(i)
										.getString("location"));
								dft.setStartTime(jsonArr.getJSONObject(i)
										.getString("startTime") + "-" + 
										jsonArr.getJSONObject(i).getString("endTime") );
								listDoctorFreeTimeModel.add(dft);
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
						if (listDoctorFreeTimeModel != null
								&& listDoctorFreeTimeModel.size() > 0) {
							Log.i("aa", "listDoctorAppointmentModels:"
									+ listDoctorFreeTimeModel.size());
							listDoctorFreeTimeApdater = new DoctorFreeTimeViewAdapter(
									listDoctorFreeTimeModel);

							lvDoctorFreeTime.setAdapter(listDoctorFreeTimeApdater);
							listDoctorFreeTimeApdater.notifyDataSetChanged();
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

	@Override
    public void onClick(View v) {
//        Intent intentCalendar = new Intent(PatientViewDoctorFreeTimeActivity.this, PatientCalendarActivity.class);
//        startActivity(intentCalendar);
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
                Intent intentHome = new Intent(this, PatientHomeActivity.class);
                startActivity(intentHome);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    class DoctorFreeTimeViewAdapter extends ArrayAdapter<DoctorFreeTimeModel> {
		ViewHolder holder;

		ArrayList<DoctorFreeTimeModel> listData;

		public DoctorFreeTimeViewAdapter(ArrayList<DoctorFreeTimeModel> listData) {
			super(PatientViewDoctorFreeTimeActivity.this,
					R.layout.patient_view_doctor_free_time_item_layout, listData);
			this.listData = listData;
		}

		public ArrayList<DoctorFreeTimeModel> getListData() {
			return listData;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) getContext()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater
						.inflate(R.layout.patient_view_doctor_free_time_item_layout,
								parent, false);
				holder = new ViewHolder();
				holder.tvLocation = (TextView) convertView
						.findViewById(R.id.tv_free_location_name);
				holder.tvStartTime = (TextView) convertView
						.findViewById(R.id.tv_free_start_time);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			DoctorFreeTimeModel dft = listData.get(position);

			holder.tvLocation.setText(dft.getLocation());
			holder.tvStartTime.setText(dft.getStartTime());
			return convertView;
		}
	}

	private class ViewHolder {
		TextView tvLocation;

		TextView tvStartTime;
	}

}
