package com.mntripclient;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

@SuppressLint("ParserError")
public class GPSOPointOverlay extends ItemizedOverlay<OverlayItem> {

	private ArrayList<OverlayItem> overlays = new ArrayList<OverlayItem>();
	private MapView mapView;
	
	public GPSOPointOverlay(Drawable defaultMarker, MapView mapView) {
		super(boundCenterBottom(defaultMarker));
		this.mapView = mapView;
		populate();
		mapView.invalidate();
	}

	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		if (!shadow) {
			super.draw(canvas, mapView, false);
		}
	}

	public void addOverlay(OverlayItem overlay) {
		overlays.add(overlay);
		populate();
		mapView.invalidate();
	}

	@Override
	protected OverlayItem createItem(int i) {		
		return overlays.get(i);
	}

	@Override
	public int size() {
		return overlays.size();
	}
	
	public void clear() {
		overlays.clear();
		populate();
		mapView.invalidate();
	}
	
	public void addUniqueOverlay(OverlayItem overlay) {
		overlays.clear();
		populate();
		overlays.add(overlay);
		populate();
		mapView.invalidate();
	}

}