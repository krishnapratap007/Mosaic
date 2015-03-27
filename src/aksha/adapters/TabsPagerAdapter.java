package aksha.adapters;

import java.util.Stack;

import aksha.addmandi.AddMandiHome;
import aksha.addretailer.MyRetailers;
import aksha.addretailer.RetailersHome;
import aksha.expenses.ExpensesHome;
import aksha.farmers.FarmerHome;
import aksha.farmers.MyFarmer;
import aksha.mopwheat.MOPWheatHome;
import aksha.mytasks.MyTasks;
import aksha.plannedactivity.PlannedActivities;
import aksha.reportsfa.Reports_FA;
import aksha.stock.Stock;
import aksha.upcomingdemo.UpcomingDemoHome;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

public class TabsPagerAdapter extends FragmentPagerAdapter{

	int lnth;
	
	public TabsPagerAdapter(Context cont,FragmentManager fm, int length) {
		super(fm);
		lnth = length;
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		
		switch (arg0) {
		
          case 0:
         	 
         	 return new MyTasks();
         	 
          	 
          case 1:
           	 
           	 return new PlannedActivities();
                           	 
        	 
         /* case 2:
         	 
         	 return new MOPWheatHome();*/
         	 
          case 2:
        	  return new MyRetailers();
        	 // return new ExpensesHome();
          
          case 3:
        	  return new FarmerHome();
          case 4:
        	  return new Stock();
          case 5:
        	  return new UpcomingDemoHome();
          case 6:
        	  return new Reports_FA();
        /*  case 4:
          	 
          	 return new MOPMustardHome();
          	 
          case 5:
        	  
        	  return new RetailerHomeScreen();*/
          	 
          /*case 6:
        	  
        	  return new PotashContent();
           	 */
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
