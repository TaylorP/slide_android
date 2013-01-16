package net.orangebytes.slide.activities;

import net.orangebytes.slide.R;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

/// The main activity for the app, containing either two fragments in seperate panels or a sliding menu
public class MainActivity extends FragmentActivity {

	@Override
	/// Creates the Activity
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        
        if(this.findViewById(R.id.sliding_menu_frag) != null) {
			FragmentTransaction t = this.getSupportFragmentManager().beginTransaction();
			GameFragment gameFrag = new GameFragment();
			OptionsFragment optionsFrag = new OptionsFragment();
			t.replace(R.id.game_frame, gameFrag);
			t.replace(R.id.options_frame, optionsFrag);
			t.commit();
        }
	}
}
