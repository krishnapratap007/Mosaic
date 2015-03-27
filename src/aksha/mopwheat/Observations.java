package aksha.mopwheat;

import java.util.ArrayList;
import java.util.HashMap;

import aksha.adapters.Myspinner;
import com.aksha.mosaic.R;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;

public class Observations extends Fragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View root = inflater.inflate(R.layout.observations_wheat, container, false);
		
		final Spinner farmernameslistspinner = (Spinner)root.findViewById(R.id.farmernameslistspinner);
		final Spinner villagenameslistspinner = (Spinner)root.findViewById(R.id.villagenameslistspinner);
		final Spinner days = (Spinner)root.findViewById(R.id.days);
		
		ArrayList<HashMap<String, String>> category1=new ArrayList<HashMap<String,String>>();
		HashMap<String, String> map1=new HashMap<String, String>();
			map1.put("1","");
			map1.put("2","Select Farmer");
			category1.add(map1);
			
		Myspinner[] redemo1 = new Myspinner[category1.size()];
		for(int i=0; i<category1.size(); i++){
			redemo1[i] = new Myspinner(category1.get(i).get("2"), category1.get(i).get("1"), "");
		}
		ArrayAdapter<Myspinner> adapter1 = new ArrayAdapter<Myspinner>(getActivity(), android.R.layout.simple_spinner_item, redemo1);
		farmernameslistspinner.setAdapter(adapter1);
		
		ArrayList<HashMap<String, String>> category2=new ArrayList<HashMap<String,String>>();
		HashMap<String, String> map2=new HashMap<String, String>();
			map2.put("1","");
			map2.put("2","Select Village");
			category2.add(map2);
			
		Myspinner[] redemo2 = new Myspinner[category2.size()];
		for(int i=0; i<category2.size(); i++){
			redemo2[i] = new Myspinner(category2.get(i).get("2"), category2.get(i).get("1"), "");
		}
		ArrayAdapter<Myspinner> adapter2 = new ArrayAdapter<Myspinner>(getActivity(), android.R.layout.simple_spinner_item, redemo2);
		villagenameslistspinner.setAdapter(adapter2);
		
		ArrayList<HashMap<String, String>> category3=new ArrayList<HashMap<String,String>>();
		HashMap<String, String> map3=new HashMap<String, String>();
			map3.put("1","");
			map3.put("2","Days After Sowing");
			category3.add(map3);
			
		Myspinner[] redemo3 = new Myspinner[category3.size()];
		for(int i=0; i<category3.size(); i++){
			redemo3[i] = new Myspinner(category3.get(i).get("2"), category3.get(i).get("1"), "");
		}
		ArrayAdapter<Myspinner> adapter3 = new ArrayAdapter<Myspinner>(getActivity(), android.R.layout.simple_spinner_item, redemo3);
		days.setAdapter(adapter3);
		
		return root;
	}

}
