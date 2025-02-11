package services;

import java.sql.SQLException;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import repositories.AccountRepository;
import models.Account;
import responses.LoginRes;
import responses.Response;

public class LoginService {
    private String loggedInUserId;
    private String loggedInUserToken;
    private int loggedInUserRole;
    private boolean noLongerLoggedIn;

    String generateToken(String id) {
       return id;
    }

    public Response login(JsonObject json) {
        if (loggedInUserId != null || loggedInUserToken != null) return new Response("004", "Already logged in");

        JsonElement userIdElement = json.get("user");
        JsonElement passwordElement = json.get("password");

        if(userIdElement == null || passwordElement == null || userIdElement.isJsonNull() || passwordElement.isJsonNull()) return new Response("002", "Fields missing");

        Account account;

        try {
            account = new AccountRepository().getById(userIdElement.getAsString());

            if(account == null || !passwordElement.getAsString().equals(account.password())) return new Response("003", "Login failed");
        } catch (SQLException ex) {
            return new Response("005", "Unknown error.");
        }

        this.loggedInUserId = account.userId();
        this.loggedInUserToken = generateToken(account.userId());
        this.loggedInUserRole = AccountService.getAccountRole(account.isAdmin());

        return new LoginRes("00" + loggedInUserRole, "Successful login", loggedInUserToken);
    }

    public Response logout(JsonObject json) {
        if (loggedInUserId == null || loggedInUserToken == null) return new Response("012", "User not logged in.");

        JsonElement token = json.get("token");

        if(token == null || token.isJsonNull()) return new Response("011", "Fields missing");

        if(!token.getAsString().equals(loggedInUserToken)) return new Response("013", "Logout failed");

        noLongerLoggedIn = true;

        return new Response("010", "Successful logout");
    }

    public String getLoggedInUserId() {
        return this.loggedInUserId;
    }

    public String getLoggedInUserToken() {
        return this.loggedInUserToken;
    }

    public int getLoggedInUserRole() {
        return this.loggedInUserRole;
    }

    public boolean isNoLongerLoggedIn() {
        return noLongerLoggedIn;
    }

    public void setNoLongerLoggedIn(boolean noLongerLoggedIn) {
        this.noLongerLoggedIn = noLongerLoggedIn;
    }
}
