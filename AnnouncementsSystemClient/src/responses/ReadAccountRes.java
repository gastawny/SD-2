package responses;

import com.google.gson.JsonElement;

public class ReadAccountRes extends Response {

    public ReadAccountRes(String responseJson) {
        super(responseJson);
    }

    public String getUserId() {
        if(jsonObject != null) {
            JsonElement userElement = jsonObject.get("user");
            if(userElement != null && !userElement.isJsonNull())
                return userElement.getAsString();
        }

        return null;
    }

    public String getPassword() {
        if(jsonObject != null) {
            JsonElement passwordElement = jsonObject.get("password");
            if(passwordElement != null && !passwordElement.isJsonNull())
                return passwordElement.getAsString();
        }

        return null;
    }

    public String getName() {
        if(jsonObject != null) {
            JsonElement nameElement = jsonObject.get("name");
            if(nameElement != null && !nameElement.isJsonNull())
                return nameElement.getAsString();
        }

        return null;
    }
}
