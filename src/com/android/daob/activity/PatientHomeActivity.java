
package com.android.daob.activity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.daob.application.AppController;
import com.android.daob.model.UserMeetingModel;
import com.android.daob.utils.Constants;
import com.android.doctor_appointment_online_booking.R;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

public class PatientHomeActivity extends BaseActivity {
    public static String TAG = PatientHomeActivity.class.getSimpleName();

    String url = Constants.URL + "patientDashboard/" + MainActivity.username;

    ListView lvMeeting;

    ArrayAdapter<UserMeetingModel> listMeetingAdapter;

    ArrayList<UserMeetingModel> listUserMeetingModels = new ArrayList<UserMeetingModel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_home_layout);
        lvMeeting = (ListView) findViewById(R.id.lvMeeting);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        getDashboard();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_right_menu, menu);
        menu.getItem(0).setTitle(getResources().getString(R.string.search_doctor));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.top_right_button:
                Intent intentSearch = new Intent(this, SearchDoctorActivity.class);
                startActivity(intentSearch);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

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
        String content = PatientHomeActivity.this.getResources().getString(R.string.loading);
        showProgressDialog(content, false);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray jsonArr) {
                for (int i = 0; i < jsonArr.length(); i++) {
                    UserMeetingModel umm = new UserMeetingModel();
                    JSONObject jsonObjDoctor = new JSONObject();
                    try {
                        jsonObjDoctor = jsonArr.getJSONObject(i).getJSONObject("doctor");
                        umm.setDoctorName(jsonObjDoctor.getString("displayName"));
                        umm.setHospital(jsonArr.getJSONObject(i).getString("location"));
                        umm.setDate(jsonArr.getJSONObject(i).getString("meetingDate"));
                        umm.setHour(jsonArr.getJSONObject(i).getString("startTime"));
                        umm.setStatus(jsonArr.getJSONObject(i).getString("status"));
                        listUserMeetingModels.add(umm);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (listUserMeetingModels != null && listUserMeetingModels.size() > 0) {
                    listMeetingAdapter = new UserMeetingViewAdapter(listUserMeetingModels);
                    lvMeeting.setAdapter(listMeetingAdapter);
                    listMeetingAdapter.notifyDataSetChanged();
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
        AppController.getInstance().addToRequestQueue(jsonArrayRequest, tag_json_getDashboard);
    }

    class UserMeetingViewAdapter extends ArrayAdapter<UserMeetingModel> {
        ViewHolder holder;

        ArrayList<UserMeetingModel> listData;

        public UserMeetingViewAdapter(ArrayList<UserMeetingModel> listData) {
            super(PatientHomeActivity.this, R.layout.patient_meeting_item_layout, listData);
            this.listData = listData;
        }

        public ArrayList<UserMeetingModel> getListData() {
            return listData;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.patient_meeting_item_layout, parent, false);
                holder = new ViewHolder();
                holder.tvDoctorName = (TextView) convertView.findViewById(R.id.tv_doctor_name);
                holder.tvHospital = (TextView) convertView.findViewById(R.id.tv_hospital_name);
                holder.tvDate = (TextView) convertView.findViewById(R.id.tv_date_meeting);
                holder.tvHour = (TextView) convertView.findViewById(R.id.tv_hour_meeting);
                holder.tvStatus = (TextView) convertView.findViewById(R.id.tv_status_meeting);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            UserMeetingModel umm = listData.get(position);

            holder.tvDoctorName.setText(umm.getDoctorName());
            holder.tvHospital.setText(umm.getHospital());
            holder.tvDate.setText(umm.getDate());
            holder.tvHour.setText(umm.getHour());

            if (umm.getStatus().equalsIgnoreCase(Constants.STATUS_NEW)) {
                holder.tvStatus.setText(getApplicationContext().getResources().getString(
                        R.string.status_new));
            } else if (umm.getStatus().equalsIgnoreCase(Constants.STATUS_CONFIRMED)) {
                holder.tvStatus.setText(getApplicationContext().getResources().getString(
                        R.string.status_confirmed));
            } else if (umm.getStatus().equalsIgnoreCase(Constants.STATUS_DONE)) {
                holder.tvStatus.setText(getApplicationContext().getResources().getString(
                        R.string.status_done));
            } else if (umm.getStatus().equalsIgnoreCase(Constants.STATUS_CANCELED)) {
                holder.tvStatus.setText(getApplicationContext().getResources().getString(
                        R.string.status_canceled));
            }

            return convertView;
        }
    }

    private class ViewHolder {
        TextView tvDoctorName;

        TextView tvHospital;

        TextView tvDate;

        TextView tvHour;

        TextView tvStatus;
    }
}
