package Model.entities;
import java.io.Serializable;
import Model.interfaces.Payment;

public class CardPayment implements Payment,Serializable {
    private static final long serialVersionUID = 1L;
    private String customerName;
    private String cardNumber;

    public CardPayment(String customerName, String cardNumber) {
        this.customerName = customerName;
        this.cardNumber = cardNumber;
    }

    public String getCustomerName(){
        return customerName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    @Override
    public void pay(double amount) {
        System.out.println("Thanh toán thẻ bởi " + customerName + " (Số thẻ: " + cardNumber + "): " + amount + " VND");
    }
}