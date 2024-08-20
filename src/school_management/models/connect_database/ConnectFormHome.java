package school_management.models.connect_database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import static school_management.models.connect_database.ConnectMySQL.conn;
import school_management.views.dialog.Message;

public class ConnectFormHome {

    public static boolean addNotification(String date, String title, String content) {
        String sql = "INSERT INTO notification (posting_date, title, content) VALUES (?, ?, ?)";

        try (Connection conn = conn(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, date);
            pstmt.setString(2, title);
            pstmt.setString(3, content);

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Data has been added to the table successfully!");
                Message messageDialog = new Message(null, true);
                messageDialog.showMessage("Thêm thông tin thành công!");
                return true;
            } else {
                Message messageDialog = new Message(null, true);
                messageDialog.showMessage("Không thể lưu dữ liệu vào bảng!");
            }
        } catch (SQLException e) {
            Message messageDialog = new Message(null, true);
            messageDialog.showMessage(e.getMessage());
            System.out.println(e.getMessage());
            return false;
        }
        return false;
    }

    public static boolean updateNotification(String date, String title, String content) {
        String sql = "UPDATE notification SET title = ?, content = ? WHERE posting_date = ?";

        try (Connection conn = conn(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setString(2, content);
            pstmt.setString(3, date);

            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Data has been updated successfully!");
                Message messageDialog = new Message(null, true);
                messageDialog.showMessage("Cập nhật thông tin thành công!");
                return true;
            } else {
                Message messageDialog = new Message(null, true);
                messageDialog.showMessage("Không thể cập nhật dữ liệu trong bảng!");
            }
        } catch (SQLException e) {
            Message messageDialog = new Message(null, true);
            messageDialog.showMessage(e.getMessage());
            System.out.println(e.getMessage());
            return false;
        }
        return false;
    }

    public static boolean deleteNotification(String dateNotification) {
        String sql = "DELETE FROM notification WHERE posting_date = ?";

        try (Connection conn = conn(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, dateNotification);

            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Data has been deleted from the table successfully!");
                Message messageDialog = new Message(null, true);
                messageDialog.showMessage("Xóa thông tin thành công!");
                return true;
            } else {
                Message messageDialog = new Message(null, true);
                messageDialog.showMessage("Không thể xóa dữ liệu trong bảng!");
            }
        } catch (SQLException e) {
            Message messageDialog = new Message(null, true);
            messageDialog.showMessage(e.getMessage());
            System.out.println(e.getMessage());
            return false;
        }
        return false;
    }

    public static String[][] displayInformation() {
        Connection conn = conn();

        if (conn != null) {
            try {
                String sql = "SELECT posting_date, title FROM notification ORDER BY posting_date ASC";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    ResultSet rs = pstmt.executeQuery();
                    List<String[]> dateTitleList = new ArrayList<>();
                    while (rs.next()) {
                        String date = rs.getString("posting_date");
                        String title = rs.getString("title");
                        dateTitleList.add(new String[]{date, title});
                    }
                    return dateTitleList.toArray(String[][]::new);
                }
            } catch (SQLException e) {
                handleSQLException(e);
                System.out.println(e);

            } finally {
                try {
                    conn.close();
                } catch (SQLException e) {
                    handleSQLException(e);
                    System.out.println(e);
                }
            }
        } else {
            System.out.println("Unable to connect to database.");
        }
        return null;
    }

    public static String[] getInfoInNotification(String dateNotification) {
        Connection conn = conn();

        if (conn != null) {
            try {
                String sql = "SELECT title, content FROM notification WHERE posting_date = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, dateNotification);
                    ResultSet rs = pstmt.executeQuery();
                    if (rs.next()) {
                        String title = rs.getString("title");
                        String content = rs.getString("content");
                        return new String[]{title, content};
                    } else {
                        Message messageDialog = new Message(null, true);
                        messageDialog.showMessage("Không tìm thấy thông tin notification!");
                    }
                }
            } catch (SQLException e) {
                handleSQLException(e);
                System.out.println(e);
            } finally {
                try {
                    conn.close();
                } catch (SQLException e) {
                    handleSQLException(e);
                    System.out.println(e);
                }
            }
        } else {
            System.out.println("Unable to connect to database.");
        }
        return null;
    }

    private static void handleSQLException(SQLException e) {
        Message messageDialog = new Message(null, true);
        messageDialog.showMessage("Lỗi SQL: " + e.getMessage());
    }
}
