package net.orangebytes.slide.utils;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Point;
import android.util.DisplayMetrics;

/// Display utilities class
public class DisplayUtils {
	
	/// The current activity that these display results describe
	private static Activity sActivity;
	
	/// The display sizes
	private static ArrayList<Point> sDisplaySizes;
	
	/// The display height
	private static int sDisplayHeight;
	
	/// The display width
	private static int sDisplayWidth;
	
	/// Sets the display values from the window manager - essentially caching them to speed up future performance
	private static void setDisplayParameters(Activity pActivity) {
		DisplayMetrics displaymetrics = new DisplayMetrics();
		pActivity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		
		sDisplayHeight = displaymetrics.heightPixels;
		sDisplayWidth = displaymetrics.widthPixels;
		sActivity = pActivity;
	}
	
	/// Build the display sizes list from the screen sizes
	private static void buildDisplaySizes(Activity pActivity) {
		if(!pActivity.equals(sActivity)) {
			setDisplayParameters(pActivity);
		}
		
		sDisplaySizes = new ArrayList<Point>();
		
		int xMax = (sDisplayWidth - 160) / 100;
		int yMax = (sDisplayHeight - 160) / 100;
		
		int min = 3;
		int max = xMax < yMax ? xMax : yMax;
		int alt = xMax < yMax ? yMax : xMax;
		
		for(int i = min; i<=max; i++) {
			if(i > alt)
				break;
			
			sDisplaySizes.add(new Point(i,i));
			
			if(i+1 <= alt)
				sDisplaySizes.add(new Point(i, i+1));
		}
	}
	
	/// Gets the display height
	public static int getDisplayHeight(Activity pActivity) {
		if(!pActivity.equals(sActivity)) {
			setDisplayParameters(pActivity);
		}
		return sDisplayHeight;
	}
	
	/// Gets the display width
	public static int getDisplayWidth(Activity pActivity) {
		if(!pActivity.equals(sActivity)) {
			setDisplayParameters(pActivity);
		}
		return sDisplayWidth;
	}
	
	/// Gets the display sizes
	public static ArrayList<Point> getDisplaySizes(Activity pActivity) {
		buildDisplaySizes(pActivity);
		return sDisplaySizes;
	}
}