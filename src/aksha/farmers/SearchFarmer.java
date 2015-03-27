package aksha.farmers;

import java.util.ArrayList;
import java.util.HashMap;

import aksha.adapters.ActivityListAdapterFarmer;
import aksha.adapters.LazyAdapter;
import aksha.adapters.Myspinner;
import aksha.adapters.RefreshableListView;
import aksha.adapters.RefreshableListView.OnRefreshListener;
import aksha.connectiondetector.ConnectionDetector;
import aksha.database.Databaseutill;
import aksha.database.GetData;
import aksha.farmers.MyFarmer.Data;
import com.aksha.mosaic.R;
import aksha.webservice.Webservicerequest;
import android.app.Activity;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SearchFarmer extends Fragment {
	View root;
	String txt;
	RefreshableListView retlist;
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {		
		 root = inflater.inflate(R.layout.pog_search, container,false);
		 retlist = (RefreshableListView)root.findViewById(R.id.sretailerlist);
			retlist.setOnRefreshListener(new OnRefreshListener() {
				
				@Override
				public void onRefresh(RefreshableListView listView) {
					try{
					if(txt.length()>3)
					{												
						new  Data().execute(txt);						
					}
					else{
						Toast.makeText(getActivity(), "Please select District/Mandi", Toast.LENGTH_SHORT).show();
					}
					}catch(Exception er){er.getMessage();retlist.completeRefreshing();}
					
				}
			});
		SearchManager searchManager = (SearchManager)getActivity().getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView)root.findViewById(R.id.searchViewpog);
		SearchableInfo searchableInfo = searchManager.getSearchableInfo(getActivity().getComponentName());
		searchView.setSearchableInfo(searchableInfo);
		searchView.setOnQueryTextListener(queryTextListener);
		return root;
	}
	
	SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() 
	{
	    @Override
	    public boolean onQueryTextChange(String newText) 
	    	{
	        
	    		if(newText.length()!=0 && newText.length()>=3 )
	    		{
	    			txt=newText;
	    			new Data().execute(newText);
	        	}
	        	return true;
	    	}
	    @Override
	    public boolean onQueryTextSubmit(String query) 
	    	{
	      
	    		if(query.length()!=0){
	    			new Data().execute(query);
	    	 	InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
			    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);}
	        	return true;
	    	}
		};
		
		
		
		class Data extends AsyncTask<String, Void, ArrayList<String>>
		{
			ProgressBar retailerpbar;
			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				try{
					retailerpbar = (ProgressBar)root.findViewById(R.id.pgretailer);
					retailerpbar.setVisibility(View.VISIBLE);
					retailerpbar.bringToFront();
				}catch(Exception e)
				{
					e.getMessage();
				}
			}
			
			
			@Override
			protected ArrayList<String> doInBackground(String... params) {
				// TODO Auto-generated method stub
				ConnectionDetector cd=new ConnectionDetector(getActivity());
				Databaseutill db=Databaseutill.getDBAdapterInstance(getActivity());
				 ArrayList<String> returnval=new ArrayList<String>();
				 if(cd.isConnectingToInternet())
				 {try
			      {
				
					 ArrayList<HashMap<String, String>> logindata = new GetData(getActivity(), db).getAllLogin();
		    		  String empid = logindata.get(0).get("empid");
		    		  String districtid = logindata.get(0).get("geoid");
					 //"farmerid,name,mobile"
					      Webservicerequest wsc = new Webservicerequest();
					      ArrayList<String> inputlist=new ArrayList<String>();
			
					      inputlist.add("faid");
					      inputlist.add(empid);	
					      inputlist.add("txt");
					      inputlist.add(params[0]);
					      String str2 = wsc.MobileWebservice(getActivity(), "getfafarmersearch", inputlist);
					      ArrayList<String> listvalue=new ArrayList<String>();
					      if (str2 != null)
					      {	
					    	  listvalue.add("farmername");
					    	  listvalue.add("villname");
					    	  listvalue.add("mobile");
					        ArrayList<String> localArrayList3 = wsc.JSONEncoding(str2, listvalue);
					        returnval = localArrayList3;
					      }
			        
			      }
					 
				 
				 catch (Exception localException)
			      {
			        localException.getMessage();
			        returnval.add("Error in Connection");
			      }
				 }
				 else
				 {
					 returnval.add("No Network Found");
				 }
				 
				return returnval;
			}
			
			@Override
			protected void onPostExecute(ArrayList<String> str) {
				// TODO Auto-generated method stub
				
				try{
					
					if(str!=null && str.size()>0){
						final ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String,String>>();
					    for(int i=0; i<str.size(); i+=3){
					    	Webservicerequest web=new Webservicerequest();
					    	HashMap<String, String> map = new HashMap<String, String>();
					    	map.put("1", web.Decrypt(str.get(i)));
					    	map.put("2", web.Decrypt(str.get(i+1)));
					    	map.put("3", web.Decrypt(str.get(i+2)));
					    	map.put("4", "tag");
					    	data.add(map);
					    }
					    
					ActivityListAdapterFarmer adapter = new ActivityListAdapterFarmer(getActivity(), data, retlist);
					retlist.setAdapter(adapter);
					
					}
					else{
						ArrayList<String> res = new ArrayList<String>();
						res.add("No Data Found");
			    		 LazyAdapter adapter = new LazyAdapter(getActivity(), res);
			    		 retlist.setAdapter(adapter);
					}
				}
				catch(Exception e)
				{
					e.getMessage();
				}
				retailerpbar.setVisibility(View.GONE);
				retlist.completeRefreshing();
				
			}
	
			
			
			public void createTable1(String color,String name,String mobile) {
				// TODO Auto-generated method stub
				LinearLayout ll = (LinearLayout)getActivity().findViewById(R.id.parent);
				
				LinearLayout ll1 = new LinearLayout(getActivity());
				LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f);
						ll1.setLayoutParams(layoutParams1);
						ll1.setOrientation(LinearLayout.HORIZONTAL);
						ll1.setBackgroundColor(Color.parseColor(color));
						
						TextView tv1 = new TextView(getActivity());
						LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(
								LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT,1f);
						tv1.setId(1);
						tv1.setText(name);
						//tv1.setTextColor(Color.parseColor("#ffffff"));
						//tv1.setTag(redemo.getValue());
						tv1.setTextSize(16);
						tv1.setPadding(5, 5, 5, 5);
						tv1.setLayoutParams(layoutParam);
					//	tv2.setTypeface(Typeface.DEFAULT_BOLD);
						
						ll1.addView(tv1);
						
						View view = new View(getActivity());
						LinearLayout.LayoutParams layoutParam3 = new LinearLayout.LayoutParams(
								2, LinearLayout.LayoutParams.MATCH_PARENT,0f);
						view.setBackgroundColor(Color.WHITE);
						view.setLayoutParams(layoutParam3);
						ll1.addView(view);
						
						TextView tv2 = new TextView(getActivity());
						LinearLayout.LayoutParams layoutParam2 = new LinearLayout.LayoutParams(
								LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT,1f);
						tv2.setId(1);
						tv2.setText(mobile);
						//tv1.setTextColor(Color.parseColor("#ffffff"));
						//tv1.setTag(redemo.getValue());
						tv2.setTextSize(16);
						tv2.setPadding(5, 5, 5, 5);
						tv2.setLayoutParams(layoutParam2);
					//	tv2.setTypeface(Typeface.DEFAULT_BOLD);
						
						ll1.addView(tv2);
						
						ll.addView(ll1);
					
				
			}
			
		}
	
}
