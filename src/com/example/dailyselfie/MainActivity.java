package com.example.dailyselfie;

import java.io.File;
import java.util.ArrayList;

import android.app.AlarmManager;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends ListActivity {

	protected static final int REQUEST_CODE_ACTION_IMAGE_CAPTURE = 2;
	protected static final int REQUEST_CODE_MAIN_ACTIVITY = 1;
	private static final long TWO_MINUTES = 2 * 60 * 1000L;
	public static final String EXTRA_FILE_NAME = "file_name";
	public static final String EXTRA_READBALE_NAME = "readable_name";

	private Selfie selfie;
	private File photoDirectory;
	private ArrayList<Selfie> selfiesTaken;
	private SelfieListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/*
		 * setContentView(R.layout.activity_main); RelativeLayout container =
		 * (RelativeLayout)findViewById(R.id.container);
		 * container.addView(getListView());
		 */
		photoDirectory = getExternalFilesDir(null);
		selfiesTaken = getCurrentSelfies();

		int thumbnailWidth = getResources().getDimensionPixelSize(
				R.dimen.thumbnail_width);
		int thumbnailHeigth = getResources().getDimensionPixelSize(
				R.dimen.thumbnail_height);

		adapter = new SelfieListAdapter(this, selfiesTaken, thumbnailWidth,
				thumbnailHeigth);
		setListAdapter(adapter);

		getListView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(MainActivity.this,
						ViewSelfieActivity.class);
				intent.putExtra(EXTRA_FILE_NAME, adapter.getItem(arg2)
						.getPhoto().getName());
				intent.putExtra(EXTRA_READBALE_NAME, adapter.getItem(arg2)
						.getReadableName());
				startActivity(intent);

			}
		});

		// Set recurring Alarm
		Intent notificationReceiver = new Intent(this,
				AlarmBroadcastReceiver.class);
		PendingIntent pendingNotification = PendingIntent.getBroadcast(this,
				AlarmBroadcastReceiver.REQUEST_CODE_ALARM_NOTIFICATION,
				notificationReceiver, 0);

		AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,
				SystemClock.elapsedRealtime() + TWO_MINUTES, TWO_MINUTES,
				pendingNotification);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_camera) {
			Intent cameraIntent = new Intent();
			cameraIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
			if (cameraIntent.resolveActivity(getPackageManager()) != null) {
				selfie = new Selfie();
				if (selfie.generateFile(photoDirectory)) {
					if (selfie.getPhoto() != null) {
						cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
								Uri.fromFile(selfie.getPhoto()));
						startActivityForResult(cameraIntent,
								REQUEST_CODE_ACTION_IMAGE_CAPTURE);
						return true;
					}
				}
			}
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE_ACTION_IMAGE_CAPTURE
				&& resultCode == RESULT_OK) {

			// Geth thumbnail when the original intent to start the camera does
			// not provides a file to write the photo to.
			/*
			 * Bitmap thumbnail = (Bitmap)(data.getExtras().get("data"));
			 * ImageView thumbView = (ImageView)findViewById(R.id.thumbnail);
			 * thumbView.setImageBitmap(thumbnail);
			 */

			selfiesTaken.add(selfie);
			adapter.notifyDataSetChanged();
		} else if(resultCode == RESULT_CANCELED) {
			selfie.getPhoto().delete();
			selfie = null;
		}
	}

	public ArrayList<Selfie> getCurrentSelfies() {
		selfiesTaken = new ArrayList<Selfie>();
		File[] filesInParent = photoDirectory.listFiles();
		if (filesInParent != null) {
			String fileName;
			for (File tempFile : filesInParent) {
				fileName = tempFile.getName();
				if (fileName.startsWith(Selfie.PREFIX_PHOTO)) {
					selfiesTaken.add(new Selfie(tempFile));
				}
			}
		}
		return selfiesTaken;
	}
}
