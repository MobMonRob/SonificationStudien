package detection.detectors;

import java.util.List;
import java.util.Optional;

import detection.Fault;
import marker.Marker;

public interface FaultDetector
{
	public Optional<Fault> detect();

	public void setMarkerToDetect(List<Marker> markerToDetect);
}
