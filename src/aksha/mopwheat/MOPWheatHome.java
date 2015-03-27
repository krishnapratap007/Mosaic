package aksha.mopwheat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import com.aksha.mosaic.R;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.TabHost.OnTabChangeListener;

public class MOPWheatHome extends Fragment {

	private View mRoot;
	private FragmentTabHost mTabHost;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		/*getSupportFragmentManager().beginTransaction()
		.replace(R.id.detail_frame, fragment,"News_SKVK").addToBackStack(null).commit();*/
		
		getActivity().getActionBar();

		mRoot = inflater.inflate(R.layout.mopwheathome, null);
		
		mTabHost = (FragmentTabHost)mRoot.findViewById(android.R.id.tabhost);
		mTabHost.setup(getActivity(),getChildFragmentManager(), R.id.realtabcontent);

		mTabHost.addTab(mTabHost.newTabSpec("0").setIndicator("Demo Farmer"),
				DemoFarmer.class, null);
		
		mTabHost.addTab(mTabHost.newTabSpec("1").setIndicator("Observations"),
				Observations.class, null);
		
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
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		
		try{
			final Uri imageUri = data.getData();
			String path = getRealImagePathFromURI(imageUri);
			
			
		}
		catch(Exception e){
			e.getMessage();
		}
	}
	
	public String getRealImagePathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
	
}
