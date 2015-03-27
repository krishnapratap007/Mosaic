package aksha.adapters;

import aksha.activityplanningth.ActivityPlanning_th;
import aksha.addmandi.AddMandiHome;
import aksha.addretailer.MyRetailers_th;
import aksha.addretailer.RetailersHome;
import aksha.executetask.TasksTH;
import aksha.expenses.ExpensesHome;
import aksha.farmers.MyFarmer;
import aksha.farmers.MyFarmerTH;
import aksha.mopwheat.MOPWheatHome;
import aksha.mytasks.MyTasks;
import aksha.plannedactivity.PlannedActivities;
import aksha.reports.ReportTH;
import aksha.stock.StockTH;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

public class MOTabsPagerAdapter extends FragmentPagerAdapter{

	int lnth;
	
	public MOTabsPagerAdapter(Context cont,FragmentManager fm, int length) {
		super(fm);
		lnth = length;
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		
		switch (arg0) {
		
          case 0:
          	 return new ActivityPlanning_th();
          	 
          case 2:
           	 return new MyRetailers_th();
           	 
          case 3:
        	  return new MyFarmerTH();
          case 4:
        	  return new StockTH();
          case 1:
        	  return new TasksTH();
          case 5:
        	  return new ReportTH();
        	  
          	 
        
		}
		
		return null;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return lnth;
	}
	
	public String makeFragmentName(int viewId, int position) {
	    return "android:switcher:" + viewId + ":" + position;
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
	    super.destroyItem(container, position, object);
	}
}
