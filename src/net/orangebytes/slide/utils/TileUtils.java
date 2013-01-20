package net.orangebytes.slide.utils;

import net.orangebytes.slide.preferences.GameState;
import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.widget.RelativeLayout;

/// Utils for computing tile sizes and paddings
public class TileUtils {
	
	/// The tile padding - TODO this should probably be moved into a resource
	private static final int sTilePadding = 6;
	
	/// The side padding - TODO this should probably be moved into a resource
	private static final int sGridPadding = 80;
	
	/// Gets the tile size for a given grid
	public static int getTileSize(Activity pActivity, GameState pState) {
    	int effectiveWidth 	= DisplayUtils.getDisplayWidth(pActivity) - sGridPadding;
    	int effectiveHeight = DisplayUtils.getDisplayHeight(pActivity) - sGridPadding;

    	return Math.min((effectiveWidth / pState.getX()), (effectiveHeight / pState.getY())) - sTilePadding;
	}
	
	/// Gets the tile padding
	public static int getTilePadding() {
		return sTilePadding;
	}
	
	/// Gets the tile scale
	public static int getTileScale(Bitmap pImage, GameState pState, Resources pResources) {
    	if(pResources.getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
    		return pImage.getHeight()/pState.getY();
    	} else {
    		return pImage.getWidth()/pState.getX();
    	}
	}
	
	/// Gets a tile layout parameter for a given size and position
	public static RelativeLayout.LayoutParams getTileLayout(int pSize, int pPositionX, int pPositionY) {
	
    	RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(pSize, pSize);
    	params.leftMargin = pPositionX;
    	params.topMargin = pPositionY;
    	
    	return params;
	}
}