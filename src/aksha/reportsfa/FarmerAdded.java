package aksha.reportsfa;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;

import aksha.adapters.ActivityListAdapterReportmandi;
import aksha.adapters.CalendarAdapter;
import aksha.adapters.LazyAdapter;
import aksha.adapters.Myspinner;
import aksha.adapters.RefreshableListView;
import aksha.connectiondetector.ConnectionDetector;
import aksha.database.Databaseutill;
import aksha.database.GetData;
import com.aksha.mosaic.R;
import aksha.reportsfa.previousMonthFA.data;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class FarmerAdded extends Fragment{
	
	
	

	public GregorianCalendar month, itemmonth;// calendar instances.
	public CalendarAdapter adapter;// adapter instance
	public Handler handler;// for grabbing some event values for showing the dot marker
	public ArrayList<String> items; // container to store calendar items which / needs showing the event marker 
	//private ArrayList<HashMap<String, String>> data;
	private GetData get;
	private Databaseutill db;
	
	
	Spinner mdoSpinner;
	View mView;
	RefreshableListView mListView;
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		
	    mView=inflater.inflate(R.layout.th_bargraphreport2,container, false);
	    db = Databaseutill.getDBAdapterInstance(getActivity());
		get = new GetData(getActivity(), db);
		mdoSpinner=(Spinner)mView.findViewById(R.id.spmon);
		mListView = ((RefreshableListView)mView.findViewById(R.id.rptlistmtd));
	    
		
		/*mdoSpinner.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh(RefreshableListView listView) {
				
				 Myspinner sp=(Myspinner)retailersdistrictlist.getSelectedItem();
				 if(!sp.getValue().equalsIgnoreCase("0")){
				  new RetailerList().execute(sp.getValue());
				  }
				
			}
		});*/
		ArrayList<HashMap<String, String>> category1=new ArrayList<HashMap<String,String>>();
		HashMap<String, String> map1=new HashMap<String, String>();
			map1.put("1","0");
			map1.put("2","Select District");
			category1.add(map1);
			
			try{
				ArrayList<HashMap<String, String>> dist = get.getDistrict();									
				for (int i=0; i<dist.size(); i++) {
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("1",dist.get(i).get("Districtid"));
	 				map.put("2",dist.get(i).get("Districtname"));
	 				category1.add(map);		 				
				}			
			}
			catch(Exception e){
				e.getMessage();
			}										
			
		Myspinner[] redemo1 = new Myspinner[category1.size()];
		if(category1.size()>0){
		   for(int i=0; i<category1.size(); i++){
		    	redemo1[i] = new Myspinner(category1.get(i).get("2"), category1.get(i).get("1"), "");
		   }
		   ArrayAdapter<Myspinner> adapter1 = new ArrayAdapter<Myspinner>(getActivity(), android.R.layout.simple_spinner_dropdown_item, redemo1);
		   mdoSpinner.setAdapter(adapter1);
		}
            
		
		mdoSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
		
			try{
				ArrayList<HashMap<String, String>> logindata = get.getAllLogin();
			    String	empid = logindata.get(0).get("empid");		
			    Myspinner sp = (Myspinner)mdoSpinner.getSelectedItem();
			    
	 	    	 if(new ConnectionDetector(getActivity()).isConnectingToInternet())
		    	  {
				
					if(!sp.getValue().equalsIgnoreCase("0")){
						
						new data().execute(empid,sp.getValue());
					}
					else{
						Toast.makeText(getActivity(), "Please select District", Toast.LENGTH_SHORT).show();
						
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
				
				
			}
			catch(Exception e){
				e.getMessage();
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		}
		});
		//end
		
		return mView;
	}
	
	

	
	public class data extends AsyncTask<String, Void, ArrayList<HashMap<String, String>>>{

		protected ArrayList<HashMap<String, String>> doInBackground(String... params) {
			// TODO Auto-generated method stub
			//GetData dt=new GetData(getActivity(),db);
			ArrayList<HashMap<String, String>> retval=new ArrayList<HashMap<String,String>>();
			try{				
			String empId = params[0];
			String dist = params[1];	
			// call
			retval=get.farmerVerifiedgraphFA(empId,dist);
			
		}
		catch(Exception e){
			e.getMessage();
			retval.clear();
		}	
			
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

							ActivityListAdapterReportmandi adp= new ActivityListAdapterReportmandi(getActivity(),result,mListView);
							mListView.setAdapter(adp);	
								
						    mListView.setOnRefreshListener(new RefreshableListView.OnRefreshListener()
					 	    {
					 	      public void onRefresh(RefreshableListView paramAnonymousRefreshableListView)
					 	      {
					 	    	  if(new ConnectionDetector(getActivity()).isConnectingToInternet())
					 	    	  {
					 	    		
					 	    		 Myspinner sp=(Myspinner)mdoSpinner.getSelectedItem();
					 				
					 				if(!sp.getValue().equalsIgnoreCase("0"))
					 				{
					 					ArrayList<HashMap<String, String>> logindata = get.getAllLogin();
					 					String	empid = logindata.get(0).get("empid");				
					 					new data().execute(empid,sp.getValue());
					 				}else{
					 					
					 					Toast.makeText(getActivity(), "Please select District", Toast.LENGTH_SHORT).show();
					 					
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
