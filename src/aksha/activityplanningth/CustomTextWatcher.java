package aksha.activityplanningth;

import java.text.DecimalFormat;
import java.util.ArrayList;

import aksha.adapters.Myspinner;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class CustomTextWatcher implements TextWatcher {
    private AutoCompleteTextView mEditText;
Context mContext;
    public CustomTextWatcher(Context cnt, AutoCompleteTextView e) { 
        mEditText = e;
        mContext=cnt;
    }

    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    public void afterTextChanged(Editable s) {
    	
    	 String val=String.valueOf( s);
    	 if(!val.equalsIgnoreCase("")&&val.length()>=0){
    		 
    		 ArrayList<Myspinner> finaldata=new ArrayList<Myspinner>();
    		 ArrayList<Myspinner> data=(ArrayList<Myspinner>)mEditText.getTag();
    		 for(int icount=0;icount<data.size();icount++)
				{
					Myspinner spsp=data.get(icount);
					if(String.valueOf(spsp.getSpinnerText()).toLowerCase().startsWith(val.toLowerCase()))
					{
						finaldata.add(spsp);
					}
					
				}
    		 AdapterSearch adap=new AdapterSearch(mContext,android.R.layout.select_dialog_item,finaldata);
    		 mEditText.setAdapter(adap);
    		 mEditText.showDropDown();
    		 //mEditText.n
    		 
    	 }
    	 
		     
    	 
    }
}
