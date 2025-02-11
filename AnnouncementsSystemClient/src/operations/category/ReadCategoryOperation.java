package operations.category;

import operations.Operation;

public class ReadCategoryOperation extends Operation {
    private final String token;

    public ReadCategoryOperation(String token) {
        super("8");
        this.token = token;
    }

    @Override
    public String toString() {
        return "ReadCategoryOperation{" +
                "token='" + token + '\'' +
                ", op='" + op + '\'' +
                '}';
    }
}
