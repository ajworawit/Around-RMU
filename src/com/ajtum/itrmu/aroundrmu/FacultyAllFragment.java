package com.ajtum.itrmu.aroundrmu;

import com.ajtum.database.DatabaseConfig;
import com.ajtum.database.DatabaseHelper;
import com.ajtum.database.DatabaseManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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

public class FacultyAllFragment extends Fragment {

	private Cursor cursor;
	private SQLiteDatabase db;
	private DatabaseConfig dbconfig;
	private DatabaseHelper dbHelper;
	private DatabaseManager manage;
	
	private String prefix_img="thum_img_";
	ListView  lv;
	View rootView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

			rootView = inflater.inflate(R.layout.showfaculty_layout, container, false);
				
	      dbHelper=new DatabaseHelper(getActivity(),SQLiteDatabase.OPEN_READWRITE);
	      dbHelper.createDatabase();
	      db =  dbHelper.openDatabase();
	      
	      cursor=DatabaseManager.getOrgbyFaculty(db, dbconfig.TABLE_Org, dbconfig.COLUMNS_Org);
	      
	     
	      
	      lv=(ListView)rootView.findViewById(R.id.lv_place);
	      
	      lv.setAdapter(new FacultyAdapter(getActivity()));
        
	      lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int item,
					long arg3) {
				
				cursor.moveToPosition(item);
				String org_id=cursor.getString(0);
				
				//Toast.makeText(getActivity(), "item : "+org_id, Toast.LENGTH_LONG).show(); 

				Intent intent=new Intent(getActivity(),ShowDetail.class);
				intent.putExtra("org_id", org_id);
				startActivity(intent);
			}
		});
		
		   // close db   
		   //dbHelper.close();
		   
		return rootView;
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
		class FacultyAdapter extends BaseAdapter{
			
			private LayoutInflater inflater;
			private Activity context;
			public FacultyAdapter(Activity ct){
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
					convertView = inflater.inflate(R.layout.list_row, null);
				}
				
				ImageView imgLogo=(ImageView)convertView.findViewById(R.id.imgLogo);
				TextView txtName = (TextView)convertView.findViewById(R.id.title);
				

			      
				if(cursor.getCount()>0){
		    		cursor.moveToPosition(position);
		    		String name = cursor.getString(3);
		    		txtName.setText(name);
		    		
				      // set image detail
				      int resId = getResources().getIdentifier(dbconfig.PackageName+":drawable/" + prefix_img+cursor.getString(1), null, 
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
