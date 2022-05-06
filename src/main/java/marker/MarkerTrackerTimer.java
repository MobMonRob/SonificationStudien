package marker;

import java.util.concurrent.TimeUnit;

public class MarkerTrackerTimer extends Thread
{
	private static final int CAPTURE_INTERVAL_IN_SECONDS = 5;
	private MarkerTracker tracker;

	public MarkerTrackerTimer(MarkerTracker tracker)
	{
		this.tracker = tracker;
	}

	@Override
	public void run()
	{
		while (true)
		{
			try
			{
				Thread.sleep(TimeUnit.SECONDS.toMillis(CAPTURE_INTERVAL_IN_SECONDS));
				tracker.captureCurrentMarkers();
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
				Thread.currentThread().interrupt();
			}
		}
	}
}
