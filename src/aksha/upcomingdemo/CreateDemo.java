package aksha.upcomingdemo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import aksha.adapters.LazyAdapter;
import aksha.adapters.Myspinner;
import aksha.adapters.RefreshableListView;
import aksha.connectiondetector.ConnectionDetector;
import aksha.database.Databaseutill;
import aksha.database.GetData;

import com.aksha.mosaic.R;

import aksha.webservice.Webservicerequest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings.Global;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class CreateDemo extends Fragment {
	
	//View mView;
	Databaseutill db;
	GetData get;
	View root;
	private RefreshableListView mListView;
	EditText name,fathname,mobile,location,village,variety;
	EditText sowingdate,potashdate;
	Calendar myCalendar1,myCalendar2;
	String sowdate,potdate,empid;
	Spinner mandi,district,crop,year,season,crop1,crop2;
	String districtval,mandival;
	Button demosubmit;
	

	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
			
	   root=inflater.inflate(R.layout.createdemo,container, false);
		db = Databaseutill.getDBAdapterInstance(getActivity());
		get = new GetData(getActivity(),db);
		ArrayList<HashMap<String, String>> logindata = get.getAllLogin();
		empid = logindata.get(0).get("empid");
		
		Drawable rightArrow = getResources().getDrawable(R.drawable.demoback);
		// setting the opacity (alpha)
		rightArrow.setAlpha(60);
		// setting the images on the ImageViews
		//root.setImageDrawable(rightArrow);

		sowingdate  = (EditText)root.findViewById(R.id.sowingdate);
		sowingdate.setKeyListener(null);
		potashdate  = (EditText)root.findViewById(R.id.potashdate);
		potashdate.setKeyListener(null);

		name = (EditText)root.findViewById(R.id.name);
		fathname = (EditText)root.findViewById(R.id.fathname);
		mobile = (EditText)root.findViewById(R.id.mobiled);
		location = (EditText)root.findViewById(R.id.location);
		village = (EditText)root.findViewById(R.id.village);
		variety = (EditText)root.findViewById(R.id.variety);
		
		district = (Spinner)root.findViewById(R.id.district);
		mandi = (Spinner)root.findViewById(R.id.mandi);
		crop = (Spinner)root.findViewById(R.id.crop);
		year = (Spinner)root.findViewById(R.id.year);
		season = (Spinner)root.findViewById(R.id.season);
		crop1 = (Spinner)root.findViewById(R.id.precrop1);
		crop2 = (Spinner)root.findViewById(R.id.precrop2);
		
		demosubmit = (Button)root.findViewById(R.id.demobutton);
		
		
		// sowing date selector
		myCalendar1 = Calendar.getInstance();
		final DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {

		    @Override
		    public void onDateSet(DatePicker view, int year, int monthOfYear,
		            int dayOfMonth) {
		        // TODO Auto-generated method stub
		        myCalendar1.set(Calendar.YEAR, year);
		        myCalendar1.set(Calendar.MONTH, monthOfYear);
		        myCalendar1.set(Calendar.DAY_OF_MONTH, dayOfMonth);
		        updateLabelsowing();
		        
		    }

		};
		
		sowingdate.setOnClickListener(new OnClickListener() {

	        @Override
	        public void onClick(View v) {
	            // TODO Auto-generated method stub
	        
	            new DatePickerDialog(getActivity(), date1, myCalendar1
	                    .get(Calendar.YEAR), myCalendar1.get(Calendar.MONTH),
	                    myCalendar1.get(Calendar.DAY_OF_MONTH)).show();
	            
	          sowdate = sowingdate.getText().toString();
	         
	        }
	    });
	  // end
		
		
		// potash date selector
		myCalendar2 = Calendar.getInstance();
		final DatePickerDialog.OnDateSetListener date2 = new DatePickerDialog.OnDateSetListener() {

		    @Override
		    public void onDateSet(DatePicker view, int year, int monthOfYear,
		            int dayOfMonth) {
		        // TODO Auto-generated method stub
		        myCalendar2.set(Calendar.YEAR, year);
		        myCalendar2.set(Calendar.MONTH, monthOfYear);
		        myCalendar2.set(Calendar.DAY_OF_MONTH, dayOfMonth);
		        updateLabelpotash();
		        
		    }

		};

		
		
		potashdate.setOnClickListener(new OnClickListener() {

	        @Override
	        public void onClick(View v) {
	            // TODO Auto-generated method stub
	        
	            new DatePickerDialog(getActivity(), date2, myCalendar2
	                    .get(Calendar.YEAR), myCalendar2.get(Calendar.MONTH),
	                    myCalendar2.get(Calendar.DAY_OF_MONTH)).show();
	            
	            potdate = potashdate.getText().toString();
	        }
	    });
		// end
		
		// district spinner start
		
		  ArrayList<HashMap<String, String>> category1 =new ArrayList<HashMap<String,String>>();
		  
		  	ArrayList<HashMap<String, String>> distdb = new GetData(getActivity(), db).getDistrict();
		
		  	   for(int i=0; i<distdb.size();i++){
		  		   HashMap<String, String> map = new HashMap<String, String>();
		  		   map.put("1", distdb.get(i).get("Districtid"));
		  		   map.put("2", distdb.get(i).get("Districtname"));
		  		   category1.add(map);
		  	   }
		
		  ArrayList<Myspinner> distlist1 = new ArrayList<Myspinner>();
		 	if(category1.size()>0){
		 	     distlist1.add(new Myspinner("Select District", "0", ""));
		 			   for(int i=0; i<category1.size(); i++){
		 			      distlist1.add(new Myspinner(category1.get(i).get("2"), category1.get(i).get("1"), ""));
		 		       }
		 		ArrayAdapter<Myspinner> adapter1 = new ArrayAdapter<Myspinner>(getActivity(), android.R.layout.simple_spinner_dropdown_item, distlist1);
		 		district.setAdapter(adapter1);
		 	}
		
		 	
		  
		// end 	
		 	
		 	
		// mandi spinner 	
		 	
		 	
		 	final ArrayList<HashMap<String, String>> category2=new ArrayList<HashMap<String,String>>();
			HashMap<String, String> map2=new HashMap<String, String>();
				map2.put("1","");
				map2.put("2","Select Mandi");
				category2.add(map2);
				
				
				district.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent, View view,
							int position, long id) {
						// TODO Auto-generated method stub
						if(position!=0)
						{
							Myspinner sp = (Myspinner)district.getSelectedItem();
							try{
								districtval = new Webservicerequest().Encrypt(sp.getValue());
							}
							catch(Exception e){
								e.getMessage();
							}
							category2.addAll(get.getMandi(districtval));	
							Myspinner[] redemo2 = new Myspinner[category2.size()];
			 				for(int i=0; i<category2.size(); i++){
			 					redemo2[i] = new Myspinner(category2.get(i).get("2"), category2.get(i).get("1"), "");
			 				}
			 				ArrayAdapter<Myspinner> adapter2 = new ArrayAdapter<Myspinner>(getActivity(), android.R.layout.simple_spinner_dropdown_item, redemo2);
			 				mandi.setAdapter(adapter2);
			 				
							Myspinner sp2 = (Myspinner)mandi.getSelectedItem();
						    mandival = sp2.getValue();
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
			 				mandi.setAdapter(adapter2);
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						// TODO Auto-generated method stub
						
					}
				});

		 // end	
		 	
	     // crop spinner 
				
				
				ArrayList<HashMap<String, String>> category3=new ArrayList<HashMap<String,String>>();
				HashMap<String, String> map1=new HashMap<String, String>();
					map1.put("1","0");
					map1.put("2","Select Crop");
					category3.add(map1);
					
					try{
						ArrayList<HashMap<String, String>> crop = get.getcrops();									
						for (int i=0; i<crop.size(); i++) {
							HashMap<String, String> map = new HashMap<String, String>();
							map.put("1",crop.get(i).get("cropid"));
			 				map.put("2",crop.get(i).get("cropname"));
			 				category3.add(map);		 				
						}			
					}
					catch(Exception e){
						e.getMessage();
					}										
					
				Myspinner[] redemo1 = new Myspinner[category3.size()];
				if(category3.size()>0){
				   for(int i=0; i<category3.size(); i++){
				    	redemo1[i] = new Myspinner(category3.get(i).get("2"), category3.get(i).get("1"), "");
				   }
				   ArrayAdapter<Myspinner> adapter3 = new ArrayAdapter<Myspinner>(getActivity(), android.R.layout.simple_spinner_dropdown_item, redemo1);
				   crop.setAdapter(adapter3);
				}
		       
				 				
			   // end	
			  	
			 // year spinner 	
				 	
						Calendar calendar = Calendar.getInstance();
						int yearval = calendar.get(Calendar.YEAR);
						String preyear = (yearval-1)+"";
						String nextyear = (yearval+1)+"";
						String curryear = yearval+"";
						
								Myspinner[] redemo4 = new Myspinner[4];
								redemo4[0] = new Myspinner("Select Year", "0", "");
					 			redemo4[1] = new Myspinner(preyear, "1", "");
					 			redemo4[2] = new Myspinner(curryear, "2", "");
					 			redemo4[3] = new Myspinner(nextyear, "3", "");
					 				
					 				ArrayAdapter<Myspinner> adapter4 = new ArrayAdapter<Myspinner>(getActivity(), android.R.layout.simple_spinner_dropdown_item, redemo4);
					 				year.setAdapter(adapter4);
					 		

			// end		
		  // Season spinner 	
					 
					Myspinner[] redemo5 = new Myspinner[3];
					redemo5[0] = new Myspinner("Select Season", "0", "");
					redemo5[1] = new Myspinner("Rabi", "1", "");
					redemo5[2] = new Myspinner("Khareef", "2", "");
							 				
					ArrayAdapter<Myspinner> adapter5 = new ArrayAdapter<Myspinner>(getActivity(), android.R.layout.simple_spinner_dropdown_item, redemo5);
					season.setAdapter(adapter5);
				
			//end
					
			 // Crop1 spinner 	

					ArrayList<HashMap<String, String>> category6=new ArrayList<HashMap<String,String>>();
					HashMap<String, String> map6=new HashMap<String, String>();
						map6.put("1","0");
						map6.put("2","Select Crop1");
						category6.add(map6);
						
						try{
							ArrayList<HashMap<String, String>> crop1 = get.getcrops();									
							for (int i=0; i<crop1.size(); i++) {
								HashMap<String, String> map = new HashMap<String, String>();
								map.put("1",crop1.get(i).get("cropid"));
				 				map.put("2",crop1.get(i).get("cropname"));
				 				category6.add(map);		 				
							}			
						}
						catch(Exception e){
							e.getMessage();
						}										
						
					Myspinner[] redemo6 = new Myspinner[category6.size()];
					if(category6.size()>0){
					   for(int i=0; i<category6.size(); i++){
					    	redemo6[i] = new Myspinner(category6.get(i).get("2"), category6.get(i).get("1"), "");
					   }
					   ArrayAdapter<Myspinner> adapter6 = new ArrayAdapter<Myspinner>(getActivity(), android.R.layout.simple_spinner_dropdown_item, redemo6);
					   crop1.setAdapter(adapter6);
					}
			       	

				 // end	
				 	
								
				// Crop2 spinner 	
							 	
					ArrayList<HashMap<String, String>> category7=new ArrayList<HashMap<String,String>>();
					HashMap<String, String> map7=new HashMap<String, String>();
						map7.put("1","0");
						map7.put("2","Select Crop2");
						category7.add(map7);
						
						try{
							ArrayList<HashMap<String, String>> crop2 = get.getcrops();									
							for (int i=0; i<crop2.size(); i++) {
								HashMap<String, String> map = new HashMap<String, String>();
								map.put("1",crop2.get(i).get("cropid"));
				 				map.put("2",crop2.get(i).get("cropname"));
				 				category7.add(map);		 				
							}			
						}
						catch(Exception e){
							e.getMessage();
						}										
						
					Myspinner[] redemo7 = new Myspinner[category7.size()];
					if(category7.size()>0){
					   for(int i=0; i<category7.size(); i++){
					    	redemo7[i] = new Myspinner(category7.get(i).get("2"), category7.get(i).get("1"), "");
					   }
					   ArrayAdapter<Myspinner> adapter7 = new ArrayAdapter<Myspinner>(getActivity(), android.R.layout.simple_spinner_dropdown_item, redemo7);
					   crop2.setAdapter(adapter7);
					}
			       	

				 // end	
					
				// submit demo details	 	
					
					 demosubmit.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								//dist,date,empId,spmandi,spretailer,spactivity,rem
								
								
								if(new ConnectionDetector(getActivity()).isConnectingToInternet()){
									Myspinner sp1 = (Myspinner)district.getSelectedItem();
									Myspinner sp2 = (Myspinner)mandi.getSelectedItem();
									Myspinner sp3 = (Myspinner)crop.getSelectedItem();
									Myspinner sp4 = (Myspinner)year.getSelectedItem();
									Myspinner sp5 = (Myspinner)season.getSelectedItem();
									Myspinner sp6 = (Myspinner)crop1.getSelectedItem();
									Myspinner sp7 = (Myspinner)crop2.getSelectedItem();
									
									String name1 = name.getText().toString();
									String fathname1 = fathname.getText().toString();
									String mobile1 = mobile.getText().toString();
									String location1 = location.getText().toString();
									String Empid = empid;
									String village1 = village.getText().toString();
									String variety1 = variety.getText().toString();
									String potashdate1 = potashdate.getText().toString();
									String sowingdate1 = sowingdate.getText().toString();
									
									
									
									if(district.getSelectedItemPosition()!=0 && mandi.getSelectedItemPosition()!=0
										&& crop.getSelectedItemPosition()!=0 && year.getSelectedItemPosition()!=0
										   && season.getSelectedItemPosition()!=0 && crop1.getSelectedItemPosition()!=0
										      && crop2.getSelectedItemPosition()!=0 
										        && name.getText().toString().length() != 0
										        && fathname.getText().toString().length() != 0 && mobile.getText().toString().length() != 0 
										        && mobile.getText().toString().length() == 10 && variety.getText().toString().length() != 0
										      && location.getText().toString().length() != 0 && village.getText().toString().length() != 0
										  && sowingdate.getText().toString().length() != 0 && potashdate.getText().toString().length() != 0
									   ){

										try{ 
											new CreateActivity().execute(
													name1,
													fathname1,
													mobile1,
													location1,
													Empid,
													sp1.getValue(),
													village1,
													sp4.getValue(),
													sp5.getValue(),
													sp3.getValue(),
													sp6.getValue(),
													sp7.getValue(),
													variety1,
													potashdate1,
													sowingdate1,
													sp2.getValue());
												
											
										 }
										  catch(Exception e){
										 	e.getMessage();
										 }				
									}
									else{
										if(district.getSelectedItemPosition()==0){
											Toast.makeText(getActivity(), "Please Select District", Toast.LENGTH_LONG).show();
										}
										else{
											if(mandi.getSelectedItemPosition()==0){
												Toast.makeText(getActivity(), "Please Select Mandi", Toast.LENGTH_LONG).show();
											}
											else{
												if(crop.getSelectedItemPosition()==0){
													Toast.makeText(getActivity(), "Please Select Crop", Toast.LENGTH_LONG).show();
												}
												else{
													if(year.getSelectedItemPosition()==0){
														Toast.makeText(getActivity(), "Please Select Year", Toast.LENGTH_LONG).show();
													}
												
												else{
													if(season.getSelectedItemPosition()==0){
														Toast.makeText(getActivity(), "Please Select Season", Toast.LENGTH_LONG).show();
													}
												
												else{
													if(crop1.getSelectedItemPosition()==0){
														Toast.makeText(getActivity(), "Please Select Crop1", Toast.LENGTH_LONG).show();
													}
												
												else{
													if(crop2.getSelectedItemPosition()==0){
														Toast.makeText(getActivity(), "Please Select Crop2", Toast.LENGTH_LONG).show();
													}
													else{
														if(name.getText().toString().length() == 0){
															name.setError("Enter Name");
															Toast.makeText(getActivity(), "Please Enter Name", Toast.LENGTH_LONG).show();
														}
														else{
															if(fathname.getText().toString().length() == 0){
																fathname.setError("Father's Name");
																Toast.makeText(getActivity(), "Please Enter Father's Name", Toast.LENGTH_LONG).show();
															}
															else{
																if(mobile.getText().toString().length() == 0){
																	mobile.setError("Enter Mobile Number");
																	Toast.makeText(getActivity(), "Please Enter Mobile Number", Toast.LENGTH_LONG).show();
																}
																else{
																	if(mobile.getText().toString().length() < 10){
																		mobile.setError("Enter Valid Mobile Number");
																		Toast.makeText(getActivity(), "Please Enter Valid Mobile Number", Toast.LENGTH_LONG).show();
																	}
																else{
																	if(location.getText().toString().length() == 0){
																		location.setError("Enter Location");
																		Toast.makeText(getActivity(), "Please Enter Location", Toast.LENGTH_LONG).show();
																	}
																	else{
																		if(village.getText().toString().length() == 0){
																			village.setError("Enter Village");
																			Toast.makeText(getActivity(), "Please Enter Village", Toast.LENGTH_LONG).show();
																		}
																		else{
																			if(variety.getText().toString().length() == 0){
																				variety.setError("Enter Variety");
																				Toast.makeText(getActivity(), "Please Enter Variety", Toast.LENGTH_LONG).show();
																			}
																			else{
																				if(sowingdate.getText().toString().length() == 0){
																					sowingdate.setError("Select Sowing Date");
																					Toast.makeText(getActivity(), "Please Select Sowing Date", Toast.LENGTH_LONG).show();
																				}
																				else{
																					if(potashdate.getText().toString().length() == 0){
																						potashdate.setError("Select Potash Date");
																						Toast.makeText(getActivity(), "Please Select Potash Date", Toast.LENGTH_LONG).show();
																					}
														
														
													}}}}}}}}}
													
												}
											 }}}}}
									}
								}
								else{
									Toast.makeText(getActivity(), "No Network Found", Toast.LENGTH_LONG).show();
								}
							}
						}); 
				
			return root;
		}
	
	
	
	 private void updateLabelsowing() {

		    String myFormat = "yyyy-MM-dd"; //In which you need put here
		    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

		    sowingdate.setText(sdf.format(myCalendar1.getTime()));
		 }
	 
	 private void updateLabelpotash() {

		    String myFormat = "yyyy-MM-dd"; //In which you need put here
		    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

		    potashdate.setText(sdf.format(myCalendar2.getTime()));
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
				str = get.createFarmer(params[0], params[1], params[2], params[3], params[4], params[5], params[6], params[7], params[8], params[9], params[10], params[11], params[12], params[13], params[14], params[15]);
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
					else
					{
						Toast.makeText(getActivity(), "Successfully Created", Toast.LENGTH_LONG).show();
					}
				}
			}
		}
		else
		{
			Toast.makeText(getActivity(), "Some Error Occured Please try again", Toast.LENGTH_LONG).show();
		}		
		//mListView.startRefreshing();
	}
}

	 
	 

}
