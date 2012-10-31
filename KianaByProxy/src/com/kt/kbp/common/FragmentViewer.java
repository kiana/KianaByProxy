package com.kt.kbp.common;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.kt.kbp.R;

public class FragmentViewer {

	public static void showFragment(FragmentManager manager, String tag, int id) {
		showFragment(manager, tag, id, null);
	}
	
	public static void showFragment(FragmentManager manager, String tag, int id, Bundle bundle) {
		Fragment fragment = manager.findFragmentByTag(tag);
		FragmentTransaction transaction = manager.beginTransaction();
		if (fragment == null) {
			fragment = FragmentFactory.getNewFragment(id);
		}

		transaction.replace(R.id.fragment_frame, fragment, tag);
		transaction.commit();
	}
	
}
