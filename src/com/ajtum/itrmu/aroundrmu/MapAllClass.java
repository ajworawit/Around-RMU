package com.ajtum.itrmu.aroundrmu;

import java.util.ArrayList;

import org.w3c.dom.Document;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.LocationSource.OnLocationChangedListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MapAllClass extends FragmentActivity  {

	public static GoogleMap myMap;
	private LatLng point;
	private Activity activity;
	private Cursor cursor;
	
	private LatLng pointCenter=new LatLng(16.200771,103.270674);
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
	
	// Constructor
	public MapAllClass(Activity act,Cursor cs){
		activity=act;
		cursor=cs;
		
		if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
		
		cd = new ConnectionDetector(getApplication());
		initil();	
	}
	

	public void initil(){

		
		initilizeMap();

    		// add marker
    		while(cursor.moveToNext()){
    			 
    			String p_name=cursor.getString(1);
    			double p_lat=Double.parseDouble(cursor.getString(2));
    			double p_lng=Double.parseDouble(cursor.getString(3));
    			
    			point=new LatLng(p_lat, p_lng);
    			
    			myMap.addMarker(new MarkerOptions().position(point)
    					.title(p_name)
    					.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker2_2)));
    		} 
    		// center marker
    		myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pointCenter, 15));
    		// show info window on marker
    		myMap.setInfoWindowAdapter(new InfoWindowAdapter() {

                @Override
                public View getInfoWindow(Marker arg0) {
                   return null;
                }

                @Override
                public View getInfoContents(Marker marker) {

                	  // Getting view from the layout file info_window_layout
                    View v =activity.getLayoutInflater().inflate(R.layout.infowindow, null);
     
                    // Getting the position from the marker
                    LatLng latLng = marker.getPosition();
                    
                    String title=marker.getTitle();
     
                    // Getting reference to the TextView to set latitude
                    TextView tile = (TextView) v.findViewById(R.id.tv_title);
     
                    // Setting the latitude
                    tile.setText("" + title);
                    return v;

                }

            });
    		

    	

	}// initil
	
	// map
    private void initilizeMap() {
    	
        myMap = ((MapFragment)activity.getFragmentManager().findFragmentById(
                R.id.mapv2)).getMap();
        myMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        myMap.setMyLocationEnabled(true);
        myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pointCenter, 15));
        
            // check if map is created successfully or not
            if (myMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }        
        
    }


}





