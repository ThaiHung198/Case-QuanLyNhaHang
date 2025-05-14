package Model.entities;
import java.io.Serializable;
public abstract class Meal implements Serializable {
    private static final long serialVersionUID = 1L;
    public String name;
    public double price;

    public Meal(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public abstract String getCategory();
}
