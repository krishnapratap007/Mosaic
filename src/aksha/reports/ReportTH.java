package aksha.reports;

import aksha.executetask.th_CurrentTask;
import aksha.executetask.th_PendingTask;
import aksha.executetask.th_UpcomingTask;
import com.aksha.mosaic.R;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost.OnTabChangeListener;

public class ReportTH extends Fragment {
	
	private View mRoot;
	private FragmentTabHost mTabHost1;
	 @SuppressLint("NewApi")
	public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
	  {
		getActivity().getActionBar();
			
			mRoot = paramLayoutInflater.inflate(R.layout.report_th, null);


			mTabHost1 = (FragmentTabHost)mRoot.findViewById(android.R.id.tabhost);
			mTabHost1.setup(getActivity(),getChildFragmentManager(), R.id.realtabcontent);
			
			mTabHost1.addTab(mTabHost1.newTabSpec("0").setIndicator("MTD"),
					th_ReportSummary.class, null);
			
			mTabHost1.addTab(mTabHost1.newTabSpec("1").setIndicator("Previous Month"), 
					th_ReportTodayAct.class, null);
			
			
			
			for(int i=0;i<mTabHost1.getTabWidget().getChildCount();i++)
			 {
				 
				 mTabHost1.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#96E0D2"));
				 
			 }  
			 mTabHost1.getTabWidget().getChildAt(Integer.parseInt("0")).setBackgroundColor(Color.parseColor("#65D2BD"));
			 
			 
			mTabHost1.setOnTabChangedListener(new OnTabChangeListener() {
				
				@Override
				public void onTabChanged(String tabId) {
					// TODO Auto-generated method stub
					
					
					 for(int i=0;i<mTabHost1.getTabWidget().getChildCount();i++)
					 {
						 mTabHost1.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#96E0D2"));
					 }  
					 mTabHost1.getTabWidget().getChildAt(Integer.parseInt(tabId)).setBackgroundColor(Color.parseColor("#65D2BD"));
				}
			});
						
				return mRoot;
			
			
		}
	
	
	
	
	
	
	

}
