package aksha.adapters;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

public class PendingActivityListAdapter extends BaseAdapter {

   Context context;
   ArrayList<HashMap<String, String>> str;
   RefreshableListView mLiView;
   int count=0;

   public PendingActivityListAdapter(Context context,ArrayList<HashMap<String, String>> category, RefreshableListView mListView) {
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
       TextView remark;
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
         holder.village		=	(TextView)convertView.findViewById(R.id.villagename);
         holder.remark		=	(TextView)convertView.findViewById(R.id.village);
         holder.sp		=	(Spinner)convertView.findViewById(R.id.spedit);
         holder.sp.setVisibility(View.GONE);
         holder.remark.setVisibility(View.GONE);
         
         //#f56545---red -- missed plan
         //#27ae60---green  -- active close
         //#ffbb22---yellow  -- approved
         //#941494 --- purple --- unapproved
         
         if(song.get("jobstatus").equalsIgnoreCase("1")){
        	 holder.datelayout.setBackgroundColor(Color.parseColor("#f56545"));
        	 
         }
         else if(song.get("jobstatus").equalsIgnoreCase("2")){
        	 holder.datelayout.setBackgroundColor(Color.parseColor("#27ae60"));
        	 
         }
         else {
        	 holder.datelayout.setBackgroundColor(Color.parseColor("#f56545"));
        	 
         }
         
         //"plandate","districtid","mandi","retailer","jobdesc","villname"
         SimpleDateFormat stringdate = new SimpleDateFormat("yyyy-MM-dd");
         String date = song.get("plandate");
         try{								
        	 Date dt = stringdate.parse(song.get("plandate"));
        	 date = new SimpleDateFormat("d,MMM yyyy").format(dt);
        	 holder.date.setText(date.split(",")[0]);
             holder.monthyear.setText(date.split(",")[1]);
         }
         catch(Exception e){
			e.getMessage();
         }         
         holder.card.setTag(song);
         holder.name.setText(song.get("retailer"));
         holder.email.setText(song.get("jobdesc"));
         holder.address.setText("District- "+song.get("districtid"));
         holder.mobile.setText("Mandi-  "+song.get("mandi"));
         if(song.get("villname").length()>0)
         {
        	 holder.village.setText("Village-  "+song.get("villname"));
         }
         else
         {
        	 holder.village.setVisibility(View.GONE);
         }
                  
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
