
package com.android.daob.component;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;

import com.android.doctor_appointment_online_booking.R;

public class CommonDialog extends Dialog implements android.view.View.OnClickListener {

    boolean isInit = false;

    public static boolean isShow = false;
    
    Object data;

    List<Object> items = new ArrayList<Object>();;

    public CommonDialog(Context context) {
        super(context, R.style.Dialog);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    protected void init() {
        // init dialog
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void setItems(ArrayList<Object> items) {
        this.items = items;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

    }

    public void setItems(List<Object> listTestModel) {
        // TODO Auto-generated method stub

    }

    @Override
    public void dismiss() {
        // TODO Auto-generated method stub
        super.dismiss();
        isShow = false;
    }

    @Override
    public void show() {        
        super.show();
        isShow = true;
    }

}
