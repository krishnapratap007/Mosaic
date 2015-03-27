package aksha.adapters;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.aksha.mosaic.R;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory.Options;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PlannedActivitiesListAdapter extends BaseAdapter {

    Context context;
    ArrayList<HashMap<String, String>> str;
    RefreshableListView mListView;

   public PlannedActivitiesListAdapter(Context context,ArrayList<HashMap<String, String>> category, RefreshableListView mListView) {
		// TODO Auto-generated constructor stub
    	this.context = context;
    	str = category;
    	this.mListView = mListView;
	}

   
   public class ViewHolder{
       TextView name;
       TextView email;
       TextView address;
       TextView mobile;
       TextView village;
       RelativeLayout card;
       Button editactivity;
   }
   

    public View getView(int position, View convertView, ViewGroup parent){
    	 ViewHolder holder;
    	 LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	 convertView =inflater.inflate(R.layout.plannedactivitieslistitem , null);
    	 final HashMap<String, String> song = str.get(position);		     
		 
         holder = new ViewHolder();
         holder.card = (RelativeLayout)convertView.findViewById(R.id.relcard);
         holder.name = (TextView)convertView.findViewById(R.id.name);
         holder.email = (TextView)convertView.findViewById(R.id.email);
         holder.address = (TextView)convertView.findViewById(R.id.address);
         holder.mobile = (TextView)convertView.findViewById(R.id.mobile);
         holder.village = (TextView)convertView.findViewById(R.id.village);
         holder.editactivity = (Button)convertView.findViewById(R.id.editactivity);
         
         holder.name.setText(song.get("firm_name"));
         holder.email.setText(song.get("jobdesc"));
         holder.address.setText("District- "+song.get("districtname"));
         holder.mobile.setText("Mandi-  "+song.get("mandi"));
         if(song.get("village").length()>0)
         {
        	 holder.village.setText("Village-  "+song.get("village"));
         }
         else
         {
        	 holder.village.setVisibility(View.GONE);
         }
         holder.editactivity.setTag(song.get("1"));
         
         holder.editactivity.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(context, "Edit Activity of "+song.get("firm_name"), Toast.LENGTH_LONG).show();
				mListView.startRefreshing();
			}
		});
            
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
