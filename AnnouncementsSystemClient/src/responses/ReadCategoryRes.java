package responses;

import com.google.gson.JsonElement;

import java.util.List;

public class ReadCategoryRes extends Response {

    public ReadCategoryRes(String responseJson) {
        super(responseJson);
    }

    public List<JsonElement> getCategories() {
        if(jsonObject != null) {
            JsonElement categoriesElement = jsonObject.get("categories");
            if(categoriesElement != null && !categoriesElement.isJsonNull() && categoriesElement.isJsonArray())
                return categoriesElement.getAsJsonArray().asList();
        }

        return null;
    }
}
