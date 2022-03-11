/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sonificationstuditest;

import de.dhbw.rahmlab.vicon.datastream.api.DataStreamClient;
import de.dhbw.rahmlab.vicon.datastream.impl.Client;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author noahh
 */
public class DataStreamClientProvider
{
    private static DataStreamClient dataStreamClient;
    
    public static DataStreamClient getConnectedDataStreamClient()
    {
        if (dataStreamClient == null)
        {
            initializeClient();
        }
        
        if (!dataStreamClient.isConnected())
        {
            connectClient();
            setupConnectedClient();
        }
        
        return dataStreamClient;
    }
    
    private static void initializeClient()
    {
        dataStreamClient = new DataStreamClient();
        connectClient();
        setupConnectedClient();
    }
    
    private static void connectClient()
    {
        dataStreamClient.connect("192.168.10.1", TimeUnit.SECONDS.toMillis(20));
    }
    
    private static void setupConnectedClient()
    {
        if (dataStreamClient != null && dataStreamClient.isConnected())
        {
            dataStreamClient.enableMarkerData();
            dataStreamClient.enableMarkerRayData();
            dataStreamClient.getFrame();
        }
    }
}
