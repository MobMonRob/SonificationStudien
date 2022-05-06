package marker;

import java.util.ArrayList;
import java.util.List;

import de.dhbw.rahmlab.vicon.datastream.api.DataStreamClient;

public class MarkerTracker
{
	private List<Marker> markers = new ArrayList<>();
	private DataStreamClient client;
	private MarkerTrackerTimer markerTrackerTimer;

	public MarkerTracker(DataStreamClient client)
	{
		this.client = client;
		markerTrackerTimer = new MarkerTrackerTimer(this);
		markerTrackerTimer.run();
	}

	/**
	 * Capture all markers which are currently visible and save it internally
	 * for subsequent uses.
	 * 
	 * @return A list with all captured (visible) markers (See also
	 *         {@link #getVisibleMarkers(List)})
	 */
	public List<Marker> captureCurrentMarkers()
	{
		markers = getVisibleMarkers(trackMarkerData());
		return markers;
	}

	/**
	 * @return A list with all markers which are registered in vicon system -
	 *         visible and not visible
	 */
	public List<Marker> trackMarkerData()
	{
		List<Marker> newMarkers = new ArrayList<>();

		long subjectCount = client.getSubjectCount();
		for (int i = 0; i < subjectCount; i++)
		{
			String subject = client.getSubjectName(i);
			newMarkers.addAll(markersInSubject(client, subject));
		}

		return newMarkers;
	}

	/**
	 * @return A list with all previously captured markers, which are not
	 *         visible anymore (See also {@link #getNotVisibleMarkers(List)})
	 */
	public List<Marker> getNotVisibleMarkers()
	{
		if (markers.isEmpty())
		{
			captureCurrentMarkers();
		}

		return getNotVisibleMarkers(markers);
	}

	/**
	 * @param markerList
	 * @return A list with all markers in given list, which are not visible <br>
	 *         (not visible = marker is captured by less than 2 cameras)
	 */
	public List<Marker> getVisibleMarkers(List<Marker> markerList)
	{
		return markerList.stream().filter(this::isMarkerVisible).toList();
	}

	/**
	 * @param markerList
	 * @return A list with all markers in given list, which are visible <br>
	 *         (visible = marker is captured by minimum 2 cameras)
	 */
	public List<Marker> getNotVisibleMarkers(List<Marker> markerList)
	{
		return markerList.stream().filter(marker -> !(isMarkerVisible(marker))).toList();
	}

	public void startMarkerTrackerTimer()
	{
		if (!markerTrackerTimer.isAlive())
		{
			markerTrackerTimer.run();
		}
	}

	public void stopMarkerTrackerTimer()
	{
		if (markerTrackerTimer.isAlive())
		{
			markerTrackerTimer.interrupt();
		}
	}

	private boolean isMarkerVisible(Marker marker)
	{
		String subject = marker.subject();
		String markerName = marker.name();
		long viewCount = client.getMarkerRayContributionCount(subject, markerName);
		return viewCount >= 2;
	}

	private List<Marker> markersInSubject(DataStreamClient client, String subjectName)
	{
		List<Marker> markersOfSubject = new ArrayList<>();

		long markerCount = client.getMarkerCount(subjectName);
		for (int i = 0; i < markerCount; i++)
		{
			String markerName = client.getMarkerName(subjectName, i);

			double[] coordinates = client.getMarkerGlobalTranslation(subjectName, markerName);
			Coordinates markerCoordinates = new Coordinates(coordinates[0],
					coordinates[1], coordinates[2]);

			Marker marker = new Marker(subjectName, markerName, markerCoordinates);
			markersOfSubject.add(marker);
		}

		return markersOfSubject;
	}
}
