package net.orangebytes.slide.preferences;

/// A utility class for holding game configuration, also a proxy to the preferences
public class GameState {
	
	/// Landscape orientation constant
	public static final boolean sLandscape = false;
	
	/// Portrait orientation constant
	public static final boolean sPortrait = true;
	
	/// The X grid size
	private int mSizeX;
	
	/// The Y grid size
	private int mSizeY;
	
	/// The image resource ID
	private int mImage;
	
	/// The rotation these preferences were stored in
	private boolean mOrientation;
	
	
	/// Constructor, taking sizes, orientation and an image resource
	public GameState(int pSizeX, int pSizeY, int pImage, boolean pOrientation) {
		mSizeX = pSizeX;
		mSizeY = pSizeY;
		mImage = pImage;
		mOrientation = pOrientation;
	}
	
	/// Returns the x size
	public int getX() {
		return mSizeX;
	}
	
	/// Returns the y size
	public int getY() {
		return mSizeY;
	}
	
	/// Returns the image resource
	public int getImage() {
		return mImage;
	}
	
	/// Returns the orientation
	public boolean getOrientation() {
		return mOrientation;
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
	
	/// Sets the orientation
	public void setOrientation(boolean pOrientation) {
		mOrientation = pOrientation;
	}
}