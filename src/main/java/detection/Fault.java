package detection;

import java.util.List;

import marker.Coordinates;

public record Fault(FaultSeverity fault, String description, List<Coordinates> coordinates)
{

}
