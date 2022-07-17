package us.masterjigsawgame.database;

import android.provider.BaseColumns;

class DatabaseInfo {

	final static String SCORES_TABLE = "Scores";

	interface ScoresColumn extends BaseColumns {
		String LEVEL = "level";
		String TIME = "time";
		String SECONDS = "seconds";
		String MOVES = "moves";
		String HINTTAKEN = "hint_taken";
	}
}
