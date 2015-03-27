package aksha.login;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import aksha.connectiondetector.ConnectionDetector;
import aksha.database.Databaseutill;
import aksha.database.GetData;
import com.aksha.mosaic.R;
import aksha.webservice.Webservicerequest;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ChangePassword extends Activity{
	
	Button btn;
	ActionBar actionBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	 setContentView(R.layout.changepassword);
		
		actionBar= getActionBar();
		// Enabling Home button
		actionBar.setHomeButtonEnabled(true);
		actionBar.setTitle("Mosaic");
		// Enabling Up navigation
		actionBar.setDisplayHomeAsUpEnabled(true);
	
		EditText etold=(EditText)findViewById(R.id.old_passwd);
		
		btn=(Button)findViewById(R.id.change_password_button);
		Button cancel = (Button)findViewById(R.id.cancel);
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
					EditText etnew=(EditText)findViewById(R.id.new_passwd);
					EditText etconf=(EditText) findViewById(R.id.confirm_passwd);
					if(etnew.length()!=0&&etconf.length()!=0){
						
						
						if(etnew.length()>6&&etconf.length()>6){
							

							if(String.valueOf(etnew.getText()).equals(String.valueOf(etconf.getText())))
							{
	  				AlertDialog.Builder bud=new AlertDialog.Builder(ChangePassword.this).setMessage(Html.fromHtml("Do you want to change your password?<br/>This will configure your personal information.")).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	  					
	  					@Override
	  					public void onClick(DialogInterface dialog, int which) {
	  						// TODO Auto-generated method stub
	  						dialog.dismiss();
	  					}

	  					
	  				}).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	  					
	  					@Override
	  					public void onClick(DialogInterface dialog, int which) {
	  						try{
	  						ConnectionDetector cd=new ConnectionDetector(ChangePassword.this);
	  						
	  						if(cd.isConnectingToInternet()){
	  							
	  							

	  							
	  							EditText etconfs=(EditText)findViewById(R.id.confirm_passwd);
	  							EditText etold=(EditText)findViewById(R.id.old_passwd);

GetData dt=new GetData(getApplicationContext(), Databaseutill.getDBAdapterInstance(ChangePassword.this));
ArrayList<HashMap<String, String>> emid= dt.getAllLogin();
	  							if(String.valueOf(etold.getText()).equalsIgnoreCase(emid.get(0).get("password"))){
	  							
	  							new chnagepassexe().execute(String.valueOf(emid.get(0).get("empid")),String.valueOf(etconfs.getText()),String.valueOf(etold.getText()));
	  							}else{
	  								
	  								Toast.makeText(getApplicationContext(), "Existing password did not match.", Toast.LENGTH_SHORT).show();
	  							}
	  							
	  							
	  						}else{
	  							Toast.makeText(getApplicationContext(), "No network found", Toast.LENGTH_LONG).show();
	  						}
	  						
	  						}catch(Exception er)
	  						{
	  							er.getMessage();
	  						}
	  						//
	  						
	  					}
	  				}) ;
	  				AlertDialog alt=bud.create();
	  				alt.show();
							}	
							else{
								Toast.makeText(getApplicationContext(), "Both password does not match", Toast.LENGTH_SHORT).show();							
							}
							}
								else{

									Toast.makeText(getApplicationContext(), "Password length must be greater than 6 digit", Toast.LENGTH_SHORT).show();
								}
					}
						else{
							Toast.makeText(getApplicationContext(), "Please enter password", Toast.LENGTH_SHORT).show();
						}
				
			}
			
		});
			
	
		}
		
		public class chnagepassexe extends AsyncTask<String, Void, String>{

			ProgressDialog pDialog;
			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				this.pDialog = new ProgressDialog(ChangePassword.this);
				pDialog.setCancelable(false);
				pDialog.setMessage("Please wait..");
				pDialog.show();
			}

			@Override
			protected String doInBackground(String... params) {
				
				
				//update password
				 String retval="";
				try{
					
					Webservicerequest wsc=new Webservicerequest();
					 ArrayList<String> inputlist=new ArrayList<String>();
					 ArrayList<String> returnval=new ArrayList<String>();
					 inputlist.add("tablename");
					 inputlist.add(wsc.Encrypt("ms_emp"));//tblmas_crop");
					inputlist.add("selectionarg");
					 inputlist.add(wsc.Encrypt(params[1]));//"Cropid,Cropname,Isactive");
					 inputlist.add("condition");
					 inputlist.add(wsc.Encrypt("where empid='"+params[0]+"'"));
					 inputlist.add("type");
					 inputlist.add(wsc.Encrypt("4"));
					  String resultdata=wsc.MobileWebservice(getApplicationContext(),"TableDataSfe",inputlist);
					  ArrayList<String> listvalue=new ArrayList<String>();
					  if(resultdata!=null){
						
						listvalue.add("Message");
						 returnval =wsc.JSONEncoding(resultdata,listvalue);
						 retval=String.valueOf(returnval.get(0));
											 
						
						}
				 }catch(Exception er)
				 {
					 er.getMessage();
				 }
				 

				
				//
				
				return retval;
		
			}
			
			protected void onPostExecute(String result) {
				
				try{
				if(result!=null)
				{
					if(result.length()>0)
					{
						if(new Webservicerequest().Decrypt(result).equalsIgnoreCase("1"))
						{
			  				AlertDialog.Builder bud=new AlertDialog.Builder(ChangePassword.this).setMessage("Your password has been updated, you will be logged off and you will need to login again to continue.")
			  						.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
			  					
			  					@Override
			  					public void onClick(DialogInterface dialog, int which) {		  								  						
			  					
			  						//remove old data

			  						try{
			  						File fdb=getApplicationContext().getDatabasePath( "/data/data/"
			  					            + getApplicationContext().getPackageName()
			  					            + "/databases/DBName.sqlite");
			  						
			  					 boolean val=fdb.delete();
			  								  					
			  					if(val==true){
			  					//intentifi
								Intent intent= new Intent(getApplicationContext(), Login.class);
								startActivity(intent);
								 finish();}
			  					else{
			  						Toast.makeText(getApplicationContext(), "Please contact admin or reinstall the app", Toast.LENGTH_SHORT).show();
			  					}
			  					dialog.cancel();
			  						}catch(Exception er)
			  						{
			  							er.getMessage();
			  						}
			  					
			  						
			  					}
			  				}).setCancelable(false);
			  				AlertDialog alt=bud.create();
			  				alt.show();
							
							
							
						}else{
							Toast.makeText(getApplicationContext(), "Please try again,some error occured", Toast.LENGTH_SHORT).show();
						}
						
					}
				}
				}catch(Exception er)
				{
				er.getMessage();	
				}
				pDialog.dismiss();
			}

}
	
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			  
			   switch (item.getItemId()) {
			      case android.R.id.home:
			    	  finish();	  
			         return true;
			    
			         
			      default:
			         return super.onOptionsItemSelected(item);
			         
			   }
			}
		
		@Override
		public void onBackPressed() {
			// TODO Auto-generated method stub
			 super.onBackPressed();
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