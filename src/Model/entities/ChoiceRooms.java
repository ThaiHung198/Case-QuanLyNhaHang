package Model.entities;
import java.io.Serializable;

public class ChoiceRooms extends Order implements Serializable {
    private static final long serialVersionUID = 1L;
    private RoomType roomType;

    public ChoiceRooms(RoomType roomType) {

        this.roomType = roomType;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    @Override
    public void printOrder() {
        System.out.println("Đơn hàng ăn tại nhà hàng (Phòng: " + roomType + "):");
        meals.forEach(meal ->
                System.out.println("- " + meal.getName() + ": " + meal.getPrice() + " VND"));
    }
}