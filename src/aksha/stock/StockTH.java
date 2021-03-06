package aksha.stock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import aksha.adapters.ActivityListAdapterStockTH;
import aksha.adapters.LazyAdapter;
import aksha.adapters.Myspinner;
import aksha.adapters.RefreshableListView;
import aksha.connectiondetector.ConnectionDetector;
import aksha.database.Databaseutill;
import aksha.database.GetData;

import com.aksha.mosaic.R;

import aksha.webservice.Webservicerequest;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

public class StockTH  extends Fragment {
	
	private GetData get;
	private Databaseutill db;
	private View root;
	private RefreshableListView mListView;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		try{
		root = inflater.inflate(R.layout.stockrefreshablelistth, container, false);
		
		db = Databaseutill.getDBAdapterInstance(getActivity());
		get = new GetData(getActivity(), db);
		
		final Spinner retailersdistrictlist = (Spinner)root.findViewById(R.id.districtlist);
		
		
		ArrayList<Myspinner> dt=new ArrayList<Myspinner>();
		dt.add(new Myspinner("Select District", "0", ""));
		ArrayAdapter<Myspinner> adapters = new ArrayAdapter<Myspinner>(getActivity(), android.R.layout.simple_spinner_dropdown_item, dt);
		retailersdistrictlist.setAdapter(adapters);
		
		
		
		ArrayList<HashMap<String, String>> category1=new ArrayList<HashMap<String,String>>();
		
			
			
				ArrayList<HashMap<String, String>> dist = new GetData(getActivity(), db).getDistrict();	
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
				
	ArrayList<Myspinner> redemo1 = new ArrayList<Myspinner>();
	if(category1.size()>0){
		redemo1.add(new Myspinner("Select District", "0", ""));
				for(int i=0; i<category1.size(); i++){
					redemo1.add(new Myspinner(category1.get(i).get("2"), category1.get(i).get("1"), ""));
				}
				ArrayAdapter<Myspinner> adapter1 = new ArrayAdapter<Myspinner>(getActivity(), android.R.layout.simple_spinner_dropdown_item, redemo1);
				retailersdistrictlist.setAdapter(adapter1);
	}
	
	

					
					
					retailersdistrictlist.setOnItemSelectedListener(new OnItemSelectedListener() {

						@Override
						public void onItemSelected(AdapterView<?> parent, View view,
								int position, long id) {

					
							Myspinner sp = (Myspinner)retailersdistrictlist.getSelectedItem();
							
							if(!sp.getValue().equalsIgnoreCase("0"))
							{
								new  GetStocks().execute(sp.getValue());
								
							}
							
						}

						@Override
						public void onNothingSelected(AdapterView<?> parent) {
							// TODO Auto-generated method stub
							
						}
						});
				
				
				
				
		mListView = ((RefreshableListView)root.findViewById(R.id.stockrefreshable_list));
		mListView.setOnRefreshListener(new RefreshableListView.OnRefreshListener()
 	    {
 	      public void onRefresh(RefreshableListView paramAnonymousRefreshableListView)
 	      {
 	    	  
 	    	 if(new ConnectionDetector(getActivity()).isConnectingToInternet())
	    	  {
	    		  
	    		  
 	    		Myspinner sp = (Myspinner)retailersdistrictlist.getSelectedItem();
 				
 				if(!sp.getValue().equalsIgnoreCase("0"))
 				{
 					new  GetStocks().execute(sp.getValue());
 					
 				}
	    	  }
	    	 else
	    	  {
	    		 ArrayList<String> str = new ArrayList<String>();
	    		 str.add("No Network Found, Please Pull Down Again to Refresh");
	    		 LazyAdapter adapter = new LazyAdapter(getActivity(), str);
	    		 mListView.setAdapter(adapter);
	    		 mListView.completeRefreshing();
	    	  } 	    	   	    
 	      }
 	    });
							
			}
			catch(Exception e){
				e.getMessage();
			}
		return root;
		}
	
	
public	class GetStocks extends AsyncTask<String, Void, ArrayList<HashMap<String, String>>>
	{
String disid;
		@Override
		protected ArrayList<HashMap<String, String>> doInBackground(String... params) {
			
			
			ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
			try{
				String selectionargdup = "firm_id,firm_name,mobile,qnty,updatedate";
				 Webservicerequest wsc = new Webservicerequest();	
				 ArrayList<String> inputlist = new ArrayList<String>();
					inputlist.add("districtid");
					inputlist.add(params[0]);
							disid=params[0];
				     String resultdata=wsc.MobileWebservice(getActivity(), "getstockTH",inputlist);
				    
				     ArrayList<String> listvalue=new ArrayList<String>();
				        if (resultdata != null)
				        {
				          String[] arrayOfString = selectionargdup.split(",");
				          for (int i = 0;i<arrayOfString.length; i++)
				          {
				            listvalue.add(arrayOfString[i]);
				          }
				          
				            	inputlist=wsc.JSONEncoding(resultdata, listvalue);
				            //	listvalue.clear();
				            /*	for (String string : inputlist) {
				            		listvalue.add(wsc.Decrypt(string));
								}*/
				            
				            	for(int icount=0;icount<inputlist.size();icount+=5)
				            	{
				            		HashMap<String, String> map=new HashMap<String, String>();
				            		map.put("1", wsc.Decrypt(inputlist.get(icount)));
				            		map.put("2", wsc.Decrypt(inputlist.get(icount+1)));
				            		map.put("3", wsc.Decrypt(inputlist.get(icount+2)));
				            		map.put("4", wsc.Decrypt(inputlist.get(icount+3)));
				            		map.put("5", wsc.Decrypt(inputlist.get(icount+4)));
				            		result.add(map);
				            		
				            	}
				        }
				        
				        
				        
				     
			}
			catch(Exception e){
				e.getMessage();
			}
			return result;
			
			
		}
		
		@Override
		protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
			
			try{
				if(result!=null && result.size()>0){
	 				ActivityListAdapterStockTH adapter = new ActivityListAdapterStockTH(getActivity(), result, mListView,disid);
	 				mListView.setAdapter(adapter);
				}
				else
		    	  {
		    		 ArrayList<String> str = new ArrayList<String>();
		    		 str.add("No Data Found, Please Pull Down Again to Refresh");
		    		 LazyAdapter adapter = new LazyAdapter(getActivity(), str);
		    		 mListView.setAdapter(adapter);
		    		 mListView.completeRefreshing();
		    	  }
			}
			catch(Exception e){
				e.getMessage();
			}
			mListView.completeRefreshing();
		
		}
	}

}
 


