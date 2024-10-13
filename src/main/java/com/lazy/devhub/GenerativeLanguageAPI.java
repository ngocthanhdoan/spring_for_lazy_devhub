package com.lazy.devhub;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONObject;

public class GenerativeLanguageAPI {

    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent";
    private String apiKey;
    private String conversationContext;

    public GenerativeLanguageAPI(String apiKey) {
        this.apiKey = apiKey;
        this.conversationContext = ""; // Initialize conversation context
    }

    public JSONObject sendRequest(String text) throws Exception {
        JSONObject requestData = createRequestData(text);
        String response = makeApiCall(requestData);
        return new JSONObject(response);
    }

    private JSONObject createRequestData(String text) {
        JSONObject requestData = new JSONObject();
        JSONArray contentsArray = new JSONArray();
        JSONObject content = new JSONObject();
        JSONArray partsArray = new JSONArray();
        JSONObject part = new JSONObject();

        part.put("text", text);
        partsArray.put(part);
        content.put("parts", partsArray);
        contentsArray.put(content);
        requestData.put("contents", contentsArray);

        return requestData;
    }

    private String makeApiCall(JSONObject requestData) throws Exception {
        URL url = new URL(API_URL + "?key=" + apiKey);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoOutput(true);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = requestData.toString().getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        StringBuilder response = new StringBuilder();
        try (Scanner scanner = new Scanner(connection.getInputStream(), "UTF-8")) {
            while (scanner.hasNextLine()) {
                response.append(scanner.nextLine());
            }
        }

        return response.toString();
    }

    public void continueConversation(String userInput) throws Exception {
        // Maintain the context for the conversation
        this.conversationContext += userInput + "\n";
        JSONObject response = sendRequest(this.conversationContext);
    
        // Fix: Access content directly as a JSONObject
        String botReply = response.getJSONArray("candidates").getJSONObject(0)
                                  .getJSONObject("content")  // content is a JSONObject, not JSONArray
                                  .getJSONArray("parts").getJSONObject(0)
                                  .getString("text");
    
        // Print the bot's reply
        System.out.println("Bot: " + botReply);
    
        // Update the context with the bot's reply
        this.conversationContext += botReply + "\n";
    }
    

    public static void main(String[] args) {
        try {
            GenerativeLanguageAPI api = new GenerativeLanguageAPI("AIzaSyCHBnM1on4ZRgs2xJROQzeV3g_yKkMh3Co");

            // Sample conversation loop
            Scanner scanner = new Scanner(System.in);
            System.out.println("Chatbot is ready! Type your message (type 'exit' to quit):");

            while (true) {
                System.out.print("You: ");
                String userInput = scanner.nextLine();

                if ("exit".equalsIgnoreCase(userInput)) {
                    break;
                }

                api.continueConversation(userInput);
            }

            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
