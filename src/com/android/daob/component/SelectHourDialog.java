
package com.android.daob.component;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.daob.activity.PatientBookingStep1Activity;
import com.android.daob.model.DoctorFreeTimeModel;
import com.android.daob.utils.Constants;
import com.android.daob.utils.GlobalUtils;
import com.android.doctor_appointment_online_booking.R;

public class SelectHourDialog extends CommonDialog {

    Context context;

    ListView lvDoctorFreeTime;

    TextView tvNoResult;

    ArrayAdapter<Object> listDoctorFreeTimeApdater;

    public SelectHourDialog(Context context) {
        super(context);
        this.context = context;
        setContentView(R.layout.patient_view_doctor_free_time_layout);
        setCanceledOnTouchOutside(true);
        init();
    }

    @Override
    protected void init() {
        if (!isInit) {
            isInit = true;

            lvDoctorFreeTime = (ListView) findViewById(R.id.lvDoctorFreeTime);

            tvNoResult = (TextView) findViewById(R.id.tv_no_result_free_time);

            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.width = (int) (GlobalUtils.getDisplayWidth() - getContext().getResources()
                    .getDimension(R.dimen.margin_dialog_vertical) * 2);
            lp.height = (int) (GlobalUtils.getDisplayHeight() - getContext().getResources()
                    .getDimension(R.dimen.margin_dialog_vertical) * 2);
        }
    }

    @Override
    public void setItems(ArrayList<Object> listFreeTime) {
        items = listFreeTime;
        if (listFreeTime != null && listFreeTime.size() > 0) {
            tvNoResult.setVisibility(View.GONE);
            lvDoctorFreeTime.setVisibility(View.VISIBLE);
            listDoctorFreeTimeApdater = new DoctorFreeTimeViewAdapter(listFreeTime);

            lvDoctorFreeTime.setAdapter(listDoctorFreeTimeApdater);
            listDoctorFreeTimeApdater.notifyDataSetChanged();
            
            lvDoctorFreeTime.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    DoctorFreeTimeModel dft = (DoctorFreeTimeModel) parent.getItemAtPosition(position);
                    Bundle bun = new Bundle();
                    bun.putSerializable(Constants.DATA_KEY, dft);
                    Intent i = new Intent(context, PatientBookingStep1Activity.class);
                    i.putExtras(bun);
                    context.startActivity(i);
                    cancel();
                }
            });
        } else {
            tvNoResult.setVisibility(View.VISIBLE);
            lvDoctorFreeTime.setVisibility(View.GONE);
        }
    }

    class DoctorFreeTimeViewAdapter extends ArrayAdapter<Object> {
        ViewHolder holder;

        ArrayList<Object> listData;

        public DoctorFreeTimeViewAdapter(ArrayList<Object> listFreeTime) {
            super(context, R.layout.patient_view_doctor_free_time_item_layout, listFreeTime);
            this.listData = listFreeTime;
        }

        public ArrayList<Object> getListData() {
            return listData;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.patient_view_doctor_free_time_item_layout,
                        parent, false);
                holder = new ViewHolder();
                holder.tvLocation = (TextView) convertView.findViewById(R.id.tv_free_location_name);
                holder.tvStartTime = (TextView) convertView.findViewById(R.id.tv_free_start_time);
                holder.tvEndTime = (TextView) convertView.findViewById(R.id.tv_free_end_time);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            DoctorFreeTimeModel dft = (DoctorFreeTimeModel) listData.get(position);

            holder.tvLocation.setText(dft.getLocation());
            holder.tvStartTime.setText(dft.getStartTime());
            holder.tvEndTime.setText(dft.getEndTime());
            return convertView;
        }
    }

    private class ViewHolder {
        TextView tvLocation;
        TextView tvStartTime;
        TextView tvEndTime;
    }
}
