package operations.category;

import operations.Operation;

import java.util.List;

public class DeleteCategoryOperation extends Operation {
    private final String token;
    private final List<String> categoryIds;

    public DeleteCategoryOperation(String token, List<String> categoryIds) {
        super("10");
        this.token = token;
        this.categoryIds = categoryIds;
    }

    @Override
    public String toString() {
        return "DeleteCategoryOperation{" +
                "token='" + token + '\'' +
                ", categoryIds=" + categoryIds +
                ", op='" + op + '\'' +
                '}';
    }
}
