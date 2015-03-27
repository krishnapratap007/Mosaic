package aksha.database;

import java.util.ArrayList;

import aksha.webservice.Webservicerequest;
import android.content.Context;




public class CreateTable {
	
	Context context;
	
	public String  createMasterTablen(String functionName, String tablename,String selectionarg,String condition,int N,
			boolean autocheck,boolean photocheck,boolean photocheck2,String type, Databaseutill db,Context context) {
	String retvalstr="";

	this.context = context;
	try{
	Webservicerequest wsc=new Webservicerequest();
	 ArrayList<String> inputlist=new ArrayList<String>();
	 ArrayList<String> returnval=new ArrayList<String>();
	 inputlist.add("tablename");
	 inputlist.add(wsc.Encrypt(tablename));//tblmas_crop");
	inputlist.add("selectionarg");
	 inputlist.add(wsc.Encrypt(selectionarg));//"Cropid,Cropname,Isactive");
	 inputlist.add("condition");
	 inputlist.add(wsc.Encrypt(condition));//"where Isactive='1'");
	 inputlist.add("type");
	 inputlist.add(wsc.Encrypt("0"));
	  String resultdata=wsc.MobileWebservice(context,functionName,inputlist);
	  ArrayList<String> listvalue=new ArrayList<String>();
	  if(resultdata!=null){
		  String [] colval=selectionarg.split(",");
		 
		  for(int strCount=0;strCount<colval.length;strCount++)
		  {
		 listvalue.add(colval[strCount]);
		 }
		
		 returnval =wsc.JSONEncoding(resultdata,listvalue);
				
		
		}
	
	db.openDataBase();
	 String query="drop table "+tablename;//tblmas_crop";
		 db.execQuery(query);
		 db.close();
		 String querycr="create table "+tablename+"(";//tblmas_crop(Cropid text,Cropname text,Isactive text)";;
		 String newquery="insert into "+tablename+" (";
		 for(int strCount=0;strCount<listvalue.size();strCount++){
			 if(strCount==0){
			 if(autocheck){
				 querycr+=listvalue.get(strCount)+" integer primary key autoincrement,";
			 }
			 else{
				 querycr+=listvalue.get(strCount)+" text,";
			 }}
			 else{
				 querycr+=listvalue.get(strCount)+" text,";
			 }
			 
		    //"Cropid text,Cropname text,Isactive text)";
		  newquery+=listvalue.get(strCount)+",";
		 }
		 querycr =querycr.substring(0,(querycr.length()-1));
		 if(photocheck){
		 querycr+=",Photodb text,statusl text)";
		 }
		 else if(photocheck2){
			 querycr+=",Photodb text,Photodb1 text,statusl text)";
			 }
		 else
		 {
			 querycr+=",statusl text)";
		 }
		 newquery =newquery.substring(0,(newquery.length()-1));
		 if(photocheck){
			 newquery+=",Photodb,statusl) values (";
		 }else if(photocheck2){
			 newquery+=",Photodb,Photodb1,statusl) values (";
		 }
		 else{
		 newquery+=",statusl) values (";
		 }
		 db.openDataBase();
		 db.execQuery(querycr);
		 db.close();
		 
		 for(int icount=0;icount<returnval.size();icount+=N)
			{
			 String qury=newquery;
			 boolean valchk=false;
			 for(int ccount=icount;ccount<(N+icount);ccount++){
				 if(!valchk){
				 retvalstr+="'"+returnval.get(ccount)+"',";
				 valchk=true;
				 }
			 qury+="'"+returnval.get(ccount)+"',";
			 }
			 if(photocheck)
			 {
				 qury+="'','1')";
			 }
			 else if(photocheck2){
				 qury+="'','','1')";
			 }
			 else{
				 qury+="'1')";
			 }
			 db.openDataBase();
			 db.execQuery(qury);
			 db.close();
		}
		 
	}catch (Exception e) {
		// TODO: handle exception
		e.getMessage();
		
	}
	
return retvalstr;
}
	
	
	public String  createMasterTablenop(String functionName, String tablename,String selectionarg,boolean autocheck,boolean photocheck,boolean photocheck2,Databaseutill db,Context context,String id) {
	String retvalstr="";

	this.context = context;
	try{
	Webservicerequest wsc=new Webservicerequest();
	 ArrayList<String> inputlist=new ArrayList<String>();
	 ArrayList<String> returnval=new ArrayList<String>();
	 inputlist.add("THid");
	 inputlist.add(id);//wsc.Encrypt(tablename));
	  String resultdata=wsc.MobileWebservice(context,functionName,inputlist);
	  ArrayList<String> listvalue=new ArrayList<String>();
	  int N=0;
	  if(resultdata!=null){
		  String [] colval=selectionarg.split(",");
		 N=colval.length;
		  for(int strCount=0;strCount<colval.length;strCount++)
		  {
		 listvalue.add(colval[strCount]);
		 }
		
		 returnval =wsc.JSONEncoding(resultdata,listvalue);
				
	  }
		
	
	db.openDataBase();
	 String query="drop table "+tablename;//tblmas_crop";
		 db.execQuery(query);
		 db.close();
		 String querycr="create table "+tablename+"(";//tblmas_crop(Cropid text,Cropname text,Isactive text)";;
		 String newquery="insert into "+tablename+" (";
		 for(int strCount=0;strCount<listvalue.size();strCount++){
			 if(strCount==0){
			 if(autocheck){
				 querycr+=listvalue.get(strCount)+" integer primary key autoincrement,";
			 }
			 else{
				 querycr+=listvalue.get(strCount)+" text primary key,";
			 }}
			 else{
				 querycr+=listvalue.get(strCount)+" text,";
			 }
			 
		    //"Cropid text,Cropname text,Isactive text)";
		  newquery+=listvalue.get(strCount)+",";
		 }
		 querycr =querycr.substring(0,(querycr.length()-1));
		 if(photocheck){
		 querycr+=",Photodb text,statusl text)";
		 }
		 else if(photocheck2){
			 querycr+=",Photodb text,Photodb1 text,statusl text)";
			 }
		 else
		 {
			 querycr+=",statusl text)";
		 }
		 newquery =newquery.substring(0,(newquery.length()-1));
		 if(photocheck){
			 newquery+=",Photodb,statusl) values (";
		 }else if(photocheck2){
			 newquery+=",Photodb,Photodb1,statusl) values (";
		 }
		 else{
		 newquery+=",statusl) values (";
		 }
		 db.openDataBase();
		 db.execQuery(querycr);
		 db.close();
		 
		 for(int icount=0;icount<returnval.size();icount+=N)
			{
			 String qury=newquery;
			 boolean valchk=false;
			 for(int ccount=icount;ccount<(N+icount);ccount++){
				 if(!valchk){
				 retvalstr+="'"+returnval.get(ccount)+"',";
				 valchk=true;
				 }
			 qury+="'"+returnval.get(ccount)+"',";
			 }
			 if(photocheck)
			 {
				 qury+="'','1')";
			 }
			 else if(photocheck2){
				 qury+="'','','1')";
			 }
			 else{
				 qury+="'1')";
			 }
			 db.openDataBase();
			 db.execQuery(qury);
			 db.close();
		}
	  
		 
	}catch (Exception e) {
		// TODO: handle exception
		e.getMessage();
		
	}
	
return retvalstr;
}
	
	
	
	//for fa
	public String  createMasterTablenopfa(String functionName, String tablename,String selectionarg,boolean autocheck,boolean photocheck,boolean photocheck2,Databaseutill db,Context context,ArrayList<String> inputlist) {
		String retvalstr="";

		this.context = context;
		try{
		Webservicerequest wsc=new Webservicerequest();
		ArrayList<String> returnval=new ArrayList<String>();
		/*ArrayList<String> inputlist=new ArrayList<String>();		
		inputlist.add("THid");
		inputlist.add(id);//wsc.Encrypt(tablename));
*/		 String resultdata=wsc.MobileWebservice(context,functionName,inputlist);
		 ArrayList<String> listvalue=new ArrayList<String>();
		 int N=0;
		 if(resultdata!=null){
		 String [] colval=selectionarg.split(",");
		N=colval.length;
		 for(int strCount=0;strCount<colval.length;strCount++)
		 {
		listvalue.add(colval[strCount]);
		}
		returnval =wsc.JSONEncoding(resultdata,listvalue);
		 }
		db.openDataBase();
		String query="drop table "+tablename;//tblmas_crop";
		db.execQuery(query);
		db.close();
		String querycr="create table "+tablename+"(";//tblmas_crop(Cropid text,Cropname text,Isactive text)";;
		String newquery="insert into "+tablename+" (";
		for(int strCount=0;strCount<listvalue.size();strCount++){
		if(strCount==0){
		if(autocheck){
		querycr+=listvalue.get(strCount)+" integer primary key autoincrement,";
		}
		else{
		querycr+=listvalue.get(strCount)+" text primary key,";
		}}
		else{
		querycr+=listvalue.get(strCount)+" text,";
		}
		 
		   //"Cropid text,Cropname text,Isactive text)";
		 newquery+=listvalue.get(strCount)+",";
		}
		querycr =querycr.substring(0,(querycr.length()-1));
		if(photocheck){
		querycr+=",Photodb text,statusl text)";
		}
		else if(photocheck2){
		querycr+=",Photodb text,Photodb1 text,statusl text)";
		}
		else
		{
		querycr+=",statusl text)";
		}
		newquery =newquery.substring(0,(newquery.length()-1));
		if(photocheck){
		newquery+=",Photodb,statusl) values (";
		}else if(photocheck2){
		newquery+=",Photodb,Photodb1,statusl) values (";
		}
		else{
		newquery+=",statusl) values (";
		}
		db.openDataBase();
		db.execQuery(querycr);
		db.close();
		 
		for(int icount=0;icount<returnval.size();icount+=N)
		{
		String qury=newquery;
		boolean valchk=false;
		for(int ccount=icount;ccount<(N+icount);ccount++){
		if(!valchk){
		retvalstr+="'"+returnval.get(ccount)+"',";
		valchk=true;
		}
		qury+="'"+returnval.get(ccount)+"',";
		}
		if(photocheck)
		{
		qury+="'','1')";
		}
		else if(photocheck2){
		qury+="'','','1')";
		}
		else{
		qury+="'1')";
		}
		db.openDataBase();
		db.execQuery(qury);
		db.close();
		}
		 
		 
		}catch (Exception e) {
		// TODO: handle exception
		e.getMessage();
		}
		return retvalstr;
		}
	
	
	
	
	public  ArrayList<String> filedata(String val)
	{ ArrayList<String> returnval = new ArrayList<String>();
	try{
		Webservicerequest wsc=new Webservicerequest();
		 ArrayList<String> inputlist=new ArrayList<String>();
		 inputlist.add(val);
		 inputlist.add(wsc.Encrypt(""));
		
		  String resultdata=wsc.MobileWebservice(context,"listFolder",inputlist);
		  if(resultdata!=null)
		  { 																																						
			  ArrayList<String> listvalue = new ArrayList<String>();
			 listvalue.add("Message");
			   returnval =wsc.JSONEncoding(resultdata,listvalue);
			  
		  }
	}catch(Exception er)
	{
		er.getMessage();
	}
	return returnval;
	}
	
}