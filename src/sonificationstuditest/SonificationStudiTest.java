/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package sonificationstuditest;

import de.dhbw.rahmlab.vicon.datastream.api.DataStreamClient;
import java.util.ArrayList;
import java.util.List;
import sonificationstuditest.Marker.Marker;
import static sonificationstuditest.MarkerTracker.captureCurrentMarkers;
import static sonificationstuditest.MarkerTracker.trackAndGetNotVisibleMarkers;

/**
 *
 * @author noahh
 */
public class SonificationStudiTest
{

    private static DataStreamClient client;

    /**
     * @param args the command line arguments
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException
    {
        MarkerTracker.captureCurrentMarkers();
        List<Marker> notVisibleMarkers = new ArrayList<>();
        while (true)
        {
            final List<Marker> newNotVisibleMarkers = trackAndGetNotVisibleMarkers();
            for (Marker marker : newNotVisibleMarkers)
            {
                if (!MarkerTracker.isMarkerInList(notVisibleMarkers, marker))
                {
                    System.out.println(marker.getMarkerName());
                    notVisibleMarkers.add(marker);
                }
            }

            for (Marker marker : notVisibleMarkers)
            {
                if (!MarkerTracker.isMarkerInList(newNotVisibleMarkers, marker))
                {
                    System.out.println(marker.getMarkerName() + " is visible again!");
                }
            }

            notVisibleMarkers = newNotVisibleMarkers;
        }
    }

}
