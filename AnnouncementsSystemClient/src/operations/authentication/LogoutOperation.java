package operations.authentication;

import operations.Operation;

public class LogoutOperation extends Operation {

    private final String token;

    public LogoutOperation(String token) {
        super("6");
        this.token = token;
    }

    @Override
    public String toString() {
        return "LogoutOperation{" +
                "token='" + token + '\'' +
                ", op='" + op + '\'' +
                '}';
    }
}
