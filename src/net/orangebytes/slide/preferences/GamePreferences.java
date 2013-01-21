package net.orangebytes.slide.preferences;

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
	
	/// The orientation key
	private static final String sOrientationKey = "ORIENTATION";
	
	
	/// The shared preferences instance, containing user settings
    private SharedPreferences mSharedPrefs;
    
    /// The editor instance for modifying preferences
    private Editor mPrefsEditor;
    
    
	/// Hidden constructor
	private GamePreferences(Context pContext) {
		
		mSharedPrefs = pContext.getSharedPreferences(sPreferencesName, Activity.MODE_PRIVATE);
		mPrefsEditor = mSharedPrefs.edit();
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
				mSharedPrefs.getBoolean(sOrientationKey, GameState.sPortrait));
		
		return state;
	}
	
	/// Stores a GameState instance into the preferences
	public void storeGameState(GameState pState) {
		mPrefsEditor.putInt(sGridXKey, pState.getX())
					.putInt(sGridYKey, pState.getY())
					.putInt(sImageKey, pState.getImage())
					.putBoolean(sOrientationKey, pState.getOrientation());
		mPrefsEditor.commit();
	}
}