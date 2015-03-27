package aksha.addmandi;

import java.util.ArrayList;
import java.util.HashMap;

import aksha.adapters.Myspinner;
import com.aksha.mosaic.R;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class AddMandi extends Fragment {

	private Spinner dist,mandi;
	private EditText mandiname;
	private Button submit;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View root = inflater.inflate(R.layout.addmandi, container, false);
		
		mandiname = (EditText)root.findViewById(R.id.mandiname);
		dist = (Spinner)root.findViewById(R.id.dist);
		mandi = (Spinner)root.findViewById(R.id.mandi);
		submit = (Button)root.findViewById(R.id.submit);
		
		ArrayList<HashMap<String, String>> category1=new ArrayList<HashMap<String,String>>();
		HashMap<String, String> map1=new HashMap<String, String>();
			map1.put("1","");
			map1.put("2","Select District");
			category1.add(map1);
			
		Myspinner[] redemo1 = new Myspinner[category1.size()];
		for(int i=0; i<category1.size(); i++){
			redemo1[i] = new Myspinner(category1.get(i).get("2"), category1.get(i).get("1"), "");
		}
		ArrayAdapter<Myspinner> adapter1 = new ArrayAdapter<Myspinner>(getActivity(), android.R.layout.simple_spinner_item, redemo1);
		dist.setAdapter(adapter1);
		
		ArrayList<HashMap<String, String>> category2=new ArrayList<HashMap<String,String>>();
		HashMap<String, String> map2=new HashMap<String, String>();
			map2.put("1","");
			map2.put("2","Mandi List");
			category2.add(map2);
			
		Myspinner[] redemo2 = new Myspinner[category2.size()];
		for(int i=0; i<category2.size(); i++){
			redemo2[i] = new Myspinner(category2.get(i).get("2"), category2.get(i).get("1"), "");
		}
		ArrayAdapter<Myspinner> adapter2 = new ArrayAdapter<Myspinner>(getActivity(), android.R.layout.simple_spinner_item, redemo2);
		mandi.setAdapter(adapter2);
		
		submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		
		submit.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getActivity(), "Add Mandi", Toast.LENGTH_LONG).show();
				return true;
			}
		});
		
		return root;
	}
	
}
