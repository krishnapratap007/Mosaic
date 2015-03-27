package aksha.activityplanningth;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;

import aksha.adapters.CalendarAdapter;
import aksha.adapters.LazyAdapter;
import aksha.adapters.Myspinner;
import aksha.adapters.PlannedActivitiesGridViewAdapterTHView;
import aksha.adapters.RefreshableListView;
import aksha.adapters.RefreshableListView.OnRefreshListener;
import aksha.connectiondetector.ConnectionDetector;
import aksha.database.Databaseutill;
import aksha.database.GetData;
import com.aksha.mosaic.R;
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
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Toast;

public class th_view_activity extends Fragment
{
	
	public GregorianCalendar month, itemmonth;// calendar instances.

	public CalendarAdapter adapter;// adapter instance
	public Handler handler;// for grabbing some event values for showing the dot
	                         // marker.
	public ArrayList<String> items; // container to store calendar items which
									// needs showing the event marker
	private ArrayList<HashMap<String, String>> data;
	private GetData get;
	private Databaseutill db;
	private String empid,districtid;
	private View root;
	private GridView gridview;
	private String encryptedgeoid;
	Spinner mdoSpinner;
	private RefreshableListView mListView;
	

	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		

	    root=inflater.inflate(R.layout.th_view_planning,container, false);
		db = Databaseutill.getDBAdapterInstance(getActivity());
		get = new GetData(getActivity(),db);
		mListView = ((RefreshableListView)root.findViewById(R.id.approve_listview));

		
		
		
		mListView.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh(RefreshableListView listView) {
				
				try{
				   // Perform action on click
         		if(new ConnectionDetector(getActivity()).isConnectingToInternet())
          	  	{
         			
         			
         			// perform when select asyncTask
         			Spinner actv1=(Spinner)root.findViewById(R.id.auto_buy2);
         			
         			Myspinner sp=(Myspinner)actv1.getSelectedItem();
                    if(!sp.getValue().equalsIgnoreCase("0"))
                    {
        	 
				                 // Perform action on click
				         		if(new ConnectionDetector(getActivity()).isConnectingToInternet())
				          	  	{
				         			
				         			
				         			// perform when select asyncTask
				         			new GetActivities().execute(sp.getValue());
				         			
				         			
				         			
				          	  	}
				        		else
				        		{
				        			Toast.makeText(getActivity(), "No Network Found", Toast.LENGTH_LONG).show();
				        		}
                    }
       
			
         			
         			
         			
          	  	}
        		else
        		{
        			Toast.makeText(getActivity(), "No Network Found", Toast.LENGTH_LONG).show();
        		}
         		
         		
			}catch(Exception er){er.getMessage();}
			}
		});
		
		new searchData().execute();
		return root;
	}
	
	
	
	class searchData extends AsyncTask<String, Void, ArrayList<Myspinner>>{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			
			
		}
		
		@Override
		protected ArrayList<Myspinner> doInBackground(String... params) {
		
			GetData gd=new GetData(getActivity(), db);
			ArrayList<HashMap<String, String>> data= gd.getmdo();
			ArrayList<Myspinner> mp=new ArrayList<Myspinner>();
			mp.add(new Myspinner("Select MDO","0",""));
			for(int icount=0;icount<data.size();icount++)
			{
				HashMap<String, String> dv=data.get(icount);
				mp.add(new Myspinner(dv.get("2"),dv.get("1"),""));
				
			}
			
			return mp;
		}
		

		@Override
		protected void onPostExecute(final ArrayList<Myspinner> result) {
			
			try{
			if(result!=null){
			
				if(result.size()>0){
					
					ArrayList<Myspinner> demo=result;					
					//data save
					AdapterSearch adap=new AdapterSearch(getActivity(), android.R.layout.select_dialog_item, demo);
					//	ArrayAdapter<Myspinner> adapter1 = new ArrayAdapter<Myspinner>(getActivity(), android.R.layout.select_dialog_item, redemo1);
						final Spinner actv2=(Spinner)root.findViewById(R.id.auto_buy2);
						adap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						actv2.setTag(demo);
						actv2.setAdapter(adap);
						actv2.setOnItemSelectedListener(new OnItemSelectedListener() {

							@Override
							public void onItemSelected(AdapterView<?> parent,
									View view, int position, long id) {
								Myspinner sp=(Myspinner) actv2.getSelectedItem();
                                if(!sp.getValue().equalsIgnoreCase("0"))
                                {
	            	 
					                 // Perform action on click
					         		if(new ConnectionDetector(getActivity()).isConnectingToInternet())
					          	  	{
					         			
					         			
					         		// perform when select asyncTask
					         			new GetActivities().execute(sp.getValue());
					         			
					         			
					         			
					          	  	}
					        		else
					        		{
					        			Toast.makeText(getActivity(), "No Network Found", Toast.LENGTH_LONG).show();
					        		}
                                }
								
							}

							@Override
							public void onNothingSelected(AdapterView<?> parent) {
								// TODO Auto-generated method stub
								
							}
						});
						
						
											
				}}}catch(Exception er){
					er.getMessage();
				}}}
	
	
class GetActivities extends AsyncTask<String, Void, ArrayList<HashMap<String, String>>>{
		
	ProgressDialog pDialog;
	@Override
	protected void onPreExecute() {
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
			  data = get.getPlannedAtivitiesToView(params[0]);
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
					ArrayList<String> date = new ArrayList<String>();
					for (HashMap<String, String> arrayList2 : result) {
					if(!date.contains(arrayList2.get("plan_date")))
					date.add(arrayList2.get("plan_date"));
					}	
					for(String dte : date){
					ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String,String>>();
					for(int i=0; i<result.size(); i++){
//						SimpleDateFormat stringdate = new SimpleDateFormat("M/dd/yyyy HH:mm:ss aa");
//						String stringdate = new SimpleDateFormat("dd MMM yyyy EEEE").format(new Date());
					if(dte.equalsIgnoreCase(result.get(i).get("plan_date"))){
					data.add(result.get(i));
					}	
					}
					arraylist.add(data);
					}
					
					
					PlannedActivitiesGridViewAdapterTHView adapter = new PlannedActivitiesGridViewAdapterTHView(getActivity(), arraylist, mListView, date);
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
			pDialog.dismiss();
			
		}
		
	}
	


	

	
}
