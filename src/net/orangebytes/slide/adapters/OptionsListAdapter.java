package net.orangebytes.slide.adapters;

import net.orangebytes.slide.R;
import net.orangebytes.slide.model.PuzzleInfo;
import net.orangebytes.slide.preferences.GamePreferences;
import net.orangebytes.slide.utils.FontUtils;
import net.orangebytes.slide.utils.TimeUtils;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/// The ListAdapter for displaying images and related data in the options menu.
public class OptionsListAdapter extends ArrayAdapter<PuzzleInfo> {
	
	/// The context that this adapter lives in, passed in by the creating list
	private final Context mContext;
	
	/// The array of values the adapter should pull from
	private final PuzzleInfo[] mValues;
	
	/// The corresponding list view
	private final ListView mList;

	
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
	public OptionsListAdapter(Context context, PuzzleInfo[] values, ListView pList) {
		super(context, R.layout.puzzle_info, values);
		
		mContext = context;
		mValues = values;
		mList = pList;
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
			viewHolder.mTitleText.setTypeface(FontUtils.getRobotoLight(mContext));
			viewHolder.mTitleText.setFocusable(false);

			viewHolder.mTimeText.setText(TimeUtils.intToMinutes(mValues[position].getTime()));
			viewHolder.mTimeText.setFocusable(false);
			
			viewHolder.mMoveText.setText(mValues[position].getMoves() + "");
			viewHolder.mMoveText.setFocusable(false);
		}
		
		Resources res = mContext.getResources();
		int resID = res.getIdentifier(mValues[position].getThumb(), "drawable", mContext.getPackageName());
		Drawable drawable = res.getDrawable(resID);
		viewHolder.mPuzzleThumb.setImageDrawable(drawable);
		viewHolder.mPuzzleThumb.setFocusable(false);
		
		convertView.setFocusable(false);
		convertView.setClickable(false);


	    AlphaAnimation aa = new AlphaAnimation(0.3f,0.3f);
	    aa.setDuration(10);
	    aa.setFillAfter(true);
	    convertView.startAnimation(aa);
	    
		return convertView;
	}
	
	public PuzzleInfo getInfo(int index) {
		return mValues[index];
	}
}