package tools;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONObject;
import org.json.JSONException;
import cartago.*;

public class Robot extends Artifact {
	String serverName = "127.0.0.1";
	int port = 8004;
	String serverUrlBase = "http://" + serverName + ":" + port;
	String token;
	String username = "robot";
	String password = "robot";

	void init() {
		defineObsProperty("pickERT", false);
		defineObsProperty("pickPress", false);
		defineObsProperty("movement", true);
	}

	@OPERATION
	void getToken() {
		try {
			token = sendHttpPostRequestWithResponse(serverUrlBase + "/getToken", username, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@OPERATION
	void getTDProperties() {
		try {
			String tdContent = sendHttpGetRequest(serverUrlBase + "/td");
			processProproetiesThingDescription(tdContent);
		} catch (Exception e) {
			System.out.println("Error in comunication for getTDProperties");
		}
	}

	@OPERATION
	void setPickPress(Boolean condition) {

		updateObsProperty("pickPress", condition);
	}

	@OPERATION
	void setPickERT(Boolean condition) {
		updateObsProperty("pickERT", condition);
	}


	@OPERATION
	void checkEmptyArm1() {
		boolean result = checkOperation("checkEmptyArm1");
		if (!result) {
			failed("Error 'checkEmptyArm1' dosen't exist in RobotTD");
			return;
		}
		try {
			Boolean response = Boolean.parseBoolean(sendHttpPostRequestWithResponse(serverUrlBase + "/checkEmptyArm1", null, null));
			updateObsProperty("emptyarm1", response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@OPERATION
	void checkEmptyArm2() {
		boolean result = checkOperation("checkEmptyArm2");
		if (!result) {
			failed("Error 'checkEmptyArm2' dosen't exist in RobotTD");
			return;
		}
		try {
			Boolean response = Boolean.parseBoolean(sendHttpPostRequestWithResponse(serverUrlBase + "/checkEmptyArm2", null, null));
			updateObsProperty("emptyarm2", response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@OPERATION
	void checkAngle() {
		boolean result = checkOperation("checkAngle");
		if (!result) {
			failed("Error 'checkAngle' dosen't exist in RobotTD");
			return;
		}
		try {
			float angle = Float.parseFloat(sendHttpPostRequestWithResponse(serverUrlBase + "/checkAngle", null, null));
			int angleInteger = Math.round(angle);
			updateObsProperty("anglerobot", angle);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@OPERATION
	void rotateUp() {
		boolean result = checkOperation("rotateUp");
		if (!result) {
			failed("Error 'rotateUp' dosen't exist in RobotTD");
			return;
		}
		try {
			sendHttpPostRequest( serverUrlBase + "/rotateUp", false, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@OPERATION
	void rotateDown() {
		boolean result = checkOperation("rotateDown");
		if (!result) {
			failed("Error 'rotateDown' dosen't exist in RobotTD");
			return;
		}
		try {
			sendHttpPostRequest(serverUrlBase + "/rotateDown", false, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@OPERATION
	void activateMagnet1() {
		boolean result = checkOperation("activateMagnet1");
		if (!result) {
			failed("Error 'activeMagnet1' dosen't exist in RobotTD");
			return;
		}
		try {
			sendHttpPostRequest(serverUrlBase + "/activeMagnet1", false, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@OPERATION
	void activateMagnet2() {
		boolean result = checkOperation("activateMagnet2");
		if (!result) {
			failed("Error 'activeMagnet2' dosen't exist in RobotTD");
			return;
		}
		try {
			sendHttpPostRequest(serverUrlBase + "/activeMagnet2", false, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@OPERATION
	void updateMovement(Boolean condition)
	{
		boolean result = checkOperation("movementProblem");
		if (!result) {
			failed("Error 'movement' dosen't exist in RobotTD");
			return;
		}
		try {
			updateObsProperty("movement", condition);
			sendHttpPostRequest(serverUrlBase + "/movementProblem", true, condition);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String sendHttpGetRequest(String endpoint) throws IOException {
		URL url = new URL(endpoint);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.setRequestProperty("Token", token);

		Scanner scanner = new Scanner(connection.getInputStream());
		StringBuilder response = new StringBuilder();
		while (scanner.hasNextLine()) {
			response.append(scanner.nextLine());
		}
		scanner.close();
		connection.disconnect();
		return response.toString();
	}

	private void processProproetiesThingDescription(String tdContent) {
		try {
			JSONObject td = new JSONObject(tdContent);
			if (td.has("properties")) {
				JSONObject properties = td.getJSONObject("properties");
				for (String propriety : properties.keySet()) {
					JSONObject types = properties.getJSONObject(propriety);
					String type = types.getString("type");
					if (type.equals("boolean"))
						defineObsProperty(propriety, true);
					else
						defineObsProperty(propriety, 0.0);
					System.out.println("Propriety '" + propriety + "' added as observable  property for ERT and it's type is " + type);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private boolean checkOperation(String action) {
		try {
			String tdURL = serverUrlBase + "/td";
			String tdContent = sendHttpGetRequest(tdURL);
			return processActionsThingDescription(tdContent, action);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private boolean processActionsThingDescription(String tdContent, String action) {
		try {
			JSONObject td = new JSONObject(tdContent);
			if (td.has("actions")) {
				JSONObject actions = td.getJSONObject("actions");
				for (String actionName : actions.keySet()) {
					if (action.equals(actionName))
						return true;
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}

	private void sendHttpPostRequest(String endpoint, Boolean isMovementWorking, Boolean condition) throws Exception {
		URL url = new URL(endpoint);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("POST");
		if (token != null)
			connection.setRequestProperty("Token", token);
			if(isMovementWorking)
				connection.setRequestProperty("Condition", String.valueOf(condition));
		int responseCode = connection.getResponseCode();
		if (responseCode != HttpURLConnection.HTTP_OK) {
			System.err.println("Error during the connection to the server in [ROBOT]. Code of Error: " + responseCode);
		}
		connection.disconnect();
	}

	private String sendHttpPostRequestWithResponse(String endpoint, String username, String password) throws Exception {
		URL url = new URL(endpoint);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("POST");
		connection.setDoOutput(true);
		connection.setRequestProperty("Content-Type", "application/json");
		if (username != null && password != null) {
			connection.setRequestProperty("Username", username);
			connection.setRequestProperty("Password", password);
		} else
			connection.setRequestProperty("Token", token);
		int responseCode = connection.getResponseCode();
		if (responseCode == HttpURLConnection.HTTP_OK) {
			InputStream in = connection.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String response = reader.readLine();
			reader.close();
			connection.disconnect();
			return response;
		}
		return null;
	}
}

