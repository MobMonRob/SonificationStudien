package detection.detectors;

import java.util.List;
import java.util.Optional;

import detection.Fault;
import detection.FaultSeverity;
import marker.DataStreamClientProvider;
import marker.Marker;
import marker.MarkerTracker;

public class NotVisibleMarkerDetector implements FaultDetector
{
	private MarkerTracker markerTracker;
	private List<Marker> markersToDetect;

	public NotVisibleMarkerDetector()
	{
		markerTracker = new MarkerTracker(new DataStreamClientProvider());
		markerTracker.captureCurrentMarkers();
	}

	public void setMarkerToDetect(List<Marker> markersToDetect)
	{
		this.markersToDetect = markersToDetect;
	}

	@Override
	public Optional<Fault> detect()
	{
		List<Marker> filteredMarkers = filterMarkers(markerTracker.getNotVisibleMarkers());
		if (filteredMarkers.isEmpty())
		{
			return Optional.empty();
		}

		return Optional.of(buildFault(filteredMarkers));
	}

	private List<Marker> filterMarkers(List<Marker> notTrackableMarkers)
	{
		return notTrackableMarkers.stream().filter(this::isMarkerToDetect).toList();
	}

	private boolean isMarkerToDetect(Marker marker)
	{
		if (null == markersToDetect || markersToDetect.isEmpty())
		{
			return true;
		}
		
		for (Marker markerToDetect : markersToDetect)
		{
			boolean subjectEqual = markerToDetect.subject().equals(marker.subject());
			boolean nameEqual = markerToDetect.name().equals(marker.name());
			if (subjectEqual && nameEqual)
			{
				return true;
			}
		}
		return false;
	}

	private Fault buildFault(List<Marker> notTrackableMarkers)
	{
		int markerCount = notTrackableMarkers.size();
		String description = getDescription(notTrackableMarkers);
		FaultSeverity severity = getSeverity(markerCount);

		return new Fault(severity, description, notTrackableMarkers);
	}

	private String getDescription(List<Marker> notTrackableMarkers)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("Detected following markers as not visible: ");
		for (Marker marker : notTrackableMarkers)
		{
			sb.append(marker.name() + ", ");
		}
		return sb.toString();
	}

	private FaultSeverity getSeverity(int notTrackableMarkerCount)
	{
		return notTrackableMarkerCount < 2 ? FaultSeverity.WARNING : FaultSeverity.ERROR;
	}
}
