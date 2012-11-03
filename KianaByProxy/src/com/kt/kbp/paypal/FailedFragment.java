package com.kt.kbp.paypal;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;
import com.kt.kbp.R;
import com.kt.kbp.common.Constants;
import com.kt.kbp.path.Path;
import com.kt.kbp.path.PathInterface;

public class FailedFragment extends Fragment implements PathInterface {

	private View view;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	super.onCreateView(inflater, container, savedInstanceState);
    	view = inflater.inflate(R.layout.fragment_failed, container, false);
    	
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
				GoogleAnalyticsTracker.getInstance().trackEvent("Paypal", "Donate", "From Failed", 0);
				Fragment fragment = getFragmentManager().findFragmentByTag("donate");
				if (fragment == null) {
					fragment = new PaypalFragment();
				}
				FragmentTransaction transaction = getFragmentManager().beginTransaction();
				transaction.replace(Constants.CONTENT_VIEW_ID, fragment);
				transaction.addToBackStack(null);
				transaction.commit();
			}
		});
        
    	return view;
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	Log.i("fragments", "resuming FailedFragment");
    }
    
	@Override
	public Path getPath() {
		return Path.FAILED;
	}
}
