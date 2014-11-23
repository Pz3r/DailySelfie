package com.example.dailyselfie;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Selfie {
	
	public static final String PREFIX_PHOTO = "PHOTO_";
	public static final String EXTENSION_JPEG = ".jpg";	

	private File photo;
	private File parentDir;
	private String readableName;

	public Selfie() {
		readableName = "";
	}
	
	public Selfie(File photo) {
		this.photo = photo;
		String[] temp = photo.getName().split("_");
		readableName = temp[1] + "_" + temp[2].substring(0, 6);
	}
	
	public boolean generateFile(File parentDir) {			
		String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());	
		try {			
			photo =  File.createTempFile(PREFIX_PHOTO + timeStamp, EXTENSION_JPEG, parentDir);
			readableName = timeStamp;
			return true;
		} catch (IOException e) {			
			e.printStackTrace();
			return false;
		}
	}
	
	public File getPhoto() {
		return photo;
	}

	public void setPhoto(File photo) {
		this.photo = photo;
	}

	public File getParentDir() {
		return parentDir;
	}

	public void setParentDir(File parentDir) {
		this.parentDir = parentDir;
	}

	public String getReadableName() {
		return readableName;
	}

	public void setReadableName(String readableName) {
		this.readableName = readableName;
	}
}
