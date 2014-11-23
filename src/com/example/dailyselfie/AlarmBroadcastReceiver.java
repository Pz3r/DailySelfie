package com.example.dailyselfie;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmBroadcastReceiver extends BroadcastReceiver {

	private static final int NOTIFICATION_ID = 1;
	protected static final int REQUEST_CODE_ALARM_NOTIFICATION = 3;

	private Intent mainActivityIntent;
	private PendingIntent wrapperIntent;

	@Override
	public void onReceive(Context context, Intent intent) {
		mainActivityIntent = new Intent(context, MainActivity.class);
		wrapperIntent = PendingIntent.getActivity(context, MainActivity.REQUEST_CODE_MAIN_ACTIVITY,
				mainActivityIntent, Intent.FLAG_ACTIVITY_NEW_TASK);

		String tickerText = context.getResources().getString(
				R.string.ticker_text);

		String title = context.getResources().getString(R.string.app_name);

		// Build notification
		Notification.Builder notificationBuilder = new Notification.Builder(
				context).setTicker(tickerText)
				.setSmallIcon(R.drawable.ic_action_camera).setAutoCancel(true)
				.setContentTitle(title).setContentText(tickerText)
				.setContentIntent(wrapperIntent);

		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		notificationManager
				.notify(NOTIFICATION_ID, notificationBuilder.build());
	}

}
