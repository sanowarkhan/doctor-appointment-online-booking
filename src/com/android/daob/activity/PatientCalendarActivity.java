
package com.android.daob.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.android.daob.application.AppController;
import com.android.daob.component.SelectHourDialog;
import com.android.daob.model.DoctorFreeTimeModel;
import com.android.daob.model.DoctorModel;
import com.android.daob.utils.Constants;
import com.android.doctor_appointment_online_booking.R;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

public class PatientCalendarActivity extends BaseActivity {
    public static String TAG = PatientCalendarActivity.class.getSimpleName();

    TextView tvDoctorName;

    CalendarView calendarView;

    Button btnSelectHour;

    Date currentDate = null;

    String urlGetTimeServer = Constants.URL + "serverDateTime";

    String urlGetFreeTime = Constants.URL + "doctorFreeTime?";

    int doctorId = 0;

    ArrayList<Object> listDoctorFreeTimeModel = new ArrayList<Object>();

    SelectHourDialog freeTimeDialog;

    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_calendar_layout);
        init(getIntent().getExtras());
    }

    void init(Bundle bun) {
        DoctorModel doctor = (DoctorModel) bun.getSerializable(Constants.DATA_KEY);
        doctorId = doctor.getDoctorId();
        tvDoctorName = (TextView) findViewById(R.id.tv_doctor_name);
        tvDoctorName.setText(doctor.getDoctorName());

        calendarView = (CalendarView) findViewById(R.id.calendarView);
        calendarView.setFirstDayOfWeek(Calendar.MONDAY);

        btnSelectHour = (Button) findViewById(R.id.btn_select_hour);

        calendarView.setOnDateChangeListener(new OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                if (currentDate != null) {
                    String selectedDayString = year + "-" + (month + 1) + "-" + dayOfMonth;
                    try {
                        Date dateSelected = new Date();
                        dateSelected = formatter.parse(selectedDayString);
                        if (dateSelected.after(currentDate) || dateSelected.equals(currentDate)) {
                            btnSelectHour.setEnabled(true);
                        } else {
                            Toast.makeText(
                                    PatientCalendarActivity.this,
                                    PatientCalendarActivity.this.getResources().getString(
                                            R.string.select_old_day), Toast.LENGTH_SHORT).show();
                            btnSelectHour.setEnabled(false);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(PatientCalendarActivity.this, "null", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnSelectHour.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                long dateSelected = calendarView.getDate();
                String dateSelectedFormat = new SimpleDateFormat("dd-MM-yyyy").format(new Date(
                        dateSelected));
                Log.i("aa", dateSelectedFormat);
                getDoctorFreeTime(dateSelectedFormat);
            }
        });

        getTimeServer();
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

    private void getTimeServer() {
        String tag_json_getTimeServer = "json_getTimeServer_req";
        String content = PatientCalendarActivity.this.getResources().getString(R.string.loading);
        showProgressDialog(content, false);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Method.GET, urlGetTimeServer,
                null, new Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            String dateResponse = jsonObject.getString("serverDate");
                            currentDate = formatter.parse(dateResponse);

                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(currentDate);

                            calendarView.setDate(calendar.getTimeInMillis());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (ParseException e) {
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
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_getTimeServer);
    }

    private void getDoctorFreeTime(String date) {
        String tag_json_getDoctorFreeTime = "json_getDoctorFreeTime_req";
        String content = PatientCalendarActivity.this.getResources().getString(R.string.loading);
        showProgressDialog(content, false);

        String url = urlGetFreeTime + "doctorId=" + doctorId + "&date=" + date;
        
        Log.i("aa", url);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url,
                new Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray jsonArr) {
                        listDoctorFreeTimeModel.clear();
                        Log.i("aa", "" + jsonArr.toString());
                        for (int i = 0; i < jsonArr.length(); i++) {
                            DoctorFreeTimeModel dft = new DoctorFreeTimeModel();
                            try {
                                dft.setLocation(jsonArr.getJSONObject(i).getString("location"));
                                dft.setStartTime(jsonArr.getJSONObject(i).getString("startTime"));
                                dft.setEndTime(jsonArr.getJSONObject(i).getString("endTime"));
                                dft.setDoctorId(jsonArr.getJSONObject(i).getInt("doctor"));
                                dft.setMeetingDate(jsonArr.getJSONObject(i).getString("meetingDate"));
                                listDoctorFreeTimeModel.add(dft);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        closeProgressDialog();
                        if (freeTimeDialog == null) {
                            freeTimeDialog = new SelectHourDialog(PatientCalendarActivity.this);
                        }
                        freeTimeDialog.setItems(listDoctorFreeTimeModel);
                        freeTimeDialog.show();

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.e(TAG, "Error: " + error.getMessage());
                        closeProgressDialog();
                    }
                });
        AppController.getInstance().addToRequestQueue(jsonArrayRequest, tag_json_getDoctorFreeTime);
    }

}
