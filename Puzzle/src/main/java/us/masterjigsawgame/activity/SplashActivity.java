package us.masterjigsawgame.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.ads.AdListener;

import us.masterjigsawgame.R;
import us.masterjigsawgame.core.MainCore;
import us.masterjigsawgame.util.Constant;


public class SplashActivity extends AppCompatActivity {
	private boolean statusIklan = true;
	int hitung = 0;
	int loadIklanBerapaKali = 5;
	private MainCore mainCore;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_layout);

		((ShimmerFrameLayout) findViewById(R.id.shimmer_view_container1)).startShimmerAnimation();
		mainCore = (MainCore) getApplication();
		mainCore.initInterstitial();
		mainCore.loadIntersTitial();
		mainCore.getInterstitial().setAdListener(new AdListener() {
			@Override
			public void onAdFailedToLoad(int i) {
				hitung++;
				//Log.d("iklan", "gagal "+ String.valueOf(hitung));
				if(hitung<loadIklanBerapaKali){
					if(statusIklan) {
						mainCore.loadIntersTitial();
					}
				}
				if(hitung == loadIklanBerapaKali){
					if(statusIklan) {
						statusIklan = false;
						mainCore.setStatusIklan(Constant.GAGAL_LOAD_IKLAN);
						bukaActivity();
					}
				}
				super.onAdFailedToLoad(i);
			}

			@Override
			public void onAdLoaded() {
				if(statusIklan) {
					//Log.d("iklan", "berhasil");
					statusIklan = false;
					mainCore.setStatusIklan(Constant.BERHASIL_LOAD_IKLAN);
					bukaActivity();
				}
				super.onAdLoaded();
			}
		});

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				if(statusIklan) {
					statusIklan = false;
					mainCore.setStatusIklan(Constant.GAGAL_LOAD_IKLAN);
					bukaActivity();
				}
			}
		}, 15000);
	}

	private void bukaActivity(){
		Intent intent = new Intent(SplashActivity.this, MenuActivity.class);
		startActivity(intent);
		finish();
	}

	@Override
	public void onBackPressed() {

	}
}
