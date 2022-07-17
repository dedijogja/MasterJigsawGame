
package us.masterjigsawgame.core;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

import us.masterjigsawgame.util.AppConstants;


public class BaseActivity extends AppCompatActivity {

	private AlertDialog mErrorDialog;
	private static ArrayList<Activity> mActivitiesList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		if (!(this instanceof GameActivity)) {
//			if (!MainCore.isTablet) {
//				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//			}
//		}
		if (mActivitiesList == null) {
			mActivitiesList = new ArrayList<>();
		}
		mActivitiesList.add(this);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean(AppConstants.EXTRA_IS_DIALOG_SHOWN, false);
	}

	@Override
	protected void onDestroy() {
		if (mErrorDialog != null) {
			mErrorDialog.cancel();
			mErrorDialog = null;
		}
		if (mActivitiesList != null && mActivitiesList.contains(this)) {
			mActivitiesList.remove(this);
		}
		super.onDestroy();
	}

}
