package net.orangebytes.slide.utils;

import net.orangebytes.slide.preferences.GameState;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.widget.RelativeLayout;

/// Utils for computing tile sizes and paddings
public class TileUtils {
	
	/// The tile padding - TODO this should probably be moved into a resource
	private static final int sTilePadding = 6;
	
	
	/// Gets the tile size for a given grid
	public static int getTileSize(Activity pActivity, GameState pState, int pOrientation) {
	
    	int effectiveWidth 	= (int) (DisplayUtils.getDisplayWidth(pActivity) * 0.9);
    	int effectiveHeight = (int) (DisplayUtils.getDisplayHeight(pActivity) *0.75);

    	return Math.min((effectiveWidth / pState.getX(pOrientation)), (effectiveHeight / pState.getY(pOrientation))) - sTilePadding;
	}
	
	/// Gets the tile padding
	public static int getTilePadding() {
		return sTilePadding;
	}
	
	/// Gets the tile scale
	public static int getTileScale(Bitmap pImage, GameState pState, Resources pResources, int pOrientation) {
    	return pImage.getWidth()/pState.getX(pOrientation);
	}
	
	/// Gets a tile layout parameter for a given size and position
	public static RelativeLayout.LayoutParams getTileLayout(int pSize, int pPositionX, int pPositionY) {
	
    	RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(pSize, pSize);
    	params.leftMargin = pPositionX;
    	params.topMargin = pPositionY;
    	
    	return params;
	}
}