package services;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import repositories.CategoryRepository;
import models.Category;
import responses.CategoryRes;
import responses.Response;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryService {

    public static Response create(JsonObject json, LoginService loginService) {
        JsonElement categories = json.get("categories");
        JsonElement token = json.get("token");

        if (categories == null || categories.isJsonNull() || token == null || token.isJsonNull()) return new Response("201", "Missing fields");

        List<JsonElement> categoriesList = categories.getAsJsonArray().asList();
        String loggedInUserToken = loginService.getLoggedInUserToken();
        int loggedInUserRole = loginService.getLoggedInUserRole();

        if(!loggedInUserToken.equals(token.getAsString()) || loggedInUserRole != 1) return new Response("202", "Invalid token");

        List<Category> categoriesToCreate = new ArrayList<>();

        for (JsonElement categoryElement : categoriesList) {
            JsonObject categoryObject;

            try {
                categoryObject = categoryElement.getAsJsonObject();
            } catch (Exception e) {
                return new Response("201", "Missing fields");
            }

            JsonElement nameElement = categoryObject.get("name");
            JsonElement descriptionElement = categoryObject.get("description");

            if (nameElement == null || nameElement.isJsonNull()) return new Response("201", "Missing fields");

            String name = nameElement.getAsString();
            String description = (descriptionElement != null && !descriptionElement.isJsonNull()) ? descriptionElement.getAsString() : "";

            if(name.isBlank()) return new Response("201", "Missing fields");

            categoriesToCreate.add(new Category("", name, description));
        }

        try {
            CategoryRepository.create(categoriesToCreate);
        } catch (SQLException e) {
            return new Response("203", "Unknown error");
        }

        return new Response("200", "Successful category creation");
    }

    public static Response read(JsonObject json, LoginService loginService) {
        JsonElement token = json.get("token");

        if (token == null || token.isJsonNull()) return new Response("211", "Missing fields");

        String loggedInUserToken = loginService.getLoggedInUserToken();
        int loggedInUserRole = loginService.getLoggedInUserRole();

        if(!token.getAsString().equals(loggedInUserToken) || loggedInUserRole != 1) return new Response("212", "Invalid token");

        try {
            return new CategoryRes("210", "Successful category read", CategoryRepository.readAll());
        } catch (SQLException e) {
            return new Response("213", "Unknown error");
        }
    }

    public static Response update(JsonObject json, LoginService loginService) {
        JsonElement categories = json.get("categories");
        JsonElement token = json.get("token");

        if (categories == null || categories.isJsonNull() || !categories.isJsonArray() || token == null || token.isJsonNull()) return new Response("221", "Missing fields");

        List<JsonElement> categoriesList = categories.getAsJsonArray().asList();
        String loggedInUserToken = loginService.getLoggedInUserToken();
        int loggedInUserRole = loginService.getLoggedInUserRole();

        if(!loggedInUserToken.equals(token.getAsString()) || loggedInUserRole != 1) return new Response("222", "Invalid token");

        List<Category> categoriesToUpdate = new ArrayList<>();

        for (JsonElement categoryElement : categoriesList) {
            JsonObject category;

            try {
                category = categoryElement.getAsJsonObject();
            } catch (Exception e) {
                return new Response("221", "Missing fields");
            }

            JsonElement idElement = category.get("id");
            JsonElement nameElement = category.get("name");
            JsonElement descriptionElement = category.get("description");

            if (idElement == null || idElement.isJsonNull())
                return new Response("221", "Missing fields");

            String categoryId = idElement.getAsString();
            String name = (nameElement != null && !nameElement.isJsonNull()) ? nameElement.getAsString() : "";
            String description = (descriptionElement != null && !descriptionElement.isJsonNull()) ? descriptionElement.getAsString() : "";

            if(categoryId.isBlank()) return new Response("221", "Missing fields");

            try {
                if(CategoryRepository.read(categoryId) == null) return new Response("223", "Invalid information inserted");
            } catch (SQLException e) {
                return new Response("224", "Unknown error");
            }

            categoriesToUpdate.add(new Category(categoryId, name, description));
        }

        try {
            CategoryRepository.update(categoriesToUpdate);

            return new Response("220", "Successful category update");
        } catch (SQLException e) {
            return new Response("224", "Unknown error");
        }
    }

    public static Response delete(JsonObject json, LoginService loginService) {
        JsonElement ids = json.get("categoryIds");
        JsonElement token = json.get("token");
        System.out.println(1);
        if (ids == null || ids.isJsonNull() || token == null || token.isJsonNull()) return new Response("231", "Missing fields");

        List<JsonElement> categoryIdsList = ids.getAsJsonArray().asList();
        String loggedInUserToken = loginService.getLoggedInUserToken();
        int loggedInUserRole = loginService.getLoggedInUserRole();

        if(!loggedInUserToken.equals(token.getAsString()) || loggedInUserRole != 1) return new Response("232", "Invalid token");

        List<String> categoriesToDelete = new ArrayList<>();

        for (JsonElement categoryIdElement : categoryIdsList) {
            System.out.println(2);
            if (categoryIdElement == null || categoryIdElement.isJsonNull()) return new Response("231", "Missing fields");
            System.out.println(3);
            String id = categoryIdElement.getAsString();
            if(id.isBlank()) return new Response("231", "Missing fields");

            categoriesToDelete.add(id);

            try {
                if (CategoryRepository.read(id) == null) return new Response("233", "Invalid information inserted");
            } catch (SQLException e) {
                return new Response("235", "Unknown error");
            }
        }

        try {
            CategoryRepository.delete(categoriesToDelete);

            return new Response("230", "Successful category deletion");
        } catch (SQLException e) {
            return new Response("234", "Category in use");
        }
    }
}
