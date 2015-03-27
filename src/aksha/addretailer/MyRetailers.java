package aksha.addretailer;

import java.util.ArrayList;
import java.util.HashMap;

import aksha.adapters.ActivityListAdapterRet;
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

public class MyRetailers extends Fragment {
	
	private GetData get;
	private Databaseutill db;
	private String encryptedgeoid;
	private View root;
	private RefreshableListView retailerlist;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		root = inflater.inflate(R.layout.retailerlist, container, false);
		
		db = Databaseutill.getDBAdapterInstance(getActivity());
		get = new GetData(getActivity(), db);
		
		final Spinner retailersdistrictlist = (Spinner)root.findViewById(R.id.retailersdistrictlist);
		final Spinner retailersmandilist = (Spinner)root.findViewById(R.id.retailersmandilist);
		 Spinner village = (Spinner)root.findViewById(R.id.retailersvillagelist);
		 village.setVisibility(View.GONE);
		retailerlist = (RefreshableListView)root.findViewById(R.id.retailerlist);
		retailerlist.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh(RefreshableListView listView) {
			
				try{
				if(new ConnectionDetector(getActivity()).isConnectingToInternet()){
					Myspinner sp = (Myspinner)retailersdistrictlist.getSelectedItem();
					Myspinner sp1 = (Myspinner)retailersmandilist.getSelectedItem();
					new RetailerList().execute(sp.getValue(),sp1.getValue());
				}
				else{
					Toast.makeText(getActivity(), "No Network Found", Toast.LENGTH_SHORT).show();
				}
				}catch(Exception er){er.getMessage();}
			}
		});
		
		ArrayList<HashMap<String, String>> category1=new ArrayList<HashMap<String,String>>();
		HashMap<String, String> map1=new HashMap<String, String>();
			map1.put("1","");
			map1.put("2","Select District");
			category1.add(map1);
			
			try{
				ArrayList<HashMap<String, String>> dist = new GetData(getActivity(), db).getDistrict();								
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
			map2.put("1","");
			map2.put("2","Select Mandi");
			category2.add(map2);
			
			retailersdistrictlist.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					if(position!=0)
					{
						Myspinner sp = (Myspinner)retailersdistrictlist.getSelectedItem();
						try{
							encryptedgeoid = new Webservicerequest().Encrypt(sp.getValue());
						}
						catch(Exception e){
							e.getMessage();
						}
						category2.addAll(get.getMandi(encryptedgeoid));	
						Myspinner[] redemo2 = new Myspinner[category2.size()];
		 				for(int i=0; i<category2.size(); i++){
		 					redemo2[i] = new Myspinner(category2.get(i).get("2"), category2.get(i).get("1"), "");
		 				}
		 				ArrayAdapter<Myspinner> adapter2 = new ArrayAdapter<Myspinner>(getActivity(), android.R.layout.simple_spinner_dropdown_item, redemo2);
		 				retailersmandilist.setAdapter(adapter2);
					}
					else{
						ArrayList<HashMap<String,String>> category2=new ArrayList<HashMap<String,String>>();
						HashMap<String, String> map2=new HashMap<String, String>();
		 				map2.put("1","");
		 				map2.put("2","Select Mandi");
		 				category2.add(map2);
						Myspinner[] redemo2 = new Myspinner[category2.size()];
		 				for(int i=0; i<category2.size(); i++){
		 					redemo2[i] = new Myspinner(category2.get(i).get("2"), category2.get(i).get("1"), "");
		 				}
		 				ArrayAdapter<Myspinner> adapter2 = new ArrayAdapter<Myspinner>(getActivity(), android.R.layout.simple_spinner_dropdown_item, redemo2);
		 				retailersmandilist.setAdapter(adapter2);
					}
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					// TODO Auto-generated method stub
					
				}
			});
			
			
		Myspinner[] redemo2 = new Myspinner[category2.size()];
		for(int i=0; i<category2.size(); i++){
			redemo2[i] = new Myspinner(category2.get(i).get("2"), category2.get(i).get("1"), "");
		}
		ArrayAdapter<Myspinner> adapter2 = new ArrayAdapter<Myspinner>(getActivity(), android.R.layout.simple_spinner_dropdown_item, redemo2);
		retailersmandilist.setAdapter(adapter2);
		
		retailersmandilist.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if(position!=0){
					if(new ConnectionDetector(getActivity()).isConnectingToInternet()){
						Myspinner sp = (Myspinner)retailersdistrictlist.getSelectedItem();
						Myspinner sp1 = (Myspinner)retailersmandilist.getSelectedItem();
						new RetailerList().execute(sp.getValue(),sp1.getValue());
					}
					else{
						Toast.makeText(getActivity(), "No Network Found", Toast.LENGTH_SHORT).show();
					}
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
	
	class RetailerList extends AsyncTask<String, Void, ArrayList<String>>{

		ProgressBar retailerpbar;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			retailerpbar = (ProgressBar)root.findViewById(R.id.retailerpbar);
			retailerpbar.setVisibility(View.VISIBLE);
		}

		@Override
		protected void onPostExecute(ArrayList<String> str) {
			// TODO Auto-generated method stub
			super.onPostExecute(str);
			retailerpbar.setVisibility(View.GONE);
			try{
				if(str!=null && str.size()>0){
					final ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String,String>>();
				    for(int i=0; i<str.size(); i+=3){
				    	HashMap<String, String> map = new HashMap<String, String>();
				    	map.put("1", str.get(i));
				    	map.put("2", str.get(i+2));
				    	map.put("3", str.get(i+1));
				    	data.add(map);
				    }
				    
					
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
		protected ArrayList<String> doInBackground(String... params) {
			// TODO Auto-generated method stub
			ArrayList<String> result = new ArrayList<String>();
			try{
				String selectionargdup = "firm_name,mandiname,mobile";
				 Webservicerequest wsc = new Webservicerequest();	
				 ArrayList<String> inputlist = new ArrayList<String>();
					inputlist.add("districtid");
					inputlist.add(params[0]);
					inputlist.add("mandiid");
					inputlist.add(params[1]);					
				     String resultdata=wsc.MobileWebservice(getActivity(), "getretalondistrictmandi",inputlist);
				     ArrayList<String> listvalue=new ArrayList<String>();
				        if (resultdata != null)
				        {
				          String[] arrayOfString = selectionargdup.split(",");
				          for (int i = 0;i<arrayOfString.length; i++)
				          {
				            listvalue.add(arrayOfString[i]);
				          }
				          if (resultdata!=null) {
				            	inputlist=wsc.JSONEncoding(resultdata, listvalue);
				            	for (String string : inputlist) {
				            		result.add(wsc.Decrypt(string));
								}
				            }
				        }
				     
			}
			catch(Exception e){
				e.getMessage();
			}
			
			return result;
		}
		
	}

}
