package aksha.activityplanningth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;

import aksha.activityplanningth.CalendarView_th.GetActivities;
import aksha.activityplanningth.CalendarView_th.searchData;
import aksha.adapters.CalendarAdapter;
import aksha.adapters.DateMapComparator;
import aksha.adapters.LazyAdapter;
import aksha.adapters.Myspinner;
import aksha.adapters.PlannedActivitiesGridViewAdapterTH;
import aksha.adapters.RefreshableListView;
import aksha.adapters.RefreshableListView.OnRefreshListener;
import aksha.connectiondetector.ConnectionDetector;
import aksha.database.Databaseutill;
import aksha.database.GetData;
import com.aksha.mosaic.R;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class th_approve_activity extends Fragment{
	
	public GregorianCalendar month, itemmonth;// calendar instances.

	public CalendarAdapter adapter;// adapter instance
	public Handler handler;// for grabbing some event values for showing the dot
							// marker.
	public ArrayList<String> items; // container to store calendar items which
									// needs showing the event marker
	private ArrayList<HashMap<String, String>> data;
	private GetData get;
	private Databaseutill db;
	private View root;

	Spinner mdoSpinner;
	private RefreshableListView mListView;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

	      root=inflater.inflate(R.layout.th_approve_planning,container, false);
		//root = inflater.inflate(R.layout.calender_th, container, false);
		
		db = Databaseutill.getDBAdapterInstance(getActivity());
		get = new GetData(getActivity(),db);

		mListView = ((RefreshableListView)root.findViewById(R.id.approve_listview));

		mListView.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh(RefreshableListView listView) {
				
				try{
				   // Perform action on click
         	
         			
         			
         			// perform when select asyncTask
         			Spinner actv1=(Spinner)root.findViewById(R.id.auto_buy1);
         			
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
					final	Spinner actv1=(Spinner)root.findViewById(R.id.auto_buy1);
						
						actv1.setTag(demo);
						adap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						actv1.setAdapter(adap);
						actv1.setOnItemSelectedListener(new OnItemSelectedListener() {

							@Override
							public void onItemSelected(AdapterView<?> parent,
									View view, int position, long id) {
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

							@Override
							public void onNothingSelected(AdapterView<?> parent) {
								// TODO Auto-generated method stub
								
							}
							
						});
						
						
						
						
												
					
				}}}catch(Exception er){
					er.getMessage();
				}}}
	
	
	
class GetActivities extends AsyncTask<String, Void, ArrayList<HashMap<String, String>>>{
		
	String id;
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
			try
			{
				id=params[0];
			  data = get.getPlannedAtivitiesToApprrove(params[0]);
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
					PlannedActivitiesGridViewAdapterTH adapter = new PlannedActivitiesGridViewAdapterTH(getActivity(), arraylist, mListView, date,id);
					mListView.setAdapter(adapter);
					
					String ids="";
				for(int icount=0;icount<arraylist.size();icount++){
					
					ArrayList<HashMap<String, String>> dt=arraylist.get(icount);
					for(int jcount=0;jcount<dt.size();jcount++)
					{
						HashMap<String, String> lv=dt.get(jcount);
						if(lv.get("planstatus").equalsIgnoreCase("2")){
						ids+="'"+lv.get("actplanid")+"',";
						}
						
					}
				}	
					
				String vids=ids.substring(0,(ids.length()-1));
					Button btn=(Button)root.findViewById(R.id.aprrovebutton);
					btn.setVisibility(View.VISIBLE);
					btn.setTag(R.id.tag_first, vids);
					btn.setTag(R.id.tag_second,id);
					btn.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(final View v) {
							
							
							AlertDialog.Builder build = new Builder(getActivity())
							.setTitle("Information")
							.setMessage("Are you sure ?")
							.setPositiveButton("Create", new DialogInterface.OnClickListener() {								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									//approve all data
									Button bt=(Button)v;
									String id=String.valueOf(bt.getTag(R.id.tag_second));
									String ids=String.valueOf(bt.getTag(R.id.tag_first));							
									new ApproveActivity().execute(ids,id);
									
								}
							})
							.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									
									dialog.cancel();
								}
							});
							AlertDialog alert = build.create();
							alert.show();
							
								
							
						}
					});
					
					
					
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
class ApproveActivity extends AsyncTask<String, Void, String>{
	ProgressDialog pDialog;
String faid;	
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
	protected String doInBackground(String... params) {
		
		String data = get.ApproveData(params[0]);
		faid=params[1];
		return data;
	}
	@Override
	protected void onPostExecute(String result) {    		
		pDialog.dismiss();
		new GetActivities().execute(faid);
	}
	
}    


	


}
