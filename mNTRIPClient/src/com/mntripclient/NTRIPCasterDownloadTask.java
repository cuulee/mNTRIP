package com.mntripclient;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;

import android.R.string;
import android.net.http.AndroidHttpClient;
import android.util.Log;

public class NTRIPCasterDownloadTask implements Runnable {

	private static String MOUNTPOINT = "http://mntripcaster.appspot.com/mntripstr0";
	
	private static String basicAuthString = "bU5UUklQVXNlcjptTlRSSVBQYXNzd29yZA==";
	
	private final ExecutorService httpExecutorService;
	private AndroidHttpClient httpClient;
	private URI uri;

	private HttpGet request;
	private HttpResponse response;
	
	private byte [] content;
	
	public NTRIPCasterDownloadTask(ExecutorService httpExecutorService) {
		this.httpExecutorService = httpExecutorService;
		httpClient = AndroidHttpClient.newInstance("mNTRIPCasterDownload");
		
		content = new byte[1000];
		
		try {
			uri = new URI(MOUNTPOINT);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		request = new HttpGet();
		request.setURI(uri);
		request.addHeader("Authorization", "Basic " + basicAuthString);
		request.addHeader("Ntrip-Version", "Ntrip/2.0");
		
	}
	
	public void run() {
		try {
			response = httpClient.execute(request);
			content = EntityUtils.toByteArray(response.getEntity());
			Log.d("NTRIPCasterDownloadTask", new String(content));
		} catch (Exception e) {
			e.printStackTrace();
		}
			
		Future<?> future = httpExecutorService.submit(this);
	}

}
