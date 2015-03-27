package aksha.activityplanningth;


import java.util.ArrayList;
import java.util.List;

import aksha.adapters.Myspinner;
import com.aksha.mosaic.R;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AdapterSearch extends ArrayAdapter<Myspinner>{
	
	

	Context mcontext;
	ArrayList<Myspinner> data;
	public AdapterSearch(Context context, int resource, ArrayList<Myspinner> objects) {
		super(context, resource, objects);
	
		mcontext=context;
		data=objects;
	}

	
	
	//	Integer [] itemimg ={R.drawable.buy_now,R.drawable.order_status,R.drawable.account_summary,R.drawable.my_performance,R.drawable.compiance};


	@Override
	public int getCount() {
		
		
		
		return data.size();
	}

	@Override
	public Myspinner getItem(int position) {


		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
	
		
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		 View vi=convertView;
		 
	        if(convertView==null){
	        	
	            LayoutInflater inflater = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            vi = inflater.inflate(R.layout.row_item_search_tipi, null);
	            Myspinner sp=data.get(position);
	        TextView title = (TextView)vi.findViewById(R.id.txtitem); 
	        title.setText(sp.getSpinnerText());
	        title.setTag(sp.getValue());
	  /*      Typeface  face=Typeface.createFromAsset(getAssets(),
	                  "font/CALIBRI.ttf");
			 title.setTypeface(face);
			 
*/	        
	       
	      /*  ImageView thumb_image=(ImageView)vi.findViewById(R.id.list_image);
	        title.setText(itemname[position]);
	        thumb_image.setImageResource(itemimg[position]);
	        */
	        }else{
	        	
	        	  LayoutInflater inflater = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		            vi = inflater.inflate(R.layout.row_item_search_tipi, null);
		            Myspinner sp=data.get(position);
		        TextView title = (TextView)vi.findViewById(R.id.txtitem); 
		        title.setText(sp.getSpinnerText());
		        title.setTag(sp.getValue());
		    /*    ImageView thumb_image=(ImageView)vi.findViewById(R.id.list_image);
		        
		        title.setText(itemname[position]);
		        thumb_image.setImageResource(itemimg[position]);
	        	*/
	        }
		return vi;
	}
	

}
