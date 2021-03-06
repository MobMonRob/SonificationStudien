package marker;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.internal.verification.VerificationModeFactory;

import de.dhbw.rahmlab.vicon.datastream.api.DataStreamClient;

public class MarkerTrackerTest
{
	private static DataStreamClient clientMock;
	private static DataStreamClientProvider clientProviderMock;
	private static final String SUBJECT = "Subject";
	private static final String MARKER = "Marker";
	private MarkerTracker markerTracker;

	@BeforeClass
	public static void setup()
	{
		clientMock = mock(DataStreamClient.class);
		when(clientMock.getSubjectCount()).thenReturn(1l);
		when(clientMock.getSubjectName(0)).thenReturn(SUBJECT);
		when(clientMock.getMarkerCount(SUBJECT)).thenReturn(1l);
		when(clientMock.getMarkerName(SUBJECT, 0)).thenReturn(MARKER);

		double[] coordinates = new double[]{1.0, 1.0, 1.0};
		when(clientMock.getMarkerGlobalTranslation(SUBJECT, MARKER)).thenReturn(coordinates);

		configureCameraDetectMarkerCount(2);

		clientProviderMock = mock(DataStreamClientProvider.class);
		when(clientProviderMock.buildClient()).thenReturn(clientMock);
		when(clientProviderMock.getClient()).thenReturn(clientMock);
	}

	private static void configureCameraDetectMarkerCount(long count)
	{
		when(clientMock.getMarkerRayContributionCount(SUBJECT, MARKER)).thenReturn(count);
	}

	@Test
	public void test()
	{
		markerTracker = new MarkerTracker(clientProviderMock);

		configureCameraDetectMarkerCount(2);

		/* Be sure to capture one marker */
		Assert.assertEquals(1, markerTracker.captureCurrentMarkers().size());
		verify(clientMock).getFrame();

		checkMarkerVisible();

		configureCameraDetectMarkerCount((long) 9e9);
		checkMarkerVisible();

		configureCameraDetectMarkerCount(1);
		checkMarkerNotVisible();

		configureCameraDetectMarkerCount(0);
		checkMarkerNotVisible();

		configureCameraDetectMarkerCount((long) -9e9);
		checkMarkerNotVisible();
	}

	private void checkMarkerVisible()
	{
		Assert.assertEquals(0, markerTracker.getNotVisibleMarkers().size());
		verify(clientMock, VerificationModeFactory.atLeastOnce()).getFrame();
	}

	private void checkMarkerNotVisible()
	{
		Assert.assertEquals(1, markerTracker.getNotVisibleMarkers().size());
		verify(clientMock, VerificationModeFactory.atLeastOnce()).getFrame();
	}
}
