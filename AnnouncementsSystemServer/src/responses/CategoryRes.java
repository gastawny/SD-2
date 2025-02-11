package responses;

import models.Category;

import java.util.List;

public class CategoryRes extends Response {
    private final List<Category> categories;

    public CategoryRes(String response, String message, List<Category> categories) {
        super(response, message);
        this.categories = categories;
    }

    @Override
    public String toString() {
        return "CategoryResponse{" +
                "categories=" + categories +
                ", response='" + response + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
