package view; // This class is in the 'view' package

import Controller.MainController;
import File.FileManager;
import Model.entities.Meal;

import java.util.List;
import java.util.Scanner;
import java.util.InputMismatchException;

public class AdminPanel {
    private Scanner scanner;
    private FileManager fileManager;
    private List<Meal> menu; // Reference to the shared menu list
    private MainController adminController;

    public AdminPanel(Scanner scanner, FileManager fileManager, List<Meal> menu, MainController adminController) {
        this.scanner = scanner;
        this.fileManager = fileManager;
        this.menu = menu;
        this.adminController = adminController;
    }

    public void startAdminSession() {
        System.out.print("Nhập mật khẩu admin: ");
        String password = scanner.nextLine();
        if (!password.equals("admin123")) {
            System.out.println("Sai mật khẩu.");
            return;
        }

        boolean running = true;
        while (running) {
            System.out.println("\n--- QUẢN LÝ MÓN ĂN ---");
            System.out.println("1. Thêm món");
            System.out.println("2. Sửa món");
            System.out.println("3. Xóa món");
            System.out.println("4. Xem danh sách món");
            System.out.println("0. Thoát");
            System.out.print("Chọn: ");

            int adminChoice = -1;
            try {
                if (scanner.hasNextInt()) {
                    adminChoice = scanner.nextInt();
                } else {
                    System.out.println("Lựa chọn không hợp lệ. Vui lòng nhập số.");
                    scanner.nextLine(); // Consume invalid input
                    continue;
                }
            } catch (InputMismatchException e) {
                System.out.println("Lựa chọn không hợp lệ. Vui lòng nhập số.");
                scanner.nextLine(); // Consume invalid input
                continue;
            }
            scanner.nextLine(); // Consume newline

            switch (adminChoice) {
                case 1:
                    // Assuming MainController.addDish updates the menu data source
                    // or modifies the 'menu' list if it has a reference to it.
                    adminController.addDish(scanner);
                    break;
                case 2:
                    adminController.editDish(scanner);
                    break;
                case 3:
                    adminController.deleteDish(scanner);
                    break;
                case 4:
                    adminController.viewDishes(); // Should display the current state of the menu
                    break;
                case 0:
                    // This saves the 'menu' list. It's crucial that MainController's
                    // operations have effectively updated this list or the underlying
                    // file that this list might be reloaded from before saving.
                    // Sticking to original logic: save the menu list instance.
                    fileManager.saveMenuToFile(this.menu);
                    System.out.println("Đã lưu và thoát.");
                    running = false;
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ.");
                    break;
            }
        }
    }
}