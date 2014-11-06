
package com.android.daob.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.android.daob.application.AppController;
import com.android.daob.database.SQLiteTable;
import com.android.daob.model.SpecialtyModel;
import com.android.daob.model.WorkingPlaceModel;
import com.android.daob.utils.Constants;
import com.android.doctor_appointment_online_booking.R;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

public class PatientSearchDoctorActivity extends BaseActivity implements OnClickListener {

    public static String TAG = PatientSearchDoctorActivity.class.getSimpleName();

    String urlGetListWorkingAndSpecialty = Constants.URL + "getListWorkingAndSpecialty";

    String urlSearch = Constants.URL + "";

    Button btnSearch;

    Spinner spinnerWorkingPlace, spinnerSpecialty;

    List<WorkingPlaceModel> listWorkingPlaceModels = new ArrayList<WorkingPlaceModel>();

    List<SpecialtyModel> listSpecialtyModels = new ArrayList<SpecialtyModel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_search_layout);
        init();
    }

    void init() {
        spinnerWorkingPlace = (Spinner) findViewById(R.id.ddl_hospital);
        spinnerSpecialty = (Spinner) findViewById(R.id.ddl_speciality);

        btnSearch = (Button) findViewById(R.id.btn_search_doctor);
        btnSearch.setOnClickListener(this);
        if (!checkHasData()) {
            getWorkingAndSpecialtyFromServer();
        } else {
            getWorkingAndSpecialtyFromDB();
        }
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

    @Override
    public void onClick(View v) {
        Intent intentDetail = new Intent(PatientSearchDoctorActivity.this,
                PatientViewDoctorDetailActivity.class);
        startActivity(intentDetail);
    }

    private boolean checkHasData() {
        boolean hasData = false;
        long countSpe = 0;
        long countWP = 0;

        SQLiteTable sqLiteTable = new SQLiteTable(PatientSearchDoctorActivity.this);

        sqLiteTable.open();
        countSpe = sqLiteTable.getCountSpecialty();
        countWP = sqLiteTable.getCountWorkingPlace();

        if (countSpe > 0 && countWP > 0) {
            hasData = true;
        }

        sqLiteTable.close();

        return hasData;
    }

    void getWorkingAndSpecialtyFromServer() {
        String tag_json_getDashboard = "json_getWorkingAndSpecialty_req";
        String content = PatientSearchDoctorActivity.this.getResources()
                .getString(R.string.loading);
        showProgressDialog(content, false);
        listWorkingPlaceModels.clear();
        listSpecialtyModels.clear();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(urlGetListWorkingAndSpecialty,
                new Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray arr) {
                        SQLiteTable sqLiteTable = new SQLiteTable(PatientSearchDoctorActivity.this);

                        sqLiteTable.open();
                        sqLiteTable.deleteSpecialty();
                        sqLiteTable.deleteWorkingPlace();

                        JSONArray arrWp = new JSONArray();
                        JSONArray arrSpec = new JSONArray();
                        try {
                            arrWp = arr.getJSONObject(0).getJSONArray("workingPlace");
                            for (int j = 0; j < arrWp.length(); j++) {
                                WorkingPlaceModel wp = new WorkingPlaceModel();
                                wp.setWorkingPlaceId(arrWp.getJSONObject(j).getInt("id"));
                                wp.setWorkingPlaceName(arrWp.getJSONObject(j).getString("name"));
                                wp.setAddress(arrWp.getJSONObject(j).getString("address"));
                                sqLiteTable.insertWorkingPlace(wp);
                                listWorkingPlaceModels.add(wp);
                            }

                            arrSpec = arr.getJSONObject(1).getJSONArray("specialty");
                            for (int k = 0; k < arrSpec.length(); k++) {
                                SpecialtyModel spec = new SpecialtyModel();
                                spec.setSpecialtyId(arrSpec.getJSONObject(k).getInt("id"));
                                spec.setSpecialtyName(arrSpec.getJSONObject(k).getString("name"));
                                sqLiteTable.insertSpecialty(spec);
                                listSpecialtyModels.add(spec);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        sqLiteTable.close();
                        fillDataToSpinner();
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

    void getWorkingAndSpecialtyFromDB() {
        SQLiteTable sqLiteTable = new SQLiteTable(PatientSearchDoctorActivity.this);

        sqLiteTable.open();
        listWorkingPlaceModels.clear();
        listSpecialtyModels.clear();
        listWorkingPlaceModels = sqLiteTable.getAllWorkingPlace();
        listSpecialtyModels = sqLiteTable.getAllSpecialty();

        fillDataToSpinner();
    }

    void fillDataToSpinner() {
        WorkingPlaceModel wp = new WorkingPlaceModel();
        wp.setWorkingPlaceId(0);
        wp.setWorkingPlaceName("Tất cả");
        listWorkingPlaceModels.add(0, wp);
        
        SpecialtyModel spec = new SpecialtyModel();
        spec.setSpecialtyId(0);
        spec.setSpecialtyName("Tất cả");
        listSpecialtyModels.add(0, spec);
        
        ArrayAdapter<WorkingPlaceModel> wpAdapter = new ArrayAdapter<WorkingPlaceModel>(this,
                android.R.layout.simple_spinner_item, listWorkingPlaceModels);
        wpAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinnerWorkingPlace.setAdapter(wpAdapter);

        ArrayAdapter<SpecialtyModel> specAdapter = new ArrayAdapter<SpecialtyModel>(this,
                android.R.layout.simple_spinner_item, listSpecialtyModels);
        specAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinnerSpecialty.setAdapter(specAdapter);
    }
}
