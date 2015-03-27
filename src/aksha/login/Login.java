package aksha.login;

import static com.aksha.mosaic.CommonUtilities.SENDER_ID;
import static com.aksha.mosaic.CommonUtilities.SERVER_URL;

import java.util.ArrayList;

import aksha.connectiondetector.ConnectionDetector;
import aksha.database.Databaseutill;

import com.aksha.mosaic.AlertDialogManager;
import com.aksha.mosaic.R;

import aksha.passcode.Passcode;
import aksha.webservice.Webservicerequest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

public class Login extends Activity {		
	// Internet detector
			ConnectionDetector cd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		// alert dialog manager
		AlertDialogManager alert = new AlertDialogManager();
		
		
		
		cd = new ConnectionDetector(getApplicationContext());

		// Check if Internet present
		if (!cd.isConnectingToInternet()) {
			// Internet Connection is not present
			alert.showAlertDialog(Login.this,
					"Internet Connection Error",
					"Please connect to working Internet connection", false);
			// stop executing code by return
			return;
		}

		// Check if GCM configuration is set
		if (SERVER_URL == null || SENDER_ID == null || SERVER_URL.length() == 0
				|| SENDER_ID.length() == 0) {
			// GCM sernder id / server url is missing
			alert.showAlertDialog(Login.this, "Configuration Error!",
					"Please set your Server URL and GCM Sender ID", false);
			// stop executing code by return
			 return;
		}

		
		
		final EditText USERNAME = (EditText)findViewById(R.id.editText1);
		final EditText PASSWORD = (EditText)findViewById(R.id.editText2);
		final Button LOGIN = (Button)findViewById(R.id.login);
		
		
		TextView forgot = (TextView)findViewById(R.id.forgot);
		forgot.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent i=new Intent(Login.this,ForgotPassword.class);
				startActivity(i);
				finish();
				
			}
		});
		
		 if(!cd.isConnectingToInternet())
		 {
			 Toast.makeText(getApplicationContext(), "No network found", Toast.LENGTH_LONG).show();
		 }
		
		PASSWORD.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				// TODO Auto-generated method stub
				if(actionId==EditorInfo.IME_ACTION_DONE)
				{
					LOGIN.performClick();
					InputMethodManager imm = (InputMethodManager)getSystemService(
						      getApplicationContext().INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(PASSWORD.getWindowToken(), 0);
				}
				return true;
			}
		});
		
		LOGIN.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(String.valueOf(USERNAME.getText()).length()!=0 && String.valueOf(PASSWORD.getText()).length()!=0)
				{
					if(!cd.isConnectingToInternet())
					 {
						 Toast.makeText(getApplicationContext(), "No network found", Toast.LENGTH_LONG).show();
					 }
					else
						{
							new AsyncLogin().execute(String.valueOf(USERNAME.getText()), String.valueOf(PASSWORD.getText()));
						}
				}
				else
				{
					Toast.makeText(getApplicationContext(), "Please Enter User Name and Password", 3000).show();
				}
			}
		});
	}
	
	class AsyncLogin extends AsyncTask<String, Void, ArrayList<String>>
	  {
	    Databaseutill db;
	    String password;	    
	    ProgressDialog pDialog;
	    
	    @Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			pDialog = new ProgressDialog(Login.this);
		      try
		      {
		        pDialog.setMessage("Please wait ...");
		        pDialog.setIndeterminateDrawable(getApplicationContext().getResources().getDrawable(R.drawable.red_progress));
		        pDialog.setIndeterminate(false);
		        pDialog.setCancelable(false);
		        pDialog.show();
		      }
		      catch (Exception e)
		      {
		        e.getMessage();
		      }
	    }
	    
	    
		@Override
	    public ArrayList<String> doInBackground(String... paramVarArgs)
	    {		
			password = paramVarArgs[1];
	    	db=Databaseutill.getDBAdapterInstance(getApplicationContext());			
			ArrayList<String> data = new ArrayList<String>();
			try
			{
				//District --> name of district, geoid --> district id, moid --> employee id, roleid --> role type (i.e. Admin or MO)
				String selectionargdup = "empid,empname,roleid,moid,geoid,District";
				Webservicerequest wsc=new Webservicerequest();
				ArrayList<String> inputlist=new ArrayList<String>();
				inputlist.add("loginid");
				inputlist.add(paramVarArgs[0]);
				inputlist.add("password");
				inputlist.add(paramVarArgs[1]);
				String str = wsc.MobileWebservice(getApplicationContext(),"Login", inputlist);
				ArrayList<String> listvalue=new ArrayList<String>();
				if (str != null)
				{
					String[] arrayOfString = selectionargdup.split(",");
					int length = arrayOfString.length;
					for (int i=0; i<length; i++)
					{
						listvalue.add(arrayOfString[i]);
					}
					
					ArrayList<String> returnval=new ArrayList<String>();
					returnval = wsc.JSONEncoding(str, listvalue);
					int size = returnval.size();					
					data.add(wsc.Decrypt(returnval.get(0)));
					data.add(wsc.Decrypt(returnval.get(1)));
					data.add(wsc.Decrypt(returnval.get(2)));
					data.add(wsc.Decrypt(returnval.get(3)));
					String geoid = "",  district = "";
					for(int i=0; i<size; i+=length){
						if(i>0 && i<size-1){
							geoid += ",";
							district += ",";
						}
						geoid += wsc.Decrypt(returnval.get(i+4));
						district += wsc.Decrypt(returnval.get(i+5));						
					}
					data.add(geoid);
					data.add(district);
				}
	      }
	      catch (Exception e)
	      {
	        e.getMessage();
	      }
		  return data;
	    }
	    
		@Override
	    public void onPostExecute(ArrayList<String> paramArrayList)
	    {
	      try{
	    	  pDialog.dismiss();
	    	  if (paramArrayList.size()!= 0)
	    	  {
	    		  if (paramArrayList.size() > 0)
	    		  {
	    			  Intent localIntent = new Intent(Login.this.getApplicationContext(), Passcode.class);
	    			  localIntent.putExtra("empid", (String)paramArrayList.get(0));
	    			  localIntent.putExtra("empname", (String)paramArrayList.get(1));
	    			  localIntent.putExtra("roleid", (String)paramArrayList.get(2));
	    			  localIntent.putExtra("moid", (String)paramArrayList.get(3));
	    			  localIntent.putExtra("geoid", (String)paramArrayList.get(4));
	    			  localIntent.putExtra("District", (String)paramArrayList.get(5));
	    			  localIntent.putExtra("Password", password);
	    			  Login.this.startActivity(localIntent);
	    			  Login.this.finish();
	    		  }
	    		  else {
	    			  Toast.makeText(Login.this.getApplicationContext(), "Connection is Too Slow", Toast.LENGTH_SHORT).show();
	    		  }
	    	  }
	    	  else {
	    		  Toast.makeText(Login.this.getApplicationContext(), "Please Enter a Valid User Name and Password", Toast.LENGTH_SHORT).show();
	    	  }
	      }
	      catch(Exception e)
	      {
	    	  e.getMessage();
	      }
	    }
	}

}
