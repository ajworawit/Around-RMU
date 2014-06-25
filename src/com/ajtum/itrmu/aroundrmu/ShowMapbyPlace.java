package com.ajtum.itrmu.aroundrmu;

import java.util.ArrayList;

import org.w3c.dom.Document;

import com.ajtum.database.DatabaseConfig;
import com.ajtum.database.DatabaseHelper;
import com.ajtum.database.DatabaseManager;
import com.ajtum.itrmu.aroundrmu.MapsLocation.Processing;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.internal.db;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.R.color;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Choreographer.FrameCallback;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ShowMapbyPlace extends FragmentActivity implements LocationSource,LocationListener,OnMyLocationChangeListener {

	GoogleMap myMap;
	GMapV2Direction md;
	
	LatLng fromPosition;
	LatLng position;
	LatLng toPosition = new LatLng(16.196769,103.272696);
	double lat,lng;
	int distance;
	String distanceText;
	
	LocationManager locationManager;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    private ProgressDialog pDialog;
    private String prefix_img="thum_img_";
	private OnLocationChangedListener mListener;
	private DatabaseConfig dbconfig;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map); 
        ActionBar actionBar = getActionBar();
		actionBar.setIcon(R.drawable.marker);
		
		if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
		
		md=new GMapV2Direction();
		cd = new ConnectionDetector(getApplicationContext());
		
		
		// check google play
		if(checkGooglePlayService()){
			// process 
			new Processing().execute();
		}else{
            showAlertDialog(this, "Check Google Play Service.","อุปกรณ์ไม่สนับสนุนแผนที่  กรุณาติดตั้ง Google Play Service !", false);
		}
		

	
	}// onCreate
	
	// 
	private void initil(){
		
	    Intent intent = getIntent();
	    String place_lat = intent.getStringExtra("place_lat");
	    String place_long = intent.getStringExtra("place_long");
	    String place_id = intent.getStringExtra("place_id");
	    String place_name = intent.getStringExtra("place_name");
	    
		lat=Double.parseDouble(place_lat);
		lng=Double.parseDouble(place_long);
	    
		position=new LatLng(lat, lng);
	    
		
		//Toast.makeText(getApplicationContext(), "name :"+place_name, Toast.LENGTH_SHORT).show();

	 	locationManager=(LocationManager)getSystemService(LOCATION_SERVICE);
		
	 	// get location current
	  	Criteria criteria = new Criteria();
	    criteria.setAccuracy(Criteria.ACCURACY_FINE);
	    criteria.setAltitudeRequired(false);
	    criteria.setBearingRequired(false);
	    criteria.setCostAllowed(true);
	    criteria.setPowerRequirement(Criteria.POWER_LOW);
        String provider = locationManager.getBestProvider(criteria, true);
        
        if(provider==null){
        	Toast.makeText(getApplicationContext(), "provider null", Toast.LENGTH_SHORT).show();
        }else{
            // Getting Current Location
            Location location = locationManager.getLastKnownLocation(provider);
            
            if (location == null){
            	Toast.makeText(getApplicationContext(), "ไม่พบข้อมูลตำแหน่งผู้ใช้งาน !", Toast.LENGTH_SHORT).show();
            }else{           
            	
            	isInternetPresent = cd.isConnectingToInternet();
            	// check internet 
            	if (isInternetPresent) {
	                String lat=Double.toString(location.getLatitude());
	                String lon=Double.toString(location.getLongitude());
	        		// get lat,long current
	        		fromPosition=new LatLng( location.getLatitude(), location.getLongitude());;
	    
	        		myMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
	        	 	myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 10));
	        		
	        		myMap.addMarker(new MarkerOptions().position(position)
	        				.title(place_name)
	        				.snippet(place_id)
	        				.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker2_2)));
	        	
	        		// round 
	        		Document doc = md.getDocument(fromPosition, position, GMapV2Direction.MODE_DRIVING);
	                
	        		// check connect google api
	        		if(doc==null){
	        			Toast.makeText(getApplicationContext(), "ไม่สามารถเชื่อมต่อ Google Map API !", Toast.LENGTH_SHORT).show();
	        		}else{
	        			
		        		ArrayList<LatLng> directionPoint = md.getDirection(doc);
		                PolylineOptions rectLine = new PolylineOptions().width(7).color(Color.RED);
		                
		                for(int i = 0 ; i < directionPoint.size() ; i++) {            
		                    rectLine.add(directionPoint.get(i));
		                }
		                
		                myMap.addPolyline(rectLine);
		                
		                distance=md.getDistanceValue(doc);
		                distanceText=md.getDistanceText(doc);
		                
		                Toast.makeText(getApplicationContext(), "ระยะทางประมาณ   "+distanceText, Toast.LENGTH_SHORT).show();
		                
		                // map info
		                mapinfo();
		                
	        		}// check connect google api

            	}else{
            		Toast.makeText(getApplicationContext(), "ไม่สามารถเชื่อมต่ออินเตอร์เน็ตได้ !", Toast.LENGTH_SHORT).show();
            	}// check internet
                
                
            }// check location
        
        }// check provider
        
	}
	
	
	// map info
	private void mapinfo(){
		// show info window on marker
		myMap.setInfoWindowAdapter(new InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {
               return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

            	  // Getting view from the layout file info_window_layout
                View v =getLayoutInflater().inflate(R.layout.infowindow, null);
 
                // Getting the position from the marker
                LatLng latLng = marker.getPosition();
                
                String title=marker.getTitle();
 
                // Getting reference to the TextView to set latitude
                TextView tile = (TextView) v.findViewById(R.id.tv_title);
                ImageView img_info = (ImageView) v.findViewById(R.id.img_map_info);
 
                // Setting the latitude
                tile.setText("" + title);
			      // set image detail
			      int resId = getResources().getIdentifier(dbconfig.PackageName+":drawable/" + prefix_img+marker.getSnippet(), null, 
			    		 null);
			      if(resId!=0){
			    	  img_info.setImageResource(resId);
			      }else{
			    	  img_info.setImageResource(R.drawable.build_logo_small);
			      }
			      
                return v;

            }

        });
	}
	
	
    private void initilizeMap() {
        if (myMap == null) {
            myMap = ((MapFragment)getFragmentManager().findFragmentById(
                    R.id.mapv2)).getMap();
        }
        
        if (myMap != null) 
        {
            setUpMap();
        }
        
        
    }

    
    private void setUpMap() 
    {
    	myMap.setMyLocationEnabled(true);
    	myMap.setOnMyLocationChangeListener(this);
    }

    // set map type
    private void mapType(){
    	
		ImageButton btnType1=(ImageButton)findViewById(R.id.btnType);

		btnType1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(myMap.getMapType()==1){
					myMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
				}else{
					myMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
				}
			}
		}); 
    }
    
    // check google play
    private boolean checkGooglePlayService(){
    	int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
    	if(status == ConnectionResult.SUCCESS) {
    	    return true;
    	}else{
    		return false;
    	}
    }
    
    
	// show message
	   public void showAlertDialog(Context context, String title, String message, Boolean status) {
	        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
	        alertDialog.setTitle(title);
	        alertDialog.setMessage(message);
	        alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);
	        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	            	// intnet update google play
	            	startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.google.android.gms")));

	            }
	        });
	        alertDialog.show();
	    }
	   
    
    @Override
   public void onMyLocationChange(Location lastKnownLocation) {
        CameraUpdate myLoc = CameraUpdateFactory.newCameraPosition(
                new CameraPosition.Builder().target(new LatLng(lastKnownLocation.getLatitude(),
                        lastKnownLocation.getLongitude())).zoom(15).build());
        myMap.moveCamera(myLoc);
        myMap.setOnMyLocationChangeListener(null);
    }
    
    @Override
    public void onPause()
    {
        if(locationManager != null)
        {
            locationManager.removeUpdates(this);
        }

        super.onPause();
    }
     
    @Override
    public void onResume()
    {
    	   super.onResume();

    	   initilizeMap();

    	    if(locationManager != null)
    	    {
    	        myMap.setMyLocationEnabled(true);
    	    }
    }
    
	@Override
	public void onLocationChanged(Location location) {

	    if( mListener != null )
	    {
	        mListener.onLocationChanged( location );

	        //Move the camera to the user's location once it's available!
	        myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()),10));
	    }
	    
		Toast.makeText(ShowMapbyPlace.this, "lat :"+location.getLatitude()+" long:"+location.getLongitude(), Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		 Toast.makeText(this, "provider disabled", Toast.LENGTH_SHORT).show();
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		 Toast.makeText(this, "provider enabled", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		 Toast.makeText(this, "status changed", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void activate(OnLocationChangedListener listener) {
		// TODO Auto-generated method stub
		mListener = listener;
	}

	@Override
	public void deactivate() {
		// TODO Auto-generated method stub
		mListener = null;
	}
    
    
	
	 
	//AsyncTask
class Processing extends AsyncTask<Context,Integer,String> {
		String s="";
      @Override
       protected void onPreExecute() {
           super.onPreExecute();
          
           pDialog = new ProgressDialog(ShowMapbyPlace.this);
           //pDialog.setTitle("Information");
           pDialog.setMessage("กำลังโหลด ...");
           pDialog.setIndeterminate(false);
           pDialog.setCancelable(false);
           pDialog.show();
       }     
      
       protected String doInBackground(Context... params ) { 
                 try {
					Thread.sleep(200);
					//initilizeMap();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                
                 return null;
       } 
       
       @Override
      protected void onProgressUpdate(Integer... values) {
    	  super.onProgressUpdate(values); 
      }   
       
       @Override
       protected void onPostExecute(String result) {
    	   super.onPostExecute(result);        	   
    	   runOnUiThread(new Runnable() {
               public void run() {
       			initilizeMap();
    			initil();
    			mapType();
               }
    	   });        	   
           pDialog.dismiss();    
       }  
       
       @Override
       protected void onCancelled()
       {
               super.onCancelled();
       }
       
    }//AsyncTask
    
    
    


}
