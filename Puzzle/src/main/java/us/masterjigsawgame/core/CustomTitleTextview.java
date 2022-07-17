/**
 * 
 */
package us.masterjigsawgame.core;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import us.masterjigsawgame.util.AppConstants;


/**
 * The class to customize textview for font.
 * 
 * @author vishalbodkhe
 * 
 */
public class CustomTitleTextview extends TextView {

	/** The Context for loading assets */
	private Context mContext;

	/** The string to hold ttf name */
	private String mTTFName;

	public CustomTitleTextview(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		mTTFName = "title.ttf";
		setTypeface();
	}

	/**
	 * This method used to set the typeface.
	 */
	private void setTypeface() {
		Typeface font = Typeface.createFromAsset(mContext.getAssets(),
				AppConstants.FONTS + mTTFName);
		setTypeface(font);
	}

	@Override
	public void setTypeface(Typeface tf) {
		super.setTypeface(tf);
	}
}
