/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package marker;

import java.util.concurrent.TimeUnit;

import de.dhbw.rahmlab.vicon.datastream.api.DataStreamClient;

/**
 *
 * @author noahh
 */
public class DataStreamClientProvider
{
	private DataStreamClient client;

	public DataStreamClientProvider()
	{
		client = buildClient();
	}

	public DataStreamClient getClient()
	{
		if (client == null)
		{
			return buildClient();
		}

		if (!client.isConnected())
		{
			connectClient(client);
			setupConnectedClient(client);
		}

		return client;
	}

	public DataStreamClient buildClient()
	{
		DataStreamClient newClient = new DataStreamClient();
		connectClient(newClient);
		setupConnectedClient(newClient);

		return newClient;
	}

	private void connectClient(DataStreamClient client)
	{
		client.connect("192.168.10.1", TimeUnit.SECONDS.toMillis(20));
	}

	private void setupConnectedClient(DataStreamClient client)
	{
		if (client != null && client.isConnected())
		{
			client.enableMarkerData();
			client.enableMarkerRayData();
			client.getFrame();
		}
	}
}
