
package us.masterjigsawgame.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import us.masterjigsawgame.R;
import us.masterjigsawgame.adaper.ScoresListAdapter;
import us.masterjigsawgame.core.BaseActivity;
import us.masterjigsawgame.database.ScoresDataProvider;
import us.masterjigsawgame.model.ScoreItem;
import us.masterjigsawgame.util.AppConstants;


public class ScoreDetailActivity extends BaseActivity {
	private Context mContext;
	private GetScoresTask mGetScoreTask;
	private ProgressDialog mProgressDialog;
	private ScoresListAdapter mScoresListAdapter;
	private ListView mListView;
	private ArrayList<ScoreItem> mScoreItems;
	private String key;
	private int level;
	private int mHighScorePosition;
	private TextView mTitleTextView;
	private TextView mNoScoreFoundTextview;
	private Button mResetScoreButton;
	private Button mOKButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.score_detail_layout);
		mContext = ScoreDetailActivity.this;
		setResult(RESULT_CANCELED);
		Intent intent = getIntent();
		if (intent != null) {
			level = intent.getIntExtra(AppConstants.EXTRA_LEVEL_STRING,
					AppConstants.DIFFICULTY_LEVEL_EASY);
		}
		initViews();
		updateTitle();
		startUpdateTask(key);
	}


	private void updateTitle() {
		if (AppConstants.DIFFICULTY_LEVEL_EASY == level) {
			mTitleTextView.setText(R.string.easy_level_score);
			key = AppConstants.LEVEL_EASY;
		} else if (AppConstants.DIFFICULTY_LEVEL_MEDIUM == level) {
			mTitleTextView.setText(R.string.medium_level_score);
			key = AppConstants.LEVEL_MEDIUM;
		} else if (AppConstants.DIFFICULTY_LEVEL_HARD == level) {
			mTitleTextView.setText(R.string.hard_level_score);
			key = AppConstants.LEVEL_HARD;
		}
	}


	private void initViews() {
		mResetScoreButton = (Button)findViewById(R.id.reset_button);
		mOKButton = (Button) findViewById(R.id.ok_button);
		mNoScoreFoundTextview = (TextView)findViewById(R.id.no_scorefound_textview);
		mTitleTextView = (TextView)findViewById(R.id.title);
		mListView = (ListView)findViewById(R.id.list_view);
		mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setMessage(getResources().getString(
				R.string.loading_scorelist));
	}


	public void viewClickHandler(View view) {
		switch (view.getId()) {
		case R.id.reset_button:
			ScoresDataProvider.removeScores(mContext, key);
			setResult(RESULT_OK);
			finish();
			break;
		case R.id.ok_button:
			finish();
			break;
		}
	}

	private void startUpdateTask(String level) {
		if (mGetScoreTask != null) {
			mGetScoreTask.cancel(true);
			mGetScoreTask = null;
		}
		mGetScoreTask = new GetScoresTask(level);
		mGetScoreTask.execute();
	}


	public class GetScoresTask extends AsyncTask<Void, Void, Boolean> {

		private String level;

		public GetScoresTask(String level) {
			this.level = level;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mProgressDialog.show();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			try {
				mScoreItems = ScoresDataProvider.getScoreList(mContext, level);
				mHighScorePosition = ScoresDataProvider
						.getHighScorePosition(mScoreItems);
			} catch (Exception e) {
				return false;
			}
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (mContext != null) {
				super.onPostExecute(result);
				mProgressDialog.dismiss();
				populateData();
			}
		}

		private void populateData() {
			if (mScoresListAdapter == null) {
				mScoresListAdapter = new ScoresListAdapter(mContext);
				mListView.setAdapter(mScoresListAdapter);
			}
			mScoresListAdapter.setCurrentSelectedPosition(mHighScorePosition);
			mScoresListAdapter.setList(mScoreItems);
			if (mScoreItems == null) {
				hideButtons();
			}
		}
	}

	/**
	 * Method to hide buttons
	 */
	public void hideButtons() {
		mResetScoreButton.setVisibility(View.GONE);
		mOKButton.setVisibility(View.GONE);
		mListView.setVisibility(View.GONE);
		mNoScoreFoundTextview.setVisibility(View.VISIBLE);

	}

	@Override
	public void onDestroy() {
		if (mContext != null) {
			if (mGetScoreTask != null) {
				mGetScoreTask.cancel(true);
				mGetScoreTask = null;
			}
			if (mScoresListAdapter != null) {
				mScoresListAdapter.cleanUp();
				mScoresListAdapter = null;
			}
			if (mProgressDialog != null) {
				mProgressDialog.dismiss();
				mProgressDialog = null;
			}
			mResetScoreButton = null;
			mOKButton = null;
			mTitleTextView = null;
			mNoScoreFoundTextview = null;
			mContext = null;
			super.onDestroy();
		}
	}
}
