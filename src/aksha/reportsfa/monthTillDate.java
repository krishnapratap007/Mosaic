package aksha.reportsfa;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import aksha.adapters.ActivityListAdapterReportTh;
import aksha.adapters.ActivityListAdapterReportfarmerTh;
import aksha.adapters.LazyAdapter;
import aksha.adapters.RefreshableListView;
import aksha.connectiondetector.ConnectionDetector;
import aksha.database.Databaseutill;
import aksha.database.GetData;
import com.aksha.mosaic.R;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class monthTillDate extends Fragment {
	
	View mView;
	
	
	Databaseutill db;
	GetData get;
	
	RefreshableListView mListView; 
	

	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
			
	    mView=inflater.inflate(R.layout.th_bargraphreport,container, false);
		db = Databaseutill.getDBAdapterInstance(getActivity());
		get = new GetData(getActivity(),db);
	
		mListView = ((RefreshableListView)mView.findViewById(R.id.rptlist));

		
		
		
			int current_month = Calendar.getInstance().get(Calendar.MONTH);
			current_month = current_month+1;
			String cm = String.valueOf(current_month);
			
			
				ArrayList<HashMap<String, String>> logindata = get.getAllLogin();
			    String empid = logindata.get(0).get("empid");
				//districtid = logindata.get(0).get("geoid");
					
		    if(new ConnectionDetector(getActivity()).isConnectingToInternet())
		  	  {
		     	new data().execute(empid,cm);
		     	//new datafarmer().execute(empid,cm);
		  	  }
		    else
		  	  {
			  		 ArrayList<String> str = new ArrayList<String>();
			  		 str.add("No Network Found, Please Pull Down Again to Refresh");
			  		 LazyAdapter adapter = new LazyAdapter(getActivity(), str);
			  		 mListView.setAdapter(adapter);
			  		 mListView.completeRefreshing();
			  	  }
			return mView;
		}
	
	
	
	
	public class data extends AsyncTask<String, Void, ArrayList<HashMap<String, String>>>{

		protected ArrayList<HashMap<String, String>> doInBackground(String... params) {
			// TODO Auto-generated method stub
			GetData dt=new GetData(getActivity(),db);
			
			
			String empId=params[0];
			String cm = params[1];
			
			ArrayList<HashMap<String, String>> retval=dt.previousmonthactivityFA(empId,cm);
			ArrayList<HashMap<String, String>> farmerdata=dt.farmergraphFA(empId,cm);
			retval.addAll(farmerdata);
			
			
			
			return retval;	
	    }

			 
		ProgressDialog	 pDialog;
		@Override
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
			protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				try{
					if(result!=null)
					{
						if(result.size()>0)
						
						{
																												
							ActivityListAdapterReportfarmerTh adp=new ActivityListAdapterReportfarmerTh(getActivity(),result,mListView);
							mListView.setAdapter(adp);
							
							mListView.setOnRefreshListener(new RefreshableListView.OnRefreshListener()
					 	    {
					 	      public void onRefresh(RefreshableListView paramAnonymousRefreshableListView)
					 	      {
					 	    	  if(new ConnectionDetector(getActivity()).isConnectingToInternet())
					 	    	  {
					 			
					 	    		    int current_month = Calendar.getInstance().get(Calendar.MONTH);
					 	    		    current_month = current_month+1;
					 			    	String cm= String.valueOf(current_month);
					 					ArrayList<HashMap<String, String>> logindata = get.getAllLogin();
					 					String	empid = logindata.get(0).get("empid");				
					 					new data().execute(empid,cm);
					 				  
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
																	
							
							
						}else
				    	  {
				    		 ArrayList<String> str = new ArrayList<String>();
				    		 str.add("No Data Found, Please Pull Down Again to Refresh");
				    		 LazyAdapter adapter = new LazyAdapter(getActivity(), str);
				    		 mListView.setAdapter(adapter);
				    		 mListView.completeRefreshing();
				    	  }
					
					}
					
					
				}catch(Exception e){
					
					e.getMessage();
				}
				mListView.completeRefreshing();
				pDialog.dismiss();
				
				
			}
			
			
			
		}
	

	

}
