package com.example.dailyselfie;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SelfieListAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<Selfie> selfiesTaken;
	private int thumbnailWidth;
	private int thumbnailHeight;

	public SelfieListAdapter(Context context, ArrayList<Selfie> selfiesTaken, int thumbnailWidth, int thumbnailHeight) {
		this.context = context;
		this.selfiesTaken = selfiesTaken;
		this.thumbnailWidth = thumbnailWidth;
		this.thumbnailHeight = thumbnailHeight;
	}

	@Override
	public int getCount() {
		return selfiesTaken.size();
	}

	@Override
	public Selfie getItem(int arg0) {
		return selfiesTaken.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.selfie_list_item, parent,
					false);
		}

		Selfie selfie = selfiesTaken.get(position);

		TextView fileName = (TextView) convertView.findViewById(R.id.file_name);
		fileName.setText(selfie.getReadableName());

		ImageView thumbnail = (ImageView) convertView
				.findViewById(R.id.thumbnail);
				
		
		thumbnail
				.setImageBitmap(getSelfieThumbnail(selfie.getPhoto()
						.getAbsolutePath(), thumbnailWidth, thumbnailHeight));

		return convertView;
	}

	private Bitmap getSelfieThumbnail(String path, int width, int height) {

		// Get the dimensions of the bitmap
		BitmapFactory.Options bmOptions = new BitmapFactory.Options();
		bmOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, bmOptions);
		int photoW = bmOptions.outWidth;
		int photoH = bmOptions.outHeight;

		// Determine how much to scale down the image
		int scaleFactor = Math.min(photoW / width, photoH / height);

		// Decode the image file into a Bitmap sized to fill the View
		bmOptions.inJustDecodeBounds = false;
		bmOptions.inSampleSize = scaleFactor;
		bmOptions.inPurgeable = true;

		Bitmap bitmap = BitmapFactory.decodeFile(path, bmOptions);
		return bitmap;
	}
}
