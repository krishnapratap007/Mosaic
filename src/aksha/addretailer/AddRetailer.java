package aksha.addretailer;

import java.util.ArrayList;
import java.util.HashMap;

import aksha.adapters.Myspinner;
import aksha.connectiondetector.ConnectionDetector;
import aksha.database.CreateTable;
import aksha.database.Databaseutill;
import aksha.database.GetData;
import com.aksha.mosaic.R;
import aksha.webservice.Webservicerequest;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class AddRetailer extends Fragment {
	
	private Spinner distlist;
	private Spinner mandilist;
	private Spinner currentretailerlist;
	private EditText mandiname;
	private EditText retailername;
	private EditText mobilenumber;
	private Button addretailer;
	private GetData get;
	private Databaseutill db;
	private String encryptedgeoid;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View root = inflater.inflate(R.layout.addretailer, container, false);
		
		db = Databaseutill.getDBAdapterInstance(getActivity());
		get = new GetData(getActivity(), db);
		
		distlist = (Spinner)root.findViewById(R.id.distlist);
		mandilist = (Spinner)root.findViewById(R.id.mandilist);
		currentretailerlist = (Spinner)root.findViewById(R.id.currentretailerlist);
		mandiname = (EditText)root.findViewById(R.id.mandiname);
		retailername = (EditText)root.findViewById(R.id.retailername);
		mobilenumber = (EditText)root.findViewById(R.id.mobilenumber);
		addretailer = (Button)root.findViewById(R.id.addretailer);
		
		ArrayList<HashMap<String, String>> category1=new ArrayList<HashMap<String,String>>();
		HashMap<String, String> map1=new HashMap<String, String>();
			map1.put("1","");
			map1.put("2","Select District");
			category1.add(map1);
			
			try{
				ArrayList<HashMap<String, String>> data = get.getAllLogin();
				String[] district = data.get(0).get("district").split(",");
				String[] geoid = data.get(0).get("geoid").split(",");					
				for (int i=0; i<district.length; i++) {
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("1",geoid[i]);
	 				map.put("2",district[i]);
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
		ArrayAdapter<Myspinner> adapter1 = new ArrayAdapter<Myspinner>(getActivity(), android.R.layout.simple_spinner_item, redemo1);
		distlist.setAdapter(adapter1);
		
		final ArrayList<HashMap<String, String>> category2=new ArrayList<HashMap<String,String>>();
		HashMap<String, String> map2=new HashMap<String, String>();
			map2.put("1","");
			map2.put("2","Select Mandi");
			category2.add(map2);
			
			distlist.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					if(position!=0)
					{
						Myspinner sp = (Myspinner)distlist.getSelectedItem();
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
		 				ArrayAdapter<Myspinner> adapter2 = new ArrayAdapter<Myspinner>(getActivity(), android.R.layout.simple_spinner_item, redemo2);
		 				mandilist.setAdapter(adapter2);
					}
					else{
						ArrayList<HashMap<String,String>> category2=new ArrayList<HashMap<String,String>>();
						HashMap<String, String> map2=new HashMap<String, String>();
		 				map2.put("1","");
		 				map2.put("2","Select Mandi");
		 				category2.add(map2);
						Myspinner[] redemo2 = new Myspinner[category2.size()];
		 				for(int i=0; i<category2.size(); i++){
		 					redemo2[i] = new Myspinner(category2.get(i).get("2"), category2.get(i).get("1"), "");
		 				}
		 				ArrayAdapter<Myspinner> adapter2 = new ArrayAdapter<Myspinner>(getActivity(), android.R.layout.simple_spinner_item, redemo2);
		 				mandilist.setAdapter(adapter2);
					}
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					// TODO Auto-generated method stub
					
				}
			});
			
		final ArrayList<HashMap<String, String>> category3=new ArrayList<HashMap<String,String>>();
		HashMap<String, String> map3=new HashMap<String, String>();
			map3.put("1","");
			map3.put("2","Retailer List");
			category3.add(map3);
			
		Myspinner[] redemo2 = new Myspinner[category2.size()];
		for(int i=0; i<category2.size(); i++){
			redemo2[i] = new Myspinner(category2.get(i).get("2"), category2.get(i).get("1"), "");
		}
		ArrayAdapter<Myspinner> adapter2 = new ArrayAdapter<Myspinner>(getActivity(), android.R.layout.simple_spinner_item, redemo2);
		mandilist.setAdapter(adapter2);
		mandilist.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if(mandilist.getSelectedItemPosition()!=0){
					Myspinner mandiid=(Myspinner)mandilist.getSelectedItem();
					category3.clear();
					HashMap<String, String> hash1 = new HashMap<String, String>();
					hash1.put("1", "");
					hash1.put("2", "Retailer List");
					category3.add(hash1);
					category3.addAll(get.getretailersList(mandiid.getValue()));
					Myspinner[] redemo1 = new Myspinner[category3.size()];
					for(int i=0; i<category3.size(); i++)
					{redemo1[i] = new Myspinner(category3.get(i).get("2"), category3.get(i).get("1"), "");}
					ArrayAdapter<Myspinner> adp1=new ArrayAdapter<Myspinner>(getActivity(),android.R.layout.simple_spinner_dropdown_item, redemo1);
					currentretailerlist.setAdapter(adp1);
				}
				else{
					category3.clear();
					HashMap<String, String> hash1 = new HashMap<String, String>();
					hash1.put("1", "");
					hash1.put("2", "Retailer List");
					category3.add(hash1);
					Myspinner[] redemo1 = new Myspinner[category3.size()];
					for(int i=0; i<category3.size(); i++)
					{redemo1[i] = new Myspinner(category3.get(i).get("2"), category3.get(i).get("1"), "");}
					ArrayAdapter<Myspinner> adp1=new ArrayAdapter<Myspinner>(getActivity(),android.R.layout.simple_spinner_dropdown_item, redemo1);
					currentretailerlist.setAdapter(adp1);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});					
			
		Myspinner[] redemo3 = new Myspinner[category3.size()];
		for(int i=0; i<category3.size(); i++){
			redemo3[i] = new Myspinner(category3.get(i).get("2"), category3.get(i).get("1"), "");
		}
		ArrayAdapter<Myspinner> adapter3 = new ArrayAdapter<Myspinner>(getActivity(), android.R.layout.simple_spinner_item, redemo3);
		currentretailerlist.setAdapter(adapter3);
		
		addretailer.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				Toast.makeText(getActivity(), "Add new Retailer", Toast.LENGTH_SHORT).show();
				return true;
			}
		});
		
		addretailer.setOnClickListener(new  OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mandilist.getSelectedItemPosition()!=0)
				{
					if(mandiname.getText().toString().length()!=0){
						Myspinner mandiid=(Myspinner)mandilist.getSelectedItem();
						Myspinner sp = (Myspinner)distlist.getSelectedItem();
						if(mobilenumber.getText().toString().length()>0){
							new SendData().execute(mandiid.getValue(),mandiname.getText().toString(),retailername.getText().toString(),mobilenumber.getText().toString(),sp.getValue());
						}
						else{
							new SendData().execute(mandiid.getValue(),mandiname.getText().toString(),retailername.getText().toString(),"0",sp.getValue());
						}
					}
					else{
						mandiname.setError("Please Enter FirmName");
						retailername.addTextChangedListener(new TextWatcher() {
							
							@Override
							public void onTextChanged(CharSequence s, int start, int before, int count) {
								// TODO Auto-generated method stub
								if(mandiname.getText().toString().length()==0){
									retailername.setError("Please Enter FirmName");
								}
								else{
									mandiname.setError(null);
								}
							}

							@Override
							public void beforeTextChanged(CharSequence s,
									int start, int count, int after) {
								// TODO Auto-generated method stub
								
							}

							@Override
							public void afterTextChanged(Editable s) {
								// TODO Auto-generated method stub
								
							}
						});
					}
				}
				else{
					Toast.makeText(getActivity(), "Please Select Mandi", Toast.LENGTH_LONG).show();
				}
				mandilist.setSelection(0);
				mandiname.setText("");
				retailername.setText("");
				mobilenumber.setText("");
			
			}
		});
		
		return root;
	}
	
	
	private class SendData extends AsyncTask<String, Void, String>{

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
			get = new GetData(getActivity(), db);
			String result="";
			String empId = get.getEmpid().get(0);
			if(new ConnectionDetector(getActivity()).isConnectingToInternet()){
				ArrayList<String> inputlist = new ArrayList<String>();
				try{
					Webservicerequest wsc = new Webservicerequest();
					inputlist.add("tablename");
					inputlist.add(wsc.Encrypt("ms_chan"));
					inputlist.add("selectionarg");
					inputlist.add(wsc.Encrypt("chantype,firm_name,Own_name,mobile,DistrictID,MandiID,faid,crtdate"));
					inputlist.add("condition");
					inputlist.add(wsc.Encrypt("(3,'"+params[1]+"','"+params[2]+"','"+params[3]+"',"+params[4]+",'"+params[0]+"','"+empId+"',NOW())"));
					inputlist.add("type");
					inputlist.add(wsc.Encrypt("2"));
					String str = wsc.MobileWebservice(getActivity(),"TableDataSfe", inputlist);
					inputlist.clear();
					inputlist.add("Message");
					result = wsc.Decrypt(wsc.JSONEncoding(str, inputlist).get(0));
					if(result.equalsIgnoreCase("1")){
						ArrayList<String> geoid = new GetData(getActivity(),db).getDistrictList();
						  db.openDataBase();
						//  new CreateTable().createMasterTablen("TableDataSfe", "ms_chan", "chanid,firm_name,state,district,StateID,DistrictID,faid,mobile,MandiID", "where DistrictID in ("+geoid.get(0)+")", 9, false, false, false, "", db, getActivity());
						  db.close();
					}
				}
				catch(Exception e){
					e.getMessage();
				}
			}
			else{
				result = "No Network Found";
			}
			return result;
		}
		
		@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				pDialog.dismiss();
			try{	if(result!=null && result.length()>0){
					if(result.equalsIgnoreCase("1")){
						Toast.makeText(getActivity(), "Successfully Submitted", Toast.LENGTH_LONG).show();
					}
					else{
						if(result.equalsIgnoreCase("0")){
							Toast.makeText(getActivity(), "Please Try Again", Toast.LENGTH_LONG).show();
						}
						else{
							Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
						}
					}
				}
				else{
					Toast.makeText(getActivity(), "Some Error Occured", Toast.LENGTH_LONG).show();
				}
			}catch(Exception e){}
			}
		
	}
	

}
