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
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class ActivityListAdapterStockTH extends BaseAdapter {
   Context context;
   ArrayList<HashMap<String, String>> str;
   RefreshableListView mLiView;
   int count=0;
String distid;
   public ActivityListAdapterStockTH(Context context,ArrayList<HashMap<String, String>> category, RefreshableListView mListView,String distid) {
		// TODO Auto-generated constructor stub
    	this.context = context;
    	str = category;
    	this.distid=distid;
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
   
   HashMap<String, String> song = new HashMap<String, String>();
    public View getView(int position, View convertView, ViewGroup parent){
    	final ViewHolder holder;
    	 LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	 convertView =inflater.inflate(R.layout.activitylistitemfa, null);
    	  
		     song = str.get(position);
		//jobdesc= Retailer visit, plandate=2015-03-05, villname=vnkhedi, mandi=Pipariya, retailer=Lalchand Mohanlal, districtid=Hoshangabad 
		 
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
         
         if(count==0){
        	 holder.datelayout.setBackgroundColor(Color.parseColor("#f56545"));
        	 count++;
         }
         else if(count==1){
        	 holder.datelayout.setBackgroundColor(Color.parseColor("#ffbb22"));
        	 count++;
         }
         else if(count==2){
        	 holder.datelayout.setBackgroundColor(Color.parseColor("#3498db"));
        	 count++;
         }
         else if(count==3){
        	 holder.datelayout.setBackgroundColor(Color.parseColor("#27ae60"));
        	 count++;
         }
         else if(count==4){
        	 holder.datelayout.setBackgroundColor(Color.parseColor("#34495e"));
        	 count++;
         }
         else if(count==5){
        	 holder.datelayout.setBackgroundColor(Color.parseColor("#e67e22"));
        	 count++;
         }
         else if(count==6){
        	 holder.datelayout.setBackgroundColor(Color.parseColor("#9b59b6"));
        	 count++;
         }
         else if(count==7){
        	 holder.datelayout.setBackgroundColor(Color.parseColor("#e74c3c"));
        	 count=0;
         }
         holder.mobile.setText("Updated on - "+song.get("5"));
         try{
        	 Date dt = new SimpleDateFormat("yyyy-MM-dd").parse(song.get("5"));
        	 //holder.date.setText(new SimpleDateFormat("d,MMM yyyy").format(dt).split(",")[0]);
        	 holder.mobile.setText("Updated on - "+new SimpleDateFormat("d,MMM yyyy").format(dt));
         }
         catch(Exception e){
        	 e.getMessage();
         }
         holder.monthyear.setText("");
         holder.date.setText(String.valueOf(position+1));
         holder.card.setTag(song);
         holder.name.setText(song.get("2"));
         holder.email.setText("Mobile-"+song.get("3"));
         holder.address.setText("Qnty- "+song.get("4"));
                          
        	 holder.village.setVisibility(View.GONE);                         
         holder.villagename.setVisibility(View.GONE);
         
     	ArrayList<String> datav=new ArrayList<String>();
     	datav.add("Select Item");
     	datav.add("Update Qnty");
     	             
     	ArrayAdapter<String> adaptersp = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, datav);
     	adaptersp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
     	holder.sp.setAdapter(adaptersp);
     	holder.sp.setSelection(0, false);
     	holder.sp.setTag(R.id.tag_first, song.get("1"));
     	holder.sp.setTag(R.id.tag_second, song.get("2"));
     	holder.sp.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {		
				try{
					
				String dat=String.valueOf(holder.sp.getSelectedItem());
				if(dat.equalsIgnoreCase("Update Qnty"))
				{
					//new ApproveActivity().execute(String.valueOf(holder.sp.getTag()));
					LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					final View activity = inflater.inflate(R.layout.editqnty, null);
					
					//
					AlertDialog.Builder build = new Builder(context)
					.setTitle(String.valueOf(holder.sp.getTag(R.id.tag_second)))
					.setView(activity)
					.setPositiveButton("Update", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							
						
						
							
						}
					})
					.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							dialog.cancel();
						}
					});
				final	AlertDialog alert = build.create();
					alert.setOnShowListener(new OnShowListener() {
						
						@Override
						public void onShow(final DialogInterface dialog) {
							
							
							
							 Button b = alert.getButton(AlertDialog.BUTTON_POSITIVE);
						        b.setOnClickListener(new OnClickListener() {
									
									@Override
									public void onClick(View v) {
										
										
										if(new ConnectionDetector(context).isConnectingToInternet()){
											
											//update qnty
											String chanid=String.valueOf(holder.sp.getTag(R.id.tag_first));
											EditText et=(EditText)activity.findViewById(R.id.etqnty);
											if(et.getText().length()>0)
											{
												//call task
												new Stockupdate().execute(String.valueOf(chanid),String.valueOf(et.getText()));
												dialog.dismiss();
											}
											else{
												Toast.makeText(context, "Please enter quantity", Toast.LENGTH_LONG).show();
											}
											
										}
										else{
											Toast.makeText(context, "No Network Found", Toast.LENGTH_LONG).show();
										}
										
									}
								});

						
						
						

							
													
							
						}});
					
					
					alert.show();

					
					
					
					//
					
					
					
					
					
				}
					
				
				}catch(Exception er){er.getMessage();}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
				
			}
     	 
		});

         
         
         Animation animation = AnimationUtils.loadAnimation(context, R.anim.card_animation);
         holder.card.startAnimation(animation);
         
         return convertView;
    }

    class Stockupdate extends AsyncTask<String, Void, String>{
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
		protected String doInBackground(String... params) {
			
			ArrayList<HashMap<String, String>> dv=new GetData(context, new Databaseutill(context).getDBAdapterInstance(context)).getAllLogin();
			
			 Webservicerequest wsc = new Webservicerequest();	
			 ArrayList<String> inputlist = new ArrayList<String>();
				inputlist.add("frim_id");
				inputlist.add(params[0]);
				inputlist.add("quantity");
				inputlist.add(params[1]);
				inputlist.add("districtid");
				inputlist.add(distid);
				inputlist.add("faid");
				inputlist.add(dv.get(0).get("empid"));
			     String resultdata=wsc.MobileWebservice(context, "updatestock",inputlist);
			
			
			return resultdata;
		}
		@Override
    	protected void onPostExecute(String result) {    		
    		pDialog.dismiss();
    		new GetStocks().execute(distid);
    	}
    	
    }    
    public	class GetStocks extends AsyncTask<String, Void, ArrayList<HashMap<String, String>>>
	{

		@Override
		protected ArrayList<HashMap<String, String>> doInBackground(String... params) {
			
			
			ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
			try{
				String selectionargdup = "firm_id,firm_name,mobile,qnty,updatedate";
				 Webservicerequest wsc = new Webservicerequest();	
				 ArrayList<String> inputlist = new ArrayList<String>();
					inputlist.add("districtid");
					inputlist.add(params[0]);
							
				     String resultdata=wsc.MobileWebservice(context, "getstockTH",inputlist);
				     ArrayList<String> listvalue=new ArrayList<String>();
				        if (resultdata != null)
				        {
				          String[] arrayOfString = selectionargdup.split(",");
				          for (int i = 0;i<arrayOfString.length; i++)
				          {
				            listvalue.add(arrayOfString[i]);
				          }
				          
				            	inputlist=wsc.JSONEncoding(resultdata, listvalue);
				            //	listvalue.clear();
				            /*	for (String string : inputlist) {
				            		listvalue.add(wsc.Decrypt(string));
								}*/
				            
				            	for(int icount=0;icount<inputlist.size();icount+=5)
				            	{
				            		HashMap<String, String> map=new HashMap<String, String>();
				            		map.put("1", wsc.Decrypt(inputlist.get(icount)));
				            		map.put("2", wsc.Decrypt(inputlist.get(icount+1)));
				            		map.put("3", wsc.Decrypt(inputlist.get(icount+2)));
				            		map.put("4", wsc.Decrypt(inputlist.get(icount+3)));
				            		map.put("5", wsc.Decrypt(inputlist.get(icount+4)));
				            		result.add(map);
				            		
				            	}
				        }
				        
				        
				        
				     
			}
			catch(Exception e){
				e.getMessage();
			}
			return result;
			
			
		}
		
		@Override
		protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
			
			try{
				if(result!=null && result.size()>0){
	 				ActivityListAdapterStockTH adapter = new ActivityListAdapterStockTH(context, result, mLiView,distid);
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
		
		}
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
	
	
}
