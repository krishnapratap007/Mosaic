package aksha.mopwheat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import aksha.adapters.LazyAdapter;
import aksha.adapters.Myspinner;
import aksha.adapters.TableRowListAdapter;
import aksha.connectiondetector.ConnectionDetector;
import aksha.database.Databaseutill;
import aksha.database.GetData;
import com.aksha.mosaic.R;
import aksha.webservice.Webservicerequest;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class DemoFarmer extends Fragment {
	
	private String date = "";
	private final int SELECT_PHOTO = 100;
	private final int SELECT_VIDEO = 101;
	private final int SELECT_AUDIO = 102;
	private String picPath,audPath,vidPath;
	private int selection = 0;
	private Databaseutill db;
	private GetData get;
	private View root;
	private ImageView pic;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		root = inflater.inflate(R.layout.mopwheatdemofarmer, container, false);
		
		db = Databaseutill.getDBAdapterInstance(getActivity());
		get = new GetData(getActivity(),db);
		
		try{
			picPath = getActivity().getIntent().getStringExtra("path");
			File imgFile = new File(savedInstanceState.getString("picPath"));
			if(imgFile.exists()){
			    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
			    pic.setImageBitmap(myBitmap);
			    pic.setTag(imgFile.getAbsolutePath());
			}
		}
		catch(Exception e){
			e.getMessage();
		} 
		
		final Spinner mopwheatdistrictlist = (Spinner)root.findViewById(R.id.mopwheatdistrictlist);		
		
		ArrayList<HashMap<String, String>> category1=new ArrayList<HashMap<String,String>>();
		HashMap<String, String> map1=new HashMap<String, String>();
			map1.put("1","");
			map1.put("2","Select District");
			category1.add(map1);
			
		try{
			ArrayList<HashMap<String, String>> data = get.getAllLogin();
			if(new ConnectionDetector(getActivity()).isConnectingToInternet()){
				new Data().execute(data.get(0).get("geoid"));
			}
			else{
				Toast.makeText(getActivity(), "No Network Found", Toast.LENGTH_LONG).show();
			}
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
		mopwheatdistrictlist.setAdapter(adapter1);
		
		mopwheatdistrictlist.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if(position!=0){
					Myspinner sp = (Myspinner)mopwheatdistrictlist.getSelectedItem();
					if(new ConnectionDetector(getActivity()).isConnectingToInternet()){
						new Data().execute(sp.getValue());
					}
					else{
						Toast.makeText(getActivity(), "No Network Found", Toast.LENGTH_LONG).show();
					}
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
		
	/*	final ArrayList<String> str = new ArrayList<String>();
	    str.add("Village 1");
	    str.add("Farmer 1");
	    str.add("Mobile 1");
	    str.add("Village 2");
	    str.add("Farmer 2");
	    str.add("Mobile 2");
	    
	    final ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String,String>>();
	    for(int i=0; i<str.size(); i+=3){
	    	HashMap<String, String> map = new HashMap<String, String>();
	    	map.put("1", str.get(i));
	    	map.put("2", str.get(i+1));
	    	map.put("3", str.get(i+2));
	    	data.add(map);
	    }
	    
		TableRowListAdapter adapter = new TableRowListAdapter(getActivity(), data);
		mopwheatdemofarmerlist.setAdapter(adapter);
		
		mopwheatdemofarmerlist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				final View layout = inflater.inflate(R.layout.mopwgeatpopupclickingfarmer, null);
				
				Button uploadimage = (Button)layout.findViewById(R.id.uploadimage);
				uploadimage.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(Intent.ACTION_PICK);
						intent.setType("image/*");
					//	intent.setAction(Intent.ACTION_GET_CONTENT);
				    //	intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
						startActivityForResult(intent, SELECT_PHOTO);
						selection = SELECT_PHOTO;
					}
				});
								
				Button sowingdate = (Button)layout.findViewById(R.id.sowingdate);
				sowingdate.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						CalendarPopup((EditText)layout.findViewById(R.id.sowingdt));
					}
				});
				
				Button mopdate = (Button)layout.findViewById(R.id.mopdate);
				mopdate.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						CalendarPopup((EditText)layout.findViewById(R.id.mopdt));
					}
				});				
				
				Button irridate1 = (Button)layout.findViewById(R.id.irridate1);
				irridate1.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						CalendarPopup((EditText)layout.findViewById(R.id.irridt1));
					}
				});
				
				Button irridate2 = (Button)layout.findViewById(R.id.irridate2);
				irridate2.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						CalendarPopup((EditText)layout.findViewById(R.id.irridt2));
					}
				});
				
				AlertDialog.Builder build = new Builder(getActivity())
				.setTitle("Details")
				.setView(layout)
				.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
					}
				})
				.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.cancel();
					}
				});
				AlertDialog alert = build.create();
				alert.show();
			}
		});
		*/
		return root;
	}
	
	private String CalendarPopup(final EditText editText){
		final AlertDialog alert;
		date = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
		LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		FrameLayout layout= (FrameLayout)inflater.inflate(R.layout.calenderpopup, null);
		CalendarView cal = (CalendarView)layout.findViewById(R.id.calendarpopup);
		// sets whether to show the week number.
		cal.setShowWeekNumber(false);
		
		// sets the first day of week according to Calendar.
		// here we set Monday as the first day of the Calendar
		cal.setFirstDayOfWeek(2);
		
		/*//The background color for the selected week.
		cal.setSelectedWeekBackgroundColor(getResources().getColor(R.color.green));
			         
		//sets the color for the dates of an unfocused month.
		cal.setUnfocusedMonthDateColor(getResources().getColor(R.color.transparent));
		
		//sets the color for the separator line between weeks.
		cal.setWeekSeparatorLineColor(getResources().getColor(R.color.transparent));
			        
		//sets the color for the vertical bar shown at the beginning and at the end of the selected date.
		cal.setSelectedDateVerticalBar(R.color.darkgreen);*/
	        
	        cal.setOnDateChangeListener(new OnDateChangeListener() {
				
			@Override
			public void onSelectedDayChange(CalendarView view, int year, int month,
					int dayOfMonth) {
				// TODO Auto-generated method stub
				try{
					Date dt = new SimpleDateFormat("yyyy/M/d").parse(String.valueOf(year)+"/"+String.valueOf(month+1)+"/"+String.valueOf(dayOfMonth));
					date = new SimpleDateFormat("yyyy/MM/dd").format(dt);
				}
				catch(Exception e){
					e.getMessage();
				}
			}
		});
		AlertDialog.Builder build = new Builder(getActivity())
		.setTitle("Select Date")
		.setView(layout)
		.setPositiveButton("Select", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				editText.setText(date);
			}
		})
		.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.cancel();
			}
		});
		alert = build.create();
		alert.show();
		return date;
	}
	
	public String size(long size){
	    String hrSize = "";
	    double m = (size/1024.0)/1024.0;
	    DecimalFormat dec = new DecimalFormat("0.00");

	    if (m > 1.0) {
	        hrSize = dec.format(m).concat(" MB");
	    } else {
	        hrSize = dec.format(size/1024.0).concat(" KB");
	    }
	    return hrSize;
	}
	
	
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	try{
    		super.onActivityResult(requestCode, resultCode, data); 

            switch(selection) { 
            case SELECT_PHOTO:
                try {
    					final Uri imageUri = data.getData();
    					String path = getRealImagePathFromURI(imageUri);
    					path = getActivity().getIntent().getStringExtra("path");
    					File file = new File(path);
    	    			String[] size = size(file.length()).split(" ");
    	    			if(size[1].contains("MB")){
    	    				if(Double.parseDouble(size[0])<2.0)
    	        			{
    	    					final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
    	    					final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
    	    					pic.setImageBitmap(selectedImage);
    	    					pic.setTag(path);
    	    					picPath = path;
    	        			}
    	    				else{
    	    					Toast.makeText(getActivity(), "You selected "+size[0]+" "+size[1]+" file. Please select less than 2 Mb File.", Toast.LENGTH_LONG).show();
    	    				}
    	    			}
    	    			else{
    	    				final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
	    					final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
	    					pic.setImageBitmap(selectedImage);
	    					pic.setTag(path);
	    					picPath = path;
    	    			}    					
    					break;
    				} catch (FileNotFoundException e) {
    					Toast.makeText(getActivity(), "File Not Found", Toast.LENGTH_LONG).show();
    				}

                
    	case SELECT_VIDEO:
            try {
					Uri imageUri = data.getData();
					String path = getRealPathFromURI(imageUri);
					File file = new File(path);
	    			String[] size = size(file.length()).split(" ");
	    			if(size[1].contains("MB")){
	    				if(Double.parseDouble(size[0])<2.0)
	        			{
	    					final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
	    					final Bitmap selectedImage = ThumbnailUtils.createVideoThumbnail(path,MediaStore.Images.Thumbnails.FULL_SCREEN_KIND);
	    					/*vid.setImageBitmap(selectedImage);
	    					vid.setTag(path);*/
	    					vidPath = path;
	        			}
	    				else{
	    					Toast.makeText(getActivity(), "You selected "+size[0]+" "+size[1]+" file. Please select less than 2 Mb File.", Toast.LENGTH_LONG).show();
	    				}
	    			}
	    			else{
	    				final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
						final Bitmap selectedImage = ThumbnailUtils.createVideoThumbnail(path,MediaStore.Images.Thumbnails.FULL_SCREEN_KIND);
						/*vid.setImageBitmap(selectedImage);
						vid.setTag(path);*/
						vidPath = path;
	    			}				
					break;
				} catch (FileNotFoundException e) {
					Toast.makeText(getActivity(), "File Not Found", Toast.LENGTH_LONG).show();
				}
            
    	case SELECT_AUDIO:
    		try{
    			Uri imageUri = data.getData();
    			String path = getRealAudioPathFromURI(imageUri);
    			File file = new File(path);
    			String[] size = size(file.length()).split(" ");
    			if(size[1].contains("MB")){
    				if(Double.parseDouble(size[0])<2.0)
        			{
        				/*audio.setVisibility(View.VISIBLE);
        				audio.setTag(path);*/
        				audPath = path;
        			}
    				else{
    					Toast.makeText(getActivity(), "You selected "+size[0]+" "+size[1]+" file. Please select less than 2 Mb File.", Toast.LENGTH_LONG).show();
    				}
    			}
    			else{
    				/*audio.setVisibility(View.VISIBLE);
    				audio.setTag(path);*/
    				audPath = path;
    			}
    		}
    		catch (Exception e) {
    			Toast.makeText(getActivity(), "File Not Found", Toast.LENGTH_LONG).show();
			}

            }            
    	}
    	catch(Exception e){
    		Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    	}
    };
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
    	// TODO Auto-generated method stub
    	super.onSaveInstanceState(outState);
    	if(outState==null){
    		outState = new Bundle();
    	}
    	try{
    		outState.putInt("selection", selection);
    		outState.putString("picPath", picPath);
        	outState.putString("vidPath", vidPath);
        	outState.putString("audPath", audPath);
    	}
    	catch(Exception e){
    		e.getMessage();
    	}
    }
    
    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	super.onViewStateRestored(savedInstanceState);
    	try{
    		if(savedInstanceState!=null){
    			selection = savedInstanceState.getInt("selection");
    			try{
    				picPath = savedInstanceState.getString("picPath");
    			}
    			catch(Exception e){
    				e.getMessage();
    			}
    			try{
    				vidPath = savedInstanceState.getString("vidPath");
    			}
    			catch(Exception e){
    				e.getMessage();
    			}
    			try{
    				audPath = savedInstanceState.getString("audPath");
    			}
    			catch(Exception e){
    				e.getMessage();
    			}
    		}    		
    	}
    	catch(Exception e){
    		Log.e("TAG:", e.getLocalizedMessage());
    	}
    }
    
    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Video.Media.DATA };
        Cursor cursor = getActivity().managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    
    public String getRealImagePathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    
    public String getRealAudioPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Audio.Media.DATA };
        Cursor cursor = getActivity().managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    
    public String convertFileToString(String pathOnSdCard){
    	String strFile=null;
    	if(pathOnSdCard.length()>0){
    		File file=new File(pathOnSdCard);    	
    	try {
    		FileInputStream fileInputStream=null;
            byte[] bFile = new byte[(int) file.length()];
            fileInputStream = new FileInputStream(file);
    	    fileInputStream.read(bFile);
    	    fileInputStream.close();

    	strFile = Base64.encodeToString(bFile, Base64.NO_WRAP);//Convert byte array into string    	    
/*
    	File fle = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"myvideo.txt");
    	FileWriter wrt = new FileWriter(fle);
    	wrt.append(strFile);
    	wrt.flush();
    	wrt.close();*/
    	} catch (IOException e) {

    	e.printStackTrace();
    	strFile = "";
    	}    	
    	}
    	else
    	{
    		strFile="";
    	}
    	return strFile;

    }

    class Data extends AsyncTask<String, Void, ArrayList<String>>
	{
		ProgressBar prog;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			try{
			prog = (ProgressBar)root.findViewById(R.id.pbbar);
			prog.setVisibility(View.VISIBLE);
			}catch(Exception e)
			{
				e.getMessage();
			}
		}
		@Override
		protected ArrayList<String> doInBackground(String... params) {
			// TODO Auto-generated method stub
			ArrayList<String> returnval=new ArrayList<String>();
			ConnectionDetector cd=new ConnectionDetector(getActivity());
			if(cd.isConnectingToInternet())
			 {try
		      {
				      Webservicerequest wsc = new Webservicerequest();
				      ArrayList<String> inputlist=new ArrayList<String>();
				      inputlist.add("districtid");
				      inputlist.add(params[0]);
				      String str2 = wsc.MobileWebservice(getActivity(),"MOPWheatDemofarmerlist", inputlist);
				      ArrayList<String> listvalue=new ArrayList<String>();
				      if (str2 != null)
				      {	
				    	  listvalue.add("farmerid");
				    	  listvalue.add("name");
				    	  listvalue.add("mobile");
				    	  listvalue.add("location");
				        ArrayList<String> localArrayList3 = wsc.JSONEncoding(str2, listvalue);
				        returnval.clear();
				        for (String string : localArrayList3) {
							returnval.add(wsc.Decrypt(string));
						}
				      }
				      else{
				    	  returnval.add("No Data Found");
				      }
		        
		      }
			 catch (Exception localException)
		      {
		        localException.getMessage();
		        returnval.add("Error in Connection");
		      }
			 }
			 else
			 {
				 returnval.add("No Network Found");
			 }
		
			return returnval;
		}
		
		@Override
		protected void onPostExecute(ArrayList<String> result) {
			// TODO Auto-generated method stub
			try{
				prog.setVisibility(View.GONE);
				if(result!=null && result.size()>2){
					final ArrayList<String> str = result;
					final ListView mopwheatdemofarmerlist = (ListView)root.findViewById(R.id.mopwheatdemofarmerlist);
				    final ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String,String>>();
				    for(int i=0; i<str.size(); i+=4){
				    	HashMap<String, String> map = new HashMap<String, String>();
				    	map.put("1", str.get(i+1));
				    	map.put("2", str.get(i+3));
				    	map.put("3", str.get(i+2));
				    	map.put("4", str.get(i));
				    	data.add(map);
				    }
				    
					TableRowListAdapter adapter = new TableRowListAdapter(getActivity(), data);
					mopwheatdemofarmerlist.setAdapter(adapter);
					
					mopwheatdemofarmerlist.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent, View view,
								int position, long id) {
							// TODO Auto-generated method stub
							LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
							final View layout = inflater.inflate(R.layout.mopwgeatpopupclickingfarmer, null);
							pic = (ImageView)layout.findViewById(R.id.farmerprofileimage);
							
							Button uploadimage = (Button)layout.findViewById(R.id.uploadimage);
							uploadimage.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									Intent intent = new Intent(Intent.ACTION_PICK);
									intent.setType("image/*");
								//	intent.setAction(Intent.ACTION_GET_CONTENT);
							    //	intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
									startActivityForResult(intent, SELECT_PHOTO);
									selection = SELECT_PHOTO;
								}
							});
											
							Button sowingdate = (Button)layout.findViewById(R.id.sowingdate);
							sowingdate.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									CalendarPopup((EditText)layout.findViewById(R.id.sowingdt));
								}
							});
							
							Button mopdate = (Button)layout.findViewById(R.id.mopdate);
							mopdate.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									CalendarPopup((EditText)layout.findViewById(R.id.mopdt));
								}
							});				
							
							Button irridate1 = (Button)layout.findViewById(R.id.irridate1);
							irridate1.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									CalendarPopup((EditText)layout.findViewById(R.id.irridt1));
								}
							});
							
							Button irridate2 = (Button)layout.findViewById(R.id.irridate2);
							irridate2.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									CalendarPopup((EditText)layout.findViewById(R.id.irridt2));
								}
							});
							
							AlertDialog.Builder build = new Builder(getActivity())
							.setTitle("Details")
							.setView(layout)
							.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									
								}
							})
							.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									dialog.cancel();
								}
							});
							AlertDialog alert = build.create();
							alert.show();
						}
					});
					
					
				}
				else {
					final ListView mopwheatdemofarmerlist = (ListView)root.findViewById(R.id.mopwheatdemofarmerlist);
					ArrayList<String> str = new ArrayList<String>();
		    		 str.add("No Data Found");
		    		 LazyAdapter adapter = new LazyAdapter(getActivity(), str);		    		 
					mopwheatdemofarmerlist.setAdapter(adapter);
					Toast.makeText(getActivity(), result.get(0), Toast.LENGTH_SHORT).show();
				}
			}
			catch(Exception e)
			{
				final ListView mopwheatdemofarmerlist = (ListView)root.findViewById(R.id.mopwheatdemofarmerlist);
				ArrayList<String> str = new ArrayList<String>();
	    		 str.add("No Data Found");
	    		 LazyAdapter adapter = new LazyAdapter(getActivity(), str);		    		 
				mopwheatdemofarmerlist.setAdapter(adapter);
				e.getMessage();				
			}			
		}
	}
}
