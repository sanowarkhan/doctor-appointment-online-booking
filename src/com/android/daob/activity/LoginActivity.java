
package com.android.daob.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.daob.utils.SessionManager;
import com.android.doctor_appointment_online_booking.R;

public class LoginActivity extends Activity implements OnClickListener {

    private EditText txtUsername, txtPwd;

    private TextView lblStatusLogin;

    private Button btnLogin;

    private ProgressBar loginProgressBar;

    private String url;

    private ImageView imgLogo;

    boolean loginStatus = false;

    boolean error = false;

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
        imgLogo = (ImageView) findViewById(R.id.img_Logo);
        // imgUname = (ImageView) findViewById(R.id.img_Username);
        // imgPwd = (ImageView) findViewById(R.id.imgPwd);
        // url = this.getString(R.string.url_server) + "api/accountapi/login/";

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
                        AsyncLogin task = new AsyncLogin();
                        // Call execute
                        task.execute();
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

    private class AsyncLogin extends AsyncTask {
        ProgressDialog loginDialog = new ProgressDialog(LoginActivity.this);

        String uName = txtUsername.getText().toString().trim();

        String pwd = txtPwd.getText().toString().trim();

        String role;

        @Override
        protected Object doInBackground(Object... params) {
            // HttpClient client = new DefaultHttpClient();
            // HttpResponse response;

            try {
                // HttpGet get = new HttpGet(url + "?username=" + uName +
                // "&password=" + pwd);
                // response = client.execute(get);
                // if (response != null) {
                // InputStream is = response.getEntity().getContent();
                // String stringRespone = JsonUtil.convertStreamToString(is);
                // is.close();

                // loginStatus = Boolean.parseBoolean(stringRespone.toString());
                if (uName.equals("patient") && pwd.equals("1234")) {
                    role = "0";
                    loginStatus = true;
                } else if (uName.equals("doctor") && pwd.equals("1234")) {
                    role = "1";
                    loginStatus = true;
                }
            } catch (Exception exp) {
                exp.printStackTrace();
                error = true;
            }
            return null;
        }

        /*
         * (non-Javadoc)
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            loginDialog.dismiss();
            if (!error) {
                if (loginStatus) {
                    // User.setUsername(uName);
                    // dbAdapter.open();
                    // dbAdapter.insertUser(uName, pwd);
                    // dbAdapter.close();
                    // getUserData();

                    sessionManager.createLoginSession(uName, role);
                    Intent intObj = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intObj);
                } else {
                    loginProgressBar.setVisibility(View.INVISIBLE);
                    txtUsername.setVisibility(View.VISIBLE);
                    txtPwd.setVisibility(View.VISIBLE);
                    btnLogin.setVisibility(View.VISIBLE);
                    imgLogo.setVisibility(View.VISIBLE);
                    lblStatusLogin.setText(getResources().getString(R.string.login_fail));
                    txtPwd.setText("");
                }
            } else { // connect fail
                loginProgressBar.setVisibility(View.INVISIBLE);
                txtUsername.setVisibility(View.VISIBLE);
                txtPwd.setVisibility(View.VISIBLE);
                btnLogin.setVisibility(View.VISIBLE);
                imgLogo.setVisibility(View.VISIBLE);
                lblStatusLogin.setText(getResources().getString(R.string.connect_fail));
            }

        }

        /*
         * (non-Javadoc)
         * @see android.os.AsyncTask#onPreExecute()
         */
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            loginDialog.setMessage(getResources().getString(R.string.login_process));
            loginDialog.setCanceledOnTouchOutside(false);
            loginDialog.show();
            // loginProgressBar.setVisibility(View.VISIBLE);
            txtUsername.setVisibility(View.INVISIBLE);
            txtPwd.setVisibility(View.INVISIBLE);
            btnLogin.setVisibility(View.INVISIBLE);
            imgLogo.setVisibility(View.INVISIBLE);

            // lblStatusLogin.setVisibility(View.INVISIBLE);
        }

        /*
         * (non-Javadoc)
         * @see android.os.AsyncTask#onProgressUpdate(Progress[])
         */
        @Override
        protected void onProgressUpdate(Object... values) {
            // TODO Auto-generated method stub
        }

    }
}
