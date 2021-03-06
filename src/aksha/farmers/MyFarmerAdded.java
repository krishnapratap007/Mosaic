package aksha.farmers;

import java.util.ArrayList;
import java.util.HashMap;

import com.aksha.mosaic.R;

import aksha.adapters.ActivityListAdapterFarmer;
import aksha.adapters.LazyAdapter;
import aksha.adapters.Myspinner;
import aksha.adapters.RefreshableListView;
import aksha.adapters.RefreshableListView.OnRefreshListener;
import aksha.connectiondetector.ConnectionDetector;
import aksha.database.Databaseutill;
import aksha.database.GetData;
import aksha.webservice.Webservicerequest;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class MyFarmerAdded extends Fragment {
	

	View root;
	String encryptedgeoid,empid;
	RefreshableListView retlist;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		root = inflater.inflate(R.layout.retailerlist, container, false);
		
		retlist = (RefreshableListView)root.findViewById(R.id.retailerlist);
		retlist.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh(RefreshableListView listView) {
				 Spinner districts = (Spinner)root.findViewById(R.id.retailersdistrictlist);
				 Spinner mandis = (Spinner)root.findViewById(R.id.retailersmandilist);
				Myspinner sp=(Myspinner)mandis.getSelectedItem();
				Myspinner spdis = (Myspinner)districts.getSelectedItem();
				if((!sp.getValue().equalsIgnoreCase("0"))&&(!spdis.getValue().equalsIgnoreCase("0")))
				{												
					new  Data().execute(sp.getValue(),spdis.getValue());						
				}
				else{
					Toast.makeText(getActivity(), "Please select District/Mandi", Toast.LENGTH_SHORT).show();
				}
				
			}
		});
		Databaseutill db = Databaseutill.getDBAdapterInstance(getActivity());
		final GetData get = new GetData(getActivity(), db);
		
		final Spinner districts = (Spinner)root.findViewById(R.id.retailersdistrictlist);
		final Spinner mandis = (Spinner)root.findViewById(R.id.retailersmandilist);
		
		final Spinner village = (Spinner)root.findViewById(R.id.retailersvillagelist);
		village.setVisibility(View.GONE);
		
		ArrayList<HashMap<String, String>> category1=new ArrayList<HashMap<String,String>>();
		HashMap<String, String> map1=new HashMap<String, String>();
			map1.put("1","0");
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
		districts.setAdapter(adapter1);
		
		final ArrayList<HashMap<String, String>> category2=new ArrayList<HashMap<String,String>>();
		HashMap<String, String> map2=new HashMap<String, String>();
			map2.put("1","0");
			map2.put("2","Select Mandi");
			category2.add(map2);
			
			districts.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					
try{
					
					if(position!=0)
					{
						Myspinner sp = (Myspinner)districts.getSelectedItem();
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
		 				mandis.setAdapter(adapter2);		 				
					}
					else{
						ArrayList<HashMap<String,String>> category2=new ArrayList<HashMap<String,String>>();
						HashMap<String, String> map2=new HashMap<String, String>();
		 				map2.put("1","0");
		 				map2.put("2","Select Mandi");
		 				category2.add(map2);
						Myspinner[] redemo2 = new Myspinner[category2.size()];
		 				for(int i=0; i<category2.size(); i++){
		 					redemo2[i] = new Myspinner(category2.get(i).get("2"), category2.get(i).get("1"), "");
		 				}
		 				ArrayAdapter<Myspinner> adapter2 = new ArrayAdapter<Myspinner>(getActivity(), android.R.layout.simple_spinner_dropdown_item, redemo2);
		 				mandis.setAdapter(adapter2);
					}
					
                   }catch(Exception er){er.getMessage();}
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					// TODO Auto-generated method stub
					
				}
			});
			
			
			
			
			
			/*mandis.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					
					try{
					//getvillage on mandi ids
					Myspinner sp=(Myspinner)mandis.getSelectedItem();
					if((!sp.getValue().equalsIgnoreCase("0"))){
						
						ArrayAdapter<Myspinner> adapter2 = new ArrayAdapter<Myspinner>(getActivity(), android.R.layout.simple_spinner_dropdown_item, get.getVillages(sp.getValue()));
						adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						village.setAdapter(adapter2);		 				
						
					}
					}catch(Exception er){er.getMessage();}
					
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					// TODO Auto-generated method stub
					
				}
				});
			
			
			ArrayList<Myspinner> sv=new ArrayList<Myspinner>();
			sv.add(new Myspinner("Select Village", "0", ""));
			
			ArrayAdapter<Myspinner> adapter2 = new ArrayAdapter<Myspinner>(getActivity(), android.R.layout.simple_spinner_dropdown_item, sv);
			adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			village.setAdapter(adapter2);*/
			
			mandis.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {

					try{
					Myspinner sp=(Myspinner)mandis.getSelectedItem();
					Myspinner spdis = (Myspinner)districts.getSelectedItem();
					Myspinner spvill=(Myspinner)village.getSelectedItem();
					
					empid = get.getAllLogin().get(0).get("empid");
					
					if((!sp.getValue().equalsIgnoreCase("0")) && (!spdis.getValue().equalsIgnoreCase("0")) && empid != null)
					{	
						new  Data().execute(sp.getValue(),spdis.getValue(),empid);						
					}
					else{
						Toast.makeText(getActivity(), "Please select District/Mandi", Toast.LENGTH_SHORT).show();
					}
					}catch(Exception er){er.getMessage();}
					
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					// TODO Auto-generated method stub
					
				}});

			
			return root;
	}
	

	class Data extends AsyncTask<String, Void, ArrayList<String>>
	{
		ProgressBar retailerpbar;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			try{
				retailerpbar = (ProgressBar)root.findViewById(R.id.retailerpbar);
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
			//	 SELECT * FROM farmer_test order by ID ASC LIMIT 40,20
				 //"farmerid,name,mobile"
				      Webservicerequest wsc = new Webservicerequest();
				      ArrayList<String> inputlist=new ArrayList<String>();
				      inputlist.add("districtid");
				      inputlist.add(params[1]);
				      inputlist.add("mandiid");
				      inputlist.add(params[0]);
				      inputlist.add("empid");
				      inputlist.add(params[2]);
				      String str2 = wsc.MobileWebservice(getActivity(), "getfarmerallFA", inputlist);
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
	}	


}
