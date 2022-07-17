package us.masterjigsawgame.core;

import android.content.Context;

import us.masterjigsawgame.model.ScoreItem;


public class Comparator {
	private static Comparator comparator;


	public static Comparator getInstance(Context context) {
		if (comparator == null) {
			comparator = new Comparator();
		}

		return comparator;
	}

	@SuppressWarnings("rawtypes")
	public static class SortCompare implements java.util.Comparator {

		boolean ascending;

		public SortCompare(boolean mode) {
			ascending = mode;
		}

		@Override
		public int compare(Object lhs, Object rhs) {
			Long seconds1 = Long.parseLong(((ScoreItem) lhs).getSeconds());
			Long seconds2 = Long.parseLong(((ScoreItem) rhs).getSeconds());
			if (ascending) {
				return seconds1.compareTo(seconds2);
			} else {
				return seconds2.compareTo(seconds1);
			}
		}

	}

}