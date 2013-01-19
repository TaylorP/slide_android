package net.orangebytes.slide.activities;

import net.orangebytes.slide.R;
import net.orangebytes.slide.utils.DisplayUtils;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/// The fragment that contain the main game view
public class GameFragment extends Fragment {
	
	/// The layout that will hold the game tiles
	RelativeLayout mGameGrid;
	
	/// The current image resource
	int mImage;
	
	/// The current xSize
	int mXSize;
	
	/// The current ySize
	int mYSize;
	
	@Override
    /// Creates the view for this fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	
    	View root = inflater.inflate(R.layout.game_fragment, container, false);
    	mGameGrid = (RelativeLayout) root.findViewById(R.id.game_grid);
    	
    	setPuzzle(R.drawable.leaf, 3, 4);
    	
        return root;
    }
    
	
	/// Sets the puzzle, given an image or size
    public void setPuzzle(int imageResource, int xSize, int ySize) {
    	
    	if(imageResource == -1) {
    		imageResource = mImage;
    	} else {
    		mImage = imageResource;
    	}
    	
    	if(xSize == -1 || ySize == -1) {
    		xSize = mXSize;
    		ySize = mYSize;
    	} else {
    		mXSize = xSize;
    		mYSize = ySize;
    	}
    	
    	mGameGrid.removeAllViews();
    	
    	int effectiveWidth 	= DisplayUtils.getDisplayWidth(getActivity()) - 60;
    	int effectiveHeight = DisplayUtils.getDisplayHeight(getActivity()) - 60;

    	int cellPadding = 6;
    	int cellSize = Math.min((effectiveWidth / xSize), (effectiveHeight / ySize)) - cellPadding;
    	
    	Bitmap originalBitmap=BitmapFactory.decodeResource(getResources(), imageResource);
    	
    	int imageWidth = originalBitmap.getWidth();
    	int imageScale = imageWidth/xSize;
    	
    	mGameGrid.setLayoutParams(new RelativeLayout.LayoutParams((cellSize+cellPadding)*xSize, (cellSize+cellPadding)*ySize));
    	
    	for(int i = 0; i < xSize; i++)
    	{
    		for(int j = 0; j< ySize; j++)
    		{
    	    	Bitmap croppedBitmap=Bitmap.createBitmap(originalBitmap, i*imageScale, j*imageScale, imageScale, imageScale);
    	    	
    			LayoutInflater vi = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	    	ImageView v = (ImageView)vi.inflate(R.layout.puzzle_tile, null);
    	    	
    	    	RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(cellSize, cellSize);
    	    	params.leftMargin = (cellSize+cellPadding)*(i);
    	    	params.topMargin = (cellSize+cellPadding)*(j);
    	    	
    	    	v.setImageBitmap(croppedBitmap);
    	    	v.setLayoutParams(params);
    	    
    	    	mGameGrid.addView(v);
    		}
    	}
    }
}