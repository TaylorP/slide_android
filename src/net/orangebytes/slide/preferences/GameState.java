package net.orangebytes.slide.preferences;

import android.content.res.Configuration;

/// A utility class for holding game configuration, also a proxy to the preferences
public class GameState {
	
	/// The X grid size
	private int mSizeX;
	
	/// The Y grid size
	private int mSizeY;
	
	/// The image resource ID
	private int mImage;
	
	/// The image name
	private String mImageName;
	
	/// Constructor, taking sizes, orientation and an image resource
	public GameState(int pSizeX, int pSizeY, int pImage, String pImageName) {
		mSizeX = pSizeX;
		mSizeY = pSizeY;
		mImage = pImage;
		mImageName = pImageName;
	}
	
	/// Returns the x size
	public int getX(int pOrientation) {
		if(pOrientation == Configuration.ORIENTATION_LANDSCAPE)
			return mSizeY;
		return mSizeX;
	}
	
	/// Returns the y size
	public int getY(int pOrientation) {
		if(pOrientation == Configuration.ORIENTATION_LANDSCAPE)
			return mSizeX;
		return mSizeY;
	}
	
	/// Returns the image resource
	public int getImage() {
		return mImage;
	}
	
	/// Returns the image name
	public String getImageName() {
		return mImageName;
	}
	
	/// Gets the x*y value, the number of tiles in total
	public int getSize() {
		return mSizeY*mSizeX;
	}
	
	
	/// Sets the x size
	public void setX(int pSizeX) {
		mSizeX = pSizeX;
	}
	
	/// Sets the y size
	public void setY(int pSizeY) {
		mSizeY = pSizeY;
	}
	
	/// Sets the image resource
	public void setImage(int pImage) {
		mImage = pImage;
	}
	
	/// Sets the image name
	public void setImageName(String pImageName) {
		mImageName = pImageName;
	}
}