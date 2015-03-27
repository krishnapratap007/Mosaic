package aksha.notification;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import aksha.connectiondetector.ConnectionDetector;
import aksha.database.Databaseutill;
import aksha.database.GetData;
import com.aksha.mosaic.R;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class NotificationActivity extends Activity {
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			setContentView(R.layout.notification);
			ArrayList<HashMap<String, String>> map = new ArrayList<HashMap<String,String>>();
			try{
				ArrayList<? extends Parcelable> data = getIntent().getParcelableArrayListExtra("DataUtil");
				map = (ArrayList<HashMap<String, String>>) data;
			}catch(Exception e){
				e.getMessage();
				HashMap<String, String> dt = new HashMap<String, String>();
				dt.put("1", "");
				dt.put("2", "No Data Found");
				map.add(dt);
			}
			
			TextView message = (TextView)findViewById(R.id.message);
			TextView notificationdate = (TextView)findViewById(R.id.notificationdate);
			String date = map.get(0).get("2");
			try {
				Date dt = new SimpleDateFormat("M/d/yyyy").parse(map.get(0).get("2"));
				date = (new SimpleDateFormat("d MMMM yyyy").format(dt));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			notificationdate.setText(date);
			message.setText(Html.fromHtml(map.get(0).get("1")));
			message.setMovementMethod(LinkMovementMethod.getInstance());
			
			Button back = (Button)findViewById(R.id.back);
			back.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					finish();
				}
			});
			
			new ActiveClose().execute();
		}
		
		 class ActiveClose extends AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>{
			  
				@Override
				protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
					// TODO Auto-generated method stub
					ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String,String>>();
					try{
						ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
					if(cd.isConnectingToInternet()){
						Databaseutill db = Databaseutill.getDBAdapterInstance(getApplicationContext());
						GetData get = new GetData(getApplicationContext(),db);
						String empId = get.getEmpid().get(0);
						result.addAll(get.notificationInactive(empId));
					}
					
					}
					catch(Exception e)
					{
						e.getMessage();
					}
					return result;
				}
			}
		
		
}