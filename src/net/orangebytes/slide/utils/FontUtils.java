package net.orangebytes.slide.utils;

import android.content.Context;
import android.graphics.Typeface;

/// Utility class for getting custo fonts
public class FontUtils {
	
	/// The roboto thin typeface
	private static Typeface sRobotoThin;
	
	/// The robot light typeface
	private static Typeface sRobotoLight;
	
	
	/// Returns the typeface object for roboto thin
	public static Typeface getRobotoThin(Context pContext) {
		if(sRobotoThin == null) {
			sRobotoThin = Typeface.createFromAsset(pContext.getAssets(), "Roboto-Thin.ttf");
		}
		
		return sRobotoThin;
	}
	
	/// Returns the typeface object for roboto light
	public static Typeface getRobotoLight(Context pContext) {
		if(sRobotoLight == null) {
			sRobotoLight = Typeface.createFromAsset(pContext.getAssets(), "Roboto-Light.ttf");
		}
		
		return sRobotoLight;
	}
}