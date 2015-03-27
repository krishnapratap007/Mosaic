package aksha.plannedactivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import aksha.adapters.ActivityListAdapter;
import aksha.adapters.LazyAdapter;
import aksha.adapters.Myspinner;
import aksha.adapters.PlannedActivitiesGridViewAdapter;
import aksha.adapters.PlannedActivitiesListAdapter;
import aksha.adapters.RefreshableListView;
import aksha.connectiondetector.ConnectionDetector;
import aksha.database.Databaseutill;
import aksha.database.GetData;
import com.aksha.mosaic.R;
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
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

public class PlannedActivities extends Fragment {
	
	private static View root;
	private RefreshableListView mListView;
	private Databaseutill db;
	private GetData get;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		root = inflater.inflate(R.layout.plannedactivities_refreshablelist, container, false);
				
		mListView = ((RefreshableListView)root.findViewById(R.id.plannedactivities_refreshable_list));
		
		db = Databaseutill.getDBAdapterInstance(getActivity());
		get = new GetData(getActivity(), db);		
	    
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
 	    	  
 	    	/*ArrayList<HashMap<String, String>> category=new ArrayList<HashMap<String,String>>();
 	    	for(int i=0; i<arraylist.size(); i++){
 	    		HashMap<String, String> map=new HashMap<String, String>();
 				map.put("1","Name"+String.valueOf(i+1));
 				category.add(map);
 	    	}
 	    	arraylist.add(category);
 			PlannedActivitiesGridViewAdapter adapter = new PlannedActivitiesGridViewAdapter(getActivity(), arraylist, mListView);
 			mListView.setAdapter(adapter);
 			mListView.completeRefreshing();*/
 	      }
 	    });
	    
	    if(new ConnectionDetector(getActivity()).isConnectingToInternet())
  	  {
  		  ArrayList<HashMap<String, String>> logindata = get.getAllLogin();
  		  if(logindata.size()>0){
  		   String empid = logindata.get(0).get("empid");
  		   String districtid = logindata.get(0).get("geoid");
  		   new GetActivities().execute(empid,districtid);
  		  }
  		  else{
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
		
		@Override
		protected ArrayList<HashMap<String, String>> doInBackground(
				String... params) {
			// TODO Auto-generated method stub
			ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String,String>>();
			try{
				data = get.getPlannedAtivities(params[0], params[1]);
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
				//{jobdesc=Field Demo Wheat, plandate=09 Mar 2015 Monday, mandi=Baroli, retailer=Om Khlad Bhandar, districtid=AGRA}
				if(result!=null && result.size()>0){
					final ArrayList<ArrayList<HashMap<String, String>>> arraylist = new ArrayList<ArrayList<HashMap<String,String>>>();
					ArrayList<String> date = new ArrayList<String>();
					for (HashMap<String, String> arrayList2 : result) {
						if(!date.contains(arrayList2.get("plandate")))
						date.add(arrayList2.get("plandate"));
					}					
					for(String dte : date){
						ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String,String>>();
						for(int i=0; i<result.size(); i++){
						//	SimpleDateFormat stringdate = new SimpleDateFormat("M/dd/yyyy HH:mm:ss aa");
						//	String stringdate = new SimpleDateFormat("dd MMM yyyy EEEE").format(new Date());
							if(dte.equalsIgnoreCase(result.get(i).get("plandate"))){
								data.add(result.get(i));
							}							
						}
						arraylist.add(data);
					}
					PlannedActivitiesGridViewAdapter adapter = new PlannedActivitiesGridViewAdapter(getActivity(), arraylist, mListView, date);
		 			mListView.setAdapter(adapter);
				}
				else
		    	  {
		    		 ArrayList<String> str = new ArrayList<String>();
		    		 str.add("No Data Found, Please Pull Down Again to Refresh");
		    		 LazyAdapter adapter = new LazyAdapter(getActivity(), str);
		    		 mListView.setAdapter(adapter);
		    	  }
			}
			catch(Exception e){
				e.getMessage();
			}
			mListView.completeRefreshing();
		}
		
	}
	
}