package responses;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Response {
    protected final String responseJson;
    protected JsonObject jsonObject;

    public Response(String responseJson) {
        this.responseJson = responseJson;
        this.jsonObject = parseJson();
    }

    public JsonObject parseJson() {
        try {
            return JsonParser.parseString(responseJson).getAsJsonObject();
        } catch (Exception e) {
            return null;
        }
    }

    public String getResponseCode() {
        if(jsonObject != null) {
            JsonElement responseElement = jsonObject.get("response");
            if (responseElement != null && !responseElement.isJsonNull())
                return responseElement.getAsString();
        }

        return "";
    }

    public String getMessage() {
        if(jsonObject != null) {
            JsonElement messageElement = jsonObject.get("message");
            if(messageElement != null && !messageElement.isJsonNull())
                return messageElement.getAsString();
        }

        return "Received an invalid response from the server.";
    }
}
