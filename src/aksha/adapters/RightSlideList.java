package aksha.adapters;

import java.util.ArrayList;
import java.util.HashMap;

import com.aksha.mosaic.R;
import android.content.Context;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RightSlideList extends BaseAdapter{


	String [] itemname={"Sync","Change Passcode","Change Password","Sleep","LogOut"};
	Integer [] itemimg ={R.drawable.sync,R.drawable.change_pass_code,R.drawable.change_password,R.drawable.sleep,R.drawable.log_out};	
	Context mcontext;
	Boolean notify = false;
	
	public RightSlideList(Context applicationContext,boolean notify) {
		// TODO Auto-generated constructor stub
		mcontext=applicationContext;
		this.notify = notify;
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
