package net.orangebytes.slide.utils;

public class TimeUtils {
	
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