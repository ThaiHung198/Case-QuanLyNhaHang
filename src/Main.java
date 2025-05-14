import Controller.MainController;
import File.FileManager;
import Model.entities.Dish;
import Model.entities.Meal;
import view.AdminPanel;
import view.UserPanel;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.InputMismatchException;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        FileManager fileManager = new FileManager();
        // MainController might need FileManager and/or the menu list.
        // For this refactoring, we'll initialize it here and pass to AdminPanel.
        MainController adminController = new MainController();

        // Đọc menu từ file
        List<Meal> menu = fileManager.loadMenuFromFile();

        // Nếu chưa có món, khởi tạo mặc định và lưu
        if (menu.isEmpty()) {
            menu = Arrays.asList(
                    new Dish("Gà nướng phô mai", "Gà nướng", 100000),
                    new Dish("Ba chỉ bò mỹ", "Bò nướng", 150000),
                    new Dish("Má heo sốt chua ngọt", "Heo nướng", 120000),
                    new Dish("Gà nướng muối ớt", "Gà nướng", 95000),
                    new Dish("Dẻ sườn bò sốt cay", "Bò nướng", 135000),
                    new Dish("Sườn cây nướng", "Heo nướng", 110000)
            );
            fileManager.saveMenuToFile(menu); // Save initial default menu
        }

        // Chọn vai trò
        System.out.println("Bạn là: 1 - ADMIN, 2 - KHÁCH HÀNG");
        int role = 0; // Default to an invalid role
        try {
            if (scanner.hasNextInt()) {
                role = scanner.nextInt();
            } else {
                System.out.println("Lựa chọn không hợp lệ. Vui lòng nhập số.");
                scanner.nextLine(); // Consume invalid input
                scanner.close();
                return;
            }
        } catch (InputMismatchException e) {
            System.out.println("Lựa chọn không hợp lệ. Vui lòng nhập số.");
            scanner.nextLine(); // Consume invalid input
            scanner.close();
            return;
        }
        scanner.nextLine(); // Consume the rest of the line after nextInt()

        if (role == 1) {
            AdminPanel adminPanel = new AdminPanel(scanner, fileManager, menu, adminController);
            adminPanel.startAdminSession();
        } else if (role == 2) {
            UserPanel userPanel = new UserPanel(scanner, fileManager, menu);
            userPanel.startUserSession();
        } else {
            System.out.println("Lựa chọn vai trò không hợp lệ.");
        }

        scanner.close();
        System.out.println("Chương trình kết thúc.");
    }
}