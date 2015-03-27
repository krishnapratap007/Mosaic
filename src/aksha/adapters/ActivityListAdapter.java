package aksha.adapters;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import aksha.connectiondetector.ConnectionDetector;
import aksha.database.Databaseutill;
import aksha.database.GetData;
import com.aksha.mosaic.R;
import aksha.webservice.Webservicerequest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.graphics.Color;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityListAdapter extends BaseAdapter {

   Context context;
   ArrayList<HashMap<String, String>> str;
   RefreshableListView mLiView;
   int count=0;
   boolean boolclose = false;

   public ActivityListAdapter(Context context,ArrayList<HashMap<String, String>> category, RefreshableListView mListView) {
		// TODO Auto-generated constructor stub
    	this.context = context;
    	str = category;
    	mLiView = mListView;
    	
    	/*int min = 0;
    	int max = 7;
    	Random r = new Random();
    	count = r.nextInt(max - min) + min;*/
	}

   
   public class ViewHolder{
	   LinearLayout datelayout;
       TextView date;
       TextView monthyear;
       TextView name;
       TextView email;
       TextView address;
       TextView mobile;
       TextView village;
       TextView villagename;
       Spinner sp;
       RelativeLayout card;
   }
   

    public View getView(int position, View convertView, ViewGroup parent){
    	 ViewHolder holder;
    	 LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	 convertView =inflater.inflate(R.layout.activitylistitemfa, null);
    	 HashMap<String, String> song = new HashMap<String, String>();
    	
		     song = str.get(position);		
		 
         holder				=	new ViewHolder();
         holder.card		=	(RelativeLayout)convertView.findViewById(R.id.card);
         holder.datelayout	=	(LinearLayout)convertView.findViewById(R.id.datelayout);
         holder.date		=	(TextView)convertView.findViewById(R.id.date);
         holder.monthyear	=	(TextView)convertView.findViewById(R.id.monthyear);
         holder.name		=	(TextView)convertView.findViewById(R.id.name);
         holder.email		=	(TextView)convertView.findViewById(R.id.email);
         holder.address		=	(TextView)convertView.findViewById(R.id.address);
         holder.mobile		=	(TextView)convertView.findViewById(R.id.mobile);
         holder.village		=	(TextView)convertView.findViewById(R.id.village);
         holder.villagename	=	(TextView)convertView.findViewById(R.id.villagename);
         holder.sp		=	(Spinner)convertView.findViewById(R.id.spedit);
         
         //#f56545---red -- missed plan
         //#27ae60---green  -- active close
         //#ffbb22---yellow  -- approved
         //#941494 --- purple --- unapproved
         
         if(song.get("jobstatus").equalsIgnoreCase("1")){
        	 holder.datelayout.setBackgroundColor(Color.parseColor("#ffbb22"));
        	 
         }
         else if(song.get("jobstatus").equalsIgnoreCase("2")){
        	 holder.datelayout.setBackgroundColor(Color.parseColor("#27ae60"));
        	// convertView.setOnClickListener(null);
        	 
         }
         else{
        	 holder.datelayout.setBackgroundColor(Color.parseColor("#941494"));
         }
         try{
        	 Date dt = new SimpleDateFormat("yyyy-MM-dd").parse(song.get("plandate"));
        	 holder.date.setText(new SimpleDateFormat("d,MMM yyyy").format(dt).split(",")[0]);
        	 holder.monthyear.setText(new SimpleDateFormat("d,MMM yyyy").format(dt).split(",")[1]);
         }
         catch(Exception e){
        	 e.getMessage();
         }
         holder.card.setTag(song);
         holder.name.setText(song.get("retailer"));
         holder.email.setText(song.get("jobdesc"));
         holder.address.setText("District- "+song.get("districtname"));
         holder.mobile.setText("Mandi- "+song.get("mandi"));
         if(song.get("villname").length()>0)
         {
        	 holder.village.setText("Village- "+song.get("villname"));
         }
         else
         {
        	 holder.village.setVisibility(View.GONE);
         }
         
         holder.card.setOnClickListener(clicklistner);
         holder.sp.setVisibility(View.GONE);
         holder.villagename.setVisibility(View.GONE);
         Animation animation = AnimationUtils.loadAnimation(context, R.anim.card_animation);
         holder.card.startAnimation(animation);
         
         return convertView;
    }


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return str.size();
	}


	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}


	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
/*	Retailer Visit   11
	- Enter Name of the person visited
	- Enter Mobile Number of person visited
	- Submit (If submitted, Status of Job will be closed and SMS will be sent to Mobile Number). "Thank you for your valuable time. MOSAIC"

	Farm Demo   14, 15
	- Enter Name of Farmer
	- Enter Number of Farmer
	- Data Collected (Y/N)
	- Submit (If submitted, Status of Job will be closed and SMS will be sent to Mobil Number). "Thank you for your valuable time. MOSAIC"

	Complaint Handling   2
	- Enter Name
	- Enter Number
	- Remarks
	- Submit (If Submitted, status of job will be closed and sms will be sent to mobile number). "Thank you for your valuable time. Mosaic"

	Farmer Meeting  3,10,1,4,13,5
	- Farmer Numbers Generated
	- Name, Number, Add and Finish

	Jeep Campaign   12
	- Remarks
	- Activity Closed
*/
	
	private OnClickListener clicklistner = new OnClickListener() {
		
		final ArrayList<HashMap<String,String>> categorytask2=new ArrayList<HashMap<String,String>>();
		
		
		@Override
		public void onClick(View v) {
			boolclose = false;
			
			// TODO Auto-generated method stub
			
/*
			if(datatags.get("jobstatus").equalsIgnoreCase("2")){
	    		//v.setOnClickListener(null);
	    		Toast.makeText(context, "Activity has been Closed", Toast.LENGTH_SHORT).show();
	    		
	    	}*/
			
			
			final GetData get = new GetData(context, Databaseutill.getDBAdapterInstance(context));
			
			
		
			try{				
				final HashMap<String, String> datatags = (HashMap<String, String>)v.getTag();
				
			
		    		
			    if((datatags.get("jobid").equalsIgnoreCase("1")	||
			    		datatags.get("jobid").equalsIgnoreCase("3")	||			    			
			    				datatags.get("jobid").equalsIgnoreCase("10") ||
			    				  datatags.get("jobid").equalsIgnoreCase("4") ||
			    			      	datatags.get("jobid").equalsIgnoreCase("13")||
			    			      	datatags.get("jobid").equalsIgnoreCase("5"))
			    			      	&& datatags.get("jobstatus").equalsIgnoreCase("1")
			    			      	)
			    {
			    	
			    	
			    	boolclose = false;
			    	//if(song.get("jobstatus").equalsIgnoreCase("2"))
					LayoutInflater inf = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					final View vew = inf.inflate(R.layout.popupfarmermeet, null);
					
					
					final ArrayList<String> actplanid = new ArrayList<String>();
					final ArrayList<String> farmername = new ArrayList<String>();
					final ArrayList<String> mobilenumber = new ArrayList<String>();
					final ArrayList<String> villagename = new ArrayList<String>();
					final ArrayList<String> demoid = new ArrayList<String>();
					final ArrayList<String> mandiid = new ArrayList<String>();
					final ArrayList<String> districtid = new ArrayList<String>();
					
					final CheckBox demofarmer = (CheckBox)vew.findViewById(R.id.demofarmer);
					demofarmer.setVisibility(View.GONE);
					
					Button add = (Button)vew.findViewById(R.id.add);
					
					TextView teh = (TextView)vew.findViewById(R.id.teh);
					TextView distName = (TextView)vew.findViewById(R.id.dist);					
					final EditText villName = (EditText)vew.findViewById(R.id.vill);
					distName.setText("District: "+datatags.get("districtname"));
					teh.setText("Mandi: "+datatags.get("mandi"));
					villName.setText(datatags.get("villname"));
					ImageButton reset = (ImageButton)vew.findViewById(R.id.reset);
					
					if(datatags.get("jobid").equalsIgnoreCase("1") || (datatags.get("jobid").equalsIgnoreCase("4"))){
					   reset.setVisibility(View.GONE);	
					}
					reset.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							villName.setText("");
						}
					});
					
				
					
					add.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							ListView addfarmers = (ListView)vew.findViewById(R.id.addfarmers);
							final EditText fname = (EditText)vew.findViewById(R.id.fname);
							final EditText mob = (EditText)vew.findViewById(R.id.mob);
							final EditText vill = (EditText)vew.findViewById(R.id.vill);
							if(fname.getText().toString().length()>0)
							{
								if(mob.getText().toString().length()==10)
								{
									if(villName.getText().toString().length()>0)
									{
										String farm = fname.getText().toString();
										String mobi = mob.getText().toString();
										String villname = String.valueOf(vill.getText());
										String demoi;
										if(demofarmer.isChecked())
										{
											demoi="1";
										}
										else
										{
											demoi="0";
										}
									//	get.insertFarmeract(datatags.get("actplanid"),farm, mobi, demoi,datatags.get("mandiid"),villName.getText().toString(),datatags.get("districtid"));
										actplanid.add(datatags.get("actplanid"));
										
										HashMap<String, String> nfam=new HashMap<String, String>();
										
										nfam.put("1", farm);
										nfam.put("2", villname);
										nfam.put("3",mobi );
										
										farmername.add(farm);
										mobilenumber.add(mobi);
										demoid.add(demoi);
										mandiid.add(datatags.get("mandiid"));
										villagename.add(villName.getText().toString());
										districtid.add(datatags.get("districtid"));
										
										fname.setText("");
										mob.setText("");
										fname.setError(null);
										mob.setError(null);
										
										
										categorytask2.add(nfam);
										
										ActivityListAdapterFarmerPop adp=new ActivityListAdapterFarmerPop(context, categorytask2, addfarmers);
										ActivityListAdapterFarmerPop adpold=(ActivityListAdapterFarmerPop) addfarmers.getAdapter();
										
										if(adpold==null){
										   addfarmers.setAdapter(adp);
										}
										else{
											ArrayList<HashMap<String, String>> str=(ArrayList<HashMap<String, String>>)adp.setItem(nfam);
											ActivityListAdapterFarmerPop adps=new ActivityListAdapterFarmerPop(context, str,addfarmers );
											addfarmers.setAdapter(null);
									    	addfarmers.setAdapter(adps);
										}
										
									}
									else
									{
										villName.setError("Enter Village");
										villName.addTextChangedListener(new TextWatcher() {
											
											@Override
											public void onTextChanged(CharSequence s, int start, int before, int count) {
												// TODO Auto-generated method stub
												if(villName.getText().toString().length()!=0)
												{
													villName.setError(null);
												}
												else
												{
													villName.setError("Enter Village");
												}
											}
											
											@Override
											public void beforeTextChanged(CharSequence s, int start, int count,
													int after) {
												// TODO Auto-generated method stub
												
											}
											
											@Override
											public void afterTextChanged(Editable s) {
												// TODO Auto-generated method stub
												
											}
										});
									}
								}
								else
								{
									mob.setError("Enter Valid Mobile Number");
									mob.requestFocus();
									mob.addTextChangedListener(new TextWatcher() {
										
										@Override
										public void onTextChanged(CharSequence s, int start, int before, int count) {
											// TODO Auto-generated method stub
											if(mob.getText().toString().length()==10)
											{
												mob.setError(null);
											}
											else
											{
												mob.setError("Enter Valid Mobile Number");
											}
										}
										
										@Override
										public void beforeTextChanged(CharSequence s, int start, int count,
												int after) {
											// TODO Auto-generated method stub
											
										}
										
										@Override
										public void afterTextChanged(Editable s) {
											// TODO Auto-generated method stub
											
										}
									});
								}
							}
							else
							{
								fname.setError("Enter Valid Farmer Name");
								fname.requestFocus();
								fname.addTextChangedListener(new TextWatcher() {
									
									@Override
									public void onTextChanged(CharSequence s, int start, int before, int count) {
										// TODO Auto-generated method stub
										if(fname.getText().toString().length()!=0)
										{
											fname.setError(null);
										}
										else
										{
											fname.setError("Enter Valid Farmer Name");
										}
									}
									
									@Override
									public void beforeTextChanged(CharSequence s, int start, int count,
											int after) {
										// TODO Auto-generated method stub
										
									}
									
									@Override
									public void afterTextChanged(Editable s) {
										// TODO Auto-generated method stub
										
									}
								});
							
							}
							
						}
					});
					
					final Dialog dialog = new Dialog(context);//,android.R.style.Theme_Translucent_NoTitleBar);
				   //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				   // dialog.setContentView(R.layout.loading_screen);
					dialog.setTitle(datatags.get("retailer"));
				    dialog.setContentView(vew);
				    Window window = dialog.getWindow();
				    WindowManager.LayoutParams wlp = window.getAttributes();

				    wlp.gravity = Gravity.CENTER;
				    wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
				    //wlp.height =  WindowManager.LayoutParams.MATCH_PARENT;
				    window.setAttributes(wlp);
				    dialog.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
				    dialog.setCancelable(false);
				    
				    Button cancel = (Button)vew.findViewById(R.id.cancel);
				    cancel.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							dialog.dismiss();
						}
					});
				    
				    Button finish = (Button)vew.findViewById(R.id.finish);
				    finish.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							ListView addfarmers = (ListView)vew.findViewById(R.id.addfarmers);
							EditText fname = (EditText)vew.findViewById(R.id.fname);
							EditText mob = (EditText)vew.findViewById(R.id.mob);

							ArrayList<HashMap<String, String>> logindata =new GetData(context, new Databaseutill(context).getDBAdapterInstance(context)).getAllLogin();
							String empidsr = logindata.get(0).get("empid");
							
							
							if(fname.getText().toString().length()>0 && mob.getText().toString().length()>0)
							{
								if(mob.getText().toString().length()==10)
								{
									if(villName.getText().toString().length()>0){
										String farm = fname.getText().toString();
									String mobi = mob.getText().toString();
									String demoi;
									if(demofarmer.isChecked())
									{
										demoi="1";
									}
									else
									{
										demoi="0";
									}
							//		get.insertFarmeract(datatags.get("actplanid"),farm, mobi, demoi,datatags.get("mandiid"),villName.getText().toString(),datatags.get("districtid"));
									actplanid.add(datatags.get("actplanid"));
									farmername.add(farm);
									mobilenumber.add(mobi);
									demoid.add(demoi);
									mandiid.add(datatags.get("mandiid"));
									villagename.add(villName.getText().toString());
									districtid.add(datatags.get("districtid"));
									
									addfarmers.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,farmername));
									fname.setText("");
									mob.setText("");
								
									if(farmername.size()>0 && mobilenumber.size()>0)
									{
										if(new ConnectionDetector(context).isConnectingToInternet()){
											
							
											new SubmitRetailer(context,mLiView).execute(actplanid.get(0),mandiid.get(0),villagename.toString(),farmername.toString(),mobilenumber.toString(),demoid.toString(),districtid.get(0),datatags.get("jobid"),empidsr);
											dialog.dismiss();
											mLiView.startRefreshing();
										}
										else{
											Toast.makeText(context, "No Network Found", Toast.LENGTH_SHORT).show();
										}												
									}
									else
									{
										Toast.makeText(context, "Please Enter Valid Data", Toast.LENGTH_LONG).show();
									}
									fname.setText("");
									mob.setText("");	
									
								}
									else{
										Toast.makeText(context, "You have not Entered any Village", Toast.LENGTH_LONG).show();
										}
									
								}
								else
								{
									Toast.makeText(context, "Please Enter Valid Mobile Number ", Toast.LENGTH_SHORT).show();
								}
							}
							else
							{
								if(farmername.size()>0 && mobilenumber.size()>0)
								{
									if(new ConnectionDetector(context).isConnectingToInternet()){
										new SubmitRetailer(context,mLiView).execute(actplanid.get(0),mandiid.get(0),villagename.toString(),farmername.toString(),mobilenumber.toString(),demoid.toString(),districtid.get(0),datatags.get("jobid"),empidsr);
										dialog.dismiss();
										mLiView.startRefreshing();
									}
									else{
										Toast.makeText(context, "No Network Found", Toast.LENGTH_SHORT).show();
									}												
								}
								else
								{
									Toast.makeText(context, "Please Enter Valid Data", Toast.LENGTH_LONG).show();								
								}								
								fname.setText("");
								mob.setText("");														
							}						
						}
					});
				    dialog.show();						
			    }
			    else{
			    	boolclose = true;
			    }
			    
			//}
			    
			    
			}
			catch(Exception e){
				Log.e("Todays Activity Adapter:", e.getLocalizedMessage());
			}
			  
			try{
				final HashMap<String, String> datatags = (HashMap<String, String>)v.getTag();
			    if((datatags.get("jobid").equalsIgnoreCase("11")) && datatags.get("jobstatus").equalsIgnoreCase("1"))
			    {
			    	boolclose = false;
					LayoutInflater inf = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					final View vew = inf.inflate(R.layout.retailervisit, null);
					AlertDialog.Builder build = new Builder(context);
					build.setTitle(datatags.get("firm_name"));
					build.setCancelable(false);
					build.setView(vew);
					build.setPositiveButton("Submit", null);
					build.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
						
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
							Button submit = alert.getButton(AlertDialog.BUTTON_POSITIVE);
							submit.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									EditText name = (EditText)vew.findViewById(R.id.nme);
									EditText mobile = (EditText)vew.findViewById(R.id.mobie);
									if(name.getText().toString().length()!=0 && mobile.getText().toString().length()==10){
										if(new ConnectionDetector(context).isConnectingToInternet()){
										
											new CloseVisit(context,mLiView).execute(datatags.get("actplanid"),mobile.getText().toString(),name.getText().toString(),datatags.get("jobid"),"","0");
											alert.dismiss();
											mLiView.startRefreshing();
										}
										else{
											Toast.makeText(context, "No Network Found", Toast.LENGTH_SHORT).show();
										}
									}
								}
							});
						}
					});
					alert.show();					
			    }	
			    else{
			    	boolclose = true;
			    }
			    
			    
			    
			}
			catch(Exception e){
				Log.e("Todays Activity Adapter:", e.getLocalizedMessage());
			}
			
			try{
				final HashMap<String, String> datatags = (HashMap<String, String>)v.getTag();
			    if((datatags.get("jobid").equalsIgnoreCase("15") || (datatags.get("jobid").equalsIgnoreCase("14"))) && datatags.get("jobstatus").equalsIgnoreCase("1"))
			    {
			    	boolclose = false;
					LayoutInflater inf = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					final View vew = inf.inflate(R.layout.farmdemo, null);					
					AlertDialog.Builder build = new Builder(context);
					build.setTitle(datatags.get("firm_name"));
					build.setCancelable(false);
					build.setView(vew);
					build.setPositiveButton("Submit", null);
					build.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
						
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
							final Button submit = alert.getButton(AlertDialog.BUTTON_POSITIVE);
							submit.setEnabled(false); 
							Switch datacollected = (Switch)vew.findViewById(R.id.datacoll);
							
							//datacollected toggle = (Switch) findViewById(R.id.togglebutton);
							datacollected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
							    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
							        if (isChecked) {
							        	submit.setEnabled(true); 
							        } else {
							        	submit.setEnabled(false); 
							        }
							    }
							});
							
							
							submit.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									final EditText name = (EditText)vew.findViewById(R.id.nme);
									final EditText mobile = (EditText)vew.findViewById(R.id.mobie);
									EditText remarks = (EditText)vew.findViewById(R.id.rmrks);
									Switch datacollected = (Switch)vew.findViewById(R.id.datacoll);
									if(name.getText().toString().length()!=0 && mobile.getText().toString().length()==10){
										if(new ConnectionDetector(context).isConnectingToInternet()){
											if(datacollected.isChecked()){ 
												new CloseVisit(context,mLiView).execute(datatags.get("actplanid"),mobile.getText().toString(),name.getText().toString(),datatags.get("jobid"),remarks.getText().toString(),"1");
											}
											else{
												//myButton.setEnabled(false);
												//Toast.makeText(context, "Please Enable the Data Collected", Toast.LENGTH_SHORT).show();
												 
												new CloseVisit(context,mLiView).execute(datatags.get("actplanid"),mobile.getText().toString(),name.getText().toString(),datatags.get("jobid"),remarks.getText().toString(),"2");
											}
											alert.dismiss();
											mLiView.startRefreshing();
										}
										else{
											Toast.makeText(context, "No Network Found", Toast.LENGTH_SHORT).show();
										}
									}
									else{
										if(name.getText().toString().length()==0){
											name.setError("Invalid Input");
											name.addTextChangedListener(new TextWatcher() {
												
												@Override
												public void onTextChanged(CharSequence s, int start, int before, int count) {
													// TODO Auto-generated method stub
													if(name.getText().toString().length()==0){
														name.setError("Invalid Input");
													}
													else{
														name.setError(null);
													}
												}
												
												@Override
												public void beforeTextChanged(CharSequence s, int start, int count,
														int after) {
													// TODO Auto-generated method stub
													
												}
												
												@Override
												public void afterTextChanged(Editable s) {
													// TODO Auto-generated method stub
													
												}
											});
										}
										if(mobile.getText().toString().length()!=10){
											mobile.setError("Invalid Input");
											mobile.addTextChangedListener(new TextWatcher() {
												
												@Override
												public void onTextChanged(CharSequence s, int start, int before, int count) {
													// TODO Auto-generated method stub
													if(mobile.getText().toString().length()!=10){
														mobile.setError("Invalid Input");
													}
													else{
														mobile.setError(null);
													}
												}
												
												@Override
												public void beforeTextChanged(CharSequence s, int start, int count,
														int after) {
													// TODO Auto-generated method stub
													
												}
												
												@Override
												public void afterTextChanged(Editable s) {
													// TODO Auto-generated method stub
													
												}
											});
										}
									}
								}
							});
						}
					});
					alert.show();					
			    }
			    else{
			    	boolclose = true;
			    }
			    
			}
			catch(Exception e){
				Log.e("Todays Activity Adapter:", e.getLocalizedMessage());
			}
			
			try{
				final HashMap<String, String> datatags = (HashMap<String, String>)v.getTag();
			    if((datatags.get("jobid").equalsIgnoreCase("2")) && datatags.get("jobstatus").equalsIgnoreCase("1"))
			    {
			    	boolclose = false;
					LayoutInflater inf = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					final View vew = inf.inflate(R.layout.complainthandling, null);
					AlertDialog.Builder build = new Builder(context);
					build.setTitle(datatags.get("firm_name"));
					build.setCancelable(false);
					build.setView(vew);
					build.setPositiveButton("Submit", null);
					build.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
						
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
							Button submit = alert.getButton(AlertDialog.BUTTON_POSITIVE);
							submit.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									final EditText name = (EditText)vew.findViewById(R.id.nme);
									final EditText mobile = (EditText)vew.findViewById(R.id.mobie);
									EditText remarks = (EditText)vew.findViewById(R.id.remrks);
									if(name.getText().toString().length()!=0 && mobile.getText().toString().length()==10){
										if(new ConnectionDetector(context).isConnectingToInternet()){
											//actplanid,mobile,name,jobid,rem,data
											new CloseVisit(context,mLiView).execute(datatags.get("actplanid"),mobile.getText().toString(),name.getText().toString(),datatags.get("jobid"),remarks.getText().toString(),"0");
											alert.dismiss();
											mLiView.startRefreshing();
										}
										else{
											Toast.makeText(context, "No Network Found", Toast.LENGTH_SHORT).show();
										}
									}
									else{
										if(name.getText().toString().length()==0){
											name.setError("Invalid Input");
											name.addTextChangedListener(new TextWatcher() {
												
												@Override
												public void onTextChanged(CharSequence s, int start, int before, int count) {
													// TODO Auto-generated method stub
													if(name.getText().toString().length()==0){
														name.setError("Invalid Input");
													}
													else{
														name.setError(null);
													}
												}
												
												@Override
												public void beforeTextChanged(CharSequence s, int start, int count,
														int after) {
													// TODO Auto-generated method stub
													
												}
												
												@Override
												public void afterTextChanged(Editable s) {
													// TODO Auto-generated method stub
													
												}
											});
										}
										if(mobile.getText().toString().length()!=10){
											mobile.setError("Invalid Input");
											mobile.addTextChangedListener(new TextWatcher() {
												
												@Override
												public void onTextChanged(CharSequence s, int start, int before, int count) {
													// TODO Auto-generated method stub
													if(mobile.getText().toString().length()!=10){
														mobile.setError("Invalid Input");
													}
													else{
														mobile.setError(null);
													}
												}
												
												@Override
												public void beforeTextChanged(CharSequence s, int start, int count,
														int after) {
													// TODO Auto-generated method stub
													
												}
												
												@Override
												public void afterTextChanged(Editable s) {
													// TODO Auto-generated method stub
													
												}
											});
										}
									}
								}
							});
						}
					});
					alert.show();					
			    }
			    else{
			    	boolclose = true;
			    }
			}
			catch(Exception e){
				Log.e("Todays Activity Adapter:", e.getLocalizedMessage());
			}
			
			try{
				final HashMap<String, String> datatags = (HashMap<String, String>)v.getTag();
			    if((datatags.get("jobid").equalsIgnoreCase("12")) && datatags.get("jobstatus").equalsIgnoreCase("1"))
			    {
			    	boolclose = false;
					LayoutInflater inf = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					final View vew = inf.inflate(R.layout.jeepcampain, null);
					AlertDialog.Builder build = new Builder(context);
					build.setTitle(datatags.get("firm_name"));
					build.setCancelable(false);
					build.setView(vew);
					build.setPositiveButton("Submit", null);
					build.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
						
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
							Button submit = alert.getButton(AlertDialog.BUTTON_POSITIVE);
							submit.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									EditText remarks = (EditText)vew.findViewById(R.id.rmrks);
									if(new ConnectionDetector(context).isConnectingToInternet()){
										//actplanid,mobile,name,jobid,rem,data
										new CloseVisit(context,mLiView).execute(datatags.get("actplanid"),"","",datatags.get("jobid"),remarks.getText().toString(),"0");
										alert.dismiss();
										mLiView.startRefreshing();
									}
									else{
										Toast.makeText(context, "No Network Found", Toast.LENGTH_SHORT).show();
									}
									
								}
							});
						}
					});					
					alert.show();					
			    }	
			    else{
			    	boolclose = true;
			    }
			    
			    
			}
			catch(Exception e){
				Log.e("Todays Activity Adapter:", e.getLocalizedMessage());
			}
			
/*
			if(boolclose){
	    		Toast.makeText(context, "Activity has been Closed", Toast.LENGTH_SHORT).show();	
			}*/
		   
			
		}
		
    
	};
		
}

class SubmitRetailer extends AsyncTask<String, Void, String>{

	Context context;
	RefreshableListView mLiView;
	ProgressDialog pDialog;
	public SubmitRetailer(Context context, RefreshableListView mLiView){
		this.context = context;
		this.mLiView = mLiView;
	}
	
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		
		  pDialog = new ProgressDialog(context);
			try{
				this.pDialog.setMessage("Please wait ...");
		        this.pDialog.setIndeterminateDrawable(context.getResources().getDrawable(R.drawable.red_progress));
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
		//actid,mandiiid,vill,frm,mobl,demof,distrid,datatags.get("jobid")
		String result="";
		try{
			Webservicerequest wsc = new Webservicerequest();
			ArrayList<String> inputlist = new ArrayList<String>();
			inputlist.add("actplanid");
			inputlist.add(params[0]);
			inputlist.add("mobile");
			inputlist.add(params[4].substring(1, params[4].length()-1));
			inputlist.add("name");
			inputlist.add(params[3].substring(1, params[3].length()-1));
			inputlist.add("jobid");
			inputlist.add(params[7]);
			inputlist.add("districtid");
			inputlist.add(params[6]);
			inputlist.add("mandiid");
			inputlist.add(params[1]);
			inputlist.add("villname");
			inputlist.add(params[2].substring(1, params[2].length()-1));
			inputlist.add("empid");
			inputlist.add(params[8].toString());
			result = wsc.MobileWebservice(context, "farmervisit", inputlist);
			if(result.length()>0)
			{
				ArrayList<String> listvalue=new ArrayList<String>();
				listvalue.add("Message");
				inputlist= wsc.JSONEncoding(result, listvalue);
				
				for(int count=0; count<inputlist.size(); count+=1)
				{
					result = wsc.Decrypt(inputlist.get(count));
				}
			}			
		}
		catch(Exception e){
			e.getMessage();
			result="5";
		}
		return result;
	}
	
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		pDialog.dismiss();
		try{
			if(result.equalsIgnoreCase("0"))
			{
				Toast.makeText(context, "Please Try Again", Toast.LENGTH_LONG).show();
			}
			if(result.equalsIgnoreCase("1"))
			{
				Toast.makeText(context, "Successfully Submitted", Toast.LENGTH_LONG).show();
			}
			else{
				Toast.makeText(context, result, Toast.LENGTH_LONG).show();
			}
		}
		catch(Exception e){
			e.getMessage();
			Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}
		mLiView.startRefreshing();
	}
	
}


class CloseVisit extends AsyncTask<String, Void, String>{

	Context context;
	RefreshableListView mLiView;
	ProgressDialog pDialog;
	public CloseVisit(Context context, RefreshableListView mLiView){
		this.context = context;
		this.mLiView = mLiView;
	}
	
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		
		  pDialog = new ProgressDialog(context);
			try{
				this.pDialog.setMessage("Please wait ...");
		        this.pDialog.setIndeterminateDrawable(context.getResources().getDrawable(R.drawable.red_progress));
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
		//actid,mandiiid,vill,frm,mobl,demof,distrid,datatags.get("jobid")
		String result="";
		try{
			Webservicerequest wsc = new Webservicerequest();
			ArrayList<String> inputlist = new ArrayList<String>();
			inputlist.add("actplanid");
			inputlist.add(params[0]);
			inputlist.add("mobile");
			inputlist.add(params[1]);
			inputlist.add("name");
			inputlist.add(params[2]);
			inputlist.add("jobid");
			inputlist.add(params[3]);
			inputlist.add("reamark");
			inputlist.add(params[4]);
			inputlist.add("datacollect");
			inputlist.add(params[5]);
			result = wsc.MobileWebservice(context, "closevisit", inputlist);
			if(result.length()>0)
			{
				ArrayList<String> listvalue=new ArrayList<String>();
				listvalue.add("Message");
				inputlist= wsc.JSONEncoding(result, listvalue);
				
				for(int count=0; count<inputlist.size(); count+=1)
				{
					result = wsc.Decrypt(inputlist.get(count));
				}
			}			
		}
		catch(Exception e){
			e.getMessage();
			result="5";
		}
		return result;
	}
	
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		pDialog.dismiss();
		try{
			if(result.equalsIgnoreCase("0"))
			{
				Toast.makeText(context, "Please Try Again", Toast.LENGTH_LONG).show();
			}
			if(result.equalsIgnoreCase("1"))
			{
				Toast.makeText(context, "Successfully Submitted", Toast.LENGTH_LONG).show();
				  ArrayList<HashMap<String, String>> logindata =new GetData(context, new Databaseutill(context).getDBAdapterInstance(context)).getAllLogin();
 	    		  String empid = logindata.get(0).get("empid");
 	    		  String districtid = logindata.get(0).get("geoid");
 	    		  new GetActivities().execute(empid,districtid);
			}
			else{
				Toast.makeText(context, result, Toast.LENGTH_LONG).show();
			}
		}
		catch(Exception e){
			e.getMessage();
			Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}
		mLiView.startRefreshing();
	}
	
class GetActivities extends AsyncTask<String, Void, ArrayList<HashMap<String, String>>>{
		
		ProgressDialog pDialog;
		@Override
		protected void onPreExecute() {
			pDialog = new ProgressDialog(context);
			try{
				this.pDialog.setMessage("Please wait ...");
		        this.pDialog.setIndeterminateDrawable(context.getResources().getDrawable(R.drawable.red_progress));
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
				data = new GetData(context, new Databaseutill(context).getDBAdapterInstance(context)).getTodaysActivities(params[0], params[1]);
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
	 				ActivityListAdapter adapter = new ActivityListAdapter(context, result, mLiView);
	 				mLiView.setAdapter(adapter);
				}
				else
		    	  {
		    		 ArrayList<String> str = new ArrayList<String>();
		    		 str.add("No Data Found, Please Pull Down Again to Refresh");
		    		 LazyAdapter adapter = new LazyAdapter(context, str);
		    		 mLiView.setAdapter(adapter);
		    		 mLiView.completeRefreshing();
		    	  }
			}
			catch(Exception e){
				e.getMessage();
			}
			mLiView.completeRefreshing();
			pDialog.dismiss();
		}
		
	}
	

	
}