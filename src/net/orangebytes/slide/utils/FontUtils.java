package net.orangebytes.slide.utils;

import android.content.Context;
import android.graphics.Typeface;

public class FontUtils {
	private static Typeface sRobotoThin;
	private static Typeface sRobotoLight;
	
	public static Typeface getRobotoThin(Context pContext) {
		if(sRobotoThin == null) {
			sRobotoThin = Typeface.createFromAsset(pContext.getAssets(), "Roboto-Thin.ttf");
		}
		
		return sRobotoThin;
	}
	
	public static Typeface getRobotoLight(Context pContext) {
		if(sRobotoLight == null) {
			sRobotoLight = Typeface.createFromAsset(pContext.getAssets(), "Roboto-Light.ttf");
		}
		
		return sRobotoLight;
	}
}