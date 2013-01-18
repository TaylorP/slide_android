package net.orangebytes.slide.utils;

import android.app.Activity;
import android.util.DisplayMetrics;

public class DisplayUtils {
	private static Activity sActivity;
	private static int sDisplayHeight;
	private static int sDisplayWidth;
	
	private static void setDisplayParameters(Activity pActivity) {
		DisplayMetrics displaymetrics = new DisplayMetrics();
		pActivity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		sDisplayHeight = displaymetrics.heightPixels;
		sDisplayWidth = displaymetrics.widthPixels;
		sActivity = pActivity;
	}
	
	public static int getDisplayHeight(Activity pActivity) {
		if(!pActivity.equals(sActivity)) {
			setDisplayParameters(pActivity);
		}
		return sDisplayHeight;
	}
	
	public static int getDisplayWidth(Activity pActivity) {
		if(!pActivity.equals(sActivity)) {
			setDisplayParameters(pActivity);
		}
		return sDisplayWidth;
	}
}