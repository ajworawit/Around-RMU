package com.ajtum.itrmu.aroundrmu;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.ajtum.database.DatabaseConfig;
import com.ajtum.database.DatabaseHelper;
import com.ajtum.database.DatabaseManager;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapAllFragment extends Fragment  {

	
	MapAllClass mapall;
	public static final int DIALOG_DOWNLOAD_XML_PROGRESS = 0;
	private ProgressDialog mProgressDialog;
	private Activity activity;
	
	private Cursor cursor;
	private SQLiteDatabase db;
	private DatabaseConfig dbconfig;
	private DatabaseHelper dbHelper;
	private DatabaseManager manage;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.map, container, false);
		activity=getActivity();
		activity.setProgressBarIndeterminateVisibility(true); 

		// get place db
	      dbHelper=new DatabaseHelper(getActivity(),SQLiteDatabase.OPEN_READWRITE);
	      dbHelper.createDatabase();
	      db =  dbHelper.openDatabase();
	      cursor=DatabaseManager.getAllInTable(db, dbconfig.TABLE_Place, dbconfig.COLUMNS_Place);

		mapall=new MapAllClass(getActivity(),cursor);

		ImageButton btnType1=(ImageButton)rootView.findViewById(R.id.btnType);

		btnType1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) { 
				// TODO Auto-generated method stub
				if(mapall.myMap.getMapType()==1){
					mapall.myMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
				}else{
					mapall.myMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
				}
			}
		}); 

		
		
		return rootView;
	}
	

	public void onDestroyView() {
	    super.onDestroyView();
		android.app.Fragment f = getActivity().getFragmentManager().findFragmentById(R.id.mapv2);
	    if (f != null) {
	        getActivity().getFragmentManager()
	        .beginTransaction().remove(f).commit();
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
	 

	

}
