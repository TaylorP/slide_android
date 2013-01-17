package net.orangebytes.slide.activities;

import net.orangebytes.slide.R;
import net.orangebytes.slide.adapters.OptionsListAdapter;
import net.orangebytes.slide.model.PuzzleInfo;
import net.orangebytes.slide.utils.TimeUtils;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

/// The options fragment, for the options menu of the game
public class OptionsFragment extends Fragment implements ViewSwitcher.ViewFactory {

	/// The options list, containing the available puzzles and some settings
	private ListView mOptionsList;
	
	/// The adapter for the options list
	private OptionsListAdapter mOptionsAdapter;
	
	/// An array of puzzles in the game. TODO: load these from shared preferences
	private PuzzleInfo[] mValues;
	
	/// TextSwitcher for the grid size text
	private TextSwitcher mSwitcher;

	/// The last selected position
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
		
		mValues = new PuzzleInfo[] {
				new PuzzleInfo("lion", "lion_thumb", 10, 20),
				new PuzzleInfo("desert", "desert_thumb", 20, 30),
				new PuzzleInfo("fruit", "fruit_thumb", 122, 30),
				new PuzzleInfo("steak", "steak_thumb", 45, 30),
				new PuzzleInfo("flower", "flower_thumb", 45, 30),
				new PuzzleInfo("plant", "plant_thumb", 45, 32),
				new PuzzleInfo("orange", "orange_thumb", 15, 20),
				new PuzzleInfo("snow", "snow_thumb", 15, 20),
				new PuzzleInfo("bird", "bird_thumb", 15, 20),
				new PuzzleInfo("slide", "slide_thumb", 15, 20) };

		
		mOptionsAdapter = new OptionsListAdapter(this.getActivity(), mValues);
		
		mOptionsList.setAdapter(mOptionsAdapter);
		mOptionsList.setClickable(true);
		mOptionsList.setOnItemClickListener(new OnItemClickListener() {
		       @SuppressLint("NewApi")
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

		    	   
		    	   if(mLastSelected != null) {
		    		    AlphaAnimation aa = new AlphaAnimation(0.3f,0.3f);
		    		    aa.setDuration(10);
		    		    aa.setFillAfter(true);
		    		    mLastSelected.startAnimation(aa);
		    	   }
		    	   
		    	   mLastSelected = view;
	    		   AlphaAnimation aa = new AlphaAnimation(0.3f,0.8f);
	    		   aa.setDuration(10);
	    		   aa.setFillAfter(true);
	    		   view.startAnimation(aa);
	    		   PuzzleInfo p = ((OptionsListAdapter)mOptionsList.getAdapter()).getInfo(position);
	    		   if(mPuzzleName != null) { 
	    			   mPuzzleName.setText(p.getTitle());
	    			   mBestTime.setText(TimeUtils.intToMinutes(p.getTime()));
	    			   mBestMoves.setText(p.getMoves()+"");
	    		   }
	    		    
		    	   if (android.os.Build.VERSION.SDK_INT >= 11)
		    	   {
		    		   mOptionsList.smoothScrollToPositionFromTop(position, 0);

		    	   }
		    	   else if (android.os.Build.VERSION.SDK_INT >= 8)
		    	   {
		    	       int firstVisible = mOptionsList.getFirstVisiblePosition();
		    	       int lastVisible = mOptionsList.getLastVisiblePosition();
		    	       if (position < firstVisible)
		    	    	   mOptionsList.smoothScrollToPosition(position);
		    	       else
		    	    	   mOptionsList.smoothScrollToPosition(position + lastVisible - firstVisible - 2);
		    	   }
		    	   else
		    	   {
		    		   mOptionsList.setSelectionFromTop(position, 0);
		    	   }
		    	   
		       }
		   });

                    

		Animation in = AnimationUtils.loadAnimation(this.getActivity(),android.R.anim.fade_in);
		Animation out = AnimationUtils.loadAnimation(this.getActivity(),R.anim.fast_fade_out);

		mSwitcher = (TextSwitcher) root.findViewById(R.id.grid_size);
		mSwitcher.setFactory(this);
		mSwitcher.setInAnimation(in);
		mSwitcher.setOutAnimation(out);
		mSwitcher.setText(getActivity().getString(R.string.grid_size));

		final SeekBar sk = (SeekBar) root.findViewById(R.id.game_size_bar);
		sk.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

				Animation in = AnimationUtils.loadAnimation(
						OptionsFragment.this.getActivity(), R.anim.slow_fade_in);
				Animation out = AnimationUtils.loadAnimation(
						OptionsFragment.this.getActivity(),
						R.anim.slow_fade_out);
				mSwitcher.setInAnimation(in);
				mSwitcher.setOutAnimation(out);
				mSwitcher.setText(getActivity().getString(R.string.grid_size));
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

				Animation in = AnimationUtils.loadAnimation(
						OptionsFragment.this.getActivity(),
						android.R.anim.fade_in);
				Animation out = AnimationUtils.loadAnimation(
						OptionsFragment.this.getActivity(),
						R.anim.fast_fade_out);
				mSwitcher.setInAnimation(in);
				mSwitcher.setOutAnimation(out);
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

				switch (progress) {
				case 0:
					mSwitcher.setText("3x3");
					break;
				case 1:
					mSwitcher.setText("3x4");
					break;
				case 2:
					mSwitcher.setText("4x4");
					break;
				case 3:
					mSwitcher.setText("4x5");
					break;
				case 4:
					mSwitcher.setText("5x5");
					break;
				}

			}
		});

		return root;
	}

	@Override
	/// View factory method, produces a text view for the TextSwitcher
	public View makeView() {
		TextView t = new TextView(getActivity());
		t.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
		t.setTextSize(18);
		LayoutParams p = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		t.setLayoutParams(p);
		t.setTextColor(Color.parseColor("#BBBBBB"));
		t.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),
				"Roboto-Light.ttf"));
		return t;
	}
}