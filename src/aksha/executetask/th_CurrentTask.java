package aksha.executetask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import aksha.adapters.ActivityListAdapter;
import aksha.adapters.ActivityListAdapterTaskTH;
import aksha.adapters.LazyAdapter;
import aksha.adapters.Myspinner;
import aksha.adapters.PlannedActivitiesGridViewAdapterTHView;
import aksha.adapters.RefreshableListView;
import aksha.connectiondetector.ConnectionDetector;
import aksha.database.Databaseutill;
import aksha.database.GetData;
import com.aksha.mosaic.R;
import aksha.webservice.Webservicerequest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.view.View.OnLongClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class th_CurrentTask extends Fragment {
	
	
	private static View root;
	private RefreshableListView mListView;
	private Databaseutill db;
	private GetData get;
	private String encryptedgeoid = "";
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		root = inflater.inflate(R.layout.thcurrenttask_refreshablelist, container, false);
		
		//ImageView createactivity = (ImageView)root.findViewById(R.id.createactivity);
		mListView = ((RefreshableListView)root.findViewById(R.id.current_refreshable_list));
		
		db = Databaseutill.getDBAdapterInstance(getActivity());
		get = new GetData(getActivity(),db);
		final String currentDate = new SimpleDateFormat("d MMM yyyy").format(new Date());
	    Calendar cal = Calendar.getInstance();
	 // add days to current date
	 // c.add(Calendar.DATE, daysToAdd);
	 // c.add(Calendar.DATE, -daysToSubtract);
	    cal.add(5, 3);
		  
	    mListView.setOnRefreshListener(new RefreshableListView.OnRefreshListener()
 	    {
 	      public void onRefresh(RefreshableListView paramAnonymousRefreshableListView)
 	      {
 	    	  if(new ConnectionDetector(getActivity()).isConnectingToInternet())
 	    	  {
 	    		  ArrayList<HashMap<String, String>> logindata = get.getAllLogin();
 	    		  String empid = logindata.get(0).get("empid"); 	    		 
 	    		  new GetActivities().execute(empid);
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
	    if(logindata.size()>0){
		  String empid = logindata.get(0).get("empid");
		
		  new GetActivities().execute(empid);
	    }else{
	    	
	    	ArrayList<String> str = new ArrayList<String>();
		    str.add("Please Pull Down to Refresh");
			LazyAdapter adapter = new LazyAdapter(getActivity(), str);
			mListView.setAdapter(adapter);
	    	
	    }
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
		ProgressDialog pDialog;
		
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			
			  pDialog = new ProgressDialog(getActivity());
				try{
					this.pDialog.setMessage("Please wait ...");
			        this.pDialog.setIndeterminateDrawable(getActivity().getResources().getDrawable(R.drawable.red_progress));
			        this.pDialog.setIndeterminate(false);
			        this.pDialog.setCancelable(false);
			        this.pDialog.show();
				}
				catch(Exception e)
				{
					e.getMessage();
				}
		}

		
		
		
		@Override
		protected ArrayList<HashMap<String, String>> doInBackground(
				String... params) {
			// TODO Auto-generated method stub
			ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String,String>>();
			try{
				data = get.currenttaskTH(params[0]);
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
					
	 				ActivityListAdapterTaskTH adapter = new ActivityListAdapterTaskTH(getActivity(), result, mListView);
	 				//PlannedActivitiesGridViewAdapterTHView adapter = new PlannedActivitiesGridViewAdapterTHView(getActivity(), arraylist, mListView, date);
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
			pDialog.dismiss();
		}
		
	}	
	

}
