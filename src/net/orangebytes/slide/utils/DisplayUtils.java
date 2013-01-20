package net.orangebytes.slide.utils;

import android.app.Activity;
import android.util.DisplayMetrics;

/// Display utilities class
public class DisplayUtils {
	
	/// The current activity that these display results describe
	private static Activity sActivity;
	
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
}