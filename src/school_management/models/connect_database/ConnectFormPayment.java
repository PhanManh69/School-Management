package school_management.models.connect_database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import school_management.models.user_controller.UserController;
import school_management.views.dialog.Message;

public class ConnectFormPayment {
    public static List<String[]> displayInformation() {
        Connection conn = ConnectMySQL.conn();
        List<String[]> tuition = new ArrayList<>();
        
        String studentCode = UserController.getUserName();

        if (conn != null) {
            try {
                String sql = "SELECT oc.semester, "
                        + "SUM((SELECT sub.number_credits WHERE sub.id = rc.subject_id) * m.tuition) AS total_cost "
                        + "FROM register_course rc "
                        + "JOIN students s ON rc.student_id = s.id "
                        + "JOIN open_courses oc ON rc.subject_id = oc.subject_id AND rc.class_id = oc.class_id "
                        + "JOIN majors m ON oc.majors_id = m.id "
                        + "JOIN subject sub ON rc.subject_id = sub.id "
                        + "WHERE s.student_code = ? "
                        + "GROUP BY oc.semester "
                        + "HAVING NOT EXISTS "
                        + "( SELECT t.semester FROM tuition t "
                        + "JOIN students s2 ON t.student_id = s2.id "
                        + "WHERE s2.student_code = ? AND "
                        + "CONVERT(t.semester USING utf8mb4) = CONVERT(oc.semester USING utf8mb4))";

                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, studentCode);
                    pstmt.setString(2, studentCode);
                    ResultSet rs = pstmt.executeQuery();
                    while (rs.next()) {
                        String semester = rs.getString("oc.semester");
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
    
    private static void handleSQLException(SQLException e) {
        Message messageDialog = new Message(null, true);
        messageDialog.showMessage("Lỗi SQL: " + e.getMessage());
    }
}
