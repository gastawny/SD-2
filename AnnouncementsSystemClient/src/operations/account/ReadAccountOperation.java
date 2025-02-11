package operations.account;

import operations.Operation;

public class ReadAccountOperation extends Operation {
    private final String user;
    private final String token;

    public ReadAccountOperation(String user, String token) {
        super("2");
        this.user = user;
        this.token = token;
    }

    @Override
    public String toString() {
        return "ReadAccountOperation [op=" + op + ", user=" + user + ", token=" + token + "]";
    }
}
