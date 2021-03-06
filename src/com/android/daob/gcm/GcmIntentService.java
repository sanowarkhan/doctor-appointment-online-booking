package com.android.daob.gcm;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.android.daob.activity.DoctorAppointmentDetailActivity;
import com.android.daob.activity.MainActivity;
import com.android.daob.activity.PatientAppointmentDetailActivity;
import com.android.doctor_appointment_online_booking.R;
import com.google.android.gms.gcm.GoogleCloudMessaging;

/**
 * This {@code IntentService} does the actual handling of the GCM message.
 * {@code GcmBroadcastReceiver} (a {@code WakefulBroadcastReceiver}) holds a
 * partial wake lock for this service while the service does its work. When the
 * service is finished, it calls {@code completeWakefulIntent()} to release the
 * wake lock.
 */
public class GcmIntentService extends IntentService {
	public static int NOTIFICATION_ID = 1;
	private NotificationManager mNotificationManager;
	NotificationCompat.Builder builder;
	private String appointmentId;
	private String role;

	public GcmIntentService() {
		super("GcmIntentService");
	}

	public static final String TAG = "GCM Demo";

	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		String messageType = gcm.getMessageType(intent);

		if (!extras.isEmpty()) { // has effect of unparcelling Bundle
			if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
					.equals(messageType)) {
				sendNotification("Send error: " + extras.toString());
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
					.equals(messageType)) {
				sendNotification("Deleted messages on server: "
						+ extras.toString());
				// If it's a regular GCM message, do some work.
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
					.equals(messageType)) {
				// This loop represents the service doing some work.
				// for (int i = 0; i < 3; i++) {
				// Log.i(TAG, "Working... " + (i + 1)
				// + "/3 @ " + SystemClock.elapsedRealtime());
				// try {
				// Thread.sleep(5000);
				// } catch (InterruptedException e) {
				// }
				// }
				Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());
				if (extras.getString("username").equals(MainActivity.username)) {
					appointmentId = extras.getString("id");
					role = extras.getString("role");
					sendNotification(extras.getString("message"));
				}
				Log.i(TAG, "id: " + appointmentId);
				Log.i(TAG, "Received: " + extras.toString());
			}
		}
		// Release the wake lock provided by the WakefulBroadcastReceiver.
		GcmBroadcastReceiver.completeWakefulIntent(intent);
	}

	// Put the message into a notification and post it.
	// This is just one simple example of what you might choose to do with
	// a GCM message.
	private void sendNotification(String msg) {
		mNotificationManager = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);

		// PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
		// new Intent(this, MainActivity.class), 0);

		// Intent resultIntent = new Intent(this,
		// PatientAppointmentDetailActivity.class);
		// TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		// stackBuilder.addParentStack(PatientAppointmentDetailActivity.class);
		// stackBuilder.addNextIntent(resultIntent);
		// // Gets a PendingIntent containing the entire back stack
		// PendingIntent resultPendingIntent =
		// stackBuilder.getPendingIntent(NOTIFICATION_ID,
		// PendingIntent.FLAG_UPDATE_CURRENT);

		Bundle bun = new Bundle();
		bun.putString("appointmentId", appointmentId);
		Log.i(TAG, "id: " + appointmentId);

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this).setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle("DAOB")
				.setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
				.setVibrate(new long[] { 1000, 1000, 1000 })
				.setContentText(msg);

		mBuilder.setContentIntent(getPendingIntent(bun, NOTIFICATION_ID, role));
		mBuilder.setAutoCancel(true);
		mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
		NOTIFICATION_ID++;
	}

	private PendingIntent getPendingIntent(Bundle bundle, int rc, String role) {
		Intent notificationIntent = new Intent();
		if (role.equals("patient")) {
			notificationIntent = new Intent(this,
					PatientAppointmentDetailActivity.class);
		} else {
			notificationIntent = new Intent(this,
					DoctorAppointmentDetailActivity.class);
		}

		notificationIntent.putExtras(bundle);
		return PendingIntent
				.getActivity(this, rc, notificationIntent,
						PendingIntent.FLAG_UPDATE_CURRENT
								| PendingIntent.FLAG_ONE_SHOT);
	}
}
