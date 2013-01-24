package net.orangebytes.slide.model;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class PuzzleTile {
	private boolean mEmptyCell = false;
	public PuzzleTile mNeighbours[];
	private View mView;
	public int id;
	
	public PuzzleTile(View pView, int pId) {
		mNeighbours = new PuzzleTile[4];
		mView = pView;
		id = pId;
	}
	
	public PuzzleTile(View pView, boolean pEmpty, int pId) {
		mNeighbours = new PuzzleTile[4];
		mView = pView;
		mEmptyCell = pEmpty;
		id = pId;
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
	    
	    final int srcX = mView.getLeft();
	    final int srcY = mView.getTop();
	    
	    final int destX = swapDest.mView.getLeft();
	    final int destY = swapDest.mView.getTop();

	    TranslateAnimation t = new TranslateAnimation (-(srcX-destX), 0, -(srcY-destY), 0);
	    t.setDuration(140);
	    swapDest.mView.startAnimation(t);
	    
	    TranslateAnimation t2 = new TranslateAnimation (srcX-destX, 0, srcY-destY, 0);
	    t2.setDuration(140);
	    mView.startAnimation(t2);
	  
	    swapDest.mView.offsetLeftAndRight(srcX-destX);
	    swapDest.mView.offsetTopAndBottom(srcY-destY);
	    mView.offsetLeftAndRight(destX-srcX);
	    mView.offsetTopAndBottom(destY-srcY);
	    
    	final RelativeLayout.LayoutParams params2 = (LayoutParams)  swapDest.mView.getLayoutParams();
    	final RelativeLayout.LayoutParams params = (LayoutParams) mView.getLayoutParams();
    	swapDest.mView.setLayoutParams(params);
    	mView.setLayoutParams(params2);
	   
	 
	}
}

//
//public class PuzzleTile {
//	private static final PuzzleTile sEmptyTile = new PuzzleTile(0);
//	
//	private static final int sDown = 0;
//	private static final int sLeft = 1;
//	private static final int sUp = 2;
//	private static final int sRight = 3;
//	
//	private PuzzleTile mLeft;
//	private PuzzleTile mRight;
//	private PuzzleTile mTop;
//	private PuzzleTile mBottom;
//	
//	private int mTileSize;
//	
//	public PuzzleTile(int pTileSize) {
//		mTileSize = pTileSize;
//	}
//	
//	public boolean canMove(int pDirection) {
//		if(pDirection == sDown) {
//			if(mBottom == sEmptyTile) {
//				return true;
//			} else if (mBottom != null) {
//				return mBottom.canMove(pDirection);
//			}
//		} else if(pDirection == sLeft) {
//			if(mLeft == sEmptyTile) {
//				return true;
//			} else if(mLeft != null) {
//				return mLeft.canMove(pDirection);
//			}
//		} else if(pDirection == sRight) {
//			if(mRight == sEmptyTile) {
//				return true;
//			} else if (mRight != null) {
//				return mRight.canMove(pDirection);
//			}
//		} else if(pDirection == sUp) {
//			if(mTop == sEmptyTile) {
//				return true;
//			} else if (mTop != null) {
//				return mTop.canMove(pDirection);
//			}
//		}
//		
//		return false;
//	}
//	
//	public void swap(int pDirection) {	
//		
//		PuzzleTile tempTop = mTop;
//		PuzzleTile tempRight = mRight;
//		PuzzleTile tempLeft = mLeft;
//		PuzzleTile tempBottom = mBottom;
//		
//		if(pDirection == sDown) {
//			mTop = mBottom;
//			mRight = mBottom.mRight;
//			mLeft = mBottom.mLeft;
//			mBottom = mBottom.mBottom;
//			
//			if(mRight != null)
//				mRight.mLeft = this;
//			if(mLeft != null)
//				mLeft.mRight = this;
//			if(mBottom != null)
//				mBottom.mTop = this;
//			
//			tempBottom.mTop = tempTop;
//			tempBottom.mRight = tempRight;
//			tempBottom.mLeft = tempLeft;
//			tempBottom.mBottom = this;
//			
//			if(tempBottom.mTop != null)
//				tempBottom.mTop.mBottom = tempBottom;
//			if(tempBottom.mRight != null)
//				tempBottom.mRight.mLeft = tempBottom;
//			if(tempBottom.mLeft != null)
//				tempBottom.mLeft.mRight = tempBottom;		
//		} else if(pDirection == sLeft) {
//			mTop = mLeft.mTop;
//			mRight = mLeft;
//			mBottom = mLeft.mBottom;
//			mLeft = mLeft.mLeft;
//			
//			if(mLeft != null)
//				mLeft.mRight = this;
//			if(mBottom != null)
//				mBottom.mTop = this;
//			if(mTop != null)
//				mTop.mBottom = this;
//			
//			tempLeft.mTop = tempTop;
//			tempLeft.mRight = tempRight;
//			tempLeft.mLeft = this;
//			tempLeft.mBottom = tempBottom;
//			
//			if(tempLeft.mTop != null)
//				tempLeft.mTop.mBottom = tempLeft;
//			if(tempLeft.mRight != null)
//				tempLeft.mRight.mLeft = tempLeft;
//			if(tempLeft.mBottom != null)
//				tempLeft.mBottom.mTop = tempLeft;
//		} else if(pDirection == sRight) {
//			mTop = mRight.mTop;
//			mBottom = mRight.mBottom;
//			mLeft = mRight;
//			mRight = mRight.mRight;
//			
//			if(mRight != null)
//				mRight.mLeft = this;
//			if(mBottom != null)
//				mBottom.mTop = this;
//			if(mTop != null)
//				mTop.mBottom = this;
//			
//			tempRight.mTop = tempTop;
//			tempRight.mRight = this;
//			tempRight.mLeft = tempLeft;
//			tempRight.mBottom = tempBottom;
//			
//			if(tempRight.mTop != null)
//				tempRight.mTop.mBottom = tempRight;
//			if(tempRight.mLeft != null)
//				tempRight.mLeft.mRight = tempRight;
//			if(tempRight.mBottom != null)
//				tempRight.mBottom.mTop = tempRight;
//		} else if(pDirection == sUp) {
//
//		}
//		
//	}
//
//}