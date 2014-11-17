
package com.android.daob.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.daob.application.AppController;
import com.android.daob.database.SQLiteTable;
import com.android.daob.model.DoctorModel;
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

    String urlGetListWorkingAndSpecialty = Constants.URL + "getListDoctorWorkingSpecialty";

    String urlSearch = Constants.URL + "";

    Button btnSearch;

    EditText doctorNameTxt;

    ListView lvDoctor;

    Spinner spinnerWorkingPlace, spinnerSpecialty;

    List<WorkingPlaceModel> listWorkingPlaceModels = new ArrayList<WorkingPlaceModel>();

    List<SpecialtyModel> listSpecialtyModels = new ArrayList<SpecialtyModel>();

    List<DoctorModel> listDoctorModels = new ArrayList<DoctorModel>();

    ArrayAdapter<DoctorModel> listDoctorResultAdapter;

    ArrayList<DoctorModel> listDoctors = new ArrayList<DoctorModel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_search_layout);
        init();
    }

    int workingPlaceId = 0;

    int specialtyId = 0;

    void init() {
        lvDoctor = (ListView) findViewById(R.id.lv_doctor_search_result);
        // lvDoctor.setOnItemClickListener((OnItemClickListener) this);
        lvDoctor.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DoctorModel doctor = (DoctorModel) parent.getItemAtPosition(position);
                Bundle bun = new Bundle();
                bun.putSerializable(Constants.DATA_KEY, doctor);
                Intent intent = new Intent(PatientSearchDoctorActivity.this,
                        PatientCalendarActivity.class);
                intent.putExtras(bun);
                startActivity(intent);

            }
        });
        spinnerWorkingPlace = (Spinner) findViewById(R.id.ddl_hospital);
        spinnerWorkingPlace.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    WorkingPlaceModel wp = (WorkingPlaceModel) parent.getItemAtPosition(position);
                    workingPlaceId = wp.getWorkingPlaceId();
                } else {
                    workingPlaceId = 0;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // nothing to do
            }
        });

        spinnerSpecialty = (Spinner) findViewById(R.id.ddl_speciality);
        spinnerSpecialty.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    SpecialtyModel wp = (SpecialtyModel) parent.getItemAtPosition(position);
                    specialtyId = wp.getSpecialtyId();
                } else {
                    specialtyId = 0;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }

        });
        doctorNameTxt = (EditText) findViewById(R.id.tv_search_doctor);
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
        // Intent intentDetail = new Intent(PatientSearchDoctorActivity.this,
        // PatientViewDoctorDetailActivity.class);
        // startActivity(intentDetail);
        // listDoctorModels.clear();
        SQLiteTable sqLiteTable = new SQLiteTable(PatientSearchDoctorActivity.this);

        sqLiteTable.open();
        listDoctors = sqLiteTable.searchDoctor(specialtyId, workingPlaceId, doctorNameTxt.getText()
                .toString());
        listDoctorResultAdapter = new DoctorResultViewAdapter(listDoctors);
        lvDoctor.setAdapter(listDoctorResultAdapter);
        listDoctorResultAdapter.notifyDataSetChanged();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(btnSearch.getWindowToken(), 0);
        // Intent intent = new Intent(this,
        // PatientViewDoctorFreeTimeActivity.class);
        // startActivity(intent);

    }

    private boolean checkHasData() {
        boolean hasData = false;
        long countSpe = 0;
        long countWP = 0;
        long countDoc = 0;

        SQLiteTable sqLiteTable = new SQLiteTable(PatientSearchDoctorActivity.this);

        sqLiteTable.open();
        countSpe = sqLiteTable.getCountSpecialty();
        countWP = sqLiteTable.getCountWorkingPlace();
        countDoc = sqLiteTable.getCountDoctor();

        if (countSpe > 0 && countWP > 0 && countDoc > 0) {
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
        listDoctorModels.clear();
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
                        JSONArray arrDoc = new JSONArray();
                        try {
                            arrDoc = arr.getJSONObject(2).getJSONArray("doctor");
                            for (int i = 0; i < arrDoc.length(); i++) {
                                DoctorModel doc = new DoctorModel();
                                doc.setDoctorId(arrDoc.getJSONObject(i).getInt("id"));
                                doc.setDoctorName(arrDoc.getJSONObject(i).getString("name"));
                                doc.setDescription(arrDoc.getJSONObject(i).getString("description"));
                                doc.setEducation(arrDoc.getJSONObject(i).getString("education"));
                                doc.setSpecialty(arrDoc.getJSONObject(i).getInt("specialty"));
                                doc.setDoctorWorkingPlace(arrDoc.getJSONObject(i).getString(
                                        "workingPlace"));
                                sqLiteTable.insertDoctor(doc);
                                listDoctorModels.add(doc);
                            }

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
        wp.setWorkingPlaceName(getResources().getString(R.string.all));
        listWorkingPlaceModels.add(0, wp);

        SpecialtyModel spec = new SpecialtyModel();
        spec.setSpecialtyId(0);
        spec.setSpecialtyName(getResources().getString(R.string.all));
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

    class DoctorResultViewAdapter extends ArrayAdapter<DoctorModel> {
        ViewHolder holder;

        ArrayList<DoctorModel> listData;

        public DoctorResultViewAdapter(ArrayList<DoctorModel> listData) {
            super(PatientSearchDoctorActivity.this,
                    R.layout.patient_search_doctor_items_result_layout, listData);
            this.listData = listData;
        }

        public ArrayList<DoctorModel> getListData() {
            return listData;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.patient_search_doctor_items_result_layout,
                        parent, false);
                holder = new ViewHolder();
                holder.tvDoctorName = (TextView) convertView
                        .findViewById(R.id.tv_doctor_search_result);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            DoctorModel doc = listData.get(position);

            holder.tvDoctorName.setText(doc.getDoctorName());

            return convertView;
        }
    }

    private class ViewHolder {
        TextView tvDoctorName;
    }
}
