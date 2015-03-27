package aksha.adapters;

import java.util.ArrayList;
import java.util.HashMap;

import aksha.database.Databaseutill;
import aksha.database.GetData;
import com.aksha.mosaic.R;
import aksha.upcomingdemo.HorizontalListView;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityListAdapterFarmerHorz extends BaseAdapter {

   Context context;
   ArrayList<HashMap<String, String>> str;
   HorizontalListView mLiView;
   int count=0;
  
   public ActivityListAdapterFarmerHorz(Context context,ArrayList<HashMap<String, String>> category, HorizontalListView mListView) {
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
   
   HashMap<String, String> song = new HashMap<String, String>();
    
   public View getView(int position, View convertView, ViewGroup parent){
    	final ViewHolder holder;
    	 LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	 convertView =inflater.inflate(R.layout.activitylistitemhorzfa, null);
    	  
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
         holder.sp.setVisibility(View.GONE);
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
         
         holder.mobile.setVisibility(View.GONE);         
         holder.monthyear.setText("");
         holder.date.setText(String.valueOf(position+1));
         
         holder.name.setText(song.get("1"));
         holder.email.setText("Village-"+song.get("2"));
         holder.address.setText("Mobile- "+song.get("3"));  
         
         holder.card.setTag(song);
       //  holder.card.setOnClickListener(newclicklistner);
         holder.village.setVisibility(View.GONE);                         
         holder.villagename.setVisibility(View.GONE);              	                  
        /* Animation animation = AnimationUtils.loadAnimation(context, R.anim.card_animation);
         holder.card.startAnimation(animation);*/
         
         
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
	
	
	
	
	
	
	
	
	
private OnClickListener newclicklistner = new OnClickListener() {
		
		final ArrayList<HashMap<String,String>> categorytask2=new ArrayList<HashMap<String,String>>();
		
		@Override
		public void onClick(View v) {
			
			// TODO Auto-generated method stub
			final GetData get = new GetData(context, Databaseutill.getDBAdapterInstance(context));
			
			Toast.makeText(context, "Activity has been Closed", Toast.LENGTH_SHORT).show();

		     	LayoutInflater inf = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		    	final View vew = inf.inflate(R.layout.managedemofa, null);
			
					
					TableLayout tableview = (TableLayout)vew.findViewById(R.id.tableview);
					tableview.setVisibility(View.VISIBLE);
					
					final Button submit = (Button)vew.findViewById(R.id.finish);
					submit.setVisibility(View.VISIBLE);
				
					
			
		}
		
    
	};
	
	
	
	
	
	
	
	
	
	
	
	
}
