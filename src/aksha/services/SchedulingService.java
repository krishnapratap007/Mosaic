package aksha.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import aksha.connectiondetector.ConnectionDetector;
import aksha.database.Databaseutill;
import aksha.database.GetData;
import com.aksha.mosaic.R;
import aksha.notification.NotificationActivity;
import aksha.webservice.Webservicerequest;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Parcelable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class SchedulingService extends IntentService {
    public SchedulingService() {
        super("SchedulingService");
    }
    
      
    
    
    

    @Override
    protected void onHandleIntent(Intent intent) {

			
			
		try{
			 Databaseutill db=new Databaseutill(getApplicationContext()).getDBAdapterInstance(getApplicationContext());			 
			 if(!db.tb_exist("moup"))
			 {
			ConnectionDetector cd=new ConnectionDetector(getApplicationContext());
			if(cd.isConnectingToInternet()){
				
				
			Webservicerequest wsc = new Webservicerequest();
				ArrayList<String> inputlist = new ArrayList<String>();
				
			     String resultdata=wsc.MobileWebservice(getApplicationContext(), "getversion",inputlist);			     
			     PackageManager manager = getApplicationContext().getPackageManager();
				 PackageInfo info = manager.getPackageInfo(getApplicationContext().getPackageName(), 0);
				 //String packageName = info.packageName;
	  			 //int versionCode = info.versionCode;
				 String versionName = info.versionName;
				 
				 double retuenv = Double.parseDouble(resultdata);
			
			if(retuenv>Double.parseDouble(versionName)){
														
							//read from local tables
							db.openDataBase();
							String query="drop table moup";
							db.execQuery(query);								 
							String query1 = "create table moup (id text)";							
							db.execQuery(query1);
							String queryin="insert into moup (id) values ('"+retuenv+"')";						
							db.execQuery(queryin);
							db.close();
							intent = new Intent(getApplicationContext(), Update.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);								
				    		startActivity(intent);													 
				  }
			}
		}
		}
			 
			  catch(Exception e)
			  {
				  e.getMessage();
				}
				      
    }
    
    // Post a aksha.notification indicating whether a doodle was found.
    private void sendNotification(String msg) {
    	NotificationManager    mNotificationManager = (NotificationManager)
               this.getSystemService(Context.NOTIFICATION_SERVICE);
    
        Intent intent = new Intent(this, Update.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
            intent, 0);
        
        
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
        .setSmallIcon(R.drawable.ic_launcher)
        .setContentTitle("Alert")
        .setStyle(new NotificationCompat.BigTextStyle()
        .bigText(msg))
        .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);
     //   mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
 
//
// The methods below this line fetch content from the specified URL and return the
// content as a string.
//
    /** Given a URL string, initiate a fetch operation. */
    private String loadFromNetwork(String urlString) throws IOException {
        InputStream stream = null;
        String str ="";
      
        try {
            stream = downloadUrl(urlString);
            str = readIt(stream);
        } finally {
            if (stream != null) {
                stream.close();
            }      
        }
        return str;
    }

    /**
     * Given a string representation of a URL, sets up a connection and gets
     * an input stream.
     * @param urlString A string representation of a URL.
     * @return An InputStream retrieved from a successful HttpURLConnection.
     * @throws IOException
     */
    private InputStream downloadUrl(String urlString) throws IOException {
    
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        // Start the query
        conn.connect();
        InputStream stream = conn.getInputStream();
        return stream;
    }

    /** 
     * Reads an InputStream and converts it to a String.
     * @param stream InputStream containing HTML from www.google.com.
     * @return String version of InputStream.
     * @throws IOException
     */
    private String readIt(InputStream stream) throws IOException {
      
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        for(String line = reader.readLine(); line != null; line = reader.readLine()) 
            builder.append(line);
        reader.close();
        return builder.toString();
    }
}
