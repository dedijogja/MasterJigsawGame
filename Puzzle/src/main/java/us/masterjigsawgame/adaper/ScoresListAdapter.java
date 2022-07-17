
package us.masterjigsawgame.adaper;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import us.masterjigsawgame.R;
import us.masterjigsawgame.model.ScoreItem;
import us.masterjigsawgame.util.AppConstants;


public class ScoresListAdapter extends BaseAdapter {

	private Context mContext;
	private int mCurrentSelectedPosition;
	private String yes;
	private String no;

	/** Holds Layout Inflater to inflate list item. */
	private LayoutInflater mLayoutInflator;

	/** Holds the list */
	private ArrayList<ScoreItem> mListItems;

	public ScoresListAdapter(Context context) {
		super();
		mContext = context;
		yes = mContext.getString(R.string.yes);
		no = mContext.getString(R.string.no);
		mLayoutInflator = (LayoutInflater) context.getApplicationContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	/**
	 * Method to set the list.
	 * 
	 * @param mScoreItems
	 */
	public void setList(ArrayList<ScoreItem> mScoreItems) {
		mListItems = mScoreItems;
		notifyDataSetChanged();
	}

	public int getSelectedPosition() {
		return mCurrentSelectedPosition;
	}

	/**
	 * method to set the selection item
	 * 
	 * @param position
	 */
	public void setCurrentSelectedPosition(int position) {
		mCurrentSelectedPosition = position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = convertView;
		final ScoreItem item = getItem(position);
		if (view == null) {
			view = mLayoutInflator.inflate(R.layout.scorelist_layout, null);
			ViewHolder holder = new ViewHolder();
			holder.number = (TextView) view.findViewById(R.id.no_textview);
			holder.time = (TextView) view.findViewById(R.id.time_textview);
			holder.moves = (TextView) view.findViewById(R.id.moves_textview);
			holder.hinttaken = (TextView) view
					.findViewById(R.id.helptaken_textview);
			view.setTag(holder);
		}

		ViewHolder viewHolder = (ViewHolder) view.getTag();
		if (viewHolder != null && item != null) {
			viewHolder.moves.setText(item.getMoves());
			viewHolder.time.setText(item.getTime());
			viewHolder.number.setText(String.valueOf(position + 1));
			if (item.getHinttaken().equalsIgnoreCase(AppConstants.TRUE)) {
				viewHolder.hinttaken.setText(yes);
			} else {
				viewHolder.hinttaken.setText(no);
			}
			if (mCurrentSelectedPosition == position) {
				view.setBackgroundResource(R.drawable.rounded_rectangle_blue_border);
			} else {
				view.setBackgroundResource(R.color.transparent);
			}

		}
		return view;
	}

	@Override
	public int getCount() {
		if (mListItems != null) {
			return mListItems.size();
		}
		return 0;
	}

	@Override
	public ScoreItem getItem(int postion) {
		if (mListItems != null) {
			return mListItems.get(postion);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * ViewHolder class to hold the views to bind on listview.
	 * 
	 * @author vishalbodkhe
	 * 
	 */
	static class ViewHolder {
		TextView number;
		TextView time;
		TextView moves;
		TextView hinttaken;
	}

	/**
	 * 
	 * For Freeing up the resources
	 */
	public void cleanUp() {
		mListItems = null;
		mLayoutInflator = null;
		mContext = null;
	}

}
