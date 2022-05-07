package detection.detectors;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import detection.Fault;
import detection.FaultSeverity;
import marker.Coordinates;
import marker.DataStreamClientProvider;
import marker.Marker;
import marker.MarkerTracker;

public class NotVisibleMarkerDetector implements FaultDetector
{
	private MarkerTracker markerTracker;

	public NotVisibleMarkerDetector()
	{
		markerTracker = new MarkerTracker(new DataStreamClientProvider());
		markerTracker.captureCurrentMarkers();
	}

	@Override
	public Optional<Fault> detect()
	{
		List<Marker> notTrackableMarkers = markerTracker.getNotVisibleMarkers();
		if (notTrackableMarkers.isEmpty())
		{
			return Optional.empty();
		}

		return Optional.of(buildFault(notTrackableMarkers));
	}

	private Fault buildFault(List<Marker> notTrackableMarkers)
	{
		int markerCount = notTrackableMarkers.size();
		String description = getDescription(markerCount);
		FaultSeverity severity = getSeverity(markerCount);
		List<Coordinates> coordinatesList = getCoordinatesList(notTrackableMarkers);

		return new Fault(severity, description, coordinatesList);
	}

	private String getDescription(int markerCount)
	{
		return String.format("Detected %s not trackable markers", markerCount);
	}

	private List<Coordinates> getCoordinatesList(List<Marker> notTrackableMarkers)
	{
		return notTrackableMarkers.stream().flatMap(marker -> Stream.of(marker.coordinates()))
				.toList();
	}

	private FaultSeverity getSeverity(int notTrackableMarkerCount)
	{
		return notTrackableMarkerCount < 2 ? FaultSeverity.WARNING : FaultSeverity.ERROR;
	}
}
