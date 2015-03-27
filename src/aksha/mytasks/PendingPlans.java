package aksha.mytasks;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import aksha.adapters.ActivityListAdapter;
import aksha.adapters.LazyAdapter;
import aksha.adapters.MultiSelectionSpinner;
import aksha.adapters.PendingActivityListAdapter;
import aksha.adapters.RefreshableListView;
import aksha.connectiondetector.ConnectionDetector;
import aksha.database.Databaseutill;
import aksha.database.GetData;
import com.aksha.mosaic.R;
import aksha.mytasks.TodaysPlan.GetActivities;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

public class PendingPlans extends Fragment {
	
	private static View root;
	private RefreshableListView mListView;
	private GetData get;
	private Databaseutill db;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		root = inflater.inflate(R.layout.pendingplans_refreshablelist, container, false);
		mListView = ((RefreshableListView)root.findViewById(R.id.pending_refreshable_list));
		
		db = Databaseutill.getDBAdapterInstance(getActivity());
		get = new GetData(getActivity(),db);
		final String currentDate = new SimpleDateFormat("d MMM yyyy").format(new Date());
	    Calendar cal = Calendar.getInstance();
	 // add days to current date
	 // c.add(Calendar.DATE, daysToAdd);
	 // c.add(Calendar.DATE, -daysToSubtract);
	    cal.add(5, 3);
		
	    final ArrayList<HashMap<String, String>> category=new ArrayList<HashMap<String,String>>();	    
	    mListView.setOnRefreshListener(new RefreshableListView.OnRefreshListener()
 	    {
 	      public void onRefresh(RefreshableListView paramAnonymousRefreshableListView)
 	      {
 	    	 if(new ConnectionDetector(getActivity()).isConnectingToInternet())
	    	  {
	    		  ArrayList<HashMap<String, String>> logindata = get.getAllLogin();
	    		  String empid = logindata.get(0).get("empid");
	    		  String districtid = logindata.get(0).get("geoid");
	    		  new GetActivities().execute(empid,districtid);
	    	  }
	    	  else
	    	  {
	    		 ArrayList<String> str = new ArrayList<String>();
	    		 str.add("No Network Found, Please Pull Down Again to Refresh");
	    		 LazyAdapter adapter = new LazyAdapter(getActivity(), str);
	    		 mListView.setAdapter(adapter);
	    		 mListView.completeRefreshing();
	    	  }
 	      }
 	    });
	    
	    
	    if(new ConnectionDetector(getActivity()).isConnectingToInternet())
  	  {
  		  ArrayList<HashMap<String, String>> logindata = get.getAllLogin();
  		  String empid = logindata.get(0).get("empid");
  		  String districtid = logindata.get(0).get("geoid");
  		  new GetActivities().execute(empid,districtid);
  	  }
  	  else
  	  {
  		 ArrayList<String> str = new ArrayList<String>();
  		 str.add("No Network Found, Please Pull Down Again to Refresh");
  		 LazyAdapter adapter = new LazyAdapter(getActivity(), str);
  		 mListView.setAdapter(adapter);
  		 mListView.completeRefreshing();
  	  }
	    
	   
		
		return root;
	}
	
	class GetActivities extends AsyncTask<String, Void, ArrayList<HashMap<String, String>>>{
		
		@Override
		protected ArrayList<HashMap<String, String>> doInBackground(
				String... params) {
			// TODO Auto-generated method stub
			ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String,String>>();
			try{
				data = get.getPendingActivities(params[0], params[1]);
			}
			catch(Exception e){
				e.getMessage();
				data.clear();
			}			
			return data;
		}
		
		@Override
		protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			try{
				if(result!=null && result.size()>0){
	 				PendingActivityListAdapter adapter = new PendingActivityListAdapter(getActivity(), result, mListView);
	 				mListView.setAdapter(adapter);
				}
				else
		    	  {
		    		 ArrayList<String> str = new ArrayList<String>();
		    		 str.add("No Data Found, Please Pull Down Again to Refresh");
		    		 LazyAdapter adapter = new LazyAdapter(getActivity(), str);
		    		 mListView.setAdapter(adapter);
		    		 mListView.completeRefreshing();
		    	  }
			}
			catch(Exception e){
				e.getMessage();
			}
			mListView.completeRefreshing();
		}
		
	}
	
}
