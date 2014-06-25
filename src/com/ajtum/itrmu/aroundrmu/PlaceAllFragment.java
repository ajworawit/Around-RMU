package com.ajtum.itrmu.aroundrmu;

import com.ajtum.database.DatabaseConfig;
import com.ajtum.database.DatabaseHelper;
import com.ajtum.database.DatabaseManager;
import com.ajtum.itrmu.aroundrmu.ShowPlace.PlaceAdapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.SyncStateContract.Helpers;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class PlaceAllFragment extends Fragment {

	private Cursor cursor;
	private SQLiteDatabase db;
	private DatabaseConfig dbconfig;
	private DatabaseHelper dbHelper;
	private DatabaseManager manage;
	
	private String prefix_img="thum_img_";
	ListView  lv;

    Boolean isInternetPresent = false;
    ConnectionDetector cd;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.showplace_layout, container, false);
		cd = new ConnectionDetector(getActivity());
		
	      dbHelper=new DatabaseHelper(getActivity(),SQLiteDatabase.OPEN_READWRITE);
	      dbHelper.createDatabase();
	      db =  dbHelper.openDatabase();
	      
	      cursor=DatabaseManager.getAllInTable(db, dbconfig.TABLE_Place, dbconfig.COLUMNS_Place);
	      
	      
	      
	      lv=(ListView)rootView.findViewById(R.id.lv_place);
	      
	      lv.setAdapter(new PlaceAdapter(getActivity()));
        
	      

	      
	      
	      lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				
				cursor.moveToPosition(position);
				  final String place_id=cursor.getString(0);
			      final String place_Name=cursor.getString(1);
			      final String place_lat=cursor.getString(2);
			      final String place_long=cursor.getString(3);
			   
			   // get Internet status
	                isInternetPresent = cd.isConnectingToInternet();
	 
    				Intent intent=new Intent(getActivity(),ShowMapbyPlace.class);
    				intent.putExtra("place_lat", place_lat);
    				intent.putExtra("place_long", place_long);
    				intent.putExtra("place_name", place_Name);
    				intent.putExtra("place_id", place_id);
    				startActivity(intent);
    				
//	                // check for Internet status
//	                if (isInternetPresent) {
//	    				Intent intent=new Intent(getActivity(),ShowMapbyPlace.class);
//	    				intent.putExtra("place_lat", place_lat);
//	    				intent.putExtra("place_long", place_long);
//	    				intent.putExtra("org_name", orgName);
//	    				startActivity(intent);
//	                } else {
//	                    showAlertDialog(getActivity(), "No Internet Connection",
//	                            "ไม่สามารถเชื่อมต่ออินเตอร์เน็ตได้. !", false);
//	                	
//	                }
	                
	                

			}
		});
		
	   // close db   
	   //dbHelper.close();
		
		return rootView;
	}
	
		// show message
	   public void showAlertDialog(Context context, String title, String message, Boolean status) {
	        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
	        alertDialog.setTitle(title);
	        alertDialog.setMessage(message);
	        alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);
	        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	            }
	        });
	        alertDialog.show();
	    }
	   

	 @Override
	public void onPause() {
	   super.onPause();
	    if (dbHelper != null) {
	        dbHelper.close();
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
	 public void onDestroyView() {
	  super.onDestroyView();
	 }
	 
	 
	// Adapter
		class PlaceAdapter extends BaseAdapter{
			
			private LayoutInflater inflater;
			private Activity context;
			public PlaceAdapter(Activity ct){
				context=ct;
				inflater = context.getLayoutInflater();
			}
			
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				if(cursor==null) return 0;
				return cursor.getCount();
			}

			@Override
			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return position;
			}

			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return position;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup arg2) {
				// TODO Auto-generated method stub
				
				if(convertView==null){
					convertView = inflater.inflate(R.layout.list_row_map, null);
				}
				
				ImageView imgLogo=(ImageView)convertView.findViewById(R.id.imgLogo);
				TextView txtName = (TextView)convertView.findViewById(R.id.title);
				
				if(cursor.getCount()>0){
		    		cursor.moveToPosition(position);
		    		String name = cursor.getString(1);
		    		txtName.setText(name);
		    		
				      // set image detail
				      int resId = getResources().getIdentifier(dbconfig.PackageName+":drawable/" + prefix_img+cursor.getString(0), null, 
				    		 null);
				      if(resId!=0){
				    	  imgLogo.setImageResource(resId);
				      }else{
				    	  imgLogo.setImageResource(R.drawable.build_logo_small);
				      }
				}
				
				
				
				return convertView;
			}
			
		}// class adapter
	
	
	
	
	
	
	
	
}
