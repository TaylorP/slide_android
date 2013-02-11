package net.orangebytes.slide.activities;

import net.orangebytes.slide.R;
import net.orangebytes.slide.adapters.OptionsListAdapter;
import net.orangebytes.slide.model.PuzzleInfo;
import net.orangebytes.slide.preferences.GamePreferences;
import net.orangebytes.slide.preferences.GameState;
import net.orangebytes.slide.utils.DisplayUtils;
import net.orangebytes.slide.utils.Sounds;
import net.orangebytes.slide.utils.TimeUtils;
import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

/// The options fragment, for the options menu of the game
public class OptionsFragment extends Fragment implements ViewSwitcher.ViewFactory {

	/// The activity this fragment is in, used as it's context as well
	private MainActivity mActivity;
	
	/// The options list, containing the available puzzles and some settings
	private ListView mOptionsList;
	
	/// The adapter for the options list
	private OptionsListAdapter mOptionsAdapter;
	
	/// An array of puzzles in the game. TODO: load these from shared preferences
	private PuzzleInfo[] mValues;
	
	/// TextSwitcher for the grid size text
	private TextSwitcher mSwitcher;

	/// The last selected position
	private int mLastPosition = -1;
	
	/// The last selected view
	private View mLastSelected = null;
	
	/// The puzzle name text
	private TextView mPuzzleName;
	
	/// The best time text
	private TextView mBestTime;
	
	/// The best moves text
	private TextView mBestMoves;

	@Override
	/// Creates the view for this fragment
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.options_fragment, container, false);
		mOptionsList = (ListView) root.findViewById(R.id.option_list);
		mPuzzleName = (TextView) root.findViewById(R.id.puzzle_name);
		mBestTime = (TextView)root.findViewById(R.id.puzzle_time);
		mBestMoves = (TextView)root.findViewById(R.id.puzzle_moves);
		
		mActivity = (MainActivity) getActivity();
		
		mValues = new PuzzleInfo[] {
				new PuzzleInfo("beach", "beach_thumb"),
				new PuzzleInfo("bird", "bird_thumb"),
				new PuzzleInfo("bug", "bug_thumb"),
				new PuzzleInfo("canyon", "canyon_thumb"),
				new PuzzleInfo("chess", "chess_thumb"),
				new PuzzleInfo("flower", "flower_thumb"),
				new PuzzleInfo("fruit", "fruit_thumb"),
				new PuzzleInfo("leaf", "leaf_thumb"),
				new PuzzleInfo("peach", "peach_thumb") };

		mOptionsAdapter = new OptionsListAdapter(mActivity, mValues);
		mOptionsList.setAdapter(mOptionsAdapter);
		mOptionsList.setClickable(true);
		mOptionsList.setOnItemClickListener(new OnItemClickListener() {
		       @SuppressLint("NewApi")
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		    	   
		    	   ((MainActivity)mActivity).setPuzzle(mValues[position].getTitle(), -1, -1);
					
		    	   if(mLastSelected != null) {
		    		    mLastSelected.getBackground().setAlpha(0);
		    	   } else if (mLastPosition >= 0) {
		    		   
		    		   int wantedPosition = mLastPosition;
		    		   int firstPosition = mOptionsList.getFirstVisiblePosition() - mOptionsList.getHeaderViewsCount(); // This is the same as child #0
		    		   int wantedChild = wantedPosition - firstPosition;
		    		   if (!(wantedChild < 0 || wantedChild >= mOptionsList.getChildCount())) {
			    		  View v = mOptionsList.getChildAt(wantedChild);
			    		  if(v != null)
			    			  v.getBackground().setAlpha(0);
		    		   }		    		   
		    	   }
		    	   
		    	   mLastPosition = position;
		    	   mOptionsAdapter.setSelected(position);
		    	   mLastSelected = view;
			   	   view.getBackground().setAlpha(60);


			   	   updateStats(mActivity.getGameState());
	    		    
		    	   if (android.os.Build.VERSION.SDK_INT >= 11){
		    		   mOptionsList.smoothScrollToPositionFromTop(position, 0);
		    	   }else if (android.os.Build.VERSION.SDK_INT >= 8){
		    	       int firstVisible = mOptionsList.getFirstVisiblePosition();
		    	       int lastVisible = mOptionsList.getLastVisiblePosition();
		    	       if (position < firstVisible) {
		    	    	   mOptionsList.smoothScrollToPosition(position);
		    	       }else{
		    	    	   mOptionsList.smoothScrollToPosition(position + lastVisible - firstVisible - 2);
		    	       }
		    	   }else{
		    		   mOptionsList.setSelectionFromTop(position, 0);
		    	   }
		       }
		   });

		Animation in = AnimationUtils.loadAnimation(mActivity,android.R.anim.fade_in);
		Animation out = AnimationUtils.loadAnimation(mActivity,R.anim.fast_fade_out);

		int orientation = getResources().getConfiguration().orientation;
		
		mSwitcher = (TextSwitcher) root.findViewById(R.id.grid_size);
		mSwitcher.setFactory(this);
		mSwitcher.setInAnimation(in);
		mSwitcher.setOutAnimation(out);
		mSwitcher.setText(mActivity.getString(R.string.grid_size) + " (" + mActivity.getGameState().getX(orientation) + "x" + mActivity.getGameState().getY(orientation) +")");

		final SeekBar sk = (SeekBar) root.findViewById(R.id.game_size_bar);
		sk.setMax(DisplayUtils.getDisplaySizes(mActivity).size()-1);
		sk.setProgress(mActivity.getGameState().getX(orientation) + mActivity.getGameState().getY(orientation) - 6);
		sk.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				
				Point p = DisplayUtils.getDisplaySizes(mActivity).get(seekBar.getProgress());
				int xSize = p.x;
				int ySize = p.y;
				int orientation = getResources().getConfiguration().orientation;
				String sizeStr;
				
				if(orientation == Configuration.ORIENTATION_LANDSCAPE) {
					sizeStr = p.y+"x"+p.x;
				} else {
					sizeStr = p.x+"x"+p.y;
				}
				mSwitcher.setText(sizeStr);
				
				Animation in = AnimationUtils.loadAnimation(
						mActivity, R.anim.slow_fade_in);
				Animation out = AnimationUtils.loadAnimation(
						mActivity, R.anim.slow_fade_out);
				mSwitcher.setInAnimation(in);
				mSwitcher.setOutAnimation(out);
				mSwitcher.setText(mActivity.getString(R.string.grid_size) + " (" + sizeStr +")");
				
				((MainActivity)mActivity).setPuzzle(null, xSize, ySize);
				updateStats(mActivity.getGameState());
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

				Animation in = AnimationUtils.loadAnimation(
						mActivity,
						android.R.anim.fade_in);
				Animation out = AnimationUtils.loadAnimation(
						mActivity,
						R.anim.fast_fade_out);
				mSwitcher.setInAnimation(in);
				mSwitcher.setOutAnimation(out);
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				Point p = DisplayUtils.getDisplaySizes(mActivity).get(seekBar.getProgress());
				String sizeStr;
				int orientation = getResources().getConfiguration().orientation;
				if(orientation == Configuration.ORIENTATION_LANDSCAPE) {
					sizeStr = p.y+"x"+p.x;
				} else {
					sizeStr = p.x+"x"+p.y;
				}
				mSwitcher.setText(sizeStr);
			}
		});
		
		
		final ImageView musicPref = (ImageView)root.findViewById(R.id.music_toggle);
		musicPref.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(GamePreferences.get(mActivity).getMusicPreference()) {
					GamePreferences.get(mActivity).setMusicPreference(false);
					musicPref.setImageResource(R.drawable.music_off);
					Sounds.get(mActivity).stopMusic(mActivity);
				} else {
					GamePreferences.get(mActivity).setMusicPreference(true);
					musicPref.setImageResource(R.drawable.music_on);
					Sounds.get(mActivity).startMusic(mActivity);
				}
				
			}});
		
		if(!GamePreferences.get(mActivity).getMusicPreference()) {
			musicPref.setImageResource(R.drawable.music_off);
			Sounds.get(mActivity).stopMusic(mActivity);
		} else {
			musicPref.setImageResource(R.drawable.music_on);
			Sounds.get(mActivity).startMusic(mActivity);
		}
		
		final ImageView soundPref = (ImageView)root.findViewById(R.id.sound_toggle);
		soundPref.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(GamePreferences.get(mActivity).getSoundsPreference()) {
					GamePreferences.get(mActivity).setSoundsPreference(false);
					soundPref.setImageResource(R.drawable.sound_off);
				} else {
					GamePreferences.get(mActivity).setSoundsPreference(true);
					soundPref.setImageResource(R.drawable.sound_on);
				}
				
			}});
		
		if(!GamePreferences.get(mActivity).getSoundsPreference()) {
			soundPref.setImageResource(R.drawable.sound_off);
		} else {
			soundPref.setImageResource(R.drawable.sound_on);
		}
		
		
		updateStats(mActivity.getGameState());
		
		return root;
	}

	@Override
	/// View factory method, produces a text view for the TextSwitcher
	public View makeView() {
		TextView t = new TextView(mActivity);
		t.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
		t.setTextSize(18);
		LayoutParams p = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		t.setLayoutParams(p);
		t.setTextColor(Color.parseColor("#BBBBBB"));
		t.setTypeface(Typeface.createFromAsset(mActivity.getAssets(),"Roboto-Light.ttf"));
		return t;
	}
	
	public void updateStats(GameState pGameState) {
		
		if(mLastPosition == -1 ) {
			for(int i = 0; i<mValues.length; i++) {
				if(mValues[i].getTitle().equals(pGameState.getImageName())) {
					mLastPosition = i;
					mOptionsList.setSelectionFromTop(i, 0);
					mOptionsAdapter.setSelected(i);
				}
			}
		}
		String title = mValues[mLastPosition].getTitle();
		
		int x = pGameState.getX(Configuration.ORIENTATION_PORTRAIT);
		int y = pGameState.getY(Configuration.ORIENTATION_PORTRAIT);
		
		int moves = GamePreferences.get(mActivity).loadMoves(title, x, y);
		int times = GamePreferences.get(mActivity).loadTimes(title, x, y);
		   
		if(mPuzzleName != null) {
			mPuzzleName.setText(title);
			mBestTime.setText(TimeUtils.intToMinutes(times));
			mBestMoves.setText(moves > 0 ? (moves+"") : "-");
		}
   	   	if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
   	   		mOptionsAdapter.notifyDataSetChanged();
	}
	
	public void scrollToSelected() {
		if(mLastPosition >=0 ) {
			mOptionsList.setSelectionFromTop(mLastPosition, 0);
		}
	}
}