package responses;

import com.google.gson.JsonElement;

public class ClientLoginRes extends Response {

    public ClientLoginRes(String responseJson) {
        super(responseJson);
    }

    public String getToken() {
        if(jsonObject != null) {
            JsonElement tokenElement = jsonObject.get("token");
            if(tokenElement != null && !tokenElement.isJsonNull())
                return tokenElement.getAsString();
        }

        return null;
    }
}
