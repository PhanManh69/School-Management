package school_management.models.connect_database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import school_management.views.dialog.Message;

public class ConnectFormLearn {

    public static List<String> getClassNames() {
        Connection conn = ConnectMySQL.conn();

        if (conn != null) {
            try {
                String sql = "SELECT class_name FROM class";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    ResultSet rs = pstmt.executeQuery();
                    List<String> classRoom = new ArrayList<>();
                    while (rs.next()) {
                        String className = rs.getString("class_name");
                        classRoom.add(className);
                    }
                    return classRoom;
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

    public static boolean addSchedule(String className, String dateApply, String content) {
        Connection conn = ConnectMySQL.conn();

        if (conn != null) {
            try {
                String sql = "INSERT INTO schedule (class_id, date_apply, content) "
                        + "SELECT c.id, ?, ? "
                        + "FROM class c "
                        + "WHERE c.class_name = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, dateApply);
                    pstmt.setString(2, content);
                    pstmt.setString(3, className);
                    int rowsAffected = pstmt.executeUpdate();
                    System.out.println("Data has been inserted successfully!");
                    Message messageDialog = new Message(null, true);
                    messageDialog.showMessage("Lưu thông tin thành công!");
                    return rowsAffected > 0;
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
        return false;
    }

    public static boolean updateSchedule(String idSchedule, String className, String dateApply, String content) {
        Connection conn = ConnectMySQL.conn();

        if (conn != null) {
            try {
                String sql = "UPDATE schedule s "
                        + "INNER JOIN class c ON s.class_id = c.id "
                        + "SET s.class_id = (SELECT id FROM class WHERE class_name = ?), "
                        + "s.date_apply = ?, s.content = ? "
                        + "WHERE s.id = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, className);
                    pstmt.setString(2, dateApply);
                    pstmt.setString(3, content);
                    pstmt.setString(4, idSchedule);
                    int rowsAffected = pstmt.executeUpdate();

                    System.out.println("Data has been updated successfully!");
                    Message messageDialog = new Message(null, true);
                    messageDialog.showMessage("Cập nhật thông tin thành công!");

                    return rowsAffected > 0;
                }
            } catch (SQLException e) {
                handleSQLException(e);
                System.out.println(e);
                return false;
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
            return false;
        }
    }

    public static boolean deleteSchedule(String idSchedule) {
        Connection conn = ConnectMySQL.conn();

        if (conn != null) {
            try {
                String sql = "DELETE FROM schedule WHERE id = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, idSchedule);
                    int rowsAffected = pstmt.executeUpdate();

                    if (rowsAffected > 0) {
                        System.out.println("Data has been deleted successfully!");
                        Message messageDialog = new Message(null, true);
                        messageDialog.showMessage("Xóa thông tin thành công!");
                    } else {
                        Message messageDialog = new Message(null, true);
                        messageDialog.showMessage("Không thể xóa dữ liệu khỏi bảng!");
                    }

                    return rowsAffected > 0;
                }
            } catch (SQLException e) {
                handleSQLException(e);
                System.out.println(e);
                return false;
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
            return false;
        }
    }

    public static List<String[]> displayInformation() {
        Connection conn = ConnectMySQL.conn();
        List<String[]> scheduleInfo = new ArrayList<>();

        if (conn != null) {
            try {
                String sql = "SELECT * FROM schedule s "
                        + "INNER JOIN class c ON s.class_id = c.id";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    ResultSet rs = pstmt.executeQuery();
                    while (rs.next()) {
                        String idSchedule = rs.getString("s.id");
                        String className = rs.getString("c.class_name");
                        String dateApply = rs.getString("s.date_apply");
                        String content = rs.getString("s.content");
                        String[] info = {idSchedule, className, dateApply, content};
                        scheduleInfo.add(info);
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
        return scheduleInfo;
    }

    public static List<String[]> searchByClassNameAndDateRange(String className, String startDate, String endDate) {
        Connection conn = ConnectMySQL.conn();
        List<String[]> scheduleInfo = new ArrayList<>();

        if (conn != null) {
            try {
                String sql = "SELECT * FROM schedule s "
                        + "INNER JOIN class c ON s.class_id = c.id "
                        + "WHERE c.class_name LIKE ? "
                        + "AND STR_TO_DATE(SUBSTRING_INDEX(s.date_apply, ' - ', 1), '%Y-%m-%d') BETWEEN STR_TO_DATE(?, '%Y-%m-%d') AND STR_TO_DATE(?, '%Y-%m-%d') "
                        + "AND STR_TO_DATE(SUBSTRING_INDEX(s.date_apply, ' - ', -1), '%Y-%m-%d') BETWEEN STR_TO_DATE(?, '%Y-%m-%d') AND STR_TO_DATE(?, '%Y-%m-%d')";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, "%" + className + "%");
                    pstmt.setString(2, startDate);
                    pstmt.setString(3, endDate);
                    pstmt.setString(4, startDate);
                    pstmt.setString(5, endDate);
                    ResultSet rs = pstmt.executeQuery();
                    while (rs.next()) {
                        String idSchedule = rs.getString("s.id");
                        String classNameResult = rs.getString("c.class_name");
                        String dateApply = rs.getString("s.date_apply");
                        String content = rs.getString("s.content");
                        String[] info = {idSchedule, classNameResult, dateApply, content};
                        scheduleInfo.add(info);
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
        return scheduleInfo;
    }

    private static void handleSQLException(SQLException e) {
        Message messageDialog = new Message(null, true);
        messageDialog.showMessage("Lỗi SQL: " + e.getMessage());
    }
}
