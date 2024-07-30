package tools;

import cartago.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class Start extends Artifact {
	String serverName = "127.0.0.1";
	int port = 8001;
	String serverUrlBase = "http://" + serverName + ":" + port;
	void init() {
		try {
			String activateProductionEndpoint = serverUrlBase + "/activateCellProduction";
			sendHttpPostRequest(activateProductionEndpoint);
			System.out.println("Activate Production Cell complete");
		} catch (Exception e) {
			System.out.println("Error in the communication for activating cell production to the server start");
			e.printStackTrace();
		}

		try {
			String activateSupplyEndpoint = serverUrlBase+ "/activateAddSupply";
			sendHttpPostRequest(activateSupplyEndpoint);
			System.out.println("Activate display for 'Add Supply' complete");

		} catch (Exception e) {
			System.out.println("Error in the communication for activating 'Add Supply' button to the server start");
			e.printStackTrace();
		}

	}
	private void sendHttpPostRequest(String endpoint) throws Exception {
		URL url = new URL(endpoint);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("POST");
		int responseCode = connection.getResponseCode();
		if (responseCode != HttpURLConnection.HTTP_OK)
			System.err.println("Error during connection to the server. Response code: " + responseCode);
		connection.disconnect();
	}

}
