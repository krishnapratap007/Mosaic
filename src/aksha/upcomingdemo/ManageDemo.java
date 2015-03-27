package aksha.upcomingdemo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import aksha.adapters.ActivityListAdapterFarmer;
import aksha.adapters.ActivityListAdapterFarmerHorz;
import aksha.adapters.LazyAdapter;
import aksha.adapters.Myspinner;
import aksha.connectiondetector.ConnectionDetector;
import aksha.database.Databaseutill;
import aksha.database.GetData;
import aksha.upcomingdemo.HorizontalListView;
import aksha.webservice.Webservicerequest;
import com.aksha.mosaic.R;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableLayout;

public class ManageDemo extends Fragment {
	
	Databaseutill db;
	GetData get;
	View root;
	String distid,empid,mandiid,villid;
	TableLayout tableview;
	Button submit;
	

	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
			
	   root=inflater.inflate(R.layout.managedemofa,container, false);
	   tableview = (TableLayout)root.findViewById(R.id.tableview);
	   submit = (Button)root.findViewById(R.id.finish);
	  // tableview.setVisibility(View.INVISIBLE);
	  // submit.setVisibility(View.GONE);
	   
	   
		db = Databaseutill.getDBAdapterInstance(getActivity());
		get = new GetData(getActivity(),db);
		
		ArrayList<HashMap<String, String>> logindata = get.getAllLogin();
		 empid = logindata.get(0).get("empid");
		/* distid = logindata.get(0).get("geoid");
		ArrayList<HashMap<String, String>> mandi = get.getMandi(distid);
		 mandiid = mandi.get(0).get("mandiid");*/
		
		
		new Data().execute(empid);
	     
	     return root;
	        
	    }
	    
	class Data extends AsyncTask<String, Void, ArrayList<String>>{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}
		
		@Override
		protected ArrayList<String> doInBackground(String... params) {
			
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
				      inputlist.add("faid");
				      inputlist.add(params[0]);	
				      String str2 = wsc.MobileWebservice(getActivity(), "getfarmerdemoFA", inputlist);
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
				
			
			 HorizontalListView listview = (HorizontalListView)root.findViewById(R.id.horzlistview);
				
			 ActivityListAdapterFarmerHorz adapter=new ActivityListAdapterFarmerHorz(getActivity(), data, listview); 
	         listview.setAdapter(adapter);
		}
	           
		
	}
		catch(Exception e)
		{
			e.getMessage();
		}
		
		
		
		
	}
	    
}	 
		
		
		
		
	
}
