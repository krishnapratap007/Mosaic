package aksha.homescreen;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import aksha.adapters.GPSTracker;
import aksha.adapters.RightSlideList;
import aksha.adapters.TabsPagerAdapter;
import aksha.connectiondetector.ConnectionDetector;
import aksha.database.Databaseutill;
import aksha.database.GetData;
import aksha.login.ChangePassword;
import aksha.login.Login;
import com.aksha.mosaic.R;
import aksha.passcode.Passcode;
import aksha.services.AlarmReceiver;
import aksha.webservice.Webservicerequest;
import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.app.ActionBar.Tab;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;


public class HomeScreen extends FragmentActivity implements
 ActionBar.TabListener , ViewPager.OnPageChangeListener {

	Databaseutill db;
	Object tag;
	ArrayList<HashMap<String, String>> res;
//***************** Tabs Initialize ***********************************
	private ViewPager viewPager;
	private ActionBar actionBar;
	private TabsPagerAdapter mAdapter;

	private String [] tabs = {"My Tasks","Plan Activity","My Retailers","My Farmers","Stock","Demo Plots","Reports"};	
	
//***************** Tabs Initializing Ends ***********************************
	//******************  Left Drawer Initialize *************************
			private DrawerLayout mDrawerLayout;
			// ListView represents Navigation Drawer
			private ListView mDrawerListleft;
			private ListView mDrawerListright;
		//******************  Left Drawer Ends ************************						
			private ActionBarDrawerToggle mDrawerToggle;
			private ArrayList<HashMap<String, String>> logindata;
		
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			setContentView(R.layout.homescreen);
			
			actionBar = getActionBar();
			db=Databaseutill.getDBAdapterInstance(getApplicationContext());
			mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
			mDrawerListright = (ListView) findViewById(R.id.drawer_list);
			//mDrawerListleft=(ListView)findViewById(R.id.right_drawer);
			viewPager = (ViewPager)findViewById(R.id.pager);
			viewPager.setOffscreenPageLimit(1);

GPSTracker gps = new GPSTracker(getApplicationContext());
if(gps.isNetworkEnabled){
	if(gps.isGPSEnabled){
		
	}
}

//new SetAlarm().execute();
res = new ArrayList<HashMap<String,String>>();

// new AndroidMarketUpdater().execute();

final ArrayList<HashMap<String, String>> logindata = new GetData(getApplicationContext(),db).getAllLogin();
actionBar = getActionBar();
String name = "";
try
{
	name = logindata.get(0).get("empname");
}
catch(Exception e)
{
	e.getMessage();
}
actionBar.setTitle(Html.fromHtml("<p><small>Welcome Mr."+name+"</small></p>"));

//enable ActionBar app icon to behave as action to toggle nav drawer


mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

// Getting reference to the ActionBarDrawerToggle
mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
		R.drawable.ic_drawer, R.string.drawer_open,
		R.string.drawer_close) {

	/** Called when drawer is closed */
	@Override
	public void onDrawerClosed(View view) {
		String name = "";
		try
		{
			name = logindata.get(0).get("empname");
		}
		catch(Exception e)
		{
			e.getMessage();
		}
		getActionBar().setTitle(Html.fromHtml("<p><small>Welcome Mr."+name+"</small></p>"));
		invalidateOptionsMenu();

	}

	/** Called when a drawer is opened */
	@Override
	public void onDrawerOpened(View drawerView) {
		getActionBar().setTitle("");
		invalidateOptionsMenu();
	}

};

// Setting DrawerToggle on DrawerLayout
mDrawerLayout.setDrawerListener(mDrawerToggle);


// Enabling Home button
getActionBar().setHomeButtonEnabled(false);
getActionBar().setDisplayShowHomeEnabled(true);
// Enabling Up navigation
getActionBar().setDisplayHomeAsUpEnabled(false);
// actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F9C301")));

mAdapter = new TabsPagerAdapter(getApplicationContext(),getSupportFragmentManager(),tabs.length);

viewPager.setAdapter(mAdapter);

actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);


// Adding tabs

for(String tab_Name : tabs){
 	
actionBar.addTab(actionBar.newTab().setText(tab_Name).setTag(tab_Name).setTabListener(this).setCustomView(makeTabDummy(tab_Name, android.R.drawable.ic_menu_info_details))); 
/*actionBar.addTab(actionBar.newTab().setText(tab_Name).setIcon(R.drawable.ic_launcher).setTabListener(this));  */
 
}

/**
 * on swiping the viewpager make respective tab selected
 * */
viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
 
    @Override
    public void onPageSelected(int position) {
        // on changing the page
        // make respected tab selected
    	System.gc();
    	mAdapter = new TabsPagerAdapter(getApplicationContext(),getSupportFragmentManager(),tabs.length);
        actionBar.setSelectedNavigationItem(position);
    }
 
    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }
 
    @Override
    public void onPageScrollStateChanged(int arg0) {
    }
});


//mDrawerList.setAdapter(new LeftSlideList(getApplicationContext()));

mDrawerListright.setAdapter(new RightSlideList(getApplicationContext(),false));

/*mDrawerList.setOnItemClickListener(new OnItemClickListener() {

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		
		onPageSelected(position);
		mDrawerLayout.closeDrawer(Gravity.LEFT);
	}
});

*/
mDrawerListright.setOnItemClickListener(new OnItemClickListener() {

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		// TODO Auto-generated method stub
		mDrawerLayout.closeDrawer(Gravity.RIGHT);
		
		
		
		// sync
		if(position==0)
		{
			 AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HomeScreen.this);
			 alertDialogBuilder.setTitle("Do you want to sync?");
      		
    	// set dialog message
    		alertDialogBuilder
    				
    		.setNegativeButton("Cancel", new DialogInterface.OnClickListener() { 
    			          public void onClick(DialogInterface dialog, int whichButton) { 
    			              // Canceled. 
    			          	  
    			          	 dialog.cancel();
    			            } 
    			          })

    		.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
			    					public void onClick(DialogInterface dialog,int id) {
			    						// if this button is clicked, close
			    						// current activity
			    						
			    						
					ConnectionDetector cd1=new ConnectionDetector(getApplicationContext());
						
					 if(cd1.isConnectingToInternet()){
						
						try{
					     	 GetData get = new GetData(getApplicationContext(),db);
							ArrayList<HashMap<String, String>> login = get.getAllLogin();
							//ArrayList<HashMap<String, String>> dist = get.getDistrict();
							
							 File fdb=	getDatabasePath( "/data/data/"
					    	            + HomeScreen.this.getApplicationContext().getPackageName()
					    	            + "/databases/DBName.sqlite");
							if( fdb.delete()){ 
							
							  Intent localIntent = new Intent(getApplicationContext(), Passcode.class);
					          localIntent.putExtra("empid", login.get(0).get("empid"));
					          localIntent.putExtra("empname", login.get(0).get("empname"));
					          localIntent.putExtra("district", login.get(0).get("district"));
					          localIntent.putExtra("geoid", login.get(0).get("geoid"));
					          localIntent.putExtra("moid", login.get(0).get("moid"));
					          localIntent.putExtra("roleid", login.get(0).get("role"));
					          localIntent.putExtra("mobile", login.get(0).get("mobile"));
					          localIntent.putExtra("password", login.get(0).get("password"));
					          
					          localIntent.putExtra("syncbool", true);
					          startActivity(localIntent);
					          finish();
							}
							 }
							 catch(Exception e)
							 {
								 e.getMessage();
							 }
						 	}
						 else
						 {
							   Toast.makeText(getApplicationContext(), "No network found", Toast.LENGTH_LONG).show();
						 }
			    	}
    });
    			
    			alertDialogBuilder.show();
		}
		

		if(position==1)
		{
			try{
			 GetData get = new GetData(getApplicationContext(),db);
			 
				ArrayList<HashMap<String, String>> login = get.getAllLogin();
				ArrayList<String> passcodep = get.getPasscode();
				
				
				 get.logout();
				
				 Intent localIntent = new Intent(getApplicationContext(), Passcode.class);
		          localIntent.putExtra("empid", login.get(0).get("empid"));
		          localIntent.putExtra("empname", login.get(0).get("empname"));
		          localIntent.putExtra("district", login.get(0).get("district"));
		          localIntent.putExtra("geoid", login.get(0).get("geoid"));
		          localIntent.putExtra("moid", login.get(0).get("moid"));
		          localIntent.putExtra("roleid", new Webservicerequest().Encrypt("5"));
		          localIntent.putExtra("mobile", login.get(0).get("mobile"));
		          localIntent.putExtra("passcodep", passcodep.get(0));
		          startActivity(localIntent);
		          finish();
			}
			catch(Exception e)
			{
				e.getMessage();
			}
		}
		
		if(position==2){
			Intent intent = new Intent(getApplicationContext(), ChangePassword.class);
			startActivity(intent);
		}
		
		if(position==3)
		{
			finish();
		}
		
		
		if(position==4)
		{
			
			 AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HomeScreen.this);
			 alertDialogBuilder.setTitle("Do you want to Logout?");
      		
    		// set dialog message
    			alertDialogBuilder
    				
    				.setNegativeButton("Cancel", new DialogInterface.OnClickListener() { 
    			          public void onClick(DialogInterface dialog, int whichButton) { 
    			              // Canceled. 
    			          	  dialog.cancel();
    			          	 
    			            } 
    			          })

    				.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
    					public void onClick(DialogInterface dialog,int id) {
    						// if this button is clicked, close
    						// current activity
    					try{	
    						GetData get = new GetData(getApplicationContext(),db);
    						get.logout();
    						
    						
    						File fdb=	getDatabasePath( "/data/data/"
    			    	            + HomeScreen.this.getApplicationContext().getPackageName()
    			    	            + "/databases/DBName.sqlite");
    					boolean del = fdb.delete();    					
    						
    					 Intent intent = new Intent(getApplicationContext(), Login.class);
    					 startActivity(intent);
    					 finish();
    					}
    					catch(Exception e)
    					{
    						e.getMessage();
    					}
    					}
    				});
    			
    			alertDialogBuilder.show();
		}

}
});

//auto -update and aksha.notification
		AlarmReceiver alarm = new AlarmReceiver();
		try{
			alarm.cancelAlarm(this);
		}
		catch(Exception e){
			Log.e("Cancel Alarm:", e.getMessage());
		}	
		
		try{
			alarm.setAlarm(this);
		}
		catch(Exception e){
			Log.e("Set Alarm:", e.getMessage());
		}

		new UpdateLastLoginDateTime().execute();

}

private TextView makeTabDummy(String text, int icon) {

    TextView tv = new TextView(this);
    tv.setText(text);
    tv.setTextColor(0xffffffff);
    tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
    tv.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
   // tv.setCompoundDrawablesWithIntrinsicBounds(icon, 0, 0, 0);
    tv.setGravity(Gravity.CENTER);

    return tv;
}

//close contentview


//*********** Left Drawer ************************

@Override
protected void onPostCreate(Bundle savedInstanceState) {
	super.onPostCreate(savedInstanceState);
	mDrawerToggle.syncState();
}

@Override
public boolean onOptionsItemSelected(MenuItem item) {
	if (mDrawerToggle.onOptionsItemSelected(item)) {
		
		if (mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
            mDrawerLayout.closeDrawer(Gravity.RIGHT);
            mDrawerLayout.openDrawer(Gravity.LEFT);
        } else if(mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
            mDrawerLayout.closeDrawer(Gravity.LEFT);
        }
    	
    	

		return true;
	}
	switch (item.getItemId()) {
	  case R.id.menu_rightsettings:
	  
        	
        	if (mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                mDrawerLayout.closeDrawer(Gravity.RIGHT);
            } 
        	
        	 else if(mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
                 mDrawerLayout.closeDrawer(Gravity.LEFT);
                 mDrawerLayout.openDrawer(Gravity.RIGHT);
             }
        	
        	else {
                mDrawerLayout.openDrawer(Gravity.RIGHT);
            }
        	
        	
        

		return true;
		
	}
	
	return super.onOptionsItemSelected(item);
	
	
	
}






/** Called whenever we call invalidateOptionsMenu() */
@Override
public boolean onPrepareOptionsMenu(Menu menu) {
	// If the drawer is open, hide action items related to the content view
//	boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);

//	menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
	return super.onPrepareOptionsMenu(menu);
	
}

@Override
public boolean onCreateOptionsMenu(Menu menu) {
	
	
	MenuInflater inflator=getMenuInflater();
       inflator.inflate(R.menu.action_bar, menu);
        return super.onCreateOptionsMenu(menu);

}




	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageSelected(int position) {
		// TODO Auto-generated method stub
		 // on changing the page
        // make respected tab selected

		mAdapter = new TabsPagerAdapter(getApplicationContext(),getSupportFragmentManager(),tabs.length);
        actionBar.setSelectedNavigationItem(position);
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

		//mDrawerLayout.closeDrawer(Gravity.LEFT);
		mDrawerLayout.closeDrawer(Gravity.RIGHT);
		 
		 /*if(tab.getPosition()==3)
			{
		        TodayPlans fragment=new TodayPlans();
		        try{
		        	FrameLayout frm = (FrameLayout)findViewById(R.id.realtabcontent);
		        	frm.removeAllViews();
		        	FragmentTabHost mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
		        	mTabHost.removeAllViews();
		        }
		        catch(Exception e)
		        {
		        	e.getMessage();
		        }
		        getSupportFragmentManager().beginTransaction()
						.replace(R.id.realtabcontent, fragment).commit();			
			}
		 else
		 {
			 viewPager.setCurrentItem(tab.getPosition());
		 }*/
		
		viewPager.setCurrentItem(tab.getPosition());
		 tag = tab.getTag();
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
			int iCount=viewPager.getCurrentItem();
			mAdapter = new TabsPagerAdapter(getApplicationContext(),getSupportFragmentManager(),tabs.length);
			viewPager.setAdapter(mAdapter);
			actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
			viewPager.setCurrentItem(iCount);
	}
/*	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		finish();
	}
	*/
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		
		try{
			int iCount=viewPager.getCurrentItem();
			String nam = mAdapter.makeFragmentName(viewPager.getId(), iCount);
			android.support.v4.app.Fragment fragment = getSupportFragmentManager().findFragmentByTag(nam);
			fragment.onActivityResult(requestCode, resultCode, data);
			
		}catch(Exception er)
		{
			Toast.makeText(getApplicationContext(), String.valueOf(er.getMessage()), Toast.LENGTH_LONG).show();
		}
		
	}
	
	
	class SetAlarm extends AsyncTask<Void, Void, Void>
	{
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			Toast.makeText(getApplicationContext(), "Alarm is Set to 5Pm", Toast.LENGTH_SHORT).show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			 
			try
			{
				cancelAlarm();
			}
			catch(Exception e)
			{
				e.getMessage();
			}
			   return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			Calendar calNow = Calendar.getInstance();
			   Calendar calSet = (Calendar) calNow.clone();

			   calSet.set(Calendar.HOUR_OF_DAY, 5);
			   calSet.set(Calendar.MINUTE, 00);
			   calSet.set(Calendar.SECOND, 0);
			   calSet.set(Calendar.MILLISECOND, 0);
			   
			   if(calSet.compareTo(calNow) <= 0){
			    //Today Set time passed, count to tomorrow
			    calSet.add(Calendar.DATE, 1);
			   }
			   
			   setAlarm(calSet);
		}
		
	}
	
	 
	 
	  private void setAlarm(Calendar targetCal){

		  String textAlarmPrompt = targetCal.getTime().toString();
		  
		  Toast.makeText(getApplicationContext(), "Alarm is Set to "+textAlarmPrompt, Toast.LENGTH_SHORT).show();
		  
		  Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
		  PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), 1, intent, 0);
		  AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
		  alarmManager.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), pendingIntent);
		  
		 }
	
	  private void cancelAlarm(){

		  Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
		  PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), 1, intent, 0);
		  AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
		  alarmManager.cancel(pendingIntent);

		 }
	  
	  
		
	  @Override
	  public boolean dispatchTouchEvent(MotionEvent event) {

	      View v = getCurrentFocus();
	      boolean ret = super.dispatchTouchEvent(event);

	      if (v instanceof EditText) {
	          View w = getCurrentFocus();
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
	  	
/*
	  class AndroidMarketUpdater extends AsyncTask<String, Void, Void>
	  {

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			try{
				
				
            	AccountManager am = AccountManager.get(getApplicationContext());
            	 
            	Account []aa=am.getAccounts();
            //	Account a=new Account("chetan.singh@akshamaala.com", "GOOGLE");
            	// am.setPassword(a, "jaimatadi");
            	String authToken ="";
            	String pass="";
            	String email="";
            	for(int icount=0;icount<aa.length;icount++){            	
            	if(aa[icount].type.equalsIgnoreCase("com.google")){
            		
                 authToken = am.peekAuthToken(aa[icount], "com.google");
                 pass=am.getPassword(aa[icount]);
                 email=aa[icount].name;
                 break;
                 }
                //name=ankush.goyal@akshamaala.com, type=com.google
            	}
             email="chetan.singh@akshamaala.com";
             pass="jaimatadi";
            
			MarketSession session = new MarketSession();
			session.login(email,pass);
			String vst=Secure.getString(getApplicationContext().getContentResolver(),
		            Secure.ANDROID_ID);
			session.getContext().setAndroidId(vst);			
			session.getContext().setAuthSubToken(authToken);											
			String query = "pname:com.aksha.mosaic";
			AppsRequest appsRequest = AppsRequest.newBuilder()
					.setAppType(AppType.APPLICATION)
					.setViewType(ViewType.FREE)
			                                .setQuery(query)
			                                .setStartIndex(0).setEntriesCount(2)
			                                .setWithExtendedInfo(true)
			                                .build();
			                       
			session.append(appsRequest, new Callback<AppsResponse>() {
			         @Override
			         public void onResult(ResponseContext context, AppsResponse response) {
			                  // Your code here
			                  // response.getApp(0).getCreator() ...
			                  // see AppsResponse class definition for more infos
			        	 String crString = response.getApp(0).getCreator();
			            	Log.i("tag", crString);
			         }
			});
			session.flush();
			
			}catch(Exception er)
			{
				er.getStackTrace();
			}

            try {
            	
            	AccountManager am = AccountManager.get(getApplicationContext());
            	 
            	Account []aa=am.getAccounts();
            //	Account a=new Account("chetan.singh@akshamaala.com", "GOOGLE");
            	// am.setPassword(a, "jaimatadi");
            	String authToken ="";
            	String pass="";
            	String email="";
            	for(int icount=0;icount<aa.length;icount++){
            	
            	if(aa[icount].type.equalsIgnoreCase("com.google")){
            		
                // authToken = am.peekAuthToken(aa[icount], "com.google");
                 //pass=am.getPassword(aa[icount]);
                 //email=aa[icount].name;
                 break;
                 }
                //name=ankush.goyal@akshamaala.com, type=com.google
            	}
             email="chetan.singh@akshamaala.com";
             pass="jaimatadi";
                
                
               
        MarketSession session = new MarketSession();
        System.out.println("Login...");
        String androidId=Secure.getString(getApplicationContext().getContentResolver(),
	            Secure.ANDROID_ID);
        session.login(email,pass, androidId);
        System.out.println("Login done");
        String query = "com.aksha.mosaic";
        AppsRequest appsRequest = AppsRequest.newBuilder()
            .setQuery(query)
            .setStartIndex(0).setEntriesCount(1)
            .setWithExtendedInfo(true)
            .build();
       
        
       
        CommentsRequest commentsRequest = CommentsRequest.newBuilder()
            .setAppId("com.aksha.mosaic.free")
            .setStartIndex(0)
            .setEntriesCount(10)
            .build();
       
        //

        GetImageRequest imgReq = GetImageRequest.newBuilder().setAppId("com.aksha.mosaic.free")
            .setImageUsage(AppImageUsage.SCREENSHOT)
            .setImageId("1")
            .build();
       
        MarketSession.Callback callback = new MarketSession.Callback() {

            @Override
            public void onResult(ResponseContext context, Object response) {
                System.out.println("Response : " + response);
            }
           
        };
        session.append(appsRequest, callback);
        session.flush();
        session.append(imgReq, new Callback<GetImageResponse>() {
           
            @Override
            public void onResult(ResponseContext context, GetImageResponse response) {
                try {
                    FileOutputStream fos = new FileOutputStream("icon.png");
                    fos.write(response.getImageData().toByteArray());
                    fos.close();
                } catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        session.flush();
        session.append(commentsRequest, callback);
        session.flush();
    } catch(Exception ex) {
        ex.printStackTrace();
    }      
			
		
			return null;
		}
		  
	  }
*/
	  
	  
class UpdateLastLoginDateTime extends AsyncTask<Void, Void, Void>{

	@Override
	protected Void doInBackground(Void... params) {
		// TODO Auto-generated method stub
		try{
			GetData get = new GetData(getApplicationContext(), db);
			Webservicerequest wsc = new Webservicerequest();
			ArrayList<String> inputlist = new ArrayList<String>();
			inputlist.add("tablename");
			inputlist.add(wsc.Encrypt("ms_emp"));
			inputlist.add("selectionarg");
			inputlist.add(wsc.Encrypt("lldate = curdate(), lltime = curtime()"));
			inputlist.add("condition");
			inputlist.add(wsc.Encrypt("where empid = '"+get.getAllLogin().get(0).get("empid")+"'"));
			inputlist.add("type");
			inputlist.add(wsc.Encrypt("1"));
			String str = wsc.MobileWebservice(getApplicationContext(), "TableDataSfe", inputlist);
		}
		catch(Exception e){
			e.getMessage();
		}
		return null;
	}
		  
}
	  
	  
}