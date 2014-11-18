
package com.android.daob.activity;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.daob.application.AppController;
import com.android.daob.utils.Constants;
import com.android.daob.utils.SessionManager;
import com.android.doctor_appointment_online_booking.R;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

public class LoginActivity extends Activity implements OnClickListener {
    public static String TAG = LoginActivity.class.getSimpleName();

    private String url = Constants.URL + "login";

    private EditText txtUsername, txtPwd;

    private TextView lblStatusLogin;

    private Button btnLogin;

    private ProgressBar loginProgressBar;

    RelativeLayout rlLogin;

    boolean loginStatus = false;

    String role;
    
    String userId;
    
    String roleId; 

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_layout);
        setup();

    }

    private void setup() {
        sessionManager = new SessionManager(getApplicationContext());
        txtUsername = (EditText) findViewById(R.id.txtUsername);
        txtPwd = (EditText) findViewById(R.id.txtPwd);
        lblStatusLogin = (TextView) findViewById(R.id.lbl_status_login);
        btnLogin = (Button) findViewById(R.id.login_btn);
        loginProgressBar = (ProgressBar) findViewById(R.id.login_progress);
        rlLogin = (RelativeLayout) findViewById(R.id.rlLogin);
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        switch (arg0.getId()) {
            case R.id.login_btn:
                // Check if text controls are not empty
                InputMethodManager inputManager = (InputMethodManager) LoginActivity.this
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                if (txtUsername.getText().length() != 0 && txtUsername.getText().toString() != "") {
                    if (txtPwd.getText().length() != 0 && txtPwd.getText().toString() != "") {

                        lblStatusLogin.setText("");
                        checkLogin();
                    }
                    // If Password text control is empty
                    else {
                        lblStatusLogin.setText(getResources().getString(R.string.pwd_empty));
                    }
                    // If Username text control is empty
                } else {
                    lblStatusLogin.setText(getResources().getString(R.string.uname_empty));
                }

                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void checkLogin() {
        showDialog();
        String tag_json_login = "json_login_req";

        final String uName = txtUsername.getText().toString().trim();

        String pwd = txtPwd.getText().toString().trim();

        HashMap<String, String> loginParams = new HashMap<String, String>();
        loginParams.put("username", uName);
        loginParams.put("password", pwd);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Method.POST, url,
                new JSONObject(loginParams), new Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {               
                        	Log.i("aa", "" + response);
                            loginStatus = response.getBoolean("login");
                            role = response.getString("role");
                            userId = response.getString("userId");
                            roleId = response.getString("roleId");
                            if (loginStatus) {
                                sessionManager.createLoginSession(uName, role, userId, roleId);
                                Intent intObj = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intObj);
                            } else {
                                loginFail();
                            }
                        } catch (JSONException e) {
                            connectFail();
                            e.printStackTrace();
                        }
                        loginProgressBar.setVisibility(View.GONE);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.e(TAG, "Error: " + error.getMessage());
                        loginProgressBar.setVisibility(View.GONE);
                        connectFail();
                    }
                }) {

        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_login);
    }

    private void showDialog() {
        loginProgressBar.setVisibility(View.VISIBLE);
        rlLogin.setVisibility(View.GONE);
    }

    private void loginFail() {
        rlLogin.setVisibility(View.VISIBLE);
        lblStatusLogin.setText(getResources().getString(R.string.login_fail));
        txtPwd.setText("");
    }

    private void connectFail() {
        rlLogin.setVisibility(View.VISIBLE);
        lblStatusLogin.setText(getResources().getString(R.string.connect_fail));
    }
}
