package aksha.reportsfa;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;

import aksha.adapters.ActivityListAdapterReportTh;
import aksha.adapters.ActivityListAdapterReportfarmerTh;
import aksha.adapters.CalendarAdapter;
import aksha.adapters.LazyAdapter;
import aksha.adapters.Myspinner1;
import aksha.adapters.RefreshableListView;
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
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class previousMonthFA extends Fragment {
	
	
	public GregorianCalendar month, itemmonth;// calendar instances.
	public CalendarAdapter adapter;// adapter instance
	public Handler handler;// for grabbing some event values for showing the dot marker
	public ArrayList<String> items; // container to store calendar items which / needs showing the event marker 
	private ArrayList<HashMap<String, String>> data;
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
		get = new GetData(getActivity(),db);
		mdoSpinner=(Spinner)mView.findViewById(R.id.spmon);
		mListView = ((RefreshableListView)mView.findViewById(R.id.rptlistmtd));
	    
		// month select 
		
		Calendar cal=Calendar.getInstance();
		SimpleDateFormat month_date = new SimpleDateFormat("M");
		String month_name = month_date.format(cal.getTime());
	
		   DateFormatSymbols symbols = new DateFormatSymbols(Locale.getDefault());
		   String[] monthNames = symbols.getMonths();
		   
		   String[] newmon=new String[12];
		   for(int icount=0;icount<monthNames.length;icount++)
		     {
		    	if(!monthNames[icount].equalsIgnoreCase(""))
		     	{
		     	newmon[icount]=(monthNames[icount]);
		    	}
		     }
		   monthNames=newmon;
		   int current_month = Calendar.getInstance().get(Calendar.MONTH);
		
		   ArrayList<Myspinner1> adddist=new ArrayList<Myspinner1>();
		   adddist.add(new Myspinner1("Select Month", "0"));
		   for(int icount=0;icount<monthNames.length-1;icount++){
		  
		    if(current_month==icount){
			   	
			   	if(icount==0)
			   	{
			   		adddist.add(new Myspinner1((monthNames[monthNames.length-3]).substring(0,3)+"-"+String.valueOf(cal.get(Calendar.YEAR)-1), String.valueOf(monthNames.length-2)));
			    	adddist.add(new Myspinner1((monthNames[monthNames.length-2]).substring(0,3)+"-"+String.valueOf(cal.get(Calendar.YEAR)-1), String.valueOf(monthNames.length-1)));
			    	adddist.add(new Myspinner1((monthNames[monthNames.length-1]).substring(0,3)+"-"+String.valueOf(cal.get(Calendar.YEAR)-1), String.valueOf(monthNames.length)));	   	   	
			   	}
			   	else if(icount==1)
			   	{	
			   		adddist.add(new Myspinner1((monthNames[monthNames.length-2]).substring(0,3)+"-"+String.valueOf(cal.get(Calendar.YEAR)-1), String.valueOf(monthNames.length-1)));
			     	adddist.add(new Myspinner1((monthNames[monthNames.length-1]).substring(0,3)+"-"+String.valueOf(cal.get(Calendar.YEAR)-1), String.valueOf(monthNames.length)));
			    	adddist.add(new Myspinner1((monthNames[icount-1]).substring(0,3)+"-"+String.valueOf(cal.get(Calendar.YEAR)), String.valueOf(icount)));	   	   	
			   	}
			   	else if(icount==2)
			   	{
			   	
			    	adddist.add(new Myspinner1((monthNames[monthNames.length-1]).substring(0,3)+"-"+String.valueOf(cal.get(Calendar.YEAR)-1), String.valueOf(monthNames.length-1)));
			   	    adddist.add(new Myspinner1((monthNames[icount-2]).substring(0,3)+"-"+String.valueOf(cal.get(Calendar.YEAR)), String.valueOf(icount-1)));
			   	    adddist.add(new Myspinner1((monthNames[icount-1]).substring(0,3)+"-"+String.valueOf(cal.get(Calendar.YEAR)), String.valueOf(icount)));
			   	
			   	}
			   	else
			   	{
			   	try{
			   	adddist.add(new Myspinner1((monthNames[icount-3]).substring(0,3)+"-"+String.valueOf(cal.get(Calendar.YEAR)), String.valueOf(icount-3)));
			   	adddist.add(new Myspinner1((monthNames[icount-2]).substring(0,3)+"-"+String.valueOf(cal.get(Calendar.YEAR)), String.valueOf(icount-2)));
			   	adddist.add(new Myspinner1((monthNames[icount-1]).substring(0,3)+"-"+String.valueOf(cal.get(Calendar.YEAR)), String.valueOf(icount-1)));
			   	}catch(Exception e){
			   	e.getMessage();
			   	}
			   	}
			   	
			   	adddist.add(new Myspinner1((monthNames[icount]).substring(0,3)+"-"+String.valueOf(cal.get(Calendar.YEAR)), String.valueOf(icount+1)));
			  
	    	}
		   
		   }
		ArrayAdapter<Myspinner1> adapter = new ArrayAdapter<Myspinner1>(getActivity(), R.layout.spinneritem_tipi, adddist );
		   adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		   mdoSpinner.setAdapter(adapter);
		
	    mdoSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
		
			try{
				ArrayList<HashMap<String, String>> logindata = get.getAllLogin();
			String	empid = logindata.get(0).get("empid");				
			Myspinner1 sp=(Myspinner1)mdoSpinner.getSelectedItem();
			  if(new ConnectionDetector(getActivity()).isConnectingToInternet())
		  	  {
			
			if(!sp.getValue().equalsIgnoreCase("0"))
			{
				new data().execute(empid,sp.getValue());
			}else{
				
				Toast.makeText(getActivity(), "Please select month", Toast.LENGTH_SHORT).show();
				
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
			GetData dt=new GetData(getActivity(),db);
			ArrayList<HashMap<String, String>> retval=new ArrayList<HashMap<String,String>>();
			try{				
			String empId=params[0];
			String cm = params[1];			
			retval=dt.previousmonthactivityFA(empId,cm);	
			ArrayList<HashMap<String, String>> farmerdata=dt.farmergraphFA(empId,cm);
			retval.addAll(farmerdata);
			
			
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
							

							ActivityListAdapterReportfarmerTh adp= new ActivityListAdapterReportfarmerTh(getActivity(),result,mListView);
							mListView.setAdapter(adp);	
								
							
							  
						    mListView.setOnRefreshListener(new RefreshableListView.OnRefreshListener()
					 	    {
					 	      public void onRefresh(RefreshableListView paramAnonymousRefreshableListView)
					 	      {
					 	    	  if(new ConnectionDetector(getActivity()).isConnectingToInternet())
					 	    	  {
					 	    		
					 	    		 Myspinner1 sp=(Myspinner1)mdoSpinner.getSelectedItem();
					 				
					 				
					 				if(!sp.getValue().equalsIgnoreCase("0"))
					 				{
					 					ArrayList<HashMap<String, String>> logindata = get.getAllLogin();
					 					String	empid = logindata.get(0).get("empid");				
					 					new data().execute(empid,sp.getValue());
					 				}else{
					 					
					 					Toast.makeText(getActivity(), "Please select month", Toast.LENGTH_SHORT).show();
					 					
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
