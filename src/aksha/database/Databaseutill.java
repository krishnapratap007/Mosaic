package aksha.database;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;

import aksha.homescreen.HomeScreen;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class Databaseutill extends SQLiteOpenHelper 

{
	 private static String DB_PATH = "";
	    private String DB_NAME = "";
	    SQLiteDatabase myDataBase;
	    private final Context myContext;
	    private static Databaseutill dbConnection;
	 
	  public Databaseutill(Context context) {
	        super(context, "DBName.sqlite", null, 1);
	        this.myContext = context;
	        DB_NAME = "DBName.sqlite";
	        // default db path for android 
	        DB_PATH = "/data/data/"
	                + context.getApplicationContext().getPackageName()
	                + "/databases/";
	     //  File sdcard = Environment.getExternalStorageDirectory();

	      //  DB_PATH = sdcard.getAbsolutePath() + File.separator+ "download" + File.separator + DB_NAME;
	    }
	 
	    public static synchronized Databaseutill getDBAdapterInstance(Context context) {
	        if (dbConnection == null) {
	            dbConnection = new Databaseutill(context);
	        }
	        return dbConnection;
	    }
	 
	    public void createDatabase() throws IOException {
	        /*boolean dbExist = checkDataBase();
	        if (!dbExist) {
	            this.getReadableDatabase();
	           // copyDataBase();
	        }else*/
	        {
	        	try {
	        		//copyDataBase();
					myDataBase=openDataBase();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
	    }
	 
	    public synchronized boolean checkDataBase() {
	        SQLiteDatabase checkDB = null;
	        boolean val=false;
	        try {
	        	String myPath=DB_PATH+DB_NAME;
	            //String myPath = DB_PATH ;//+ DB_NAME;
	            checkDB = SQLiteDatabase.openDatabase(myPath, null,
	                    SQLiteDatabase.OPEN_READONLY);
	            
	        /*    File fdb=myContext.getDatabasePath(myPath);
	            if(fdb.exists())
	            {
	            	val=true;
	            }else{
	            	val=false;
	            }*/
	 
	        } 
	        catch (SQLiteException e)
	        {
	        	e.getMessage(); 
	        	//Toast.makeText(myContext, e.getMessage(), Toast.LENGTH_LONG).show();
	        }
	        if (checkDB != null) {
	            checkDB.close();
	        }
	        
	        if(checkDB != null)
	        {
	        	val=true;
	        }
	        
	        return val;
	    }
	 
	    
	    public synchronized boolean tb_exist(String tb_name)
	    {
	    	boolean result=false;
	    	if(checkDataBase())
	    	{
	    	   Cursor crs;
	    	
	    	    try{
	    		     openDataBase();
	    	         String qury="select * from "+tb_name+"";
	    	         crs=myDataBase.rawQuery(qury, null);
	    	         myDataBase.close();
	    	        }
	    	    catch (Exception e)
	    	        {
				     return false;
			        }
	    	    result= true;
	    	}


	    		return result;
	    	
	    }
	    
	    public void copyDataBase() throws IOException {
	    	 
	        InputStream myInput = myContext.getAssets().open(DB_NAME);
	       String outFileName = DB_PATH + DB_NAME;
	        //String outFileName = DB_PATH ;//+ DB_NAME;
	        OutputStream myOutput = new FileOutputStream(outFileName);
	 
	        byte[] buffer = new byte[1024];
	        int length;
	        while ((length = myInput.read(buffer)) > 0) {
	            myOutput.write(buffer, 0, length);
	        }
	 
	        myOutput.flush();
	        myOutput.close();
	        myInput.close();
	    }
	 
	    public SQLiteDatabase openDataBase() throws SQLException {
	    	
	    	
	    	String myPath = DB_PATH + DB_NAME;
	      // String myPath = DB_PATH ;//+ DB_NAME;
	    	
	    	File file = new File(myPath);    
	    	if (file.exists() && !file.isDirectory()){
	    	// if(myDataBase==null){
	          myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
	    	 }else{
	    		 myDataBase=this.getReadableDatabase();
	    	 }
	        return myDataBase;
	    }
	 
	    @Override
	    public synchronized void close() {
	        if (myDataBase != null)
	        {
	            myDataBase.close();
	        super.close();}
	    }
	    
	    // db create data base 
	 
	    @Override
	    public void onCreate(SQLiteDatabase db) {
	    }
	 
	    @Override
	    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	    }
	 
	    // ----------------------- DB Operations ------------------------------
	
	    public Cursor selectRecordsFromDB(String tableName, String[] tableColumns,
	                                      String whereClause, String whereArgs[], String groupBy,
	                                      String having, String orderBy) {
	        return myDataBase.query(tableName, tableColumns, whereClause, whereArgs,
	                groupBy, having, orderBy);
	    }
	 
	    public ArrayList DBAdapter(String tableName, String[] tableColumns,
	                               String whereClause, String whereArgs[], String groupBy,
	                               String having, String orderBy) {
	 
	        ArrayList retList = new ArrayList();
	        ArrayList list;
	        Cursor cursor = myDataBase.query(tableName, tableColumns, whereClause, whereArgs,
	                groupBy, having, orderBy);
	        if (cursor.moveToFirst()) {
	            do {
	                list = new ArrayList();
	                for(int i=0; i<cursor.getColumnCount(); i++){
	                    list.add( cursor.getString(i) );
	                }
	                retList.add(list);
	            } while (cursor.moveToNext());
	        }
	 
	        return retList;
	    }
	    public int updateRecordsInDB(String tableName, ContentValues initialValues, String whereClause, String whereArgs[]) {
	        return myDataBase.update(tableName, initialValues, whereClause, whereArgs);
	    }
	 
	    public long insertRecordsToDB(String tableName, String nullColumnHack, ContentValues contentValues) {
	        return myDataBase.insert(tableName, nullColumnHack, contentValues);
	    }
	 public synchronized void execQuery(String sql)
	 { 
		 try
		 {
		 myDataBase.execSQL(sql);
	
		 }
		 catch (Exception e)
		 {
		e.getMessage();
	}
	 }
	 
	 public synchronized Cursor selectData(String query)
	 { 
		 Cursor cur=null;
		 try
		 {
			 openDataBase();
		    cur=myDataBase.rawQuery(query, null);
		    int s=cur.getCount();
		    close();
		 }
		 catch (Exception e) {
			return cur;
		}
		    return cur;
	 }
	 
	 
	 public void delete_tbl(String query)
	 {
		 try
		 {
		 openDataBase();
		 myDataBase.execSQL(query);
		 }
		 catch (Exception e) {
			e.getMessage();
		}
		 
	 }
	    public int deleteRecordFromDB(String tableName, String whereClause,
	                                  String[] whereArgs) {
	        return myDataBase.delete(tableName, whereClause, whereArgs);
	    }
	 
	    public Cursor selectRecordsFromDB(String query, String[] selectionArgs) {
	        return myDataBase.rawQuery(query, selectionArgs);
	    }
	 
	    public ArrayList selectRecordsFromDBList(String query, String[] selectionArgs) {
	        ArrayList retList = new ArrayList();
	        ArrayList list;
	        Cursor cursor = myDataBase.rawQuery(query, selectionArgs);
	        if(cursor!=null)
	        {if (cursor.moveToFirst()) {
	            do {
	                list = new ArrayList();
	                for(int i=0; i<cursor.getColumnCount(); i++){
	                    list.add( cursor.getString(i) );
	                }
	                retList.add(list);
	            } while (cursor.moveToNext());
	        }
	        }
	        if (cursor != null && !cursor.isClosed()) {
	            cursor.close();
	        }
	        return retList;
	    }
   
 
}
