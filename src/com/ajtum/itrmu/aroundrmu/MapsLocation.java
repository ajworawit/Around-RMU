package com.ajtum.itrmu.aroundrmu;

import com.ajtum.database.DatabaseConfig;
import com.ajtum.database.DatabaseHelper;
import com.ajtum.database.DatabaseManager;
import com.ajtum.itrmu.aroundrmu.MainActivity.Processing;
import com.ajtum.tabadaper.TabsPagerAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MapsLocation extends Activity {

	public static GoogleMap myMap;
	private LatLng point;
	private Cursor cursor;
	
	private LatLng pointCenter=new LatLng(16.200771,103.270674);
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    
	public static final int DIALOG_DOWNLOAD_XML_PROGRESS = 0;
	private ProgressDialog mProgressDialog;
	private Activity activity;

	private SQLiteDatabase db;
	private DatabaseConfig dbconfig;
	private DatabaseHelper dbHelper;
	private DatabaseManager manage;
	private ImageButton btnMapType;
	private ProgressDialog pDialog;
	
	private String prefix_img="thum_img_";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);

		setContentView(R.layout.map);
        ActionBar actionBar = getActionBar();
		actionBar.setIcon(R.drawable.marker);
        
	    if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
	    
	    	    
		setProgressBarIndeterminateVisibility(true); 

		// get place db
	      dbHelper=new DatabaseHelper(this,SQLiteDatabase.OPEN_READWRITE);
	      dbHelper.createDatabase();
	      db =  dbHelper.openDatabase();


		cd = new ConnectionDetector(getApplication());
		
		// check google play
		if(checkGooglePlayService()){
			new Processing().execute();
		}else{
            showAlertDialog(this, "Check Google Play Service.","อุปกรณ์ไม่สนับสนุนแผนที่  กรุณาติดตั้ง Google Play Service !", false);
		}
		


		
	}
	
	public void initil(){

		isInternetPresent = cd.isConnectingToInternet();
		
		if (isInternetPresent) {
			

    		// add marker
    		while(cursor.moveToNext()){
    			
    			//Toast.makeText(getApplicationContext(), ""+i, Toast.LENGTH_SHORT).show();
    			String p_name=cursor.getString(1);
    			double p_lat=Double.parseDouble(cursor.getString(2));
    			double p_lng=Double.parseDouble(cursor.getString(3));
    			
    			point=new LatLng(p_lat, p_lng);
    			
    			myMap.addMarker(new MarkerOptions().position(point)
    					.title(p_name)
    					.snippet(cursor.getString(0))
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

		}else{
			Toast.makeText(getApplicationContext(), "ไม่สามารถเชื่อมต่ออินเตอร์เน็ตได้ !", Toast.LENGTH_SHORT).show();
		}

    		

    	

	}// initil
	
	// map
    private void initilizeMap() {
    	
        myMap = ((MapFragment)getFragmentManager().findFragmentById(
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
    
    private void mapType(){

	    
		btnMapType=(ImageButton)findViewById(R.id.btnType);

		
    	btnMapType.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) { 
				// TODO Auto-generated method stub
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
    
    
	private void setTitleBar(){
	    final boolean customTitleSupported = requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);

	    setContentView(R.layout.map);

	    if ( customTitleSupported ) {
	        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);
	    }

	    final TextView myTitleText = (TextView) findViewById(R.id.myTitle);
	    if ( myTitleText != null ) {
	        myTitleText.setText("========= NEW TITLE ==========");
	        myTitleText.setBackgroundColor(Color.GREEN);
	    }

	}
	
	 @Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		    super.onDestroy();
		    if (cursor!=null){
		    	cursor.close();
		    }
		    if (dbHelper != null) {
		        dbHelper.close();
		    }
	}
	
	 @Override
	public void onPause() {
	   super.onPause();
	    if (dbHelper != null) {
	        dbHelper.close();
	    }
	 }
    
	 
	 
		//AsyncTask
	class Processing extends AsyncTask<Context,Integer,String> {
			String s="";
	      @Override
	       protected void onPreExecute() {
	           super.onPreExecute();
	          
	           pDialog = new ProgressDialog(MapsLocation.this);
	           //pDialog.setTitle("Information");
	           pDialog.setMessage("กำลังโหลด ...");
	           pDialog.setIndeterminate(false);
	           pDialog.setCancelable(false);
	           pDialog.show();
	       }     
	      
	       protected String doInBackground(Context... params ) { 
	                 try {
						Thread.sleep(200);
					      cursor=DatabaseManager.getAllInTable(db, dbconfig.TABLE_Place, dbconfig.COLUMNS_Place);			
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
