package view;

import File.FileManager;
import Model.entities.*;
import Model.interfaces.Payment;
import java.util.List;
import java.util.Scanner;
import java.util.InputMismatchException;

public class UserPanel {
    private Scanner scanner;
    private FileManager fileManager;
    private List<Meal> menu;

    public UserPanel(Scanner scanner, FileManager fileManager, List<Meal> menu) {
        this.scanner = scanner;
        this.fileManager = fileManager;
        this.menu = menu;
    }

    public void startUserSession() {
        System.out.println("Chọn hình thức (1 - Mang về, 2 - Ăn tại nhà hàng):");
        int choice = -1;
        try {
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
            } else {
                System.out.println("Lựa chọn không hợp lệ. Vui lòng nhập số.");
                scanner.nextLine(); // Consume invalid input
                return;
            }
        } catch (InputMismatchException e) {
            System.out.println("Lựa chọn không hợp lệ. Vui lòng nhập số.");
            scanner.nextLine(); // Consume invalid input
            return;
        }
        scanner.nextLine(); // Consume newline

        Order order;
        if (choice == 1) {
            order = new TakeAway();
        } else if (choice == 2) {
            System.out.println("Chọn loại phòng (1 - STANDARD, 2 - VIP, 3 - DOUBLE):");
            int roomChoice = -1;
            try {
                if (scanner.hasNextInt()) {
                    roomChoice = scanner.nextInt();
                } else {
                    System.out.println("Lựa chọn không hợp lệ. Vui lòng nhập số.");
                    scanner.nextLine(); // Consume invalid input
                    return;
                }
            } catch (InputMismatchException e) {
                System.out.println("Lựa chọn không hợp lệ. Vui lòng nhập số.");
                scanner.nextLine(); // Consume invalid input
                return;
            }
            scanner.nextLine(); // Consume newline

            if (roomChoice < 1 || roomChoice > RoomType.values().length) {
                System.out.println("Loại phòng không hợp lệ.");
                return;
            }
            RoomType roomType = RoomType.values()[roomChoice - 1];
            order = new ChoiceRooms(roomType);
        } else {
            System.out.println("Lựa chọn hình thức không hợp lệ.");
            return;
        }

        // In menu
        System.out.println("\n--- MENU ---");
        if (menu == null || menu.isEmpty()) {
            System.out.println("Xin lỗi, menu hiện tại trống.");
            return;
        }
        for (int i = 0; i < menu.size(); i++) {
            Meal m = menu.get(i);
            if (m != null) { // Basic null check for safety
                System.out.println((i + 1) + ". " + m.getCategory() + " - " + m.getName() + ": " + String.format("%,.0f", m.getPrice()) + " VND");
            }
        }

        // Chọn món
        while (true) {
            System.out.print("Nhập số thứ tự món muốn chọn (0 để kết thúc): ");
            int idxOrderChoice = -1;
            try {
                if (scanner.hasNextInt()) {
                    idxOrderChoice = scanner.nextInt();
                } else {
                    System.out.println("Vui lòng nhập số thứ tự hợp lệ.");
                    scanner.nextLine(); // Consume invalid input
                    continue;
                }
            } catch (InputMismatchException e) {
                System.out.println("Vui lòng nhập số thứ tự hợp lệ.");
                scanner.nextLine(); // Consume invalid input
                continue;
            }
            scanner.nextLine(); // Consume newline

            if (idxOrderChoice == 0) break;
            if (idxOrderChoice > 0 && idxOrderChoice <= menu.size()) {
                order.addMeal(menu.get(idxOrderChoice - 1));
                System.out.println("Đã thêm: " + menu.get(idxOrderChoice - 1).getName());
            } else {
                System.out.println("Lựa chọn không hợp lệ. Vui lòng chọn lại.");
            }
        }

        if (order.getMealList().isEmpty()) {
            System.out.println("Đơn hàng của bạn trống. Kết thúc phiên.");
            return;
        }

        // In hóa đơn
        System.out.println("\n--- ĐƠN HÀNG ---");
        order.printOrder();
        double total = order.getTotal();

        System.out.println("\nChọn phương thức thanh toán (1 - Tiền mặt, 2 - Chuyển khoản):");
        int payMethod = -1;
        try {
            if (scanner.hasNextInt()) {
                payMethod = scanner.nextInt();
            } else {
                System.out.println("Lựa chọn không hợp lệ. Vui lòng nhập số.");
                scanner.nextLine(); // Consume invalid input
                return;
            }
        } catch (InputMismatchException e) {
            System.out.println("Lựa chọn không hợp lệ. Vui lòng nhập số.");
            scanner.nextLine(); // Consume invalid input
            return;
        }
        scanner.nextLine(); // Consume newline
        Payment payment;

        if (payMethod == 1) {
            System.out.print("Nhập họ tên: ");
            String name = scanner.nextLine();
            payment = new CashPayment(name);
        } else if (payMethod == 2) {
            System.out.print("Nhập họ tên: ");
            String name = scanner.nextLine();
            System.out.print("Nhập số thẻ: ");
            String card = scanner.nextLine();
            payment = new CardPayment(name, card);
        } else {
            System.out.println("Phương thức thanh toán không hợp lệ.");
            return;
        }

        System.out.println();
        payment.pay(total);
        System.out.println("Thanh toán thành công. Tổng tiền: " + String.format("%,.0f", total) + " VND");

        // Lưu hóa đơn
        fileManager.exportBillToTextFile(order);
        System.out.println("Hóa đơn đã được xuất ra file text.");
    }
}