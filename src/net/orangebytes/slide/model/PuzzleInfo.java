package net.orangebytes.slide.model;

/// A simple container class for puzzle information
public class PuzzleInfo {
	
	/// The title of the puzzle
	private String 	mPuzzleTitle;
	
	/// The puzzle thumbnail
	private String	mPuzzleThumb;
	
	
	/// Constructor taking details about the puzzle as parameters
	public PuzzleInfo(String puzzleTitle, String puzzleThumb) {
		mPuzzleTitle = puzzleTitle;
		mPuzzleThumb = puzzleThumb;
	}
	
	/// Returns the puzzle title
	public String getTitle() {
		return mPuzzleTitle;
	}
	
	/// Returns the puzzle thumbnail
	public String getThumb() {
		return mPuzzleThumb;
	}
}