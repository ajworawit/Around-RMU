package com.ajtum.itrmu.aroundrmu;


import com.ajtum.database.DatabaseConfig;
import com.ajtum.database.DatabaseHelper;
import com.ajtum.database.DatabaseManager;
import com.ajtum.itrmu.aroundrmu.FacultyAllFragment.FacultyAdapter;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SearchResultsActivity extends Activity {

	private TextView txtQuery;
	private Cursor cursor;
	private SQLiteDatabase db;
	private DatabaseConfig dbconfig;
	private DatabaseHelper dbHelper;
	private DatabaseManager manage;
	
	private String prefix_img="thum_img_";
	private ListView  lv;
	private ProgressDialog pDialog;
	private String query;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_results);
		
		ActionBar actionBar = getActionBar();
		actionBar.setIcon(R.drawable.marker);
		
		
	      dbHelper=new DatabaseHelper(this,SQLiteDatabase.OPEN_READWRITE);
	      dbHelper.createDatabase();
	      db =  dbHelper.openDatabase();
	      
	      lv=(ListView)findViewById(R.id.lv_search_results);
	      handleIntent(getIntent());
	}

	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
		handleIntent(intent);
	}

	private void handleIntent(Intent intent) {
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			query = intent.getStringExtra(SearchManager.QUERY);


				new Processing().execute();
				
			      lv.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1, int item,
								long arg3) {
							
							cursor.moveToPosition(item);
						      final String orgName=cursor.getString(1);
						      final String place_lat=cursor.getString(2);
						      final String place_long=cursor.getString(3);
							
		    				Intent intent=new Intent(getApplicationContext(),ShowMapbyPlace.class);
		    				intent.putExtra("place_lat", place_lat);
		    				intent.putExtra("place_long", place_long);
		    				intent.putExtra("org_name", orgName);
		    				startActivity(intent);
						}
					});
			      
		}

	}
	
	
//	 @Override
//	public void onPause() {
//	   super.onPause();
//	    if (dbHelper != null) {
//	        dbHelper.close();
//	    }
//	 }
	
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
	
	 
	 
		// show message
	public void showAlertDialog(Context context, String title, String message, Boolean status) {
	        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
	        alertDialog.setTitle(title);
	        alertDialog.setMessage(message);
	        alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);
	        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	            	// intnet update google play
	            	finish();
	            }
	        });
	        alertDialog.show();
	    }
	   
	
	// Adapter
	class SearchAdapter extends BaseAdapter{
		
		private LayoutInflater inflater;
		private Activity context;
		public SearchAdapter(Activity ct){
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


	
	
	//AsyncTask
    class Processing extends AsyncTask<Context,Integer,String> {
		String s="";
      @Override
       protected void onPreExecute() {
           super.onPreExecute();
          
           pDialog = new ProgressDialog(SearchResultsActivity.this);
           //pDialog.setTitle("Information");
           pDialog.setMessage("กำลังค้นหา ...");
           pDialog.setIndeterminate(false);
           pDialog.setCancelable(false);
           pDialog.show();
       }     
      
       protected String doInBackground(Context... params ) { 
                 try {
					Thread.sleep(300);
					//cursor=DatabaseManager.searchByActionBar(db, query);
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

            	cursor=DatabaseManager.searchByActionBar(db, query);
   				
            	if(cursor.getCount()!=0){
					
					lv.setAdapter(new SearchAdapter(SearchResultsActivity.this));
     
				}else{
					showAlertDialog(SearchResultsActivity.this, "Search Result.","ไม่พบข้อมูลที่ค้นหา !", false);
					//finish();
				}

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
