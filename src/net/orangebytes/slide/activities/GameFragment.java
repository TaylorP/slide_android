package net.orangebytes.slide.activities;

import net.orangebytes.slide.R;
import net.orangebytes.slide.adapters.GameGridAdapter;
import net.orangebytes.slide.adapters.OptionsListAdapter;
import net.orangebytes.slide.model.PuzzleInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

/// The fragment that contain the main game view
public class GameFragment extends Fragment {
	
	/// The grid view that contains the game tiles
	GridView mGameGrid;
	
	/// The adapter for the game grid
	GameGridAdapter mGameAdapter;
	
    @Override
    /// Creates the view for this fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	
    	View root = inflater.inflate(R.layout.game_fragment, container, false);
    	
    	PuzzleInfo[] mValues = new PuzzleInfo[] {
				new PuzzleInfo("lion", "lion_thumb", 10, 20),
				new PuzzleInfo("desert", "desert_thumb", 20, 30),
				new PuzzleInfo("fruit", "fruit_thumb", 122, 30),
				new PuzzleInfo("steak", "steak_thumb", 45, 30),
				new PuzzleInfo("flower", "flower_thumb", 45, 30),
				new PuzzleInfo("plant", "plant_thumb", 45, 32),
				new PuzzleInfo("orange", "orange_thumb", 15, 20),
				new PuzzleInfo("snow", "snow_thumb", 15, 20),
				new PuzzleInfo("bird", "bird_thumb", 15, 20),
				new PuzzleInfo("slide", "slide_thumb", 15, 20) };

		
    	mGameAdapter = new GameGridAdapter(this.getActivity(), mValues);
    	
    	mGameGrid = (GridView) root.findViewById(R.id.game_grid);
    	mGameGrid.setAdapter(mGameAdapter);
    	
        return root;
    }
}