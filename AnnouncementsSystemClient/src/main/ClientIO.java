package main;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import models.Category;
import operations.account.CreateAccountOperation;
import operations.account.DeleteAccountOperation;
import operations.account.ReadAccountOperation;
import operations.account.UpdateAccountOperation;
import operations.authentication.LoginOperation;
import operations.authentication.LogoutOperation;
import operations.category.CreateCategoryOperation;
import operations.category.DeleteCategoryOperation;
import operations.category.ReadCategoryOperation;
import operations.category.UpdateCategoryOperation;

import java.io.IOException;
import java.util.*;

public class ClientIO {
    Client client;

    public ClientIO(Client client){
        this.client = client;
    }

    public void start(){
        Scanner scann = new Scanner(System.in);

        System.out.println("Enter the server IP: ");
        String ip = scann.nextLine();

        System.out.println("Enter the server port: ");
        int port = scann.nextInt();

        new Client().startConnection(ip, port);
    }

    public void operations() {
        Scanner scann = new Scanner(System.in);

        try {
            while(true) {
                System.out.println("1 - Register");
                System.out.println("2 - Login");
                System.out.println("3 - Logout");
                System.out.println("4 - View user info");
                System.out.println("5 - Update user info");
                System.out.println("6 - Delete user");
                System.out.println("7 - View categories");
                System.out.println("8 - Create category");
                System.out.println("9 - Update category");
                System.out.println("10 - Delete category");

                int option = scann.nextInt();
                scann.nextLine();

                switch(option) {
                    case 1:
                        signUp(scann);
                        break;
                    case 2:
                        login(scann);
                        break;
                    case 3:
                        logout();
                        return;
                    case 4:
                         readAccount();
                        break;
                    case 5:
                         updateAccount();
                        break;
                    case 6:
                         deleteAccount();
                        break;
                    case 7:
                         viewCategories();
                        break;
                    case 8:
                         createCategory();
                        break;
                    case 9:
                         updateCategory();
                        break;
                        case 10:
                         deleteCategory();
                        break;
                    default:
                        System.out.println("Invalid option");
                }
            }
        } catch (IOException e) {
            System.out.println("ERR: " + e.getLocalizedMessage());
        }
    }

    private void signUp(Scanner scann) throws IOException {
        System.out.println("Enter your RA: ");
        String ra = scann.nextLine();
        System.out.println("Enter your name: ");
        String name = scann.nextLine();
        System.out.println("Enter your password: ");
        String password = scann.nextLine();

        String response = client.sendToServer(new CreateAccountOperation(ra, password, name));
        handleResponse(response, new String[]{"100"}, "Registering user");
    }

    private void login(Scanner scann) throws IOException {
        System.out.println("Enter your RA: ");
        String ra = scann.nextLine();
        System.out.println("Enter your password: ");
        String password = scann.nextLine();

        String response = client.sendToServer(new LoginOperation(ra, password));
        handleResponse(response, new String[]{"000", "001"}, "Logging in");

        JsonObject json = JsonParser.parseString(response).getAsJsonObject();
        this.client.setLoggedInUserToken(json.get("token") != null ? json.get("token").getAsString() : "");
    }

    private void logout() throws IOException {
        String response = client.sendToServer(new LogoutOperation(this.client.getLoggedInUserToken()));
        handleResponse(response, new String[]{"010"}, "Logging out");
    }

    private void readAccount() throws IOException {
        String response = client.sendToServer(new ReadAccountOperation(this.client.getLoggedInUserId(), this.client.getLoggedInUserToken()));
        handleResponse(response, new String[]{"110", "111"}, "Reading user info");
    }

    private void updateAccount() throws IOException {
        Scanner scann = new Scanner(System.in);

        System.out.println("Enter your new name: ");
        String name = scann.nextLine();
        System.out.println("Enter your new password: ");
        String password = scann.nextLine();

        String response = client.sendToServer(new UpdateAccountOperation(this.client.getLoggedInUserId(), password, name, this.client.getLoggedInUserToken()));
        handleResponse(response, new String[]{"120"}, "Updating user info");
    }

    private void deleteAccount() throws IOException {
        String response = client.sendToServer(new DeleteAccountOperation(this.client.getLoggedInUserId(), this.client.getLoggedInUserToken()));
        handleResponse(response, new String[]{"130"}, "Deleting user");
    }

    private void viewCategories() throws IOException {
        String response = client.sendToServer(new ReadCategoryOperation(this.client.getLoggedInUserToken()));
        handleResponse(response, new String[]{"210"}, "Viewing categories");
    }

    private void createCategory() throws IOException {
        Scanner scann = new Scanner(System.in);
        List<Category> categories = new ArrayList<>();

        while(true) {
            System.out.println("1 - Create category");
            System.out.println("2 - Cancel");

            if(Objects.equals(scann.nextLine(), "2")) break;

            System.out.println("Enter the category name: ");
            String name = scann.nextLine();

            System.out.println("Enter the category description: ");
            String description = scann.nextLine();

            categories.add(new Category(name, description));
        }

        String response = client.sendToServer(new CreateCategoryOperation(this.client.getLoggedInUserToken(), categories));
        handleResponse(response, new String[]{"200"}, "Creating category");
    }

    private void updateCategory() throws IOException {
        Scanner scann = new Scanner(System.in);
        List<Category> categories = new ArrayList<>();

        while(true) {
            System.out.println("1 - Update category");
            System.out.println("2 - Cancel");

            if(Objects.equals(scann.nextLine(), "2")) break;

            System.out.println("Enter the category id: ");
            String id = scann.nextLine();

            System.out.println("Enter the category name: ");
            String name = scann.nextLine();

            System.out.println("Enter the category description: ");
            String description = scann.nextLine();

            categories.add(new Category(id, name, description));
        }

        String response = client.sendToServer(new UpdateCategoryOperation(this.client.getLoggedInUserToken(), categories));
        handleResponse(response, new String[]{"220"}, "Updating category");
    }

    private void deleteCategory() throws IOException {
        Scanner scann = new Scanner(System.in);
        List<String> ids = new ArrayList<>();

        while(true) {
            System.out.println("1 - Delete category");
            System.out.println("2 - Cancel");

            if(Objects.equals(scann.nextLine(), "2")) break;

            System.out.println("Enter the category id: ");
            ids.add(scann.nextLine());
        }

        String response = client.sendToServer(new DeleteCategoryOperation(this.client.getLoggedInUserToken(), ids));
        handleResponse(response, new String[]{"230"}, "Deleting category");
    }

    private void handleResponse(String response, String[] op, String errMessage) {
        if(response == null) {
            System.out.println("ERR: The received response was invalid");
            return;
        }

        JsonObject receivedJson = JsonParser.parseString(response).getAsJsonObject();
        System.out.println("RES: " + receivedJson);

        String res = receivedJson.get("response") != null ? receivedJson.get("response").getAsString() : "";
        String message = receivedJson.get("message") != null ? receivedJson.get("message").getAsString() : "";

        if(Arrays.asList(op).contains(res)) {
            System.out.println(message);

            return;
        }

        System.out.println("ERR: " + errMessage + " - " + message);
    }
}