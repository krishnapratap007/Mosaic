package aksha.adapters;


import com.aksha.mosaic.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LeftSlideList extends BaseAdapter{


	String [] itemname={"POG","Activity Planning","Retailer"};
	Integer [] itemimg ={R.drawable.product_list,R.drawable.product_list,R.drawable.product_list};
	
	
	Context mcontext;
	
	public LeftSlideList(Context applicationContext) {
		// TODO Auto-generated constructor stub
		mcontext=applicationContext;
		
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return itemname.length;
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		 View vi=convertView;
		 
	        if(convertView==null){
	        	
	            LayoutInflater inflater = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            vi = inflater.inflate(R.layout.list_row, null);
	 
	        TextView title = (TextView)vi.findViewById(R.id.title); 
	        ImageView thumb_image=(ImageView)vi.findViewById(R.id.list_image);
	        title.setText(itemname[position]);
	        thumb_image.setImageResource(itemimg[position]);
	        
	        }else{
	        	
	        	  LayoutInflater inflater = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		            vi = inflater.inflate(R.layout.list_row, null);
		 
		        TextView title = (TextView)vi.findViewById(R.id.title); 
		        ImageView thumb_image=(ImageView)vi.findViewById(R.id.list_image);
		        
		        title.setText(itemname[position]);
		        thumb_image.setImageResource(itemimg[position]);
	        	
	        }
		return vi;
	}
	
	
	

}
