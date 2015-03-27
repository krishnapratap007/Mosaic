package aksha.adapters;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import aksha.connectiondetector.ConnectionDetector;
import aksha.database.Databaseutill;
import aksha.database.GetData;
import com.aksha.mosaic.R;
import aksha.webservice.Webservicerequest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory.Options;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class PlannedActivitiesGridViewAdapterTHView extends BaseAdapter {

    Context context;
    ArrayList<ArrayList<HashMap<String, String>>> str;
    RefreshableListView mListView;
    ArrayList<String> date;
    GetData get;
    Databaseutill db;
    String encryptedgeoid;
    
   public PlannedActivitiesGridViewAdapterTHView(Context context,ArrayList<ArrayList<HashMap<String, String>>> category, RefreshableListView mListView, ArrayList<String> date2) {
		// TODO Auto-generated constructor stub
    	this.context = context;
    	str = category;
    	this.mListView = mListView;
    	date = date2;
    	db = Databaseutill.getDBAdapterInstance(context);
    	get = new GetData(context, db);
	}


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return date.size();
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
   
   public class ViewHolder{
       TextView date;
       ImageView shiftbutton;
   //    MyGridView gv;
     //  ListView gv;
       RelativeLayout card;
       LinearLayout parent;
   }
   

    public View getView(final int position, View convertView1, ViewGroup parent){
    	 ViewHolder holder;
    	 LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	 convertView1 =inflater.inflate(R.layout.activitygridlayoutview , null);
    	 ArrayList<HashMap<String, String>> song = new ArrayList<HashMap<String, String>>();
		     song = str.get(position);
		 
         holder = new ViewHolder();
         holder.card = (RelativeLayout)convertView1.findViewById(R.id.rlcard);
         holder.date = (TextView)convertView1.findViewById(R.id.activitydate);
         holder.shiftbutton = (ImageView)convertView1.findViewById(R.id.shiftbutton);
      //   holder.gv = (MyGridView)convertView.findViewById(R.id.activities);
       //  holder.addnewactivity = (LinearLayout)convertView1.findViewById(R.id.addnewactivity);
         holder.parent = (LinearLayout)convertView1.findViewById(R.id.parent);
  
         
         SimpleDateFormat stringdate = new SimpleDateFormat("M/dd/yyyy HH:mm:ss aa");
      //   SimpleDateFormat stringdate = new SimpleDateFormat("dd MMM yyyy EEEE");
         String dte = date.get(position);
         try{								
        	 Date dt = stringdate.parse(dte);
        	 dte = new SimpleDateFormat("d MMM yyyy EEEE").format(dt);
        	 holder.date.setText(dte);
         }
         catch(Exception e){
			e.getMessage();
         }         
         holder.date.setText(dte);
         holder.shiftbutton.setVisibility(View.GONE);
       //  holder.addnewactivity.setTag(dte);
         
        /* holder.addnewactivity.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

					LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					View activity = inflater.inflate(R.layout.createactivity, null);
					
					final Spinner districtlist = (Spinner)activity.findViewById(R.id.district);
					final Spinner mandilist = (Spinner)activity.findViewById(R.id.mandi);
					final Spinner retailerslist = (Spinner)activity.findViewById(R.id.retailers);
					final Spinner activitieslist = (Spinner)activity.findViewById(R.id.activitieslist);
					final EditText remarks = (EditText)activity.findViewById(R.id.remarks);				
					
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
						ArrayList<HashMap<String, String>> dist = new GetData(context, db).getfadistrict("194");								
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
	 				ArrayAdapter<Myspinner> adapter1 = new ArrayAdapter<Myspinner>(context, android.R.layout.simple_spinner_dropdown_item, redemo1);
	 				adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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
				 				ArrayAdapter<Myspinner> adapter2 = new ArrayAdapter<Myspinner>(context, android.R.layout.simple_spinner_dropdown_item, redemo2);
				 				adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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
				 				ArrayAdapter<Myspinner> adapter2 = new ArrayAdapter<Myspinner>(context, android.R.layout.simple_spinner_dropdown_item, redemo2);
				 				adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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
				 				ArrayAdapter<Myspinner> adapter3 = new ArrayAdapter<Myspinner>(context, android.R.layout.simple_spinner_dropdown_item, redemo3);
				 				adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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
				 				ArrayAdapter<Myspinner> adapter3 = new ArrayAdapter<Myspinner>(context, android.R.layout.simple_spinner_dropdown_item, redemo3);
				 				adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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
	 				ArrayAdapter<Myspinner> adapter2 = new ArrayAdapter<Myspinner>(context, android.R.layout.simple_spinner_dropdown_item, redemo2);
	 				adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	 				mandilist.setAdapter(adapter2); 				 			
	 				
	 				Myspinner[] redemo3 = new Myspinner[category3.size()];
	 				for(int i=0; i<category3.size(); i++){
	 					redemo3[i] = new Myspinner(category3.get(i).get("2"), category3.get(i).get("1"), "");
	 				}
	 				ArrayAdapter<Myspinner> adapter3 = new ArrayAdapter<Myspinner>(context, android.R.layout.simple_spinner_dropdown_item, redemo3);
	 				adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	 				retailerslist.setAdapter(adapter3); 				 				
	 				
	 				Myspinner[] redemo4 = new Myspinner[category4.size()];
	 				for(int i=0; i<category4.size(); i++){
	 					redemo4[i] = new Myspinner(category4.get(i).get("2"), category4.get(i).get("1"), "");
	 				}
	 				ArrayAdapter<Myspinner> adapter4 = new ArrayAdapter<Myspinner>(context, android.R.layout.simple_spinner_dropdown_item, redemo4);
	 				adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	 				activitieslist.setAdapter(adapter4);
					
	 				try{
	 				final Date dt = new SimpleDateFormat("d MMMM yyyy").parse(v.getTag().toString());	 				
					AlertDialog.Builder build = new Builder(context)
					.setTitle(""+new SimpleDateFormat("d MMM yyyy").format(dt))
					.setView(activity)
					.setPositiveButton("Create", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							//dist,date,empId,spmandi,spretailer,spactivity,rem
							if(new ConnectionDetector(context).isConnectingToInternet()){
								Myspinner sp1 = (Myspinner)districtlist.getSelectedItem();
								Myspinner sp2 = (Myspinner)mandilist.getSelectedItem();
								Myspinner sp3 = (Myspinner)retailerslist.getSelectedItem();
								Myspinner sp4 = (Myspinner)activitieslist.getSelectedItem();
								try{
									String date = new SimpleDateFormat("yyyy-MM-dd").format(dt);
									new CreateActivity().execute(sp1.getValue(),date,get.getAllLogin().get(0).get("empid"),sp2.getValue(),sp3.getValue(),sp4.getValue(),remarks.getText().toString());
								}
								catch(Exception e){
									e.getMessage();
								}							
							}
							else{
								Toast.makeText(context, "No Network Found", Toast.LENGTH_LONG).show();
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
	 				catch(Exception e){
	 					e.getMessage();
	 				}
				
			}
         });*/
         
       /*  holder.addnewactivity.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(context, "Create new Activity for "+v.getTag().toString(), Toast.LENGTH_LONG).show();
				return true;
			}
		});
         */
   //      holder.gv.setAdapter(new CalenderActivityListAdapter(context, song));
       //  Helper.getListViewSize(holder.gv);
         int cnt = 0;
         int count=0;
         for (HashMap<String, String> hashMap : song) {
        	 LayoutInflater inf = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        	 View vew =inf.inflate(R.layout.activitylistitemview, null);
        	 LinearLayout datelayout	=	(LinearLayout)vew.findViewById(R.id.datelayout);
             TextView date				=	(TextView)vew.findViewById(R.id.date);
             TextView monthyear			=	(TextView)vew.findViewById(R.id.monthyear);
             TextView name				=	(TextView)vew.findViewById(R.id.name);
             TextView email				=	(TextView)vew.findViewById(R.id.email);
             TextView address			=	(TextView)vew.findViewById(R.id.address);
             TextView mobile			=	(TextView)vew.findViewById(R.id.mobile);
             TextView village			=	(TextView)vew.findViewById(R.id.village);

             
             //#f56545---red -- missed plan --- 1 -- past
             //#27ae60---green  -- active close  --2
             //#ffbb22---yellow  -- approved
             //#941494 --- purple --- unapproved
             
             
                SimpleDateFormat stringdate1 = new SimpleDateFormat("M/dd/yyyy");
                String seldate1 = hashMap.get("plan_date");
             
                Calendar cal1 = Calendar.getInstance();
                String currdate1 = stringdate1.format(cal1.getTime());
                
           
    				
    			     if(hashMap.get("planstatus").equalsIgnoreCase("1") && hashMap.get("jobstatus").equalsIgnoreCase("1")){
    			    	
    			    	 try {
    						if(stringdate1.parse(seldate1).before(stringdate1.parse(currdate1))){
    						    datelayout.setBackgroundColor(Color.parseColor("#f56545"));
    						 }
    						 else{
    						   datelayout.setBackgroundColor(Color.parseColor("#ffbb22")); 
    						 }
    					} catch (ParseException e) {
    						// TODO Auto-generated catch block
    						e.printStackTrace();
    					}
    			    	 
    			     }
    			     else if(hashMap.get("planstatus").equalsIgnoreCase("1") && hashMap.get("jobstatus").equalsIgnoreCase("2")){
    			    	 try {
    						if(stringdate1.parse(seldate1).before(stringdate1.parse(currdate1))){
    						    datelayout.setBackgroundColor(Color.parseColor("#27ae60"));
    						 }
    						 else{
    							datelayout.setBackgroundColor(Color.parseColor("#27ae60")); 
    						 }
    					} catch (ParseException e) {
    						// TODO Auto-generated catch block
    						e.printStackTrace();
    					}
    			    	 
    			     }
    			  else if(hashMap.get("planstatus").equalsIgnoreCase("2") && hashMap.get("jobstatus").equalsIgnoreCase("1")){
 			    	 try {
 						if(stringdate1.parse(seldate1).before(stringdate1.parse(currdate1))){
 						    datelayout.setBackgroundColor(Color.parseColor("#941494"));
 						 }
 						 else{
 							datelayout.setBackgroundColor(Color.parseColor("#941494")); 
 						 }
 					} catch (ParseException e) {
 						// TODO Auto-generated catch block
 						e.printStackTrace();
 					}
 			    	 
 			     }
    		
    			     else{
    			    	 datelayout.setBackgroundColor(Color.parseColor("#941494"));
    			     }
    			
            
//             SimpleDateFormat stringdate = new SimpleDateFormat("dd MMM yyyy EEEE");
             String dtm = hashMap.get("plan_date");
             try{								
            	 //09 Mar 2015 Monday
            	 Date dt = stringdate.parse(dtm);
            	 dtm = new SimpleDateFormat("d,MMM yyyy EEEE").format(dt);
            	 date.setText(dtm.split(",")[0]);
                 monthyear.setText(dtm.split(",")[1]);
             }
             catch(Exception e){
    			e.getMessage();
             }
             cnt++;
             date.setText(String.valueOf(cnt));
             date.setGravity(Gravity.CENTER);
             monthyear.setVisibility(View.GONE);
             vew.setTag(hashMap);
             name.setText(hashMap.get("firm_name"));
             email.setText(hashMap.get("jobdesc"));
             address.setText("District- "+hashMap.get("districtname"));
             mobile.setText("Mandi-  "+hashMap.get("mandi"));
             
             try{
            	 if(hashMap.get("village").length()>0)
                 {
                	 village.setText("Village-  "+hashMap.get("village"));
                 }
                 else
                 {
                	 village.setVisibility(View.GONE);
                 }
             }
             catch(Exception e){
            	 e.getMessage();
             }
             holder.parent.addView(vew);
         }
            
         Animation animation = AnimationUtils.loadAnimation(context, R.anim.card_animation);
         holder.card.startAnimation(animation);
         return convertView1;
    }
    
    
    class DeleteActivity extends AsyncTask<String, Void, String>{

    	@Override
    	protected void onPreExecute() {    		
    		    		
    	}
    	@Override
		protected String doInBackground(String... params) {
			GetData get=new GetData(context, db);
    		String data = get.DeleteData(params[0]);
			return null;
		}
    	@Override
    	protected void onPostExecute(String result) {    		
    		
    	}
		
		
    	
    }    

    class ApproveActivity extends AsyncTask<String, Void, String>{
    	@Override
    	protected void onPreExecute() {    		
    		    		
    	}
		@Override
		protected String doInBackground(String... params) {
			String data = get.ApproveData(params[0]);
			return null;
		}
		@Override
    	protected void onPostExecute(String result) {    		
    		
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
		String str = "0";
		ConnectionDetector cd = new ConnectionDetector(context);
		if(cd.isConnectingToInternet())
		{
			try{
				db = Databaseutill.getDBAdapterInstance(context);
				get = new GetData(context,db);
				str = get.createActivity(params[0], params[1], params[2], params[3], params[4], params[5], params[6],"","");
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
				Toast.makeText(context, "Some Error Occured Please try again", Toast.LENGTH_LONG).show();
			}
			else
			{
				if(result.equalsIgnoreCase("3"))
				{
					Toast.makeText(context, "No Network Found", Toast.LENGTH_LONG).show();
				}
				else
				{
					if(result.equalsIgnoreCase("2"))
					{
						Toast.makeText(context, "This Activity is Already planned", Toast.LENGTH_LONG).show();
					}
					else
					{
						Toast.makeText(context, "Planned Successfully", Toast.LENGTH_LONG).show();
					}
				}
			}
		}
		else
		{
			Toast.makeText(context, "Some Error Occured Please try again", Toast.LENGTH_LONG).show();
		}
		mListView.startRefreshing();
	}
			
}


    
}
