package net.orangebytes.slide.model;

import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class PuzzleTile {
	private boolean mEmptyCell;
	public PuzzleTile mNeighbours[];
	private View mView;
	private RelativeLayout.LayoutParams mRealLayout;
	
	public PuzzleTile() {
		mNeighbours = new PuzzleTile[4];
		mEmptyCell = false;
	}
	
	public void setView(View pView) {
		mView = pView;
		mRealLayout = new LayoutParams((LayoutParams) mView.getLayoutParams());
	}
	
	public void setEmpty(boolean pEmpty) {
		mEmptyCell = pEmpty;
	}
	
	public boolean isEmpty() {
		return mEmptyCell;
	}
	
	public boolean canSlide(int pDirection) {
		if (mEmptyCell) {
	        return true;
	    }
	    
	    if( mNeighbours[pDirection] != null && mNeighbours[pDirection].canSlide(pDirection)){
	        return true;
	    }
	    
	    return false;
	}
	
	public void slide(int pDirection, int amount) {
	    if(mEmptyCell)
	        return;
	    
	    if(mNeighbours[pDirection] != null) {
	    	mNeighbours[pDirection].slide(pDirection, amount);
	    }
	    
	    final RelativeLayout.LayoutParams params = (LayoutParams) mView.getLayoutParams();
	    
	    if(pDirection == 0 || pDirection == 2) {
	    	params.leftMargin += amount;
	    } else {
	    	params.topMargin += amount;
	    }
	    
	    mView.setLayoutParams(params);
	}
	
	public void unslide(int pDirection, int amount) {
	    if(mEmptyCell)
	        return;
	    
	    if(mNeighbours[pDirection] != null) {
	    	mNeighbours[pDirection].unslide(pDirection, amount);
	    }
	    
	    final RelativeLayout.LayoutParams params = (LayoutParams) mView.getLayoutParams();
	    
	    if(pDirection == 0 || pDirection == 2) {
	    	params.leftMargin -= amount;
	    } else {
	    	params.topMargin -= amount;
	    }
	    
	    mView.setLayoutParams(params);
	}
	
	public void swap(int pDirection)
	{
	    if(mEmptyCell)
	        return;
	    
	    if(mNeighbours[pDirection] != null)
	        mNeighbours[pDirection].swap(pDirection);
	    
	    final PuzzleTile swapDest = mNeighbours[pDirection];
	    PuzzleTile dstCells[] = new PuzzleTile[4];
	    PuzzleTile srcCells[] = new PuzzleTile[4];
	    
	    for(int i =0; i<4; i++)
	    {
	        dstCells[i] = swapDest.mNeighbours[i];
	        srcCells[i] = mNeighbours[i];
	    }
	    
	    for(int i =0; i<4; i++)
	    {
	        if (dstCells[i] == this) 
	        {
	            this.mNeighbours[i] = swapDest;
	        }
	        else 
	        {
	        	this.mNeighbours[i] = dstCells[i];
	        	if(dstCells[i] != null)
	        		dstCells[i].mNeighbours[(i+2)%4] = this;
	        }
	        
	        if(srcCells[i] == swapDest)
	        {
	        	if(swapDest != null) 
	        		swapDest.mNeighbours[i] = this;
	        }
	        else 
	        {
	        	if(swapDest != null) 
	        		swapDest.mNeighbours[i] = srcCells[i];
	        	
	        	if(srcCells[i] != null)
	        		srcCells[i].mNeighbours[(i+2)%4] = swapDest;
	        }
	    }
	    
	  
	    final int srcX = mRealLayout.leftMargin;
	    final int srcY = mRealLayout.topMargin;
	    
	    final int destX = swapDest.mRealLayout.leftMargin;
	    final int destY = swapDest.mRealLayout.topMargin;

	    TranslateAnimation t = new TranslateAnimation (destX - srcX, 0, destY-srcY, 0);
	    t.setDuration(100);
	    swapDest.mView.startAnimation(t);
	    
	    TranslateAnimation t2 = new TranslateAnimation (mView.getLeft()-destX, 0, mView.getTop()-destY, 0);
	    t2.setDuration(100);
	    mView.startAnimation(t2);
	  
	    swapDest.mView.offsetLeftAndRight(srcX-destX);
	    swapDest.mView.offsetTopAndBottom(srcY-destY);
	    mView.offsetLeftAndRight(destX-mView.getLeft());
	    mView.offsetTopAndBottom(destY-mView.getRight());
	    
    	swapDest.mView.setLayoutParams(new LayoutParams(mRealLayout));
    	mView.setLayoutParams(new LayoutParams(swapDest.mRealLayout));
    	
    	RelativeLayout.LayoutParams temp = swapDest.mRealLayout;
    	swapDest.mRealLayout = mRealLayout;
    	mRealLayout = temp;
	}
}