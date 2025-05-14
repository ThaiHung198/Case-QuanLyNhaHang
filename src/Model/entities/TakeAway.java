package Model.entities;
import java.io.Serializable;

public class TakeAway extends Order implements Serializable{
    private static final long serialVersionUID = 1L;
    @Override
    public void printOrder() {
        System.out.println("Đơn hàng mang về:");
        meals.forEach(meal ->
                System.out.println("- " + meal.getName() + ": " + meal.getPrice() + " VND"));
    }
}