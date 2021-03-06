package com.mntripclient;

import java.util.concurrent.ArrayBlockingQueue;

import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

public class CoordinatesTask implements Runnable {

	private ArrayBlockingQueue<byte[]> bluetoothCoordinatesQueue;
	private GPSOPointOverlay gpsBluetoothOverlay;
	private GPSOPointOverlay dgpsBluetoothOverlay;
	private GPSOPointOverlay dgpsBluetoothBufferedOverlay;

	private GeoPoint point;

	private StringBuffer coordinate;

	public CoordinatesTask(ArrayBlockingQueue<byte[]> bluetoothCoordinatesQueue, GPSOPointOverlay gpsBluetoothOverlay, GPSOPointOverlay dgpsBluetoothOverlay,
			GPSOPointOverlay dgpsBluetoothBufferedOverlay) {

		this.bluetoothCoordinatesQueue = bluetoothCoordinatesQueue;
		this.gpsBluetoothOverlay = gpsBluetoothOverlay;
		this.dgpsBluetoothBufferedOverlay = dgpsBluetoothBufferedOverlay;
		this.dgpsBluetoothOverlay = dgpsBluetoothOverlay;

		coordinate = new StringBuffer();

		// TODO Auto-generated constructor stub
	}

	public void run() {
		while (true) {
			try {
				parse(bluetoothCoordinatesQueue.take());
			} catch (InterruptedException e) {
				Log.e("mNTRIPClient", e.getMessage());
			}
		}
	}

	private void parse(byte[] data) {
		coordinate.append(new String(data));
		Log.e("mNTRIPClient", new String(data));

		int pos = coordinate.indexOf("\r\n");

		if (pos > 0) {
			String[] components = coordinate.substring(0, pos).split(" ");
			coordinate.delete(0, pos + 2);

			if (components.length == 4) {
				if ((components[0].compareTo("BUFF") == 0) || (components[0].compareTo("GPSB") == 0) || (components[0].compareTo("DGPS") == 0)) {

					int time = Integer.parseInt(components[1]);
					double lat = Double.parseDouble(components[2]);
					double lon = Double.parseDouble(components[3]);
					point = new GeoPoint((int) (lat * 1000000.0), (int) (lon * 1000000.0));
					
					if ((components[0].compareTo("BUFF") == 0)) {
						dgpsBluetoothBufferedOverlay.addUniqueOverlay(new OverlayItem(point, components[0], ""));
					}
					if ((components[0].compareTo("GPSB") == 0)) {
						gpsBluetoothOverlay.addUniqueOverlay(new OverlayItem(point, components[0], ""));
					} 
					if ((components[0].compareTo("DGPS") == 0)) {
						dgpsBluetoothOverlay.addUniqueOverlay(new OverlayItem(point, components[0], ""));
					} 
					
					FileLog.getInstance().log(components[0] + " " + time + " " + lat + " " + lon + "\r\n");
				}

			}
		}

	}

}
