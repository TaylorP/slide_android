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
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
	private Activity mActivity;
	
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
	
	@Override
    /// Creates the view for this fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	
		mActivity = getActivity();
    	View root = inflater.inflate(R.layout.game_fragment, container, false);
    	
    	mGameState = GamePreferences.get(mActivity).loadGameState();
    	mPuzzle = Puzzle.get();
    	
    	mGameGrid = (RelativeLayout) root.findViewById(R.id.game_grid);
    	mFlipper = (ViewFlipper) root.findViewById(R.id.game_flipper);
        mPreview = (ImageView) root.findViewById(R.id.preview_image);
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
    	
    	int orientation = getResources().getConfiguration().orientation;
    	
		if(orientation == Configuration.ORIENTATION_LANDSCAPE) {
			if(mGameState.getOrientation() == GameState.sLandscape) {
				setPuzzle(mGameState.getImage(), mGameState.getX(), mGameState.getY());
			} else {
				setPuzzle(mGameState.getImage(), mGameState.getY(), mGameState.getX());
			}
			mGameState.setOrientation(GameState.sLandscape);
		} else {
			if(mGameState.getOrientation() == GameState.sPortrait) {
				setPuzzle(mGameState.getImage(), mGameState.getX(), mGameState.getY());
			} else {
				setPuzzle(mGameState.getImage(), mGameState.getY(), mGameState.getX());
			}
			mGameState.setOrientation(GameState.sPortrait);
		}
    	
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
		AnimationFactory.flipTransition(mFlipper, FlipDirection.RIGHT_LEFT);
	}
	
	/// Sets the puzzle, given an image and/or size
    public void setPuzzle(int pImage, int pSizeX, int pSizeY) {
    	
    	mPuzzle.setActive(false);
    	
    	if(pImage == -1) {
    		pImage = mGameState.getImage();
    	} else {
    		mGameState.setImage(pImage);
    	}
    	
    	if(pSizeX == -1 || pSizeY == -1) {
    		pSizeX = mGameState.getX();
    		pSizeY = mGameState.getY();
    	} else {
    		mGameState.setX(pSizeX);
    		mGameState.setY(pSizeY);
    	}
    
    	Bitmap image = BitmapFactory.decodeResource(getResources(), pImage);
    	
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
    	    	tile.setOnTouchListener(this);
    	    	
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
    }

	@Override
    public boolean onTouch(View v, MotionEvent e) {
    	if(e.getAction() == MotionEvent.ACTION_UP) {
			PuzzleTile p = (PuzzleTile)v.getTag();
			if(p.isEmpty()) {
				if(!mPuzzle.isActive()) {
					((ImageView)v).setImageBitmap(null);
					mPuzzle.setActive(true);
					shufflePuzzle();
				}
			} else {
	    		for(int i = 0; i<4; i++) {
	    			if (p.canSlide(i)){
	    				p.swap(i);
	    				break;
	    			}
	    		}
			}
    	}
        return true;
    }
	
	static int sLastDirection = -1;
	static int sMixCount = 0;
	public void shufflePuzzle() {
		
	    int cellCount = mViews.length;
	    int cell = (int) (Math.random()*(cellCount-1));
	    int dir = (int) (Math.random()*4);
	    
	    while(dir%2 == sLastDirection%2 && (Math.random()*100 < 80)) {
	        dir = (int) (Math.random()*4);
	    }

	    if (((PuzzleTile)mViews[cell].getTag()).canSlide(dir)) {
	        sLastDirection = dir;
	        sMixCount++;
	        if(sMixCount >= mGameState.getSize() * 3){
	        	sMixCount = 0;
	        	sLastDirection = -1;
	            return;
	        }
	        
	        ((PuzzleTile)mViews[cell].getTag()).swap(dir);

	        final Handler handler = new Handler();
	        handler.postDelayed(new Runnable() {
	          @Override
	          public void run() {
	        	  shufflePuzzle();
	          }
	        }, 110);

	    }
	    else{
	    	shufflePuzzle();
	    }
	}
}