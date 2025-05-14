package Model.entities;
import java.io.Serializable;
import Model.interfaces.Payment;
import java.util.Date;
import java.util.UUID;
import java.util.ArrayList;
import java.util.List;

public abstract class Order implements Serializable {
    private static final long serialVersionUID = 2L;
    protected List<Meal> meals = new ArrayList<>();
    private String orderId;
    private Date timestamp;
    private Payment paymentDetails;
    private String customerIdentifier;

    public Order() {
        this.orderId = UUID.randomUUID().toString();
        this.timestamp = new Date();
    }
    public String getOrderId() {
        return orderId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public Payment getPaymentDetails() {
        return paymentDetails;
    }

    public void setPaymentDetails(Payment paymentDetails) {
        this.paymentDetails = paymentDetails;
    }

    public String getCustomerIdentifier() {
        return customerIdentifier;
    }

    public void setCustomerIdentifier(String customerIdentifier) {
        this.customerIdentifier = customerIdentifier;
    }

    public void addMeal(Meal meal) {
        meals.add(meal);
    }

    public double getTotal() {
        return meals.stream().mapToDouble(Meal::getPrice).sum();
    }

    public abstract void printOrder();

    public List<Meal> getMealList() {
        return new ArrayList<>(meals);
    }
}