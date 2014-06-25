package com.ajtum.itrmu.aroundrmu;

import com.ajtum.database.DatabaseConfig;
import com.ajtum.database.DatabaseHelper;
import com.ajtum.database.DatabaseManager;
import com.ajtum.itrmu.aroundrmu.SearchResultsActivity.Processing;
import com.ajtum.itrmu.aroundrmu.SearchResultsActivity.SearchAdapter;

import android.R.drawable;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ShowDetail extends Activity {

	private Cursor cursor;
	private SQLiteDatabase db;
	private DatabaseConfig dbconfig;
	private DatabaseHelper dbHelper;
	private String prefix_img="img_";
	
	TextView tvName,tvPlace,tvWebsite,tvTel,tvWorkdate,tvWorktime,tvAddress;
	ImageView img_build;
	ImageView btnNearBy;
	private ProgressDialog pDialog;
	private String place_id,orgName,place_lat,place_long;
	String org_id;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	
	    setContentView(R.layout.show_layout);
	    
        ActionBar actionBar = getActionBar();
		actionBar.setIcon(R.drawable.marker);
		
		
	    tvName=(TextView)findViewById(R.id.tvname);
	    tvPlace=(TextView)findViewById(R.id.tvPlace);
	    tvWebsite=(TextView)findViewById(R.id.tvwebsite);
	    tvTel=(TextView)findViewById(R.id.tvtel);
	    tvWorkdate=(TextView)findViewById(R.id.tvworkdate);
	    tvWorktime=(TextView)findViewById(R.id.tvworktime);
	    tvAddress=(TextView)findViewById(R.id.tvaddress);
	    btnNearBy=(ImageView)findViewById(R.id.btnNearby);
	    img_build=(ImageView)findViewById(R.id.img_build);
	    
	      dbHelper=
	    		  new DatabaseHelper(getApplicationContext(),SQLiteDatabase.OPEN_READWRITE);
	      dbHelper.createDatabase();
	      db =  dbHelper.openDatabase();
	      
	      Intent intent = getIntent();
	      org_id = intent.getStringExtra("org_id");
	     
	      new Processing().execute();
	      
 	      // click link
 	      tvWebsite.setOnClickListener(new OnClickListener() {
 			
 			@Override
 			public void onClick(View v) {
 				// TODO Auto-generated method stub
 				String url = cursor.getString(4);
 				Intent i = new Intent(Intent.ACTION_VIEW);
 				i.setData(Uri.parse(url));
 				startActivity(i);
 			}
 		});
 	      
 	      
 	      // click Near by
 	      btnNearBy.setOnClickListener(new OnClickListener() {
 			
 			@Override
 			public void onClick(View v) {
 				// TODO Auto-generated method stub
 				
 				//Toast.makeText(getApplicationContext(), "lat : "+place_lat+" long :"+place_long, Toast.LENGTH_LONG).show(); 
 				Intent intent=new Intent(getApplicationContext(),ShowMapbyPlace.class);
 				intent.putExtra("place_lat", place_lat);
 				intent.putExtra("place_long", place_long);
 				intent.putExtra("place_name", orgName);
 				intent.putExtra("place_id", place_id);
 				startActivity(intent);
 				
 			}
 		});

	      
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

	 
	 
	//AsyncTask
	    class Processing extends AsyncTask<Context,Integer,String> {
			String s="";
	      @Override
	       protected void onPreExecute() {
	           super.onPreExecute();
	          
	           pDialog = new ProgressDialog(ShowDetail.this);
	           //pDialog.setTitle("Information");
	           pDialog.setMessage("°”≈—ß‚À≈¥ ...");
	           pDialog.setIndeterminate(false);
	           pDialog.setCancelable(false);
	           pDialog.show();
	       }     
	      
	       protected String doInBackground(Context... params ) { 
	                 try {
						Thread.sleep(300);
					      cursor=DatabaseManager.getOrgbyOrgID(db,org_id);
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

	            	   cursor.moveToPosition(0);
	         	      // set image detail
	         	      int resId = getResources().getIdentifier(dbconfig.PackageName+":drawable/" + prefix_img+cursor.getString(10), null, getPackageName());
	         	      if(resId!=0){
	         	    	  img_build.setImageResource(resId);
	         	      }else{
	         	    	  img_build.setImageResource(R.drawable.build_logo_m);
	         	      }
	         	      	      	           
	         	      
	         	      tvPlace.setText(""+cursor.getString(1));
	         	      tvName.setText(""+cursor.getString(2));
	         	      tvAddress.setText(""+cursor.getString(3));
	         	      tvWebsite.setText(""+cursor.getString(4));
	         	      tvTel.setText(""+cursor.getString(5));
	         	      tvWorkdate.setText(""+cursor.getString(6));
	         	      tvWorktime.setText(""+cursor.getString(7));
	         	      
	         	      orgName=cursor.getString(2);
	         	      place_lat=cursor.getString(8);
	         	      place_long=cursor.getString(9);
	         	      place_id=cursor.getString(10);

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
