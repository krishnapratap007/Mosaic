package aksha.addmandi;

import com.aksha.mosaic.R;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.TabHost.OnTabChangeListener;

public class AddMandiHome extends Fragment {

	private View mRoot;
	private FragmentTabHost mTabHost;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		/*getSupportFragmentManager().beginTransaction()
		.replace(R.id.detail_frame, fragment,"News_SKVK").addToBackStack(null).commit();*/
		
		getActivity().getActionBar();

		mRoot = inflater.inflate(R.layout.addmandihome, null);
		
		mTabHost = (FragmentTabHost)mRoot.findViewById(android.R.id.tabhost);
		mTabHost.setup(getActivity(),getChildFragmentManager(), R.id.realtabcontent);

		mTabHost.addTab(mTabHost.newTabSpec("0").setIndicator("Add Mandi"),
				AddMandi.class, null);
		
		mTabHost.addTab(mTabHost.newTabSpec("1").setIndicator("My Mandi"),
				MandiList.class, null);
		
		 for(int i=0;i<mTabHost.getTabWidget().getChildCount();i++)
		 {
			 
			 mTabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#96E0D2"));
			 
		 }  
		 mTabHost.getTabWidget().getChildAt(Integer.parseInt("0")).setBackgroundColor(Color.parseColor("#65D2BD"));
		 
		 mTabHost.setOnTabChangedListener(new OnTabChangeListener() {
				
				@Override
				public void onTabChanged(String tabId) {
					// TODO Auto-generated method stub
					
					/*InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
				    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
				    */
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
