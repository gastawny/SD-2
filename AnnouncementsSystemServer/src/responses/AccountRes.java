package responses;

public class AccountRes extends Response {
    private final String user;
    private final String name;
    private final String password;

    public AccountRes(String response, String message, String user, String name, String password) {
        super(response, message);
        this.user = user;
        this.name = name;
        this.password = password;
    }

    @Override
    public String toString() {
        return "AccountResponse{" +
                ", user='" + user + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", response='" + response + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
