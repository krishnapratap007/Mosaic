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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class ActivityListAdapterReportfarmerTh extends BaseAdapter {

   Context context;
   ArrayList<HashMap<String, String>> str;
   RefreshableListView mLiView;
   int count=0;
  
   public ActivityListAdapterReportfarmerTh(Context context,ArrayList<HashMap<String, String>> category, RefreshableListView mListView) {
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
              
       
   }
   
   HashMap<String, String> song = new HashMap<String, String>();
   
    public View getView(int position, View convertView, ViewGroup parent){
    	final ViewHolder holder;
    	 LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	 convertView =inflater.inflate(R.layout.reportshow, null);
    	  
		     song = str.get(position);
	 
		 
         holder				=	new ViewHolder();
       
         holder.title		=	(TextView)convertView.findViewById(R.id.title);
         holder.doneper		=	(TextView)convertView.findViewById(R.id.txtdoneper);
         holder.donecount	=	(TextView)convertView.findViewById(R.id.txtdonecount);
         holder.exeper		=	(TextView)convertView.findViewById(R.id.txtexeper);
         holder.execount	=	(TextView)convertView.findViewById(R.id.txtexecount);
         
         holder.txtplab		=	(TextView)convertView.findViewById(R.id.txtplab);
         
         holder.brdone		=	(ProgressBar)convertView.findViewById(R.id.pBar1_main);
         holder.brexe		=	(ProgressBar)convertView.findViewById(R.id.pBar2_main);
         
         
         if(count==0){
        	 holder.title.setText("Activities");
        	 holder.txtplab.setText("Done");
        	 count++;
         }
         else{
        	 holder.title.setText("Farmer"); 
        	 holder.txtplab.setText("Verified");
         }
        // holder.title.setText(song.get("1"));
         
         holder.donecount.setText(song.get("2"));
         holder.execount.setText(song.get("3"));   
         
         holder.brdone.setMax(Double.valueOf(song.get("2")).intValue());
         holder.brexe.setMax(Double.valueOf(song.get("2")).intValue());
         
         holder.brdone.setProgress(Double.valueOf(song.get("2")).intValue());
      
         holder.brexe.setProgress(Double.valueOf(song.get("3")).intValue()); 
         holder.doneper.setText("");
         holder.exeper.setText("");
         /*Animation animation = AnimationUtils.loadAnimation(context, R.anim.card_animation);
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
