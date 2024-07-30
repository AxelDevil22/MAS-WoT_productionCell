package tools;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONObject;
import org.json.JSONException;
import cartago.*;


public class ERT extends Artifact {
	String serverName = "127.0.0.1";
	int port = 8003;
	String serverUrlBase = "http://" + serverName + ":" + port;

	String token;
	String username = "ert";
	String password	= "ert";


	//Propriety use for malfunction
	void init(){
		defineObsProperty("movement", true);
	}

	@OPERATION
	void getToken(){
		try {
			token = sendHttpPostRequestWithResponse(serverUrlBase  + "/getToken", username, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@OPERATION
	void getTDProperties(){
		try{
			String tdContent = sendHttpGetRequest(serverUrlBase + "/td");
			processProproetiesThingDescription(tdContent);
		}catch(Exception e)
		{
			System.out.println("Error in comunication for getTDProperties in ERT");
		}
	}

	@OPERATION
	void rotate() {
		boolean result = checkOperation("rotate");
		if (!result) {
			failed("Error 'rotate' dosen't exist in ErtTD");
			return;
		}
		try {
			sendHttpPostRequest(serverUrlBase + "/rotate", false, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@OPERATION
	void up() {
		boolean result = checkOperation("up");
		if (!result) {
			failed("Error 'up' dosen't exist in ErtTd");
			return;
		}
		try {
			sendHttpPostRequest(serverUrlBase + "/up", false, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@OPERATION
	void down() {
		boolean result = checkOperation("down");
		if (!result) {
			failed("Error 'down' dosen't exist in ErtTD");
			return;
		}
		try {
			sendHttpPostRequest(serverUrlBase + "/down", false, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@OPERATION
	void transferTo() {
		boolean result = checkOperation("transferTo");
		if (!result) {
			failed("Error 'transferTo' dosen't exist in ErtTD");
			return;
		}
		try {
			sendHttpPostRequest(serverUrlBase+ "/transferTO", false, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@OPERATION
	void checkEmpty() {
		boolean result = checkOperation("checkEmpty");
		if (!result) {
			failed("Error 'checkSupply' dosen't exist in ErtTD");
			return;
		}
		try {
			String response = sendHttpPostRequestWithResponse(serverUrlBase + "/checkEmpty", null, null);
			Boolean empty = Boolean.parseBoolean(response);
			updateObsProperty("empty", empty);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

		@OPERATION
	void checkAngle() {
			boolean result = checkOperation("checkAngle");
			if (!result) {
				failed("Error 'checkAngle' dosen't exist in ErtTD");
				return;
			}
		try {
			String response = sendHttpPostRequestWithResponse(serverUrlBase + "/checkAngle", null, null);
			float angle = Float.parseFloat(response);
			updateObsProperty("angle", angle);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@OPERATION
	void checkPositionY() {
		boolean result = checkOperation("checkPositionY");
		if (!result) {
			failed("Error 'checkPositionY' dosen't exist in ErtTD");
			return;
		}
		try {
			String response = sendHttpPostRequestWithResponse(serverUrlBase + "/checkPositionY", null, null);
			int position = Integer.parseInt(response);
			updateObsProperty("positionY", position);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@OPERATION
	void updateMovement(Boolean condition)
	{
		boolean result = checkOperation("movementProblem");
		if (!result) {
			failed("Error 'movement' dosen't exist in ErtTD");
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
					else if(type.equals("int"))
						defineObsProperty(propriety, 0);
					else
						defineObsProperty(propriety, 0.0);
					System.out.println("Propriety '" + propriety + "' added as observable  property for ERT and it's type is" + type);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private boolean checkOperation(String action)
	{
		try{
			String tdContent = sendHttpGetRequest(serverUrlBase + "/td");
			return processActionsThingDescription(tdContent, action);
		}catch(Exception e)
		{e.printStackTrace();
		}
		return false;
	}

	private boolean processActionsThingDescription(String tdContent, String action)
	{
		try {
			JSONObject td = new JSONObject(tdContent);
			if (td.has("actions")) {
				JSONObject actions = td.getJSONObject("actions");
				for (String actionName : actions.keySet()) {
					if (action.equals(actionName))
						return true;
				}
			}
		}catch (JSONException e) {e.printStackTrace();}
		return false;
	}

	private void sendHttpPostRequest(String endpoint, Boolean isMovementWorking, Boolean condition) throws Exception {
		URL url = new URL(endpoint);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("POST");
		if (token != null) {
			connection.setRequestProperty("Token", token);
			if (isMovementWorking)
				connection.setRequestProperty("Condition", String.valueOf(condition));
		}
		int responseCode = connection.getResponseCode();
		if (responseCode != HttpURLConnection.HTTP_OK) {
			System.err.println("Error during the connection to the server in [ERT]. Code of Error: " + responseCode);
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
