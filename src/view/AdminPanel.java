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
                scanner.nextLine();
                continue;
            }
            scanner.nextLine();

            switch (adminChoice) {
                case 1:

                    adminController.addDish(scanner);
                    break;
                case 2:
                    adminController.editDish(scanner);
                    break;
                case 3:
                    adminController.deleteDish(scanner);
                    break;
                case 4:
                    adminController.viewDishes();
                    break;
                case 0:
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