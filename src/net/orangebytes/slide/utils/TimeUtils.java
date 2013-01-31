package net.orangebytes.slide.utils;

/// Utility class for time management
public class TimeUtils {
	
	/// Converts an integer time in seconds to a minutes string
	public static String intToMinutes(int intTime) {
		String time;
		if (intTime < 0) {
			time = "-:--";
		} else if (intTime < 10) {
			time = "0:0" + intTime;
		}
		else if (intTime < 60) {
			time = "0:" + intTime;
		}
		else {
			if (intTime % 60 < 10) {
				time = intTime / 60 + ":0" + intTime % 60;
			} else {
				time = intTime / 60 + ":" + intTime % 60;
			}
		}
		
		return time;
	}
}