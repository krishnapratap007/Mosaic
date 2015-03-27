package aksha.addmandi;

import java.util.ArrayList;
import java.util.HashMap;

import aksha.adapters.LazyAdapter;
import aksha.adapters.Myspinner;
import aksha.adapters.TableRowListAdapter;
import com.aksha.mosaic.R;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

public class MandiList extends Fragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		final View root = inflater.inflate(R.layout.mandilist, container, false);
		
		Spinner mandidistrictlist = (Spinner)root.findViewById(R.id.mandidistrictlist);
		final ListView mandilist = (ListView)root.findViewById(R.id.mandilist);
		
		ArrayList<HashMap<String, String>> category1=new ArrayList<HashMap<String,String>>();
		HashMap<String, String> map1=new HashMap<String, String>();
			map1.put("1","");
			map1.put("2","Select District");
			category1.add(map1);
			
		Myspinner[] redemo1 = new Myspinner[category1.size()];
		for(int i=0; i<category1.size(); i++){
			redemo1[i] = new Myspinner(category1.get(i).get("2"), category1.get(i).get("1"), "");
		}
		ArrayAdapter<Myspinner> adapter1 = new ArrayAdapter<Myspinner>(getActivity(), android.R.layout.simple_spinner_item, redemo1);
		mandidistrictlist.setAdapter(adapter1);
		
		final ArrayList<String> str = new ArrayList<String>();
	    str.add("Firm 1");
	    str.add("Name 1");
	    str.add("Mobile 1");
	    str.add("Firm 2");
	    str.add("Name 2");
	    str.add("Mobile 2");
	    str.add("Firm 3");
	    str.add("Name 3");
	    str.add("Mobile 3");
	    
	    final ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String,String>>();
	    for(int i=0; i<str.size(); i+=3){
	    	HashMap<String, String> map = new HashMap<String, String>();
	    	map.put("1", str.get(i));
	    	map.put("2", str.get(i+1));
	    	map.put("3", str.get(i+2));
	    	data.add(map);
	    }
	    
		TableRowListAdapter adapter = new TableRowListAdapter(getActivity(), data);
		mandilist.setAdapter(adapter);
		
	/*	mandilist.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				int position = firstVisibleItem+visibleItemCount;
				if(position==totalItemCount){
			        	((ProgressBar)root.findViewById(R.id.pbar)).setVisibility(View.VISIBLE);
			        	str.add("Firm "+String.valueOf(data.size()+2));
			    	    str.add("Name "+String.valueOf(data.size()+2));
			    	    str.add("Mobile "+String.valueOf(data.size()+2));
			    	    
			    	    for(int i=0; i<str.size(); i+=3){
			    	    	HashMap<String, String> map = new HashMap<String, String>();
			    	    	map.put("1", str.get(i));
			    	    	map.put("2", str.get(i+1));
			    	    	map.put("3", str.get(i+2));
			    	    	data.add(map);
			    	    }
			    	    
			    		TableRowListAdapter adapter = new TableRowListAdapter(getActivity(), data);
			    		mandilist.setAdapter(adapter);			    		
			    		((ProgressBar)root.findViewById(R.id.pbar)).setVisibility(View.GONE);
			        }
			        else{
			        	((ProgressBar)root.findViewById(R.id.pbar)).setVisibility(View.GONE);
			        }
			        
			    }
		});
		*/
		return root;
	}

}
