package net.orangebytes.slide.activities;

import net.orangebytes.slide.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/// The fragment that contain the main game view
public class GameFragment extends Fragment {
	
    @Override
    /// Creates the view for this fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.game_fragment, container, false);
    }
}