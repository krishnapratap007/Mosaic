package aksha.adapters;

import java.util.ArrayList;
import java.util.HashMap;

import com.aksha.mosaic.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ActivityListAdapterReportmandi extends BaseAdapter {

   Context context;
   ArrayList<HashMap<String, String>> str;
   RefreshableListView mLiView;
   int count=0;
  
   public ActivityListAdapterReportmandi(Context context,ArrayList<HashMap<String, String>> category, RefreshableListView mListView) {
		// TODO Auto-generated constructor stub
    	this.context = context;
    	str = category;
        int count=0;
    	mLiView = mListView;
    	/*int min = 0;
    	int max = 7;
    	Random r = new Random();
    	count = r.nextInt(max - min) + min;*/
	}

   
   public class ViewHolder{
	   
	   TextView title;
       TextView doneper;
       TextView donecount;
       TextView exeper;
       TextView execount;
       TextView txtplab;
       ProgressBar brdone;
       ProgressBar brexe;
	public Object card;
              
       
   }
   
   HashMap<String, String> song = new HashMap<String, String>();
   
    public View getView(int position, View convertView, ViewGroup parent){
    	final ViewHolder holder;
    	 LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	 convertView =inflater.inflate(R.layout.reportshowmandifa, null);
    	  
		     song = str.get(position);
		 
         holder				=	new ViewHolder();
       
         holder.title		=	(TextView)convertView.findViewById(R.id.title);
         holder.exeper		=	(TextView)convertView.findViewById(R.id.txtexeper);
         holder.execount	=	(TextView)convertView.findViewById(R.id.txtexecount);
         holder.txtplab		=	(TextView)convertView.findViewById(R.id.txtplab);
         
         holder.brexe		=	(ProgressBar)convertView.findViewById(R.id.pBar2_main);
         
        // if(count==0){
        	 holder.title.setText(song.get("1"));
        	 holder.txtplab.setText("Verified");
        	 count++;
         //}
         holder.execount.setText(song.get("2"));   
         holder.brexe.setMax(Double.valueOf(song.get("2")).intValue());
         holder.brexe.setProgress(Double.valueOf(song.get("2")).intValue()); 
         holder.exeper.setText("");
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
	
	
}
