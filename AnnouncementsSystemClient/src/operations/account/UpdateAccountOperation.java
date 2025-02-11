package operations.account;

import operations.Operation;

public class UpdateAccountOperation extends Operation {
    private final String user;
    private final String token;
    private final String password;
    private final String name;

    public UpdateAccountOperation(String user, String password, String name, String token) {
        super("3");
        this.user = user;
        this.password = password;
        this.name = name;
        this.token = token;
    }

    @Override
    public String toString() {
        return "UpdateAccountOperation{" +
                "user='" + user + '\'' +
                ", token='" + token + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", op='" + op + '\'' +
                '}';
    }
}
