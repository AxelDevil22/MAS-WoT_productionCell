package tools;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONObject;
import org.json.JSONException;
import cartago.*;

public class Press extends Artifact {
    String serverName = "127.0.0.1";
	int port = 8005;
	String serverUrlBase = "http://" + serverName + ":" + port;

	String token;
	String username = "press";
	String password	= "press";

	void init(){
		defineObsProperty("movement", true);
	}

	@OPERATION
	void getToken(){
		try {
			token = sendHttpPostRequestWithResponse( serverUrlBase  + "/getToken", username, password);
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
			System.out.println("Error in comunication for getTDProperties");
		}
	}


	@OPERATION
	void checkEmpty() {
		boolean result = checkOperation("checkEmpty");
		if (!result) {
			failed("Error 'checkEmpty' dosen't exist");
			return;
		}
		try {
			String checkEmptyURL = serverUrlBase+ "/checkEmpty";
			Boolean response = Boolean.parseBoolean(sendHttpPostRequestWithResponse(checkEmptyURL, null, null));
			updateObsProperty("empty", response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@OPERATION
	void checkPositionX() {
		boolean result = checkOperation("checkPositionX");
		if (!result) {
			failed("Error 'checkPositionX' dosen't exist");
			return;
		}
		try {
			String checkPositionXURL = serverUrlBase+ "/checkPositionX";
			int position = Integer.parseInt(sendHttpPostRequestWithResponse(checkPositionXURL, null, null));
			updateObsProperty("positionX", position);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@OPERATION
	void checkForging() {
		boolean result = checkOperation("checkForging");
		if (!result) {
			failed("Error 'checkForging' dosen't exist");
			return;
		}
        try {
			String checkForgingdURL = serverUrlBase + "/checkForging";
			Boolean response = Boolean.parseBoolean(sendHttpPostRequestWithResponse(checkForgingdURL, null, null));
			updateObsProperty("forging", response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@OPERATION
	void checkIsForged() {
		boolean result = checkOperation("checkIsForged");
		if (!result) {
			failed("Error 'checkIsForged' dosen't exist");
			return;
		}
        try {
			String checkIsForgedURL = serverUrlBase+ "/checkIsForged";
			Boolean response = Boolean.parseBoolean(sendHttpPostRequestWithResponse(checkIsForgedURL,null,null));
			updateObsProperty("isForged", response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@OPERATION
	void forgePlate() {
		boolean result = checkOperation("forgePlate");
		if (!result) {
			failed("Error 'forgePlate' dosen't exist");
			return;
		}
		try {
			String forgePlateURL = serverUrlBase+ "/forgePlate";
			sendHttpPostRequest(forgePlateURL, false, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@OPERATION
	void openPress(){
		boolean result = checkOperation("openPress");
		if (!result) {
			failed("Error 'checkEmptyArm1' dosen't exist");
			return;
		}
		try {
			String openPressURL = serverUrlBase + "/openPress";
			sendHttpPostRequest(openPressURL, false, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@OPERATION
	void closePress(){
		boolean result = checkOperation("closePress");
		if (!result) {
			failed("Error 'closePress' dosen't exist");
			return;
		}
		try {
			String closePressURL = serverUrlBase+ "/closePress";
			sendHttpPostRequest(closePressURL, false, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@OPERATION
	void setCondition(){
		updateObsProperty("isForged", true);
		updateObsProperty("forging", true);
		
	}

	@OPERATION
	void updateMovement(Boolean condition)
	{
		boolean result = checkOperation("movementProblem");
		if (!result) {
			failed("Error 'movement' dosen't exist in PressTD");
			return;
		}
		try {
			sendHttpPostRequest(serverUrlBase + "/movementProblem", true, condition);
			updateObsProperty("movement", condition);
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
					if (type.equals("boolean") && propriety.equals("empty"))
						defineObsProperty(propriety, true);
					else if (type.equals("boolean"))
						defineObsProperty(propriety, false);
					else
						defineObsProperty(propriety, 0);
					System.out.println("Propriety '" + propriety + "' added as observable  property for ERT and it's type is " + type);

				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private boolean checkOperation(String action)
	{
		try{
			String tdURL = serverUrlBase + "/td";
			String tdContent = sendHttpGetRequest(tdURL);
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
		if (token != null)
			connection.setRequestProperty("Token", token);
			if(isMovementWorking)
				connection.setRequestProperty("Condition", String.valueOf(condition));
		int responseCode = connection.getResponseCode();
		if (responseCode != HttpURLConnection.HTTP_OK) {
			System.err.println("Error during the connection to the server in [PRESS]. Code of Error: " + responseCode);
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
			// Leggi la risposta dal server
			InputStream in = connection.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String response = reader.readLine();
			reader.close();
			connection.disconnect();
			return response;
		} else {
			System.err.println("Errore durante la connessione al server. Codice di risposta: " + responseCode);
			connection.disconnect();
			return null;
		}
	}

}
