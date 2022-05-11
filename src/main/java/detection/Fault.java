package detection;

import java.util.List;

import marker.Marker;

public record Fault(FaultSeverity severity, String description, List<Marker> markers)
{
	public boolean equals(Object object)
	{
		if (object instanceof Fault faultToCompare)
		{
			boolean equalSeverity = this.severity.equals(faultToCompare.severity);
			boolean equalDescription = this.description.equals(faultToCompare.description);

			List<Marker> equalMarkerList = getEqualMarkers(faultToCompare);
			int equalMarkersCount = equalMarkerList.size();
			boolean equalMarkers = markers.size() == equalMarkersCount;

			return equalSeverity && equalDescription && equalMarkers;
		}
		return false;
	}

	private List<Marker> getEqualMarkers(Fault faultToCompare)
	{
		return faultToCompare.markers().stream().filter(this::containsMarker).toList();
	}

	private boolean containsMarker(Marker markerToCheck)
	{
		for (Marker marker : markers)
		{
			if (marker.equals(markerToCheck))
			{
				return true;
			}
		}
		return false;
	}
}
