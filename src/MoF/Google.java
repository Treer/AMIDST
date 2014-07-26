package MoF;

import amidst.Amidst;

import com.boxysystems.jgoogleanalytics.*;


public class Google {
	private static JGoogleAnalyticsTracker tracker;
	public static void startTracking() {
		// Skiph's tracking id
		// tracker = new JGoogleAnalyticsTracker("AMIDST", Amidst.version(), "UA-27092717-1");
		
		// Treer's tracking id (perhaps we should use both?)
		tracker = new JGoogleAnalyticsTracker("AMIDST", Amidst.version(), "UA-48787072-4");
	}
	
	public static void track(String s) {
		  FocusPoint focusPoint = new FocusPoint(s);
		  tracker.trackAsynchronously(focusPoint);
	}
	
}
