package aksha.farmers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import aksha.adapters.Myspinner;
import aksha.connectiondetector.ConnectionDetector;
import aksha.database.Databaseutill;
import aksha.database.GetData;
import com.aksha.mosaic.R;
import aksha.webservice.Webservicerequest;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class Farmer extends Fragment {
	
	String encryptedgeoid;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		final View vRoot = inflater.inflate(R.layout.farmer, container, false);
		
		Databaseutill db = Databaseutill.getDBAdapterInstance(getActivity());
		final GetData get = new GetData(getActivity(),db);
		final Spinner districts = (Spinner)vRoot.findViewById(R.id.districts);
		final Spinner mandis = (Spinner)vRoot.findViewById(R.id.mandis);
		final AutoCompleteTextView villages = (AutoCompleteTextView)vRoot.findViewById(R.id.autoCompletevillage);
		
		ArrayList<HashMap<String, String>> category1=new ArrayList<HashMap<String,String>>();
		HashMap<String, String> map1=new HashMap<String, String>();
			map1.put("1","");
			map1.put("2","Select District");
			category1.add(map1);
			
			try{
				ArrayList<HashMap<String, String>> dist = new GetData(getActivity(), db).getDistrict();								
				for (int i=0; i<dist.size(); i++) {
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("1",dist.get(i).get("Districtid"));
	 				map.put("2",dist.get(i).get("Districtname"));
	 				category1.add(map);		 				
				}
			}
			catch(Exception e){
				e.getMessage();
			}								
			
		Myspinner[] redemo1 = new Myspinner[category1.size()];
		for(int i=0; i<category1.size(); i++){
			redemo1[i] = new Myspinner(category1.get(i).get("2"), category1.get(i).get("1"), "");
		}
		ArrayAdapter<Myspinner> adapter1 = new ArrayAdapter<Myspinner>(getActivity(), android.R.layout.simple_spinner_dropdown_item, redemo1);
		districts.setAdapter(adapter1);
		
		final ArrayList<HashMap<String, String>> category2=new ArrayList<HashMap<String,String>>();
		HashMap<String, String> map2=new HashMap<String, String>();
			map2.put("1","");
			map2.put("2","Select Mandi");
			category2.add(map2);
			
			districts.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					if(position!=0)
					{
						Myspinner sp = (Myspinner)districts.getSelectedItem();
						try{
							encryptedgeoid = new Webservicerequest().Encrypt(sp.getValue());
						}
						catch(Exception e){
							e.getMessage();
						}
						category2.addAll(get.getMandi(encryptedgeoid));	
						Myspinner[] redemo2 = new Myspinner[category2.size()];
		 				for(int i=0; i<category2.size(); i++){
		 					redemo2[i] = new Myspinner(category2.get(i).get("2"), category2.get(i).get("1"), "");
		 				}
		 				ArrayAdapter<Myspinner> adapter2 = new ArrayAdapter<Myspinner>(getActivity(), android.R.layout.simple_spinner_dropdown_item, redemo2);
		 				mandis.setAdapter(adapter2);
		 				ArrayAdapter<String> adaptervillages = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, get.getVillageNames(encryptedgeoid));
						villages.setAdapter(adaptervillages);
						villages.setThreshold(3);
						villages.setOnItemClickListener(new OnItemClickListener() {

							  @Override
							  public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
							    InputMethodManager in = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
							    in.hideSoftInputFromWindow(villages.getWindowToken(), 0);
							  }

						});
					}
					else{
						villages.setAdapter(null);
						ArrayList<HashMap<String,String>> category2=new ArrayList<HashMap<String,String>>();
						HashMap<String, String> map2=new HashMap<String, String>();
		 				map2.put("1","");
		 				map2.put("2","Select Mandi");
		 				category2.add(map2);
						Myspinner[] redemo2 = new Myspinner[category2.size()];
		 				for(int i=0; i<category2.size(); i++){
		 					redemo2[i] = new Myspinner(category2.get(i).get("2"), category2.get(i).get("1"), "");
		 				}
		 				ArrayAdapter<Myspinner> adapter2 = new ArrayAdapter<Myspinner>(getActivity(), android.R.layout.simple_spinner_dropdown_item, redemo2);
		 				mandis.setAdapter(adapter2);
					}
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					// TODO Auto-generated method stub
					
				}
			});
				
		final Button ok = (Button)vRoot.findViewById(R.id.ok);
		
		ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(districts.getSelectedItemPosition()>0 && mandis.getSelectedItemPosition()>0 && villages.getText().toString().length()>0)
				{
					Myspinner sp = (Myspinner)mandis.getSelectedItem();
					new InsertVillage().execute(villages.getText().toString(),sp.getValue());
				LinearLayout top = (LinearLayout)vRoot.findViewById(R.id.top);
				LinearLayout bottom = (LinearLayout)vRoot.findViewById(R.id.bottom);
				top.setBackgroundColor(Color.parseColor("#e7f5d2"));
				districts.setEnabled(false);
				mandis.setEnabled(false);
				villages.setEnabled(false);
				ok.setEnabled(false);
				bottom.setBackgroundColor(Color.parseColor("#FFFFFF"));
				TextView teh = (TextView)vRoot.findViewById(R.id.teh);
				TextView vill = (TextView)vRoot.findViewById(R.id.vill);
				EditText fname = (EditText)vRoot.findViewById(R.id.fname);
				EditText mob = (EditText)vRoot.findViewById(R.id.mob);
				Button add = (Button)vRoot.findViewById(R.id.add);
				Button finish = (Button)vRoot.findViewById(R.id.finish);
				CheckBox demo = (CheckBox)vRoot.findViewById(R.id.demo);
				teh.setEnabled(true);
				vill.setEnabled(true);
				fname.setEnabled(true);
				mob.setEnabled(true);
				add.setEnabled(true);
				finish.setEnabled(true); 
				demo.setEnabled(true);
				teh.setText("Tehsil:\n"+sp.getSpinnerText());
				vill.setText("Village:\n"+villages.getText().toString());
				}
				else
				{
					if(mandis.getSelectedItemPosition()==0)
					{
						Toast.makeText(getActivity(), "Please Select Tehsil", Toast.LENGTH_LONG).show();
					}
					else
					{
						Toast.makeText(getActivity(), "Please Fill Village", Toast.LENGTH_LONG).show();
					}
				}
			}
		});
		
		Button add = (Button)vRoot.findViewById(R.id.add);
		add.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Myspinner sp = (Myspinner)mandis.getSelectedItem();
				Myspinner sp1 = (Myspinner)districts.getSelectedItem();
				EditText fname = (EditText)vRoot.findViewById(R.id.fname);
				EditText mob = (EditText)vRoot.findViewById(R.id.mob);
				CheckBox demo = (CheckBox)vRoot.findViewById(R.id.demo);
				if(fname.getText().toString().length()>0)
				{
					if(mob.getText().toString().length()==10)
					{
						String farm = fname.getText().toString();
						String mobi = mob.getText().toString();
						String demoi;
						if(demo.isChecked())
						{
							demoi="1";
						}
						else
						{
							demoi="0";
						}
						get.insertFarmer(farm, mobi, demoi, sp.getValue(), villages.getText().toString(), sp1.getValue());
						fname.setText("");
						mob.setText("");
					}
					else
					{
						Toast.makeText(getActivity(), "Enter Valid Mobile Number", Toast.LENGTH_SHORT).show();
					}
				}
				else
				{
					Toast.makeText(getActivity(), "Enter Valid Farmer Name", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		
		Button finish = (Button)vRoot.findViewById(R.id.finish);
		finish.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Myspinner sp = (Myspinner)mandis.getSelectedItem();
				Myspinner sp1 = (Myspinner)districts.getSelectedItem();
				EditText fname = (EditText)vRoot.findViewById(R.id.fname);
				EditText mob = (EditText)vRoot.findViewById(R.id.mob);
				String frm = "";
				String mobl = "";
				String dmo = "";
				String mandiid="";
				String villname = "";
				if(fname.getText().toString().length()>0 && mob.getText().toString().length()>0)
				{
					if(mob.getText().toString().length()==10)
					{
						CheckBox demo = (CheckBox)vRoot.findViewById(R.id.demo);
						String farm = fname.getText().toString();
						String mobi = mob.getText().toString();
						String demoi;
						if(demo.isChecked())
						{
							demoi="1";
						}
						else
						{
							demoi="0";
						}
						get.insertFarmer(farm, mobi, demoi, sp.getValue(), villages.getText().toString(), sp1.getValue());
						fname.setText("");
						mob.setText("");
						ArrayList<String> farmer = get.farmerName();
						ArrayList<String> mobile = get.mobile();
						ArrayList<String> demof = get.demo();
						ArrayList<String> mandi = get.mandi();
						ArrayList<String> villa = get.village();
						frm = farmer.toString();
						mobl = mobile.toString();
						dmo = demof.toString();
						mandiid = mandi.toString();
						villname = villa.toString();
						if(farmer.size()>0 && mobile.size()>0)
						{
							if(new ConnectionDetector(getActivity()).isConnectingToInternet())
							{
								new SubmitRetailer().execute(mandiid,villname,frm,mobl,dmo);
							}
							else
							{
								Toast.makeText(getActivity(), "Please connect to Internet", Toast.LENGTH_LONG).show();
							}
							farmer.clear();
							mobile.clear();
							demof.clear();
							mandi.clear();
							villa.clear();
						}
						else
						{
							Toast.makeText(getActivity(), "Please Enter Data", Toast.LENGTH_LONG).show();
						}
						fname.setText("");
						mob.setText("");
						LinearLayout top = (LinearLayout)vRoot.findViewById(R.id.top);
						LinearLayout bottom = (LinearLayout)vRoot.findViewById(R.id.bottom);
						top.setBackgroundColor(Color.parseColor("#FFFFFF"));
						districts.setEnabled(true);
						mandis.setEnabled(true);
						villages.setEnabled(true);
						ok.setEnabled(true);
						bottom.setBackgroundColor(Color.parseColor("#e7f5d2"));
						TextView teh = (TextView)vRoot.findViewById(R.id.teh);
						TextView vill = (TextView)vRoot.findViewById(R.id.vill);
						Button add = (Button)vRoot.findViewById(R.id.add);
						Button finish = (Button)vRoot.findViewById(R.id.finish);
						teh.setEnabled(false);
						vill.setEnabled(false);
						fname.setEnabled(false);
						mob.setEnabled(false);
						add.setEnabled(false);
						finish.setEnabled(false);
						demo.setEnabled(false);
					}
					else
					{
						Toast.makeText(getActivity(), "Enter Valid Mobile Number", Toast.LENGTH_SHORT).show();
					}
				}
				else
				{
					ArrayList<String> farmer = get.farmerName();
					ArrayList<String> mobile = get.mobile();
					ArrayList<String> demof = get.demo();
					ArrayList<String> mandi = get.mandi();
					ArrayList<String> villa = get.village();
					frm = farmer.toString();
					mobl = mobile.toString();
					dmo = demof.toString();
					mandiid = mandi.toString();
					villname = villa.toString();
					if(farmer.size()>0 && mobile.size()>0)
					{
						if(new ConnectionDetector(getActivity()).isConnectingToInternet())
						{
							new SubmitRetailer().execute(mandiid,villname,frm,mobl,dmo);
						}
						else
						{
							Toast.makeText(getActivity(), "Please connect to Internet", Toast.LENGTH_LONG).show();
						}
						farmer.clear();
						mobile.clear();
						demof.clear();
						mandi.clear();
						villa.clear();
					}
					else
					{
						Toast.makeText(getActivity(), "Please Enter Data", Toast.LENGTH_LONG).show();
					}
					fname.setText("");
					mob.setText("");
					LinearLayout top = (LinearLayout)vRoot.findViewById(R.id.top);
					LinearLayout bottom = (LinearLayout)vRoot.findViewById(R.id.bottom);
					top.setBackgroundColor(Color.parseColor("#FFFFFF"));
					districts.setEnabled(true);
					mandis.setEnabled(true);
					villages.setEnabled(true);
					ok.setEnabled(true);
					bottom.setBackgroundColor(Color.parseColor("#e7f5d2"));
					TextView teh = (TextView)vRoot.findViewById(R.id.teh);
					TextView vill = (TextView)vRoot.findViewById(R.id.vill);
					Button add = (Button)vRoot.findViewById(R.id.add);
					Button finish = (Button)vRoot.findViewById(R.id.finish);
					teh.setEnabled(false);
					vill.setEnabled(false);
					fname.setEnabled(false);
					mob.setEnabled(false);
					add.setEnabled(false);
					finish.setEnabled(false);				
				}
				
				
			}
		});
		
		return vRoot;
	}
	
	
	
	private int getIndex(Spinner spinner, String myString){

        int index = 0;
        for (int i=0;i<spinner.getCount();i++){
        	Myspinner sp = (Myspinner)spinner.getItemAtPosition(i);
        	try{
        		if (new Webservicerequest().Decrypt(sp.getValue()).equalsIgnoreCase(myString)){
                    index = i;
                }
        	}catch(Exception e)
        	{
        		e.getMessage();
        	}
            
        }
        return index;
}
	
	
	
	
	
	class SubmitRetailer extends AsyncTask<String, Void, String>
	{
		ProgressDialog pDialog;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			
			  pDialog = new ProgressDialog(getActivity());
				try{
					this.pDialog.setMessage("Please wait ...");
			        this.pDialog.setIndeterminateDrawable(getActivity().getResources().getDrawable(R.drawable.red_progress));
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
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			String str = params[0], str1 = params[1], str2= params[2], str3= params[3], str4 = params[4];
			ConnectionDetector cd=new ConnectionDetector(getActivity());
			Databaseutill db=Databaseutill.getDBAdapterInstance(getActivity());
			 if(cd.isConnectingToInternet())
			 {try
		      {
				 Webservicerequest wsc = new Webservicerequest();
				 try{
				 
				 ArrayList<String> inputlist1=new ArrayList<String>();
					inputlist1.add("tehsil");
					inputlist1.add(wsc.Encrypt(str));
					inputlist1.add("village");
					inputlist1.add(wsc.Encrypt(str1));
					inputlist1.add("farmer");
					inputlist1.add(wsc.Encrypt(str2));
					inputlist1.add("mobile");
					inputlist1.add(wsc.Encrypt(str3));
					inputlist1.add("demofarmer");
					inputlist1.add(wsc.Encrypt(str4));
		        str = wsc.MobileWebservice(getActivity(), "Create_farmer", inputlist1);
		        if(str.equalsIgnoreCase("Farmer created successfully")||
		        		str.contains("These Mobile Number already registered - ")){
		        	new GetData(getActivity(),db).dropfarmer();
		        }
		        
		      }
				 catch (Exception localException)
			      {
			        localException.getMessage();
			        str = "Error in Connection";
			      }
			 }
			 catch (Exception localException)
		      {
		        localException.getMessage();
		        str = "Error in Connection";
		      }
			 }
			 else
			 {
				 str = "No Network Found";
			 }
			 
			return str;
		}

		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
		try{	if(result.length()>0){
				Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
			}
		}
		catch(Exception e)
		{
			e.getMessage();
			Toast.makeText(getActivity(), "Some Error Occures", Toast.LENGTH_LONG).show();
		}
			pDialog.dismiss();
		}
	}
	
	
	
	class InsertVillage extends AsyncTask<String, Void, Void>
	{

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			Databaseutill db = Databaseutill.getDBAdapterInstance(getActivity());
			GetData get = new GetData(getActivity(), db);
			get.setVillage(params[0],params[0]);
			return null;
		}
		
	}
	
	
}
