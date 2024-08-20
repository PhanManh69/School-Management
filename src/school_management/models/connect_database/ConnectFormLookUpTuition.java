package school_management.models.connect_database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import school_management.models.user_controller.UserController;
import school_management.views.dialog.Message;

public class ConnectFormLookUpTuition {
    public static List<String[]> displayInformation() {
        Connection conn = ConnectMySQL.conn();
        List<String[]> tuition = new ArrayList<>();
        
        String studentCode = UserController.getUserName();

        if (conn != null) {
            try {
                String sql = "SELECT oc.semester, SUM((SELECT sub.number_credits WHERE sub.id = rc.subject_id) * m.tuition) AS total_cost "
                        + "FROM register_course rc "
                        + "JOIN students s ON rc.student_id = s.id "
                        + "JOIN open_courses oc ON rc.subject_id = oc.subject_id AND rc.class_id = oc.class_id "
                        + "JOIN majors m ON oc.majors_id = m.id "
                        + "JOIN subject sub ON rc.subject_id = sub.id "
                        + "WHERE s.student_code = ? "
                        + "GROUP BY oc.semester;";

                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, studentCode);
                    ResultSet rs = pstmt.executeQuery();
                    while (rs.next()) {
                        String semester = rs.getString("semester");
                        String totalCost = rs.getString("total_cost");
                        String[] info = {semester, totalCost};
                        tuition.add(info);
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
        return tuition;
    }
    
    public static List<String[]> displayInformationPay() {
        Connection conn = ConnectMySQL.conn();
        List<String[]> tuition = new ArrayList<>();
        
        String studentCode = UserController.getUserName();

        if (conn != null) {
            try {
                String sql = "SELECT * FROM tuition t "
                        + "JOIN students s ON t.student_id = s.id "
                        + "WHERE s.student_code = ?";

                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, studentCode);
                    ResultSet rs = pstmt.executeQuery();
                    while (rs.next()) {
                        String money = rs.getString("money");
                        String date = rs.getString("created_at");
                        String semester = rs.getString("semester");
                        String[] info = {semester, date, money};
                        tuition.add(info);
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
        return tuition;
    }
    
    public static String displayTotalMoney() {
        Connection conn = ConnectMySQL.conn();
        String totalMoney = null;

        String studentCode = UserController.getUserName();
        
        if (conn != null) {
            try {
                String sql = "SELECT SUM(money) AS total_money "
                        + "FROM tuition t "
                        + "JOIN students s ON t.student_id = s.id "
                        + "WHERE s.student_code = ?;";

                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, studentCode);
                    ResultSet rs = pstmt.executeQuery();
                    while (rs.next()) {
                        totalMoney = rs.getString("total_money");
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
        return totalMoney;
    }

    private static void handleSQLException(SQLException e) {
        Message messageDialog = new Message(null, true);
        messageDialog.showMessage("Lỗi SQL: " + e.getMessage());
    }
}
