package detection;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import detection.detectors.FaultDetector;

public class FaultMonitor
{
	private List<FaultDetector> detectors;
	private Thread monitoringThread;
	private boolean monitorIsActive = false;

	/**
	 * This class covers monitoring over the given detectors. <br>
	 * 
	 * During monitoring, this class iterates over every given detector and
	 * evaluates the returning {@link Fault} Object. Iteration is done by
	 * separate thread which is started and stopped by
	 * {@link #startMonitoring()} / {@link #stopMonitoring()}.
	 * 
	 * @param detectors
	 */
	public FaultMonitor(FaultDetector... detectors)
	{
		this.detectors = Arrays.asList(detectors);
		this.monitoringThread = new Thread(() -> {
			monitorIsActive = true;
			while (monitorIsActive)
			{
				monitor();
			}
		});
	}

	public void startMonitoring()
	{
		monitoringThread.start();
	}

	public void stopMonitoring()
	{
		monitorIsActive = false;
	}

	private void monitor()
	{
		for (FaultDetector detector : detectors)
		{
			evaluateFault(detector.detect());
		}
	}

	private void evaluateFault(Optional<Fault> optionalFault)
	{
		if (optionalFault.isEmpty())
		{
			return;
		}
		Fault fault = optionalFault.get();
		System.out.println(fault.description());
	}

	public static void main(String[] args)
	{

	}
}
// MarkerTracker.captureCurrentMarkers();
// List<Marker> notVisibleMarkers = new ArrayList<>();
// while (true)
// {
// final List<Marker> newNotVisibleMarkers =
// MarkerTracker.trackAndGetNotVisibleMarkers();
// for (Marker marker : newNotVisibleMarkers)
// {
// if (!MarkerTracker.isMarkerInList(notVisibleMarkers, marker))
// {
// System.out.println(marker.name());
// notVisibleMarkers.add(marker);
// }
// }
//
// for (Marker marker : notVisibleMarkers)
// {
// if (!MarkerTracker.isMarkerInList(newNotVisibleMarkers, marker))
// {
// System.out.println(marker.name() + " is visible again!");
// }
// }
//
// notVisibleMarkers = newNotVisibleMarkers;
// }
