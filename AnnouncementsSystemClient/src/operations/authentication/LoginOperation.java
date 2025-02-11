package operations.authentication;

import operations.Operation;

public class LoginOperation extends Operation {
    private final String user;
    private final String password;

    public LoginOperation(String user, String password) {
        super("5");
        this.user = user;
        this.password = password;
    }

    @Override
    public String toString() {
        return "LoginOperation{" +
                "user='" + user + '\'' +
                ", password='" + password + '\'' +
                ", op='" + op + '\'' +
                '}';
    }
}
