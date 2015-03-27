package aksha.adapters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import com.aksha.mosaic.R;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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

public class ActivityListAdapterTaskTH extends BaseAdapter {

   Context context;
   ArrayList<HashMap<String, String>> str;
   RefreshableListView mLiView;
   int count=0;

   public ActivityListAdapterTaskTH(Context context,ArrayList<HashMap<String, String>> category, RefreshableListView mListView) {
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
       TextView faname;
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
    	 convertView =inflater.inflate(R.layout.activitylistitem, null);
    	 HashMap<String, String> song = new HashMap<String, String>();
		     song = str.get(position);
		//jobdesc= Retailer visit, plandate=2015-03-05, villname=vnkhedi, mandi=Pipariya, retailer=Lalchand Mohanlal, districtid=Hoshangabad 
		 
         holder				=	new ViewHolder();
         holder.card		=	(RelativeLayout)convertView.findViewById(R.id.card);
         holder.datelayout	=	(LinearLayout)convertView.findViewById(R.id.datelayout);
         holder.date		=	(TextView)convertView.findViewById(R.id.date);
         holder.monthyear	=	(TextView)convertView.findViewById(R.id.monthyear);
         holder.name		=	(TextView)convertView.findViewById(R.id.name);
         holder.faname		=	(TextView)convertView.findViewById(R.id.faname);
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
         
         
         
         
         //#f56545---red -- missed plan --- 1 -- past
         //#27ae60---green  -- active close  --2
         //#ffbb22---yellow  -- approved  -- open
         //#941494 --- purple --- unapproved
         
         
          /*  SimpleDateFormat stringdate1 = new SimpleDateFormat("M/dd/yyyy");
            String seldate = song.get("plan_date");
         
            Calendar cal = Calendar.getInstance();
            String currdate = stringdate1.format(cal.getTime());
            
       
				
			     if(song.get("planstatus").equalsIgnoreCase("1") && song.get("jobstatus").equalsIgnoreCase("1")){
			    	
			    	 try {
						if(stringdate1.parse(seldate).before(stringdate1.parse(currdate))){
						  holder.datelayout.setBackgroundColor(Color.parseColor("#f56545"));
						 }
						 else{
						   holder.datelayout.setBackgroundColor(Color.parseColor("#ffbb22")); 
						 }
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			    	 
			     }
			     else if(song.get("planstatus").equalsIgnoreCase("1") && song.get("jobstatus").equalsIgnoreCase("2")){
			    	 try {
						if(stringdate1.parse(seldate).before(stringdate1.parse(currdate))){
						  holder.datelayout.setBackgroundColor(Color.parseColor("#27ae60"));
						 }
						 else{
							 holder.datelayout.setBackgroundColor(Color.parseColor("#27ae60")); 
						 }
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			    	 
			     }
			     else{
			    	 holder.datelayout.setBackgroundColor(Color.parseColor("#941494"));
			     }
			
         */
       
         try{
        	 Date dt = new SimpleDateFormat("yyyy-MM-dd").parse(song.get("plandate"));
        	 holder.date.setText(new SimpleDateFormat("d,MMM yyyy").format(dt).split(",")[0]);
        	 holder.monthyear.setText(new SimpleDateFormat("d,MMM yyyy").format(dt).split(",")[1]);
         }
         catch(Exception e){
        	 e.getMessage();
         }
         holder.card.setTag(song);
         
         if(song.get("empname").length()>0)
         {
        	 holder.faname.setText("MDO- "+song.get("empname"));
         }
         else
         {
        	 holder.faname.setVisibility(View.GONE);
         }
         holder.name.setText(song.get("retailer"));
         holder.email.setText(song.get("jobdesc"));
         holder.address.setText("District- "+song.get("districtid"));
         holder.mobile.setText("Mandi- "+song.get("mandi"));
         if(song.get("villname").length()>0)
         {
        	 holder.village.setText("Village- "+song.get("villname"));
         }
         else
         {
        	 holder.village.setVisibility(View.GONE);
         }
         
       //  holder.card.setOnClickListener(clicklistner);
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
		
}
