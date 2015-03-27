package aksha.addretailer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import aksha.adapters.ActivityListAdapterRet;
import aksha.adapters.ActivityListAdapterStockTH;
import aksha.adapters.LazyAdapter;
import aksha.adapters.Myspinner;
import aksha.adapters.RefreshableListView;
import aksha.adapters.RefreshableListView.OnRefreshListener;
import aksha.adapters.TableRowListAdapter;
import aksha.connectiondetector.ConnectionDetector;
import aksha.database.Databaseutill;
import aksha.database.GetData;

import com.aksha.mosaic.R;

import aksha.webservice.Webservicerequest;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;

public class MyRetailers_th extends Fragment {
	
	private GetData get;
	private Databaseutill db;
	private View root;	
	private RefreshableListView retailerlist;
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		root = inflater.inflate(R.layout.retailerlistmo, container, false);
		
		db = Databaseutill.getDBAdapterInstance(getActivity());
		get = new GetData(getActivity(), db);
		
		final Spinner retailersdistrictlist = (Spinner)root.findViewById(R.id.retailersdistrictlist);
		
		retailerlist = (RefreshableListView)root.findViewById(R.id.retailerlist);
		
		retailerlist.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh(RefreshableListView listView) {
				
				 Myspinner sp=(Myspinner)retailersdistrictlist.getSelectedItem();
				 if(!sp.getValue().equalsIgnoreCase("0")){
				  new RetailerList().execute(sp.getValue());
				  }
				
			}
		});
		ArrayList<HashMap<String, String>> category1=new ArrayList<HashMap<String,String>>();
		HashMap<String, String> map1=new HashMap<String, String>();
			map1.put("1","0");
			map1.put("2","Select District");
			category1.add(map1);
			
			try{
				ArrayList<HashMap<String, String>> dist = new GetData(getActivity(), db).getDistrict();
				// add elements to al, including duplicates
				HashSet hs = new HashSet();
				hs.addAll(dist);
				dist.clear();
				dist.addAll(hs);
				
				for (int i=0; i<dist.size(); i++) {
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("1",dist.get(i).get("Districtid"));
	 				map.put("2",dist.get(i).get("Districtname"));
	 				category1.add(map);		 				
				}			
			}
			catch(Exception e){
				e.getMessage();
			}										
			
		Myspinner[] redemo1 = new Myspinner[category1.size()];
		for(int i=0; i<category1.size(); i++){
			redemo1[i] = new Myspinner(category1.get(i).get("2"), category1.get(i).get("1"), "");
		}
		ArrayAdapter<Myspinner> adapter1 = new ArrayAdapter<Myspinner>(getActivity(), android.R.layout.simple_spinner_dropdown_item, redemo1);
		retailersdistrictlist.setAdapter(adapter1);
		
		final ArrayList<HashMap<String, String>> category2=new ArrayList<HashMap<String,String>>();
		HashMap<String, String> map2=new HashMap<String, String>();
			map2.put("1","0");
			map2.put("2","Select Mandi");
			category2.add(map2);
			
			retailersdistrictlist.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					
					
					//show relater on district basic
					 Myspinner sp=(Myspinner)retailersdistrictlist.getSelectedItem();
					 if(!sp.getValue().equalsIgnoreCase("0")){
					  new RetailerList().execute(sp.getValue());
					  }

				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					// TODO Auto-generated method stub
					
				}
			});
			
			
				
		final ArrayList<String> str = new ArrayList<String>();
	    /*str.add("Firm 1");
	    str.add("Name 1");
	    str.add("Mobile 1");
	    str.add("Firm 2");
	    str.add("Name 2");
	    str.add("Mobile 2");
	    str.add("Firm 3");
	    str.add("Name 3");
	    str.add("Mobile 3");*/
	    
	    final ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String,String>>();
	    for(int i=0; i<str.size(); i+=3){
	    	HashMap<String, String> map = new HashMap<String, String>();
	    	map.put("1", str.get(i));
	    	map.put("2", str.get(i+1));
	    	map.put("3", str.get(i+2));
	    	data.add(map);
	    }
	    
		TableRowListAdapter adapter = new TableRowListAdapter(getActivity(), data);
		retailerlist.setAdapter(adapter);
		
		/*retailerlist.setOnScrollListener(new OnScrollListener() {
			
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
			        	((ProgressBar)root.findViewById(R.id.retailerpbar)).setVisibility(View.VISIBLE);
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
			    		retailerlist.setAdapter(adapter);
			    		retailerlist.setSelection(firstVisibleItem);
			    		((ProgressBar)root.findViewById(R.id.retailerpbar)).setVisibility(View.GONE);
			        }
			        else{
			        	((ProgressBar)root.findViewById(R.id.retailerpbar)).setVisibility(View.GONE);
			        }
			        
			    }
		});
		*/
		return root;
	}
	
	class RetailerList extends AsyncTask<String, Void, ArrayList<HashMap<String, String>>>{

		ProgressBar retailerpbar;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			retailerpbar = (ProgressBar)root.findViewById(R.id.retailerpbar);
			retailerpbar.setVisibility(View.VISIBLE);
		}

		@Override
		protected void onPostExecute(ArrayList<HashMap<String, String>> str) {
			// TODO Auto-generated method stub
			
			retailerpbar.setVisibility(View.GONE);
			try{
				if(str!=null && str.size()>0){
					final ArrayList<HashMap<String, String>> data = str;
				    				    														
					
					ActivityListAdapterRet adapter = new ActivityListAdapterRet(getActivity(), data, retailerlist);
					retailerlist.setAdapter(adapter);
				}
				else{
					ArrayList<String> res = new ArrayList<String>();
		    		 res.add("No Data Found");
		    		 LazyAdapter adapter = new LazyAdapter(getActivity(), res);
		    		 retailerlist.setAdapter(adapter);
				}
			}
			catch(Exception e){
				e.getMessage();
				ArrayList<String> res = new ArrayList<String>();
	    		 res.add("No Data Found");
	    		 LazyAdapter adapter = new LazyAdapter(getActivity(), res);
	    		 retailerlist.setAdapter(adapter);
			}
			retailerlist.completeRefreshing();
		}

		@Override
		protected ArrayList<HashMap<String, String>> doInBackground(String... params) {
			// TODO Auto-generated method stub
			ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
			try{
				
				 Webservicerequest wsc = new Webservicerequest();
			      ArrayList<String> inputlist=new ArrayList<String>();
			      inputlist.add("districtid");
			      inputlist.add(params[0]);			      				      				     
			      String str2 = wsc.MobileWebservice(getActivity(), "getretalondistrict", inputlist);
			      ArrayList<String> listvalue=new ArrayList<String>();
			      if (str2 != null)
			      {	
			    	listvalue.add("firm_name");
			    	listvalue.add("mobile");
			    	listvalue.add("mandiname");
			        ArrayList<String> localArrayList3 = wsc.JSONEncoding(str2, listvalue);
			        listvalue.clear();
			        listvalue = localArrayList3;
			      }
			      
			      for(int icount=0;icount<listvalue.size();icount+=3){
			    	  HashMap<String, String> map=new HashMap<String, String>();
			    	  map.put("1",wsc.Decrypt(listvalue.get(icount)));
			    	  map.put("2",wsc.Decrypt( listvalue.get(icount+1)));
			    	  map.put("3", wsc.Decrypt(listvalue.get(icount+2)));
			    	  result.add(map);
			      }
			      
				   				     
			}
			catch(Exception e){
				e.getMessage();
			}
			return result;
		}
		
	}

}
