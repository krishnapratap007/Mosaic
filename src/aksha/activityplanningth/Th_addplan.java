package aksha.activityplanningth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import aksha.adapters.LazyAdapter;
import aksha.adapters.PlannedActivitiesGridViewAdapterTH;
import aksha.adapters.RefreshableListView;
import aksha.connectiondetector.ConnectionDetector;
import aksha.database.Databaseutill;
import aksha.database.GetData;
import com.aksha.mosaic.R;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Th_addplan extends Fragment {
	
	private static View root;
	private RefreshableListView mListView;
	private Databaseutill db;
	private GetData get;
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		root = inflater.inflate(R.layout.th_addplan, container, false);
				
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
	    
	    ArrayList<String> str = new ArrayList<String>();
	    str.add("Please Pull Down to Refresh");
		LazyAdapter adapter = new LazyAdapter(getActivity(), str);
		mListView.setAdapter(adapter);
		
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
				if(result!=null && result.size()>0){
					final ArrayList<ArrayList<HashMap<String, String>>> arraylist = new ArrayList<ArrayList<HashMap<String,String>>>();
					String[] date = result.get(result.size()-1).get("date").split(",");
					ArrayList<String> dat=new ArrayList<String>();
					for(String dte : date){
						dat.add(dte);
						ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String,String>>();
						for(int i=0; i<result.size()-1; i++){
							SimpleDateFormat stringdate = new SimpleDateFormat("M/dd/yyyy HH:mm:ss aa");
							Date plandate = stringdate.parse(result.get(i).get("plan_date"));
							Date selecteddate = stringdate.parse(dte);
							if(plandate.equals(selecteddate)){
								data.add(result.get(i));
							}
						}
						arraylist.add(data);
					}
					PlannedActivitiesGridViewAdapterTH adapter = new PlannedActivitiesGridViewAdapterTH(getActivity(), arraylist, mListView, dat,"");
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
