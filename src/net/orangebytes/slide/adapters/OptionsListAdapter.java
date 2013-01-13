package net.orangebytes.slide.adapters;

import net.orangebytes.slide.R;
import net.orangebytes.slide.model.PuzzleInfo;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/// The ListAdapter for displaying images and related data in the options menu.
public class OptionsListAdapter extends ArrayAdapter<PuzzleInfo> {
	
	/// The context that this adapter lives in, passed in by the creating list
	private final Context mContext;
	
	/// The array of values the adapter should pull from
	private final PuzzleInfo[] mValues;
	
	/// A Typeface used for the image titles, loaded with Roboto-Light at runtime.
	private final Typeface mTypeface;

	
	/// Static class to implement the ViewHolder pattern, for speeding up list processing
	static class OptionsListViewHolder {
		
		/// The title text field of a view
		TextView mTitleText;
		
		/// The time text field of a view
		TextView mTimeText;
		
		/// The move text field of a view
		TextView mMoveText;
		
		/// The puzzle thumbnail image view
		ImageView mPuzzleThumb;
	}

	
	/// Constructor taking a context and data array as parameters
	public OptionsListAdapter(Context context, PuzzleInfo[] values) {
		super(context, R.layout.puzzle_info, values);
		mContext = context;
		mValues = values;
		mTypeface = Typeface.createFromAsset(mContext.getAssets(), "Roboto-Light.ttf");
	}

	@Override
	/// Returns the view for a given position
	public View getView(int position, View convertView, ViewGroup parent) {

		OptionsListViewHolder viewHolder;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.puzzle_info, parent, false);

			viewHolder = new OptionsListViewHolder();
			viewHolder.mTitleText 	= (TextView) convertView.findViewById(R.id.title_text);
			viewHolder.mTimeText  	= (TextView) convertView.findViewById(R.id.time_text);
			viewHolder.mMoveText  	= (TextView) convertView.findViewById(R.id.move_text);
			viewHolder.mPuzzleThumb = (ImageView) convertView.findViewById(R.id.puzzle_thumb);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (OptionsListViewHolder) convertView.getTag();
		}

		if (viewHolder.mTitleText != null) {
			viewHolder.mTitleText.setText(mValues[position].getTitle());
			viewHolder.mTitleText.setTypeface(mTypeface);

			int puzzleTime = mValues[position].getTime();

			if (puzzleTime < 10) {
				viewHolder.mTimeText.setText("0:0" + puzzleTime);
			}
			else if (puzzleTime < 60) {
				viewHolder.mTimeText.setText("0:" + puzzleTime);
			}
			else {
				if (puzzleTime % 60 < 10) {
					viewHolder.mTimeText.setText(puzzleTime / 60 + ":0" + puzzleTime % 60);
				} else {
					viewHolder.mTimeText.setText(puzzleTime / 60 + ":" + puzzleTime % 60);
				}
			}

			viewHolder.mMoveText.setText(mValues[position].getMoves() + "");
		}
		
		Resources res = mContext.getResources();
		int resID = res.getIdentifier(mValues[position].getThumb(), "drawable", mContext.getPackageName());
		Drawable drawable = res.getDrawable(resID);
		viewHolder.mPuzzleThumb.setImageDrawable(drawable);

		return convertView;
	}
}