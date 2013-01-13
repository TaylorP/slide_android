package net.orangebytes.slide.model;

public class PuzzleInfo {
	private String 	mPuzzleTitle;
	private int 	mPuzzleTime;
	private int		mPuzzleMoves;
	private String	mPuzzleThumb;
	
	public PuzzleInfo(String puzzleTitle, String puzzleThumb, int puzzleTime, int puzzleMoves) {
		mPuzzleTitle = puzzleTitle;
		mPuzzleTime = puzzleTime;
		mPuzzleMoves = puzzleMoves;
		mPuzzleThumb = puzzleThumb;
	}
	
	public String getTitle() {
		return mPuzzleTitle;
	}
	
	public String getThumb() {
		return mPuzzleThumb;
	}
	
	public int getTime() {
		return mPuzzleTime;
	}
	
	public int getMoves() {
		return mPuzzleMoves;
	}
}