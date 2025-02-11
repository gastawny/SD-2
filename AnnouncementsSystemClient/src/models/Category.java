package models;

public class Category {
    private String id;
    private final String name;
    private final String description;

    public Category(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Category(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    @Override
    public String toString() {
        return "Id: " + id + ", Nome: " + name + ", Descrição: " + description + '\n';
    }
}
