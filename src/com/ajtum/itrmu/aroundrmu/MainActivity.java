package com.ajtum.itrmu.aroundrmu;


import com.ajtum.database.DatabaseManager;
import com.ajtum.itrmu.aroundrmu.SearchResultsActivity.Processing;
import com.ajtum.itrmu.aroundrmu.SearchResultsActivity.SearchAdapter;
import com.ajtum.tabadaper.TabsPagerAdapter;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.SearchView;
import android.widget.TextView;

public class MainActivity extends FragmentActivity implements
		ActionBar.TabListener {

	private ViewPager viewPager;
	private TabsPagerAdapter mAdapter;
	private ActionBar actionBar;
	// Tab titles
	public static String[] tabs = { "อาคาร","คณะ","หน่วยงาน"};
	private String titileBar="เส้นทาง มรม.";
	private ProgressDialog pDialog;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Initilization
		viewPager = (ViewPager) findViewById(R.id.pager);
		actionBar = getActionBar();
		actionBar.setIcon(R.drawable.marker);
		//actionBar.setTitle(titileBar);
		
		
		new Processing().execute();


		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);	
//		actionBar.setDisplayShowTitleEnabled(false);
//		actionBar.setDisplayShowHomeEnabled(false);
//		actionBar.setHomeButtonEnabled(false);
		
        if (savedInstanceState != null) {
        	actionBar.setSelectedNavigationItem(savedInstanceState.getInt("tab", 0));
        }
        
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// on changing the page
				// make respected tab selected
				actionBar.setSelectedNavigationItem(position);
				if(position==0){
					actionBar.setTitle("แสดงอาคาร");
				}else if(position==1){
					actionBar.setTitle("แสดงคณะ");
				}else{
					actionBar.setTitle("แสดงหน่วยงาน");
				}
				
				
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	} 
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_main_actions, menu);

		// Associate searchable configuration with the SearchView
//		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//		SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
//				.getActionView();
//		searchView.setSearchableInfo(searchManager
//				.getSearchableInfo(getComponentName()));

		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Take appropriate action for each action item click
		switch (item.getItemId()) {
//		case R.id.action_search:
//			// search action
//			return true;
		case R.id.action_location_found:
			// location found
			MapsIntent();
			return true;
		case R.id.action_contact:
			// contact
			ContactIntent();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	
	/**
	 * Launching new activity
	 * */
	private void MapsIntent() {
		Intent i = new Intent(MainActivity.this, MapsLocation.class);
		startActivity(i);
	}
	private void ContactIntent() {
		Intent i = new Intent(MainActivity.this, Contact.class);
		startActivity(i);
	}
	
	
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		//super.onSaveInstanceState(outState);
		outState.putInt("tab", getActionBar().getSelectedNavigationIndex());
	}

	@Override
	public void onBackPressed() {
	  if(getSupportFragmentManager().getBackStackEntryCount() != 0) {
	    getSupportFragmentManager().popBackStack();
	  } else {
	    super.onBackPressed();
	  }
	}
	
	
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// on tab selected
		// show respected fragment view
		viewPager.setCurrentItem(tab.getPosition());
		if(tab.getPosition()==0){
			actionBar.setTitle("แสดงอาคาร");
		}else if(tab.getPosition()==1){
			actionBar.setTitle("แสดงคณะ");
		}else{
			actionBar.setTitle("แสดงหน่วยงาน");
		}
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}

	
	
	
	//AsyncTask
    class Processing extends AsyncTask<Context,Integer,String> {
		String s="";
      @Override
       protected void onPreExecute() {
           super.onPreExecute();
          
           pDialog = new ProgressDialog(MainActivity.this);
           //pDialog.setTitle("Information");
           pDialog.setMessage("กำลังโหลด ..");
           pDialog.setIndeterminate(false);
           pDialog.setCancelable(false);
           pDialog.show();
       }     
      
       protected String doInBackground(Context... params ) { 
                 try {
					Thread.sleep(300);
					mAdapter = new TabsPagerAdapter(getSupportFragmentManager());
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

					// Adding Tabs
					for (String tab_name : tabs) {
						actionBar.addTab(actionBar.newTab().setText(tab_name)
								.setTabListener(MainActivity.this));
					}

        		viewPager.setAdapter(mAdapter);
  
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
