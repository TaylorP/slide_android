package net.orangebytes.slide.adapters;

import java.util.Hashtable;

import net.orangebytes.slide.R;
import net.orangebytes.slide.activities.MainActivity;
import net.orangebytes.slide.model.PuzzleInfo;
import net.orangebytes.slide.preferences.GamePreferences;
import net.orangebytes.slide.utils.TimeUtils;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/// The ListAdapter for displaying images and related data in the options menu.
public class OptionsListAdapter extends ArrayAdapter<PuzzleInfo> {
	
	/// The context that this adapter lives in, passed in by the creating list
	private final MainActivity mContext;
	
	/// The array of values the adapter should pull from
	private final PuzzleInfo[] mValues;
	
	/// The selected view
	private int mSelected;
	
	/// Image cache for drawables
	private Hashtable<String,Drawable> mCache;

	
	/// Static class to implement the ViewHolder pattern, for speeding up list processing
	static class OptionsListViewHolder {
		
		/// The title text field of a view
		TextView mTitleText;
		
		/// The times text
		TextView mTimesText;
		
		/// The moves text
		TextView mMovesText;
		
		/// The puzzle thumbnail image view
		ImageView mPuzzleThumb;
	}

	
	/// Constructor taking a context and data array as parameters
	public OptionsListAdapter(MainActivity context, PuzzleInfo[] values) {
		super(context, R.layout.puzzle_info, values);
		
		mContext = context;
		mValues = values;
		
		mCache = new Hashtable<String,Drawable>();
	}

	@Override
	/// Returns the view for a given position
	public View getView(int position, View convertView, ViewGroup parent) {

		OptionsListViewHolder viewHolder;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.puzzle_info, parent, false);

			viewHolder = new OptionsListViewHolder();
			viewHolder.mPuzzleThumb = (ImageView) convertView.findViewById(R.id.puzzle_thumb);
			
			viewHolder.mTimesText = (TextView)convertView.findViewById(R.id.list_time_text);
			viewHolder.mTitleText = (TextView)convertView.findViewById(R.id.list_title_text);
			viewHolder.mMovesText = (TextView)convertView.findViewById(R.id.list_move_text);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (OptionsListViewHolder) convertView.getTag();
		}
		
		Drawable drawable = mCache.get(mValues[position].getThumb());
		if(drawable == null) {
			Resources res = mContext.getResources();
			int resID = res.getIdentifier(mValues[position].getThumb(), "drawable", mContext.getPackageName());
			drawable = res.getDrawable(resID);
			mCache.put(mValues[position].getThumb(), drawable);
		}
		
		viewHolder.mPuzzleThumb.setImageDrawable(drawable);
		viewHolder.mPuzzleThumb.setFocusable(false);
		
		if(viewHolder.mTitleText != null) {
			int x = mContext.getGameState().getX(Configuration.ORIENTATION_PORTRAIT);
			int y = mContext.getGameState().getY(Configuration.ORIENTATION_PORTRAIT);
			int moves = GamePreferences.get(mContext).loadMoves(mValues[position].getTitle(),x,y); 
			int times = GamePreferences.get(mContext).loadTimes(mValues[position].getTitle(), x, y);
			viewHolder.mTitleText.setText(mValues[position].getTitle());
			viewHolder.mMovesText.setText(moves > 0 ? (moves+"") : "--");
			viewHolder.mTimesText.setText(TimeUtils.intToMinutes(times));
		}
		
		convertView.setFocusable(false);
		convertView.setClickable(false);
		
		if(position == mSelected) {
			convertView.getBackground().setAlpha(60);
			viewHolder.mPuzzleThumb.getBackground().setAlpha(255);
		} else {
			convertView.getBackground().setAlpha(0);
			viewHolder.mPuzzleThumb.getBackground().setAlpha(150);
		}

		return convertView;
	}
	
	public PuzzleInfo getInfo(int index) {
		return mValues[index];
	}
	
	public void setSelected(int pSelected) {
		mSelected = pSelected;
	}
}