package com.mntripclient;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.concurrent.ArrayBlockingQueue;

import android.os.Environment;

public final class FileLog implements Runnable {
	private final static FileLog instance = new FileLog();
	private static FileOutputStream fileOutputStream;
	
	
	private ArrayBlockingQueue<String> queue;
	
    public static FileLog getInstance() {
        return instance;
    }
	
    synchronized void log(String s) {
		queue.add(s);
	}
	
	private FileLog() {
	}

	public void run() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMHHmm");
		String fname = Environment.getExternalStorageDirectory().getAbsolutePath() + "/mNTRIPClientLog_" + dateFormat.format(new Date(System.currentTimeMillis())) + ".txt";

		try {
			fileOutputStream = new FileOutputStream(fname);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		queue = new ArrayBlockingQueue<String>(10);
		
		while (true) {
			try {
				fileOutputStream.write(queue.take().getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
}
