package aksha.mytasks;

import aksha.calender.CalendarView;
import com.aksha.mosaic.R;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;

public class MyTasks extends Fragment {

	private View mRoot;
	private FragmentTabHost mTabHost;
	MenuInflater inflater;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		mRoot = inflater.inflate(R.layout.mytasks, null);
		
		final FrameLayout today = (FrameLayout)mRoot.findViewById(R.id.today);
		final FrameLayout demographic = (FrameLayout)mRoot.findViewById(R.id.pendings);
		final FrameLayout others = (FrameLayout)mRoot.findViewById(R.id.calender);

		final FrameLayout today_content = (FrameLayout)mRoot.findViewById(R.id.today_content);
		final FrameLayout pending_content = (FrameLayout)mRoot.findViewById(R.id.pendings_content);
		final FrameLayout future_content = (FrameLayout)mRoot.findViewById(R.id.future_content);
		final FrameLayout calander_content = (FrameLayout)mRoot.findViewById(R.id.calender_content);
		
		final TodaysPlan tdplan =  new TodaysPlan();
		final PendingPlans pndplans = new PendingPlans();
		
		getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.today_content, tdplan,"TodayPlan").commit();
		getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.pendings_content, pndplans,"PendingPlans").commit();				
		
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
				final CalendarView act = new CalendarView();
				getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.calender_content, act,"Calender").commit();
				today_content.setVisibility(View.GONE);
				pending_content.setVisibility(View.GONE);
				future_content.setVisibility(View.GONE);
				calander_content.setVisibility(View.VISIBLE);
			}
		});
			
			return mRoot;
		
		
	}
}
