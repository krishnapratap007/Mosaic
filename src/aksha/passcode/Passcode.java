package aksha.passcode;
import static com.aksha.mosaic.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static com.aksha.mosaic.CommonUtilities.EXTRA_MESSAGE;
import static com.aksha.mosaic.CommonUtilities.SENDER_ID;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import com.google.android.gcm.GCMRegistrar;

import aksha.connectiondetector.ConnectionDetector;
import aksha.database.CreateTable;
import aksha.database.Databaseutill;
import aksha.database.GetData;
import aksha.homescreen.HomeScreen;
import aksha.homescreen.MOHomeScreen;
import aksha.login.Login;

import com.aksha.mosaic.AlertDialogManager;
import com.aksha.mosaic.R;
import com.aksha.mosaic.ServerUtilities;
import com.aksha.mosaic.WakeLocker;

import aksha.notification.NotificationActivity;
import aksha.webservice.Webservicerequest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class Passcode extends Activity {

	Webservicerequest wsc;
	GetData get;
	Databaseutill db;
	Sync sync;
	boolean set = false;
	boolean syncbool=false;
	String pass;
	 Button setpasscode;
	 EditText passcode;
	 String prevpasscode;
	AsyncTask<Void, Void, Void> mRegisterTask;	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	//	getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.passcode);
		
		db=Databaseutill.getDBAdapterInstance(getApplicationContext());
		get = new GetData(getApplicationContext(),db);
		wsc=new Webservicerequest();

		
		
		try{
			((TextView)findViewById(R.id.ver)).setText(((TextView)findViewById(R.id.ver)).getText()+getApplicationContext().getPackageManager().getPackageInfo(getPackageName(), 0).versionName);		
		}
		catch(Exception e){
			e.getMessage();
		}
		
		
		 set = getIntent().getBooleanExtra("Enter", false);
		 
		// new changes 1
		// if(db.tb_exist("login")){
	//		 set=true;
	//	 }
		 
		 setpasscode = (Button)findViewById(R.id.txt_setpasscode);
		 passcode = (EditText)findViewById(R.id.et_passcode);
		 final Button reset = (Button)findViewById(R.id.txt_resetpasscode);
		

		 prevpasscode = getIntent().getExtras().getString("passcodep");
		 String currentpasscode = String.valueOf(passcode.getText());
		 
		 
		//change 2
		try{
			syncbool=getIntent().getExtras().getBoolean("syncbool");
		}catch(Exception e){
			e.getMessage();
		}
		
		
		if(set)
		{
		  setpasscode.setText("Enter");
		}
		
		passcode.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				// TODO Auto-generated method stub
				if(actionId==EditorInfo.IME_ACTION_DONE){
					setpasscode.performClick();
					InputMethodManager imm = (InputMethodManager)getSystemService(
			        getApplicationContext().INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(passcode.getWindowToken(), 0);
				}
				return true;
			}
		});
		
		setpasscode.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(passcode.getText().length()!= 0)
				{
					if(passcode.getText().length()== 4)
					{
						String val=String.valueOf(passcode.getText()).trim();
						
						if(!setpasscode.getText().toString().equalsIgnoreCase("Enter"))
						{
							try {
							final String empid = getIntent().getExtras().getString("empid");
							final String empname = getIntent().getExtras().getString("empname");
							final String district = getIntent().getExtras().getString("district");
							final String geoid = getIntent().getExtras().getString("geoid");
							final String moid = getIntent().getExtras().getString("moid");
							String roleid = getIntent().getExtras().getString("roleid");
							final String password = getIntent().getExtras().getString("password");
							pass = wsc.Encrypt(val);
							
							db=Databaseutill.getDBAdapterInstance(getApplicationContext());
							ConnectionDetector cd=new ConnectionDetector(getApplicationContext());
							if(cd.isConnectingToInternet()){
							/*if(roleid.equalsIgnoreCase("5"))
								{*/
									if(!db.checkDataBase())
									{
									//	new pass().execute(val);									
									sync = new Sync(Passcode.this, db, false, true, false);
									sync.loginTable(empid, empname, district, pass, geoid, moid, password,roleid);
									sync.execute();
									}
									else
									{
										get.setLogin(empid, empname, district, pass, geoid, moid, password,roleid);
									}
								
								/*else
								{
									Toast.makeText(getApplicationContext(), "User Not Valid", Toast.LENGTH_LONG).show();
								}*/
								InputMethodManager imm = (InputMethodManager)getSystemService(
									      getApplicationContext().INPUT_METHOD_SERVICE);
									imm.hideSoftInputFromWindow(passcode.getWindowToken(), 0);
							}else{
								Toast.makeText(getApplicationContext(), "No network found", Toast.LENGTH_LONG).show();
							}
					
							if(db.checkDataBase())
							{
								if(!db.tb_exist("login"))
								{
									get.setLogin(empid, empname, district, pass, geoid, moid, password,roleid);
								}
								try{
								roleid= wsc.Decrypt(roleid);
								}catch(Exception er)
								{
									er.getMessage();
								}
								if(roleid.equalsIgnoreCase("4"))
								{
									Intent intent = new Intent(getApplicationContext(),MOHomeScreen.class);
									startActivity(intent);
									finish();
								}
								else if(roleid.equalsIgnoreCase("5"))
								{
									Intent intent = new Intent(getApplicationContext(),HomeScreen.class);
									startActivity(intent);
									finish();
								}
								
								
								
							}
							else
							{
								if(sync.getStatus() == AsyncTask.Status.FINISHED)
								{

									Toast.makeText(getApplicationContext(), "Sucessfully Downloaded Please Click on Set", Toast.LENGTH_LONG);
									if(roleid.equalsIgnoreCase("4"))
									{
										Intent intent = new Intent(getApplicationContext(),MOHomeScreen.class);
										startActivity(intent);
										finish();
									}
									else if(roleid.equalsIgnoreCase("5"))
									{
										Intent intent = new Intent(getApplicationContext(),HomeScreen.class);
										startActivity(intent);
										finish();
									}
								}
								else
								{
									Toast.makeText(getApplicationContext(), "Please Download Again Not correctly Downloaded", Toast.LENGTH_LONG);
								}
							}
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
						else
						{
							
							
							
							ArrayList<String> pass = get.getPasscode();
							String str = String.valueOf(pass.get(0)).trim();
							String strrole = String.valueOf(pass.get(1)).trim();
							if(val.equals(str)&&strrole.equalsIgnoreCase("4"))
							{	
								//if(!syncbool){
								Intent intent = new Intent(getApplicationContext(),MOHomeScreen.class);
								startActivity(intent);
								finish();
								/*}else{
									final String empid = getIntent().getExtras().getString("empid");
									final String empname = getIntent().getExtras().getString("empname");
									final String district = getIntent().getExtras().getString("District");
									final String geoid = getIntent().getExtras().getString("geoid");
									final String moid = getIntent().getExtras().getString("moid");
									final String roleid = getIntent().getExtras().getString("roleid");
									final String password = getIntent().getExtras().getString("Password");
									
									cleanData(syncbool);
									
									sync = new Sync(Passcode.this, db, false, true, false);
									sync.loginTable(empid, empname, district, str, geoid, moid, password,roleid);
									sync.execute();
									
								}*/
							}
							else if(val.equals(str)&&strrole.equalsIgnoreCase("5"))
							{
								Intent intent = new Intent(getApplicationContext(),HomeScreen.class);
								startActivity(intent);
								finish();
							}
							else
							{
								
								if(getCurrentFocus()!=null) {
							        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
							        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
							    }
								Toast.makeText(getApplicationContext(), "Enter a Valid passcode", 3000).show();
							}
						}
						
					}
					else
					{
						Toast.makeText(getApplicationContext(), "Enter 4 Digit Passcode", 3000).show();
					}
				}
				else
				{
					Toast.makeText(getApplicationContext(), "Enter Passcode", 3000).show();
				}
			}
		});
		
		
		reset.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				get.logout();
				Intent intent = new Intent(getApplicationContext(),Login.class);
				startActivity(intent);
				finish();
			}
		});
		
		
		
		// Alert dialog manager
		com.aksha.mosaic.AlertDialogManager alert = new com.aksha.mosaic.AlertDialogManager();
		
		// Connection detector
		ConnectionDetector cd;
		
		cd = new ConnectionDetector(getApplicationContext());

		// Check if Internet present
		if (!cd.isConnectingToInternet()) {
			// Internet Connection is not present
			alert.showAlertDialog(Passcode.this,
					"Internet Connection Error",
					"Please connect to working Internet connection", false);
			// stop executing code by return
			return;
		}
		
		try{
		if(getIntent().getExtras().containsKey("nmsg"))
		{
			alert.showAlertDialog(Passcode.this,
					"Alert",
					String.valueOf(getIntent().getExtras().getString("nmsg")), true);
			
		}
		}catch(Exception er){er.getMessage();}

	}
	
	
	/**
	 * Receiving push messages
	 * */
	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
			// Waking up mobile if it is sleeping
			WakeLocker.acquire(getApplicationContext());
			
			/**
			 * Take appropriate action on this message
			 * depending upon your app requirement
			 * For now i am just displaying it on the screen
			 * */
			
			// Showing received message
		//	lblMessage.append(newMessage + "\n"); notification
			
			 NotificationManager mgr=
		            (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
		        Notification note=new Notification(R.drawable.loglogo,
		                                                        "App Installed",
		                                                        System.currentTimeMillis());
		         
		        // This pending intent will open after notification click
		        PendingIntent i=PendingIntent.getActivity(getApplicationContext(), 0,getIntent(),0);
		         
		        note.setLatestEventInfo(getApplicationContext(), "",
		                                newMessage, i);
		        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		        note.sound=(alarmSound);     
		        //After uncomment this line you will see number of notification arrived
		        //note.number=2;
		        mgr.notify(1337, note);
			
			Toast.makeText(getApplicationContext(), "New Message: " + newMessage, Toast.LENGTH_LONG).show();
			
			// Releasing wake lock
			WakeLocker.release();
		}
	};
	
	public void minimize()
	{
		set = true;
		Intent main = new Intent(Intent.ACTION_MAIN);
        main.addCategory(Intent.CATEGORY_HOME);
        main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(main);
	}
	
/*	public void cleanData(boolean sync){
	
		if(sync){
			File fdb=	getDatabasePath( "/data/data/"
	            + Passcode.this.getApplicationContext().getPackageName()
	            + "/databases/DBName.sqlite");
	          fdb.delete();
		}
	}*/
	
	
	
	class pass extends AsyncTask<String, Void, ArrayList<String>>
	{
		PowerManager.WakeLock wakeLock;
		@Override
		public ArrayList<String> doInBackground(String... params) {
			ArrayList<String> returnval=new ArrayList<String>();
			try
			{
				String selectionarg="passcode='"+params[0]+"'";
				Webservicerequest wsc=new Webservicerequest();
				ArrayList<String> inputlist=new ArrayList<String>();
				inputlist.add("tablename");
				inputlist.add(wsc.Encrypt("ms_emp"));
				inputlist.add("selectionarg");
				inputlist.add(wsc.Encrypt(selectionarg));
				inputlist.add("condition");
				inputlist.add(wsc.Encrypt("where empid= '"+wsc.Decrypt(params[1])+"'"));
				inputlist.add("type");
				inputlist.add(wsc.Encrypt("1"));
				String resultdata=wsc.MobileWebservice(getApplicationContext(),"TableDataSfe",inputlist);
				ArrayList<String> listvalue=new ArrayList<String>();
				if(resultdata!=null){
				  String [] colval=selectionarg.split(",");
				  	for(int strCount=0;strCount<colval.length;strCount++)
					{
				  		listvalue.add(colval[strCount]);
					}					
				  returnval =wsc.JSONEncoding(resultdata,listvalue);
				}
			}
			catch(Exception ex)
			{
				ex.getMessage();
			}
			return returnval;
		}
		
		@Override
		public void onPostExecute(ArrayList<String> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			try{
				  wakeLock.release();
			  }
			  catch(Exception e){
				  e.getMessage();
			  }
		}
		
		@Override
		public void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			
			try{
				  PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
				  wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"MyWakelockTag");
				  wakeLock.acquire();
				  getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
			  }
			  catch(Exception e){
				  e.getMessage();
			  }
		}
		
	}
	
	
	
	class Sync extends AsyncTask<Void, Integer, Integer>
	{
		PowerManager.WakeLock wakeLock;
		int count=1;
		Context context;
		Databaseutill db;
		int myProgress;
		ProgressDialog pDialog;
		String st1 = null;
		String st2 = null;
		String st3 = null;
		String st4 = null;
		String st5 = null;
		String st6 = null;
		String st7 = null;
		String st8 = null;
		CreateTable tb;
		String urlstr;
		boolean knowledge = false;
		boolean pog = false;
		boolean image = false;
		Context cont;
		int max=4;
	  
	  public Sync(Context paramContext, Databaseutill paramDatabaseutill, boolean knowledge, boolean pog, boolean image)
	  {
	    this.context = paramContext;
	    this.db = paramDatabaseutill;
	    this.tb = new CreateTable();
	   // this.urlstr = "http://182.18.129.19/indogulf/assets/mimages/";
	    this.urlstr = "";
	    this.knowledge = knowledge;
	    this.pog = pog;
	    this.image = image;
	  }
	  
	  @Override
	  protected Integer doInBackground(Void... paramVarArgs)
	  {
		  try{
			  if(!db.checkDataBase())
			  {
				  db.createDatabase();
				  db.openDataBase();
				  myProgress++;
				  String value="";
				  new GetData(getApplicationContext(),this.db).setLogin(this.st1, this.st2, this.st3, this.st4, this.st5, this.st6, this.st7,this.st8);
				  db.close();
				  if(pog)
				  {
					  
					  
					  //data for fa roleid=5
					  if(this.st8.equalsIgnoreCase("5")){
					  ArrayList<String> inputlist=new ArrayList<String>();
					  inputlist.add("faid");
					  inputlist.add(this.st1);
					  db.openDataBase();
                      tb.createMasterTablenopfa("getfadistrict","ms_emp_geo_map","Districtid,Districtname",false,false,false,db,context,inputlist);
                      myProgress++;
                      publishProgress(myProgress);
                      db.close();
					  
					  db.openDataBase();
                      tb.createMasterTablenopfa("getmandi","ms_geo","mandiid,mandiname,districtid",false,false,false,db,context,inputlist);
                      myProgress++;
                      publishProgress(myProgress);
                      db.close();
                      
                      db.openDataBase();
                      tb.createMasterTablenopfa("getactivity","ap_jobmaster","jobid,jobdesc",false,false,false,db,context,inputlist);
                      myProgress++;
                      publishProgress(myProgress);
                      db.close();
                      
					  db.openDataBase();
                      tb.createMasterTablenopfa("getretal","ms_chan","chanid,firm_name,mandiid",false,false,false,db,context,inputlist);
                      myProgress++;
                      publishProgress(myProgress);
                      db.close();
                      
                      db.openDataBase();
                      tb.createMasterTablenopfa("getvillage","villages","villid,villname,mandiid",false,false,false,db,context,inputlist);
                      myProgress++;
                      publishProgress(myProgress);
                      db.close();					  	
					  
					  db.openDataBase();
                      tb.createMasterTablenopfa("getcrops","crops","cropid,cropname",false,false,false,db,context,inputlist);
                      myProgress++;
                      publishProgress(myProgress);
                      db.close();					  	
					  }
					  //end data for fa
                      
                      
                      
                      //for role id 4
					  if(this.st8.equalsIgnoreCase("4")){
					  db.openDataBase();					 
					  tb.createMasterTablenop("GETMDO","ms_emp","empid,empname,roleid,moid,geoid,District",false,false,false,db,context,this.st1);
					  myProgress++;
					  publishProgress(myProgress);
					  db.close();	
					  
					  db.openDataBase();					 
					  tb.createMasterTablenop("GETMDOFADISTRICT","ms_emp_geo_map","empgeoid,empid,empname,districtid,districtname",false,false,false,db,context,this.st1);
					  myProgress++;
					  publishProgress(myProgress);
					  db.close();	
					  
					  
					  db.openDataBase();					 
					  tb.createMasterTablenop("getmandiTH","ms_geo","mandiid,mandiname,districtid",false,false,false,db,context,this.st1);
					  myProgress++;
					  publishProgress(myProgress);
					  db.close();	
					  
					  db.openDataBase();					 
					  tb.createMasterTablenop("getretalTH","ms_chan","chanid,firm_name,mandiid,mobile,districtname,districtID",false,false,false,db,context,this.st1);
					  myProgress++;
					  publishProgress(myProgress);
					  db.close();
					  
					  db.openDataBase();					 
					  tb.createMasterTablenop("getactivity","ap_jobmaster","jobid,jobdesc",false,false,false,db,context,this.st1);
					  myProgress++;
					  publishProgress(myProgress);
					  db.close();					  					  
					  
					  db.openDataBase();					 
					  tb.createMasterTablenop("getvillageTH","ms_vill","villid,villname,mandiid",false,false,false,db,context,this.st1);
					  myProgress++;
					  publishProgress(myProgress);
					  db.close();
					  }
					  
				
				  }	        	        			
			  
			  }
		  }
		  catch (Exception localException)
		  {
			  localException.getMessage();
			  count=0;
		  }
		return count;
	  }
	  
	  public void loginTable(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, String password,String roleid)
	  {
	    this.st1 = paramString1;
	    this.st2 = paramString2;
	    this.st3 = paramString3;
	    this.st4 = paramString4;
	    this.st5 = paramString5;
	    this.st6 = paramString6;
	    this.st7 = password;
	    this.st8 = roleid;
	  }
	  
	  @Override
	  protected void onPostExecute(Integer result) {
		 
		 
		 try{
			  wakeLock.release();
		  
		 
		 {
			 

				try{
				// Make sure the device has the proper dependencies.
				GCMRegistrar.checkDevice(getApplicationContext());

				try{
				// Make sure the manifest was properly set - comment out this line
				// while developing the app, then uncomment it when it's ready.
				GCMRegistrar.checkManifest(getApplicationContext());
				}catch(Exception er){er.getMessage();}
				
				registerReceiver(mHandleMessageReceiver, new IntentFilter(
						DISPLAY_MESSAGE_ACTION));
				
				
				// Get GCM registration id
				final String regId = GCMRegistrar.getRegistrationId(getApplicationContext());

				// Check if regid already presents
				if (regId.equals("")) {
					// Registration is not present, register now with GCM			
					GCMRegistrar.register(getApplicationContext(), SENDER_ID);
				} else {
					// Device is already registered on GCM
					if (GCMRegistrar.isRegisteredOnServer(getApplicationContext())) {
						// Skips registration.				
						//Toast.makeText(getApplicationContext(), "Already registered with GCM", Toast.LENGTH_LONG).show();
					} else {
						// Try to register again, but not in the UI thread.
						// It's also necessary to cancel the thread onDestroy(),
						// hence the use of AsyncTask instead of a raw thread.
						final Context context = getApplicationContext();
						mRegisterTask = new AsyncTask<Void, Void, Void>() {

							@Override
							protected Void doInBackground(Void... params) {
								// Register on our server
								// On server creates a new user
								ServerUtilities.register(context, getIntent().getExtras().getString("empname"), getIntent().getExtras().getString("empid"), regId);
								return null;
							}

							@Override
							protected void onPostExecute(Void result) {
								mRegisterTask = null;
							}

						};
						mRegisterTask.execute(null, null, null);
					}}
				}catch(Exception er){
					Toast.makeText(getApplicationContext(), "Device not registered with GCM", Toast.LENGTH_LONG).show();
					er.getMessage();
					}
				
			 
			 
			 //data
			 
			 Intent intent;
			 if(this.st8.equalsIgnoreCase("4")){
				 intent = new Intent(getApplicationContext(),MOHomeScreen.class);
				 
			 }else{
			  intent = new Intent(getApplicationContext(),HomeScreen.class);
			 }
			 context.startActivity(intent);
			 
			 finish();
			 pDialog.dismiss();
		 }
		 }
		  catch(Exception e){
			  e.getMessage();
		  }
		 
	  } 
	  
	  @Override
	  protected void onPreExecute() { 
		  
		  try{
			  PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
			  wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"MyWakelockTag");
			  wakeLock.acquire();
			  getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		  }
		  catch(Exception e){
			  e.getMessage();
		  }
		  
		  pDialog = new ProgressDialog(context);
			try{
				pDialog.setMessage("Downloading data, Please wait...");
				pDialog.setMax(max);
				pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				pDialog.setProgressDrawable(context.getResources().getDrawable(R.drawable.custom_progress_bar_horizontal));
				pDialog.setIndeterminate(false);
				pDialog.setCancelable(false);
				pDialog.show();
			}
			catch(Exception e)
			{
				e.getMessage();
			}
			myProgress = 0;
			pDialog.setSecondaryProgress(0);
	   } 
	  
	  @Override
	  protected void onProgressUpdate(Integer... values) { 
		  pDialog.setProgress(values[0]);
		  pDialog.setSecondaryProgress(values[0] + 1);
		  count++;
	  }
	  
	  private void downloadfolderdata(String foldername)
	  {
		  try{
			  Webservicerequest wsc=new Webservicerequest();
			  ArrayList<String> inputlist=new ArrayList<String>();
			  inputlist.add("dir");
			  inputlist.add(wsc.Encrypt(foldername));
			  ArrayList<String> returnval = new ArrayList<String>();
			  String resultdata=wsc.MobileWebservice(getApplicationContext(),"listFolderFiles",inputlist);
			  if(resultdata!=null)
			  {
				  ArrayList<String> listvalue = new ArrayList<String>();
				  listvalue.add("Message");
				  returnval =wsc.JSONEncoding(resultdata,listvalue);
			  }
			  for(int icount=0;icount<returnval.size();icount++)
			  {
				  if(icount==(returnval.size()/2))
				  {
					  myProgress++; 
					  publishProgress(myProgress);
				  }
				  
				  String str = wsc.Decrypt(returnval.get(icount));
				  str=str.replace("\\", "/");
				  String fval=str.replace(" ", "%20");
				  String URLtext=urlstr+fval;
				  String PATHF = "/data/data/"+ context.getPackageName()+ "/images";
				  File mFolder = new File(PATHF);
		          if (!mFolder.exists()) {
		        	  mFolder.mkdir();
		          }
		          String PATHFs = "/data/data/"+ context.getPackageName()+ "/images/"+foldername;
				  File mFolders = new File(PATHFs);
		          if (!mFolders.exists()) {
		        	  mFolders.mkdir();
		          }
		          
		          String PATH = "/data/data/"+ context.getPackageName()+ "/images/"+str;
		          File mFile = new File(PATH);
		          if (!mFile.exists()) {
		        	  int count;
		        	  URL url = new URL(URLtext);
		        	  URLConnection conection = url.openConnection();
		        	  conection.connect();
		        	  // getting file length
		        	  int lenghtOfFile = conection.getContentLength();
		        	  // input stream to read file - with 8k buffer
		        	  InputStream input = new BufferedInputStream(url.openStream(), 8192);
		        	  // Output stream to write file
		        	  FileOutputStream fos=new FileOutputStream(PATH);
		        	  //OutputStream output = fos; 
		        	  byte data[] = new byte[1024];  
		        	  long total = 0; 
		        	  while ((count = input.read(data)) != -1) {
		        		  total += count;			              			              			              
		        		  // writing data to file
		        		  fos.write(data, 0, count);
		        	  }
		        	  // flushing output
		        	  fos.flush();
		        	  // closing streams
		        	  fos.close();
		        	  input.close();
		        	  System.gc();  
		           	}
		      }
		  }catch (Exception e) {
			e.getMessage();
		  }
	  }
	}
	@Override
	protected void onDestroy() {
	if (mRegisterTask != null) {
	mRegisterTask.cancel(true);
	}
	try {
	unregisterReceiver(mHandleMessageReceiver);
	GCMRegistrar.onDestroy(this);
	} catch (Exception e) {
	Log.e("UnRegister Receiver Error", "> " + e.getMessage());
	}
	super.onDestroy();
	}
}
