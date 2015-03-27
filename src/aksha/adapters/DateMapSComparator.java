package aksha.adapters;


import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;

public class DateMapSComparator implements Comparator<Map<String, String>>{
    private final String key;

    public DateMapSComparator(String key){
        this.key = key;
    }

    public int compare(Map<String, String> first,
                       Map<String, String> second){
        // TODO: Null checking, both for maps and values
    	
        String firstValue = first.get(key);
        String secondValue = second.get(key);
        Date formattedDate3 = new Date();
        Date formattedDate4 = new Date();
        try{                              
        SimpleDateFormat df4 = new SimpleDateFormat("MM/dd/yyyy");    
         formattedDate3 = df4.parse(firstValue);
         SimpleDateFormat df5 = new SimpleDateFormat("MM/dd/yyyy");    
         formattedDate4=df5.parse(secondValue);
        }catch(Exception er){er.getMessage();}
        //return firstValue.compareTo(secondValue);
        return formattedDate4.compareTo(formattedDate3);
    }
}

