package net.orangebytes.slide.activities;

import net.orangebytes.slide.R;
import net.orangebytes.slide.preferences.GamePreferences;
import net.orangebytes.slide.preferences.GameState;
import net.orangebytes.slide.utils.Sounds;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;

import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingFragmentActivity;

/// The main activity for the app, containing either two fragments in seperate panels or a sliding menu
public class MainActivity extends SlidingFragmentActivity {

	/// The game fragment
	private GameFragment mGameFragment;
	
	/// The options fragment
	private OptionsFragment mOptionsFragment;
	
	/// The shared game state
	private GameState mGameState;
	
	@Override
	/// Creates the Activity
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        setContentView(R.layout.game_frame);
        setBehindContentView(R.layout.options_frame);
        
		mGameFragment = new GameFragment();
		mOptionsFragment = new OptionsFragment();
		
		mGameState = GamePreferences.get(this).loadGameState();
		
		FragmentTransaction t1 = this.getSupportFragmentManager().beginTransaction();
		t1.replace(R.id.game_frame, mGameFragment);
		t1.commit();
		
		FragmentTransaction t2 = this.getSupportFragmentManager().beginTransaction();
		t2.replace(R.id.options_frame, mOptionsFragment);
		t2.commit();
		
		SlidingMenu sm = getSlidingMenu();
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		sm.setBehindWidthRes(R.dimen.slidingmenu_width);
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setShadowDrawable(R.drawable.slide_shadow);
		sm.setBehindScrollScale(0.0f);
		sm.setFadeDegree(0.35f);
		
		Sounds.get(this);
	}
	
	@Override
	///Called when the activity pauses - use this to store the game state
	public void onPause () {
		super.onPause();
		GamePreferences.get(this).storeGameState(mGameState);
		Sounds.get(this).stopMusic(this);
	}
	
	@Override
	///Called when the activity resumes - used to restart music
	public void onResume() {
		super.onResume();
		if(GamePreferences.get(this).getMusicPreference()) {
			Sounds.get(this).startMusic(this);
		}
	}
	
	/// Sets the puzzle based on an image resource and size
	public void setPuzzle(String imageName, int xSize, int ySize) {
		mGameFragment.setPuzzle(imageName, xSize, ySize);
	}
	
	/// Toggles the sliding menu
	public void toggleOptions() {
		getSlidingMenu().toggle(true);
		mOptionsFragment.scrollToSelected();
	}
	
	/// Toggle preview
	public void togglePreview() {
		if(mGameFragment.toggleView()) {
			if(getSlidingMenu().isMenuShowing()) {
				toggleOptions();
			}
		}
	}
	
	/// Completion call
	public void onComplete(GameState pGameState) {
		mOptionsFragment.updateStats(pGameState);
	}
	
	@Override
	/// Override the key down - we use this to open the options and preview using hard buttons if available
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if ( keyCode == KeyEvent.KEYCODE_MENU ) {
	    	toggleOptions();
	        return true;
	    } else if (keyCode == KeyEvent.KEYCODE_SEARCH) {
	    	togglePreview();
	    	return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
	/// Returns the current game state
	public GameState getGameState() {
		return mGameState;
	}
}
