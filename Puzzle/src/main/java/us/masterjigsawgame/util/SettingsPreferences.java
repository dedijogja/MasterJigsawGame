
package us.masterjigsawgame.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingsPreferences {

	private static final String SETTING_PUZZLE_PREF = "setting_puzzle_pref";
	private static final String DIFFICULTY_LEVEL = "difficulty_level";
	private static final String SHOW_HINT_ONOFF = "showhint_enable_disable";
	private static final String VIBRATION = "vibrate_status";

	public static void setVibration(Context context, Boolean result) {
		SharedPreferences prefs = context.getSharedPreferences(
				SETTING_PUZZLE_PREF, Context.MODE_WORLD_WRITEABLE);
		SharedPreferences.Editor prefEditor = prefs.edit();
		prefEditor.putBoolean(VIBRATION, result);
		prefEditor.commit();
	}

	public static boolean getVibration(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(
				SETTING_PUZZLE_PREF, Context.MODE_WORLD_WRITEABLE);
		return prefs.getBoolean(VIBRATION, false);
	}

	public static void setDifficultyLevel(Context context, int level) {
		SharedPreferences prefs = context.getSharedPreferences(
				SETTING_PUZZLE_PREF, Context.MODE_WORLD_WRITEABLE);
		SharedPreferences.Editor prefEditor = prefs.edit();
		prefEditor.putInt(DIFFICULTY_LEVEL, level);
		prefEditor.commit();
	}

	public static int getDifficultyLevel(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(
				SETTING_PUZZLE_PREF, Context.MODE_WORLD_WRITEABLE);
		return prefs.getInt(DIFFICULTY_LEVEL,
				AppConstants.DIFFICULTY_LEVEL_EASY);
	}
	public static void setHintEnableDisable(Context context, Boolean result) {
		SharedPreferences prefs = context.getSharedPreferences(
				SETTING_PUZZLE_PREF, Context.MODE_WORLD_WRITEABLE);
		SharedPreferences.Editor prefEditor = prefs.edit();
		prefEditor.putBoolean(SHOW_HINT_ONOFF, result);
		prefEditor.commit();
	}

	public static boolean getHintEnableDisable(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(
				SETTING_PUZZLE_PREF, Context.MODE_WORLD_WRITEABLE);
		return prefs.getBoolean(SHOW_HINT_ONOFF,
				AppConstants.DEFAULT_HINT_SETTING);
	}
}
