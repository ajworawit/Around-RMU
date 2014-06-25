package com.ajtum.itrmu.aroundrmu;

import com.ajtum.database.DatabaseConfig;
import com.ajtum.database.DatabaseHelper;
import com.ajtum.database.DatabaseManager;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ShowPlace extends Activity {

	private Cursor cursor;
	private SQLiteDatabase db;
	private DatabaseConfig dbconfig;
	private DatabaseHelper dbHelper;
	private DatabaseManager manage;
	
	ListView  lv;
	
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.showplace_layout);
	    
	      dbHelper=new DatabaseHelper(this,SQLiteDatabase.OPEN_READWRITE);
	      dbHelper.createDatabase();
	      db =  dbHelper.openDatabase();
	      
	     // String place[]={"_place_id,place_name"};
	      cursor=manage.getAllInTable(db, dbconfig.TABLE_Place, dbconfig.COLUMNS_Place);
	      
	      
	      
	      lv=(ListView)findViewById(R.id.lv_place);
	      
	      lv.setAdapter(new PlaceAdapter(this));
	      
	      
	      
	      
	      
	   
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
				convertView = inflater.inflate(R.layout.showplace_item, null);
			}
			
			ImageView imgLogo=(ImageView)convertView.findViewById(R.id.imgLogo);
			TextView txtName = (TextView)convertView.findViewById(R.id.txtName);
			
			if(cursor.getCount()>0){
	    		cursor.moveToPosition(position);
	    		String name = cursor.getString(1);
	    		txtName.setText(name);
			}
			
			
			
			return convertView;
		}
		
	}
	
	
	
	
	
	
	

}
