package aksha.executetask;

import aksha.activityplanningth.CalendarView_th;
import aksha.activityplanningth.th_approve_activity;
import aksha.activityplanningth.th_view_activity;
import com.aksha.mosaic.R;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.TabHost.OnTabChangeListener;

public class TasksTH extends Fragment {
	
	
	private View mRoot;
	private FragmentTabHost mTabHost;
	 @SuppressLint("NewApi")
	public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
	  {
		getActivity().getActionBar();
			
			mRoot = paramLayoutInflater.inflate(R.layout.task_th, null);
			mTabHost = (FragmentTabHost)mRoot.findViewById(android.R.id.tabhost);
			mTabHost.setup(getActivity(),getChildFragmentManager(), R.id.realtabcontent);
			
			mTabHost.addTab(mTabHost.newTabSpec("0").setIndicator("Current Task"),
					th_CurrentTask.class, null);
			
			mTabHost.addTab(mTabHost.newTabSpec("1").setIndicator("Pending Task"), 
					th_PendingTask.class, null);
			
			mTabHost.addTab(mTabHost.newTabSpec("2").setIndicator("Upcoming Task"),
					th_UpcomingTask.class, null);
			
			
			for(int i=0;i<mTabHost.getTabWidget().getChildCount();i++)
			 {
				 
				 mTabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#96E0D2"));
				 
			 }  
			 mTabHost.getTabWidget().getChildAt(Integer.parseInt("0")).setBackgroundColor(Color.parseColor("#65D2BD"));
			 
			 
			mTabHost.setOnTabChangedListener(new OnTabChangeListener() {
				
				@Override
				public void onTabChanged(String tabId) {
					// TODO Auto-generated method stub
					
					
					 for(int i=0;i<mTabHost.getTabWidget().getChildCount();i++)
					 {
						 mTabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#96E0D2"));
					 }  
					 mTabHost.getTabWidget().getChildAt(Integer.parseInt(tabId)).setBackgroundColor(Color.parseColor("#65D2BD"));
				}
			});
			
				return mRoot;
			
			
		}
	
	
	
	
	

}
