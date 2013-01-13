package net.orangebytes.slide.model;

/// A simple container class for puzzle information
public class PuzzleInfo {
	
	/// The title of the puzzle
	private String 	mPuzzleTitle;
	
	/// The best solve time for the puzzle
	private int 	mPuzzleTime;
	
	/// The best move count for the puzzle
	private int		mPuzzleMoves;
	
	/// The puzzle thumbnail
	private String	mPuzzleThumb;
	
	
	/// Constructor taking details about the puzzle as parameters
	public PuzzleInfo(String puzzleTitle, String puzzleThumb, int puzzleTime, int puzzleMoves) {
		mPuzzleTitle = puzzleTitle;
		mPuzzleTime = puzzleTime;
		mPuzzleMoves = puzzleMoves;
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
	
	/// Returns the best puzzle time
	public int getTime() {
		return mPuzzleTime;
	}
	
	/// Returns the best puzzle moves
	public int getMoves() {
		return mPuzzleMoves;
	}
}