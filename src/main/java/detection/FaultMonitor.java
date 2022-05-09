package detection;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import controlling.Controller;
import detection.detectors.FaultDetector;
import detection.detectors.NotVisibleMarkerDetector;
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
	public FaultMonitor(FaultDetector... detectors)
	{
		this.detectors = Arrays.asList(detectors);
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

		soundFactory.playSound(fault);
	}

	public static void main(String[] args)
	{
		FaultMonitor faultMonitor = new FaultMonitor(new NotVisibleMarkerDetector());
		faultMonitor.startMonitoring();
	}
}
