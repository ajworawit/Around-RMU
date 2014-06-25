package com.ajtum.tabadaper;

import com.ajtum.itrmu.aroundrmu.FacultyAllFragment;
import com.ajtum.itrmu.aroundrmu.InstituteAllFragment;
import com.ajtum.itrmu.aroundrmu.MainActivity;
import com.ajtum.itrmu.aroundrmu.MapAllFragment;
import com.ajtum.itrmu.aroundrmu.PlaceAllFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

public class TabsPagerAdapter extends FragmentPagerAdapter {

	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {

		if(index==0){
			return new PlaceAllFragment();
		}else if(index==1){
			return new FacultyAllFragment();
		}else{
			return new InstituteAllFragment();
		}
 

	}
	
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }
    
	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 3;
	}

}
