
package us.masterjigsawgame.util;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Vibrator;


public class AppUtils {

	private static Vibrator sVibrator;

	public static int getScreenWidth(Context context) {
		return context.getResources().getDisplayMetrics().widthPixels;
	}


	public static Object[] swap(Object array[], int i, int j) {
		if (i >= array.length || j >= array.length || i < 0 || j < 0) {
			return array;
		}
		Object temp;
		temp = array[i];
		array[i] = array[j];
		array[j] = temp;
		return array;
	}

	public static void vibrate(Context context, long duration) {
		if (sVibrator == null) {
			sVibrator = (Vibrator) context
					.getSystemService(Context.VIBRATOR_SERVICE);
		}
		if (sVibrator != null) {
			if (duration == 0) {
				duration = 50;
			}
			sVibrator.vibrate(duration);
		}
	}

}
