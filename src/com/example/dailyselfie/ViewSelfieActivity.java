package com.example.dailyselfie;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

public class ViewSelfieActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_selfie);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		Intent intent = getIntent();
		String readableName = intent.getExtras().getString(
				MainActivity.EXTRA_READBALE_NAME);
		setTitle(readableName);
		String filename = intent.getExtras().getString(
				MainActivity.EXTRA_FILE_NAME);
		
		File file = new File(getExternalFilesDir(null).getAbsolutePath(),
				filename);
		Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

		ImageView selfieView = (ImageView) findViewById(R.id.selfie_image_view);
		selfieView.setImageBitmap(bitmap);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_selfie, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.

		return super.onOptionsItemSelected(item);
	}
}
