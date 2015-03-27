package aksha.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;

import com.aksha.mosaic.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Update extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		try{	
			requestWindowFeature(Window.FEATURE_NO_TITLE);
	        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
	            WindowManager.LayoutParams.FLAG_FULLSCREEN);
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);				
		}
		catch(Exception er){
			Log.e("Update Activity:", er.getLocalizedMessage());
		}
		setContentView(R.layout.update);
		
		Button download = (Button)findViewById(R.id.download);
		download.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {								
				Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("market://details?id=com.aksha.mosaic"));
				startActivity(intent);
			}
		});
		
		Button cancel = (Button)findViewById(R.id.cancel);
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
	}
	
	/*class Download extends AsyncTask<Void, Void, String>{

		ProgressDialog pDialog;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			
			this.pDialog = new ProgressDialog(Update.this);
		      try
		      {
		        this.pDialog.setMessage("Please wait ...");
		        //this.pDialog.setIndeterminateDrawable(getApplicationContext().getResources().getDrawable(R.drawable.red_progress));
		        this.pDialog.setIndeterminate(false);
		        this.pDialog.setCancelable(false);
		        this.pDialog.show();
		      }
		      catch (Exception localException)
		      {
		        localException.getMessage();
		      }
			
			File sdcard = Environment.getExternalStorageDirectory();
	        File file = new File(sdcard, "PartnerPi.apk");
	        if(file.exists())
	        {
	        	file.delete();
	        }
		}
		
		
		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			String selectionargdup = "AppVersion";
			String vrenum = "";
			
		try{	 Webservicerequest wsc = new Webservicerequest();
				ArrayList<String> inputlist = new ArrayList<String>();
				inputlist.add("tablename");
				inputlist.add(wsc.Encrypt("AppVersion"));
				inputlist.add("selectionarg");
				inputlist.add(wsc.Encrypt(selectionargdup));
				inputlist.add("condition");
				inputlist.add("");
				inputlist.add("type");
				inputlist.add(wsc.Encrypt("0"));
			     String resultdata=wsc.MobileWebservice("TableData",inputlist);
			     ArrayList<String> listvalue=new ArrayList<String>();

			        if (resultdata != null)
			        {
			          String[] arrayOfString = selectionargdup.split(",");
			          for (int i = 0;i<arrayOfString.length; i++)
			          {
			            listvalue.add(arrayOfString[i]);
			          }
			          if (resultdata!=null) {
			            	inputlist=wsc.JSONEncoding(resultdata, listvalue);
			            }
			        }
			     
			     
			     PackageManager manager = getApplicationContext().getPackageManager();
				 PackageInfo info = manager.getPackageInfo("com.aksha.partnerpi", 0);
				 String packageName = info.packageName;
	  			 int versionCode = info.versionCode;
				 String versionName = info.versionName;
				 
				 double retuenv = Double.parseDouble(wsc.Decrypt(inputlist.get(0)));
				 vrenum = wsc.Decrypt(inputlist.get(0));
			if(retuenv>Double.parseDouble(versionName)){
				
				
				        URL url = new URL("http://pipartner.akshapp.com/apk/PartnerPi.apk");
					  
				        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
				        urlConnection.setRequestMethod("GET");
				        //urlConnection.setDoOutput(true);
				        
				        String eAm = Build.VERSION.RELEASE;
				        DecimalFormat dF = new DecimalFormat("0.00");
				        Number num = dF.parse(eAm);
				        double ddata= num.doubleValue();//round(val,2);
				        
				        if(ddata > 2.2 && ddata < 4.0){
				        urlConnection.setDoOutput(true);}
				        urlConnection.connect();

				        File sdcard = Environment.getExternalStorageDirectory();
				        File file = new File(sdcard, "PartnerPi.apk");

				        FileOutputStream fileOutput = new FileOutputStream(file);
				        InputStream inputStream = urlConnection.getInputStream();

				        byte[] buffer = new byte[1024];
				        int bufferLength = 0;

				        while ( (bufferLength = inputStream.read(buffer)) > 0 ) {
				            fileOutput.write(buffer, 0, bufferLength);
				        }
				        fileOutput.close();
				 
				  }
		}
			 
			  catch(Exception e)
			  {
				  e.getMessage();
				}
				  
			return vrenum;
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			
			try{
				File sdcard = Environment.getExternalStorageDirectory();
		        File file = new File(sdcard, "PartnerPi.apk");
				PackageManager manager1 = getApplicationContext().getPackageManager();
		        PackageInfo info1 =  manager1.getPackageArchiveInfo(file.getAbsolutePath(), PackageManager.GET_GIDS );
		        int versionCode1 = info1.versionCode;
		        String versionName1 = info1.versionName;
		        
		        //
		   	 //File file1 = new File("http://dsclcement.akshapp.com/Dscl%20cement.apk");
		        if(Double.parseDouble(info1.versionName)<Double.parseDouble(result)){
		        
		        	
		        	
		        	 Uri packageURI = Uri.parse("package:com.aksha.partnerpi");
					 Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
					 startActivity(uninstallIntent); 
					 
					 
		        	//
					 
					 
					 
					 
					 
			 Intent intent1 = new Intent(Intent.ACTION_VIEW);
			 intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			 
			 
			 
			
			 
			 

			 intent1.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");

			 startActivity(intent1);
			 
		        }else
		        {
		        	
	        	 file.delete();
		        }
	        }
		        
		        
			 catch (Exception e) {
		            e.printStackTrace();
		            Toast.makeText(getApplicationContext(), "You have already Installed an Updated Version", Toast.LENGTH_LONG).show();
		    }
			
			
			pDialog.dismiss();
			finish();
		  
	}
		
	     
		}
*/
}
