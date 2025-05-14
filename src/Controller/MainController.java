package Controller;

import File.FileManager;
import Model.entities.*;
import Model.interfaces.Payment;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class MainController {
    private FileManager dao;
    private List<Meal> currentMenu;

    public MainController() {
        this.dao = new FileManager();
        this.currentMenu = dao.loadMenuFromFileBinary();
        if (this.currentMenu == null || this.currentMenu.isEmpty()) {
            System.out.println("Không tìm thấy file menu hoặc menu rỗng. Đang khởi tạo menu mặc định...");
            this.currentMenu = createAndReturnDefaultMenu();
            dao.saveMenuToFileBinary(this.currentMenu);
            System.out.println("Đã tạo và lưu menu mặc định vào file.");
        } else {
            System.out.println("Đã tải menu thành công từ file.");

        }
    }

    private List<Meal> createAndReturnDefaultMenu() {
        List<Meal> defaultMeals = new ArrayList<>();
        defaultMeals.add(new Dish("Gà nướng phô mai", "Gà nướng", 100000));
        defaultMeals.add(new Dish("Ba chỉ bò mỹ", "Bò nướng", 150000));
        defaultMeals.add(new Dish("Má heo sốt chua ngọt", "Heo nướng", 120000));
        defaultMeals.add(new Dish("Gà nướng muối ớt", "Gà nướng", 95000));
        defaultMeals.add(new Dish("Dẻ sườn bò sốt cay", "Bò nướng", 135000));
        defaultMeals.add(new Dish("Sườn cây nướng", "Heo nướng", 110000));
        return defaultMeals;
    }

    // --- QUẢN LÝ MENU ---

    public void addDish(Scanner scanner) {
        System.out.println("\n--- THÊM MÓN MỚI ---");
        try {
            System.out.print("Nhập tên món ăn: ");
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                System.out.println("Tên món không được để trống.");
                return;
            }

            for (Meal existingMeal : currentMenu) {
                if (existingMeal.getName().equalsIgnoreCase(name)) {
                    System.out.println("Món ăn với tên '" + name + "' đã tồn tại trong menu.");
                    return;
                }
            }

            System.out.print("Nhập loại món ăn (ví dụ: Món chính, Tráng miệng, Khai vị,...): ");
            String category = scanner.nextLine().trim();
            if (category.isEmpty()) {
                System.out.println("Loại món không được để trống.");
                return;
            }

            double price = -1;
            while (price < 0) {
                System.out.print("Nhập giá món ăn (số dương, ví dụ: 50000): ");
                String priceInput = scanner.nextLine().trim();
                try {
                    price = Double.parseDouble(priceInput);
                    if (price < 0) {
                        System.out.println("Giá phải là số dương. Vui lòng nhập lại.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Giá không hợp lệ. Vui lòng nhập số.");
                    price = -1;
                }
            }

            Dish newDish = new Dish(name, category, price);
            this.currentMenu.add(newDish);
            dao.saveMenuToFileBinary(this.currentMenu);
            System.out.println("Đã thêm món '" + name + "' vào menu thành công!");

        } catch (Exception e) {
            System.err.println("Đã xảy ra lỗi không mong muốn khi thêm món: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void viewDishesWithIndices() {
        if (this.currentMenu.isEmpty()) {
            System.out.println("Menu hiện tại rỗng.");
            return;
        }
        System.out.println("\n--- DANH SÁCH MÓN ĂN HIỆN TẠI ---");
        for (int i = 0; i < this.currentMenu.size(); i++) {
            Meal meal = this.currentMenu.get(i);
            String categoryStr = (meal instanceof Dish) ? ((Dish) meal).getCategory() : meal.getCategory();
            System.out.printf("%d. %s (%s) - %.0f VND\n", (i + 1), meal.getName(), categoryStr, meal.getPrice());
        }
        System.out.println("---------------------------------");
    }

    public void editDish(Scanner scanner) {
        System.out.println("\n--- SỬA MÓN ĂN ---");
        if (this.currentMenu.isEmpty()) {
            System.out.println("Menu rỗng, không có gì để sửa.");
            return;
        }
        viewDishesWithIndices();

        int choice = -1;
        while (choice == -1) {
            System.out.print("Chọn số thứ tự món cần sửa (nhập 0 để hủy): ");
            String choiceInput = scanner.nextLine().trim();
            try {
                choice = Integer.parseInt(choiceInput);
                if (choice == 0) {
                    System.out.println("Đã hủy thao tác sửa món.");
                    return;
                }
                if (choice < 1 || choice > this.currentMenu.size()) {
                    System.out.println("Lựa chọn không hợp lệ. Vui lòng chọn lại.");
                    choice = -1;
                }
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập một số hợp lệ.");
                choice = -1;
            }
        }

        Meal mealToEdit = this.currentMenu.get(choice - 1);
        if (!(mealToEdit instanceof Dish)) {
            System.out.println("Món được chọn không phải là 'Dish' và không thể sửa bằng chức năng này.");
            return;
        }
        Dish dishToEdit = (Dish) mealToEdit;

        System.out.println("Đang sửa món: " + dishToEdit.getName());
        System.out.println("Nhập thông tin mới (nhấn Enter để giữ nguyên thông tin cũ):");

        System.out.print("Tên món mới (" + dishToEdit.getName() + "): ");
        String newName = scanner.nextLine().trim();
        if (!newName.isEmpty()) {
            boolean nameExists = false;
            for (int i = 0; i < currentMenu.size(); i++) {
                if (i != (choice - 1) && currentMenu.get(i).getName().equalsIgnoreCase(newName)) {
                    nameExists = true;
                    break;
                }
            }
            if (nameExists) {
                System.out.println("Tên món '" + newName + "' đã tồn tại. Giữ nguyên tên cũ.");
            } else {
                dishToEdit.name = newName;
            }
        }

        System.out.print("Loại món mới (" + dishToEdit.getCategory() + "): ");
        String newCategory = scanner.nextLine().trim();
        if (!newCategory.isEmpty()) {
            dishToEdit.category = newCategory;
        }

        System.out.print("Giá mới (" + dishToEdit.getPrice() + "): ");
        String priceStr = scanner.nextLine().trim();
        if (!priceStr.isEmpty()) {
            try {
                double newPrice = Double.parseDouble(priceStr);
                if (newPrice >= 0) {
                    dishToEdit.price = newPrice;
                } else {
                    System.out.println("Giá mới phải là số không âm. Giữ nguyên giá cũ.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Giá nhập không phải là số. Giữ nguyên giá cũ.");
            }
        }
        dao.saveMenuToFileBinary(this.currentMenu);
        System.out.println("Đã cập nhật thông tin món '" + dishToEdit.getName() + "' thành công!");
    }

    public void deleteDish(Scanner scanner) {
        System.out.println("\n--- XOÁ MÓN ĂN ---");
        if (this.currentMenu.isEmpty()) {
            System.out.println("Menu rỗng, không có gì để xoá.");
            return;
        }
        viewDishesWithIndices();

        int choice = -1;
        while (choice == -1) {
            System.out.print("Chọn số thứ tự món cần xoá (nhập 0 để hủy): ");
            String choiceInput = scanner.nextLine().trim();
            try {
                choice = Integer.parseInt(choiceInput);
                if (choice == 0) {
                    System.out.println("Đã hủy thao tác xoá món.");
                    return;
                }
                if (choice < 1 || choice > this.currentMenu.size()) {
                    System.out.println("Lựa chọn không hợp lệ. Vui lòng chọn lại.");
                    choice = -1;
                }
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập một số hợp lệ.");
                choice = -1;
            }
        }

        Meal mealToRemove = this.currentMenu.get(choice - 1);
        System.out.print("Bạn có chắc chắn muốn xoá món '" + mealToRemove.getName() + "'? (y/n): ");
        String confirm = scanner.nextLine().trim().toLowerCase();

        if (confirm.equals("y") || confirm.equals("yes")) {
            this.currentMenu.remove(choice - 1);
            dao.saveMenuToFileBinary(this.currentMenu);
            System.out.println("Đã xoá món '" + mealToRemove.getName() + "' khỏi menu.");
        } else {
            System.out.println("Hủy thao tác xoá.");
        }
    }

    public void viewDishes() {
        if (this.currentMenu.isEmpty()) {
            System.out.println("\nMenu hiện tại rỗng.");
            return;
        }
        System.out.println("\n--- MENU HIỆN TẠI ---");
        for (Meal meal : this.currentMenu) {
            String categoryStr = (meal instanceof Dish) ? ((Dish) meal).getCategory() : meal.getCategory();
            System.out.printf("- %s (%s): %.0f VND\n", meal.getName(), categoryStr, meal.getPrice());
        }
        System.out.println("--------------------");
    }

    // --- QUẢN LÝ LỊCH SỬ ĐƠN HÀNG ---

    public void viewOrderHistory(Scanner scanner) {
        List<Order> orderHistory = dao.loadOrderHistoryFromFile();

        if (orderHistory.isEmpty()) {
            System.out.println("Không có lịch sử đơn hàng nào được ghi nhận.");
            return;
        }

        System.out.println("\n================ LỊCH SỬ ĐƠN HÀNG ================");
        for (int i = 0; i < orderHistory.size(); i++) {

            Order order = orderHistory.get(i);
            System.out.println("\n--- Đơn hàng #" + (i + 1) + " (ID: " + order.getOrderId() + ") ---");
            System.out.println("  Thời gian: " + order.getTimestamp());

            if (order.getCustomerIdentifier() != null && !order.getCustomerIdentifier().isEmpty()) {
                System.out.println("  Khách hàng: " + order.getCustomerIdentifier());
            }

            System.out.print("  Loại đơn: " + order.getClass().getSimpleName());
            if (order instanceof ChoiceRooms) {
                System.out.println(" (Phòng: " + ((ChoiceRooms) order).getRoomType() + ")");
            } else {
                System.out.println();
            }

            System.out.println("  Các món ăn:");
            List<Meal> mealsInOrder = order.getMealList();
            if (mealsInOrder.isEmpty()) {
                System.out.println("    (Không có món nào)");
            } else {
                for (Meal meal : mealsInOrder) {
                    String mealCategory = (meal instanceof Dish) ? ((Dish) meal).getCategory() : meal.getCategory();
                    System.out.println("    - " + meal.getName() + " (" + mealCategory + "): " + meal.getPrice() + " VND");
                }
            }

            System.out.println("  Tổng tiền: " + order.getTotal() + " VND");

            Payment paymentDetails = order.getPaymentDetails();
            if (paymentDetails != null) {
                System.out.print("  Hình thức thanh toán: ");
                if (paymentDetails instanceof CardPayment) {
                    CardPayment cp = (CardPayment) paymentDetails; // Dòng 278
                    System.out.println("Thẻ - Khách: " + cp.getCustomerName() + ", Số thẻ: " + cp.getCardNumber());
                } else if (paymentDetails instanceof CashPayment) {
                    CashPayment cp = (CashPayment) paymentDetails;
                    System.out.println("Tiền mặt - Khách: " + cp.getCustomerName());
                } else {
                    System.out.println("Chưa xác định (" + paymentDetails.getClass().getSimpleName() + ")");
                }
            }
            System.out.println("--------------------------------------------------");
        }
        System.out.println("==================================================");

        while (true) {
            System.out.print("\nBạn có muốn xuất hóa đơn chi tiết (file text) cho một đơn hàng không? \nNhập ID đơn hàng để xuất, hoặc 'k' để không/thoát: ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("k") || input.equalsIgnoreCase("không") || input.equalsIgnoreCase("thoat")) {
                break;
            }

            String orderIdToExport = input;
            Optional<Order> selectedOrderOpt = orderHistory.stream()
                    .filter(o -> o.getOrderId().equals(orderIdToExport))
                    .findFirst();

            if (selectedOrderOpt.isPresent()) {
                dao.exportBillToTextFile(selectedOrderOpt.get());
            } else {
                System.out.println("Không tìm thấy đơn hàng với ID: '" + orderIdToExport + "'. Vui lòng kiểm tra lại ID.");
            }
        }
    }
}