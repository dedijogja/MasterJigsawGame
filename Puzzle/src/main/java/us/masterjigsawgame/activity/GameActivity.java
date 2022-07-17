package us.masterjigsawgame.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.startapp.android.publish.ads.banner.Banner;

import java.io.IOException;
import java.util.Random;

import us.masterjigsawgame.R;
import us.masterjigsawgame.asyncronus.SingleGameDecryption;
import us.masterjigsawgame.core.BaseActivity;
import us.masterjigsawgame.core.MainCore;
import us.masterjigsawgame.customview.PuzzleView;
import us.masterjigsawgame.database.ScoresDataProvider;
import us.masterjigsawgame.model.ScoreItem;
import us.masterjigsawgame.util.AppConstants;
import us.masterjigsawgame.util.AppUtils;
import us.masterjigsawgame.util.Constant;
import us.masterjigsawgame.util.Komunikator;
import us.masterjigsawgame.util.SettingsPreferences;


public class GameActivity extends BaseActivity implements OnKeyListener {

	private Context mContext;
	private PuzzleView mPuzzleView;
	private Chronometer mTimerView;
	private long mTime;
	private int movesCount;
	private boolean mIsHintOn;
	private boolean isWin;
	private boolean isHintTaken;
	private TextView mTitleTextView;
	private TextView moves_textview;
	private ImageView mCompleteView;
	private ImageView tabs_bar5_checkbox;
	private ImageView undo_imageview;
	private ImageView play_pause_imageview;
	private ImageView empty_view;
	private ActionBrodcastListener actionBroadcastReceiver;
	private Dialog mCustomDialog;
    private String[] semuaPuzzle;

    private byte[] byteku;
    private ProgressDialog progressDialog;


	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.slide_puzzle);

		mIsHintOn = SettingsPreferences.getHintEnableDisable(this);

		MainCore ndewoDewe = (MainCore) getApplication();
		String status = ndewoDewe.getStatusIklan();
		if(status.equals(Constant.BERHASIL_LOAD_IKLAN)){
			LinearLayout adContainer = (LinearLayout) findViewById(R.id.adGame);
			AdView adView = new AdView(this);
			adView.setAdSize(AdSize.SMART_BANNER);
			adView.setAdUnitId(new Komunikator(this).getAdBanner());
			adView.loadAd(new AdRequest.Builder().build());
			adContainer.addView(adView);
		}else{
			LinearLayout linearLayout = (LinearLayout) findViewById(R.id.adGame);
			Banner startAppBanner = new Banner(this);
			linearLayout.addView(startAppBanner);
		}

        mContext = this;
        try {
            semuaPuzzle = Constant.LIST_SEMUA_KONTEN(this);
        } catch (IOException e) {
            e.printStackTrace();
        }


        progressDialog =  ProgressDialog.show(this, "Loading",
                "Please wait ...", true);
        progressDialog.show();

        new CountDownTimer(1500, 1000) {
            public void onTick(long millisUntilFinished) {

            }
            public void onFinish() {
                progressDialog.dismiss();
                if(getIntent().getStringExtra(Constant.KODE_INDEX)!=null){
                    if(!getIntent().getStringExtra(Constant.KODE_INDEX).equals(Constant.KODE_INDEX_CUSTOM)) {
                        SingleGameDecryption singleGameDecryption = new SingleGameDecryption(mContext);
                        singleGameDecryption.setListenerDecrypt(new SingleGameDecryption.ListenerDecrypt() {
                            @Override
                            public void onSelesaiDecrypt(byte[] hasilDeskripsi) {
                                byteku = hasilDeskripsi;
                                initViews(byteku);
                                startNewgame();
                                updateShowHintCheckbox();
                                updateTitle();
                                updateTileView();
                            }
                        });
                        singleGameDecryption.execute(semuaPuzzle[Integer.parseInt(getIntent().getStringExtra(Constant.KODE_INDEX))]);
                    }else{
                        byteku = Constant.CUSTOM_BYTE;
                        initViews(byteku);
                        startNewgame();
                        updateShowHintCheckbox();
                        updateTitle();
                        updateTileView();
                    }
                }
            }
        }.start();
	}

	private void initViews(byte[]bitmapArray) {
		empty_view = (ImageView) findViewById(R.id.empty_view);
		moves_textview = (TextView) findViewById(R.id.moves_textview);
		undo_imageview = (ImageView) findViewById(R.id.undo_imageview);
		play_pause_imageview = (ImageView) findViewById(R.id.play_pause_imageview);
		tabs_bar2_view2 = (RelativeLayout) findViewById(R.id.tabs_bar2_view2);
		actionBroadcastReceiver = new ActionBrodcastListener();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(AppConstants.UPDATE_DIFFICULTY_LEVEL);
		registerReceiver(actionBroadcastReceiver, intentFilter);
		tabs_bar5_checkbox = (ImageView) findViewById(R.id.tabs_bar5_checkbox);
		mTitleTextView = (TextView) findViewById(R.id.title_textview);
		mPuzzleView = (PuzzleView) findViewById(R.id.tile_view);
		mPuzzleView.requestFocus();
		mPuzzleView.setOnKeyListener(this);
        mPuzzleView.setmByteArray(bitmapArray);
		mCompleteView = (ImageView) findViewById(R.id.complete_view);
		mTimerView = (Chronometer) findViewById(R.id.timer_view);

		empty_view.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});
	}

	public class ActionBrodcastListener extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(AppConstants.UPDATE_DIFFICULTY_LEVEL)) {
				updateTileView();
				//loadNextPhoto();
               // reloadGame();
				startNewgame();

			}
		}
	}

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			mHandler.removeMessages(1);
			empty_view.setVisibility(View.GONE);
			String winMessege = " " + movesCount + " "
					+ getString(R.string.moves).toLowerCase() + " "
					+ getString(R.string.and) + " "
					+ mTimerView.getText().toString() + " "
					+ getString(R.string.time).toLowerCase();

            showWinDialog(getString(R.string.win),
                    getString(R.string.congrates) + winMessege,
                    getString(R.string.next), getString(R.string.retry));

		}
	};

	private AnimationListener mCompleteAnimListener = new AnimationListener() {
		@Override
		public void onAnimationEnd(Animation animation) {
			mPuzzleView.setVisibility(View.GONE);
			mHandler.sendEmptyMessageDelayed(1, AppConstants.WIN_DIALOG_DURATION);
		}
		@Override
		public void onAnimationRepeat(Animation animation) {
		}

		@Override
		public void onAnimationStart(Animation animation) {

		}
	};

	private void updateMovesText() {
		moves_textview.setText(String.valueOf(movesCount));
	}

	private void loadNextPhoto() {
		mPuzzleView.setVisibility(View.VISIBLE);
		mCompleteView.setVisibility(View.GONE);
        SingleGameDecryption singleGameDecryption = new SingleGameDecryption(mContext);
        singleGameDecryption.setListenerDecrypt(new SingleGameDecryption.ListenerDecrypt() {
            @Override
            public void onSelesaiDecrypt(byte[] hasilDeskripsi) {
                byteku = hasilDeskripsi;
                mPuzzleView.setmByteArray(byteku);
                startNewgame();
            }
        });
        singleGameDecryption.execute(semuaPuzzle[new Random().nextInt(semuaPuzzle.length)]);
	}

	private void updateTileView() {
		mPuzzleView.updateInstantPrefs();
		mTimerView.setBase(SystemClock.elapsedRealtime() - mTime);
		if (!mPuzzleView.isSolved()) {
			mTimerView.start();
		}
	}

	private void updateShowHintCheckbox() {
		if (mIsHintOn) {
			tabs_bar5_checkbox.setImageResource(R.drawable.hint);
		} else {
			tabs_bar5_checkbox.setImageResource(R.drawable.hint);
		}
	}


	private void updateTitle() {
		int level = SettingsPreferences.getDifficultyLevel(mContext);
		if (AppConstants.DIFFICULTY_LEVEL_EASY == level) {
			mTitleTextView.setText("Easy");
		} else if (AppConstants.DIFFICULTY_LEVEL_MEDIUM == level) {
			mTitleTextView.setText("Medium");
		} else if (AppConstants.DIFFICULTY_LEVEL_HARD == level) {
			mTitleTextView.setText("Hard");
		}
	}

	private RelativeLayout tabs_bar2_view2;
	private boolean isPaused;

	private void updateUndoStatus() {
		if (movesCount > 0) {
			undo_imageview.setImageResource(R.drawable.back_step_on);
			tabs_bar2_view2.setClickable(true);
		} else {
			undo_imageview.setImageResource(R.drawable.back_step_off);
			tabs_bar2_view2.setClickable(false);
		}
	}

	private void updatePlayPauseStatus() {
		if (isPaused) {
			play_pause_imageview.setImageResource(R.drawable.play);
		} else {
			play_pause_imageview.setImageResource(R.drawable.pause);
		}
	}

	public void viewClickHandler(View view) {
		switch (view.getId()) {
		case R.id.tabs_bar2_view2:
			if (SettingsPreferences.getVibration(mContext)) {
				AppUtils.vibrate(mContext, AppConstants.VIBRATION_DURATION);
			}
			movesCount = movesCount - 1;
            mPuzzleView.doBackStepGame(mPuzzleView.getBackStep(movesCount), mTimerView);
			updateMovesText();
			updateUndoStatus();
			break;
		case R.id.tabs_bar2_view1:
			isPaused = !isPaused;
			updatePlayPauseStatus();
			if (isPaused) {
				pauseTimer();
			} else {
				updateTileView();
			}
			break;
		case R.id.tabs_bar_view3:
			if (isWin) {
				isWin = false;
				loadNextPhoto();
			} else {
				reloadGame();
			}
			break;
		case R.id.tabs_bar_view4:
			Intent intent = new Intent(mContext, SettingsActivity.class);
			intent.putExtra(AppConstants.EXTRA_PROCESSING_GAME_STRING, true);
			startActivityForResult(intent, AppConstants.REQUEST_CODE_SETTINGS);
			break;
		case R.id.tabs_bar_view5:
			if (isWin) {
				isWin = false;
				mPuzzleView.setVisibility(View.VISIBLE);
				mCompleteView.setVisibility(View.GONE);
				mIsHintOn = !mIsHintOn;
				SettingsPreferences.setHintEnableDisable(mContext, mIsHintOn);
				updateShowHintCheckbox();
				mPuzzleView.updateHintStatus();
				startNewgame();
			} else {
				mIsHintOn = !mIsHintOn;
				SettingsPreferences.setHintEnableDisable(mContext, mIsHintOn);
				updateShowHintCheckbox();
				mPuzzleView.updateHintStatus();
				if (!isHintTaken) {
					isHintTaken = SettingsPreferences
							.getHintEnableDisable(mContext);
				}
			}
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
        updateTitle();
        if(!isPaused) {
            updateTileView();
        }
	}

	private void startNewgame() {
		empty_view.setVisibility(View.GONE);
		isHintTaken = SettingsPreferences.getHintEnableDisable(mContext);
		movesCount = 0;
		updateMovesText();
		isPaused = false;
		updatePlayPauseStatus();
		updateUndoStatus();
		mTime = 0;
		mTimerView.setBase(SystemClock.elapsedRealtime() - mTime);
		mPuzzleView.newGame(null,  mTimerView);
		mTimerView.start();
	}

	private void reloadGame() {
		movesCount = 0;
		updateMovesText();
		isPaused = false;
		updatePlayPauseStatus();
		updateUndoStatus();
		mTime = 0;
		mTimerView.setBase(SystemClock.elapsedRealtime() - mTime);
		mPuzzleView.reloadGame(mPuzzleView.getDefaultTiles(), mTimerView);
		mTimerView.start();
	}

	@Override
	public void onPause() {
		super.onPause();
		//pauseTimer();
	}

	private void pauseTimer() {
		if (!mPuzzleView.isSolved()) {
			mTime = (SystemClock.elapsedRealtime() - mTimerView.getBase());
		}
		mTimerView.stop();
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if (mPuzzleView.isSolved()) {
			return false;
		}
		boolean moved;
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			switch (keyCode) {
			case KeyEvent.KEYCODE_DPAD_DOWN: {
				moved = mPuzzleView.move(PuzzleView.DIR_DOWN);
				break;
			}
			case KeyEvent.KEYCODE_DPAD_UP: {
				moved = mPuzzleView.move(PuzzleView.DIR_UP);
				break;
			}
			case KeyEvent.KEYCODE_DPAD_LEFT: {
				moved = mPuzzleView.move(PuzzleView.DIR_LEFT);
				break;
			}
			case KeyEvent.KEYCODE_DPAD_RIGHT: {
				moved = mPuzzleView.move(PuzzleView.DIR_RIGHT);
				break;
			}
			default:
				return false;
			}

			if (moved) {
				if (SettingsPreferences.getVibration(mContext)) {
					AppUtils.vibrate(mContext, AppConstants.VIBRATION_DURATION);
				}
			}
			if (mPuzzleView.checkSolved()) {
				onPuzzleSolved();
			}
			return true;
		}
		return false;
	}

	private void postScore() {
		mTime = SystemClock.elapsedRealtime() - mTimerView.getBase();
		mTimerView.stop();
		mTimerView.setBase(SystemClock.elapsedRealtime() - mTime);
		mTimerView.invalidate();
		String level = AppConstants.LEVEL_EASY;
		switch (SettingsPreferences.getDifficultyLevel(mContext)) {
		case AppConstants.DIFFICULTY_LEVEL_EASY:
			level = AppConstants.LEVEL_EASY;
			break;
		case AppConstants.DIFFICULTY_LEVEL_MEDIUM:
			level = AppConstants.LEVEL_MEDIUM;
			break;
		case AppConstants.DIFFICULTY_LEVEL_HARD:
			level = AppConstants.LEVEL_HARD;
			break;
		}
		ScoreItem item = new ScoreItem();
		item.setLevel(level);
		item.setTime(mTimerView.getText().toString());
		item.setSeconds(String.valueOf(mTime));
		item.setMoves(String.valueOf(movesCount));
		if (isHintTaken) {
			item.setHinttaken(AppConstants.TRUE);
		} else {
			item.setHinttaken(AppConstants.FALSE);
		}
		ScoresDataProvider.addScore(mContext, item);
		ScoreItem highItem = ScoresDataProvider.getHighScore(mContext, level);
		boolean isHighScore = false;

		if (Long.parseLong(item.getSeconds()) < Long.parseLong(highItem
				.getSeconds())) {
			isHighScore = true;
		}
		if (isHighScore) {
			Toast.makeText(mContext, "New High Score", Toast.LENGTH_SHORT).show();
		}
		if (SettingsPreferences.getVibration(mContext)) {
			AppUtils.vibrate(mContext, AppConstants.VIBRATION_DURATION);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);
		if (isPaused) {
			return true;
		}
		if (mPuzzleView.isSolved()) {
			return false;
		}

		int action = event.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN: {
			mPuzzleView.grabTile(event.getX(), event.getY());
			return true;
		}
		case MotionEvent.ACTION_MOVE: {
			mPuzzleView.dragTile(event.getX(), event.getY());
			return true;
		}
		case MotionEvent.ACTION_UP: {
			boolean moved = mPuzzleView.dropTile();
			if (moved) {
				movesCount = movesCount + 1;
				mPuzzleView.onMoved(movesCount);
				updateMovesText();
				updateUndoStatus();
				if (SettingsPreferences.getVibration(mContext)) {
					AppUtils.vibrate(mContext, AppConstants.VIBRATION_DURATION);
				}
			}
			if (AppConstants.TESTING_PUZZLE) {
				onPuzzleSolved();
			} else {
				if (mPuzzleView.checkSolved()) {
					onPuzzleSolved();
				}
			}
			return true;
		}
		}
		return false;
	}

	private void onPuzzleSolved() {
		isWin = true;
		mCompleteView.setImageBitmap(mPuzzleView.getCurrentImage());
		mCompleteView.setVisibility(View.VISIBLE);
		empty_view.setVisibility(View.VISIBLE);
		empty_view.bringToFront();
		Animation animation = AnimationUtils
				.loadAnimation(this, R.anim.fade_in);
		animation.setAnimationListener(mCompleteAnimListener);
		mCompleteView.startAnimation(animation);
		postScore();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelableArray("puzzles", mPuzzleView.getPuzzles());
        outState.putInt("blank_first", 0);
		outState.putLong("time", mTime);
	}

	@Override
	public void onBackPressed() {
		if (movesCount > 0) {
			showGameInProgressDialog(getString(R.string.confirmation),
					getString(R.string.difficulty_level_confirmation),
					getString(R.string.yes), getString(R.string.no), false);
		} else {
			finish();
		}
	}

	private void showGameInProgressDialog(String title, String message, String okText, String cancelText, boolean singleButtonEnabled) {
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View dialogView = inflater.inflate(R.layout.confirmation_dialog_view, null);
		TextView titleTextview = dialogView
				.findViewById(R.id.title_textview);
		TextView messageTextview = dialogView
				.findViewById(R.id.message_textview);
		Button okButton = dialogView.findViewById(R.id.ok_button);
		Button cancelButton = dialogView
				.findViewById(R.id.cancel_button);
		if (mCustomDialog != null) {
			mCustomDialog.dismiss();
			mCustomDialog = null;
		}
		mCustomDialog = new Dialog(mContext,
				android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
		mCustomDialog.setContentView(dialogView);

		mCustomDialog.setCanceledOnTouchOutside(false);

		titleTextview.setText(title);
		messageTextview.setText(message);
		if (singleButtonEnabled) {
			okButton.setText(okText);
			cancelButton.setVisibility(View.GONE);
		} else {
			okButton.setText(okText);
			cancelButton.setText(cancelText);
		}

		okButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
				mCustomDialog.dismiss();
			}
		});

		cancelButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				mCustomDialog.dismiss();
			}
		});
		mCustomDialog.show();
	}

	private void showWinDialog(String title, String message, String okText, final String cancelText) {
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View dialogView = inflater.inflate(R.layout.confirmation_dialog_view, null);
		TextView titleTextview = dialogView
				.findViewById(R.id.title_textview);
		TextView messageTextview = dialogView
				.findViewById(R.id.message_textview);
		Button okButton = dialogView.findViewById(R.id.ok_button);
		Button cancelButton = dialogView
				.findViewById(R.id.cancel_button);
		if (mCustomDialog != null) {
			mCustomDialog.dismiss();
			mCustomDialog = null;
		}
		mCustomDialog = new Dialog(mContext,
				android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
		mCustomDialog.setContentView(dialogView);
		mCustomDialog.setCancelable(false);

		mCustomDialog.setCanceledOnTouchOutside(false);

		titleTextview.setText(title);
		messageTextview.setText(message);
        okButton.setText(okText);
        cancelButton.setText(cancelText);

		okButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				mCustomDialog.dismiss();
                loadNextPhoto();
			}
		});

		cancelButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				mPuzzleView.setVisibility(View.VISIBLE);
				mCompleteView.setVisibility(View.GONE);
				reloadGame();
				mCustomDialog.dismiss();
			}
		});

		mCustomDialog.show();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	protected void onDestroy() {
		if (actionBroadcastReceiver != null) {
			unregisterReceiver(actionBroadcastReceiver);
			actionBroadcastReceiver = null;
		}
		if (mCustomDialog != null) {
			mCustomDialog.dismiss();
			mCustomDialog = null;
		}
		mTitleTextView = null;
		moves_textview = null;
		mCompleteView = null;
		tabs_bar5_checkbox = null;
		undo_imageview = null;
		play_pause_imageview = null;
		empty_view = null;
		if (mHandler != null) {
			mHandler.removeMessages(1);
			mHandler = null;
		}
		super.onDestroy();
	}
}