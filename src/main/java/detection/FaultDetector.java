package detection;

import java.util.ArrayList;
import java.util.List;

import marker.Marker;
import marker.MarkerTracker;

public class FaultDetector
{
	public static void main(String[] args)
	{
		MarkerTracker.captureCurrentMarkers();
		List<Marker> notVisibleMarkers = new ArrayList<>();
		while (true)
		{
			final List<Marker> newNotVisibleMarkers = MarkerTracker.trackAndGetNotVisibleMarkers();
			for (Marker marker : newNotVisibleMarkers)
			{
				if (!MarkerTracker.isMarkerInList(notVisibleMarkers, marker))
				{
					System.out.println(marker.name());
					notVisibleMarkers.add(marker);
				}
			}

			for (Marker marker : notVisibleMarkers)
			{
				if (!MarkerTracker.isMarkerInList(newNotVisibleMarkers, marker))
				{
					System.out.println(marker.name() + " is visible again!");
				}
			}

			notVisibleMarkers = newNotVisibleMarkers;
		}
	}
}
