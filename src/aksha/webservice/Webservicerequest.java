package aksha.webservice;


import java.io.IOException;
import java.util.ArrayList;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.content.Context;
import android.widget.Toast;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;

public class Webservicerequest {
		
		 public String MobileWebservice(Context context, String Methodname, ArrayList<String> propertyArray)		 		 
			{	
			 //	String URL="http://182.18.175.57/MosWeb/Service.asmx";
			 String URL = "http://mosaic.akshapp.com/mobapp/service.asmx";
	 			String NAMESPACE="http://aksha/app/";
	 			String SOAPAction=NAMESPACE+Methodname;
				SoapObject request=new SoapObject(NAMESPACE, Methodname);				
				HttpTransportSE androidhttptransport=new HttpTransportSE(URL);			
				for(int icount=0;icount<propertyArray.size();icount+=2){					
				request.addProperty(propertyArray.get(icount), propertyArray.get(icount+1));
				}

			    System.setProperty("http.keepAlive", "false");
			    try {
					androidhttptransport.getServiceConnection().setRequestProperty("Connection", "close");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}


			
				SoapSerializationEnvelope envelope= new SoapSerializationEnvelope(SoapEnvelope.VER11);
				envelope.dotNet=true;
				envelope.setOutputSoapObject(request);
				
				String resultstring = null;
				
					try {
						androidhttptransport.call(SOAPAction, envelope);
						Object results=(Object)envelope.getResponse();
						androidhttptransport.reset();
	 					resultstring=results.toString();
					}  catch (Exception e) {
						
						e.printStackTrace();
					} 
					System.gc();
					
				    try {
						androidhttptransport.getServiceConnection().disconnect();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						try{
							Toast.makeText(context, "Connection Failed", Toast.LENGTH_LONG).show();
						}
						catch(Exception ex){
							ex.getMessage();
						}
					}
				    return resultstring;
			}
		 
			public ArrayList<String> JSONEncoding(String result, ArrayList<String> listval)
			
			
			{
			ArrayList<String> al=new ArrayList<String>();
			
			try
			{
			JSONArray array = new JSONArray(result);
			for (int i = 0; i < array.length(); i++)
			{
			    JSONObject row = array.getJSONObject(i);
			    for(int icount = 0; icount < listval.size(); icount++){
			   String servicename = row.getString(listval.get(icount));
			   
			 
			   al.add(servicename);
			 
			
			}
			}
			}
			catch(Exception e)
			{
				String msg=e.getMessage().toString();
			}
			return al;
			}
		

			public String Encrypt(String text)
					throws Exception {
				 String iv = "!QAZ2WSX#EDC4RFV";
				    String key = "5TGB&YHN7UJM(IK<";
					Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
					byte[] keyBytes= new byte[16];
					byte[] b= key.getBytes("UTF-8");
					int len= b.length;
					if (len > keyBytes.length) len = keyBytes.length;
					System.arraycopy(b, 0, keyBytes, 0, len);
					SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
					IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes("UTF-8"));
					cipher.init(Cipher.ENCRYPT_MODE,keySpec,ivSpec);

					byte[] results = cipher.doFinal(text.getBytes("UTF-8"));
					BASE64Encoder encoder = new BASE64Encoder();
					return encoder.encode(results);
					}
			
			
					public String Decrypt(String text) throws Exception{
						 String iv = "!QAZ2WSX#EDC4RFV";
						    String key = "5TGB&YHN7UJM(IK<";
					Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
					byte[] keyBytes= new byte[16];
					byte[] b= key.getBytes("UTF-8");
					int len= b.length;
					if (len > keyBytes.length) len = keyBytes.length;
					System.arraycopy(b, 0, keyBytes, 0, len);
					SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
					IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes("UTF-8"));
					cipher.init(Cipher.DECRYPT_MODE,keySpec,ivSpec);

					
					BASE64Decoder decoder = new BASE64Decoder();
					byte [] results = cipher.doFinal(decoder.decodeBuffer(text));
					String datas=new String(results,"UTF-8");
					char [] schars=datas.toCharArray();
					StringBuilder news=new StringBuilder();
					int ccount=0;
					for(int count=0;count<schars.length;count++)
					{
						 int val= CharToASCII(schars[count]);
						
						if(val!=0){
							if(ccount>1){
							ccount=0;
						news.append(schars[count]);
							}else{
								news.append(schars[count]);ccount=0;
							}
							
						}
						else{
							ccount++;
							
						}
					}
					return news.toString();
					}
			
					public  int CharToASCII(char character){
						return (int)character;
					}


}
