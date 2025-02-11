package services;

import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import repositories.AccountRepository;
import models.Account;
import responses.AccountRes;
import responses.Response;

import java.sql.SQLException;

public class AccountService {

    private static boolean isNotValidUserId(String userId) {
        return (userId == null) || (!userId.matches("\\d{7}"));
    }

    private static boolean isInvalidPassowrd(String password) {
        return (password == null) || (!password.matches("\\d{4}"));
    }

    private static boolean isNotValidName(String name) {
        return (name == null) || (name.length() <= 1);
    }

    private static String truncateString(String str) {
        return (str != null) && (str.length() > 40) ? str.substring(0, 40) : str;
    }

    public static int getAccountRole(Boolean isAdmin) {
        return (isAdmin != null && isAdmin) ? 1 : 0;
    }

    public static Response create(JsonObject json) {
        JsonElement user = json.get("user");
        JsonElement password = json.get("password");
        JsonElement name = json.get("name");

        if(user == null || password == null || name == null || user.isJsonNull() || password.isJsonNull() || name.isJsonNull()) return new Response("101", "Fields missing");

        String id = user.getAsString();
        String pass = password.getAsString();
        String nam = name.getAsString();

        if(isNotValidUserId(id) || isInvalidPassowrd(pass) || isNotValidName(nam)) return new Response("102", "Invalid information inserted: user or password");

        try {
            if(new AccountRepository().getById(id) != null) return new Response("103", "Already exists an account with the username");

            new AccountRepository().create(id, truncateString(nam), pass);
        } catch (SQLException e) {
            System.out.println("INFO: Account creation error: " + e.getLocalizedMessage());
            return new Response("104", "Unknown error");
        }

        return new Response("100", "Successful account creation");
    }

    public static Response read(JsonObject json, LoginService loginService) {
        String loggedInUserId = loginService.getLoggedInUserId();
        String loggedInUserToken = loginService.getLoggedInUserToken();
        int loggedInUserRole = loginService.getLoggedInUserRole();

        if(loggedInUserId == null || loggedInUserToken == null) {
            return new Response(
                    "116",
                    "Cannot perform operation while logged out."
            );
        }

        JsonElement user = json.get("user");
        JsonElement token = json.get("token");

        String id = (user != null && !user.isJsonNull()) ? user.getAsString() : loggedInUserId;
        String tokenStr = (token != null && !token.isJsonNull()) ? token.getAsString() : null;

        if (id.isBlank()) id = loggedInUserId;

        if(loggedInUserRole != 1) {
            if(!loggedInUserToken.equals(tokenStr)) return new Response("112", "Invalid or empty token");

            if(!loggedInUserId.equals(id)) return new Response("113", "Invalid Permission, user does not have permission to visualize other users data");
        }

        Account account;

        try {
            account = new AccountRepository().getById(id);
            if(account == null) return new Response("114", "User not found ( Admin Only )");
        } catch (SQLException e) {
            System.out.println("INFO: Account read error: " + e.getLocalizedMessage());
            return new Response("115", "Unknown error");
        }

        return new AccountRes("11" + getAccountRole(account.isAdmin()), "Returns all information of the account", account.userId(), account.name(), account.password());
    }

    public static Response update(JsonObject json, LoginService loginService) {
        String loggedInUserId = loginService.getLoggedInUserId();
        String loggedInUserToken = loginService.getLoggedInUserToken();
        int loggedInUserRole = loginService.getLoggedInUserRole();

        if(loggedInUserId == null || loggedInUserToken == null) return new Response("125", "Cannot perform operation while logged out.");

        JsonElement user = json.get("user");
        JsonElement password = json.get("password");
        JsonElement name = json.get("name");
        JsonElement token = json.get("token");

        String id = (user != null && !user.isJsonNull()) ? user.getAsString() : loggedInUserId;
        String passwordStr = (password != null && !password.isJsonNull()) ? password.getAsString() : "";
        String nameStr = (name != null && !name.isJsonNull()) ? name.getAsString() : "";
        String tokenStr = (token != null && !token.isJsonNull()) ? token.getAsString() : null;

        if (id.isBlank()) id = loggedInUserId;

        if(!loggedInUserToken.equals(tokenStr)) return new Response("121", "Invalid or empty token");

        if(loggedInUserRole != 1 && !loggedInUserId.equals(id)) return new Response("122", "Invalid Permission, user does not have permission to update other users data");

        try {
            if(new AccountRepository().getById(id) == null) return new Response("123", "No user or token found ( Admin Only )}");

            if (!nameStr.isBlank()) {
                if (isNotValidName(nameStr)) return new Response("126", "Invalid information inserted");

                new AccountRepository().updateName(id, truncateString(nameStr));
            }

            if(!passwordStr.isBlank()) {
                if (isInvalidPassowrd(passwordStr)) return new Response("126", "Invalid information inserted");

                new AccountRepository().updatePassword(id, passwordStr);
            }
        } catch (SQLException e) {
            System.out.println("INFO: Account update error: " + e.getLocalizedMessage());
            return new Response("124", "Unknown error");
        }

        return new Response("120", "Account successfully updated");
    }

    public static Response delete(JsonObject json, LoginService loginService) {
        String loggedInUserId = loginService.getLoggedInUserId();
        String loggedInUserToken = loginService.getLoggedInUserToken();
        int loggedInUserRole = loginService.getLoggedInUserRole();

        if(loggedInUserId == null || loggedInUserToken == null) return new Response("136", "Cannot perform operation while logged out.");

        JsonElement user = json.get("user");
        JsonElement token = json.get("token");

        if(token == null || token.isJsonNull()) return new Response("131", "Fields missing");

        String id = (user != null && !token.isJsonNull()) ? user.getAsString() : loggedInUserId;

        if (id.isBlank()) id = loggedInUserId;

        if(!loggedInUserToken.equals(token.getAsString())) return new Response("132", "Invalid Token");

        if(loggedInUserRole != 1 && !loggedInUserId.equals(id)) return new Response("133", "Invalid Permission, user does not have permission to delete other users data");

        try {
            if(new AccountRepository().getById(id) == null) return new Response("134", "User not found ( Admin Only )");

            new AccountRepository().delete(id);
        } catch (SQLException e) {
            System.out.println("INFO: Account deletion error: " + e.getLocalizedMessage());
            return new Response("135", "Unknown error");
        }

        if(loggedInUserId.equals(id)) loginService.setNoLongerLoggedIn(true);

        return new Response("130", "Account successfully deleted");
    }
}
