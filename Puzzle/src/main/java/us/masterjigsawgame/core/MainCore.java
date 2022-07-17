package us.masterjigsawgame.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Handler;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import us.masterjigsawgame.util.AppConstants;
import us.masterjigsawgame.util.AppUtils;
import us.masterjigsawgame.util.Constant;
import us.masterjigsawgame.util.Komunikator;


public class MainCore extends Application {

	private InterstitialAd interstitial;

	public void setHitungFailed() {
		this.hitungFailed++;
	}

	public void setHitungFailed(int hitungFailed) {
		this.hitungFailed = hitungFailed;
	}

	int hitungFailed = 0;

	public int getHitungFailed() {
		return hitungFailed;
	}


	public String getStatusIklan() {
		return statusIklan;
	}

	private String statusIklan = Constant.GAGAL_LOAD_IKLAN;

	public int getPenghitungStartApp() {
		return penghitungStartApp;
	}

	public void setPenghitungStartApp(int penghitungStartApp) {
		this.penghitungStartApp = penghitungStartApp;
	}

	private int penghitungStartApp = 0;

	public void setStatusIklan(String status){
		statusIklan = status;
	}

	public void initInterstitial(){
		interstitial = new InterstitialAd(this);
		interstitial.setAdUnitId(new Komunikator(this).getAdInterstitial());
	}


	public void loadIntersTitial(){
		//Log.d("iklan", "load interstitial");
		AdRequest adRequest = new AdRequest.Builder().build();
		interstitial.loadAd(adRequest);
	}

	public InterstitialAd getInterstitial(){
		return interstitial;
	}

	public boolean isBolehMenampilkanIklanWaktu() {
		return bolehMenampilkanIklanWaktu;
	}

	boolean bolehMenampilkanIklanWaktu = true;

	public boolean isBolehMenampilkanIklanHitung() {
		return bolehMenampilkanIklanHitung;
	}

	boolean bolehMenampilkanIklanHitung = true;
	public void tampilkanInterstitial(){
		if(bolehMenampilkanIklanWaktu && bolehMenampilkanIklanHitung) {
			if(interstitial.isLoaded()) {
				interstitial.show();
				setHitungFalse();
				manajemenWaktu();
			}else {
				loadIntersTitial();
			}
		}else{
			if(!bolehMenampilkanIklanHitung) {
				setHitungTrue();
			}
		}
	}
	private void manajemenWaktu(){
		bolehMenampilkanIklanWaktu =  false;
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				// Log.d("iklan", "waktu boleh");
				bolehMenampilkanIklanWaktu = true;
			}
		}, 120000);
	}

	public void setHitungFalse(){
		bolehMenampilkanIklanHitung = false;
	}

	public void setHitungTrue(){
		//Log.d("iklan", "hitung boleh");
		bolehMenampilkanIklanHitung = true;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		copyDatabaseFromAssetToLocal();
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}

	private void copyDatabaseFromAssetToLocal() {
		try {
			File dir;
			if(Build.VERSION.SDK_INT >= 17){
				dir =new File(getApplicationInfo().dataDir + "/databases/");
			}else{
				dir =  new File("/data/data/" + getPackageName() + "/databases");
			}
			if (!dir.exists()) {
				dir.mkdir();
				File file = new File(dir + "/" + "photopuzzle.db");
				if (!file.exists()) {
					file.createNewFile();
				}
				InputStream source = getResources().getAssets().open(
						"photopuzzle.sqlite");
				String databasePath = file.getAbsolutePath();
				OutputStream out = new FileOutputStream(databasePath);

				byte[] buf = new byte[1024];
				int len;
				while ((len = source.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				source.close();
				out.close();
			}
		} catch (Exception e) {
		}
	}
}
