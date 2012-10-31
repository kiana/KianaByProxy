package com.kt.kbp.paypal;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kt.kbp.R;
import com.kt.kbp.common.Constants;
import com.kt.kbp.path.Path;
import com.kt.kbp.path.PathInterface;

public class SucceededFragment extends Fragment implements PathInterface {

	private View view;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	super.onCreateView(inflater, container, savedInstanceState);
    	view = inflater.inflate(R.layout.fragment_succeeded, container, false);
        
        TextView backToMain = (TextView)view.findViewById(R.id.back_from_success);
        backToMain.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getFragmentManager().popBackStack(Constants.MAIN_FRAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
			}
		});
        
    	return view;
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	Log.i("fragments", "resuming SucceededFragment");
    }
    
    
	@Override
	public Path getPath() {
		return Path.SUCCEEDED;
	}
    
}
