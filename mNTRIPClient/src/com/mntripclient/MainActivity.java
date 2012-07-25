package com.mntripclient;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;

public class MainActivity extends MapActivity {

	private GPSOPointOverlay gpsPhoneOverlay;
	private GPSOPointOverlay gpsBluetoothOverlay;
	private GPSOPointOverlay dgpsBluetoothOverlay;
	private GPSOPointOverlay dgpsBluetoothBufferedOverlay;
	private GPSRouteOverlay gpsRouteOverlay;
	
	private LocationManager locationManager;
	private ExecutorService httpExecutorService;

	private List<Overlay> mapOverlays;
	private MapView mapView;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapview);
        
        prepareMapView();
        prepareOverlays();
        addOverlays();

        subcribeLocationManager();
        initNTRIPCasterDownloadTask();
        
    }

	@Override
	protected void onDestroy() {
		super.onDestroy();	
		httpExecutorService.shutdown();
	}
    
    private void prepareMapView() {
        mapView = (MapView) findViewById(R.id.mapView);
        mapView.setSatellite(true);
        mapView.setBuiltInZoomControls(true);
	}

	private void prepareOverlays() {
        mapOverlays = mapView.getOverlays();
        gpsPhoneOverlay = new GPSOPointOverlay(this.getResources().getDrawable(R.drawable.redpoint), mapView);
        gpsBluetoothOverlay = new GPSOPointOverlay(this.getResources().getDrawable(R.drawable.greenpoint), mapView);
        dgpsBluetoothOverlay = new GPSOPointOverlay(this.getResources().getDrawable(R.drawable.bluepoint), mapView);
        dgpsBluetoothBufferedOverlay = new GPSOPointOverlay(this.getResources().getDrawable(R.drawable.yellowpoint), mapView);
        gpsRouteOverlay = new GPSRouteOverlay(mapView);
	}

	private void subcribeLocationManager() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new GPSPhoneListener(gpsPhoneOverlay));
	}

	private void addOverlays() {
        mapOverlays.add(gpsRouteOverlay);
        mapOverlays.add(gpsPhoneOverlay);
        mapOverlays.add(gpsBluetoothOverlay);
        mapOverlays.add(dgpsBluetoothOverlay);
        mapOverlays.add(dgpsBluetoothBufferedOverlay);
        
        gpsRouteOverlay.readPointsFromFile("mNTRIPClientRoute.txt");
	}

    private void initNTRIPCasterDownloadTask() {
    	httpExecutorService = Executors.newSingleThreadExecutor();
    	httpExecutorService.execute(new NTRIPCasterDownloadTask(httpExecutorService));
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mapview, menu);
        return true;
    }

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
}
