package File;

import Model.entities.Meal;
import Model.entities.Order;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileManager implements Serializable {

    private static final String MENU_FILE_PATH_BINARY = "./data/menu.bin";
    private static final String ORDER_HISTORY_FILE_PATH_BINARY = "./data/order_history.bin";
    private static final String Bill_EXPORT_TEXT_PATH_PREFIX = "./data/bill_export_";

    static {
        File dataDir = new File("./data");
        if (!dataDir.exists()) {
            boolean created = dataDir.mkdirs();
            if (created) {
                System.out.println("Thư mục './data' đã được tạo");
            } else {
                System.out.println("Không thể tạo thư mục './data'. Vui lòng kiểm tra quyền ghi");
            }
        }
    }

    public void saveMenuToFile(List<Meal> menu) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(MENU_FILE_PATH_BINARY))) {
            oos.writeObject(menu);
            System.out.println("Menu (nhị phân) đã được lưu vào: " + MENU_FILE_PATH_BINARY);
        } catch (IOException e) {
            System.out.println("Lỗi khi ghi file menu nhị phân: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public List<Meal> loadMenuFromFile() {
        List<Meal> menu = new ArrayList<>();
        File file = new File(MENU_FILE_PATH_BINARY);
        if (!file.exists()) {
            System.out.println("File menu nhị phân '" + MENU_FILE_PATH_BINARY + "' không tồn tại. Trả về menu rỗng.");
            return menu;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Object readObject = ois.readObject();
            if (readObject instanceof List) {
                menu = (List<Meal>) readObject;
                System.out.println("Menu (nhị phân) đã được tải từ : " + MENU_FILE_PATH_BINARY);
            } else {
                System.out.println("Dữ liệu trong file menu nhị phân không phải là 1 list.");
            }
        } catch (IOException e) {
            System.err.println("Lỗi khi đọc file menu nhị phân: " + e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("Lỗi: Không tìm thấy lớp (class not found) khi đọc file menu nhị phân. " + "Điều này có thể xảy ra nếu cấu trúc class đã thay đổi hoặc class không có trong classpath: " + e.getMessage());
            e.printStackTrace();
        }
        return menu;
    }

    @SuppressWarnings("unchecked")
    public List<Order> loadOrderHistoryFromFile() {
        List<Order> orderHistory = new ArrayList<>();
        File file = new File(ORDER_HISTORY_FILE_PATH_BINARY);
        if (!file.exists()) {
            System.out.println("File lịch sử đơn hàng '" + ORDER_HISTORY_FILE_PATH_BINARY + "' không tồn tại. Trả về danh sách rỗng.");
            return orderHistory;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Object readObject = ois.readObject();
            if (readObject instanceof List) {
                orderHistory = (List<Order>) readObject;
                System.out.println("Lịch sử đơn hàng đã được tải từ: " + ORDER_HISTORY_FILE_PATH_BINARY);
            } else {
                System.out.println("Dữ liệu trong file lịch sử đơn hàng không phải là một List.");
            }
        } catch (EOFException e) {
            System.err.println("File lịch sử đơn hàng rỗng hoặc chưa được khởi tạo: " + ORDER_HISTORY_FILE_PATH_BINARY);
        } catch (IOException e) {
            System.err.println("Lỗi khi đọc file lịch sử đơn hàng: " + e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("Lỗi: Không tìm thấy lớp khi đọc file lịch sử đơn hàng: " + e.getMessage());
            e.printStackTrace();
        }
        return orderHistory;
    }

    public void addOrderToHistoryAndSave(Order newOrder) {
        if (newOrder == null) {
            System.out.println("Không thể thêm order null vào lịch sử.");
            return;
        }
        List<Order> orderHistory = loadOrderHistoryFromFile();
        orderHistory.add(newOrder);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ORDER_HISTORY_FILE_PATH_BINARY))) {
            oos.writeObject(orderHistory);

            
            System.out.println("Order ID: " + newOrder.getOrderId() + " đã được thêm vào lịch sử và lưu vào: " + ORDER_HISTORY_FILE_PATH_BINARY);
        } catch (IOException e) {
            System.err.println("Lỗi khi ghi file lịch sử đơn hàng: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void exportBillToTextFile(Order order) {
        if (order == null) {
            System.err.println("Không thể xuất hóa đơn: đối tượng Order là null.");
            return;
        }
        String fileName = Bill_EXPORT_TEXT_PATH_PREFIX + order.getOrderId() + "_" + order.getTimestamp().getTime() + ".txt";

        try (PrintWriter pw = new PrintWriter(new FileWriter(fileName))) {
            pw.println("===HÓA ĐƠN(TEXT EXPORT)===");
            pw.println("Order ID: " + order.getOrderId());
            pw.println("Thời gian: " + order.getTimestamp());
            if (order.getCustomerIdentifier() != null && !order.getCustomerIdentifier().isEmpty()) {
                pw.println("Khách hàng: " + order.getCustomerIdentifier());
            }
            pw.println("----------------------------------");
            List<Meal> mealsInOrder = order.getMealList();
            if (mealsInOrder != null && !mealsInOrder.isEmpty()) {
                for (Meal meal : mealsInOrder) {
                    if (meal != null) {
                        pw.println("- " + meal.getName() + " (" + meal.getCategory() + meal.getPrice() + " VND)");
                    }
                }
            } else {
                pw.println("(Không có món ăn nào trong đơn hàng)");
            }
            pw.println("----------------------------------");
            pw.println("Tổng cộng: " + order.getTotal() + " VND");
            if (order.getPaymentDetails() != null) {
                pw.println("Hình thức thanh toán: Chi tiết trong đối tượng thanh toán");
            }
            pw.println("===================================");
            System.out.println("Hóa đơn đã được xuất ra file text: " + fileName);
        } catch (IOException e) {
            System.err.println("Lỗi khi ghi hóa đơn text (" + fileName + "): " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void saveMenuToFileBinary(List<Meal> menu) {

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("./data/menu.bin"))) { // Đảm bảo đường dẫn đúng
            oos.writeObject(menu);
            System.out.println("Menu (nhị phân) đã được lưu vào: ./data/menu.bin");
        } catch (IOException e) {
            System.err.println("Lỗi khi ghi file menu nhị phân: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<Meal> loadMenuFromFileBinary() {
        List<Meal> menuFromFile = new ArrayList<>();
        File file = new File("./data/menu.bin");

        if (!file.exists()) {
            System.out.println("File menu nhị phân '" + file.getAbsolutePath() + "' không tồn tại. Trả về menu rỗng.");
            return menuFromFile;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Object readObject = ois.readObject();
            if (readObject instanceof List) {
                List<?> rawList = (List<?>) readObject;
                for (Object item : rawList) {
                    if (item instanceof Meal) {
                        menuFromFile.add((Meal) item);
                    } else {
                        System.err.println("Cảnh báo: Đối tượng không phải Meal được tìm thấy trong file menu: " + item);
                    }
                }
                System.out.println("Menu (nhị phân) đã được tải từ: " + file.getAbsolutePath() + " với " + menuFromFile.size() + " món.");
            } else if (readObject != null) {
                System.err.println("Dữ liệu trong file menu nhị phân không phải là một List. Kiểu dữ liệu: " + readObject.getClass().getName());
            } else {
                System.out.println("File menu nhị phân không chứa đối tượng List nào (có thể rỗng hoặc lỗi ghi trước đó).");
            }
        } catch (EOFException e) {
            System.out.println("File menu nhị phân rỗng hoặc đã đọc hết: " + file.getAbsolutePath());

        } catch (IOException e) {
            System.err.println("Lỗi IO khi đọc file menu nhị phân: " + e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("Lỗi: Không tìm thấy lớp khi đọc file menu nhị phân: " + e.getMessage());
            e.printStackTrace();
        } catch (ClassCastException e) {
            System.err.println("Lỗi ép kiểu khi đọc dữ liệu từ file menu: " + e.getMessage());
            e.printStackTrace();
        }
        return menuFromFile;
    }
}
