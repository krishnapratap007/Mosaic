package aksha.adapters;

import java.lang.reflect.Array;
import java.util.ArrayList;

import com.aksha.mosaic.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LazyAdapter extends BaseAdapter {

    Context context;
    ArrayList<String> str;

   public LazyAdapter(Context context,ArrayList<String> str) {
		// TODO Auto-generated constructor stub
    	this.context = context;
    	this.str = str;
	}

   
   public class ViewHolder{
	   LinearLayout card;
       TextView text;
   }
   

    public View getView(int position, View convertView, ViewGroup parent){
    	 ViewHolder holder = new ViewHolder();
    	 LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	 convertView = inflater.inflate(R.layout.cardlayout, null);
    	 holder.card = (LinearLayout)convertView.findViewById(R.id.llcard);
         holder.text = (TextView)convertView.findViewById(R.id.title);
         holder.text.setText(str.get(position));
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
