package tools;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONObject;
import org.json.JSONException;
import cartago.*;

public class FeedBelt extends Artifact {
	String serverName = "127.0.0.1";
	int port = 8002;
	String serverUrlBase = "http://" + serverName + ":" + port;

	String token;
	String username = "feedBelt";
	String password = "feedBelt";

	void init() {
		defineObsProperty("request", false);
	}

	@OPERATION
	void setRequest(Boolean condition) {
		updateObsProperty("request", condition);
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
			System.out.println("Error in comunication for getTDProperties in FeedBelt");
		}
	}

	@OPERATION
	void checkSupply() {
		boolean result = checkOperation("checkSupply");
		if (!result) {
			failed("Error 'checkSupply' dosen't exist in FeedBeltTD");
			return;
		}
		try {
			boolean response = Boolean.parseBoolean(sendHttpPostRequestWithResponse(serverUrlBase + "/checkSupply", null, null));
			updateObsProperty("supply", response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@OPERATION
	void checkFeedBelt() {
		boolean result = checkOperation("checkFeedBelt");
		if (!result) {
			failed("Error 'checkCheckFeedBelt' dosen't exist in FeedBeltTD");
			return;
		}
		try {
			boolean response = Boolean.parseBoolean(sendHttpPostRequestWithResponse(serverUrlBase + "/checkFeedBelt", null, null));
			updateObsProperty("empty", response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@OPERATION
	void addRawPlate() {
		boolean result = checkOperation("addRawPlate");
		if (!result) {
			failed("Error 'addRawPlate' dosen't exist in FeedBeltTD");
			return;
		}
		try {
			sendHttpPostRequest(serverUrlBase + "/addRawPlate", false, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@OPERATION
	void changeStateOfRunning(Boolean running) {
		boolean result = checkOperation("changeStateOfRunning");
		if (!result) {
			failed("Error 'changeStateOfRunning' dosen't exist in FeedBeltTD");
			return;
		}
		try {
			sendHttpPostRequest(serverUrlBase + "/changeStateOfRunning", true, running);
			updateObsProperty("running", running);
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
					System.out.println("Propriety '" + propriety + "' added as observable property for FEEDBELT and it's type is" + type);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private boolean checkOperation(String action) {
		try {
			String tdContent = sendHttpGetRequest(serverUrlBase + "/td");
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

	private void sendHttpPostRequest(String endpoint, Boolean isRunningorWorking, Boolean condition) throws Exception {
		URL url = new URL(endpoint);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("POST");
		if (token != null) {
			connection.setRequestProperty("Token", token);
			if (isRunningorWorking)
				connection.setRequestProperty("Condition", String.valueOf(condition));
		}
		int responseCode = connection.getResponseCode();
		if (responseCode != HttpURLConnection.HTTP_OK) {
			System.err.println("Error during the connection to the server. Code of Error: " + responseCode);
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
