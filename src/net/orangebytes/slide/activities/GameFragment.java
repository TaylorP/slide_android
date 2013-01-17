package net.orangebytes.slide.activities;

import net.orangebytes.slide.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/// The fragment that contain the main game view
public class GameFragment extends Fragment {
	
	
    @SuppressLint("NewApi")
	@Override
    /// Creates the view for this fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	
    	View root = inflater.inflate(R.layout.game_fragment, container, false);
    	
    	Display display = getActivity().getWindowManager().getDefaultDisplay();
    	Point size = new Point();
    	display.getSize(size);
    	int width = size.x - 60;
    	int height = size.y - 60;
    	
    	int padding = 6;
    	
    	int x = 4;
    	int y = 4;
    	int tileSizeTryX = width / x;
    	int tileSizeTryY = height / y;
    	
    	int tileSize = Math.min(tileSizeTryX, tileSizeTryY)-padding;
    	
    	
    	Bitmap originalBitmap=BitmapFactory.decodeResource(getResources(), R.drawable.lion);
    	int bwidth = originalBitmap.getWidth();
    	int bheight = originalBitmap.getHeight();
    	
    	RelativeLayout parent = (RelativeLayout) root.findViewById(R.id.game_grid);
    	parent.setLayoutParams(new RelativeLayout.LayoutParams((tileSize+padding)*x, (tileSize+padding)*y));
    	
    	for(int i = 0; i < x; i++)
    	{
    		for(int j = 0; j< y; j++)
    		{
    	    	Bitmap croppedBitmap=Bitmap.createBitmap(originalBitmap, i*(bwidth/x), j*(bwidth/x), (bwidth/x), (bwidth/x));
    	    	
    	    	getActivity();
    			LayoutInflater vi = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	    	ImageView v = (ImageView)vi.inflate(R.layout.puzzle_tile, null);
    	    	v.setImageBitmap(croppedBitmap);
    	    	v.setX((tileSize+padding)*(i));
    	    	v.setY((tileSize+padding)*(j));
    	    
    	    	parent.addView(v, i, new ViewGroup.LayoutParams(tileSize, tileSize));
    		}
    	}

        return root;
    }
}