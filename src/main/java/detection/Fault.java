package detection;

import java.util.List;

import marker.Marker;

public record Fault(FaultSeverity fault, String description, List<Marker> markers)
{
	
}
