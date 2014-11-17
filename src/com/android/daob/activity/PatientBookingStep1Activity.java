/**
 * 
 */

package com.android.daob.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.daob.model.DoctorFreeTimeModel;
import com.android.daob.utils.Constants;
import com.android.doctor_appointment_online_booking.R;

public class PatientBookingStep1Activity extends BaseActivity implements OnClickListener {

    public static String TAG = PatientBookingStep1Activity.class.getSimpleName();

    RadioButton rbForMe, rbForOther, rbMale, rbFemale;

    Button btnBooking;

    LinearLayout llForOther;

    EditText edName, edOld, edPhone, edDes;

    TextView tvStartTime, tvLocation;

    boolean isDelegated = false;

    boolean gender = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_booking_dtep1_layout);
        init(getIntent().getExtras());
    }

    void init(Bundle bun) {
        DoctorFreeTimeModel dft = (DoctorFreeTimeModel) bun.get(Constants.DATA_KEY);
        rbForMe = (RadioButton) findViewById(R.id.rb_for_me);
        rbForOther = (RadioButton) findViewById(R.id.rb_for_other);
        rbMale = (RadioButton) findViewById(R.id.rb_male);
        rbFemale = (RadioButton) findViewById(R.id.rb_female);

        llForOther = (LinearLayout) findViewById(R.id.ll_booking_for_other);
        edName = (EditText) findViewById(R.id.ed_booking_name);
        edOld = (EditText) findViewById(R.id.ed_booking_old);
        edPhone = (EditText) findViewById(R.id.ed_booking_phone);
        edDes = (EditText) findViewById(R.id.edDescription);

        tvStartTime = (TextView) findViewById(R.id.tv_start_time);
        tvStartTime.setText(dft.getStartTime());
        tvLocation = (TextView) findViewById(R.id.tv_location_name);
        tvLocation.setText(dft.getLocation());

        btnBooking = (Button) findViewById(R.id.btn_booking);
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

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.rb_for_me:
                if (checked) {
                    llForOther.setVisibility(View.GONE);
                    isDelegated = false;
                }
                break;
            case R.id.rb_for_other:
                if (checked) {
                    llForOther.setVisibility(View.VISIBLE);
                    edName.requestFocus();
                    isDelegated = true;
                }
            case R.id.rb_male:
                if (checked) {
                    gender = false;
                }
                break;
            case R.id.rb_female:
                if (checked) {
                    gender = true;
                }
                break;
        }
    }

    @Override
    public void onClick(View arg0) {

    }

}
