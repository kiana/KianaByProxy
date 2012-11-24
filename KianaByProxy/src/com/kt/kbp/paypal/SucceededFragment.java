package com.kt.kbp.paypal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kt.kbp.R;
import com.kt.kbp.common.Constants;
import com.kt.kbp.googleanalytics.GoogleAnalyticsFragment;

public class SucceededFragment extends GoogleAnalyticsFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	super.onCreateView(inflater, container, savedInstanceState);
    	View view = inflater.inflate(R.layout.fragment_succeeded, container, false);
        
        TextView backToMain = (TextView)view.findViewById(R.id.back_from_success);
        backToMain.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getFragmentManager().popBackStack(Constants.MAIN_FRAG, 0);
			}
		});
        
    	return view;
    }
}
