package Model.entities;

import Model.interfaces.Payment;
import java.io.Serializable;
public class CashPayment implements Payment,Serializable {
    private String customerName;
    private static final long serialVersionUID = 1L;

    public CashPayment(String customerName) {

        this.customerName = customerName;
    }
    public String getCustomerName() {
        return customerName;
    }
    @Override
    public void pay(double amount) {
        System.out.println("Thanh toán tiền mặt bởi " + customerName + ": " + amount + " VND");
    }
}