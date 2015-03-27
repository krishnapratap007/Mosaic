package aksha.activityplanningth;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import aksha.activityplanningth.CalendarView_th.GetActivities;
import aksha.adapters.Myspinner;
import aksha.calender.CalendarView;
import aksha.connectiondetector.ConnectionDetector;
import com.aksha.mosaic.R;
import aksha.mytasks.PendingPlans;
import aksha.mytasks.TodaysPlan;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.TabHost.OnTabChangeListener;

public class ActivityPlanning_th extends Fragment {
	
	
	private View mRoot;
	private FragmentTabHost mTabHost;
	 @SuppressLint("NewApi")
	public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
	  {
		 // getActivity().getActionBar();

			
			mRoot = paramLayoutInflater.inflate(R.layout.main_th, null);


			final FrameLayout today = (FrameLayout)mRoot.findViewById(R.id.today);
			final FrameLayout demographic = (FrameLayout)mRoot.findViewById(R.id.pendings);
			final FrameLayout others = (FrameLayout)mRoot.findViewById(R.id.calender);

			final FrameLayout today_content = (FrameLayout)mRoot.findViewById(R.id.today_content1);
			final FrameLayout pending_content = (FrameLayout)mRoot.findViewById(R.id.pendings_content);
			final FrameLayout future_content = (FrameLayout)mRoot.findViewById(R.id.future_content);
			final FrameLayout calander_content = (FrameLayout)mRoot.findViewById(R.id.calender_content);
			
			final CalendarView_th act = new CalendarView_th();
			getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.today_content1, act,"Calender").commit();
			
			today.setBackgroundColor(Color.parseColor("#65D2BD"));
			demographic.setBackgroundColor(Color.parseColor("#96E0D2"));
			others.setBackgroundColor(Color.parseColor("#96E0D2"));
			today_content.setVisibility(View.VISIBLE);
			pending_content.setVisibility(View.GONE);
			future_content.setVisibility(View.GONE);
			calander_content.setVisibility(View.GONE);
			
			today.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					today.setBackgroundColor(Color.parseColor("#65D2BD"));
					demographic.setBackgroundColor(Color.parseColor("#96E0D2"));
					others.setBackgroundColor(Color.parseColor("#96E0D2"));
					final CalendarView_th act = new CalendarView_th();
					getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.today_content1, act,"Calender").commit();
				//	FrameLayout fm = (FrameLayout)mRoot.findViewById(R.id.content);
				//	fm.removeAllViews();
				//	getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.today_content,tdplan,"Tag").commit();
					today_content.setVisibility(View.VISIBLE);
					pending_content.setVisibility(View.GONE);
					future_content.setVisibility(View.GONE);
					calander_content.setVisibility(View.GONE);
				}
			});
			
			demographic.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					today.setBackgroundColor(Color.parseColor("#96E0D2"));
					demographic.setBackgroundColor(Color.parseColor("#65D2BD"));
					others.setBackgroundColor(Color.parseColor("#96E0D2"));
				//	FrameLayout fm = (FrameLayout)mRoot.findViewById(R.id.content);
				//	fm.removeAllViews();
				//	getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.pendings_content,pndplans ,"Tag").commit();
					final th_approve_activity act = new th_approve_activity();
					getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.pendings_content, act,"Calender").commit();
					today_content.setVisibility(View.GONE);
					pending_content.setVisibility(View.VISIBLE);
					future_content.setVisibility(View.GONE);
					calander_content.setVisibility(View.GONE);
				}
			});
			
			others.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					today.setBackgroundColor(Color.parseColor("#96E0D2"));
					demographic.setBackgroundColor(Color.parseColor("#96E0D2"));
					others.setBackgroundColor(Color.parseColor("#65D2BD"));
					final th_view_activity act = new th_view_activity();
					getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.calender_content, act,"Calender").commit();
					today_content.setVisibility(View.GONE);
					pending_content.setVisibility(View.GONE);
					future_content.setVisibility(View.GONE);
					calander_content.setVisibility(View.VISIBLE);
				}
			});
				
				return mRoot;
			
			
		}
	 
	 
	 // refresh when back to previous activity CalenderView_th
	 @Override
	 public void onActivityResult(int requestCode, int resultCode, Intent data) {

	 	
	 try{
	 
		 android.support.v4.app.Fragment fragment =getActivity().getSupportFragmentManager().findFragmentByTag("Calender");
			fragment.onActivityResult(requestCode, resultCode, data);
	 	
	 }catch(Exception er){
		 er.getMessage();
		 }

	 }
	 
	 
	}
