package marker;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import de.dhbw.rahmlab.vicon.datastream.api.DataStreamClient;

public class MarkerTracker
{
	private static List<Marker> markers = new ArrayList<>();

	private MarkerTracker()
	{
		// static class needs no constructor
	}

	public static boolean isMarkerInList(List<Marker> markerList, Marker marker)
	{
		return getMarkerIndexInList(markerList, marker) != -1;
	}

	private static int getMarkerIndexInList(List<Marker> markerList, Marker marker)
	{
		for (int i = 0; i < markerList.size(); i++)
		{
			if (markerList.get(i).name().equals(marker.name()))
			{
				return i;
			}
		}
		return -1;
	}

	public static void captureCurrentMarkers()
	{
		markers = trackNewMarkerData();
	}

	public static List<Marker> trackAndGetChangedMarkers()
	{
		if (markers.isEmpty())
		{
			captureCurrentMarkers();
		}

		List<Marker> changedMarkers = new ArrayList<>();
		for (Marker newMarker : trackNewMarkerData())
		{
			Optional<Marker> oldMarker = getCapturedMarker(newMarker.name());
			if (oldMarker.isPresent() && !isMarkerDataEqual(oldMarker.get(), newMarker))
			{
				changedMarkers.add(newMarker);
			}
		}

		return changedMarkers;
	}

	public static List<Marker> trackAndGetNewMarkers()
	{
		if (markers.isEmpty())
		{
			captureCurrentMarkers();
			return markers;
		}

		List<Marker> newMarkersToReturn = new ArrayList<>();
		for (Marker newMarker : trackNewMarkerData())
		{
			if (getCapturedMarker(newMarker.name()).isEmpty())
			{
				newMarkersToReturn.add(newMarker);
			}
		}

		return newMarkersToReturn;
	}

	public static List<Marker> trackAndGetNotVisibleMarkers()
	{
		if (markers.isEmpty())
		{
			captureCurrentMarkers();
		}

		List<Marker> notVisibleMarkers = new ArrayList<>();
		for (Marker marker : trackNewMarkerData())
		{
			if (isMarkerNotVisible(marker))
			{
				notVisibleMarkers.add(marker);
			}
		}

		return notVisibleMarkers;
	}

	public static List<Marker> trackNewMarkerData()
	{
		DataStreamClient client = DataStreamClientProvider.getConnectedDataStreamClient();
		client.getFrame();

		List<Marker> markersOfAllSubjects = new ArrayList<>();

		long subjectCount = client.getSubjectCount();
		for (int i = 0; i < subjectCount; i++)
		{
			List<Marker> markersInSubject = captureCurrentMarkersInSubject(client,
					client.getSubjectName(i));
			markersOfAllSubjects.addAll(markersInSubject);
		}

		return markersOfAllSubjects;
	}

	private static boolean isMarkerNotVisible(Marker marker)
	{
		DataStreamClient client = DataStreamClientProvider.getConnectedDataStreamClient();
		long markerRayContributionCount = client.getMarkerRayContributionCount(marker.subject(),
				marker.name());
		return markerRayContributionCount < 2;
	}

	private static List<Marker> captureCurrentMarkersInSubject(DataStreamClient client,
			String subjectName)
	{
		List<Marker> markersOfSubject = new ArrayList<>();

		long markerCount = client.getMarkerCount(subjectName);
		for (int i = 0; i < markerCount; i++)
		{
			String markerName = client.getMarkerName(subjectName, i);

			double[] coordinates = client.getMarkerGlobalTranslation(subjectName, markerName);
			MarkerCoordinates markerCoordinates = new MarkerCoordinates(coordinates[0],
					coordinates[1], coordinates[2]);

			Marker marker = new Marker(subjectName, markerName, markerCoordinates);
			markersOfSubject.add(marker);
		}

		return markersOfSubject;
	}

	private static boolean isMarkerDataEqual(Marker markerA, Marker markerB)
	{
		boolean subjectNameIsEqual = markerA.subject().equals(markerB.subject());
		boolean markerNameIsEqual = markerA.name().equals(markerB.name());

		boolean coordinatesAreEqual = markerA.coordinates().equals(markerB.coordinates());

		return subjectNameIsEqual && markerNameIsEqual && coordinatesAreEqual;
	}

	private static Optional<Marker> getCapturedMarker(String markerName)
	{
		for (Marker marker : markers)
		{
			if (marker.name().equals(markerName))
			{
				return Optional.of(marker);
			}
		}
		return Optional.empty();
	}
}
