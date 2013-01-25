package net.orangebytes.slide.model;

import net.orangebytes.slide.preferences.GameState;
import android.widget.ImageView;

/// Singleton puzzle class, for storing the puzzle
public class Puzzle {
	
	/// The shared instance
	private static Puzzle sInstance = null;
	
	/// Whether or not a puzzle is in progress
	private boolean mPuzzleActive;
	
	private PuzzleTile mPuzzleTiles[];
	
	
	/// Private constructor
	private Puzzle() {
		mPuzzleActive = false;
	}
	
	/// Singleton accessor
	public static Puzzle get() {
		if(sInstance == null) {
			sInstance = new Puzzle();
		}
		return sInstance;
	}
	
	public boolean isActive() {
		return mPuzzleActive;
	}
	
	public void setActive(boolean pActive) {
		mPuzzleActive = pActive;
	}

	public void generateLinks(GameState pState) {
		
		mPuzzleTiles = new PuzzleTile[pState.getSize()];
    	int index = 0;

    	for(int i = 0; i < pState.getX(); i++){
    		for(int j = 0; j< pState.getY(); j++){
    			mPuzzleTiles[index] = new PuzzleTile();
    			index++;
    		}
    	}
    	
    	index = 0;
    	for(int i = 0; i < pState.getX(); i++){
    		for(int j = 0; j< pState.getY(); j++){
                if(j > 0){
                	mPuzzleTiles[index].mNeighbours[1] = mPuzzleTiles[index-1];
                }
                if(j< pState.getY()-1){
                	mPuzzleTiles[index].mNeighbours[3] = mPuzzleTiles[index+1];
                }
                if(i > 0){
                	mPuzzleTiles[index].mNeighbours[0] = mPuzzleTiles[index-pState.getY()];
                }
                if(i< pState.getX()-1){
                	mPuzzleTiles[index].mNeighbours[2] = mPuzzleTiles[index+pState.getY()];
                }
                index++;
    		}
    	}
	}
	
	public void linkPuzzle(GameState pState, ImageView pViews[]) {

    	int index = 0;
    	for(int i = 0; i < pState.getX(); i++){
    		for(int j = 0; j< pState.getY(); j++){
                pViews[index].setTag(mPuzzleTiles[index]);
                mPuzzleTiles[index].setView(pViews[index]);
                
                if(index == pState.getSize()-1)
                	mPuzzleTiles[index].setEmpty(true);
                
                index++;
    		}
    	}
	}
	
	public boolean isSolved(GameState pState) {
		if(!isActive())
			return false;
		
		int index = 0;
    	for(int i = 0; i < pState.getX(); i++){
    		for(int j = 0; j< pState.getY(); j++){
    			
                if(j > 0){
                	if(mPuzzleTiles[index].mNeighbours[1] != mPuzzleTiles[index-1])
                		return false;
                }
                if(j< pState.getY()-1){
                	if(mPuzzleTiles[index].mNeighbours[3] != mPuzzleTiles[index+1])
                		return false;
                }
                if(i > 0){
                	if(mPuzzleTiles[index].mNeighbours[0] != mPuzzleTiles[index-pState.getY()])
                		return false;
                }
                if(i< pState.getX()-1){
                	if(mPuzzleTiles[index].mNeighbours[2] != mPuzzleTiles[index+pState.getY()])
                		return false;
                }
                index++;
		    }
    	}  
    	
    	return true;
	}
}