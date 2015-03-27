package com.aksha.mosaic;

import java.util.ArrayList;
import aksha.database.Databaseutill;
import aksha.database.GetData;
import aksha.login.Login;
import aksha.passcode.Passcode;
import aksha.services.Update;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.aksha.mosaic.R;

public class SplashScreen extends Activity {
	
	/**
	* <h1>Mosaic Splash Screen</h1>
	* <p>This Screen is shows only for 1 second</p>
	* <b>Note:</b> onCreate method is a default Activity lifecycle method.
	* This method is used to create view and show on screen.
	*
	* @author  Ankush Goyal
	* @version 3.0
	* @since   2015-04-02
	*/

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen);
		
		/* This Thread is used to show screen only for 1 Second (i.e. 1000 milliseconds).
		 * <p>
		 * This Thread object constructs a new Thread with a Runnable object and a newly generated name.
		 * The new Thread will belong to the same ThreadGroup as the Thread calling this constructor.
		 * A Thread is a concurrent unit of execution.
		 * It has its own call stack for methods being invoked, their arguments and local variables.
		 * Each virtual machine instance has at least one main Thread running when it is started; typically, there are several others for housekeeping.
		 * The application might decide to launch additional Threads for specific purposes.
		 * Threads in the same VM interact and synchronize by the use of shared objects and monitors associated with these objects.
		 * Synchronized methods and part of the API in Object also allow Threads to cooperate.
		 * There are basically two main ways of having a Thread execute application code.
		 * One is providing a new class that extends Thread and overriding its run() method.
		 * The other is providing a new Thread instance with a Runnable object during its creation.
		 * In both cases, the start() method must be called to actually execute the new Thread.
		 * Each Thread has an integer priority that basically determines the amount of CPU time the Thread gets.
		 * It can be set using the setPriority(int) method.
		 * A Thread can also be made a daemon, which makes it run in the background.
		 * The latter also affects VM termination behavior: the VM does not terminate automatically as long as there are non-daemon threads running.
		 * Runnable represents a command that can be executed.
		 * </p>
		*/
		
	/*	UpdateApp upApp = new UpdateApp();
		
         upApp.setContext(getApplicationContext());
         atualizaApp.execute("http://serverurl/appfile.apk");*/
		
		new Thread(new Runnable() {
			
			/*
			 * In this run method
			 * @see aksha.database.Databaseutill is use to get instance of aksha.database to open and close aksha.database or to check aksha.database.
			 * @see aksha.database.GetData class is used to input and output in local aksha.database.
			 * <p>
			 * In this run method:
			 * First:  check aksha.login table exists in local aksha.database or not.
			 * Second: If exists? then get Passcode from aksha.database else goto aksha.login page
			 * Third: If passcode length>0 ? goto passcode page and pass boolean value to passcode page to change text of button
			 * else goto aksha.login page
			 * </p>
			*/
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try
				{
						Databaseutill db=Databaseutill.getDBAdapterInstance(getApplicationContext());									 													 
						GetData get = new GetData(getApplicationContext(),db);
						
						
						//update check
						if(db.tb_exist("moup")){
						PackageManager manager = getApplicationContext().getPackageManager();
						 PackageInfo info = manager.getPackageInfo(getApplicationContext().getPackageName(), 0);						 
						 String versionName = info.versionName;
						String nversionName=get.getVersion();
						double retuenv = Double.parseDouble(nversionName);
						if(Double.parseDouble(versionName)>=retuenv){
							
							//delete table
							db.openDataBase();
							String query="drop table moup";
							db.execQuery(query);
							
							//login

							//login
							boolean check = db.tb_exist("login");
							Thread.sleep(1000);
							if(check)
							{
								ArrayList<String> pass = get.getPasscode();
								if(pass.size()>0)
								{
									if(String.valueOf(pass.get(0)).trim() != null)
									{
										PASSCODE();
									}
									else
									{
										LOGIN();
									}
								}
								else
								{
									LOGIN();
								}
							}
							else
							{
								LOGIN();
							}
							//
							
						}
						else
						{
						//
							Intent intent= new Intent(getApplicationContext(), Update.class);
							startActivity(intent);
						}
						
						}
						else{
						
						//login
						boolean check = db.tb_exist("login");
						Thread.sleep(1000);
						if(check)
						{
							ArrayList<String> pass = get.getPasscode();
							if(pass.size()>0)
							{
								if(String.valueOf(pass.get(0)).trim() != null)
								{
									PASSCODE();
								}
								else
								{
									LOGIN();
								}
							}
							else
							{
								LOGIN();
							}
						}
						else
						{
							LOGIN();
						}
						
						}
						 
						finish();
				}
				catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	private void LOGIN(){
		Intent intent= new Intent(getApplicationContext(), Login.class);
		startActivity(intent);
	}
	
	private void PASSCODE(){
		Intent intent= new Intent(getApplicationContext(), Passcode.class);
		intent.putExtra("Enter", true);
		try{
		if(getIntent().getExtras().containsKey("nmsg")){
		intent.putExtra("nmsg", getIntent().getExtras().getString("nmsg"));
		}}catch(Exception er){er.getMessage();}
		startActivity(intent);
	}
	
}
