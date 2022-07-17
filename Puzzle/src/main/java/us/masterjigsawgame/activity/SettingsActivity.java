
package us.masterjigsawgame.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import us.masterjigsawgame.R;
import us.masterjigsawgame.core.BaseActivity;
import us.masterjigsawgame.util.AppConstants;
import us.masterjigsawgame.util.SettingsPreferences;


public class SettingsActivity extends BaseActivity {
	private Context mContext;
	private Dialog mCustomDialog;
	private RadioButton mEasyRadioButton;
	private RadioButton mMediumRadioButton;
	private RadioButton mHardRadioButton;
	private CheckBox mVibrationCheckBox;
	private CheckBox mHintCheckBox;
	private TextView mDifficultySelectedTextview;
	private int mDifficultyCurrentValue = 0;
	private int mPreviousDifficulyLevel;
	private boolean isVibrationOn;
	private boolean isHintOn;
	private boolean isGameRunning;
	private boolean isDifficultyDialogShown;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);
		mContext = this;
		Intent intent = getIntent();
		if (intent != null) {
			isGameRunning = intent.getBooleanExtra(
					AppConstants.EXTRA_PROCESSING_GAME_STRING, false);
		}
		initViews();
		mPreviousDifficulyLevel = SettingsPreferences
				.getDifficultyLevel(mContext);
		if (savedInstanceState != null) {
			isDifficultyDialogShown = savedInstanceState
					.getBoolean(AppConstants.EXTRA_IS_DIFFICULTY_DIALOG_SHOWN);
			if (isDifficultyDialogShown) {
				showDifficultyCustomDialog(getString(R.string.difficulti_level));
			}
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean(AppConstants.EXTRA_IS_DIFFICULTY_DIALOG_SHOWN,
				isDifficultyDialogShown);
	}


	private void initViews() {
		mVibrationCheckBox = (CheckBox) findViewById(R.id.vibration_checkbox);
		mHintCheckBox = (CheckBox) findViewById(R.id.show_hint_checkbox);
		mDifficultySelectedTextview = (TextView) findViewById(R.id.difficulty_selected_textview);

		populateVibrationContents();
		populateHintContents();
		setDifficultyLevelContents();
	}


	private void switchDifficultyRadioButtons() {
		mDifficultyCurrentValue = SettingsPreferences
				.getDifficultyLevel(mContext);
		int position = mDifficultyCurrentValue;
		if (AppConstants.DIFFICULTY_LEVEL_EASY == mDifficultyCurrentValue) {
			populateDifficultyContents(position);
		} else if (AppConstants.DIFFICULTY_LEVEL_MEDIUM == mDifficultyCurrentValue) {
			populateDifficultyContents(position);
		} else if (AppConstants.DIFFICULTY_LEVEL_HARD == mDifficultyCurrentValue) {
			populateDifficultyContents(position);
		}
	}

	private void switchVibrationCheckbox() {
		isVibrationOn = !isVibrationOn;
		SettingsPreferences.setVibration(mContext, isVibrationOn);
		populateVibrationContents();
	}


	private void switchHintCheckbox() {
		isHintOn = !isHintOn;
		if (isHintOn) {
			SettingsPreferences.setHintEnableDisable(mContext, true);
		} else {
			SettingsPreferences.setHintEnableDisable(mContext, false);
		}
		populateHintContents();
	}

	protected void populateDifficultyContents(int position) {
		if (position == AppConstants.DIFFICULTY_LEVEL_EASY) {
			mEasyRadioButton.setChecked(true);
			mMediumRadioButton.setChecked(false);
			mHardRadioButton.setChecked(false);
		} else if (position == AppConstants.DIFFICULTY_LEVEL_MEDIUM) {
			mMediumRadioButton.setChecked(true);
			mEasyRadioButton.setChecked(false);
			mHardRadioButton.setChecked(false);
		} else if (position == AppConstants.DIFFICULTY_LEVEL_HARD) {
			mHardRadioButton.setChecked(true);
			mEasyRadioButton.setChecked(false);
			mMediumRadioButton.setChecked(false);
		}
		mDifficultyCurrentValue = position;
		SettingsPreferences.setDifficultyLevel(mContext, mDifficultyCurrentValue);
	}

	protected void populateVibrationContents() {
		if (SettingsPreferences.getVibration(mContext)) {
			mVibrationCheckBox.setChecked(true);
		} else {
			mVibrationCheckBox.setChecked(false);
		}
		isVibrationOn = SettingsPreferences.getVibration(mContext);
	}


	protected void populateHintContents() {
		if (SettingsPreferences.getHintEnableDisable(mContext)) {
			mHintCheckBox.setChecked(true);
		} else {
			mHintCheckBox.setChecked(false);
		}
		isHintOn = SettingsPreferences.getHintEnableDisable(mContext);
	}

	protected void setDifficultyLevelContents() {
		int setLevelValue = SettingsPreferences.getDifficultyLevel(mContext);
		if (setLevelValue == AppConstants.DIFFICULTY_LEVEL_EASY) {
			mDifficultySelectedTextview.setText(getString(R.string.easy_level));
		} else if (setLevelValue == AppConstants.DIFFICULTY_LEVEL_MEDIUM) {
			mDifficultySelectedTextview
					.setText(getString(R.string.medium_level));
		} else if (setLevelValue == AppConstants.DIFFICULTY_LEVEL_HARD) {
			mDifficultySelectedTextview.setText(getString(R.string.hard_level));
		}

	}

	@Override
	public void onBackPressed() {
		if (mPreviousDifficulyLevel == SettingsPreferences.getDifficultyLevel(mContext)) {
			super.onBackPressed();
		} else {
			setResult(RESULT_OK);
			finish();
		}
	}


	public void viewClickHandler(View view) {
		switch (view.getId()) {
		case R.id.difficulty_layout:
			showDifficultyCustomDialog(getString(R.string.difficulti_level));
			isDifficultyDialogShown = true;
			break;
		case R.id.vibration_layout:
			switchVibrationCheckbox();
			break;
		case R.id.vibration_checkbox:
			switchVibrationCheckbox();
			break;
		case R.id.show_hint_layout:
			switchHintCheckbox();
			break;
		case R.id.show_hint_checkbox:
			switchHintCheckbox();
			break;

		}
	}

	private void showCustomDialog(String title, String message, String okText,
			String cancelText, boolean singleButtonEnabled, final int position) {
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View dialogView = inflater.inflate(R.layout.confirmation_dialog_view, null);
		TextView titleTextview = dialogView.findViewById(R.id.title_textview);
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
		mCustomDialog.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if (KeyEvent.KEYCODE_BACK == keyCode) {
					dialog.dismiss();
				}
				return false;
			}
		});

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
				Intent intent = new Intent(AppConstants.UPDATE_DIFFICULTY_LEVEL);
				sendBroadcast(intent);
				populateDifficultyContents(position);
				setDifficultyLevelContents();
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


	private void showDifficultyCustomDialog(String title) {
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View dialogView = inflater.inflate(R.layout.difficulty_custem_dialog, null);
		TextView titleTextView = dialogView
				.findViewById(R.id.title_textview);
		titleTextView.setText(title);
		mEasyRadioButton = dialogView
				.findViewById(R.id.easy_radiobutton);
		mMediumRadioButton = dialogView
				.findViewById(R.id.medium_radiobutton);
		mHardRadioButton = dialogView
				.findViewById(R.id.hard_radiobutton);
		RelativeLayout easyRelativeLayout = dialogView
				.findViewById(R.id.easy);
		RelativeLayout mediumRelativeLayout = dialogView
				.findViewById(R.id.medium);
		RelativeLayout hardRelativeLayout = dialogView
				.findViewById(R.id.hard);
		mEasyRadioButton.setOnClickListener(mViewClickListener);
		mMediumRadioButton.setOnClickListener(mViewClickListener);
		mHardRadioButton.setOnClickListener(mViewClickListener);
		easyRelativeLayout.setOnClickListener(mViewClickListener);
		mediumRelativeLayout.setOnClickListener(mViewClickListener);
		hardRelativeLayout.setOnClickListener(mViewClickListener);

		switchDifficultyRadioButtons();
		if (mCustomDialog != null) {
			mCustomDialog.dismiss();
			mCustomDialog = null;
		}
		mCustomDialog = new Dialog(mContext,
				android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
		mCustomDialog.setContentView(dialogView);
		mCustomDialog.setCanceledOnTouchOutside(false);
		mCustomDialog.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if (KeyEvent.KEYCODE_BACK == keyCode) {
					dialog.dismiss();
				}
				return false;
			}
		});
		mCustomDialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				isDifficultyDialogShown = false;
			}
		});
		mCustomDialog.show();
	}

	private OnClickListener mViewClickListener = new OnClickListener() {

		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.easy_radiobutton:
				if (isGameRunning) {
					int isSetLevel = SettingsPreferences
							.getDifficultyLevel(mContext);
					if (isSetLevel != AppConstants.DIFFICULTY_LEVEL_EASY) {
						showCustomDialog(
								getString(R.string.confirmation),
								getString(R.string.difficulty_level_confirmation),
								getString(R.string.yes),
								getString(R.string.no), false,
								AppConstants.DIFFICULTY_LEVEL_EASY);
					}
				} else {
					populateDifficultyContents(AppConstants.DIFFICULTY_LEVEL_EASY);
					setDifficultyLevelContents();
					mCustomDialog.dismiss();
				}
				break;
			case R.id.easy:
				if (isGameRunning) {
					int isSetLevel = SettingsPreferences
							.getDifficultyLevel(mContext);
					if (isSetLevel != AppConstants.DIFFICULTY_LEVEL_EASY) {
						showCustomDialog(
								getString(R.string.confirmation),
								getString(R.string.difficulty_level_confirmation),
								getString(R.string.yes),
								getString(R.string.no), false,
								AppConstants.DIFFICULTY_LEVEL_EASY);
					}
				} else {
					populateDifficultyContents(AppConstants.DIFFICULTY_LEVEL_EASY);
					setDifficultyLevelContents();
					mCustomDialog.dismiss();
				}
				break;
			case R.id.medium_radiobutton:
				if (isGameRunning) {
					int isSetLevel = SettingsPreferences
							.getDifficultyLevel(mContext);
					if (isSetLevel != AppConstants.DIFFICULTY_LEVEL_MEDIUM) {
						showCustomDialog(
								getString(R.string.confirmation),
								getString(R.string.difficulty_level_confirmation),
								getString(R.string.yes),
								getString(R.string.no), false,
								AppConstants.DIFFICULTY_LEVEL_MEDIUM);
					}
				} else {
					populateDifficultyContents(AppConstants.DIFFICULTY_LEVEL_MEDIUM);
					setDifficultyLevelContents();
					mCustomDialog.dismiss();
				}
				break;
			case R.id.medium:
				if (isGameRunning) {
					int isSetLevel = SettingsPreferences
							.getDifficultyLevel(mContext);
					if (isSetLevel != AppConstants.DIFFICULTY_LEVEL_MEDIUM) {
						showCustomDialog(
								getString(R.string.confirmation),
								getString(R.string.difficulty_level_confirmation),
								getString(R.string.yes),
								getString(R.string.no), false,
								AppConstants.DIFFICULTY_LEVEL_MEDIUM);
					}
				} else {
					populateDifficultyContents(AppConstants.DIFFICULTY_LEVEL_MEDIUM);
					setDifficultyLevelContents();
					mCustomDialog.dismiss();
				}
				break;
			case R.id.hard_radiobutton:
				if (isGameRunning) {
					int isSetLevel = SettingsPreferences
							.getDifficultyLevel(mContext);
					if (isSetLevel != AppConstants.DIFFICULTY_LEVEL_HARD) {
						showCustomDialog(
								getString(R.string.confirmation),
								getString(R.string.difficulty_level_confirmation),
								getString(R.string.yes),
								getString(R.string.no), false,
								AppConstants.DIFFICULTY_LEVEL_HARD);
					}
				} else {
					populateDifficultyContents(AppConstants.DIFFICULTY_LEVEL_HARD);
					setDifficultyLevelContents();
					mCustomDialog.dismiss();
				}
				break;
			case R.id.hard:
				if (isGameRunning) {
					int isSetLevel = SettingsPreferences
							.getDifficultyLevel(mContext);
					if (isSetLevel != AppConstants.DIFFICULTY_LEVEL_HARD) {
						showCustomDialog(
								getString(R.string.confirmation),
								getString(R.string.difficulty_level_confirmation),
								getString(R.string.yes),
								getString(R.string.no), false,
								AppConstants.DIFFICULTY_LEVEL_HARD);
					}
				} else {
					populateDifficultyContents(AppConstants.DIFFICULTY_LEVEL_HARD);
					setDifficultyLevelContents();
					mCustomDialog.dismiss();
				}
				break;
			}
		}
	};

	@Override
	public void onDestroy() {
		if (mContext != null) {
			if (mCustomDialog != null) {
				mCustomDialog.dismiss();
				mCustomDialog = null;
			}
			mEasyRadioButton = null;
			mMediumRadioButton = null;
			mHardRadioButton = null;
			mVibrationCheckBox = null;
			mHintCheckBox = null;
			mHintCheckBox = null;
			mContext = null;
			super.onDestroy();
		}
	}
}
