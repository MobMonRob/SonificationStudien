package detection.detectors;

import java.util.Optional;

import detection.Fault;

public interface FaultDetector
{
	public Optional<Fault> detect();
}
