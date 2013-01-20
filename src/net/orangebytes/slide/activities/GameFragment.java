package net.orangebytes.slide.activities;

import net.orangebytes.slide.R;
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
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
	
	/// The views
	private View mViews[];
	
	/// The current game state
	private GameState mGameState;
	
	@Override
    /// Creates the view for this fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	
		mActivity = getActivity();
		
    	View root = inflater.inflate(R.layout.game_fragment, container, false);
    	
    	mGameGrid = (RelativeLayout) root.findViewById(R.id.game_grid);
    	mFlipper = (ViewFlipper) root.findViewById(R.id.game_flipper);
        mPreview = (ImageView) root.findViewById(R.id.preview_image);
        
    	Animation in  = AnimationUtils.loadAnimation(mActivity, R.anim.grow_from_middle);
    	Animation out = AnimationUtils.loadAnimation(mActivity, R.anim.shrink_to_middle);
    	mFlipper.setInAnimation(in);
    	mFlipper.setOutAnimation(out);
    	
    	  
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
    	
    	
    	mGameState = GamePreferences.get(mActivity).loadGameState();
		if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			setPuzzle(mGameState.getImage(), mGameState.getY(), mGameState.getX());
		} else {
			setPuzzle(mGameState.getImage(), mGameState.getX(), mGameState.getY());
		}
    	
        return root;
    }
	
	@Override
	public void onPause () {
		super.onPause();
		GamePreferences.get(mActivity).storeGameState(mGameState);
	}
    
	
	/// Toggles the view
	public void toggleView() {
		mFlipper.showNext();
	}
	
	/// Sets the puzzle, given an image or size
    public void setPuzzle(int pImage, int pSizeX, int pSizeY) {
    	
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
    	Log.d("TileScale", tileScale + "");
    	int tileCount = mGameState.getX() * mGameState.getY();
    	
    	int gridWidth = (tileSize + tilePadding)* mGameState.getX();
    	int gridHeight = (tileSize + tilePadding)* mGameState.getY();
    	
    	
    	mPreview.setImageBitmap(Bitmap.createBitmap(image,0,0,tileScale*mGameState.getX(),tileScale*mGameState.getY()));
    	LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(gridWidth, gridHeight);
    	mPreview.setLayoutParams(params);
    	
    	
    	mGameGrid.removeAllViews();
    	mGameGrid.setLayoutParams(new RelativeLayout.LayoutParams(gridWidth, gridHeight));
    	mViews = new View[tileCount];
    	
    	LayoutInflater viewInflator = (LayoutInflater)mActivity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	
    	int count = 0;
    	for(int i = 0; i < mGameState.getX(); i++)
    	{
    		for(int j = 0; j< mGameState.getY(); j++)
    		{
    	    	int xPos = (tileSize + tilePadding) * i;
    	    	int yPos = (tileSize + tilePadding) * j;
    	    	

    	    	Bitmap tileImage = Bitmap.createBitmap(image, i*tileScale, j*tileScale, tileScale, tileScale);
    	    	
    	    	ImageView tile = (ImageView)viewInflator.inflate(R.layout.puzzle_tile, null);
    	    	tile.setLayoutParams(TileUtils.getTileLayout(tileSize, xPos, yPos));
    	    	tile.setOnTouchListener(this);
    	    	
    	    	mViews[count] = tile;
    	    	
    	    	if(count == tileCount-1) {
    	    		tile.setTag(new PuzzleTile(tile,true,count));
    	    		tile.setBackgroundColor(getResources().getColor(R.color.clear));
    	    	} else {
    	    		tile.setTag(new PuzzleTile(tile,count));
    	    		tile.setImageBitmap(tileImage);
    	    	}

    	    	mGameGrid.addView(tile);
    	    	count++;
    		}
    	}
    	
    	int index = 0;
    	for(int i = 0; i < mGameState.getX(); i++){
    		for(int j = 0; j< mGameState.getY(); j++){
                if(j > 0){
                	((PuzzleTile)mViews[index].getTag()).mNeighbours[1] = ((PuzzleTile)mViews[index-1].getTag());
                }
                if(j< mGameState.getY()-1){
                	((PuzzleTile)mViews[index].getTag()).mNeighbours[3] = ((PuzzleTile)mViews[index+1].getTag());
                }
                if(i > 0){
                	((PuzzleTile)mViews[index].getTag()).mNeighbours[0] = ((PuzzleTile)mViews[index-mGameState.getY()].getTag());
                }
                if(i< mGameState.getX()-1){
                	((PuzzleTile)mViews[index].getTag()).mNeighbours[2] = ((PuzzleTile)mViews[index+mGameState.getY()].getTag());
                }
                index++;
    		}
    	}
    	
    }

	@Override
    public boolean onTouch(View v, MotionEvent e) {
    	if(e.getAction() == MotionEvent.ACTION_UP) {
    		for(int i = 0; i<4; i++) {
    			PuzzleTile p = (PuzzleTile)v.getTag();
    			if (p.canSlide(i)){
    				p.swap(i);
    				break;
    			}
    		}
    	}
        return true;
    }
}