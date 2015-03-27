package aksha.calender;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;

import aksha.adapters.CalendarAdapter;
import aksha.adapters.CalenderActivityListAdapter;
import aksha.adapters.LazyAdapter;
import aksha.adapters.Myspinner;
import aksha.adapters.PendingActivityListAdapter;
import aksha.connectiondetector.ConnectionDetector;
import aksha.database.Databaseutill;
import aksha.database.GetData;
import com.aksha.mosaic.R;
import aksha.webservice.Webservicerequest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class CalendarView extends Fragment {

	public GregorianCalendar month, itemmonth;// calendar instances.

	public CalendarAdapter adapter;// adapter instance
	public Handler handler;// for grabbing some event values for showing the dot
							// marker.
	public ArrayList<String> items; // container to store calendar items which
									// needs showing the event marker
	private ArrayList<HashMap<String, String>> data;
	private GetData get;
	private Databaseutill db;
	private String fdate,ldate,empid,districtid;
	private View root;
	private GridView gridview;
	private String encryptedgeoid;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		root = inflater.inflate(R.layout.calendar, container, false);
		
		db = Databaseutill.getDBAdapterInstance(getActivity());
		get = new GetData(getActivity(), db);
		
		gridview = (GridView) root.findViewById(R.id.gridviewc);
		Locale.setDefault( Locale.US );
		month = (GregorianCalendar) GregorianCalendar.getInstance();
		itemmonth = (GregorianCalendar) month.clone();			
		DrawCalender();
		
		try{
			ArrayList<HashMap<String, String>> logindata = get.getAllLogin();
			empid = logindata.get(0).get("empid");
			districtid = logindata.get(0).get("geoid");
		}
		catch(Exception e){
			e.getMessage();
		}
		
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
			new GetActivities().execute(fdate,ldate,empid,districtid);
  	  	}
		else
		{
			Toast.makeText(getActivity(), "No Network Found", Toast.LENGTH_LONG).show();
		}
		
		return root;
	}

	protected void setNextMonth() {
		if (month.get(GregorianCalendar.MONTH) == month
				.getActualMaximum(GregorianCalendar.MONTH)) {
			month.set((month.get(GregorianCalendar.YEAR) + 1),
					month.getActualMinimum(GregorianCalendar.MONTH), 1);
		} else {
			month.set(GregorianCalendar.MONTH,
					month.get(GregorianCalendar.MONTH) + 1);
		}
		Date dt = month.getTime();
		SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
		fdate = dft.format(dt);
		String str[] = fdate.split("-");
		int numberOfDays = month.getActualMaximum(month.DAY_OF_MONTH);
		ldate = str[0]+"-"+str[1]+"-"+String.valueOf(numberOfDays);
		if(new ConnectionDetector(getActivity()).isConnectingToInternet())
  	  	{
			new GetActivities().execute(fdate,ldate,empid,districtid);
  	  	}
		else
		{
			Toast.makeText(getActivity(), "No Network Found", Toast.LENGTH_LONG).show();
		}
	}

	protected void setPreviousMonth() {
		if (month.get(GregorianCalendar.MONTH) == month
				.getActualMinimum(GregorianCalendar.MONTH)) {
			month.set((month.get(GregorianCalendar.YEAR) - 1),
					month.getActualMaximum(GregorianCalendar.MONTH), 1);
		} else {
			month.set(GregorianCalendar.MONTH,
					month.get(GregorianCalendar.MONTH) - 1);
		}
		
		Date dt = month.getTime();
		SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
		fdate = dft.format(dt);
		String str[] = fdate.split("-");
		int numberOfDays = month.getMaximum(month.DAY_OF_MONTH);
		ldate = str[0]+"-"+str[1]+"-"+String.valueOf(numberOfDays);		
		if(new ConnectionDetector(getActivity()).isConnectingToInternet())
  	  	{
			new GetActivities().execute(fdate,ldate,empid,districtid);
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
			// TODO Auto-generated method stub
			super.onPreExecute();
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
				data = get.getCalendarActivities(params[0], params[1], params[2], params[3]);
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
			pDialog.dismiss();
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
							if(items.contains(selectedGridDate)){
								try{
									//3/10/2015 12:00:00 AM
									SimpleDateFormat stringdate = new SimpleDateFormat("M/dd/yyyy HH:mm:ss aa");
									ArrayList<HashMap<String, String>> listdata = new ArrayList<HashMap<String,String>>();
									for (HashMap<String, String> hashMap : data) {
										if(new SimpleDateFormat("yyyy-MM-dd").format(stringdate.parse(hashMap.get("plan_date"))).equalsIgnoreCase(selectedGridDate)){
											listdata.add(hashMap);
										}
									}
									Date dt = new SimpleDateFormat("yyyy-MM-dd").parse(selectedGridDate);
									String date = new SimpleDateFormat("d MMMM yyyy").format(dt);
									LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
									View layout = inflater.inflate(R.layout.popuplistview, null);
									
									ListView lv = (ListView)layout.findViewById(R.id.popuplist);
									
									
									// call calender adapter on grid click 
									lv.setAdapter(new CalenderActivityListAdapter(getActivity(), listdata));
									
									AlertDialog.Builder build = new Builder(getActivity())
									.setTitle(date)
									.setView(layout)
									.setNegativeButton("Back", new DialogInterface.OnClickListener() {
										
										@Override
										public void onClick(DialogInterface dialog, int which) {
											// TODO Auto-generated method stub
											dialog.cancel();
										}
									});
									
									final Date selecteddate = new SimpleDateFormat("yyyy-MM-dd").parse(selectedGridDate);
									Date now = new SimpleDateFormat("yyyy-MM-dd").parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
								//	selecteddate.equals(now) || selecteddate.after(now)
									if(false){
										build.setPositiveButton("Create Activity", new DialogInterface.OnClickListener() {
											
											@Override
											public void onClick(DialogInterface dialog, int which) {
												// TODO Auto-generated method stub

												
												LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
												View activity = inflater.inflate(R.layout.createactivity, null);
												
												final Spinner districtlist = (Spinner)activity.findViewById(R.id.district);
												final Spinner mandilist = (Spinner)activity.findViewById(R.id.mandi);
												final Spinner retailerslist = (Spinner)activity.findViewById(R.id.retailers);
												final Spinner activitieslist = (Spinner)activity.findViewById(R.id.activitieslist);
												final EditText remarks = (EditText)activity.findViewById(R.id.remarks);				
												final AutoCompleteTextView villages = (AutoCompleteTextView)activity.findViewById(R.id.autovillage);
												
												ArrayList<HashMap<String, String>> category1=new ArrayList<HashMap<String,String>>();
												HashMap<String, String> map1=new HashMap<String, String>();
								 				map1.put("1","");
								 				map1.put("2","Select District");
								 				category1.add(map1);
								 				
								 				final ArrayList<HashMap<String, String>> category2=new ArrayList<HashMap<String,String>>();
												HashMap<String, String> map2=new HashMap<String, String>();
								 				map2.put("1","");
								 				map2.put("2","Select Mandi");
								 				category2.add(map2);
								 				
								 				final ArrayList<HashMap<String, String>> category3=new ArrayList<HashMap<String,String>>();
												HashMap<String, String> map3=new HashMap<String, String>();
								 				map3.put("1","");
								 				map3.put("2","Select Retailer");
								 				category3.add(map3);
												
								 				final ArrayList<HashMap<String, String>> category4=new ArrayList<HashMap<String,String>>();
												HashMap<String, String> map4=new HashMap<String, String>();
								 				map4.put("1","");
								 				map4.put("2","Select Activity");
								 				category4.add(map4);
								 				
												try{
													ArrayList<HashMap<String, String>> dist = new GetData(getActivity(), db).getDistrict();								
													for (int i=0; i<dist.size(); i++) {
														HashMap<String, String> map = new HashMap<String, String>();
														map.put("1",dist.get(i).get("Districtid"));
										 				map.put("2",dist.get(i).get("Districtname"));
										 				category1.add(map);		 				
													}
													category4.addAll(get.getActivity());
													
												}
												catch(Exception e){
													e.getMessage();
												}								
								 				
								 				Myspinner[] redemo1 = new Myspinner[category1.size()];
								 				for(int i=0; i<category1.size(); i++){
								 					redemo1[i] = new Myspinner(category1.get(i).get("2"), category1.get(i).get("1"), "");
								 				}
								 				ArrayAdapter<Myspinner> adapter1 = new ArrayAdapter<Myspinner>(getActivity(), android.R.layout.simple_spinner_dropdown_item, redemo1);
								 				districtlist.setAdapter(adapter1);
								 				
								 				districtlist.setOnItemSelectedListener(new OnItemSelectedListener() {

													@Override
													public void onItemSelected(AdapterView<?> parent,
															View view, int position, long id) {
														// TODO Auto-generated method stub
														if(position!=0)
														{
															Myspinner sp = (Myspinner)districtlist.getSelectedItem();
															try{
																encryptedgeoid = new Webservicerequest().Encrypt(sp.getValue());
															}
															catch(Exception e){
																e.getMessage();
															}
															category2.addAll(get.getMandi(encryptedgeoid));	
															Myspinner[] redemo2 = new Myspinner[category2.size()];
											 				for(int i=0; i<category2.size(); i++){
											 					redemo2[i] = new Myspinner(category2.get(i).get("2"), category2.get(i).get("1"), "");
											 				}
											 				ArrayAdapter<Myspinner> adapter2 = new ArrayAdapter<Myspinner>(getActivity(), android.R.layout.simple_spinner_dropdown_item, redemo2);
											 				mandilist.setAdapter(adapter2);
														}
														else{
															ArrayList<HashMap<String,String>> category2=new ArrayList<HashMap<String,String>>();
															HashMap<String, String> map2=new HashMap<String, String>();
											 				map2.put("1","");
											 				map2.put("2","Select Mandi");
											 				category2.add(map2);
															Myspinner[] redemo2 = new Myspinner[category2.size()];
											 				for(int i=0; i<category2.size(); i++){
											 					redemo2[i] = new Myspinner(category2.get(i).get("2"), category2.get(i).get("1"), "");
											 				}
											 				ArrayAdapter<Myspinner> adapter2 = new ArrayAdapter<Myspinner>(getActivity(), android.R.layout.simple_spinner_dropdown_item, redemo2);
											 				mandilist.setAdapter(adapter2);
														}
													}

													@Override
													public void onNothingSelected(AdapterView<?> parent) {
														// TODO Auto-generated method stub
														
													}
												});
								 				
								 				mandilist.setOnItemSelectedListener(new OnItemSelectedListener() {

													@Override
													public void onItemSelected(AdapterView<?> parent,
															View view, int position, long id) {
														// TODO Auto-generated method stub
														if(position!=0)
														{
															Myspinner sp = (Myspinner)mandilist.getSelectedItem();
															try{
																encryptedgeoid = new Webservicerequest().Encrypt(sp.getValue());
															}
															catch(Exception e){
																e.getMessage();
															}
															category3.addAll(get.getretailersList(encryptedgeoid));	
															Myspinner[] redemo3 = new Myspinner[category3.size()];
											 				for(int i=0; i<category3.size(); i++){
											 					redemo3[i] = new Myspinner(category3.get(i).get("2"), category3.get(i).get("1"), "");
											 				}
											 				ArrayAdapter<Myspinner> adapter3 = new ArrayAdapter<Myspinner>(getActivity(), android.R.layout.simple_spinner_dropdown_item, redemo3);
											 				retailerslist.setAdapter(adapter3); 				 	
											 				ArrayAdapter<String> adaptervillages = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, get.getVillageNames(encryptedgeoid));
											 				villages.setAdapter(adaptervillages);
															villages.setThreshold(3);
															villages.setOnItemClickListener(new OnItemClickListener() {

																  @Override
																  public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
																    InputMethodManager in = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
																    in.hideSoftInputFromWindow(villages.getWindowToken(), 0);
																  }

															});
														}
														else{
															villages.setAdapter(null);
															ArrayList<HashMap<String, String>> category3=new ArrayList<HashMap<String,String>>();
															HashMap<String, String> map3=new HashMap<String, String>();
											 				map3.put("1","");
											 				map3.put("2","Select Retailer");
											 				category3.add(map3);
											 				Myspinner[] redemo3 = new Myspinner[category3.size()];
											 				for(int i=0; i<category3.size(); i++){
											 					redemo3[i] = new Myspinner(category3.get(i).get("2"), category3.get(i).get("1"), "");
											 				}
											 				ArrayAdapter<Myspinner> adapter3 = new ArrayAdapter<Myspinner>(getActivity(), android.R.layout.simple_spinner_dropdown_item, redemo3);
											 				retailerslist.setAdapter(adapter3); 				 	
														}
													}

													@Override
													public void onNothingSelected(AdapterView<?> parent) {
														// TODO Auto-generated method stub
														
													}
												});
								 				
								 				Myspinner[] redemo2 = new Myspinner[category2.size()];
								 				for(int i=0; i<category2.size(); i++){
								 					redemo2[i] = new Myspinner(category2.get(i).get("2"), category2.get(i).get("1"), "");
								 				}
								 				ArrayAdapter<Myspinner> adapter2 = new ArrayAdapter<Myspinner>(getActivity(), android.R.layout.simple_spinner_dropdown_item, redemo2);
								 				mandilist.setAdapter(adapter2); 				 			
								 				
								 				Myspinner[] redemo3 = new Myspinner[category3.size()];
								 				for(int i=0; i<category3.size(); i++){
								 					redemo3[i] = new Myspinner(category3.get(i).get("2"), category3.get(i).get("1"), "");
								 				}
								 				ArrayAdapter<Myspinner> adapter3 = new ArrayAdapter<Myspinner>(getActivity(), android.R.layout.simple_spinner_dropdown_item, redemo3);
								 				retailerslist.setAdapter(adapter3); 				 				
								 				
								 				Myspinner[] redemo4 = new Myspinner[category4.size()];
								 				for(int i=0; i<category4.size(); i++){
								 					redemo4[i] = new Myspinner(category4.get(i).get("2"), category4.get(i).get("1"), "");
								 				}
								 				ArrayAdapter<Myspinner> adapter4 = new ArrayAdapter<Myspinner>(getActivity(), android.R.layout.simple_spinner_dropdown_item, redemo4);
								 				activitieslist.setAdapter(adapter4);
												
												AlertDialog.Builder build1 = new Builder(getActivity())
												.setTitle(""+new SimpleDateFormat("d MMM yyyy").format(selecteddate))
												.setView(activity)
												.setPositiveButton("Create", null)
												.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
													
													@Override
													public void onClick(DialogInterface dialog, int which) {
														// TODO Auto-generated method stub
														dialog.cancel();
													}
												});
												final AlertDialog alert1 = build1.create();
												alert1.setOnShowListener(new OnShowListener() {
													
													@Override
													public void onShow(DialogInterface dialog) {
														// TODO Auto-generated method stub
														Button b = alert1.getButton(AlertDialog.BUTTON_POSITIVE);
														b.setOnClickListener(new OnClickListener() {
															
															@Override
															public void onClick(View v) {
																// TODO Auto-generated method stub
																//dist,date,empId,spmandi,spretailer,spactivity,rem
																if(new ConnectionDetector(getActivity()).isConnectingToInternet()){
																	Myspinner sp1 = (Myspinner)districtlist.getSelectedItem();
																	Myspinner sp2 = (Myspinner)mandilist.getSelectedItem();
																	Myspinner sp3 = (Myspinner)retailerslist.getSelectedItem();
																	Myspinner sp4 = (Myspinner)activitieslist.getSelectedItem();
																	if(districtlist.getSelectedItemPosition()!=0
																			&& mandilist.getSelectedItemPosition()!=0
																				&& retailerslist.getSelectedItemPosition()!=0
																					&& activitieslist.getSelectedItemPosition()!=0){
																		try{
																			String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
																			ArrayList<String> id = new GetData(getActivity(), db).getVillageId(villages.getText().toString());
																			if(id.size()>0 && id.get(0).length()>0){
																				new CreateActivity().execute(sp1.getValue(),new SimpleDateFormat("yyyy-MM-dd").format(selecteddate),get.getAllLogin().get(0).get("empid"),sp2.getValue(),sp3.getValue(),sp4.getValue(),remarks.getText().toString(), id.get(0), villages.getText().toString());
																			}
																			else{
																				new CreateActivity().execute(sp1.getValue(),new SimpleDateFormat("yyyy-MM-dd").format(selecteddate),get.getAllLogin().get(0).get("empid"),sp2.getValue(),sp3.getValue(),sp4.getValue(),remarks.getText().toString(), "0", villages.getText().toString());
																			}
																			alert1.dismiss();
																		}
																		catch(Exception e){
																			e.getMessage();
																		}
																	}
																	else{
																		if(districtlist.getSelectedItemPosition()==0){
																			Toast.makeText(getActivity(), "Please Select District", Toast.LENGTH_LONG).show();
																		}
																		else{
																			if(mandilist.getSelectedItemPosition()==0){
																				Toast.makeText(getActivity(), "Please Select Mandi", Toast.LENGTH_LONG).show();
																			}
																			else{
																				if(retailerslist.getSelectedItemPosition()==0){
																					Toast.makeText(getActivity(), "Please Select Retailer", Toast.LENGTH_LONG).show();
																				}
																				else{
																					if(activitieslist.getSelectedItemPosition()==0){
																						Toast.makeText(getActivity(), "Please Select Activity", Toast.LENGTH_LONG).show();
																					}
																				}
																			}
																		}
																	}
																}
																else{
																	Toast.makeText(getActivity(), "No Network Found", Toast.LENGTH_LONG).show();
																}
															
															}
														});
													}
												});
												alert1.show();					
											
											}
										});
									}
									
									AlertDialog alert = build.create();
									alert.show();
																		
								}
								catch(Exception e){
									e.getMessage();
								}
							}
							else{
								
								Toast.makeText(getActivity(), "No Data Found", Toast.LENGTH_LONG).show();
								
								
								/*
								//showToast(selectedGridDate);
								try{

									final Date selecteddate = new SimpleDateFormat("yyyy-MM-dd").parse(selectedGridDate);
									Date now = new SimpleDateFormat("yyyy-MM-dd").parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
									if(selecteddate.equals(now) || selecteddate.after(now)){
										
										LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
										View activity = inflater.inflate(R.layout.createactivity, null);
										
										final Spinner districtlist = (Spinner)activity.findViewById(R.id.district);
										final Spinner mandilist = (Spinner)activity.findViewById(R.id.mandi);
										final Spinner retailerslist = (Spinner)activity.findViewById(R.id.retailers);
										final Spinner activitieslist = (Spinner)activity.findViewById(R.id.activitieslist);
										final EditText remarks = (EditText)activity.findViewById(R.id.remarks);				
										final AutoCompleteTextView villages = (AutoCompleteTextView)activity.findViewById(R.id.autovillage);
										
										ArrayList<HashMap<String, String>> category1=new ArrayList<HashMap<String,String>>();
										HashMap<String, String> map1=new HashMap<String, String>();
						 				map1.put("1","");
						 				map1.put("2","Select District");
						 				category1.add(map1);
						 				
						 				final ArrayList<HashMap<String, String>> category2=new ArrayList<HashMap<String,String>>();
										HashMap<String, String> map2=new HashMap<String, String>();
						 				map2.put("1","");
						 				map2.put("2","Select Mandi");
						 				category2.add(map2);
						 				
						 				final ArrayList<HashMap<String, String>> category3=new ArrayList<HashMap<String,String>>();
										HashMap<String, String> map3=new HashMap<String, String>();
						 				map3.put("1","");
						 				map3.put("2","Select Retailer");
						 				category3.add(map3);
										
						 				final ArrayList<HashMap<String, String>> category4=new ArrayList<HashMap<String,String>>();
										HashMap<String, String> map4=new HashMap<String, String>();
						 				map4.put("1","");
						 				map4.put("2","Select Activity");
						 				category4.add(map4);
						 				
										try{
											ArrayList<HashMap<String, String>> dist = new GetData(getActivity(), db).getDistrict();								
											for (int i=0; i<dist.size(); i++) {
												HashMap<String, String> map = new HashMap<String, String>();
												map.put("1",dist.get(i).get("Districtid"));
								 				map.put("2",dist.get(i).get("Districtname"));
								 				category1.add(map);		 				
											}
											category4.addAll(get.getActivity());
											
										}
										catch(Exception e){
											e.getMessage();
										}								
						 				
						 				Myspinner[] redemo1 = new Myspinner[category1.size()];
						 				for(int i=0; i<category1.size(); i++){
						 					redemo1[i] = new Myspinner(category1.get(i).get("2"), category1.get(i).get("1"), "");
						 				}
						 				ArrayAdapter<Myspinner> adapter1 = new ArrayAdapter<Myspinner>(getActivity(), android.R.layout.simple_spinner_dropdown_item, redemo1);
						 				districtlist.setAdapter(adapter1);
						 				
						 				districtlist.setOnItemSelectedListener(new OnItemSelectedListener() {

											@Override
											public void onItemSelected(AdapterView<?> parent,
													View view, int position, long id) {
												// TODO Auto-generated method stub
												if(position!=0)
												{
													Myspinner sp = (Myspinner)districtlist.getSelectedItem();
													try{
														encryptedgeoid = new Webservicerequest().Encrypt(sp.getValue());
													}
													catch(Exception e){
														e.getMessage();
													}
													category2.addAll(get.getMandi(encryptedgeoid));	
													Myspinner[] redemo2 = new Myspinner[category2.size()];
									 				for(int i=0; i<category2.size(); i++){
									 					redemo2[i] = new Myspinner(category2.get(i).get("2"), category2.get(i).get("1"), "");
									 				}
									 				ArrayAdapter<Myspinner> adapter2 = new ArrayAdapter<Myspinner>(getActivity(), android.R.layout.simple_spinner_dropdown_item, redemo2);
									 				mandilist.setAdapter(adapter2);
												}
												else{
													ArrayList<HashMap<String,String>> category2=new ArrayList<HashMap<String,String>>();
													HashMap<String, String> map2=new HashMap<String, String>();
									 				map2.put("1","");
									 				map2.put("2","Select Mandi");
									 				category2.add(map2);
													Myspinner[] redemo2 = new Myspinner[category2.size()];
									 				for(int i=0; i<category2.size(); i++){
									 					redemo2[i] = new Myspinner(category2.get(i).get("2"), category2.get(i).get("1"), "");
									 				}
									 				ArrayAdapter<Myspinner> adapter2 = new ArrayAdapter<Myspinner>(getActivity(), android.R.layout.simple_spinner_dropdown_item, redemo2);
									 				mandilist.setAdapter(adapter2);
												}
											}

											@Override
											public void onNothingSelected(AdapterView<?> parent) {
												// TODO Auto-generated method stub
												
											}
										});
						 				
						 				mandilist.setOnItemSelectedListener(new OnItemSelectedListener() {

											@Override
											public void onItemSelected(AdapterView<?> parent,
													View view, int position, long id) {
												// TODO Auto-generated method stub
												if(position!=0)
												{
													Myspinner sp = (Myspinner)mandilist.getSelectedItem();
													try{
														encryptedgeoid = new Webservicerequest().Encrypt(sp.getValue());
													}
													catch(Exception e){
														e.getMessage();
													}
													category3.addAll(get.getretailersList(encryptedgeoid));	
													Myspinner[] redemo3 = new Myspinner[category3.size()];
									 				for(int i=0; i<category3.size(); i++){
									 					redemo3[i] = new Myspinner(category3.get(i).get("2"), category3.get(i).get("1"), "");
									 				}
									 				ArrayAdapter<Myspinner> adapter3 = new ArrayAdapter<Myspinner>(getActivity(), android.R.layout.simple_spinner_dropdown_item, redemo3);
									 				retailerslist.setAdapter(adapter3); 				 	
												}
												else{
													ArrayList<HashMap<String, String>> category3=new ArrayList<HashMap<String,String>>();
													HashMap<String, String> map3=new HashMap<String, String>();
									 				map3.put("1","");
									 				map3.put("2","Select Retailer");
									 				category3.add(map3);
									 				Myspinner[] redemo3 = new Myspinner[category3.size()];
									 				for(int i=0; i<category3.size(); i++){
									 					redemo3[i] = new Myspinner(category3.get(i).get("2"), category3.get(i).get("1"), "");
									 				}
									 				ArrayAdapter<Myspinner> adapter3 = new ArrayAdapter<Myspinner>(getActivity(), android.R.layout.simple_spinner_dropdown_item, redemo3);
									 				retailerslist.setAdapter(adapter3); 				 	
												}
											}

											@Override
											public void onNothingSelected(AdapterView<?> parent) {
												// TODO Auto-generated method stub
												
											}
										});
						 				
						 				Myspinner[] redemo2 = new Myspinner[category2.size()];
						 				for(int i=0; i<category2.size(); i++){
						 					redemo2[i] = new Myspinner(category2.get(i).get("2"), category2.get(i).get("1"), "");
						 				}
						 				ArrayAdapter<Myspinner> adapter2 = new ArrayAdapter<Myspinner>(getActivity(), android.R.layout.simple_spinner_dropdown_item, redemo2);
						 				mandilist.setAdapter(adapter2); 				 			
						 				
						 				Myspinner[] redemo3 = new Myspinner[category3.size()];
						 				for(int i=0; i<category3.size(); i++){
						 					redemo3[i] = new Myspinner(category3.get(i).get("2"), category3.get(i).get("1"), "");
						 				}
						 				ArrayAdapter<Myspinner> adapter3 = new ArrayAdapter<Myspinner>(getActivity(), android.R.layout.simple_spinner_dropdown_item, redemo3);
						 				retailerslist.setAdapter(adapter3); 				 				
						 				
						 				Myspinner[] redemo4 = new Myspinner[category4.size()];
						 				for(int i=0; i<category4.size(); i++){
						 					redemo4[i] = new Myspinner(category4.get(i).get("2"), category4.get(i).get("1"), "");
						 				}
						 				ArrayAdapter<Myspinner> adapter4 = new ArrayAdapter<Myspinner>(getActivity(), android.R.layout.simple_spinner_dropdown_item, redemo4);
						 				activitieslist.setAdapter(adapter4);
										
										AlertDialog.Builder build = new Builder(getActivity())
										.setTitle(""+new SimpleDateFormat("d MMM yyyy").format(selecteddate))
										.setView(activity)
										.setPositiveButton("Create",null)
										.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
											
											@Override
											public void onClick(DialogInterface dialog, int which) {
												// TODO Auto-generated method stub
												dialog.cancel();
											}
										});
										final AlertDialog alert = build.create();
										alert.setOnShowListener(new OnShowListener() {
											
											@Override
											public void onShow(DialogInterface dialog) {
												// TODO Auto-generated method stub
												Button b = alert.getButton(AlertDialog.BUTTON_POSITIVE);
												b.setOnClickListener(new OnClickListener() {
													
													@Override
													public void onClick(View v) {
														// TODO Auto-generated method stub
														//dist,date,empId,spmandi,spretailer,spactivity,rem
														if(new ConnectionDetector(getActivity()).isConnectingToInternet()){
															Myspinner sp1 = (Myspinner)districtlist.getSelectedItem();
															Myspinner sp2 = (Myspinner)mandilist.getSelectedItem();
															Myspinner sp3 = (Myspinner)retailerslist.getSelectedItem();
															Myspinner sp4 = (Myspinner)activitieslist.getSelectedItem();
															if(districtlist.getSelectedItemPosition()!=0
																	&& mandilist.getSelectedItemPosition()!=0
																		&& retailerslist.getSelectedItemPosition()!=0
																			&& activitieslist.getSelectedItemPosition()!=0){
																try{
																	String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
																	ArrayList<String> id = new GetData(getActivity(), db).getVillageId(villages.getText().toString());
																	if(id.size()>0 && id.get(0).length()>0){
																		new CreateActivity().execute(sp1.getValue(),new SimpleDateFormat("yyyy-MM-dd").format(selecteddate),get.getAllLogin().get(0).get("empid"),sp2.getValue(),sp3.getValue(),sp4.getValue(),remarks.getText().toString(), id.get(0), villages.getText().toString());
																	}
																	else{
																		new CreateActivity().execute(sp1.getValue(),new SimpleDateFormat("yyyy-MM-dd").format(selecteddate),get.getAllLogin().get(0).get("empid"),sp2.getValue(),sp3.getValue(),sp4.getValue(),remarks.getText().toString(), "0", villages.getText().toString());
																	}
																	alert.dismiss();
																}
																catch(Exception e){
																	e.getMessage();
																}
															}
															else{
																if(districtlist.getSelectedItemPosition()==0){
																	Toast.makeText(getActivity(), "Please Select District", Toast.LENGTH_LONG).show();
																}
																else{
																	if(mandilist.getSelectedItemPosition()==0){
																		Toast.makeText(getActivity(), "Please Select Mandi", Toast.LENGTH_LONG).show();
																	}
																	else{
																		if(retailerslist.getSelectedItemPosition()==0){
																			Toast.makeText(getActivity(), "Please Select Retailer", Toast.LENGTH_LONG).show();
																		}
																		else{
																			if(activitieslist.getSelectedItemPosition()==0){
																				Toast.makeText(getActivity(), "Please Select Activity", Toast.LENGTH_LONG).show();
																			}
																		}
																	}
																}
															}
														}
														else{
															Toast.makeText(getActivity(), "No Network Found", Toast.LENGTH_LONG).show();
														}
													
													}
												});
											}
										});
										alert.show();					
									}
								
								}
								catch(Exception e){
									e.getMessage();
								}
							*/}
						}
					});
				}				
			}
			catch(Exception e){
				e.getMessage();
			}
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
				setPreviousMonth();
				refreshCalendar();
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
				try{
					final Date selecteddate = new SimpleDateFormat("yyyy-MM-dd").parse(selectedGridDate);
					Date now = new SimpleDateFormat("yyyy-MM-dd").parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
					if(selecteddate.equals(now) || selecteddate.after(now)){
						
						LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
						View activity = inflater.inflate(R.layout.createactivity, null);
						
						final Spinner districtlist = (Spinner)activity.findViewById(R.id.district);
						final Spinner mandilist = (Spinner)activity.findViewById(R.id.mandi);
						final Spinner retailerslist = (Spinner)activity.findViewById(R.id.retailers);
						final Spinner activitieslist = (Spinner)activity.findViewById(R.id.activitieslist);
						final EditText remarks = (EditText)activity.findViewById(R.id.remarks);
						final AutoCompleteTextView villages = (AutoCompleteTextView)activity.findViewById(R.id.autovillage);
						
						ArrayList<HashMap<String, String>> category1=new ArrayList<HashMap<String,String>>();
						HashMap<String, String> map1=new HashMap<String, String>();
		 				map1.put("1","");
		 				map1.put("2","Select District");
		 				category1.add(map1);
		 				
		 				final ArrayList<HashMap<String, String>> category2=new ArrayList<HashMap<String,String>>();
						HashMap<String, String> map2=new HashMap<String, String>();
		 				map2.put("1","");
		 				map2.put("2","Select Mandi");
		 				category2.add(map2);
		 				
		 				final ArrayList<HashMap<String, String>> category3=new ArrayList<HashMap<String,String>>();
						HashMap<String, String> map3=new HashMap<String, String>();
		 				map3.put("1","");
		 				map3.put("2","Select Retailer");
		 				category3.add(map3);
						
		 				final ArrayList<HashMap<String, String>> category4=new ArrayList<HashMap<String,String>>();
						HashMap<String, String> map4=new HashMap<String, String>();
		 				map4.put("1","");
		 				map4.put("2","Select Activity");
		 				category4.add(map4);
		 				
						try{
							ArrayList<HashMap<String, String>> data = get.getAllLogin();
							String[] district = data.get(0).get("district").split(",");
							String[] geoid = data.get(0).get("geoid").split(",");					
							for (int i=0; i<district.length; i++) {
								HashMap<String, String> map = new HashMap<String, String>();
								map.put("1",geoid[i]);
				 				map.put("2",district[i]);
				 				category1.add(map);		 				
							}
							
							category4.addAll(get.getActivity());
							
						}
						catch(Exception e){
							e.getMessage();
						}								
		 				
		 				Myspinner[] redemo1 = new Myspinner[category1.size()];
		 				for(int i=0; i<category1.size(); i++){
		 					redemo1[i] = new Myspinner(category1.get(i).get("2"), category1.get(i).get("1"), "");
		 				}
		 				ArrayAdapter<Myspinner> adapter1 = new ArrayAdapter<Myspinner>(getActivity(), android.R.layout.simple_spinner_dropdown_item, redemo1);
		 				districtlist.setAdapter(adapter1);
		 				
		 				districtlist.setOnItemSelectedListener(new OnItemSelectedListener() {

							@Override
							public void onItemSelected(AdapterView<?> parent,
									View view, int position, long id) {
								// TODO Auto-generated method stub
								if(position!=0)
								{
									Myspinner sp = (Myspinner)districtlist.getSelectedItem();
									try{
										encryptedgeoid = new Webservicerequest().Encrypt(sp.getValue());
									}
									catch(Exception e){
										e.getMessage();
									}
									category2.addAll(get.getMandi(encryptedgeoid));	
									Myspinner[] redemo2 = new Myspinner[category2.size()];
					 				for(int i=0; i<category2.size(); i++){
					 					redemo2[i] = new Myspinner(category2.get(i).get("2"), category2.get(i).get("1"), "");
					 				}
					 				ArrayAdapter<Myspinner> adapter2 = new ArrayAdapter<Myspinner>(getActivity(), android.R.layout.simple_spinner_dropdown_item, redemo2);
					 				mandilist.setAdapter(adapter2);
								}
								else{
									ArrayList<HashMap<String,String>> category2=new ArrayList<HashMap<String,String>>();
									HashMap<String, String> map2=new HashMap<String, String>();
					 				map2.put("1","");
					 				map2.put("2","Select Mandi");
					 				category2.add(map2);
									Myspinner[] redemo2 = new Myspinner[category2.size()];
					 				for(int i=0; i<category2.size(); i++){
					 					redemo2[i] = new Myspinner(category2.get(i).get("2"), category2.get(i).get("1"), "");
					 				}
					 				ArrayAdapter<Myspinner> adapter2 = new ArrayAdapter<Myspinner>(getActivity(), android.R.layout.simple_spinner_dropdown_item, redemo2);
					 				mandilist.setAdapter(adapter2);
								}
							}

							@Override
							public void onNothingSelected(AdapterView<?> parent) {
								// TODO Auto-generated method stub
								
							}
						});
		 				
		 				mandilist.setOnItemSelectedListener(new OnItemSelectedListener() {

							@Override
							public void onItemSelected(AdapterView<?> parent,
									View view, int position, long id) {
								// TODO Auto-generated method stub
								if(position!=0)
								{
									Myspinner sp = (Myspinner)mandilist.getSelectedItem();
									try{
										encryptedgeoid = new Webservicerequest().Encrypt(sp.getValue());
									}
									catch(Exception e){
										e.getMessage();
									}
									category3.addAll(get.getretailersList(encryptedgeoid));	
									Myspinner[] redemo3 = new Myspinner[category3.size()];
					 				for(int i=0; i<category3.size(); i++){
					 					redemo3[i] = new Myspinner(category3.get(i).get("2"), category3.get(i).get("1"), "");
					 				}
					 				ArrayAdapter<Myspinner> adapter3 = new ArrayAdapter<Myspinner>(getActivity(), android.R.layout.simple_spinner_dropdown_item, redemo3);
					 				retailerslist.setAdapter(adapter3);
					 				ArrayAdapter<String> adaptervillages = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, get.getVillageNames(encryptedgeoid));
					 				villages.setAdapter(adaptervillages);
									villages.setThreshold(3);
									villages.setOnItemClickListener(new OnItemClickListener() {

										  @Override
										  public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
										    InputMethodManager in = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
										    in.hideSoftInputFromWindow(villages.getWindowToken(), 0);
										  }

									});
								}
								else{
									villages.setAdapter(null);
									ArrayList<HashMap<String, String>> category3=new ArrayList<HashMap<String,String>>();
									HashMap<String, String> map3=new HashMap<String, String>();
					 				map3.put("1","");
					 				map3.put("2","Select Retailer");
					 				category3.add(map3);
					 				Myspinner[] redemo3 = new Myspinner[category3.size()];
					 				for(int i=0; i<category3.size(); i++){
					 					redemo3[i] = new Myspinner(category3.get(i).get("2"), category3.get(i).get("1"), "");
					 				}
					 				ArrayAdapter<Myspinner> adapter3 = new ArrayAdapter<Myspinner>(getActivity(), android.R.layout.simple_spinner_dropdown_item, redemo3);
					 				retailerslist.setAdapter(adapter3); 				 	
								}
							}

							@Override
							public void onNothingSelected(AdapterView<?> parent) {
								// TODO Auto-generated method stub
								
							}
						});
		 				
		 				Myspinner[] redemo2 = new Myspinner[category2.size()];
		 				for(int i=0; i<category2.size(); i++){
		 					redemo2[i] = new Myspinner(category2.get(i).get("2"), category2.get(i).get("1"), "");
		 				}
		 				ArrayAdapter<Myspinner> adapter2 = new ArrayAdapter<Myspinner>(getActivity(), android.R.layout.simple_spinner_dropdown_item, redemo2);
		 				mandilist.setAdapter(adapter2); 				 			
		 				
		 				Myspinner[] redemo3 = new Myspinner[category3.size()];
		 				for(int i=0; i<category3.size(); i++){
		 					redemo3[i] = new Myspinner(category3.get(i).get("2"), category3.get(i).get("1"), "");
		 				}
		 				ArrayAdapter<Myspinner> adapter3 = new ArrayAdapter<Myspinner>(getActivity(), android.R.layout.simple_spinner_dropdown_item, redemo3);
		 				retailerslist.setAdapter(adapter3); 				 				
		 				
		 				Myspinner[] redemo4 = new Myspinner[category4.size()];
		 				for(int i=0; i<category4.size(); i++){
		 					redemo4[i] = new Myspinner(category4.get(i).get("2"), category4.get(i).get("1"), "");
		 				}
		 				ArrayAdapter<Myspinner> adapter4 = new ArrayAdapter<Myspinner>(getActivity(), android.R.layout.simple_spinner_dropdown_item, redemo4);
		 				activitieslist.setAdapter(adapter4);
						
		 				
						AlertDialog.Builder build = new Builder(getActivity())
						.setTitle(""+new SimpleDateFormat("d MMM yyyy").format(selecteddate))
						.setView(activity)
						.setPositiveButton("Create", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								//dist,date,empId,spmandi,spretailer,spactivity,rem
								if(new ConnectionDetector(getActivity()).isConnectingToInternet()){
									Myspinner sp1 = (Myspinner)districtlist.getSelectedItem();
									Myspinner sp2 = (Myspinner)mandilist.getSelectedItem();
									Myspinner sp3 = (Myspinner)retailerslist.getSelectedItem();
									Myspinner sp4 = (Myspinner)activitieslist.getSelectedItem();
									try{
										String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
										new CreateActivity().execute(sp1.getValue(),new SimpleDateFormat("yyyy-MM-dd").format(selecteddate),get.getAllLogin().get(0).get("empid"),sp2.getValue(),sp3.getValue(),sp4.getValue(),remarks.getText().toString());
									}
									catch(Exception e){
										e.getMessage();
									}							
								}
								else{
									Toast.makeText(getActivity(), "No Network Found", Toast.LENGTH_LONG).show();
								}
							}
						})
						.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								dialog.cancel();
							}
						});
						AlertDialog alert = build.create();
						alert.show();					
					}
				}
				catch(Exception e){
					e.getMessage();
				}
			}
		});
	}
	

class CreateActivity extends AsyncTask<String, Void, String>
{
	ProgressDialog pDialog;
	Spinner ret;
	Databaseutill db;
	GetData get;
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
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		String str = "0";
		ConnectionDetector cd = new ConnectionDetector(getActivity());
		if(cd.isConnectingToInternet())
		{
			try{
				db = Databaseutill.getDBAdapterInstance(getActivity());
				get = new GetData(getActivity(),db);
				str = get.createActivity(params[0], params[1], params[2], params[3], params[4], params[5], params[6],params[7],params[8]);
			}
			catch(Exception e)
			{
				e.getMessage();
				str = "0";
			}
		}
		else
		{
			str = "3";
		}
	
		return str;
	}

	
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		pDialog.dismiss();
		if(result!=null)
		{
			if(result.equalsIgnoreCase("0"))
			{
				Toast.makeText(getActivity(), "Some Error Occured Please try again", Toast.LENGTH_LONG).show();
			}
			else
			{
				if(result.equalsIgnoreCase("3"))
				{
					Toast.makeText(getActivity(), "No Network Found", Toast.LENGTH_LONG).show();
				}
				else
				{
					if(result.equalsIgnoreCase("2"))
					{
						Toast.makeText(getActivity(), "This Activity is Already planned", Toast.LENGTH_LONG).show();
					}
					else
					{
						Toast.makeText(getActivity(), "Planned Successfully", Toast.LENGTH_LONG).show();
					}
				}
			}
		}
		else
		{
			Toast.makeText(getActivity(), "Some Error Occured Please try again", Toast.LENGTH_LONG).show();
		}
		
	}
			
}


	
}
