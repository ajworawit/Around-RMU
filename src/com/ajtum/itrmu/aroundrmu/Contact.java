package com.ajtum.itrmu.aroundrmu;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;

public class Contact extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.contact);
        ActionBar actionBar = getActionBar();
		actionBar.setIcon(R.drawable.marker);
		
		

	}

}
