package aksha.plannedactivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import aksha.activityplanningth.CalendarView_th;
import aksha.adapters.ActivityListAdapter;
import aksha.adapters.LazyAdapter;
import aksha.adapters.Myspinner;
import aksha.adapters.PlannedActivitiesGridViewAdapterTH;
import aksha.adapters.PlannedActivitiesListAdapter;
import aksha.adapters.RefreshableListView;
import aksha.connectiondetector.ConnectionDetector;
import aksha.database.Databaseutill;
import aksha.database.GetData;
import com.aksha.mosaic.R;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

public class MOPlannedActivities extends Activity{
	
	
	private RefreshableListView mListView;
	private Databaseutill db;
	private GetData get;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.plannedactivities_refreshablelist);
			
		try{
			actionBar.setTitle(getIntent().getExtras().getString("mdoname"));
		mListView = ((RefreshableListView)findViewById(R.id.plannedactivities_refreshable_list));
		
		db = Databaseutill.getDBAdapterInstance(getApplicationContext());
		get = new GetData(getApplicationContext(), db);	
	    
	    mListView.setOnRefreshListener(new RefreshableListView.OnRefreshListener()
 	    {
 	      public void onRefresh(RefreshableListView paramAnonymousRefreshableListView)
 	      {
 	    	  
 	    	 if(new ConnectionDetector(getApplicationContext()).isConnectingToInternet())
	    	  {
	    		  
	    		  String empid = getIntent().getExtras().getString("mdoid");
	    		  String districtid = getIntent().getExtras().getString("currentdate");
	    		  new GetActivities().execute(empid,districtid);
	    	  }
	    	 else
	    	  {
	    		 ArrayList<String> str = new ArrayList<String>();
	    		 str.add("No Network Found, Please Pull Down Again to Refresh");
	    		 LazyAdapter adapter = new LazyAdapter(getApplicationContext(), str);
	    		 mListView.setAdapter(adapter);
	    		 mListView.completeRefreshing();
	    	  } 	    	   	    
 	      }
 	    });
	    
	    //ArrayList<String> str = new ArrayList<String>();
	    //str.add("Please Pull Down to Refresh");
		//LazyAdapter adapter = new LazyAdapter(getApplicationContext(), str);
		//mListView.setAdapter(adapter);	    
 		  String empid = getIntent().getExtras().getString("mdoid");
		  String districtid = getIntent().getExtras().getString("currentdate");
		  new GetActivities().execute(empid,districtid);
		
		}catch(Exception er){
			er.getMessage();
			
		}
		
	}
	

	
	class GetActivities extends AsyncTask<String, Void, ArrayList<HashMap<String, String>>>{
		ProgressDialog pDialog;
		@Override
		protected void onPreExecute() {
			pDialog = new ProgressDialog(MOPlannedActivities.this);
			try{
				this.pDialog.setMessage("Please wait ...");
		        this.pDialog.setIndeterminateDrawable(getApplicationContext().getResources().getDrawable(R.drawable.red_progress));
		        this.pDialog.setIndeterminate(false);
		        this.pDialog.setCancelable(false);
		        this.pDialog.show();
			}
			catch(Exception e)
			{
				e.getMessage();
			}
		
		}
		@Override
		protected ArrayList<HashMap<String, String>> doInBackground(
				String... params) {
			// TODO Auto-generated method stub
			ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String,String>>();
			try{
			  data = get.getPlannedAtivitiesTH(params[0], params[1]);
			}
			catch(Exception e){
				e.getMessage();
				data.clear();
			}			
			return data;
		}
		
		@Override
		protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			try{
				if(result!=null && result.size()>0){
					final ArrayList<ArrayList<HashMap<String, String>>> arraylist = new ArrayList<ArrayList<HashMap<String,String>>>();
					ArrayList<String> date = new ArrayList<String>();
					for (HashMap<String, String> arrayList2 : result) {
					if(!date.contains(arrayList2.get("plan_date")))
					date.add(arrayList2.get("plan_date"));
					}	
					for(String dte : date){
					ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String,String>>();
					for(int i=0; i<result.size(); i++){
//						SimpleDateFormat stringdate = new SimpleDateFormat("M/dd/yyyy HH:mm:ss aa");
//						String stringdate = new SimpleDateFormat("dd MMM yyyy EEEE").format(new Date());
					if(dte.equalsIgnoreCase(result.get(i).get("plan_date"))){
					data.add(result.get(i));
					}	
					}
					arraylist.add(data);
					}
					
					
					PlannedActivitiesGridViewAdapterTH adapter = new PlannedActivitiesGridViewAdapterTH(MOPlannedActivities.this, arraylist, mListView, date,getIntent().getExtras().getString("mdoid"));
					mListView.setAdapter(adapter);
					
				}
				else
		    	  {
		    		 ArrayList<String> str = new ArrayList<String>();
		    		 str.add("No Data Found, Please Pull Down Again to Refresh");
		    		 LazyAdapter adapter = new LazyAdapter(getApplicationContext(), str);
		    		 mListView.setAdapter(adapter);
		    	  }
			}
			catch(Exception e){
				e.getMessage();
			}
			mListView.completeRefreshing();
			pDialog.dismiss();
		}
		
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case android.R.id.home:
	            // app icon in action bar clicked; goto parent activity.
	        	
	            this.finish();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	
}