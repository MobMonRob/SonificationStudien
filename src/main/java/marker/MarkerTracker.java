package marker;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import de.dhbw.rahmlab.vicon.datastream.api.DataStreamClient;

public class MarkerTracker
{
	private static final long CAPTURE_PERIOD = TimeUnit.SECONDS.toMillis(5);

	// If this period is short, the program will crash (...faster) - Need to
	// investigate
	private static final long RECONNECT_PERIOD = TimeUnit.SECONDS.toMillis(60);

	private List<Marker> markers = new ArrayList<>();
	private final DataStreamClientProvider clientProvider;
	private DataStreamClient client;
	private final Timer captureTimer;
	private final Timer reconnectTimer;

	public MarkerTracker(DataStreamClientProvider clientProvider)
	{
		this.clientProvider = clientProvider;
		this.client = clientProvider.buildClient();

		captureTimer = new Timer();
		captureTimer.scheduleAtFixedRate(captureTask, CAPTURE_PERIOD, CAPTURE_PERIOD);

		reconnectTimer = new Timer();
		reconnectTimer.scheduleAtFixedRate(clientReconnectTask, RECONNECT_PERIOD, RECONNECT_PERIOD);
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
		markers = trackMarkerData();
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
	public List<Marker> getNotVisibleMarkers(List<Marker> markerList)
	{
		return markerList.stream().filter(marker -> !(isMarkerVisible(marker))).toList();
	}

	private boolean isMarkerVisible(Marker marker)
	{
		String subject = marker.subject();
		String markerName = marker.name();

		client.getFrame();
		if (!isSubjectEnabled(subject))
		{
			return true;
		}

		long viewCount = client.getMarkerRayContributionCount(subject, markerName);
		return viewCount >= 2;
	}

	private boolean isSubjectEnabled(String subject)
	{
		for (int i = 0; i < client.getSubjectCount(); i++)
		{
			if (client.getSubjectName(i).equals(subject))
			{
				return true;
			}
		}
		return false;
	}

	private List<Marker> markersInSubject(DataStreamClient client, String subjectName)
	{
		client.getFrame();
		List<Marker> markersOfSubject = new ArrayList<>();

		long markerCount = client.getMarkerCount(subjectName);
		for (int i = 0; i < markerCount; i++)
		{
			String markerName = client.getMarkerName(subjectName, i);

			double[] coordinates = client.getMarkerGlobalTranslation(subjectName, markerName);
			Coordinates markerCoordinates = new Coordinates(coordinates[0], coordinates[1],
					coordinates[2]);

			Marker marker = new Marker(subjectName, markerName, markerCoordinates);
			markersOfSubject.add(marker);
		}

		return markersOfSubject;
	}

	private TimerTask captureTask = new TimerTask()
	{
		// Capture new markers frequently
		@Override
		public void run()
		{
			captureCurrentMarkers();
		}
	};

	private TimerTask clientReconnectTask = new TimerTask()
	{
		// Capture new markers frequently
		@Override
		public void run()
		{
			synchronized (clientProvider)
			{
				DataStreamClient newClient = clientProvider.buildClient();
				while (!newClient.isConnected())
				{
					// wait
				}
				client = newClient;
			}
		}
	};
}
