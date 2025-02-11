package operations.category;

import models.Category;
import operations.Operation;

import java.util.List;

public class CreateCategoryOperation extends Operation {
    private final String token;
    private final List<Category> categories;

    public CreateCategoryOperation(String token, List<Category> categories) {
        super("7");
        this.token = token;
        this.categories = categories;
    }

    @Override
    public String toString() {
        return "CreateCategoryOperation{" +
                "token='" + token + '\'' +
                ", categories=" + categories +
                ", op='" + op + '\'' +
                '}';
    }
}
