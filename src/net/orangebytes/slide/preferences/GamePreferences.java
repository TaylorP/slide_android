package net.orangebytes.slide.preferences;

import java.util.Hashtable;

import net.orangebytes.slide.R;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/// Singleton preferences class, for reading and writing user preferences
public final class GamePreferences {
	
	/// The singleton instance of the class
	private static GamePreferences sInstance = null;
   
	/// The preference file name
	private static final String sPreferencesName = "net.orangebytes.slide.prefs";
	
	
	/// The x grid size key
	private static final String sGridXKey = "GRID_X";
	
	/// The y grid size key
	private static final String sGridYKey = "GRID_Y";
	
	/// The image id key
	private static final String sImageKey = "IMAGE_ID";
	
	/// The image name key
	private static final String sImageNameKey = "IMAGE_NAME";
	
	/// The game times array key
	private static final String sGameTimes = "GAME_TIMES_";
	
	/// The game moves array key
	private static final String sGameMoves = "GAME_MOVES_";
	
	
	/// The shared preferences instance, containing user settings
    private SharedPreferences mSharedPrefs;
    
    /// The editor instance for modifying preferences
    private Editor mPrefsEditor;
    
    /// Cache of game stats, so repeated look ups aren't needed
    private Hashtable<String, Integer> mGameStatsCache;
    
    
	/// Hidden constructor
	private GamePreferences(Context pContext) {
		
		mSharedPrefs = pContext.getSharedPreferences(sPreferencesName, Activity.MODE_PRIVATE);
		mPrefsEditor = mSharedPrefs.edit();
		
		mGameStatsCache = new Hashtable<String, Integer>();
	}
	
	/// Singleton accessor
	public static GamePreferences get(Context pContext) {
		if(sInstance == null) {
			sInstance = new GamePreferences(pContext);
		}
		
		return sInstance;
	}
	
	/// Loads the current preferences into a GameState instance
	public GameState loadGameState() {
		GameState state = new GameState(
				mSharedPrefs.getInt(sGridXKey, 3),
				mSharedPrefs.getInt(sGridYKey, 3),
				mSharedPrefs.getInt(sImageKey, R.drawable.beach),
				mSharedPrefs.getString(sImageNameKey, "beach"));
		
		return state;
	}
	
	/// Stores a GameState instance into the preferences
	public void storeGameState(GameState pState) {
		mPrefsEditor.putInt(sGridXKey, pState.getX())
					.putInt(sGridYKey, pState.getY())
					.putInt(sImageKey, pState.getImage())
					.putString(sImageNameKey, pState.getImageName());
		mPrefsEditor.commit();
	}
	
	/// Loads the game times scores for a given puzzle
	public int loadTimes(String pPuzzle, int pX, int pY) {
		String key = sGameTimes + String.valueOf(pX) + String.valueOf(pY) + pPuzzle;
		
		if(mGameStatsCache.containsKey(key)){
			return mGameStatsCache.get(key);
		}
		
		return mSharedPrefs.getInt(key, -1);
	}
	
	///Saves the game times scores for a given puzzle
	public void saveTimes(int pTimes, String pPuzzle, int pX, int pY) {
		
		int last = loadTimes(pPuzzle, pX, pY);
		if(pTimes >= last && last >= 0)
			return;
		
		String key = sGameTimes + String.valueOf(pX) + String.valueOf(pY) + pPuzzle;
		
		mGameStatsCache.put(key, pTimes);
		
		mPrefsEditor.putInt(key, pTimes);
		mPrefsEditor.commit();
	}
	
	/// Loads the game move scores for a given puzzle
	public int loadMoves(String pPuzzle, int pX, int pY) {
		String key = sGameMoves + String.valueOf(pX) + String.valueOf(pY) + pPuzzle;
		
		if(mGameStatsCache.containsKey(key)){
			return mGameStatsCache.get(key);
		}
		
		return mSharedPrefs.getInt(key, -1);
	}
	
	///Saves the game move scores for a given puzzle
	public void saveMoves(int pMoves, String pPuzzle, int pX, int pY) {
		
		int last = loadMoves(pPuzzle, pX, pY);
		if(pMoves >= last && last >= 0)
			return;
		
		String key = sGameMoves + String.valueOf(pX) + String.valueOf(pY) + pPuzzle;
		
		mGameStatsCache.put(key, pMoves);
		
		mPrefsEditor.putInt(key, pMoves);
		mPrefsEditor.commit();
	}
}