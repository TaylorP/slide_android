package net.orangebytes.slide.activities;

import net.orangebytes.slide.R;
import net.orangebytes.slide.animation.AnimationFactory;
import net.orangebytes.slide.animation.AnimationFactory.FlipDirection;
import net.orangebytes.slide.model.Puzzle;
import net.orangebytes.slide.model.PuzzleTile;
import net.orangebytes.slide.preferences.GamePreferences;
import net.orangebytes.slide.preferences.GameState;
import net.orangebytes.slide.utils.FontUtils;
import net.orangebytes.slide.utils.TileUtils;
import net.orangebytes.slide.utils.TimeUtils;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

/// The fragment that contain the main game view
public class GameFragment extends Fragment implements OnTouchListener{
	
	/// The activity this fragment is in, used as it's context as well
	private MainActivity mActivity;
	
	/// The layout that will hold the game tiles
	private RelativeLayout mGameGrid;
	
	/// The text for displaying the time
	private TextView mTimeText;
	
	/// The view flipper, for toggling the puzzle and solution
	private ViewFlipper mFlipper;
	
	/// The preview image
	private ImageView mPreview;
	
	/// The current game state
	private GameState mGameState;
	
	/// The current puzzle
	private Puzzle mPuzzle;
	
	/// The views
	private ImageView mViews[];
	
	/// The current time
	private int mTime = -1;
	
	///Runnable to check completion of the puzzle
	private final Runnable mCompletionCheck = new Runnable() {
		public void run() {
			if (mPuzzle.isSolved(mGameState, mActivity)) {
				mViews[mGameState.getSize() - 1].setImageResource(R.drawable.shuffle);
				toggleView();
				GamePreferences.get(mActivity).saveTimes(mTime, mGameState.getImageName(), mGameState.getX(), mGameState.getY());
				mActivity.onComplete(mGameState);
				mTime = -1;
			}
		}
	};
	
	///Runnable to tick timer
	private final Runnable mTickTimer = new Runnable() {
		public void run() {
			if(mPuzzle.isActive()) {
				mTime++;
				mTimeText.setText(TimeUtils.intToMinutes(mTime));
				tick();
			}
		}
	};
	
	
	@Override
    /// Creates the view for this fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	
		mActivity = (MainActivity) getActivity();
    	View root = inflater.inflate(R.layout.game_fragment, container, false);
    	
    	mGameState = GamePreferences.get(mActivity).loadGameState();
    	mPuzzle = Puzzle.get();
    	
    	mGameGrid = (RelativeLayout) root.findViewById(R.id.game_grid);
    	mFlipper  = (ViewFlipper) root.findViewById(R.id.game_flipper);
        mPreview  = (ImageView) root.findViewById(R.id.preview_image);
    	mTimeText = (TextView) root.findViewById(R.id.time_text);
    	
    	mTimeText.setTypeface(FontUtils.getRobotoLight(getActivity()));
    	
    	ImageView v = (ImageView)root.findViewById(R.id.options_button);
    	v.setClickable(true);
    	v.setOnClickListener(new OnClickListener() {
    		   @Override
    		   public void onClick(View v) {
    		      ((MainActivity)mActivity).toggleOptions();
    		   }
    		  });
    	
    	ImageView i = (ImageView)root.findViewById(R.id.flip_button);
    	i.setClickable(true);
    	i.setOnClickListener(new OnClickListener() {
 		   @Override
 		   public void onClick(View v) {
 		      ((MainActivity)mActivity).togglePreview();
 		   }
 		  });
    	
    	setPuzzle(mGameState.getImageName(), mGameState.getX(), mGameState.getY());
    	
        return root;
    }
	
	@Override
	///Called when the activity pauses - use this to store the game state
	public void onPause () {
		super.onPause();
		GamePreferences.get(mActivity).storeGameState(mGameState);
	}
    
	/// Toggles the view
	public void toggleView() {
		if(!mPuzzle.isShuffling()) {
			AnimationFactory.flipTransition(mFlipper, FlipDirection.RIGHT_LEFT);
			
			if(!mPuzzle.isActive()) {
				mTimeText.setText("0:00");
			}
		}
	}
	
	/// Gets the child view for a give position
	private ImageView childAtPosition(int pX, int pY) {
		for(int i =0; i<mViews.length; i++) {
		    Rect _bounds = new Rect();
		    mViews[i].getHitRect(_bounds);
		    if (_bounds.contains(pX, pY)) {
		    	return mViews[i];
		    }
		}
		
		return null;
	}
	
	/// Sets the puzzle, given an image and/or size
    public void setPuzzle(String pImageName, int pSizeX, int pSizeY) {
    	
    	if(mPuzzle.isShuffling()) {
    		mPuzzle.stopShuffle();
    	}
    	
    	int imageRes = -1;
    	if(pImageName == null) {
    		imageRes = mGameState.getImage();
    	} else {
	   		Resources res = mActivity.getResources();
			imageRes = res.getIdentifier(pImageName, "drawable", mActivity.getPackageName());
    		mGameState.setImage(imageRes);
    		mGameState.setImageName(pImageName);
    	}
    	
    	if(pSizeX == -1 || pSizeY == -1) {
    		pSizeX = mGameState.getX();
    		pSizeY = mGameState.getY();
    	} else {
    		mGameState.setX(pSizeX);
    		mGameState.setY(pSizeY);
    	}
    	
    	mTime = -1;
    
    	Bitmap image = BitmapFactory.decodeResource(getResources(), imageRes);
    	
    	int tileSize = TileUtils.getTileSize(mActivity, mGameState); // This is the UI view size
    	int tilePadding = TileUtils.getTilePadding();
    	int tileScale = TileUtils.getTileScale(image, mGameState, getResources()); // This is the actual image size
    	
    	int gridWidth = (tileSize + tilePadding)* mGameState.getX();
    	int gridHeight = (tileSize + tilePadding)* mGameState.getY();
    	
    	mPreview.setImageBitmap(Bitmap.createBitmap(image,0,0,tileScale*mGameState.getX(),tileScale*mGameState.getY()));
    	LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(gridWidth, gridHeight);
    	mPreview.setLayoutParams(params);
    	
    	mGameGrid.removeAllViews();
    	mGameGrid.setLayoutParams(new RelativeLayout.LayoutParams(gridWidth, gridHeight));
    	mGameGrid.setOnTouchListener(this);
    	mViews = new ImageView[mGameState.getSize()];
    	
    	LayoutInflater viewInflator = (LayoutInflater)mActivity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	
    	int count = 0;
    	for(int i = 0; i < mGameState.getX(); i++){
    		for(int j = 0; j< mGameState.getY(); j++){
    	    	int xPos = (tileSize + tilePadding) * i;
    	    	int yPos = (tileSize + tilePadding) * j;
    	    	
    	    	Bitmap tileImage = Bitmap.createBitmap(image, i*tileScale, j*tileScale, tileScale, tileScale);
    	    	RelativeLayout.LayoutParams tileLayout = TileUtils.getTileLayout(tileSize, xPos, yPos);
    	    	
    	    	ImageView tile = (ImageView)viewInflator.inflate(R.layout.puzzle_tile, null);
    	    	tile.setLayoutParams(tileLayout);
    	    	
    	    	if(count == mGameState.getSize()-1) {;
    	    		tile.setBackgroundColor(getResources().getColor(R.color.clear));
    	    		tile.setImageResource(R.drawable.shuffle);
    	    	} else {
    	    		tile.setImageBitmap(tileImage);
    	    	}
    	    	
    	    	mGameGrid.addView(tile);
    	    	mViews[count] = tile;
    	    	
    	    	count++;
    		}
    	}
    	mPuzzle.generateLinks(mGameState);
    	mPuzzle.linkPuzzle(mGameState, mViews);
    	
    	if(mFlipper.getDisplayedChild() == 1) {
    		toggleView();
    	}
    }
    
    private void tick() {
    	if(mPuzzle.isActive()) {
    		Handler h = new Handler();
    		h.postDelayed(mTickTimer, 1000);
    	}
    }

	@Override
    public boolean onTouch(View view, MotionEvent e) {
		if(mPuzzle.isActive()) {
			if(mTime == -1) {
				mTime = 0;
				tick();
			}
			
			if(e.getAction() == MotionEvent.ACTION_DOWN) {
				View v = childAtPosition((int)e.getX(), (int)e.getY());
				if(v != null) {
					mPuzzle.touchDown(v, e.getX(), e.getY());
				}
				return true;
				
			} else if (e.getAction() == MotionEvent.ACTION_MOVE) {
				mPuzzle.touchMove(e.getX(), e.getY());
				return true;
				
			} else if(e.getAction() == MotionEvent.ACTION_UP) {
				if(mPuzzle.touchFinished(e.getX(), e.getY())) {
					final Handler handler = new Handler();
					handler.postDelayed(mCompletionCheck,150);
					return true;
				}
				return true;
			}
		} else {
			if(e.getAction() == MotionEvent.ACTION_DOWN) {
				if(mPuzzle.isShuffling()) {
					mPuzzle.speedShuffle();
				} else {
					ImageView v = childAtPosition((int)e.getX(), (int)e.getY());
					if(v != null) {
						PuzzleTile p = (PuzzleTile)v.getTag();
						if(p.isEmpty()) {
							v.setImageBitmap(null);
							mPuzzle.shuffle(mGameState);
						}
					}
				}
			}
		}
		
		return false;
    }
}