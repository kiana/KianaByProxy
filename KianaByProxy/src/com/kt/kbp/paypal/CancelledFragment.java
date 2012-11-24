package com.kt.kbp.paypal;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kt.kbp.R;
import com.kt.kbp.common.Constants;
import com.kt.kbp.googleanalytics.GoogleAnalyticsFragment;

public class CancelledFragment extends GoogleAnalyticsFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	super.onCreateView(inflater, container, savedInstanceState);
    	View view = inflater.inflate(R.layout.fragment_cancelled, container, false);
    	
        TextView back = (TextView) view.findViewById(R.id.back_to_main);
        back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getFragmentManager().popBackStack(Constants.MAIN_FRAG, 0);
			}
		});
        
        TextView donate = (TextView) view.findViewById(R.id.donate);
        donate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				trackEvent("Paypal", "Donate", "From Cancelled", 0);
				
				Fragment fragment = getFragmentManager().findFragmentByTag("donate");
				if (fragment == null) {
					fragment = new PaypalFragment();
				}
				FragmentTransaction transaction = getFragmentManager().beginTransaction();
				transaction.replace(R.id.fragment_frame, fragment);
				transaction.addToBackStack(null);
				transaction.commit();
			}
		});
        
    	return view;
    }
    
}
