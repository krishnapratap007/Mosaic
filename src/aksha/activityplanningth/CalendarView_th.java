package aksha.activityplanningth;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;

import aksha.adapters.CalendarAdapter;
import aksha.adapters.Myspinner;
import aksha.connectiondetector.ConnectionDetector;
import aksha.database.Databaseutill;
import aksha.database.GetData;
import com.aksha.mosaic.R;
import aksha.plannedactivity.MOPlannedActivities;
import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class CalendarView_th extends Fragment {

	public GregorianCalendar month, itemmonth;// calendar instances.

	public CalendarAdapter adapter;// adapter instance
	public Handler handler;// for grabbing some event values for showing the dot
							// marker.
	public ArrayList<String> items; // container to store calendar items which
									// needs showing the event marker
	private ArrayList<HashMap<String, String>> data;
	private GetData get;
	private Databaseutill db;
	private String fdate,ldate,districtid;
	private View root;
	private GridView gridview;
	private String sfaid;
	Spinner mdoSpinner;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		
  try{
		root = inflater.inflate(R.layout.calender_th, container, false);		
		db = Databaseutill.getDBAdapterInstance(getActivity());
		get = new GetData(getActivity(),db);
		
		
		
		}
		catch(Exception e){
			e.getMessage();
		}
		////////////////////////////////////////////////////
		
		db = Databaseutill.getDBAdapterInstance(getActivity());
		get = new GetData(getActivity(), db);
														
			
		
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
					final Spinner actv=(Spinner)root.findViewById(R.id.auto_buy);
						
						actv.setTag(demo);
						adap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						actv.setAdapter(adap);
						actv.setOnItemSelectedListener(new OnItemSelectedListener() {

							@Override
							public void onItemSelected(AdapterView<?> parent,
									View view, int position, long id) {
	                        	 
                              
                                         Myspinner sp=(Myspinner)actv.getSelectedItem();
                                         if(!sp.getValue().equalsIgnoreCase("0"))
                                         {
                                        	gridview = (GridView) root.findViewById(R.id.gridviewc);
                                            gridview.setVisibility(View.VISIBLE);
                                      		Locale.setDefault( Locale.US );
                                      		month = (GregorianCalendar) GregorianCalendar.getInstance();
                                      		itemmonth = (GregorianCalendar) month.clone();
                                         	 
                                         	 DrawCalender();
                                         	 
			                 // Perform action on click
			         		if(new ConnectionDetector(getActivity()).isConnectingToInternet())
			          	  	{
			        			Calendar cal = Calendar.getInstance();
			        			cal.set(5, 1);
			        			Date localDate1 = cal.getTime();
			        			cal.set(5, cal.getActualMaximum(5));
			        			Date localDate2 = cal.getTime();
			        			SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
			        			fdate = localSimpleDateFormat.format(localDate1);
			        			ldate = localSimpleDateFormat.format(localDate2);
			                	sfaid=sp.getValue();		
			        			new GetActivities().execute(fdate,ldate,sp.getValue(),districtid,new SimpleDateFormat("MM", Locale.US).format(localDate2));
			          	  	}
			        		else
			        		{
			        			sfaid="0";
			        			Toast.makeText(getActivity(), "No Network Found", Toast.LENGTH_LONG).show();
			        		}
                                         }
                                         else{
                                        	 Toast.makeText(getActivity(), "Please select MDO", Toast.LENGTH_LONG).show();
                                        	 
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
	
	
    int nextmcount=0;
	protected void setNextMonth() {
		
		nextmcount++;
		if (month.get(GregorianCalendar.MONTH) == month.getActualMaximum(GregorianCalendar.MONTH)) {
			month.set((month.get(GregorianCalendar.YEAR) + 1),month.getActualMinimum(GregorianCalendar.MONTH), 1);
	    }
		else {
			month.set(GregorianCalendar.MONTH,month.get(GregorianCalendar.MONTH) + 1);
		}
		Date dt = month.getTime();
		SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
		fdate = dft.format(dt);
		String str[] = fdate.split("-");
		int numberOfDays = month.getActualMaximum(month.DAY_OF_MONTH);
		ldate = str[0]+"-"+str[1]+"-"+String.valueOf(numberOfDays);
		if(new ConnectionDetector(getActivity()).isConnectingToInternet())
  	  	{
			new GetActivities().execute(fdate,ldate,sfaid,districtid,str[1]);
  	  	}
		else
		{
			Toast.makeText(getActivity(), "No Network Found", Toast.LENGTH_LONG).show();
		}
	}

	protected void setPreviousMonth() {
		nextmcount--;
		if (month.get(GregorianCalendar.MONTH) == month.getActualMinimum(GregorianCalendar.MONTH)) {
			
			month.set((month.get(GregorianCalendar.YEAR) - 1),month.getActualMaximum(GregorianCalendar.MONTH), 1);
		} 
		
		else {
	    	 
			month.set(GregorianCalendar.MONTH, month.get(GregorianCalendar.MONTH) - 1);
	    
		}
		
		Date dt = month.getTime();
		SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
		fdate = dft.format(dt);
		String str[] = fdate.split("-");
		int numberOfDays = month.getMaximum(month.DAY_OF_MONTH);
		ldate = str[0]+"-"+str[1]+"-"+String.valueOf(numberOfDays);		
		if(new ConnectionDetector(getActivity()).isConnectingToInternet())
  	  	{
			new GetActivities().execute(fdate,ldate,sfaid,districtid,str[1]);
  	  	}
		else
		{
			Toast.makeText(getActivity(), "No Network Found", Toast.LENGTH_LONG).show();
		}

	}

	protected void showToast(String string) {
		Toast.makeText(getActivity(), string, Toast.LENGTH_SHORT).show();

	}

	public void refreshCalendar() {
		TextView title = (TextView) root.findViewById(R.id.title);

		adapter.refreshDays();
		adapter.notifyDataSetChanged();
		handler.post(calendarUpdater); // generate some calendar items

		title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));
	}

	public Runnable calendarUpdater = new Runnable() {

		@Override
		public void run() {
			items.clear();

			// Print dates of the current week
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd",Locale.US);
			String itemvalue;
			for (int i = 0; i < 7; i++) {
				itemvalue = df.format(itemmonth.getTime());
				itemmonth.add(GregorianCalendar.DATE, 1);
				/*items.add("2015-02-12");
				items.add("2015-02-07");
				items.add("2015-02-15");
				items.add("2015-02-20");
				items.add("2015-02-01");
				items.add("2015-02-28");*/
				
			}
			
			try{
				if(data!=null && data.size()>0){
					SimpleDateFormat stringdate = new SimpleDateFormat("M/dd/yyyy HH:mm:ss aa");
					for (HashMap<String, String> hashMap : data) {
						try{								
							Date dt = stringdate.parse(hashMap.get("plan_date"));
							String date = new SimpleDateFormat("yyyy-MM-dd").format(dt);
							items.add(date);
						}
						catch(Exception e){
							e.getMessage();
						}
					}
				}
			}
			catch(Exception e){
				e.getMessage();
			}

			adapter.setItems(items);
			adapter.notifyDataSetChanged();
		}
	};
	
	
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
			data = new ArrayList<HashMap<String,String>>();
			try{
				data = get.getCalendarActivities(params[0], params[1], params[2], params[3],params[4]);
			}
			catch(Exception e){
				e.getMessage();
				data.clear();
			}			
			return data;			
		}
		
		@Override
		protected void onPostExecute(final ArrayList<HashMap<String, String>> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			try{
				if(result!=null && result.size()>0){									
					DrawCalender();
					
					gridview.setOnItemClickListener(new OnItemClickListener() {
						public void onItemClick(AdapterView<?> parent, View v,
								int position, long id) {

							((CalendarAdapter) parent.getAdapter()).setSelected(v);
							String selectedGridDate = CalendarAdapter.dayString
									.get(position);
							String[] separatedTime = selectedGridDate.split("-");
							String gridvalueString = separatedTime[2].replaceFirst("^0*",
									"");// taking last part of date. ie; 2 from 2012-12-02.
							//showToast(selectedGridDate);
							
							// call new class
							Intent in=new Intent(getActivity(), MOPlannedActivities.class);	
							
							String mdo_id = "";
							Spinner actv=(Spinner)root.findViewById(R.id.auto_buy);		               										               
			                Myspinner sp=(Myspinner)actv.getSelectedItem();
			                mdo_id = sp.getValue();		
			                
						    in.putExtra("mdoid", mdo_id);
						    in.putExtra("mdoname", sp.getSpinnerText());
						    in.putExtra("currentdate", selectedGridDate);
							startActivityForResult(in, 1);

						}
					});
				}				
			}
			catch(Exception e){
				e.getMessage();
			}
			pDialog.dismiss();
		}
		
	}

	private void DrawCalender(){
		
		adapter = new CalendarAdapter(getActivity(), month);
		items = new ArrayList<String>();
		
		gridview.setAdapter(adapter);

		handler = new Handler();
		handler.post(calendarUpdater);

		TextView title = (TextView) root.findViewById(R.id.title);
		title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));

		RelativeLayout previous = (RelativeLayout) root.findViewById(R.id.previous);

		previous.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			
			if(nextmcount>0)
			{
				setPreviousMonth();
				refreshCalendar();	
			 }
		   }
		});

		RelativeLayout next = (RelativeLayout) root.findViewById(R.id.next);
		next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setNextMonth();
				refreshCalendar();

			}
		});

		gridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {

				((CalendarAdapter) parent.getAdapter()).setSelected(v);
				String selectedGridDate = CalendarAdapter.dayString
						.get(position);
				String[] separatedTime = selectedGridDate.split("-");
				String gridvalueString = separatedTime[2].replaceFirst("^0*",
						"");// taking last part of date. ie; 2 from 2012-12-02.
				int gridvalue = Integer.parseInt(gridvalueString);
				// navigate to next or previous month on clicking offdays.
				if ((gridvalue > 10) && (position < 8)) {
					setPreviousMonth();
					refreshCalendar();
				} else if ((gridvalue < 7) && (position > 28)) {
					setNextMonth();
					refreshCalendar();
				}
				((CalendarAdapter) parent.getAdapter()).setSelected(v);
				//showToast(selectedGridDate);
				
				String mdo_id = "";
				Spinner actv=(Spinner)root.findViewById(R.id.auto_buy);
                //String txt= String.valueOf(actv.getText());
				//AdapterSearch ad=(AdapterSearch)actv.getAdapter();
               
                
				
                
                        Myspinner sp=(Myspinner)actv.getSelectedItem();
                        mdo_id = sp.getValue();
                
                        
                //String mdo_id = String.valueOf(actv);
                
				
				Intent in=new Intent(getActivity(), MOPlannedActivities.class);
				
			    in.putExtra("mdoid", mdo_id);
			    in.putExtra("mdoname", sp.getSpinnerText());
			    in.putExtra("currentdate", selectedGridDate);
				startActivityForResult(in, 1);
				

				
			}
		});
	}
	

/*class Calender extends AsyncTask<String, Void, ArrayList<String>>
{
	ProgressDialog pDialog;

	@Override
	protected ArrayList<String> doInBackground(String... arg0) {
		
		return null;
	}
	
	@Override
	  protected void onPreExecute()
	    {
	      
	      pDialog = new ProgressDialog(getActivity());
	      try
	      {
	        this.pDialog.setMessage("Please wait ...");
	        this.pDialog.setIndeterminateDrawable(getActivity().getResources().getDrawable(R.drawable.red_progress));
	        this.pDialog.setIndeterminate(false);
	        this.pDialog.setCancelable(false);
	        this.pDialog.show();
	        
	      }
	      catch (Exception localException)
	      {
	        localException.getMessage();
	      }
	    }
	@Override
	 protected void onPostExecute(ArrayList<String> paramArrayList)
	    {
	      super.onPostExecute(paramArrayList);
	
	    }
	
	
}*/
@Override
public void onActivityResult(int requestCode, int resultCode, Intent data) {

	
try{
	Spinner actv=(Spinner)root.findViewById(R.id.auto_buy);
	 Myspinner sp=(Myspinner)actv.getSelectedItem();
     if(!sp.getValue().equalsIgnoreCase("0"))
     {
    	gridview = (GridView) root.findViewById(R.id.gridviewc);
        gridview.setVisibility(View.VISIBLE);
  		Locale.setDefault( Locale.US );
  		month = (GregorianCalendar) GregorianCalendar.getInstance();
  		itemmonth = (GregorianCalendar) month.clone();
     	 
     	 DrawCalender();
     	 
// Perform action on click
if(new ConnectionDetector(getActivity()).isConnectingToInternet())
{
Calendar cal = Calendar.getInstance();
cal.set(5, 1);
Date localDate1 = cal.getTime();
cal.set(5, cal.getActualMaximum(5));
Date localDate2 = cal.getTime();
SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
fdate = localSimpleDateFormat.format(localDate1);
ldate = localSimpleDateFormat.format(localDate2);
sfaid=sp.getValue();		
new GetActivities().execute(fdate,ldate,sp.getValue(),districtid,new SimpleDateFormat("MM", Locale.US).format(localDate2));
}
else
{
sfaid="0";
Toast.makeText(getActivity(), "No Network Found", Toast.LENGTH_LONG).show();
}
     }	
}catch(Exception er){er.getMessage();}
	
	

}	
	
	
}
	

