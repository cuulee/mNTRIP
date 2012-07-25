package com.mntripclient;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;

import android.os.Environment;
import android.util.Log;

public final class FileLog {
	private final static FileLog instance = new FileLog();
	private static FileOutputStream fileOutputStream;
	
    public static FileLog getInstance() {
        return instance;
    }
	
    synchronized void log(String s) {
		try {
			fileOutputStream.write(s.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private FileLog() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMHHmm");
		String fname = Environment.getExternalStorageDirectory().getAbsolutePath() + "/mNTRIPClientLog_" + dateFormat.format(new Date(System.currentTimeMillis())) + ".txt";

		try {

			Log.e("asdasd", fname);
			fileOutputStream = new FileOutputStream(fname);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}