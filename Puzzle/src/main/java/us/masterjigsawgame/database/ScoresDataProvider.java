
package us.masterjigsawgame.database;

import java.util.ArrayList;
import java.util.Collections;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils.InsertHelper;
import android.database.sqlite.SQLiteDatabase;

import us.masterjigsawgame.core.Comparator;
import us.masterjigsawgame.model.ScoreItem;


public class ScoresDataProvider {

	public synchronized static boolean addScore(Context context, ScoreItem item) {
		boolean result = false;
		try {
			SQLiteDatabase db = DBHelper.getInstance(context).getSqLiteDb();
			db.beginTransaction();
			InsertHelper inserHelper = new InsertHelper(db,
					DatabaseInfo.SCORES_TABLE);
			final int firstColumn = inserHelper
					.getColumnIndex(DatabaseInfo.ScoresColumn.LEVEL);
			final int secondColumn = inserHelper
					.getColumnIndex(DatabaseInfo.ScoresColumn.TIME);
			final int thirdColumn = inserHelper
					.getColumnIndex(DatabaseInfo.ScoresColumn.SECONDS);
			final int fourColumn = inserHelper
					.getColumnIndex(DatabaseInfo.ScoresColumn.MOVES);
			final int fifthColumn = inserHelper
					.getColumnIndex(DatabaseInfo.ScoresColumn.HINTTAKEN);
			try {
				db.setLockingEnabled(false);
				inserHelper.prepareForInsert();
				inserHelper.bind(firstColumn, item.getLevel());
				inserHelper.bind(secondColumn, item.getTime());
				inserHelper.bind(thirdColumn, item.getSeconds());
				inserHelper.bind(fourColumn, item.getMoves());
				inserHelper.bind(fifthColumn, item.getHinttaken());
				inserHelper.execute();
				db.setTransactionSuccessful();
				result = true;
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				inserHelper.close();
				db.setLockingEnabled(true);
				db.endTransaction();
			}
		} catch (Exception e) {
		}
		return result;
	}


	public synchronized static boolean removeScores(Context context,
			String level) {
		boolean result = false;
		String where = DatabaseInfo.ScoresColumn.LEVEL + "='" + level + "'";
		int count = DBHelper.getInstance(context).delete(
				DatabaseInfo.SCORES_TABLE, where, null);
		if (count > 0) {
			result = true;
		}
		return result;
	}


	public synchronized static ScoreItem getHighScore(Context context,
			String level) {
		ScoreItem item = null;
		ArrayList<ScoreItem> scoreItems = ScoresDataProvider.getScoreList(
				context, level);
		if (scoreItems != null) {
			ArrayList<ScoreItem> list = new ArrayList<ScoreItem>(scoreItems);
			Collections.sort(list, new Comparator.SortCompare(true));
			if (list != null) {
				item = list.get(0);
			}
		}
		return item;
	}


	public synchronized static ArrayList<ScoreItem> getScoreList(
			Context context, String level) {
		ArrayList<ScoreItem> list = null;
		String where = DatabaseInfo.ScoresColumn.LEVEL + "='" + level + "'";
		Cursor scoreCursor = DBHelper.getInstance(context).select(
				DatabaseInfo.SCORES_TABLE, null, where, null, null, null, null);
		if (scoreCursor != null && scoreCursor.getCount() > 0) {
			list = new ArrayList<>();
			scoreCursor.moveToFirst();
			for (int k = 0; k < scoreCursor.getCount(); k++) {
				ScoreItem item = new ScoreItem();
				item.setLevel(scoreCursor.getString(scoreCursor
						.getColumnIndex(DatabaseInfo.ScoresColumn.LEVEL)));
				item.setTime(scoreCursor.getString(scoreCursor
						.getColumnIndex(DatabaseInfo.ScoresColumn.TIME)));
				item.setSeconds(scoreCursor.getString(scoreCursor
						.getColumnIndex(DatabaseInfo.ScoresColumn.SECONDS)));
				item.setMoves(scoreCursor.getString(scoreCursor
						.getColumnIndex(DatabaseInfo.ScoresColumn.MOVES)));
				item.setHinttaken(scoreCursor.getString(scoreCursor
						.getColumnIndex(DatabaseInfo.ScoresColumn.HINTTAKEN)));
				item.setPosition(k);
				list.add(item);
				scoreCursor.moveToNext();
			}
		}
		return list;
	}



	@SuppressWarnings("unchecked")
	public synchronized static int getHighScorePosition(
			ArrayList<ScoreItem> scorelist) {
		int position = 0;
		ArrayList<ScoreItem> list = new ArrayList<ScoreItem>(scorelist);
		Collections.sort(list, new Comparator.SortCompare(true));
		position = list.get(0).getPosition();
		return position;
	}
}