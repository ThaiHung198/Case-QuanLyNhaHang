package Model.entities;
import java.io.Serializable;

public class Dish extends Meal implements Serializable {
    private static final long serialVersionUID = 1L;
    public String category;

    public Dish(String name, String category, double price) {
        super(name, price);
        this.category = category;
    }

    @Override
    public String getCategory() {
        return category;
    }
}
