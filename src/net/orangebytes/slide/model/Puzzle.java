package net.orangebytes.slide.model;

import net.orangebytes.slide.activities.GameFragment;
import net.orangebytes.slide.preferences.GamePreferences;
import net.orangebytes.slide.preferences.GameState;
import net.orangebytes.slide.utils.Sounds;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

/// Singleton puzzle class, for storing the puzzle
public class Puzzle {
	
	/// The shared instance
	private static Puzzle sInstance = null;
	
	/// Whether or not a puzzle is in progress
	private boolean mPuzzleActive;
	
	/// Whether or not a puzzle is being shuffled
	private boolean mShuffling;
	
	/// Shuffling direction
	private int mLastDirection = -1;
	
	/// Shuffling mix count
	private int mMixCount = 0;
	
	/// Move count for current puzzle
	private int mMoveCount = 0;
	
	/// The model for the tiles
	private PuzzleTile mPuzzleTiles[];
	
	private boolean mStopFlag = false;
	private boolean mSpeedFlag = false;
	
	
	private View  mView;
	private float mLastX;
	private float mLastY;
	private float mMaxDeltaX;
	private float mMaxDeltaY;
	private boolean mSliding;
	private boolean mBlock;
	
	
	/// Private constructor
	private Puzzle() {
		mPuzzleActive = false;
	}
	
	/// Private method for shuffling the puzzle recursively
	private void shufflePuzzle(final GameState pGameState, final GameFragment pFragment) {
		if(mStopFlag) {
			mStopFlag = false;
			return;
		}
		
	    int cellCount = mPuzzleTiles.length;
	    int cell = (int) (Math.random()*(cellCount-1));
	    int dir = (int) (Math.random()*4);
	    
	    while(dir%2 == mLastDirection%2 && (Math.random()*100 < 80)) {
	        dir = (int) (Math.random()*4);
	    }

	    if (mPuzzleTiles[cell].canSlide(dir)) {
	        mLastDirection = dir;
	        mMixCount++;
	        
	        if(mMixCount >= pGameState.getSize() * 3){
	        	mMixCount = 0;
	        	mLastDirection = -1;
	        	mShuffling = false;
	        	mPuzzleActive = true;
	        	mSpeedFlag = false;
	        	pFragment.doneShuffle();
	            return;
	        }
	        
	        if(mSpeedFlag) {
	        	mPuzzleTiles[cell].swap(dir, 0);
	        } else {
	        	mPuzzleTiles[cell].swap(dir, 1);
	        }
	        
	        Sounds.get(pFragment.getActivity()).playSound(pFragment.getActivity());

	        int time = 110;
	        if(mSpeedFlag) {
	        	time = 30;
	        }
	        
	        final Handler handler = new Handler();
	        handler.postDelayed(new Runnable() {
	          @Override
	          public void run() {
	        	  shufflePuzzle(pGameState, pFragment);
	          }
	        }, time);
	    }else{
	    	shufflePuzzle(pGameState, pFragment);
	    }
	}
	
	/// Singleton accessor
	public static Puzzle get() {
		if(sInstance == null) {
			sInstance = new Puzzle();
		}
		return sInstance;
	}
	
	/// Returns true if the puzzle is active
	public boolean isActive() {
		return mPuzzleActive;
	}
	
	/// Returns true if the puzzle is being shuffled
	public boolean isShuffling() {
		return mShuffling;
	}

	/// Generates the initial puzzle model links for a given puzzle size
	public void generateLinks(GameState pState, int pOrientation) {
		mPuzzleActive = false;
		
		mPuzzleTiles = new PuzzleTile[pState.getSize()];
    	int index = 0;

    	for(int i = 0; i < pState.getX(pOrientation); i++){
    		for(int j = 0; j< pState.getY(pOrientation); j++){
    			mPuzzleTiles[index] = new PuzzleTile();
    			index++;
    		}
    	}
    	
    	index = 0;
    	for(int i = 0; i < pState.getX(pOrientation); i++){
    		for(int j = 0; j< pState.getY(pOrientation); j++){
                if(j > 0){
                	mPuzzleTiles[index].mNeighbours[1] = mPuzzleTiles[index-1];
                }
                if(j< pState.getY(pOrientation)-1){
                	mPuzzleTiles[index].mNeighbours[3] = mPuzzleTiles[index+1];
                }
                if(i > 0){
                	mPuzzleTiles[index].mNeighbours[0] = mPuzzleTiles[index-pState.getY(pOrientation)];
                }
                if(i< pState.getX(pOrientation)-1){
                	mPuzzleTiles[index].mNeighbours[2] = mPuzzleTiles[index+pState.getY(pOrientation)];
                }
                index++;
    		}
    	}
	}
	
	/// Links a set of views into the puzzle
	public void linkPuzzle(GameState pState, ImageView pViews[], int pOrientation) {
    	int index = 0;
    	for(int i = 0; i < pState.getX(pOrientation); i++){
    		for(int j = 0; j< pState.getY(pOrientation); j++){
                pViews[index].setTag(mPuzzleTiles[index]);
                mPuzzleTiles[index].setView(pViews[index]);
                
                if(index == pState.getSize()-1)
                	mPuzzleTiles[index].setEmpty(true);
                
                index++;
    		}
    	}
	}

	/// Shuffles a puzzle for a given game state
	public void shuffle(GameState pGameState, GameFragment pFragment) {
		mShuffling = true;
		mLastDirection = -1;
		mMixCount = 0;
		mMoveCount = 0;
		
		shufflePuzzle(pGameState, pFragment);
	}
	
	/// Aborts the shuffle
	public void stopShuffle() {
		mStopFlag = true;
		mShuffling = false;
	}
	
	/// speeds up the shuffle
	public void speedShuffle() {
		mSpeedFlag = true;
	}
	
	/// Returns true if the puzzle has been solved
	public boolean isSolved(GameState pState, Context pContext, int pOrientation) {
		if(!isActive() && !isShuffling())
			return false;
		
		int index = 0;
    	for(int i = 0; i < pState.getX(pOrientation); i++){
    		for(int j = 0; j< pState.getY(pOrientation); j++){
    			
                if(j > 0){
                	if(mPuzzleTiles[index].mNeighbours[1] != mPuzzleTiles[index-1])
                		return false;
                }
                if(j< pState.getY(pOrientation)-1){
                	if(mPuzzleTiles[index].mNeighbours[3] != mPuzzleTiles[index+1])
                		return false;
                }
                if(i > 0){
                	if(mPuzzleTiles[index].mNeighbours[0] != mPuzzleTiles[index-pState.getY(pOrientation)])
                		return false;
                }
                if(i< pState.getX(pOrientation)-1){
                	if(mPuzzleTiles[index].mNeighbours[2] != mPuzzleTiles[index+pState.getY(pOrientation)])
                		return false;
                }
                index++;
		    }
    	}  
    	
    	mPuzzleActive = false;
    	GamePreferences.get(pContext).saveMoves(mMoveCount, pState.getImageName(), pState.getX(pOrientation), pState.getY(pOrientation));
    	
    	return true;
	}
	
	
	public void touchDown(View pView, float pX, float pY, Context pContext) {
		mSliding = false;
		mView = pView;
		mLastX = pX;
		mLastY = pY;
		mBlock = false;
		mMaxDeltaX = 0;
		mMaxDeltaY = 0;
		

	}
	
	public void touchMove(float pX, float pY, Context pContext) {
		
		if(mView != null && !mBlock) {
			PuzzleTile p = (PuzzleTile)mView.getTag();
			
			if(p.isEmpty())
				return;
			
			mSliding = true;
			
			float deltaX = (pX - mLastX);
			float deltaY = (pY - mLastY);
			
			mLastX = pX;
			mLastY = pY;
			
			if(deltaX < 0) {
				if(p.canSlide(0)){
					mLastDirection = 0;
					mMaxDeltaX=Math.min(deltaX, mMaxDeltaX);
					mBlock = p.slide(0, (int) deltaX);
					if(mBlock) {
						Sounds.get(pContext).playSound(pContext);
					}
					return;
				}
			} else {
				if(p.canSlide(2)) {
					mLastDirection = 2;
					mMaxDeltaX=Math.max(deltaX, mMaxDeltaX);
					mBlock = p.slide(2, (int) deltaX);
					if(mBlock) {
						Sounds.get(pContext).playSound(pContext);
					}
					return;
				}
			}
			
			if(deltaY < 0) {
				if(p.canSlide(1)) {
					mLastDirection = 1;
					mMaxDeltaY=Math.min(deltaY, mMaxDeltaY);
					mBlock = p.slide(1, (int) deltaY);
					if(mBlock) {
						Sounds.get(pContext).playSound(pContext);
					}	
					return;
				}
			}
			else {
				if(p.canSlide(3)) {
					mLastDirection = 3;
					mMaxDeltaY=Math.max(deltaY, mMaxDeltaY);
					mBlock = p.slide(3, (int) deltaY);
					if(mBlock) {
						Sounds.get(pContext).playSound(pContext);
					}		
					return;
				}
			}
		}
	}
	
	public boolean touchFinished(float pX, float pY, Context pContext) {
		if(mBlock) {
			mMoveCount++;
			return true;
		}
		
		if(mView != null) {
			
			PuzzleTile p = (PuzzleTile)mView.getTag();
			
			if(mSliding) {
				if(mMaxDeltaX <= -50) {
					if(p.canSlide(0)){
						p.swap(0, 0);
						mMoveCount++;
						Sounds.get(pContext).playSound(pContext);
						return true;
					}
				} else if(mMaxDeltaX >= 50) {
					if(p.canSlide(2)) {
						p.swap(2, 0);
						mMoveCount++;
						Sounds.get(pContext).playSound(pContext);
						return true;
					}
				}
				
				if(mMaxDeltaY <= -50) {
					if(p.canSlide(1)) {
						p.swap(1, 0);
						mMoveCount++;
						Sounds.get(pContext).playSound(pContext);
						return true;
					}
				}
				else if(mMaxDeltaY >= 50) {
					if(p.canSlide(3)) {
						p.swap(3, 0);
						mMoveCount++;
						Sounds.get(pContext).playSound(pContext);
						return true;
					}
				}
				
				float halfWay = mView.getWidth() / 3;
				float deltaX = (mView.getLeft() - p.getRealLayout().leftMargin);
				float deltaY = (mView.getTop() - p.getRealLayout().topMargin);
				
				if(mLastDirection == 0 || mLastDirection == 2) {
					if(Math.abs(deltaX) >= halfWay ) {
						p.swap(mLastDirection, 2);
						mMoveCount++;
						Sounds.get(pContext).playSound(pContext);
						return true;
					} else if(Math.abs(deltaX) >= 10) {
						p.unslide(mLastDirection);
						Sounds.get(pContext).playSound(pContext);
						return false;
					}
				} else {
					if(Math.abs(deltaY) >= halfWay) {
						p.swap(mLastDirection, 2);
						mMoveCount++;
						Sounds.get(pContext).playSound(pContext);
						return true;
					} else if(Math.abs(deltaY) >= 10) { 
						p.unslide(mLastDirection);
						Sounds.get(pContext).playSound(pContext);
						return false;
					}
				}
			}
			
    		for(int i = 0; i<4; i++) {
    			if (p.canSlide(i) && !p.isEmpty()){
    				p.swap(i, 1);
    				mMoveCount++;
    				Sounds.get(pContext).playSound(pContext);
    				return true;
    			}
    		}
		}
		
		return false;
	}
}