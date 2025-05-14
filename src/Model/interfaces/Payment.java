package Model.interfaces;
import java.io.Serializable;

public interface Payment extends Serializable {
    void pay(double amount);
}
