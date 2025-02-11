package operations.category;

import models.Category;
import operations.Operation;

import java.util.List;

public class UpdateCategoryOperation extends Operation {
    private final String token;
    private final List<Category> categories;

    public UpdateCategoryOperation(String token, List<Category> categories) {
        super("9");
        this.token = token;
        this.categories = categories;
    }

    @Override
    public String toString() {
        return "UpdateCategoryOperation{" +
                "token='" + token + '\'' +
                ", categories=" + categories +
                ", op='" + op + '\'' +
                '}';
    }
}
