package aksha.database;

import aksha.adapters.Myspinner;
import aksha.webservice.Webservicerequest;
import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
public class GetData
{
  Databaseutill db;
  Webservicerequest encrypt;
  Context context;
  
  public GetData(Context context,Databaseutill paramDatabaseutill)
  {
	this.context = context;
    this.db = paramDatabaseutill;
    this.encrypt = new Webservicerequest();
  }
  
  
  
	public ArrayList<HashMap<String, String>> getCalendarActivities(String fromdate, String todate, String empId, String district)
	{
	  ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
		String retval="0";
		String[] list = {"plan_date","actplanid","actid","jobstatus","firm_id","firm_name","jobid","jobdesc","reamarks","actplanid1","mandi","villname","districtname","planstatus"};
		try{
			Webservicerequest wsc=new Webservicerequest();
			ArrayList<String> inputlist=new ArrayList<String>();
			inputlist.add("fromdate");
			inputlist.add(fromdate);
			inputlist.add("todate");
			inputlist.add(todate);
			inputlist.add("mid");			
			inputlist.add(fromdate.split("-")[1]);
			inputlist.add("faid");
			inputlist.add(empId);
			inputlist.add("districtid");
			inputlist.add(district);
			retval=wsc.MobileWebservice(context,"Calenderfa",inputlist);
			inputlist.clear();			
			if(retval!=null)
			{
				ArrayList<String> listvalue=new ArrayList<String>();
				for (String string : list) {
					listvalue.add(string);
				}
				inputlist= wsc.JSONEncoding(retval, listvalue);
				int size = list.length;
				for(int count=0; count<inputlist.size(); count+=size)
				{
					HashMap<String, String> map = new HashMap<String, String>();
					for(int i=0; i<size; i++) {
						map.put(list[i], encrypt.Decrypt(inputlist.get(count+i)));
					}					
					data.add(map);
				}
			}			
		}catch(Exception e)
		{
			e.getMessage();
		}
		return data;
	}
	public ArrayList<String> getVillageNames(String mandiid){
		ArrayList<String> result = new ArrayList<String>();
			try
			{
				db.openDataBase();
				String query = "select villname from villages where mandiid = '"+mandiid+"'";
				 Cursor cur=db.selectData(query);
				 
				 if(cur!=null){
					 
				 cur.moveToFirst();
				  
				 for(int iCount=0;iCount<cur.getCount();iCount++)
				 {
					 for(int colCount=0;colCount<cur.getColumnCount();colCount+=1)
					 {
		 					result.add(encrypt.Decrypt(cur.getString(colCount)));
					 }
					cur.moveToNext();
				 }
				 cur.close();				 
			}
				db.close();
			}
			catch(Exception e)
			{
				e.getMessage();
			}
			return result;
	}
	
	public ArrayList<String> getVillageId(String villname){
		ArrayList<String> result = new ArrayList<String>();
			try
			{
				db.openDataBase();
				String query = "select villid from villages where villname = '"+encrypt.Encrypt(villname)+"'";
				 Cursor cur=db.selectData(query);
				 
				 if(cur!=null){
					 
				 cur.moveToFirst();
				  
				 for(int iCount=0;iCount<cur.getCount();iCount++)
				 {
					 for(int colCount=0;colCount<cur.getColumnCount();colCount+=1)
					 {
		 					result.add(encrypt.Decrypt(cur.getString(colCount)));
					 }
					cur.moveToNext();
				 }
				 cur.close();				 
			}
				db.close();
			}
			catch(Exception e)
			{
				e.getMessage();
			}
			return result;
	}
	
	
	
	// get crops
	
	
	
	
	
	
	
	
	public  ArrayList<HashMap<String, String>>  getcrops()
	{
		 ArrayList<HashMap<String, String>> returnval =new ArrayList<HashMap<String, String>>();
			try{
			db.openDataBase();
			//read from local tables
			 String query= "select cropid,cropname from crops";
			 Cursor cur=db.selectData(query);
			 
			 if(cur!=null){
				 
			 cur.moveToFirst();
			   
			 for(int iCount=0;iCount<cur.getCount();iCount++)
			 {
				 for(int colCount=0;colCount<cur.getColumnCount();colCount+=2)
				 {
					 HashMap<String, String> hash = new HashMap<String, String>();
					 
					 
					 hash.put("cropid",encrypt.Decrypt( cur.getString(colCount)));
					 hash.put("cropname", encrypt.Decrypt(cur.getString(colCount+1)));
					 returnval.add(hash);
				 }
				cur.moveToNext();
			 }
			 cur.close();
		}db.close();}catch (Exception e) {
			// TODO: handle exception
			e.getMessage();
		}
		return returnval;
	}
	
	// end crops
	
	
	
	
	
	
  
  public  ArrayList<HashMap<String, String>>  getDistrict()
 	{
 		 ArrayList<HashMap<String, String>> returnval =new ArrayList<HashMap<String, String>>();
 			try{
 			db.openDataBase();
 			//read from local tables
 			 String query="select Districtid,Districtname from ms_emp_geo_map";
 			 Cursor cur=db.selectData(query);
 			 
 			 if(cur!=null){
 				 
 			 cur.moveToFirst();
 			   
 			 for(int iCount=0;iCount<cur.getCount();iCount++)
 			 {
 				 for(int colCount=0;colCount<cur.getColumnCount();colCount+=6)
 				 {
 					 HashMap<String, String> hash = new HashMap<String, String>();
 					 hash.put("Districtid", encrypt.Decrypt(cur.getString(colCount)));
 					 hash.put("Districtname", encrypt.Decrypt(cur.getString(colCount+1)));
 					 returnval.add(hash);
 				 }
 				cur.moveToNext();
 			 }
 			 cur.close();
 		}db.close();}catch (Exception e) {
 			// TODO: handle exception
 			e.getMessage();
 		}
 		return returnval;
 	}
  
  
  public int calculateInSampleSize(BitmapFactory.Options options,
	        int reqWidth, int reqHeight) {
	    final int height = options.outHeight;
	    final int width = options.outWidth;
	    int inSampleSize = 1;

	    if (height > reqHeight || width > reqWidth) {
	    	
	        if (width > height) {
	         inSampleSize = Math.round((float)height / (float)reqHeight);    
	        } else {
	         inSampleSize = Math.round((float)width / (float)reqWidth);    
	        }
	        
	    	
	       }
	    return inSampleSize;
	    }
  
	
 public  ArrayList<String>  getEmpid()
	{
		 ArrayList<String> returnval =new ArrayList<String>();
			try{
			db.openDataBase();
			//read from local tables
			 String query="select empid from login";
			 Cursor cur=db.selectData(query);
			 
			 if(cur!=null){
				 
			 cur.moveToFirst();
			   
			 for(int iCount=0;iCount<cur.getCount();iCount++)
			 {
				 for(int colCount=0;colCount<cur.getColumnCount();colCount++)
				 {
					 returnval.add(cur.getString(colCount));
				 }
				cur.moveToNext();
			 }
			 cur.close();
		}
			 db.close(); 
			}catch (Exception e) {
			// TODO: handle exception
			e.getMessage();
		}
		return returnval;
	}
 

 
 public  ArrayList<String>  getPasscode()
	{
		 ArrayList<String> returnval =new ArrayList<String>();
			try{
			db.openDataBase();
			//read from local tables
			 String query="select passcode,role from login";
			 Cursor cur=db.selectData(query);
			 
			 if(cur!=null){
				 
			 cur.moveToFirst();
			   
			 for(int iCount=0;iCount<cur.getCount();iCount++)
			 {
				 for(int colCount=0;colCount<cur.getColumnCount();colCount++)
				 {
					 if(cur.getColumnName(colCount).equalsIgnoreCase("id"))
					 {returnval.add((cur.getString(colCount)));}
					 
					 else{
						 try{
					 returnval.add(encrypt.Decrypt(cur.getString(colCount)));
						 }catch(Exception er){
							 er.getMessage();
							 returnval.add((cur.getString(colCount)));
						 }
					 }
						 
					 
					 }
				cur.moveToNext();
			 }
			 cur.close();
		}
			 db.close(); 
			}catch (Exception e) {
			// TODO: handle exception
			e.getMessage();
		}
		return returnval;
	}


 public  String  getVersion()
	{
		 String returnval ="0.0";
			try{
			db.openDataBase();
			//read from local tables
			 String query="select id from moup";
			 Cursor cur=db.selectData(query);
			 
			 if(cur!=null){
				 
			 cur.moveToFirst();
			   
			 for(int iCount=0;iCount<cur.getCount();iCount++)
			 {
				 for(int colCount=0;colCount<cur.getColumnCount();colCount++)
				 {
					 
					 
					 
						 
					 returnval=((cur.getString(colCount)));
						 
					 
						 
					 
					 }
				cur.moveToNext();
			 }
			 cur.close();
		}
			 db.close(); 
			}catch (Exception e) {
			// TODO: handle exception
			e.getMessage();
			returnval="0.0";
		}
		return returnval;
	}

 
 	public void logout()
	{
		try{
			db.openDataBase();
			//read from local tables
			String query="drop table login";
		
			 db.execQuery(query);
			 db.close();
			}
			catch(Exception e)
			{
				e.getMessage();
			}
	}
  
	
 public void setLogin(String empid, String empname, String district, String passcode, String geoid, String moid, String password,String roleid)
	{
			try{
			db.openDataBase();
			//read from local tables
			
			String query1 = "create table login (empid varchar(50), empname varchar(50), district varchar(50), passcode varchar(50), geoid varchar(50), moid varchar(50), password varchar(50), role TEXT)";
			
			 db.execQuery(query1);
			 String query="insert into login (empid,empname,district,passcode,geoid,moid,password,role) values ('"+empid+"','"+empname+"','"+district+"','"+passcode+"','"+geoid+"','"+moid+"','"+password+"','"+roleid+"')";
		
			 db.execQuery(query);
			 db.close();
			}
			catch(Exception e)
			{
				e.getMessage();
			}
			
	}

 
 public  ArrayList<HashMap<String, String>>  getfadistrict(String faid)
	{
		 ArrayList<HashMap<String, String>> returnval =new ArrayList<HashMap<String, String>>();
			try{
			db.openDataBase();
			//read from local tables
			 String query="select districtid,districtname from ms_emp_geo_map where empid='"+encrypt.Encrypt(faid)+"'";
			 Cursor cur=db.selectData(query);
			 
			 if(cur!=null){
				 
			 cur.moveToFirst();
			   
			 for(int iCount=0;iCount<cur.getCount();iCount++)
			 {
				 for(int colCount=0;colCount<cur.getColumnCount();colCount+=2)
				 {
					 HashMap<String, String> hash = new HashMap<String, String>();
					 
					 
					 hash.put("Districtid",encrypt.Decrypt( cur.getString(colCount)));
					 hash.put("Districtname", encrypt.Decrypt(cur.getString(colCount+1)));
					 returnval.add(hash);
				 }
				cur.moveToNext();
			 }
			 cur.close();
		}db.close();}catch (Exception e) {
			// TODO: handle exception
			e.getMessage();
		}
		return returnval;
	}

 
 
 public  ArrayList<HashMap<String, String>>  getAllLogin()
	{
		 ArrayList<HashMap<String, String>> returnval =new ArrayList<HashMap<String, String>>();
			try{
			db.openDataBase();
			//read from local tables
			 String query="select empid,empname,district,geoid,moid,password,role from login";
			 Cursor cur=db.selectData(query);
			 
			 if(cur!=null){
				 
			 cur.moveToFirst();
			   
			 for(int iCount=0;iCount<cur.getCount();iCount++)
			 {
				 for(int colCount=0;colCount<cur.getColumnCount();colCount+=7)
				 {
					 HashMap<String, String> hash = new HashMap<String, String>();
					 hash.put("empid", cur.getString(colCount));
					 hash.put("empname", cur.getString(colCount+1));
					 hash.put("district", cur.getString(colCount+2));
					 hash.put("geoid", cur.getString(colCount+3));
					 hash.put("moid", cur.getString(colCount+4));
					 hash.put("password", cur.getString(colCount+5));
					 hash.put("role", cur.getString(colCount+6));
					 returnval.add(hash);
				 }
				cur.moveToNext();
			 }
			 cur.close();
		}db.close();}catch (Exception e) {
			// TODO: handle exception
			e.getMessage();
		}
		return returnval;
	}
 
 
 
	public ArrayList<Object> readtbl(String id)
	{ 
		ArrayList<Object> retval=new ArrayList<Object>();
		
		try{
			
			id=encrypt.Encrypt(id);
		db.openDataBase();
		//read from local tables
		 ArrayList<String> returnval =new ArrayList<String>();
		 ArrayList<String> returnval1=new ArrayList<String>();
		 ArrayList<String> returnval2=new ArrayList<String>();
		 ArrayList<String> returnval3=new ArrayList<String>();
		 ArrayList<String> returnval4=new ArrayList<String>();
		 ArrayList<String> returnval5=new ArrayList<String>();
		 ArrayList<String> returnval6=new ArrayList<String>();
		 ArrayList<String> returnval7=new ArrayList<String>();
		// String query="select elements from modx_site_tmplvars  where  name='ProductType'";
		String query="select A.LevelOptionID, B.LevelName ,A.LevelOptionName, A.Image1,A.Image2,A.Image3,A.Image4,A.Image5,B.Description from leveloptionmaster as A join diagnosislevel as B on B.LevelID=A.DiagnosisLevelID where B.LevelID='"+id+"'";
		 Cursor cur=db.selectData(query);
		 
		 if(cur!=null){
			 
		 cur.moveToFirst();
		  
		 for(int iCount=0;iCount<cur.getCount();iCount++)
		 {
			 for(int colCount=0;colCount<cur.getColumnCount();colCount++)
			 {
				 if(cur.getColumnName(colCount).equalsIgnoreCase("LevelName")){
				 returnval.add(encrypt.Decrypt(cur.getString(colCount)));}
				 else if(cur.getColumnName(colCount).equalsIgnoreCase("Image1")){
					 returnval2.add(encrypt.Decrypt(cur.getString(colCount).replace("assets/img/", "")));}
				 else if(cur.getColumnName(colCount).equalsIgnoreCase("Image2")){
					 returnval3.add(encrypt.Decrypt(cur.getString(colCount).replace("assets/img/", "")));}
				 else if(cur.getColumnName(colCount).equalsIgnoreCase("Image3")){
					 returnval4.add(encrypt.Decrypt(cur.getString(colCount).replace("assets/img/", "")));}
				 else if(cur.getColumnName(colCount).equalsIgnoreCase("Image4")){
					 returnval5.add(encrypt.Decrypt(cur.getString(colCount).replace("assets/img/", "")));}
				 else if(cur.getColumnName(colCount).equalsIgnoreCase("Image5")){
					 returnval6.add(encrypt.Decrypt(cur.getString(colCount).replace("assets/img/", "")));}
				 else if(cur.getColumnName(colCount).equalsIgnoreCase("LevelOptionID")){
					 returnval1.add(encrypt.Decrypt(cur.getString(colCount)));}
				 else if(cur.getColumnName(colCount).equalsIgnoreCase("LevelOptionName")){
					 returnval1.add(encrypt.Decrypt(cur.getString(colCount)));}
				 
				 else if(cur.getColumnName(colCount).equalsIgnoreCase("Description")){
					 returnval7.add(encrypt.Decrypt(cur.getString(colCount)));}
				 
				 }
			cur.moveToNext();
		 }
	retval.add(returnval);
	retval.add(returnval1);
	retval.add(returnval2);
	retval.add(returnval3);
	retval.add(returnval4);
	retval.add(returnval5);
	retval.add(returnval6);
	retval.add(returnval7);
	 cur.close();
	}
		
		 db.close();
		 }catch (Exception e) {
		// TODO: handle exception
	}
	return retval;
	}
 
	
	
	public ArrayList<HashMap<String, String>> getMandi(String districtid) {
		// TODO Auto-generated method stub
		ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String,String>>();
		try
		{
			db.openDataBase();
			// 2 state, 3 tehsil, 4 village
	//		String query = "select geoid,geoname from ms_geo where markid = '"+encrypt.Encrypt("9")+"' AND (par_geo = '"+districtid+"' OR zoneid = '"+districtid+"')";
			String query = "select mandiid,mandiname from ms_geo where districtid = '"+districtid+"'";
			 Cursor cur=db.selectData(query);
			 if(cur!=null){
				 
			 cur.moveToFirst();
			  
			 for(int iCount=0;iCount<cur.getCount();iCount++)
			 {
				 for(int colCount=0;colCount<cur.getColumnCount();colCount+=2)
				 {
					HashMap<String, String> map=new HashMap<String, String>();
					map.put("1",encrypt.Decrypt(cur.getString(colCount)));
					map.put("2",encrypt.Decrypt(cur.getString(colCount+1)));
					result.add(map);
				 }
				cur.moveToNext();
			 }
			 cur.close();
			 Collections.sort(result, new MapComparator("2"));
		}
			db.close();
		}
		catch(Exception e)
		{
			e.getMessage();
		}
		return result;
	}
	
	
	
	
	
	
	// get graph data
	
	
	
	
	/*
	
	
	public ArrayList<HashMap<String, String>> previousmonthactivity(String empId, String mo){
        
		ArrayList<String> returnlist =new ArrayList<String>();
//         ArrayList<HashMap<String, String>> returnlisti=new ArrayList<HashMap<String,String>>();
        
//        ArrayList<String> v= getCustomerCode();

        //select ytmpy,ytmcyp,ytmcya from pm_sales_perf where fy_year='2014' and salesgroup='102'
        try{
                
        db.openDataBase();
      //  String v= new  Webservicerequest().Decrypt(getCustomerCodelocal().get(0));
                       String selectionarg = "SUM(YTMPY),SUM(YTMCYP),SUM(YTMCYA)" ;
        
                        ArrayList<String> inputlist=new ArrayList<String>();
                        ArrayList<String> returnval=new ArrayList<String>();
                        inputlist.add("tablename");
                        inputlist.add(wsc.Encrypt("pm_sales_perf"));
                        inputlist.add("selectionarg");
                        inputlist.add(wsc.Encrypt(selectionarg));
                        inputlist.add("condition");
                        inputlist.add(wsc.Encrypt("where fy_year='2014' and salesgroup='"+v+"' and Type='2'"));
                        inputlist.add("type");
                        inputlist.add(wsc.Encrypt("0"));
                        
                        Webservicerequest wsc=new Webservicerequest();
            			ArrayList<String> inputlist=new ArrayList<String>();
            			inputlist.add("THid");
            			inputlist.add(empId);
            			inputlist.add("mid");
            			inputlist.add(mo);
            			String resultdata=wsc.MobileWebservice(context,"previousmonthactivity",inputlist);
            			inputlist.clear();
                        
                        //tablename, i0wq8OOY06CT/OHM84J6RQ==, selectionarg, 3d7sYchUfyTx+9kuJQyhkzI0gqHQEe2yKwPm0nQELgk=,
                        //condition, NjEsWqqv+BiUDdaIrdBjSzmFVhFmxs+SOyPRxkZEgSZjfnwZFIN+3DVxxy6tMbDE, type, dKsrs3USnzvTuASpwVI6Mw==
                        ArrayList<String> listvalue=new ArrayList<String>();
                        if(resultdata!=null){
                                String [] colval=selectionarg.split(",");
                
                                for(int strCount=0;strCount<colval.length;strCount++)
                                {
                                        listvalue.add(colval[strCount]);
                                }
                
                                returnval =wsc.JSONEncoding(resultdata,listvalue);

                                for(int icount=0;icount<returnval.size();icount++){
	                                String val="";
	                                try{
	                                  val=  wsc.Decrypt( returnval.get(icount));
	                                }
	                                catch(Exception er){
	                                 val=returnval.get(icount);
	                                }
	
	                                returnlist.add(val);
	                                
                                }
                
                
                        }

        
                }catch (Exception e) {
        // TODO: handle exception
                        e.getMessage();
                }
        
        
        
        return returnlist;
        
}
	
	*/
	
	
	
	
	public ArrayList<HashMap<String, String>> previousmonthactivity(String THid, String mid){
		
	  ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
		String retval="0";
		String[] list = {"faid","totalactivity","totalactivitydone"};
		try{
			Webservicerequest wsc=new Webservicerequest();
			ArrayList<String> inputlist=new ArrayList<String>();
			inputlist.add("THid");
			inputlist.add(THid);
			inputlist.add("mid");
			inputlist.add(mid);
			retval=wsc.MobileWebservice(context,"previousmonthactivity",inputlist);
			inputlist.clear();			
			if(retval!=null)
			{
				ArrayList<String> listvalue=new ArrayList<String>();
				for (String string : list) {
					listvalue.add(string);
				}
				inputlist= wsc.JSONEncoding(retval, listvalue);
				int size = list.length;
				for(int count=0; count<inputlist.size(); count+=size)
				{
					HashMap<String, String> map = new HashMap<String, String>();
					for(int i=0; i<size; i++) {
						map.put(String.valueOf((i+1)), encrypt.Decrypt(inputlist.get(count+i)));
					}					
					data.add(map);
				}
			}			
		}catch(Exception e)
		{
			e.getMessage();
		}
		return data;
	
}	
	
	
	
	
	
	//end
	
	
	
	
	
	
	// activity graph for FA
	

	public ArrayList<HashMap<String, String>> previousmonthactivityFA(String faid, String mid){
		
	  ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
		String retval="0";
		String[] list = {"faid","totalactivity","totalactivitydone"};
		try{
			Webservicerequest wsc=new Webservicerequest();
			ArrayList<String> inputlist=new ArrayList<String>();
			inputlist.add("faid");
			inputlist.add(faid);
			inputlist.add("mid");
			inputlist.add(mid);
			retval=wsc.MobileWebservice(context,"previousmonthactivityFA",inputlist);
			inputlist.clear();			
			if(retval!=null)
			{
				ArrayList<String> listvalue=new ArrayList<String>();
				for (String string : list) {
					listvalue.add(string);
				}
				inputlist= wsc.JSONEncoding(retval, listvalue);
				int size = list.length;
				for(int count=0; count<inputlist.size(); count+=size)
				{
					HashMap<String, String> map = new HashMap<String, String>();
					for(int i=0; i<size; i++) {
						map.put(String.valueOf((i+1)), encrypt.Decrypt(inputlist.get(count+i)));
					}					
					data.add(map);
				}
			}			
		}catch(Exception e)
		{
			e.getMessage();
		}
		return data;
	
}	
	
	
	
	
	//end
	
	
	
	// farmer graph for FA
	

		public ArrayList<HashMap<String, String>> farmergraphFA(String empid, String mid){
			
		  ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
			String retval="0";
			String[] list = {"empid","totalfarmer","totalfarmerver"};
			try{
				Webservicerequest wsc=new Webservicerequest();
				ArrayList<String> inputlist=new ArrayList<String>();
				inputlist.add("empid");
				inputlist.add(empid);
				inputlist.add("mid");
				inputlist.add(mid);
				retval=wsc.MobileWebservice(context,"farmergraphFA",inputlist);
				inputlist.clear();			
				if(retval!=null)
				{
					ArrayList<String> listvalue=new ArrayList<String>();
					for (String string : list) {
						listvalue.add(string);
					}
					inputlist= wsc.JSONEncoding(retval, listvalue);
					int size = list.length;
					for(int count=0; count<inputlist.size(); count+=size)
					{
						HashMap<String, String> map = new HashMap<String, String>();
						for(int i=0; i<size; i++) {
							map.put(String.valueOf((i+1)), encrypt.Decrypt(inputlist.get(count+i)));
						}					
						data.add(map);
					}
				}			
			}catch(Exception e)
			{
				e.getMessage();
			}
			return data;
		
	}	
		
		
		
		
		//end
	
	
		
		// mandi graph for FA
		

		public ArrayList<HashMap<String, String>> farmerVerifiedgraphFA(String empid, String districtid){
			
		  ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
			String retval="0";
			String[] list = {"mandi","totalfarmerver"};
			try{
				Webservicerequest wsc=new Webservicerequest();
				ArrayList<String> inputlist=new ArrayList<String>();
				inputlist.add("empid");
				inputlist.add(empid);
				inputlist.add("districtid");
				inputlist.add(districtid);
				retval=wsc.MobileWebservice(context,"farmerVerifiedgraphFA",inputlist);
				inputlist.clear();			
				if(retval!=null)
				{
					ArrayList<String> listvalue=new ArrayList<String>();
					for (String string : list) {
						listvalue.add(string);
					}
					inputlist= wsc.JSONEncoding(retval, listvalue);
					int size = list.length;
					for(int count=0; count<inputlist.size(); count+=size)
					{
						HashMap<String, String> map = new HashMap<String, String>();
						for(int i=0; i<size; i++) {
							map.put(String.valueOf((i+1)), encrypt.Decrypt(inputlist.get(count+i)));
						}					
						data.add(map);
					}
				}			
			}catch(Exception e)
			{
				e.getMessage();
			}
			return data;
		
	}	
		
		
		
		
		//end
		
		
		
		
		
		// mandi graph for FA
		

		public ArrayList<HashMap<String, String>> retailergraphFA(String empid, String districtid){
			
		  ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
			String retval="0";
			String[] list = {"mandi","totalretailer"};
			try{
				Webservicerequest wsc=new Webservicerequest();
				ArrayList<String> inputlist=new ArrayList<String>();
				inputlist.add("empid");
				inputlist.add(empid);
				inputlist.add("districtid");
				inputlist.add(districtid);
				retval=wsc.MobileWebservice(context,"retailergraphFA",inputlist);
				inputlist.clear();			
				if(retval!=null)
				{
					ArrayList<String> listvalue=new ArrayList<String>();
					for (String string : list) {
						listvalue.add(string);
					}
					inputlist= wsc.JSONEncoding(retval, listvalue);
					int size = list.length;
					for(int count=0; count<inputlist.size(); count+=size)
					{
						HashMap<String, String> map = new HashMap<String, String>();
						for(int i=0; i<size; i++) {
							map.put(String.valueOf((i+1)), encrypt.Decrypt(inputlist.get(count+i)));
						}					
						data.add(map);
					}
				}			
			}catch(Exception e)
			{
				e.getMessage();
			}
			return data;
		
	}	
		
		
		
		
		//end
		
		
		
		
	
	
	
	
	
	
	public String getName(String id) {
		// TODO Auto-generated method stub
		String result = "";
		try
		{
			db.openDataBase();
			String query = "select geoname from ms_geo where geoid = '"+encrypt.Encrypt(id)+"'";
			 Cursor cur=db.selectData(query);
			 
			 if(cur!=null){
				 
			 cur.moveToFirst();
			  
			 for(int iCount=0;iCount<cur.getCount();iCount++)
			 {
				 for(int colCount=0;colCount<cur.getColumnCount();colCount++)
				 {
					 result = encrypt.Decrypt(cur.getString(colCount));
				 }
				cur.moveToNext();
			 }
			 cur.close();
		}
			db.close();
		}
		catch(Exception e)
		{
			e.getMessage();
		}
		return result;
	}

	public ArrayList<String> getVillage() {
		// TODO Auto-generated method stub
		ArrayList<String> result = new ArrayList<String>();
		try
		{
			db.openDataBase();
			// 2 state, 3 tehsil, 4 village
			String query = "select geoname from ms_geo where markid = '"+encrypt.Encrypt("8")+"'";
			 Cursor cur=db.selectData(query);
			 
			 if(cur!=null){
				 
			 cur.moveToFirst();
			  
			 for(int iCount=0;iCount<cur.getCount();iCount++)
			 {
				 for(int colCount=0;colCount<cur.getColumnCount();colCount+=1)
				 {
						result.add(encrypt.Decrypt(cur.getString(colCount)));
				 }
				cur.moveToNext();
			 }
			 cur.close();
			 Collections.sort(result);
		}
			db.close();
		}
		catch(Exception e)
		{
			e.getMessage();
		}
		return result;
	}
	public void setVillage(String Villagename, String mandiid) {
		// TODO Auto-generated method stub
		try
		{
			String result = "";
			db.openDataBase();
			String query = "Select villid from villages where villname= '" + encrypt.Encrypt(Villagename) + "'";
			 Cursor cur=db.selectData(query);
			 
			 if(cur!=null){
				 
			 cur.moveToFirst();
			  
			 for(int iCount=0;iCount<cur.getCount();iCount++)
			 {
				 for(int colCount=0;colCount<cur.getColumnCount();colCount+=1)
				 {
						result += encrypt.Decrypt(cur.getString(colCount));
				 }
				cur.moveToNext();
			 }
			 cur.close();
			 }
			 db.close();
			 db.openDataBase();
			 if(result.length()==0)
			 {int id=1;
			 String query1 = "Select villid from villages";
			 Cursor cur1=db.selectData(query1);
			 
			 if(cur1!=null){
				 
			 cur1.moveToFirst();
			  
			 for(int iCount=0;iCount<cur1.getCount();iCount++)
			 {
				 for(int colCount=0;colCount<cur1.getColumnCount();colCount+=1)
				 {
					 if(id<Integer.parseInt(encrypt.Decrypt(cur1.getString(colCount))))
						{
						 	id = Integer.parseInt(encrypt.Decrypt(cur1.getString(colCount)));
						}
				 }
				cur1.moveToNext();
			 }
			 cur1.close();
			 }
			 db.close();
			 db.openDataBase(); 
			 String query2 = "insert into villages (villid,villname,mandiid) values ('"+encrypt.Encrypt(String.valueOf(id+1))+"','"+encrypt.Encrypt(Villagename)+"','"+encrypt.Encrypt(mandiid)+"');";
			 db.execQuery(query2);
			 }
			db.close();
		}
		catch(Exception e)
		{
			e.getMessage();
		}
	}
	public void setVillage(String Villagename) {
		// TODO Auto-generated method stub
		try
		{
			String result = "";
			db.openDataBase();
			String query = "Select geoid from ms_geo where geoname='" + encrypt.Encrypt(Villagename) + "' and markid='"+encrypt.Encrypt("8")+"'";
			 Cursor cur=db.selectData(query);
			 
			 if(cur!=null){
				 
			 cur.moveToFirst();
			  
			 for(int iCount=0;iCount<cur.getCount();iCount++)
			 {
				 for(int colCount=0;colCount<cur.getColumnCount();colCount+=1)
				 {
						result += encrypt.Decrypt(cur.getString(colCount));
				 }
				cur.moveToNext();
			 }
			 cur.close();
			 }
			 db.close();
			 db.openDataBase();
			 if(result.length()==0)
			 {int id=1;
			 String query1 = "Select geoid from ms_geo";
			 Cursor cur1=db.selectData(query1);
			 
			 if(cur1!=null){
				 
			 cur1.moveToFirst();
			  
			 for(int iCount=0;iCount<cur1.getCount();iCount++)
			 {
				 for(int colCount=0;colCount<cur1.getColumnCount();colCount+=1)
				 {
					 if(id<Integer.parseInt(encrypt.Decrypt(cur1.getString(colCount))))
						{
						 	id = Integer.parseInt(encrypt.Decrypt(cur1.getString(colCount)));
						}
				 }
				cur1.moveToNext();
			 }
			 cur1.close();
			 }
			 db.close();
			 db.openDataBase(); 
			 String query2 = "insert into ms_geo (geoid,geoname,markid) values ('"+encrypt.Encrypt(String.valueOf(id+1))+"', '"+encrypt.Encrypt(Villagename)+"' , '"+encrypt.Encrypt("8")+"');";
			 db.execQuery(query2);
			 }
			db.close();
		}
		catch(Exception e)
		{
			e.getMessage();
		}
	}
	public String popup1(String pst) {
		// TODO Auto-generated method stub
		String result = "";
		try
		{
			db.openDataBase();
			// 2 state, 3 tehsil, 4 village
			String query = "select jobid from ap_jobmaster where jobdesc = '"+encrypt.Encrypt(pst)+"'";
			 Cursor cur=db.selectData(query);
			 
			 if(cur!=null){
				 
			 cur.moveToFirst();
			  
			 for(int iCount=0;iCount<cur.getCount();iCount++)
			 {
				 for(int colCount=0;colCount<cur.getColumnCount();colCount++)
				 {
						result = encrypt.Decrypt(cur.getString(colCount));
				 }
				cur.moveToNext();
			 }
			 cur.close();
		}
			db.close();
		}
		catch(Exception e)
		{
			e.getMessage();
		}
		return result;
	}
  
	
	public void insertFarmer(String farmer,String mobile,String demo,String mandiid,String village){
		try{
			db.openDataBase();
			if(!db.tb_exist("farmer"))
			{
				String query = "create table farmer (farmername varchar(100),mobile varchar(10),demo varchar(2),mandi varchar(50),village varchar(50))";
				db.execQuery(query);
			}
			db.close();
			db.openDataBase();
			String query = "insert into farmer (farmername,mobile,demo,mandi,village) values ('"+farmer+"','"+mobile+"','"+demo+"','"+encrypt.Decrypt(mandiid)+"','"+village+"')";
			db.execQuery(query);
			
			db.close();
		}
		catch(Exception e){
			e.getMessage();
		}
	}
	
	
	public void insertFarmeract(String actid,String farmer,String mobile,String demo,String mandiid,String village){
		try{
			db.openDataBase();
			if(!db.tb_exist("farmerclose"))
			{
				String query = "create table farmerclose (actid varchar(50),farmername varchar(100),mobile varchar(10),demo varchar(2),mandi varchar(50),village varchar(50))";
				db.execQuery(query);
			}
			db.close();
			db.openDataBase();
			String query = "insert into farmerclose (actid,farmername,mobile,demo,mandi,village) values ('"+actid+"','"+farmer+"','"+mobile+"','"+demo+"','"+mandiid+"','"+village+"')";
			db.execQuery(query);
			
			db.close();
		}
		catch(Exception e){
			e.getMessage();
		}
	}
	
	public ArrayList<String> farmerName(){
		ArrayList<String> result = new ArrayList<String>();
		try{
			db.openDataBase();
			String query = "select farmername from farmer";
			Cursor cur = db.selectData(query);
			if(cur!=null)
			{
				cur.moveToFirst();
				for(int i=0; i<cur.getCount(); i++)
				{
					for(int icount=0; icount<cur.getColumnCount(); icount++){
					result.add(cur.getString(icount));
					}
					cur.moveToNext();
				}
				
				cur.close();
			}
			db.close();
		}
		catch(Exception e)
		{
			e.getMessage();
		}
		return result;
	}
	
	public ArrayList<String> mobile(){
		ArrayList<String> result = new ArrayList<String>();
		try{
			db.openDataBase();
			String query = "select mobile from farmer";
			Cursor cur = db.selectData(query);
			if(cur!=null)
			{
				cur.moveToFirst();
				for(int i=0; i<cur.getCount(); i++)
				{
					for(int icount=0; icount<cur.getColumnCount(); icount++){
						result.add(cur.getString(icount));
						}
						cur.moveToNext();
				}
				cur.close();
			}
			db.close();
		}
		catch(Exception e)
		{
			e.getMessage();
		}
		return result;
	}
	
	public ArrayList<String> demo(){
		ArrayList<String> result = new ArrayList<String>();
		try{
			db.openDataBase();
			String query = "select demo from farmer";
			Cursor cur = db.selectData(query);
			if(cur!=null)
			{
				cur.moveToFirst();
				for(int i=0; i<cur.getCount(); i++)
				{
					for(int icount=0; icount<cur.getColumnCount(); icount++){
						result.add(cur.getString(icount));
						}
						cur.moveToNext();
				}
				cur.close();
			}
			db.close();
		}
		catch(Exception e)
		{
			e.getMessage();
		}
		return result;
	}
	
	public ArrayList<String> mandi(){
		ArrayList<String> result = new ArrayList<String>();
		try{
			db.openDataBase();
			String query = "select mandi from farmer";
			Cursor cur = db.selectData(query);
			if(cur!=null)
			{
				cur.moveToFirst();
				for(int i=0; i<cur.getCount(); i++)
				{
					for(int icount=0; icount<cur.getColumnCount(); icount++){
						result.add(cur.getString(icount));
						}
						cur.moveToNext();
				}
				cur.close();
			}
			db.close();
		}
		catch(Exception e)
		{
			e.getMessage();
		}
		return result;
	}
	
	public ArrayList<String> village(){
		ArrayList<String> result = new ArrayList<String>();
		try{
			db.openDataBase();
			String query = "select village from farmer";
			Cursor cur = db.selectData(query);
			if(cur!=null)
			{
				cur.moveToFirst();
				for(int i=0; i<cur.getCount(); i++)
				{
					for(int icount=0; icount<cur.getColumnCount(); icount++){
						result.add(cur.getString(icount));
						}
						cur.moveToNext();
				}
				cur.close();
			}
			db.close();
		}
		catch(Exception e)
		{
			e.getMessage();
		}
		return result;
	}
	
	public void dropfarmer(){
		try{
			db.openDataBase();
			if(db.tb_exist("farmer"))
			{
				String query = "drop table farmer";
				db.delete_tbl(query);
			}
			db.close();
		}
		catch(Exception e){
			e.getMessage();
		}
	}
	
	public ArrayList<String> farmercloseactid(){
		ArrayList<String> result = new ArrayList<String>();
		try{
			db.openDataBase();
			String query = "select actid from farmerclose";
			Cursor cur = db.selectData(query);
			if(cur!=null)
			{
				cur.moveToFirst();
				for(int i=0; i<cur.getCount(); i++)
				{
					for(int icount=0; icount<cur.getColumnCount(); icount++){
					result.add(cur.getString(icount));
					}
					cur.moveToNext();
				}
				
				cur.close();
			}
			db.close();
		}
		catch(Exception e)
		{
			e.getMessage();
		}
		return result;
	}
	
	
	
	public ArrayList<String> farmercloseName(){
		ArrayList<String> result = new ArrayList<String>();
		try{
			db.openDataBase();
			String query = "select farmername from farmerclose";
			Cursor cur = db.selectData(query);
			if(cur!=null)
			{
				cur.moveToFirst();
				for(int i=0; i<cur.getCount(); i++)
				{
					for(int icount=0; icount<cur.getColumnCount(); icount++){
					result.add(cur.getString(icount));
					}
					cur.moveToNext();
				}
				
				cur.close();
			}
			db.close();
		}
		catch(Exception e)
		{
			e.getMessage();
		}
		return result;
	}
	
	public ArrayList<String> mobileclose(){
		ArrayList<String> result = new ArrayList<String>();
		try{
			db.openDataBase();
			String query = "select mobile from farmerclose";
			Cursor cur = db.selectData(query);
			if(cur!=null)
			{
				cur.moveToFirst();
				for(int i=0; i<cur.getCount(); i++)
				{
					for(int icount=0; icount<cur.getColumnCount(); icount++){
						result.add(cur.getString(icount));
						}
						cur.moveToNext();
				}
				cur.close();
			}
			db.close();
		}
		catch(Exception e)
		{
			e.getMessage();
		}
		return result;
	}
	
	public ArrayList<String> democlose(){
		ArrayList<String> result = new ArrayList<String>();
		try{
			db.openDataBase();
			String query = "select demo from farmerclose";
			Cursor cur = db.selectData(query);
			if(cur!=null)
			{
				cur.moveToFirst();
				for(int i=0; i<cur.getCount(); i++)
				{
					for(int icount=0; icount<cur.getColumnCount(); icount++){
						result.add(cur.getString(icount));
						}
						cur.moveToNext();
				}
				cur.close();
			}
			db.close();
		}
		catch(Exception e)
		{
			e.getMessage();
		}
		return result;
	}
	
	public ArrayList<String> mandiclose(){
		ArrayList<String> result = new ArrayList<String>();
		try{
			db.openDataBase();
			String query = "select mandi from farmerclose";
			Cursor cur = db.selectData(query);
			if(cur!=null)
			{
				cur.moveToFirst();
				for(int i=0; i<cur.getCount(); i++)
				{
					for(int icount=0; icount<cur.getColumnCount(); icount++){
						result.add(cur.getString(icount));
						}
						cur.moveToNext();
				}
				cur.close();
			}
			db.close();
		}
		catch(Exception e)
		{
			e.getMessage();
		}
		return result;
	}
	
	public ArrayList<String> villageclose(){
		ArrayList<String> result = new ArrayList<String>();
		try{
			db.openDataBase();
			String query = "select village from farmerclose";
			Cursor cur = db.selectData(query);
			if(cur!=null)
			{
				cur.moveToFirst();
				for(int i=0; i<cur.getCount(); i++)
				{
					for(int icount=0; icount<cur.getColumnCount(); icount++){
						result.add(cur.getString(icount));
						}
						cur.moveToNext();
				}
				cur.close();
			}
			db.close();
		}
		catch(Exception e)
		{
			e.getMessage();
		}
		return result;
	}
	
	public void dropfarmerclose(){
		try{
			db.openDataBase();
			if(db.tb_exist("farmerclose"))
			{
				String query = "drop table farmerclose";
				db.delete_tbl(query);
			}
			db.close();
		}
		catch(Exception e){
			e.getMessage();
		}
	}
	
	
	public ArrayList<HashMap<String, String>> getmdo()
	{
		 ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String,String>>();
			try
			{
				db.openDataBase();
				String query = "select empid,empname from ms_emp";
				 Cursor cur=db.selectData(query);
				 
				 if(cur!=null){
					 
				 cur.moveToFirst();
				  
				 for(int iCount=0;iCount<cur.getCount();iCount++)
				 {
					 for(int colCount=0;colCount<cur.getColumnCount();colCount+=2)
					 {
						 HashMap<String, String> map = new HashMap<String, String>();
		 					map.put("1", encrypt.Decrypt(cur.getString(colCount)));
		 					map.put("2", encrypt.Decrypt(cur.getString(colCount+1)));
		 					result.add(map);
					 }
					cur.moveToNext();
				 }
				 cur.close();
				 
				 Collections.sort(result, new MapComparator("2"));
				 
			}
				db.close();
			}
			catch(Exception e)
			{
				e.getMessage();
			}
			return result;
		
	}
	
	
	public ArrayList<HashMap<String, String>> getretailersList(String mandiid){
		 ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String,String>>();
			try
			{
				db.openDataBase();
				String query = "select chanid,firm_name from ms_chan where mandiid = '"+mandiid+"'";
				 Cursor cur=db.selectData(query);
				 
				 if(cur!=null){
					 
				 cur.moveToFirst();
				  
				 for(int iCount=0;iCount<cur.getCount();iCount++)
				 {
					 for(int colCount=0;colCount<cur.getColumnCount();colCount+=2)
					 {
						 HashMap<String, String> map = new HashMap<String, String>();
		 					map.put("1", encrypt.Decrypt(cur.getString(colCount)));
		 					map.put("2", encrypt.Decrypt(cur.getString(colCount+1)));
		 					result.add(map);
					 }
					cur.moveToNext();
				 }
				 cur.close();
				 
				 Collections.sort(result, new MapComparator("2"));
				 
			}
				db.close();
			}
			catch(Exception e)
			{
				e.getMessage();
			}
			return result;
	}
	
	public ArrayList<HashMap<String, String>> getretailersListDistrict(String districtid){
		 ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String,String>>();
			try
			{
				db.openDataBase();
				String query = "select firm_name,districtname,mobile from ms_chan where districtid = '"+encrypt.Encrypt(districtid)+"'";
				 Cursor cur=db.selectData(query);
				 
				 if(cur!=null){
					 
				 cur.moveToFirst();
				  
				 for(int iCount=0;iCount<cur.getCount();iCount++)
				 {
					 for(int colCount=0;colCount<cur.getColumnCount();colCount+=2)
					 {
						 HashMap<String, String> map = new HashMap<String, String>();
		 					map.put("1", encrypt.Decrypt(cur.getString(colCount)));
		 					map.put("2", encrypt.Decrypt(cur.getString(colCount+1)));
		 					result.add(map);
					 }
					cur.moveToNext();
				 }
				 cur.close();
				 
				 Collections.sort(result, new MapComparator("2"));
				 
			}
				db.close();
			}
			catch(Exception e)
			{
				e.getMessage();
			}
			return result;
	}
	
	
	public ArrayList<String> getDistrictList() {
		// TODO Auto-generated method stub
		ArrayList<String> result = new ArrayList<String>();
		try
		{
			db.openDataBase();
			String query = "select geoid from login";
			 Cursor cur=db.selectData(query);
			 
			 if(cur!=null){
				 
			 cur.moveToFirst();
			  
			 for(int iCount=0;iCount<cur.getCount();iCount++)
			 {
				 for(int colCount=0;colCount<cur.getColumnCount();colCount++)
				 {
					 result.add(cur.getString(colCount));
				 }
				cur.moveToNext();
			 }
			 cur.close();			 
		}
			db.close();
		}
		catch(Exception e)
		{
			e.getMessage();
		}
		return result;
	}
	
	public ArrayList<HashMap<String, String>> notification(String empId)
 	{
	  ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
 		String retval="0";
 		try{
 			Webservicerequest wsc=new Webservicerequest();
 			ArrayList<String> inputlist=new ArrayList<String>();
 			inputlist.add("tablename");
 			inputlist.add(encrypt.Encrypt("notifications"));
 			inputlist.add("selectionarg");
 			inputlist.add(encrypt.Encrypt("Date,Message"));
 			inputlist.add("condition");
 			inputlist.add(encrypt.Encrypt("where EmpId = '"+empId+"' and Active = '1'"));
 			inputlist.add("type");
 			inputlist.add(encrypt.Encrypt("0"));
 			retval=wsc.MobileWebservice(context,"TableDataSfe",inputlist );
 			inputlist.clear();
			if(retval!=null)
			{
				ArrayList<String> listvalue=new ArrayList<String>();
				listvalue.add("Date");
				listvalue.add("Message");
				inputlist= wsc.JSONEncoding(retval, listvalue);
 				
 				for(int count=0; count<inputlist.size(); count+=2)
 				{
 					String date[] = encrypt.Decrypt(inputlist.get(count)).split(" ");
 					HashMap<String, String> map = new HashMap<String, String>();
 					map.put("1", encrypt.Decrypt(inputlist.get(count+1)));
 					map.put("2", date[0]);
 					data.add(map);
 				}
			}			
 		}catch(Exception e)
 		{
 			e.getMessage();
 		}
 		
 	return data;
 	}
  
	
	public ArrayList<HashMap<String, String>> notificationInactive(String empId)
	{
	  ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
		String retval="0";
		try{
			Webservicerequest wsc=new Webservicerequest();
			ArrayList<String> inputlist=new ArrayList<String>();
			inputlist.add("tablename");
			inputlist.add(encrypt.Encrypt("notifications"));
			inputlist.add("selectionarg");
			inputlist.add(encrypt.Encrypt("Active = '0'"));
			inputlist.add("condition");
			inputlist.add(encrypt.Encrypt("where EmpId = '"+empId+"'"));
			inputlist.add("type");
			inputlist.add(encrypt.Encrypt("1"));
			retval=wsc.MobileWebservice(context,"TableDataSfe",inputlist );
			inputlist.clear();
			if(retval!=null)
			{
				ArrayList<String> listvalue=new ArrayList<String>();
				listvalue.add("Message");
				inputlist= wsc.JSONEncoding(retval, listvalue);
				
				for(int count=0; count<inputlist.size(); count+=1)
				{
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("1", encrypt.Decrypt(inputlist.get(count)));
					data.add(map);
				}
			}			
		}catch(Exception e)
		{
			e.getMessage();
		}
		return data;
	}
	public ArrayList<HashMap<String, String>> getTodaysActivities(String empId, String district)
	{
	  ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
		String retval="0";
		String[] list = {"plandate","actplanid","districtname","districtid","mandi","mandiid","retailer","jobid","jobdesc","villname","jobstatus"};
		try{
			Webservicerequest wsc=new Webservicerequest();
			ArrayList<String> inputlist=new ArrayList<String>();
			inputlist.add("faid");
			inputlist.add(empId);
			retval=wsc.MobileWebservice(context,"TodaysPlan",inputlist);
			inputlist.clear();			
			if(retval!=null)
			{
				ArrayList<String> listvalue=new ArrayList<String>();
				for (String string : list) {
					listvalue.add(string);
				}
				inputlist= wsc.JSONEncoding(retval, listvalue);
				int size = list.length;
				for(int count=0; count<inputlist.size(); count+=size)
				{
					HashMap<String, String> map = new HashMap<String, String>();
					for(int i=0; i<size; i++) {
						map.put(list[i], encrypt.Decrypt(inputlist.get(count+i)));
					}					
					data.add(map);
				}
			}			
		}catch(Exception e)
		{
			e.getMessage();
		}
		return data;
	}
	
	
	
	// current task  TH start
	
	public ArrayList<HashMap<String, String>> currenttaskTH(String empId)
	{
	  ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
		String retval="0";
		String[] list = {"plandate","empname","districtid","mandi","retailer","jobid","jobdesc","villname"};
		try{
			Webservicerequest wsc=new Webservicerequest();
			ArrayList<String> inputlist=new ArrayList<String>();
			inputlist.add("THid");
			inputlist.add(empId);
			retval=wsc.MobileWebservice(context,"currenttaskTH",inputlist);
			inputlist.clear();			
			if(retval!=null)
			{
				ArrayList<String> listvalue=new ArrayList<String>();
				for (String string : list) {
					listvalue.add(string);
				}
				inputlist= wsc.JSONEncoding(retval, listvalue);
				int size = list.length;
				for(int count=0; count<inputlist.size(); count+=size)
				{
					HashMap<String, String> map = new HashMap<String, String>();
					for(int i=0; i<size; i++) {
						map.put(list[i], encrypt.Decrypt(inputlist.get(count+i)));
					}					
					data.add(map);
				}
			}			
		}catch(Exception e)
		{
			e.getMessage();
		}
		return data;
	}
	
	
	
	
	//end

	
	
	// pending task  TH start
	
		public ArrayList<HashMap<String, String>> pendingtaskTH(String empId)
		{
		  ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
			String retval="0";
			String[] list = {"plandate","empname","districtid","mandi","retailer","jobid","jobdesc","villname"};
			try{
				Webservicerequest wsc=new Webservicerequest();
				ArrayList<String> inputlist=new ArrayList<String>();
				inputlist.add("THid");
				inputlist.add(empId);
				retval=wsc.MobileWebservice(context,"pendingtaskTH",inputlist);
				inputlist.clear();			
				if(retval!=null)
				{
					ArrayList<String> listvalue=new ArrayList<String>();
					for (String string : list) {
						listvalue.add(string);
					}
					inputlist= wsc.JSONEncoding(retval, listvalue);
					int size = list.length;
					for(int count=0; count<inputlist.size(); count+=size)
					{
						HashMap<String, String> map = new HashMap<String, String>();
						for(int i=0; i<size; i++) {
							map.put(list[i], encrypt.Decrypt(inputlist.get(count+i)));
						}					
						data.add(map);
					}
				}			
			}catch(Exception e)
			{
				e.getMessage();
			}
			return data;
		}
		
		
		
		
		//end
		
		
		
		
		// upcoming task TH start
		
		public ArrayList<HashMap<String, String>> upcomingtaskTH(String empId)
		{
		  ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
			String retval="0";
			String[] list = {"plandate","empname","districtid","mandi","retailer","jobid","jobdesc","villname"};
			try{
				Webservicerequest wsc=new Webservicerequest();
				ArrayList<String> inputlist=new ArrayList<String>();
				inputlist.add("THid");
				inputlist.add(empId);
				retval=wsc.MobileWebservice(context,"upcomingtaskTH",inputlist);
				inputlist.clear();			
				if(retval!=null)
				{
					ArrayList<String> listvalue=new ArrayList<String>();
					for (String string : list) {
						listvalue.add(string);
					}
					inputlist= wsc.JSONEncoding(retval, listvalue);
					int size = list.length;
					for(int count=0; count<inputlist.size(); count+=size)
					{
						HashMap<String, String> map = new HashMap<String, String>();
						for(int i=0; i<size; i++) {
							map.put(list[i], encrypt.Decrypt(inputlist.get(count+i)));
						}					
						data.add(map);
					}
				}			
			}catch(Exception e)
			{
				e.getMessage();
			}
			return data;
		}
		
		
		
		
		//end
		
		
		
	
	
	
	
	
	
	
	
	
	
	public ArrayList<HashMap<String, String>> getPendingActivities(String empId, String district)
	{
	  ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
		String retval="0";
		//String[] list = {"plan_date","actid","jobstatus","firm_id","firm_name","jobid","jobdesc","reamarks","actplanid","mandi","village","districtname"};
		String[] list = {"plandate","districtid","mandi","retailer","jobdesc","villname","jobstatus"};
		try{
			Webservicerequest wsc=new Webservicerequest();
			ArrayList<String> inputlist=new ArrayList<String>();
			inputlist.add("faid");
			inputlist.add(empId);
			inputlist.add("districtid");
			inputlist.add(district);
			retval=wsc.MobileWebservice(context,"PendingPlans",inputlist);
			inputlist.clear();			
			if(retval!=null)
			{
				ArrayList<String> listvalue=new ArrayList<String>();
				for (String string : list) {
					listvalue.add(string);
				}
				inputlist= wsc.JSONEncoding(retval, listvalue);
				int size = list.length;
				for(int count=0; count<inputlist.size(); count+=size)
				{
					HashMap<String, String> map = new HashMap<String, String>();
					for(int i=0; i<size; i++) {
						map.put(list[i], encrypt.Decrypt(inputlist.get(count+i)));
					}					
					data.add(map);
				}
			}			
		}catch(Exception e)
		{
			e.getMessage();
		}
		return data;
	}
	
	public ArrayList<HashMap<String, String>> getCalendarActivities(String fromdate, String todate, String empId, String district,String mid)
	{
		
		ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
		String retval="0";
		String[] list = {"plan_date","actid","jobstatus","firm_id","firm_name","jobid","jobdesc","reamarks","actplanid","mandi","villname","districtname","planstatus"};
		try{
			Webservicerequest wsc=new Webservicerequest();
			ArrayList<String> inputlist=new ArrayList<String>();			
			inputlist.add("faid");
			inputlist.add(empId);
			inputlist.add("mid");
			inputlist.add(mid);
			
			retval=wsc.MobileWebservice(context,"Calenderfa",inputlist);
			inputlist.clear();			
			if(retval!=null)
			{
				ArrayList<String> listvalue=new ArrayList<String>();
				for (String string : list) {
					listvalue.add(string);
				}
				inputlist= wsc.JSONEncoding(retval, listvalue);
				int size = list.length;
				for(int count=0; count<inputlist.size(); count+=size)
				{
					HashMap<String, String> map = new HashMap<String, String>();
					for(int i=0; i<size; i++) {
						map.put(list[i], encrypt.Decrypt(inputlist.get(count+i)));
					}					
					data.add(map);
				}
			}			
		}catch(Exception e)
		{
			e.getMessage();
		}
		return data;
	}
	//mo
	public ArrayList<HashMap<String, String>> getPlannedAtivitiesTH(String empId, String district)
	{
	  ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
		String retval="0";	
		String[] list = {"plan_date","actid","jobstatus","firm_id","firm_name","jobid","jobdesc","reamarks","actplanid","mandi","villname","districtname","actplanid","planstatus"};
		try{
			Webservicerequest wsc=new Webservicerequest();
			ArrayList<String> inputlist=new ArrayList<String>();
			inputlist.add("faid");
			inputlist.add(empId);
			inputlist.add("startdate");
			inputlist.add(district);
			retval=wsc.MobileWebservice(context,"OneWeekLaterPlans",inputlist);
			inputlist.clear();			
			if(retval!=null)
			{
				ArrayList<String> listvalue=new ArrayList<String>();
				for (String string : list) {
					listvalue.add(string);
				}
				inputlist= wsc.JSONEncoding(retval.split("\\$")[0], listvalue);
				int size = list.length;
				for(int count=0; count<inputlist.size(); count+=size)
				{
					HashMap<String, String> map = new HashMap<String, String>();
					for(int i=0; i<size; i++) {
						map.put(list[i], encrypt.Decrypt(inputlist.get(count+i)));
					}					
					data.add(map);
				}
				listvalue.clear();
				listvalue.add("date");
				inputlist = wsc.JSONEncoding(retval.split("\\$")[1], listvalue);
				String dates = "";
				for(int i=0; i<inputlist.size(); i++){
					if(i>0 && i<inputlist.size()){
						dates += ",";
					}
					dates += encrypt.Decrypt(inputlist.get(i));
				}
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("date", dates);
				data.add(map);
			}			
		}catch(Exception e)
		{
			e.getMessage();
		}
		return data;
	}
	
	//
	
	public ArrayList<HashMap<String, String>> getPlannedAtivities(String empId, String district)
	{
	  ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
		String retval="0";	
	//	plandate,actplanid,districtid,mandi,villname,retailer,jobdesc,planstatus
		String[] list = {"plandate","actplanid","districtid","mandi","villname","retailer","jobdesc","planstatus","jobstatus"};
		try{
			Webservicerequest wsc=new Webservicerequest();
			ArrayList<String> inputlist=new ArrayList<String>();
			inputlist.add("faid");
			inputlist.add(empId);
			retval=wsc.MobileWebservice(context,"getplansfa",inputlist);
			inputlist.clear();
			if(retval!=null)
			{
				ArrayList<String> listvalue=new ArrayList<String>();
				for (String string : list) {
					listvalue.add(string);
				}
				inputlist= wsc.JSONEncoding(retval, listvalue);
				int size = list.length;
				for(int count=0; count<inputlist.size(); count+=size)
				{
					HashMap<String, String> map = new HashMap<String, String>();
					for(int i=0; i<size; i++) {
						map.put(list[i], encrypt.Decrypt(inputlist.get(count+i)));
					}					
					data.add(map);
				}
			}			
		}catch(Exception e)
		{
			e.getMessage();
		}
		return data;
	}
	
	public String DeleteData(String id)
	{
		
		String retval="0";	
		Webservicerequest wsc=new Webservicerequest();
		ArrayList<String> inputlist=new ArrayList<String>();
		inputlist.add("actplanid");
		inputlist.add(id);		
		retval=wsc.MobileWebservice(context,"deleteplan",inputlist);		
		return retval;
	}
	public String ApproveData(String id)
	{
		
		String retval="0";	
		Webservicerequest wsc=new Webservicerequest();
		ArrayList<String> inputlist=new ArrayList<String>();
		inputlist.add("actplanid");
		inputlist.add(id);		
		retval=wsc.MobileWebservice(context,"approveplan",inputlist);		
		return retval;
	}
	
	
	//start
	public ArrayList<HashMap<String, String>> getPlannedAtivitiesToApprrove(String empId)
	{
	  ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
		String retval="0";	
		String[] list = {"plan_date","actid","jobstatus","firm_id","firm_name","jobid","jobdesc","reamarks","actplanid","mandi","districtname","villname","actplanid","planstatus"};
		try{
			Webservicerequest wsc=new Webservicerequest();
			ArrayList<String> inputlist=new ArrayList<String>();
			inputlist.add("faid");
			inputlist.add(empId);
			
			retval=wsc.MobileWebservice(context,"getplansfatoapprove",inputlist);
			inputlist.clear();			
			if(retval!=null)
			{
				ArrayList<String> listvalue=new ArrayList<String>();
				for (String string : list) {
					listvalue.add(string);
				}
				inputlist= wsc.JSONEncoding(retval.split("\\$")[0], listvalue);
				int size = list.length;
				for(int count=0; count<inputlist.size(); count+=size)
				{
					HashMap<String, String> map = new HashMap<String, String>();
					for(int i=0; i<size; i++) {
						map.put(list[i], encrypt.Decrypt(inputlist.get(count+i)));
					}					
					data.add(map);
				}
				listvalue.clear();
				listvalue.add("date");
				inputlist = wsc.JSONEncoding(retval.split("\\$")[1], listvalue);
				String dates = "";
				for(int i=0; i<inputlist.size(); i++){
					if(i>0 && i<inputlist.size()){
						dates += ",";
					}
					dates += encrypt.Decrypt(inputlist.get(i));
				}
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("date", dates);
				data.add(map);
			}			
		}catch(Exception e)
		{
			e.getMessage();
		}
		return data;
	}
	
	
	//end
	
	
	
	
	//start for TH View tab
		public ArrayList<HashMap<String, String>> getPlannedAtivitiesToView(String empId)
		{
		  ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
			String retval="0";	
			String[] list = {"plan_date","actid","jobstatus","firm_id","firm_name","jobid","jobdesc","reamarks","actplanid","mandi","villname","districtname","actplanid","planstatus"};
			try{
				Webservicerequest wsc=new Webservicerequest();
				ArrayList<String> inputlist=new ArrayList<String>();
				inputlist.add("faid");
				inputlist.add(empId);
				
				retval=wsc.MobileWebservice(context,"getplansfatoview",inputlist);
				inputlist.clear();			
				if(retval!=null)
				{
					ArrayList<String> listvalue=new ArrayList<String>();
					for (String string : list) {
						listvalue.add(string);
					}
					inputlist= wsc.JSONEncoding(retval.split("\\$")[0], listvalue);
					int size = list.length;
					for(int count=0; count<inputlist.size(); count+=size)
					{
						HashMap<String, String> map = new HashMap<String, String>();
						for(int i=0; i<size; i++) {
							map.put(list[i], encrypt.Decrypt(inputlist.get(count+i)));
						}					
						data.add(map);
					}
					listvalue.clear();
					listvalue.add("date");
					inputlist = wsc.JSONEncoding(retval.split("\\$")[1], listvalue);
					String dates = "";
					for(int i=0; i<inputlist.size(); i++){
						if(i>0 && i<inputlist.size()){
							dates += ",";
						}
						dates += encrypt.Decrypt(inputlist.get(i));
					}
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("date", dates);
					data.add(map);
				}			
			}catch(Exception e)
			{
				e.getMessage();
			}
			return data;
		}
	
	
	
	
	
	
	
	public String createActivity(String dist,String date,String empId,String spmandi,String spretailer,String spactivity,String rem,String vid,String vname)
	{
		String retval="0";
		try{
			Webservicerequest wsc=new Webservicerequest();
			ArrayList<String> inputlist=new ArrayList<String>();
			inputlist.add("retailer");
			inputlist.add((spretailer));
			inputlist.add("activity");
			inputlist.add((spactivity));
			inputlist.add("remark");
			inputlist.add((rem));
			inputlist.add("actiondate");
			inputlist.add(date);
			inputlist.add("dist");
			inputlist.add((dist));
			inputlist.add("empid");
			inputlist.add((empId));
			inputlist.add("mandiid");
			inputlist.add((spmandi));
			inputlist.add("villid");
			inputlist.add((vid));
			inputlist.add("villname");
			inputlist.add((vname));
			retval=wsc.MobileWebservice(context,"Create_Activity_FA",inputlist );
			
		}catch(Exception e)
		{
			e.getMessage();
		}
		
	return retval;
	}

	
	
	// create farmer
	
	public String createFarmer(String name,String fathername,String mobile,String location,String mdoid,String districtid,String village,String year,String season,String crop,String crop1,String crop2,String variety,String potashappdate,String showingdate,String mandi)
	{
		String retval="0";
		try{
			Webservicerequest wsc=new Webservicerequest();
			ArrayList<String> inputlist=new ArrayList<String>();
			inputlist.add("name");
			inputlist.add((name));
			inputlist.add("fathername");
			inputlist.add((fathername));
			inputlist.add("mobile");
			inputlist.add((mobile));
			inputlist.add("location");
			inputlist.add(location);
			inputlist.add("mdoid");
			inputlist.add((mdoid));
			inputlist.add("districtid");
			inputlist.add((districtid));
			inputlist.add("village");
			inputlist.add((village));
			inputlist.add("year");
			inputlist.add((year));
			inputlist.add("season");
			inputlist.add((season));
			inputlist.add("crop");
			inputlist.add((crop));
			inputlist.add("crop1");
			inputlist.add((crop1));
			inputlist.add("crop2");
			inputlist.add((crop2));
			inputlist.add("variety");
			inputlist.add((variety));
			inputlist.add("potashappdate");
			inputlist.add((potashappdate));
			inputlist.add("showingdate");
			inputlist.add((showingdate));
			inputlist.add("mandi");
			inputlist.add((mandi));
			retval=wsc.MobileWebservice(context,"Create_demofarmer",inputlist );
			
		}catch(Exception e)
		{
			e.getMessage();
		}
		
	return retval;
	}
	
	
	// end
	
	
	
	public String createActivityTH(String dist,String date,String empId,String spmandi,String spretailer,String spactivity,String rem,String vid,String vname)
	{
		String retval="0";
		try{
			Webservicerequest wsc=new Webservicerequest();
			ArrayList<String> inputlist=new ArrayList<String>();
			inputlist.add("retailer");
			inputlist.add((spretailer));
			inputlist.add("activity");
			inputlist.add((spactivity));
			inputlist.add("remark");
			inputlist.add((rem));
			inputlist.add("actiondate");
			inputlist.add(date);
			inputlist.add("dist");
			inputlist.add((dist));
			inputlist.add("empid");
			inputlist.add((empId));
			inputlist.add("mandiid");
			inputlist.add((spmandi));
			inputlist.add("villid");
			inputlist.add((vid));
			inputlist.add("villname");
			inputlist.add((vname));
			retval=wsc.MobileWebservice(context,"Create_Activity_TH",inputlist );
			
		}catch(Exception e)
		{
			e.getMessage();
		}
		
	return retval;
	}
	
	public ArrayList<HashMap<String, String>> getRetailer()
 	{
		ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String,String>>();
		try
		{
			db.openDataBase();
			String query = "select chanid,firm_name from ms_chan";
			 Cursor cur=db.selectData(query);
			 
			 if(cur!=null){
				 
			 cur.moveToFirst();
			  
			 for(int iCount=0;iCount<cur.getCount();iCount++)
			 {
				 for(int colCount=0;colCount<cur.getColumnCount();colCount+=2)
				 {
					 HashMap<String, String> map = new HashMap<String, String>();
	 					map.put("1", encrypt.Decrypt(cur.getString(colCount)));
	 					map.put("2", encrypt.Decrypt(cur.getString(colCount+1)));
	 					result.add(map);
				 }
				cur.moveToNext();
			 }
			 cur.close();
			 
			 Collections.sort(result, new MapComparator("2"));
			 
		}
			db.close();
		}
		catch(Exception e)
		{
			e.getMessage();
		}
		return result;
	  
 	}
  
  
  public ArrayList<HashMap<String, String>> getActivity()
	{
	  ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String,String>>();
		try
		{
			db.openDataBase();
			String query = "select jobid,jobdesc from ap_jobmaster";
			 Cursor cur=db.selectData(query);
			 
			 if(cur!=null){
				 
			 cur.moveToFirst();
			  
			 for(int iCount=0;iCount<cur.getCount();iCount++)
			 {
				 for(int colCount=0;colCount<cur.getColumnCount();colCount+=2)
				 {
					 HashMap<String, String> map = new HashMap<String, String>();
	 					map.put("1", encrypt.Decrypt(cur.getString(colCount)));
	 					map.put("2", encrypt.Decrypt(cur.getString(colCount+1)));
	 					result.add(map);
				 }
				cur.moveToNext();
			 }
			 cur.close();
			 Collections.sort(result, new MapComparator("2"));
		}
			db.close();
		}
		catch(Exception e)
		{
			e.getMessage();
		}
		return result;
	  
	  
	}

  public ArrayList<Myspinner> getVillages(String mid)
	{
	  ArrayList<Myspinner> result = new ArrayList<Myspinner>();
	  
		try
		{
			result.add(new Myspinner("Select Village", "0", ""));
			db.openDataBase();
			String query = "select villid,villname from villages where mandiid='"+encrypt.Encrypt( mid)+"'";
			 Cursor cur=db.selectData(query);
			 
			 if(cur!=null){
				 
			 cur.moveToFirst();
			  
			 for(int iCount=0;iCount<cur.getCount();iCount++)
			 {
				 for(int colCount=0;colCount<cur.getColumnCount();colCount+=2)
				 {
					 
	 					Myspinner sp=new Myspinner(encrypt.Decrypt(cur.getString(colCount+1)),encrypt.Decrypt(cur.getString(colCount)),"");
	 					result.add(sp);
				 }
				cur.moveToNext();
			 }
			 cur.close();
			 
		}
			db.close();
		}
		catch(Exception e)
		{
			e.getMessage();
		}
		return result;
	  
	  
	}

  public ArrayList<Myspinner> getVill(String mid)
	{
	  ArrayList<Myspinner> result = new ArrayList<Myspinner>();
		try
		{
			db.openDataBase();
			String query = "select villid,villname from ms_vill where mandiid='"+mid+"'";
			 Cursor cur=db.selectData(query);
			 
			 if(cur!=null){
				 
			 cur.moveToFirst();
			  
			 for(int iCount=0;iCount<cur.getCount();iCount++)
			 {
				 for(int colCount=0;colCount<cur.getColumnCount();colCount+=2)
				 {
					 
	 					Myspinner sp=new Myspinner(encrypt.Decrypt(cur.getString(colCount+1)),encrypt.Decrypt(cur.getString(colCount)),"");
	 					result.add(sp);
				 }
				cur.moveToNext();
			 }
			 cur.close();
			 
		}
			db.close();
		}
		catch(Exception e)
		{
			e.getMessage();
		}
		return result;
	  
	  
	}

 
	public void insertFarmer(String farmer,String mobile,String demo,String mandiid,String village, String districtid){
		try{
			db.openDataBase();
			if(!db.tb_exist("farmer"))
			{
				String query = "create table farmer (farmername varchar(100),mobile varchar(10),demo varchar(2),mandi varchar(50),village varchar(50),district varchar(50))";
				db.execQuery(query);
			}
			db.close();
			db.openDataBase();
			String query = "insert into farmer (farmername,mobile,demo,mandi,village,district) values ('"+farmer+"','"+mobile+"','"+demo+"','"+encrypt.Decrypt(mandiid)+"','"+village+"','"+districtid+"')";
			db.execQuery(query);
			
			db.close();
		}
		catch(Exception e){
			e.getMessage();
		}
	}



	
	
	
	
	
	
  
  
  
	
	
}