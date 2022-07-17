
package us.masterjigsawgame.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.startapp.android.publish.ads.banner.Banner;
import com.startapp.android.publish.adsCommon.StartAppAd;

import java.io.IOException;

import us.masterjigsawgame.R;
import us.masterjigsawgame.adaper.ChooseCollectionsAdapter;
import us.masterjigsawgame.core.BaseActivity;
import us.masterjigsawgame.core.MainCore;
import us.masterjigsawgame.util.Constant;
import us.masterjigsawgame.util.Komunikator;

import static android.R.attr.data;


public class ChooseCollections extends BaseActivity {

	private Context mContext;
	private GridView grid_view;
	private ChooseCollectionsAdapter mAllGridAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choose_favorite_photos_layout);

        MainCore ndewoDewe = (MainCore) getApplication();
        String status = ndewoDewe.getStatusIklan();
        if(status.equals(Constant.BERHASIL_LOAD_IKLAN)){
            LinearLayout adContainer = (LinearLayout) findViewById(R.id.adGameList);
            AdView adView = new AdView(this);
            adView.setAdSize(AdSize.SMART_BANNER);
            adView.setAdUnitId(new Komunikator(this).getAdBanner());
            adView.loadAd(new AdRequest.Builder().build());
            adContainer.addView(adView);
        }else{
            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.adGameList);
            Banner startAppBanner = new Banner(this);
            linearLayout.addView(startAppBanner);
        }


        mContext = this;
		initViews();
		populateGridViewContents();
	}

	private void populateGridViewContents() {
		if (mAllGridAdapter == null) {
			try {
				mAllGridAdapter = new ChooseCollectionsAdapter(mContext, Constant.LIST_SEMUA_KONTEN(this));
			} catch (IOException e) {
				e.printStackTrace();
			}
			grid_view.setAdapter(mAllGridAdapter);
		}
	}


	private void initViews() {
		grid_view = (GridView) findViewById(R.id.grid_view);
		grid_view.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, final int position, long arg3) {
                final MainCore app = (MainCore) getApplication();
                String status = app.getStatusIklan();
                if (status.equals(Constant.BERHASIL_LOAD_IKLAN)) {
                    if (!app.isBolehMenampilkanIklanHitung() || !app.isBolehMenampilkanIklanWaktu()
                            || !app.getInterstitial().isLoaded()) {
                        startAktifitas(position);
                    }
                    app.getInterstitial().setAdListener(new AdListener() {
                        @Override
                        public void onAdClosed() {
                            startAktifitas(position);
                            app.loadIntersTitial();
                            super.onAdClosed();
                        }

                        @Override
                        public void onAdFailedToLoad(int i) {
                            if (app.getHitungFailed() < 2) {
                                app.loadIntersTitial();
                                app.setHitungFailed();
                            }
                            super.onAdFailedToLoad(i);
                        }

                        @Override
                        public void onAdLoaded() {
                            app.setHitungFailed(0);
                            super.onAdLoaded();
                        }
                    });
                    app.tampilkanInterstitial();
                } else {
                    if (app.getPenghitungStartApp() == 0) {
                        app.setPenghitungStartApp(1);
                        startAktifitas(position);
                        StartAppAd.showAd(mContext);
                    } else {
                        app.setPenghitungStartApp(0);
                        startAktifitas(position);
                    }
                }
			}
		});
	}

	private void startAktifitas(int position){
        Intent intent = new Intent(mContext, GameActivity.class);
        intent.putExtra(Constant.KODE_INDEX, String.valueOf(position));
        startActivity(intent);
    }

	@Override
	public void onDestroy() {
		if (mContext != null) {
			mContext = null;
			mAllGridAdapter = null;
			super.onDestroy();
		}
	}

	@Override
	public void onBackPressed() {
		finish();
	}
}
