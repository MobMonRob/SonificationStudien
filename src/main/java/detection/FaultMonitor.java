package detection;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import detection.detectors.FaultDetector;
import detection.detectors.NotVisibleMarkerDetector;
import marker.Coordinates;
import marker.Marker;
import sound.CountRelatedSoundFactory;
import sound.SoundFactory;

public class FaultMonitor
{
	private List<FaultDetector> detectors;
	private Thread monitoringThread;
	private boolean monitorIsActive = false;
	private SoundFactory soundFactory;

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
	public FaultMonitor(List<Marker> markersToMonitor, FaultDetector... detectors)
	{
		this.detectors = Arrays.asList(detectors);
		for (FaultDetector faultDetector : detectors)
		{
			faultDetector.setMarkerToDetect(markersToMonitor);
		}
		this.monitoringThread = initializeMonitoringThread();

		soundFactory = new CountRelatedSoundFactory(5);
	}

	public void startMonitoring()
	{
		monitoringThread.start();
	}

	public void stopMonitoring()
	{
		monitorIsActive = false;
	}

	private Thread initializeMonitoringThread()
	{
		return new Thread(() -> {
			monitorIsActive = true;
			while (monitorIsActive)
			{
				monitor();
			}
		});
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

		Logger.getAnonymousLogger().log(Level.INFO, fault.description());
		soundFactory.playSound(fault);
	}

	public static void main(String[] args)
	{
		Marker baseMarker1 = new Marker("BASE", "BASE1", Coordinates.zero());
		Marker baseMarker2 = new Marker("BASE", "BASE2", Coordinates.zero());
		Marker baseMarker3 = new Marker("BASE", "BASE3", Coordinates.zero());
		Marker baseMarker4 = new Marker("BASE", "BASE4", Coordinates.zero());
		List<Marker> markerList = Arrays.asList(baseMarker1, baseMarker2, baseMarker3, baseMarker4);

		FaultMonitor faultMonitor = new FaultMonitor(markerList, new NotVisibleMarkerDetector());
		faultMonitor.startMonitoring();
	}
}
