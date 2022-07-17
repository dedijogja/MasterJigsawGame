
package us.masterjigsawgame.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.startapp.android.publish.ads.splash.SplashConfig;
import com.startapp.android.publish.adsCommon.StartAppAd;
import com.startapp.android.publish.adsCommon.StartAppSDK;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import us.masterjigsawgame.R;
import us.masterjigsawgame.asyncronus.SingleGameDecryption;
import us.masterjigsawgame.core.BaseActivity;
import us.masterjigsawgame.core.MainCore;
import us.masterjigsawgame.util.Constant;
import us.masterjigsawgame.util.Komunikator;


public class MenuActivity extends BaseActivity {
	private Context mContext;
	private String[] semuaKonten;
    private final int kodeReqGallery = 0;
    private final int kodereqCamera = 1;
    private  Dialog dialog;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_activity_layout);
		mContext = MenuActivity.this;

        if(getIntent().getStringExtra("perintah")!=null){
            tampilkanDialogPilihanGalleryAtauCamera();
        }

        final MainCore applications = (MainCore) getApplication();
        String status = applications.getStatusIklan();
        if(status.equals(Constant.GAGAL_LOAD_IKLAN)) {
            StartAppSDK.init(this, new Komunikator(this).getStartAppId(),false);
            if (!isNetworkConnected()) {
                StartAppAd.disableSplash();
            }else{
                StartAppAd.showSplash(this, savedInstanceState,
                        new SplashConfig()
                                .setTheme(SplashConfig.Theme.USER_DEFINED)
                                .setCustomScreen(R.layout.splash_layout)
                );
            }
        }

        if(status.equals(Constant.BERHASIL_LOAD_IKLAN)){
            AdView adView = new AdView(this);
            adView.setAdSize(AdSize.MEDIUM_RECTANGLE);
            adView.setAdUnitId(new Komunikator(this).getAdBanner());
            adView.loadAd(new AdRequest.Builder().build());
            ((LinearLayout) findViewById(R.id.adMenu)).addView(adView);
        }

		try {
			semuaKonten = Constant.LIST_SEMUA_KONTEN(this);
		} catch (IOException e) {
			e.printStackTrace();
		}

		SingleGameDecryption singleGameDecryption = new SingleGameDecryption(this);
		singleGameDecryption.setListenerDecrypt(new SingleGameDecryption.ListenerDecrypt() {
			@Override
			public void onSelesaiDecrypt(byte[] hasilDeskripsi) {
				findViewById(R.id.areaBgRandom).setBackground(new BitmapDrawable(getResources(),
						BitmapFactory.decodeByteArray(hasilDeskripsi, 0, hasilDeskripsi.length)));
			}
		});
		singleGameDecryption.execute(semuaKonten[new Random().nextInt(semuaKonten.length)]);

	}

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

	private View.OnClickListener dengar = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.pickfromcamera:
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, kodereqCamera);
                    break;
                case R.id.choosefromgallery:
                    Intent views = new Intent("android.intent.action.PICK", MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    views.setType("image/*");
                    startActivityForResult(views, kodeReqGallery);
                    break;
            }
        }
    };

	private void tampilkanDialogPilihanGalleryAtauCamera(){
        if(dialog == null) {
            dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
            LayoutInflater layoutInflater = this.getLayoutInflater();
            View dialogv = layoutInflater.inflate(R.layout.choose_camera_gallery_dialog, null);
            dialogv.findViewById(R.id.pickfromcamera).setOnClickListener(dengar);
            dialogv.findViewById(R.id.choosefromgallery).setOnClickListener(dengar);
            dialog.setContentView(dialogv);
        }
        dialog.show();
	}


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(dialog!=null){
            dialog.dismiss();
        }
        if(resultCode == RESULT_OK){
           switch (requestCode){
               case kodereqCamera: {
                   Bitmap bmp = (Bitmap) data.getExtras().get("data");
                   ByteArrayOutputStream stream = new ByteArrayOutputStream();
                   assert bmp != null;
                   bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                   Constant.CUSTOM_BYTE = stream.toByteArray();
                   Intent intentCamera = new Intent(MenuActivity.this, GameActivity.class);
                   intentCamera.putExtra(Constant.KODE_INDEX, Constant.KODE_INDEX_CUSTOM);
                   startActivity(intentCamera);
                   break;
               }
               case kodeReqGallery: {
                   Uri selectedImage = data.getData();
                   InputStream imageStream;
                   byte[] inputData = null;
                   try {
                       imageStream = getContentResolver().openInputStream(selectedImage);
                       inputData = getBytes(imageStream);
                   } catch (IOException e) {
                       e.printStackTrace();
                   }
                   Constant.CUSTOM_BYTE = inputData;
                   Intent intentGallery = new Intent(MenuActivity.this, GameActivity.class);
                   intentGallery.putExtra(Constant.KODE_INDEX, Constant.KODE_INDEX_CUSTOM);
                   startActivity(intentGallery);
                   break;
               }
           }
        }
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    public void viewClickHandler(View view) {
		switch (view.getId()) {
		case R.id.btnCollections:
			Intent intent = new Intent(mContext, ChooseCollections.class);
			startActivity(intent);
			break;
		case R.id.btnSetting:
			Intent settingIntent = new Intent(mContext, SettingsActivity.class);
			startActivity(settingIntent);
			break;
		case R.id.btnScore:
			Intent scoreIntent = new Intent(mContext, ScoreActivity.class);
			startActivity(scoreIntent);
			break;
        case R.id.btnCustomPuzzle:
            tampilkanDialogPilihanGalleryAtauCamera();
            break;
		}
	}

	@Override
	protected void onDestroy() {
		if (mContext != null) {
			mContext = null;
			super.onDestroy();
		}
	}

    @Override
    public void onBackPressed() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Exit Confirmation");
        alertDialog.setMessage("Are you sure you want to close this game?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
}
