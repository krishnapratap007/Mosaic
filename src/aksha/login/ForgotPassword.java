package aksha.login;

import java.util.ArrayList;








import aksha.connectiondetector.ConnectionDetector;
import aksha.database.Databaseutill;
import com.aksha.mosaic.R;
import aksha.webservice.Webservicerequest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ForgotPassword extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forgotpassword);
		Button submit = (Button)findViewById(R.id.submit);
		
		Button login=(Button)findViewById(R.id.login);
		login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent i=new Intent(ForgotPassword.this,Login.class);
				startActivity(i);
				finish();
				
			}
		});
		
		
		
		submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				EditText text = (EditText)findViewById(R.id.text);
				 ConnectionDetector cd=new ConnectionDetector(getApplicationContext());
				 if(!cd.isConnectingToInternet())
				 {
					 Toast.makeText(getApplicationContext(), "No network found", Toast.LENGTH_LONG).show();
				 }
				 else
				 {
					 if(text.getText().toString().length()>0)
					 {new AsyncLogin().execute(text.getText().toString());}
					 else
					 {Toast.makeText(getApplicationContext(), "Enter Loginid", Toast.LENGTH_LONG).show();}
				 }
			text.setText("");
			}
		});
	}
	
	
	class AsyncLogin extends AsyncTask<String, Void, String>
	  {
	    Databaseutill db;
	  
	    String mobile;
	    
	    
		@Override
	    public String doInBackground(String... paramVarArgs)
	    {
			mobile = paramVarArgs[0];
	    	 db=Databaseutill.getDBAdapterInstance(getApplicationContext());
				String abc = "0";
	      try
	      {
	    	  Webservicerequest wsc=new Webservicerequest();
				ArrayList<String> inputlist=new ArrayList<String>();
				inputlist.add("loginid");
				inputlist.add((mobile));
	        String str = wsc.MobileWebservice(getApplicationContext(),"forgetpass", inputlist);
	       abc=str;
	      }
	      catch (Exception localException)
	      {
	        localException.getMessage();
	        abc = "5";
	      }
		return abc;
	    }
	    
	    public void onPostExecute(String paramArrayList)
	    {
	    	try{
	    	 if(paramArrayList.equalsIgnoreCase("1"))
		        {
	    		 paramArrayList = "Password has been sent to your registered Mobile Number.";
		        }
		        if(paramArrayList.equalsIgnoreCase("2"))
		        {
		        	paramArrayList = "Sorry we could not find you, please contact your admin  ";
		        }
		        if(paramArrayList.equalsIgnoreCase("0"))
		        {
		        	paramArrayList = "Sorry we could not find you, please contact your admin";
		        }
		        if(paramArrayList.equalsIgnoreCase("5"))
		        {
		        	paramArrayList = "Some Error Occures,please contact your admin";
		        }
		        finish();  
	    	  Toast.makeText(getApplicationContext(), paramArrayList, 3000).show();
	    	}catch(Exception er){er.getMessage();}
	    	  
	    }
	}
	
	
	public boolean dispatchTouchEvent(MotionEvent event) {

	    View v =getCurrentFocus();
	    boolean ret = super.dispatchTouchEvent(event);

	    if (v instanceof EditText) {
	        View w =getCurrentFocus();
	        int scrcoords[] = new int[2];
	        w.getLocationOnScreen(scrcoords);
	        float x = event.getRawX() + w.getLeft() - scrcoords[0];
	        float y = event.getRawY() + w.getTop() - scrcoords[1];

	        Log.d("Activity", "Touch event "+event.getRawX()+","+event.getRawY()+" "+x+","+y+" rect "+w.getLeft()+","+w.getTop()+","+w.getRight()+","+w.getBottom()+" coords "+scrcoords[0]+","+scrcoords[1]);
	        if (event.getAction() == MotionEvent.ACTION_UP && (x < w.getLeft() || x >= w.getRight() || y < w.getTop() || y > w.getBottom()) ) { 

	            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
	            imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
	        }
	    }
	return ret;
	}
	
 }