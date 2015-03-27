package aksha.mytasks;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import aksha.adapters.ActivityListAdapter;
import aksha.adapters.LazyAdapter;
import aksha.adapters.MultiSelectionSpinner;
import aksha.adapters.Myspinner;
import aksha.adapters.RefreshableListView;
import aksha.connectiondetector.ConnectionDetector;
import aksha.database.Databaseutill;
import aksha.database.GetData;
import com.aksha.mosaic.R;
import aksha.plannedactivity.MOPlannedActivities;
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
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

public class TodaysPlan extends Fragment {
	
	private static View root;
	private RefreshableListView mListView;
	private Databaseutill db;
	private GetData get;
	private String encryptedgeoid = "";
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		root = inflater.inflate(R.layout.todaysplan_refreshablelist, container, false);
		
		ImageView createactivity = (ImageView)root.findViewById(R.id.createactivity);
		mListView = ((RefreshableListView)root.findViewById(R.id.today_refreshable_list));
		
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
 	      }
 	    });
	    
	    if(new ConnectionDetector(getActivity()).isConnectingToInternet())
	  	  {
	    
	    ArrayList<HashMap<String, String>> logindata = get.getAllLogin();
	    if(logindata.size()>0){
		  String empid = logindata.get(0).get("empid");
		  String districtid = logindata.get(0).get("geoid");
		  new GetActivities().execute(empid,districtid);
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
		createactivity.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getActivity(), "Create Activity", Toast.LENGTH_LONG).show();
				return true;
			}
		});
		
		createactivity.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
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
							category2.clear();
							HashMap<String, String> map2=new HashMap<String, String>();
			 				map2.put("1","");
			 				map2.put("2","Select Mandi");
			 				category2.add(map2);
							category2.addAll(get.getMandi(encryptedgeoid));	
							Myspinner[] redemo2 = new Myspinner[category2.size()];
			 				for(int i=0; i<category2.size(); i++){
			 					redemo2[i] = new Myspinner(category2.get(i).get("2"), category2.get(i).get("1"), "");
			 				}
			 				ArrayAdapter<Myspinner> adapter2 = new ArrayAdapter<Myspinner>(getActivity(), android.R.layout.simple_spinner_dropdown_item, redemo2);
			 				mandilist.setAdapter(adapter2);
						}
						else{
							category2.clear();
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
							category3.clear();
							HashMap<String, String> map3=new HashMap<String, String>();
			 				map3.put("1","");
			 				map3.put("2","Select Retailer");
			 				category3.add(map3);
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
							category3.clear();
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
				
 				try{ 				
				AlertDialog.Builder build = new Builder(getActivity())
				.setTitle(""+new SimpleDateFormat("d MMM yyyy").format(new Date()))
				.setView(activity)
				.setPositiveButton("Create", null)
				.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.cancel();
					}
				});
				final AlertDialog alert = build.create();
				
				//  alert.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
				
				alert.setOnShowListener(new DialogInterface.OnShowListener() {
					@Override
                    public void onShow(DialogInterface dialog) {
					//Control Input
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
											new CreateActivity().execute(sp1.getValue(),date,get.getAllLogin().get(0).get("empid"),sp2.getValue(),sp3.getValue(),sp4.getValue(),remarks.getText().toString(), id.get(0), villages.getText().toString());
										}
										else{
											new CreateActivity().execute(sp1.getValue(),date,get.getAllLogin().get(0).get("empid"),sp2.getValue(),sp3.getValue(),sp4.getValue(),remarks.getText().toString(), "0", villages.getText().toString());
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
 				catch(Exception e){
 					e.getMessage();
 				}
			
		}
		});
		
		return root;
	}
	
	
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
				data = get.getTodaysActivities(params[0], params[1]);
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
	 				ActivityListAdapter adapter = new ActivityListAdapter(getActivity(), result, mListView);
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
				str = get.createActivity(params[0], params[1], params[2], params[3], params[4], params[5], params[6], params[7], params[8]);
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
						Toast.makeText(getActivity(), "This Activity is Already Created", Toast.LENGTH_LONG).show();
					}
					if(result.equalsIgnoreCase("6"))
					{
						Toast.makeText(getActivity(), "Six Activities Completed for a Day", Toast.LENGTH_LONG).show();
					}
					else
					{
						Toast.makeText(getActivity(), "Activity Successfully Updated", Toast.LENGTH_LONG).show();
					}
				}
			}
		}
		else
		{
			Toast.makeText(getActivity(), "Some Error Occured Please try again", Toast.LENGTH_LONG).show();
		}		
		mListView.startRefreshing();
	}
			
}



	
	
	
}
