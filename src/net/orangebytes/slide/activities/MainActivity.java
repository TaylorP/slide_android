package net.orangebytes.slide.activities;

import net.orangebytes.slide.R;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingFragmentActivity;

/// The main activity for the app, containing either two fragments in seperate panels or a sliding menu
public class MainActivity extends SlidingFragmentActivity {

	@Override
	/// Creates the Activity
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        setContentView(R.layout.game_frame);
        setBehindContentView(R.layout.options_frame);
        
		GameFragment gameFrag = new GameFragment();
		OptionsFragment optionsFrag = new OptionsFragment();
		
		FragmentTransaction t1 = this.getSupportFragmentManager().beginTransaction();
		t1.replace(R.id.game_frame, gameFrag);
		t1.commit();
		
		
		FragmentTransaction t2 = this.getSupportFragmentManager().beginTransaction();
		t2.replace(R.id.options_frame, optionsFrag);
		t2.commit();
		
		// customize the SlidingMenu
		SlidingMenu sm = getSlidingMenu();
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		sm.setBehindWidthRes(R.dimen.slidingmenu_width);
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setShadowDrawable(R.drawable.slide_shadow);
		sm.setBehindScrollScale(0.0f);
		sm.setFadeDegree(0.35f);
	}
}
