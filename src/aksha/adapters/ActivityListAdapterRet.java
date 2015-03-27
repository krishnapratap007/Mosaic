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

public class ActivityListAdapterRet extends BaseAdapter {

   Context context;
   ArrayList<HashMap<String, String>> str;
   RefreshableListView mLiView;
   int count=0;
  
   public ActivityListAdapterRet(Context context,ArrayList<HashMap<String, String>> category, RefreshableListView mListView) {
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
    	 convertView =inflater.inflate(R.layout.activitylistitemfa, null);
    	  
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
         holder.card.setTag(song);
         holder.name.setText(song.get("1"));
         holder.email.setText("Mobile-"+song.get("2"));
         holder.address.setText("Mandi- "+song.get("3"));                          
        	 holder.village.setVisibility(View.GONE);                         
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
