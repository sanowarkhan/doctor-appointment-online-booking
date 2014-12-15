package com.android.daob.activity;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.json.JSONObject;

import com.android.daob.application.AppController;
import com.android.daob.gcm.GcmBroadcastReceiver;
import com.android.daob.utils.Constants;
import com.android.daob.utils.SessionManager;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends Activity {

	SessionManager sessionManager;

	public static String username;

	public static String role;

	public static String patientId;

	private String url = Constants.URL + "receiveUserRegId";

	public static final String EXTRA_MESSAGE = "message";
	public static final String PROPERTY_REG_ID = "registration_id";
	private static final String PROPERTY_APP_VERSION = "appVersion";
	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	String SENDER_ID = "269785447514";
	static final String TAG = "GCMDemo";
	TextView mDisplay;
	GoogleCloudMessaging gcm;
	AtomicInteger msgId = new AtomicInteger();
	SharedPreferences prefs;
	Context context;

	String regid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		context = getApplicationContext();

		// Check device for Play Services APK.
		if (checkPlayServices()) {
			gcm = GoogleCloudMessaging.getInstance(this);
			regid = getRegistrationId(context);

			if (regid.isEmpty()) {
				registerInBackground();
			} else {
				Log.i(TAG, "No valid Google Play Services APK found.");
			}
		}
		sessionManager = new SessionManager(getApplicationContext());
		sessionManager.checkLogin();
		HashMap<String, String> user = sessionManager.getUserDetails();
		username = user.get(SessionManager.KEY_NAME);
		role = user.get(SessionManager.KEY_ROLE);
		
		if (role.isEmpty()) {
			return;
		} else if (role.equals("patient")) {
			patientId = user.get(SessionManager.KEY_ROLEID);
		} else if (role.equals("doctor")){ 
			patientId = user.get(SessionManager.KEY_ROLEID);
		}

		String role = user.get(SessionManager.KEY_ROLE);
		if (role != null) {
			Intent i;
			if (role.equalsIgnoreCase(Constants.ROLE_PATIENT)) { // redirect to
																	// patient
																	// activity
				i = new Intent(MainActivity.this, PatientHomeActivity.class);
			} else if (role.equalsIgnoreCase(Constants.ROLE_DOCTOR)) { // redirect
																		// to
																		// doctor
				// activity
				i = new Intent(MainActivity.this, DoctorHomeActivity.class);
			} else { // redirect to doctor login activity
				i = new Intent(MainActivity.this, LoginActivity.class);
			}
			startActivity(i);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		checkPlayServices();
	}

	private String getRegistrationId(Context context) {
		final SharedPreferences prefs = getGCMPreferences(context);
		String registrationId = prefs.getString(PROPERTY_REG_ID, "");
		if (registrationId.isEmpty()) {
			Log.i(TAG, "Registration not found.");
			return "";
		}
		// Check if app was updated; if so, it must clear the registration ID
		// since the existing regID is not guaranteed to work with the new
		// app version.
		int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION,
				Integer.MIN_VALUE);
		int currentVersion = getAppVersion(context);
		if (registeredVersion != currentVersion) {
			Log.i(TAG, "App version changed.");
			return "";
		}
		return registrationId;
	}

	private SharedPreferences getGCMPreferences(Context context) {
		// This sample app persists the registration ID in shared preferences,
		// but
		// how you store the regID in your app is up to you.
		return getSharedPreferences(MainActivity.class.getSimpleName(),
				Context.MODE_PRIVATE);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private static int getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			// should never happen
			throw new RuntimeException("Could not get package name: " + e);
		}
	}

	private void registerInBackground() {
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
				try {
					if (gcm == null) {
						gcm = GoogleCloudMessaging.getInstance(context);
					}
					regid = gcm.register(SENDER_ID);
					msg = "Device registered, registration ID=" + regid;

					// You should send the registration ID to your server over
					// HTTP, so it
					// can use GCM/HTTP or CCS to send messages to your app.
					sendRegistrationIdToBackend(regid);

					// For this demo: we don't need to send it because the
					// device will send
					// upstream messages to a server that echo back the message
					// using the
					// 'from' address in the message.

					// Persist the regID - no need to register again.
					storeRegistrationId(context, regid);
				} catch (IOException ex) {
					msg = "Error :" + ex.getMessage();
					// If there is an error, don't just keep trying to register.
					// Require the user to click a button again, or perform
					// exponential back-off.
				}
				return msg;
			}

			@Override
			protected void onPostExecute(String msg) {
				mDisplay.append(msg + "\n");
			}
		}.execute(null, null, null);
	}

	private void sendRegistrationIdToBackend(String regid) {
		// Your implementation here.
		HashMap<String, String> regId = new HashMap<String, String>();
		regId.put("regId", regid);
		regId.put("username", MainActivity.username);
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
				Method.POST, url, new JSONObject(regId),
				new Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						VolleyLog.e("sendRegRespone", "sendRegRespone: "
								+ response);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						VolleyLog.e(TAG, "Error: " + error.getMessage());
					}
				}) {
		};
		AppController.getInstance().addToRequestQueue(jsonObjectRequest,
				"sendRegIdtoServer");
	}

	private void storeRegistrationId(Context context, String regId) {
		final SharedPreferences prefs = getGCMPreferences(context);
		int appVersion = getAppVersion(context);
		Log.i(TAG, "Saving regId on app version " + appVersion);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(PROPERTY_REG_ID, regId);
		editor.putInt(PROPERTY_APP_VERSION, appVersion);
		editor.commit();
		Log.i("regId:", regId);
	}

	private boolean checkPlayServices() {
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, this,
						PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {
				Log.i(TAG, "This device is not supported.");
				finish();
			}
			return false;
		}
		return true;
	}

}
