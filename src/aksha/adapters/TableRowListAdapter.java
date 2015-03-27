package aksha.adapters;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

import com.aksha.mosaic.R;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TableRowListAdapter extends BaseAdapter {

    Context context;
    ArrayList<HashMap<String, String>> str;

   public TableRowListAdapter(Context context,ArrayList<HashMap<String, String>> str) {
		// TODO Auto-generated constructor stub
    	this.context = context;
    	this.str = str;
	}

    public View getView(int position, View convertView, ViewGroup parent){    	
    	 LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	 convertView = inflater.inflate(R.layout.tablerowlistitem, null);
    	 HashMap<String, String> song = str.get(position);
    	 
    	 TextView firm = (TextView)convertView.findViewById(R.id.firm);
    	 TextView name = (TextView)convertView.findViewById(R.id.name);
    	 TextView mobile = (TextView)convertView.findViewById(R.id.mobile);
    	 
    	 firm.setText(song.get("1"));
    	 name.setText(song.get("2"));
    	 mobile.setText(song.get("3"));
    	 
    	 if(position==0 || position%2 == 0){
    		 firm.setBackgroundColor(Color.parseColor("#ccdad2"));
    		 name.setBackgroundColor(Color.parseColor("#ccdad2"));
    		 mobile.setBackgroundColor(Color.parseColor("#ccdad2"));
    	 }
    	 else{
    		 firm.setBackgroundColor(Color.parseColor("#e7edea"));
    		 name.setBackgroundColor(Color.parseColor("#e7edea"));
    		 mobile.setBackgroundColor(Color.parseColor("#e7edea"));
    	 }
    	 
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
