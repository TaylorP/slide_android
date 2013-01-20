package net.orangebytes.slide.activities;

import net.orangebytes.slide.R;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingFragmentActivity;

/// The main activity for the app, containing either two fragments in seperate panels or a sliding menu
public class MainActivity extends SlidingFragmentActivity {

	private GameFragment mGameFragment;
	private OptionsFragment mOptionsFragment;
	
	@Override
	/// Creates the Activity
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        setContentView(R.layout.game_frame);
        setBehindContentView(R.layout.options_frame);
        
		mGameFragment = new GameFragment();
		mOptionsFragment = new OptionsFragment();
		
		FragmentTransaction t1 = this.getSupportFragmentManager().beginTransaction();
		t1.replace(R.id.game_frame, mGameFragment);
		t1.commit();
		
		FragmentTransaction t2 = this.getSupportFragmentManager().beginTransaction();
		t2.replace(R.id.options_frame, mOptionsFragment);
		t2.commit();
		
		SlidingMenu sm = getSlidingMenu();
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		sm.setBehindWidthRes(R.dimen.slidingmenu_width);
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setShadowDrawable(R.drawable.slide_shadow);
		sm.setBehindScrollScale(0.0f);
		sm.setFadeDegree(0.35f);
	}
	
	public void setPuzzle(int resource, int xSize, int ySize) {
		mGameFragment.setPuzzle(resource, xSize, ySize);
	}
	
	public void toggleOptions() {
		getSlidingMenu().toggle(true);
	}
}
